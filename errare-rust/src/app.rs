use std::sync::{Arc, Mutex};
use winit::{
    application::ApplicationHandler,
    event::*,
    event_loop::ActiveEventLoop,
    window::{Window, WindowId},
};

use crate::renderer::State;

use crate::loaders::md2::load_md2;
use crate::loaders::mod3ds::Loader3DS;
use crate::resources::load_binary;
use crate::texture::Texture;
use crate::player::{Player, Item};
use std::io::Cursor;
use std::collections::HashSet;
use winit::keyboard::KeyCode;

pub struct App {
    state: Arc<Mutex<Option<State>>>,
    window: Option<Arc<Window>>,
    pressed_keys: HashSet<KeyCode>,
}

impl App {
    pub fn new() -> Self {
        Self {
            state: Arc::new(Mutex::new(None)),
            window: None,
            pressed_keys: HashSet::new(),
        }
    }
}

async fn initialize_state(window: Arc<Window>) -> State {
    // Load assets
    let mut state = State::new(window).await;
    
    // Terrain
    let terrain_bytes = load_binary("assets/terrain.3ds").await;
    let terrain_mesh = Loader3DS::load_bytes(&terrain_bytes);
    
    // Build physics octree for terrain collision
    {
        use crate::octree::{AABox, Octree, Triangle};
        let mut min = glam::Vec3::splat(f32::MAX);
        let mut max = glam::Vec3::splat(f32::MIN);
        for v in &terrain_mesh.vertices {
            let pos = glam::Vec3::from_slice(&v.position);
            min = min.min(pos);
            max = max.max(pos);
        }
        
        let mut octree = Octree::new(AABox::new(min, max), 4);
        
        for chunk in terrain_mesh.indices.chunks_exact(3) {
            let i0 = chunk[0] as usize;
            let i1 = chunk[1] as usize;
            let i2 = chunk[2] as usize;
            
            let p0 = glam::Vec3::from_slice(&terrain_mesh.vertices[i0].position);
            let p1 = glam::Vec3::from_slice(&terrain_mesh.vertices[i1].position);
            let p2 = glam::Vec3::from_slice(&terrain_mesh.vertices[i2].position);
            
            let normal = (p1 - p0).cross(p2 - p0).normalize_or_zero();
            let tri = Triangle { p0, p1, p2, normal };
            let centroid = (p0 + p1 + p2) / 3.0;
            octree.insert(tri, centroid);
        }
        state.octree = Some(octree);
    }
    
    let terrain_tex_bytes = load_binary("assets/terrain.png").await;
    let terrain_tex = Texture::from_bytes(&state.device, &state.queue, &terrain_tex_bytes, "terrain_tex").unwrap();
    let terrain_bind_group = state.device.create_bind_group(&wgpu::BindGroupDescriptor {
        layout: &state.texture_bind_group_layout,
        entries: &[
            wgpu::BindGroupEntry {
                binding: 0,
                resource: wgpu::BindingResource::TextureView(&terrain_tex.view),
            },
            wgpu::BindGroupEntry {
                binding: 1,
                resource: wgpu::BindingResource::Sampler(&terrain_tex.sampler),
            },
        ],
        label: None,
    });
    state.add_object(&terrain_mesh, terrain_bind_group);

    // Character
    let hobgoblin_bytes = load_binary("assets/hobgoblin.md2").await;
    let animated_mesh = load_md2(Cursor::new(hobgoblin_bytes));
    
    if animated_mesh.num_frames > 0 {
        let character_tex_bytes = load_binary("assets/hobgoblin.png").await;
        let character_tex = Texture::from_bytes(&state.device, &state.queue, &character_tex_bytes, "character_tex").unwrap();
        let character_bind_group = state.device.create_bind_group(&wgpu::BindGroupDescriptor {
            layout: &state.texture_bind_group_layout,
            entries: &[
                wgpu::BindGroupEntry {
                    binding: 0,
                    resource: wgpu::BindingResource::TextureView(&character_tex.view),
                },
                wgpu::BindGroupEntry {
                    binding: 1,
                    resource: wgpu::BindingResource::Sampler(&character_tex.sampler),
                },
            ],
            label: None,
        });

        let mut player = Player::new("Hero", animated_mesh);
        player.life = 100.0;
        player.max_life = 100.0;
        player.mana = 50.0;
        player.max_mana = 50.0;
        player.level = 1;
        
        player.inventory.bags[0][0] = Some(Item {
            id: "1".to_string(),
            name: "Iron Sword".to_string(),
            icon_path: "assets/gui/0101.png".to_string(),
        });
        player.inventory.bags[0][1] = Some(Item {
            id: "2".to_string(),
            name: "Health Potion".to_string(),
            icon_path: "assets/gui/0801.png".to_string(),
        });
        player.inventory.bags[0][2] = Some(Item {
            id: "3".to_string(),
            name: "Mana Potion".to_string(),
            icon_path: "assets/gui/0802.png".to_string(),
        });
        player.inventory.bags[0][3] = Some(Item {
            id: "4".to_string(),
            name: "Shield".to_string(),
            icon_path: "assets/gui/0201.png".to_string(),
        });
        player.inventory.bags[0][4] = Some(Item {
            id: "5".to_string(),
            name: "Iron Helmet".to_string(),
            icon_path: "assets/gui/0301.png".to_string(),
        });

        state.set_player(player, character_bind_group);
    }

    // Skybox
    let sky_bytes = load_binary("assets/sky.png").await;
    let skybox = crate::renderer::skybox::Skybox::new(
        &state.device,
        &state.queue,
        &state.texture_bind_group_layout,
        &state.camera_bind_group_layout,
        state.config.format,
        &sky_bytes,
    ).await;
    state.skybox = Some(skybox);

    state
}

