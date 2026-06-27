use winit::{
    window::Window,
};
use std::sync::Arc;
use wgpu::util::DeviceExt;

use crate::mesh::{Vertex, Mesh};

#[repr(C)]
#[derive(Debug, Copy, Clone, bytemuck::Pod, bytemuck::Zeroable)]
struct CameraUniform {
    view_proj: [[f32; 4]; 4],
}

#[allow(dead_code)]
async fn capture_screenshot(
    device: &wgpu::Device,
    queue: &wgpu::Queue,
    texture: &wgpu::Texture,
    width: u32,
    height: u32,
) {
    let u32_size = std::mem::size_of::<u32>() as u32;
    let output_buffer_size = (u32_size * width * height) as wgpu::BufferAddress;
    let output_buffer_desc = wgpu::BufferDescriptor {
        size: output_buffer_size,
        usage: wgpu::BufferUsages::COPY_DST | wgpu::BufferUsages::MAP_READ,
        label: Some("Screenshot Buffer"),
        mapped_at_creation: false,
    };
    let output_buffer = device.create_buffer(&output_buffer_desc);

    let mut encoder = device.create_command_encoder(&wgpu::CommandEncoderDescriptor {
        label: Some("Screenshot Encoder"),
    });

    encoder.copy_texture_to_buffer(
        wgpu::TexelCopyTextureInfo {
            aspect: wgpu::TextureAspect::All,
            texture,
            mip_level: 0,
            origin: wgpu::Origin3d::ZERO,
        },
        wgpu::TexelCopyBufferInfo {
            buffer: &output_buffer,
            layout: wgpu::TexelCopyBufferLayout {
                offset: 0,
                bytes_per_row: Some(u32_size * width),
                rows_per_image: Some(height),
            },
        },
        wgpu::Extent3d {
            width,
            height,
            depth_or_array_layers: 1,
        },
    );

    queue.submit(Some(encoder.finish()));

    let buffer_slice = output_buffer.slice(..);
    let (tx, rx) = std::sync::mpsc::channel();
    buffer_slice.map_async(wgpu::MapMode::Read, move |v| tx.send(v).unwrap());

    let _ = device.poll(wgpu::PollType::Wait { submission_index: None, timeout: None });

    if let Ok(Ok(())) = rx.recv() {
        let data = buffer_slice.get_mapped_range();
        use image::{ImageBuffer, Rgba};
        let buffer = ImageBuffer::<Rgba<u8>, _>::from_raw(width, height, data.to_vec()).unwrap();
        buffer.save("screenshot.png").unwrap();
        drop(data);
        output_buffer.unmap();
        println!("Screenshot saved to screenshot.png");
    }
}

pub struct RenderObject {
    pub vertex_buffer: wgpu::Buffer,
    pub index_buffer: wgpu::Buffer,
    pub num_indices: u32,
    pub bind_group: wgpu::BindGroup,
    pub min: glam::Vec3,
    pub max: glam::Vec3,
}

pub struct AnimatedRenderObject {
    pub vertex_buffer: wgpu::Buffer,
    pub index_buffer: wgpu::Buffer,
    pub num_indices: u32,
    pub num_frames: u32,
    pub vertices_per_frame: u32,
    pub bind_group: wgpu::BindGroup, // Texture
    pub animation_buffer: wgpu::Buffer,
    pub animation_bind_group: wgpu::BindGroup,
}

pub mod skybox;
use crate::frustum::Frustum;
use crate::camera::{Camera, CameraController};
use crate::player::Player;
use crate::octree::{Octree, Triangle};

