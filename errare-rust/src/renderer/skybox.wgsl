struct CameraUniform {
    view_proj: mat4x4<f32>,
};

@group(0) @binding(0)
var<uniform> camera: CameraUniform;

@group(1) @binding(0)
var t_sky: texture_2d<f32>;
@group(1) @binding(1)
var s_sky: sampler;

struct VertexInput {
    @location(0) position: vec3<f32>,
};

struct VertexOutput {
    @builtin(position) clip_position: vec4<f32>,
    @location(0) view_dir: vec3<f32>,
};

@vertex
fn vs_main(model: VertexInput) -> VertexOutput {
    var out: VertexOutput;
    out.view_dir = model.position;
    
    // Remove translation from view matrix
    var view = camera.view_proj;
    view[3][0] = 0.0;
    view[3][1] = 0.0;
    view[3][2] = 0.0;
    
    let pos = camera.view_proj * vec4<f32>(model.position, 1.0);
    // Force depth to 1.0
    out.clip_position = pos.xyww;
    return out;
}

@fragment
fn fs_main(in: VertexOutput) -> @location(0) vec4<f32> {
    // Basic spherical mapping for a 2D texture since we don't have a cubemap yet
    let dir = normalize(in.view_dir);
    let u = 0.5 + atan2(dir.z, dir.x) / (2.0 * 3.14159);
    let v = 0.5 - asin(dir.y) / 3.14159;
    
    return textureSample(t_sky, s_sky, vec2<f32>(u, v));
}
