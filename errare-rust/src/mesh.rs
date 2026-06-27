use bytemuck::{Pod, Zeroable};

#[repr(C)]
#[derive(Copy, Clone, Debug, Pod, Zeroable)]
pub struct Vertex {
    pub position: [f32; 3],
    pub tex_coords: [f32; 2],
    pub normal: [f32; 3],
}

pub struct Mesh {
    pub vertices: Vec<Vertex>,
    pub indices: Vec<u32>,
}

pub trait ModelLoader {
    fn load(path: &str) -> Mesh;
}

pub struct AnimatedMesh {
    pub all_vertices: Vec<Vertex>,
    pub indices: Vec<u32>,
    pub num_frames: u32,
    pub vertices_per_frame: u32,
    pub current_frame: f32,
}