pub struct State {
    surface: wgpu::Surface<'static>,
    pub device: wgpu::Device,
    pub queue: wgpu::Queue,
    pub config: wgpu::SurfaceConfiguration,
    pub size: winit::dpi::PhysicalSize<u32>,
    render_pipeline: wgpu::RenderPipeline,
    animated_pipeline: wgpu::RenderPipeline,
    pub camera: Camera,
    pub camera_controller: CameraController,
    camera_uniform: CameraUniform,
    camera_buffer: wgpu::Buffer,
    camera_bind_group: wgpu::BindGroup,
    pub texture_bind_group_layout: wgpu::BindGroupLayout,
    pub camera_bind_group_layout: wgpu::BindGroupLayout,
    pub animation_bind_group_layout: wgpu::BindGroupLayout,
    depth_texture_view: wgpu::TextureView,
    frustum: Frustum,
    pub objects: Vec<RenderObject>,
    pub player: Option<(Player, AnimatedRenderObject)>,
    pub skybox: Option<skybox::Skybox>,
    pub egui: Option<egui_wgpu::Renderer>,
    pub egui_state: egui_winit::State,
    pub last_render_time: std::time::Instant,
    pub screenshot_requested: bool,
    pub octree: Option<Octree<Triangle>>,
}

impl State {
    fn create_depth_texture(
        device: &wgpu::Device,
        config: &wgpu::SurfaceConfiguration,
    ) -> wgpu::TextureView {
        let size = wgpu::Extent3d {
            width: config.width,
            height: config.height,
            depth_or_array_layers: 1,
        };
        let desc = wgpu::TextureDescriptor {
            label: Some("depth_texture"),
            size,
            mip_level_count: 1,
            sample_count: 1,
            dimension: wgpu::TextureDimension::D2,
            format: wgpu::TextureFormat::Depth32Float,
            usage: wgpu::TextureUsages::RENDER_ATTACHMENT | wgpu::TextureUsages::TEXTURE_BINDING,
            view_formats: &[],
        };
        let texture = device.create_texture(&desc);
        texture.create_view(&wgpu::TextureViewDescriptor::default())
    }