impl ApplicationHandler for App {
    fn resumed(&mut self, event_loop: &ActiveEventLoop) {
        if self.window.is_none() {
            let window = Arc::new(
                event_loop
                    .create_window(Window::default_attributes().with_title("Errare Rust"))
                    .unwrap(),
            );
            self.window = Some(window.clone());

            #[cfg(target_arch = "wasm32")]
            {
                use wasm_bindgen::prelude::*;
                use web_sys::{Document, Window};

                let window_web = web_sys::window().expect("no global `window` exists");
                let document = window_web.document().expect("should have a document on window");
                let body = document.body().expect("document should have a body");

                // In WASM we need to append the canvas to the body
                use winit::platform::web::WindowExtWebSys;
                let canvas = window.canvas().expect("should have a canvas");
                let _ = body.append_child(&canvas);

                let state_clone = self.state.clone();
                wasm_bindgen_futures::spawn_local(async move {
                    let state = initialize_state(window).await;
                    *state_clone.lock().unwrap() = Some(state);
                });
            }
            #[cfg(not(target_arch = "wasm32"))]
            {
                let state = pollster::block_on(initialize_state(window));
                *self.state.lock().unwrap() = Some(state);
            }
        }
    }

    fn device_event(
        &mut self,
        _event_loop: &ActiveEventLoop,
        _device_id: DeviceId,
        event: DeviceEvent,
    ) {
        let mut state_lock = self.state.lock().unwrap();
        if let Some(state) = state_lock.as_mut() {
            match event {
                DeviceEvent::MouseMotion { delta } => {
                    state.camera_controller.process_mouse(delta.0, delta.1);
                }
                _ => (),
            }
        }
    }

