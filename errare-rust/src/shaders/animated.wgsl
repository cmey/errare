struct CameraUniform {
    view_proj: mat4x4<f32>,
};
@group(0) @binding(0)
var<uniform> camera: CameraUniform;

struct AnimationUniform {
    interpolation: f32,
    _pad0: f32,
    _pad1: f32,
    _pad2: f32,
    model_matrix: mat4x4<f32>,
};
@group(2) @binding(0)
var<uniform> animation: AnimationUniform;

struct VertexInput {
    @location(0) position_a: vec3<f32>,
    @location(1) tex_coords: vec2<f32>,
    @location(2) normal_a: vec3<f32>,
    @location(3) position_b: vec3<f32>,
    @location(4) normal_b: vec3<f32>,
};

struct VertexOutput {
    @builtin(position) clip_position: vec4<f32>,
    @location(0) tex_coords: vec2<f32>,
    @location(1) world_normal: vec3<f32>,
};

@vertex
fn vs_main(
    model: VertexInput,
) -> VertexOutput {
    var out: VertexOutput;
    
    // Interpolate position and normal
    let pos = mix(model.position_a, model.position_b, animation.interpolation);
    let normal = mix(model.normal_a, model.normal_b, animation.interpolation);
    
    out.clip_position = camera.view_proj * animation.model_matrix * vec4<f32>(pos, 1.0);
    out.tex_coords = model.tex_coords;
    out.world_normal = (animation.model_matrix * vec4<f32>(normal, 0.0)).xyz;
    return out;
}

@group(1) @binding(0)
var t_diffuse: texture_2d<f32>;
@group(1) @binding(1)
var s_diffuse: sampler;

@fragment
fn fs_main(in: VertexOutput) -> @location(0) vec4<f32> {
    let object_color = textureSample(t_diffuse, s_diffuse, in.tex_coords);
    
    // Simple directional light
    let light_dir = normalize(vec3<f32>(1.0, 1.0, 1.0));
    let diffuse_strength = max(dot(in.world_normal, light_dir), 0.2); // 0.2 ambient
    
    return vec4<f32>(object_color.rgb * diffuse_strength, object_color.a);
}