    pub async fn new(window: Arc<Window>) -> Self {
        let size = window.inner_size();

        let instance = wgpu::Instance::default();

        let surface = instance.create_surface(window.clone()).unwrap();

        let adapter = instance.request_adapter(
            &wgpu::RequestAdapterOptions {
                power_preference: wgpu::PowerPreference::default(),
                compatible_surface: Some(&surface),
                force_fallback_adapter: false,
            },
        ).await.unwrap();

        let (device, queue) = adapter.request_device(
            &wgpu::DeviceDescriptor {
                label: None,
                required_features: wgpu::Features::empty(),
                required_limits: wgpu::Limits::default(),
                memory_hints: Default::default(),
                experimental_features: Default::default(),
                trace: wgpu::Trace::Off,
            },
        ).await.unwrap();

        let caps = surface.get_capabilities(&adapter);
        let config = wgpu::SurfaceConfiguration {
            usage: wgpu::TextureUsages::RENDER_ATTACHMENT | wgpu::TextureUsages::COPY_SRC,
            format: caps.formats[0],
            width: size.width,
            height: size.height,
            present_mode: wgpu::PresentMode::Fifo,
            alpha_mode: caps.alpha_modes[0],
            view_formats: vec![],
            desired_maximum_frame_latency: 2,
        };
        surface.configure(&device, &config);

        let shader = device.create_shader_module(wgpu::include_wgsl!("shader.wgsl"));

        let camera = Camera {
            eye: (0.0, 5.0, 10.0).into(),
            target: (0.0, 0.0, 0.0).into(),
            up: glam::Vec3::Y,
            aspect: config.width as f32 / config.height as f32,
            fovy: 45.0,
            znear: 0.1,
            zfar: 1000.0,
        };
        let camera_controller = CameraController::new(1.0, 0.05);

        let camera_uniform = CameraUniform {
            view_proj: camera.build_view_projection_matrix().to_cols_array_2d(),
        };
        let camera_buffer = device.create_buffer_init(
            &wgpu::util::BufferInitDescriptor {
                label: Some("Camera Buffer"),
                contents: bytemuck::cast_slice(&[camera_uniform]),
                usage: wgpu::BufferUsages::UNIFORM | wgpu::BufferUsages::COPY_DST,
            }
        );

        let camera_bind_group_layout = device.create_bind_group_layout(&wgpu::BindGroupLayoutDescriptor {
            label: Some("camera_bind_group_layout"),
            entries: &[
                wgpu::BindGroupLayoutEntry {
                    binding: 0,
                    visibility: wgpu::ShaderStages::VERTEX | wgpu::ShaderStages::FRAGMENT,
                    ty: wgpu::BindingType::Buffer {
                        ty: wgpu::BufferBindingType::Uniform,
                        has_dynamic_offset: false,
                        min_binding_size: None,
                    },
                    count: None,
                }
            ],
        });

        let camera_bind_group = device.create_bind_group(&wgpu::BindGroupDescriptor {
            layout: &camera_bind_group_layout,
            entries: &[
                wgpu::BindGroupEntry {
                    binding: 0,
                    resource: camera_buffer.as_entire_binding(),
                }
            ],
            label: Some("camera_bind_group"),
        });

        let texture_bind_group_layout = device.create_bind_group_layout(&wgpu::BindGroupLayoutDescriptor {
            entries: &[
                wgpu::BindGroupLayoutEntry {
                    binding: 0,
                    visibility: wgpu::ShaderStages::FRAGMENT,
                    ty: wgpu::BindingType::Texture {
                        sample_type: wgpu::TextureSampleType::Float { filterable: true },
                        view_dimension: wgpu::TextureViewDimension::D2,
                        multisampled: false,
                    },
                    count: None,
                },
                wgpu::BindGroupLayoutEntry {
                    binding: 1,
                    visibility: wgpu::ShaderStages::FRAGMENT,
                    ty: wgpu::BindingType::Sampler(wgpu::SamplerBindingType::Filtering),
                    count: None,
                },
            ],
            label: Some("texture_bind_group_layout"),
        });

        let render_pipeline_layout = device.create_pipeline_layout(&wgpu::PipelineLayoutDescriptor {
            label: Some("Render Pipeline Layout"),
            bind_group_layouts: &[
                Some(&camera_bind_group_layout),
                Some(&texture_bind_group_layout),
            ],
            immediate_size: 0,
        });

        let animation_bind_group_layout = device.create_bind_group_layout(&wgpu::BindGroupLayoutDescriptor {
            label: Some("animation_bind_group_layout"),
            entries: &[
                wgpu::BindGroupLayoutEntry {
                    binding: 0,
                    visibility: wgpu::ShaderStages::VERTEX,
                    ty: wgpu::BindingType::Buffer {
                        ty: wgpu::BufferBindingType::Uniform,
                        has_dynamic_offset: false,
                        min_binding_size: None,
                    },
                    count: None,
                }
            ],
        });

        let animated_pipeline_layout = device.create_pipeline_layout(&wgpu::PipelineLayoutDescriptor {
            label: Some("Animated Pipeline Layout"),
            bind_group_layouts: &[
                Some(&camera_bind_group_layout),
                Some(&texture_bind_group_layout),
                Some(&animation_bind_group_layout),
            ],
            immediate_size: 0,
        });

        let depth_texture_view = Self::create_depth_texture(&device, &config);

        let render_pipeline = device.create_render_pipeline(&wgpu::RenderPipelineDescriptor {
            label: Some("Render Pipeline"),
            layout: Some(&render_pipeline_layout),
            vertex: wgpu::VertexState {
                module: &shader,
                entry_point: Some("vs_main"),
                buffers: &[
                    wgpu::VertexBufferLayout {
                        array_stride: std::mem::size_of::<Vertex>() as wgpu::BufferAddress,
                        step_mode: wgpu::VertexStepMode::Vertex,
                        attributes: &wgpu::vertex_attr_array![0 => Float32x3, 1 => Float32x2, 2 => Float32x3],
                    }
                ],
                compilation_options: Default::default(),
            },
            fragment: Some(wgpu::FragmentState {
                module: &shader,
                entry_point: Some("fs_main"),
                targets: &[Some(wgpu::ColorTargetState {
                    format: config.format,
                    blend: Some(wgpu::BlendState::REPLACE),
                    write_mask: wgpu::ColorWrites::ALL,
                })],
                compilation_options: Default::default(),
            }),
            primitive: wgpu::PrimitiveState {
                topology: wgpu::PrimitiveTopology::TriangleList,
                strip_index_format: None,
                front_face: wgpu::FrontFace::Ccw,
                cull_mode: None,
                polygon_mode: wgpu::PolygonMode::Fill,
                unclipped_depth: false,
                conservative: false,
            },
            depth_stencil: Some(wgpu::DepthStencilState {
                format: wgpu::TextureFormat::Depth32Float,
                depth_write_enabled: Some(true),
                depth_compare: Some(wgpu::CompareFunction::Less),
                stencil: wgpu::StencilState::default(),
                bias: wgpu::DepthBiasState::default(),
            }),
            multisample: wgpu::MultisampleState {
                count: 1,
                mask: !0,
                alpha_to_coverage_enabled: false,
            },
            multiview_mask: None,
            cache: None,
        });

        let animated_shader = device.create_shader_module(wgpu::include_wgsl!("shaders/animated.wgsl"));

        let animated_pipeline = device.create_render_pipeline(&wgpu::RenderPipelineDescriptor {
            label: Some("Animated Render Pipeline"),
            layout: Some(&animated_pipeline_layout),
            vertex: wgpu::VertexState {
                module: &animated_shader,
                entry_point: Some("vs_main"),
                buffers: &[
                    wgpu::VertexBufferLayout {
                        array_stride: std::mem::size_of::<Vertex>() as wgpu::BufferAddress,
                        step_mode: wgpu::VertexStepMode::Vertex,
                        attributes: &wgpu::vertex_attr_array![
                            0 => Float32x3, // pos_a
                            1 => Float32x2, // uv
                            2 => Float32x3, // norm_a
                        ],
                    },
                    wgpu::VertexBufferLayout {
                        array_stride: std::mem::size_of::<Vertex>() as wgpu::BufferAddress,
                        step_mode: wgpu::VertexStepMode::Vertex,
                        attributes: &wgpu::vertex_attr_array![
                            3 => Float32x3, // pos_b
                            4 => Float32x3, // norm_b
                        ],
                    }
                ],
                compilation_options: Default::default(),
            },
            fragment: Some(wgpu::FragmentState {
                module: &animated_shader,
                entry_point: Some("fs_main"),
                targets: &[Some(wgpu::ColorTargetState {
                    format: config.format,
                    blend: Some(wgpu::BlendState::REPLACE),
                    write_mask: wgpu::ColorWrites::ALL,
                })],
                compilation_options: Default::default(),
            }),
            primitive: wgpu::PrimitiveState {
                topology: wgpu::PrimitiveTopology::TriangleList,
                strip_index_format: None,
                front_face: wgpu::FrontFace::Ccw,
                cull_mode: None,
                polygon_mode: wgpu::PolygonMode::Fill,
                unclipped_depth: false,
                conservative: false,
            },
            depth_stencil: Some(wgpu::DepthStencilState {
                format: wgpu::TextureFormat::Depth32Float,
                depth_write_enabled: Some(true),
                depth_compare: Some(wgpu::CompareFunction::Less),
                stencil: wgpu::StencilState::default(),
                bias: wgpu::DepthBiasState::default(),
            }),
            multisample: wgpu::MultisampleState {
                count: 1,
                mask: !0,
                alpha_to_coverage_enabled: false,
            },
            multiview_mask: None,
            cache: None,
        });

        let egui_context = egui::Context::default();
        egui_extras::install_image_loaders(&egui_context);
        
        let egui_state = egui_winit::State::new(
            egui_context,
            egui::viewport::ViewportId::ROOT,
            &window,
            None,
            None,
            None,
        );
        let egui_renderer = egui_wgpu::Renderer::new(
            &device,
            config.format,
            egui_wgpu::RendererOptions {
                depth_stencil_format: None,
                msaa_samples: 1,
                dithering: true,
                predictable_texture_filtering: true,
            },
        );

        Self {
            surface,
            device,
            queue,
            config,
            size,
            render_pipeline,
            animated_pipeline,
            camera,
            camera_controller,
            camera_uniform,
            camera_buffer,
            camera_bind_group,
            texture_bind_group_layout,
            camera_bind_group_layout,
            animation_bind_group_layout,
            depth_texture_view,
            frustum: Frustum::from_matrix(glam::Mat4::IDENTITY),
            objects: Vec::new(),
            player: None,
            skybox: None,
            egui: Some(egui_renderer),
            egui_state,
            last_render_time: std::time::Instant::now(),
            screenshot_requested: false,
            octree: None,
        }
    }