    fn window_event(&mut self, event_loop: &ActiveEventLoop, _id: WindowId, event: WindowEvent) {
        let mut state_lock = self.state.lock().unwrap();
        let state = match state_lock.as_mut() {
            Some(state) => state,
            None => return,
        };

        // UI events
        let ui_responded = state.egui_state.on_window_event(self.window.as_ref().unwrap(), &event);
        if ui_responded.consumed {
            return;
        }

        match event {
            WindowEvent::CloseRequested
            | WindowEvent::KeyboardInput {
                event:
                    KeyEvent {
                        state: ElementState::Pressed,
                        logical_key: winit::keyboard::Key::Named(winit::keyboard::NamedKey::Escape),
                        ..
                    },
                ..
            } => {
                event_loop.exit();
            }
            WindowEvent::KeyboardInput {
                event: KeyEvent {
                    physical_key: winit::keyboard::PhysicalKey::Code(code),
                    state,
                    ..
                },
                ..
            } => {
                if state == ElementState::Pressed {
                    self.pressed_keys.insert(code);
                } else {
                    self.pressed_keys.remove(&code);
                }
            }
            WindowEvent::Resized(physical_size) => {
                state.resize(physical_size);
            }
            WindowEvent::RedrawRequested => {
                let now = std::time::Instant::now();
                state.last_render_time = now;
                
                state.update(&self.pressed_keys);

                // egui
                let raw_input = state.egui_state.take_egui_input(self.window.as_ref().unwrap());
                let mut player_data = state.player.as_mut().map(|(p, _)| {
                    (
                        p.name.clone(), 
                        p.life, p.max_life, 
                        p.mana, p.max_mana, 
                        p.level,
                        &mut p.inventory
                    )
                });

                let full_output = state.egui_state.egui_ctx().run_ui(raw_input, |ctx| {
                    egui::Area::new(egui::Id::new("action_bar_area"))
                        .anchor(egui::Align2::CENTER_BOTTOM, egui::vec2(0.0, -10.0))
                        .show(ctx, |ui| {
                            egui::Frame::default().show(ui, |ui: &mut egui::Ui| {
                                ui.horizontal(|ui: &mut egui::Ui| {
                                // Inventory button
                                let inv_icon = egui::include_image!("../assets/gui/inventory.png");
                                if ui.add(egui::Button::image(inv_icon).min_size(egui::vec2(32.0, 32.0))).clicked() {
                                    if let Some((_, _, _, _, _, _, inv)) = player_data.as_mut() {
                                        inv.show_inventory = !inv.show_inventory;
                                    }
                                }

                                // Character button
                                let char_icon = egui::include_image!("../assets/gui/characteristic.png");
                                if ui.add(egui::Button::image(char_icon).min_size(egui::vec2(32.0, 32.0))).clicked() {
                                    if let Some((_, _, _, _, _, _, inv)) = player_data.as_mut() {
                                        inv.show_equipment = !inv.show_equipment;
                                    }
                                }

                                    // Health/Mana info
                                    if let Some((_, life, max_life, mana, max_mana, _, _)) = &player_data {
                                        ui.vertical(|ui: &mut egui::Ui| {
                                            ui.add(egui::ProgressBar::new(*life as f32 / *max_life as f32).text(format!("HP: {}/{}", life, max_life)).desired_width(120.0));
                                            ui.add(egui::ProgressBar::new(*mana as f32 / *max_mana as f32).text(format!("MP: {}/{}", mana, max_mana)).desired_width(120.0));
                                        });
                                    }

                                    if ui.button("📸").on_hover_text("Take Screenshot").clicked() {
                                        let mut state_lock = self.state.lock().unwrap();
                                        if let Some(st) = state_lock.as_mut() {
                                            st.screenshot_requested = true;
                                            println!("Screenshot requested at {:?}", std::time::Instant::now());
                                        }
                                    }
                                });
                            });
                        });

                    if let Some((name, life, max_life, mana, max_mana, level, inv)) = player_data.as_mut() {
                        if inv.show_inventory {
                            egui::Window::new("Inventory")
                                .collapsible(false)
                                .show(ctx, |ui| {
                                egui::Grid::new("bag_grid").spacing(egui::vec2(4.0, 4.0)).show(ui, |ui| {
                                    for row in 0..5 {
                                        for col in 0..3 {
                                            let idx = row * 3 + col;
                                            let item = &inv.bags[0][idx];
                                            
                                                let response = ui.group(|ui| {
                                                ui.set_min_size(egui::vec2(32.0, 32.0));
                                                if let Some(item) = item {
                                                    // Use current working directory to build absolute path
                                                    let cwd = std::env::current_dir().unwrap_or_else(|_| std::path::PathBuf::from("."));
                                                    let icon_name = item.icon_path.split('/').last().unwrap_or("undef.png");
                                                    let abs_path = cwd.join("assets").join("gui").join(icon_name);
                                                    let uri = format!("file://{}", abs_path.to_str().unwrap_or(""));
                                                    ui.add(egui::Image::new(uri).max_width(32.0));
                                                } else {
                                                    ui.add(egui::Image::new(egui::include_image!("../assets/gui/slot.png")).max_width(32.0));
                                                }
                                            }).response;

                                            if let Some(item) = item {
                                                response.on_hover_text(&item.name);
                                            }
                                        }
                                        ui.end_row();
                                    }
                                });
                            });
                        }

                        if inv.show_equipment {
                            egui::Window::new("Character").show(ctx, |ui| {
                                ui.horizontal(|ui| {
                                    // Show the characteristic icon
                                    ui.add(egui::Image::new(egui::include_image!("../assets/gui/characteristic.png")).max_width(64.0));
                                    ui.vertical(|ui| {
                                        ui.heading(name);
                                        ui.label(format!("Level: {}", level));
                                    });
                                });
                                ui.separator();
                                ui.add(egui::ProgressBar::new(*life as f32 / *max_life as f32).text(format!("HP: {}/{}", life, max_life)));
                                ui.add(egui::ProgressBar::new(*mana as f32 / *max_mana as f32).text(format!("MP: {}/{}", mana, max_mana)));
                                
                                ui.separator();
                                ui.label("Equipment");
                                // Background for equipment
                                ui.add(egui::Image::new(egui::include_image!("../assets/gui/equipement.png")).max_width(200.0));
                            });
                        }
                    }
                });

                match state.render(self.window.as_ref().unwrap(), full_output) {
                    Ok(_) => {}
                    // Reconfigure the surface if lost
                    Err("Surface lost") => state.resize(state.size),
                    // All other errors (Outdated, etc.) should be resolved by the next frame
                    Err(e) => eprintln!("{:?}", e),
                }
            }
            ref event => {
                state.camera_controller.process_events(event);
            }
        }

        if let Some(window) = self.window.as_ref() {
            window.request_redraw();
        }
    }
}