    pub fn resize(&mut self, new_size: winit::dpi::PhysicalSize<u32>) {
        if new_size.width > 0 && new_size.height > 0 {
            self.size = new_size;
            self.config.width = new_size.width;
            self.config.height = new_size.height;
            self.surface.configure(&self.device, &self.config);
            self.depth_texture_view = Self::create_depth_texture(&self.device, &self.config);
        }
    }

    pub fn add_object(&mut self, mesh: &Mesh, bind_group: wgpu::BindGroup) {
        let vertex_buffer = self.device.create_buffer_init(
            &wgpu::util::BufferInitDescriptor {
                label: Some("Vertex Buffer"),
                contents: bytemuck::cast_slice(&mesh.vertices),
                usage: wgpu::BufferUsages::VERTEX,
            }
        );
        let index_buffer = self.device.create_buffer_init(
            &wgpu::util::BufferInitDescriptor {
                label: Some("Index Buffer"),
                contents: bytemuck::cast_slice(&mesh.indices),
                usage: wgpu::BufferUsages::INDEX,
            }
        );

        let mut min = glam::Vec3::splat(f32::MAX);
        let mut max = glam::Vec3::splat(f32::MIN);
        for v in &mesh.vertices {
            let pos = glam::Vec3::from_slice(&v.position);
            min = min.min(pos);
            max = max.max(pos);
        }

        self.objects.push(RenderObject {
            vertex_buffer,
            index_buffer,
            num_indices: mesh.indices.len() as u32,
            bind_group,
            min,
            max,
        });
    }

    pub fn set_player(&mut self, player: Player, texture_bind_group: wgpu::BindGroup) {
        let vertex_buffer = self.device.create_buffer_init(
            &wgpu::util::BufferInitDescriptor {
                label: Some("Player Vertex Buffer"),
                contents: bytemuck::cast_slice(&player.animated_mesh.all_vertices),
                usage: wgpu::BufferUsages::VERTEX,
            }
        );

        let index_buffer = self.device.create_buffer_init(
            &wgpu::util::BufferInitDescriptor {
                label: Some("Player Index Buffer"),
                contents: bytemuck::cast_slice(&player.animated_mesh.indices),
                usage: wgpu::BufferUsages::INDEX,
            }
        );

        let animation_buffer = self.device.create_buffer(
            &wgpu::BufferDescriptor {
                label: Some("Animation Buffer"),
                size: 80, // f32 + pad + mat4
                usage: wgpu::BufferUsages::UNIFORM | wgpu::BufferUsages::COPY_DST,
                mapped_at_creation: false,
            }
        );

        let animation_bind_group = self.device.create_bind_group(&wgpu::BindGroupDescriptor {
            layout: &self.animation_bind_group_layout,
            entries: &[
                wgpu::BindGroupEntry {
                    binding: 0,
                    resource: animation_buffer.as_entire_binding(),
                }
            ],
            label: Some("animation_bind_group"),
        });

        let animated_obj = AnimatedRenderObject {
            vertex_buffer,
            index_buffer,
            num_indices: player.animated_mesh.indices.len() as u32,
            num_frames: player.animated_mesh.num_frames,
            vertices_per_frame: player.animated_mesh.vertices_per_frame,
            bind_group: texture_bind_group,
            animation_buffer,
            animation_bind_group,
        };

        self.player = Some((player, animated_obj));
    }

    pub fn update_camera(&mut self, view_proj: glam::Mat4) {
        self.camera_uniform.view_proj = view_proj.to_cols_array_2d();
        self.queue.write_buffer(&self.camera_buffer, 0, bytemuck::cast_slice(&[self.camera_uniform]));
        self.frustum = Frustum::from_matrix(view_proj);
    }

    pub fn update(&mut self, pressed_keys: &std::collections::HashSet<winit::keyboard::KeyCode>) {
        if let Some((player, _)) = &self.player {
            // In follow orbit mode, update the camera orbit angle based on WASD steering or mouse,
            // and position the camera EYE/TARGET tracking the player coordinates.
            let cam_forward = (self.camera.target - self.camera.eye).normalize();
            let mut cam_forward_flat = glam::Vec3::new(cam_forward.x, 0.0, cam_forward.z);
            if cam_forward_flat.length_squared() < 1e-6 {
                cam_forward_flat = glam::Vec3::new(0.0, 0.0, 1.0);
            } else {
                cam_forward_flat = cam_forward_flat.normalize();
            }
            let cam_right_flat = cam_forward_flat.cross(glam::Vec3::Y).normalize();

            let mut movement_dir = glam::Vec3::ZERO;
            let speed = 20.0; // matching player movement max speed

            if pressed_keys.contains(&winit::keyboard::KeyCode::KeyW) {
                movement_dir += cam_forward_flat;
            }
            if pressed_keys.contains(&winit::keyboard::KeyCode::KeyS) {
                movement_dir -= cam_forward_flat;
            }
            if pressed_keys.contains(&winit::keyboard::KeyCode::KeyA) {
                movement_dir -= cam_right_flat;
            }
            if pressed_keys.contains(&winit::keyboard::KeyCode::KeyD) {
                movement_dir += cam_right_flat;
            }

            let dt = 1.0 / 60.0; // fixed physics update step
            let desired_move_horizontal = if movement_dir.length_squared() > 1e-6 {
                movement_dir.normalize() * speed * dt
            } else {
                glam::Vec3::ZERO
            };

            // Calculate direct player rotation to look where they are moving (aligning md2 orientation)
            let mut player_rotation = player.rotation_y;
            if desired_move_horizontal.length_squared() > 1e-6 {
                player_rotation = desired_move_horizontal.z.atan2(desired_move_horizontal.x);
            }

            let jump_requested = pressed_keys.contains(&winit::keyboard::KeyCode::Space);

            // Re-bind mutable borrow on player and invoke the comprehensive octree-physics loop
            if let Some((player, obj)) = &mut self.player {
                if desired_move_horizontal.length_squared() > 1e-6 {
                    player.rotation_y = player_rotation;
                }
                
                if let Some(ref octree) = self.octree {
                    player.update_physics(octree, desired_move_horizontal, jump_requested, dt);
                } else {
                    // fallback offline flat translation if octree is not initialized
                    player.position += desired_move_horizontal;
                }

                // If moving horizontally, speed up custom MD2 animation loops
                let animation_velocity = if desired_move_horizontal.length_squared() > 1e-6 { 0.2 } else { 0.02 };
                player.animated_mesh.current_frame += animation_velocity;
                if player.animated_mesh.current_frame >= (player.animated_mesh.num_frames - 1) as f32 {
                    player.animated_mesh.current_frame = 0.0;
                }

                let interp = player.animated_mesh.current_frame.fract();
                let model_matrix = glam::Mat4::from_translation(player.position) * 
                                  glam::Mat4::from_rotation_y(player.rotation_y);

                let mut data = [0.0f32; 20];
                data[0] = interp;
                data[4..20].copy_from_slice(&model_matrix.to_cols_array());
                self.queue.write_buffer(&obj.animation_buffer, 0, bytemuck::cast_slice(&data));

                // Anchor and orbit the follower CAMERA focusing of the player
                let yaw = self.camera_controller.yaw;
                let pitch = self.camera_controller.pitch;

                let orbit_offset = glam::Vec3::new(
                    yaw.cos() * pitch.cos(),
                    pitch.sin(),
                    yaw.sin() * pitch.cos(),
                ).normalize() * 15.0; // 15 distance zoom backing off

                self.camera.target = player.position + glam::Vec3::new(0.0, 1.5, 0.0);
                self.camera.eye = self.camera.target - orbit_offset;
            }
        }

        // Keep view matrix uniform buffers updated
        self.camera_uniform.view_proj = self.camera.build_view_projection_matrix().to_cols_array_2d();
        self.queue.write_buffer(&self.camera_buffer, 0, bytemuck::cast_slice(&[self.camera_uniform]));
        self.frustum = Frustum::from_matrix(self.camera.build_view_projection_matrix());
    }

    pub fn render(&mut self, _window: &Window, full_output: egui::FullOutput) -> Result<(), &'static str> {
        let output = self.surface.get_current_texture();
        let output = match output {
            wgpu::CurrentSurfaceTexture::Success(t) => t,
            wgpu::CurrentSurfaceTexture::Suboptimal(t) => t,
            _ => return Err("Surface lost"),
        };
        let view = output.texture.create_view(&wgpu::TextureViewDescriptor::default());
        let mut encoder = self.device.create_command_encoder(&wgpu::CommandEncoderDescriptor {
            label: Some("Render Encoder"),
        });

        // Egui preparations
        let paint_jobs = self.egui_state.egui_ctx().tessellate(full_output.shapes, full_output.pixels_per_point);
        let screen_descriptor = egui_wgpu::ScreenDescriptor {
            size_in_pixels: [self.config.width, self.config.height],
            pixels_per_point: full_output.pixels_per_point,
        };

        let mut egui = self.egui.take().unwrap();

        for (id, image_delta) in &full_output.textures_delta.set {
            egui.update_texture(&self.device, &self.queue, *id, image_delta);
        }
        egui.update_buffers(&self.device, &self.queue, &mut encoder, &paint_jobs, &screen_descriptor);

        {
            let mut render_pass = encoder.begin_render_pass(&wgpu::RenderPassDescriptor {
                label: Some("Render Pass"),
                color_attachments: &[Some(wgpu::RenderPassColorAttachment {
                    view: &view,
                    resolve_target: None,
                    ops: wgpu::Operations {
                        load: wgpu::LoadOp::Clear(wgpu::Color {
                            r: 0.1,
                            g: 0.2,
                            b: 0.3,
                            a: 1.0,
                        }),
                        store: wgpu::StoreOp::Store,
                    },
                    depth_slice: None,
                })],
                depth_stencil_attachment: Some(wgpu::RenderPassDepthStencilAttachment {
                    view: &self.depth_texture_view,
                    depth_ops: Some(wgpu::Operations {
                        load: wgpu::LoadOp::Clear(1.0),
                        store: wgpu::StoreOp::Store,
                    }),
                    stencil_ops: None,
                }),
                occlusion_query_set: None,
                timestamp_writes: None,
                multiview_mask: None,
            });

            if let Some(skybox) = &self.skybox {
                render_pass.set_pipeline(&skybox.pipeline);
                render_pass.set_bind_group(0, &self.camera_bind_group, &[]);
                render_pass.set_bind_group(1, &skybox.bind_group, &[]);
                render_pass.set_vertex_buffer(0, skybox.vertex_buffer.slice(..));
                render_pass.set_index_buffer(skybox.index_buffer.slice(..), wgpu::IndexFormat::Uint32);
                render_pass.draw_indexed(0..skybox.num_indices, 0, 0..1);
            }

            render_pass.set_pipeline(&self.render_pipeline);
            render_pass.set_bind_group(0, &self.camera_bind_group, &[]);
            
            for obj in &self.objects {
                if self.frustum.aabb_in_frustum(obj.min, obj.max) != crate::frustum::Intersection::Outside {
                    render_pass.set_bind_group(1, &obj.bind_group, &[]);
                    render_pass.set_vertex_buffer(0, obj.vertex_buffer.slice(..));
                    render_pass.set_index_buffer(obj.index_buffer.slice(..), wgpu::IndexFormat::Uint32);
                    render_pass.draw_indexed(0..obj.num_indices, 0, 0..1);
                }
            }

            // Draw player
            if let Some((player, obj)) = &self.player {
                render_pass.set_pipeline(&self.animated_pipeline);
                render_pass.set_bind_group(0, &self.camera_bind_group, &[]);
                render_pass.set_bind_group(1, &obj.bind_group, &[]);
                render_pass.set_bind_group(2, &obj.animation_bind_group, &[]);

                let frame_a = player.animated_mesh.current_frame as u32;
                let frame_b = (frame_a + 1) % player.animated_mesh.num_frames;

                let stride = std::mem::size_of::<Vertex>() as u32;
                let offset_a = (frame_a * obj.vertices_per_frame * stride) as u64;
                let offset_b = (frame_b * obj.vertices_per_frame * stride) as u64;

                render_pass.set_vertex_buffer(0, obj.vertex_buffer.slice(offset_a..));
                render_pass.set_vertex_buffer(1, obj.vertex_buffer.slice(offset_b..));
                render_pass.set_index_buffer(obj.index_buffer.slice(..), wgpu::IndexFormat::Uint32);
                render_pass.draw_indexed(0..obj.num_indices, 0, 0..1);
            }
        }

        {
            let mut render_pass = encoder.begin_render_pass(&wgpu::RenderPassDescriptor {
                label: Some("Egui Render Pass"),
                color_attachments: &[Some(wgpu::RenderPassColorAttachment {
                    view: &view,
                    resolve_target: None,
                    ops: wgpu::Operations {
                        load: wgpu::LoadOp::Load,
                        store: wgpu::StoreOp::Store,
                    },
                    depth_slice: None,
                })],
                depth_stencil_attachment: None,
                timestamp_writes: None,
                occlusion_query_set: None,
                multiview_mask: None,
            });

            // Workaround for egui-wgpu 0.35 / wgpu 29 lifetime issue.
            // egui.render() sometimes forces a 'static lifetime on the render pass due to potential callbacks.
            // Since we know we don't use callbacks that outlive this frame, we can safely erase the lifetime.
            unsafe {
                let render_pass_static: &mut wgpu::RenderPass<'static> = std::mem::transmute(&mut render_pass);
                egui.render(render_pass_static, &paint_jobs, &screen_descriptor);
            }
        }

        for id in &full_output.textures_delta.free {
            egui.free_texture(id);
        }

        self.egui = Some(egui);

        if self.screenshot_requested {
            self.screenshot_requested = false;
            let width = self.config.width;
            let height = self.config.height;
            let u32_size = std::mem::size_of::<u32>() as u32;
            
            // Align bytes_per_row to 256
            let bytes_per_row = u32_size * width;
            let padding = (256 - bytes_per_row % 256) % 256;
            let padded_bytes_per_row = bytes_per_row + padding;
            
            let output_buffer_size = (padded_bytes_per_row * height) as wgpu::BufferAddress;
            let output_buffer_desc = wgpu::BufferDescriptor {
                size: output_buffer_size,
                usage: wgpu::BufferUsages::COPY_DST | wgpu::BufferUsages::MAP_READ,
                label: Some("Screenshot Buffer"),
                mapped_at_creation: false,
            };
            let output_buffer = self.device.create_buffer(&output_buffer_desc);

            encoder.copy_texture_to_buffer(
                wgpu::TexelCopyTextureInfo {
                    aspect: wgpu::TextureAspect::All,
                    texture: &output.texture,
                    mip_level: 0,
                    origin: wgpu::Origin3d::ZERO,
                },
                wgpu::TexelCopyBufferInfo {
                    buffer: &output_buffer,
                    layout: wgpu::TexelCopyBufferLayout {
                        offset: 0,
                        bytes_per_row: Some(padded_bytes_per_row),
                        rows_per_image: Some(height),
                    },
                },
                wgpu::Extent3d {
                    width,
                    height,
                    depth_or_array_layers: 1,
                },
            );

            self.queue.submit(std::iter::once(encoder.finish()));
            
            let buffer_slice = output_buffer.slice(..);
            let (tx, rx) = std::sync::mpsc::channel();
            buffer_slice.map_async(wgpu::MapMode::Read, move |v| tx.send(v).unwrap());

            let _ = self.device.poll(wgpu::PollType::Wait { submission_index: None, timeout: None });

            if let Ok(Ok(())) = rx.recv() {
                let data = buffer_slice.get_mapped_range();
                use image::{ImageBuffer, Rgba};
                
                // We need to handle padding when creating the ImageBuffer
                let mut unpadded_data = Vec::with_capacity((width * height * 4) as usize);
                for y in 0..height {
                    let start = (y * padded_bytes_per_row) as usize;
                    let end = start + (width * 4) as usize;
                    unpadded_data.extend_from_slice(&data[start..end]);
                }

                let buffer = ImageBuffer::<Rgba<u8>, _>::from_raw(width, height, unpadded_data).unwrap();
                buffer.save("screenshot.png").unwrap();
                drop(data);
                output_buffer.unmap();
                println!("Screenshot saved to screenshot.png");
            }

            output.present();
            return Ok(());
        }

        self.queue.submit(std::iter::once(encoder.finish()));
        output.present();

        Ok(())
    }
}

