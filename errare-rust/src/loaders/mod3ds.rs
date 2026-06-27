use std::io::{Read, Seek, SeekFrom, Cursor};
use crate::mesh::{Vertex, Mesh};

const MAIN_CHUNK: u16 = 0x4D4D;
const EDITOR_CHUNK: u16 = 0x3D3D;
const OBJECT_BLOCK: u16 = 0x4000;
const TRIANGULAR_MESH: u16 = 0x4100;
const VERTICES_LIST: u16 = 0x4110;
const FACES_DESCRIPTION: u16 = 0x4120;
const MAPPING_COORDINATES: u16 = 0x4140;

pub struct Loader3DS;

impl Loader3DS {
    pub fn load_bytes(bytes: &[u8]) -> Mesh {
        let mut reader = Cursor::new(bytes);

        let mut all_vertices = Vec::new();
        let mut all_indices = Vec::new();

        let file_size = bytes.len() as u64;
        
        Self::process_chunks(&mut reader, file_size, &mut all_vertices, &mut all_indices);

        Mesh {
            vertices: all_vertices,
            indices: all_indices,
        }
    }
}

impl Loader3DS {
    fn process_chunks<R: Read + Seek>(
        reader: &mut R,
        end_pos: u64,
        all_vertices: &mut Vec<Vertex>,
        all_indices: &mut Vec<u32>,
    ) {
        while let Ok(pos) = reader.stream_position() {
            if pos >= end_pos {
                break;
            }

            let mut id_buf = [0u8; 2];
            if reader.read_exact(&mut id_buf).is_err() { break; }
            let id = u16::from_le_bytes(id_buf);

            let mut len_buf = [0u8; 4];
            if reader.read_exact(&mut len_buf).is_err() { break; }
            let length = u32::from_le_bytes(len_buf);
            let chunk_data_end = pos + length as u64;

            match id {
                MAIN_CHUNK | EDITOR_CHUNK => {
                    Self::process_chunks(reader, chunk_data_end, all_vertices, all_indices);
                }
                OBJECT_BLOCK => {
                    // Read name (null terminated)
                    let mut name = Vec::new();
                    loop {
                        let mut b = [0u8; 1];
                        if reader.read_exact(&mut b).is_err() { break; }
                        if b[0] == 0 { break; }
                        name.push(b[0]);
                    }
                    Self::process_chunks(reader, chunk_data_end, all_vertices, all_indices);
                }
                TRIANGULAR_MESH => {
                    Self::process_triangular_mesh(reader, chunk_data_end, all_vertices, all_indices);
                }
                _ => {
                    // Skip unknown chunk
                    if reader.seek(SeekFrom::Start(chunk_data_end)).is_err() {
                        break;
                    }
                }
            }
            // Ensure we move to the next chunk even if sub-processing didn't consume all
            let _ = reader.seek(SeekFrom::Start(chunk_data_end));
        }
    }

    fn process_triangular_mesh<R: Read + Seek>(
        reader: &mut R,
        end_pos: u64,
        all_vertices: &mut Vec<Vertex>,
        all_indices: &mut Vec<u32>,
    ) {
        let mut positions = Vec::new();
        let mut tex_coords = Vec::new();
        let mut faces = Vec::new();

        while let Ok(pos) = reader.stream_position() {
            if pos >= end_pos {
                break;
            }

            let mut id_buf = [0u8; 2];
            if reader.read_exact(&mut id_buf).is_err() { break; }
            let id = u16::from_le_bytes(id_buf);

            let mut len_buf = [0u8; 4];
            if reader.read_exact(&mut len_buf).is_err() { break; }
            let length = u32::from_le_bytes(len_buf);
            let chunk_data_end = pos + length as u64;

            match id {
                VERTICES_LIST => {
                    let mut count_buf = [0u8; 2];
                    if reader.read_exact(&mut count_buf).is_ok() {
                        let count = u16::from_le_bytes(count_buf);
                        for _ in 0..count {
                            let mut v = [0.0f32; 3];
                            for i in 0..3 {
                                let mut f_buf = [0u8; 4];
                                if reader.read_exact(&mut f_buf).is_ok() {
                                    v[i] = f32::from_le_bytes(f_buf);
                                }
                            }
                            positions.push(v);
                        }
                    }
                }
                FACES_DESCRIPTION => {
                    let mut count_buf = [0u8; 2];
                    if reader.read_exact(&mut count_buf).is_ok() {
                        let count = u16::from_le_bytes(count_buf);
                        for _ in 0..count {
                            let mut f = [0u16; 3];
                            for i in 0..3 {
                                let mut i_buf = [0u8; 2];
                                if reader.read_exact(&mut i_buf).is_ok() {
                                    f[i] = u16::from_le_bytes(i_buf);
                                }
                            }
                            // skip flags (u16)
                            let mut dummy = [0u8; 2];
                            let _ = reader.read_exact(&mut dummy);
                            faces.push(f);
                        }
                    }
                    // Faces description often contains materials and smoothing groups as sub-chunks
                    // We skip them for this basic loader
                }
                MAPPING_COORDINATES => {
                    let mut count_buf = [0u8; 2];
                    if reader.read_exact(&mut count_buf).is_ok() {
                        let count = u16::from_le_bytes(count_buf);
                        for _ in 0..count {
                            let mut uv = [0.0f32; 2];
                            for i in 0..2 {
                                let mut f_buf = [0u8; 4];
                                if reader.read_exact(&mut f_buf).is_ok() {
                                    uv[i] = f32::from_le_bytes(f_buf);
                                }
                            }
                            tex_coords.push(uv);
                        }
                    }
                }
                _ => {}
            }
            let _ = reader.seek(SeekFrom::Start(chunk_data_end));
        }

        // Finalize this mesh object into the global buffers
        let base_idx = all_vertices.len() as u32;
        let mut mesh_vertices = Vec::with_capacity(positions.len());

        for i in 0..positions.len() {
            let tc = if i < tex_coords.len() {
                let mut uv = tex_coords[i];
                uv[1] = 1.0 - uv[1]; // Flip Y for WGPU
                uv
            } else {
                [0.0, 0.0]
            };
            
            // 3DS coordinate fix: typically Z is up in 3DS Max.
            // We want Y up.
            // [x, y, z] in 3DS -> [x, z, -y] in our Y-up RH system.
            let p = positions[i];
            let fixed_pos = [p[0], p[2], -p[1]];

            mesh_vertices.push(Vertex {
                position: fixed_pos,
                tex_coords: tc,
                normal: [0.0, 0.0, 0.0], // Will calculate
            });
        }

        // Calculate flat normals for the faces
        for f in &faces {
            let i0 = f[0] as usize;
            let i1 = f[1] as usize;
            let i2 = f[2] as usize;

            let p0 = glam::Vec3::from_slice(&mesh_vertices[i0].position);
            let p1 = glam::Vec3::from_slice(&mesh_vertices[i1].position);
            let p2 = glam::Vec3::from_slice(&mesh_vertices[i2].position);

            let v1 = p1 - p0;
            let v2 = p2 - p0;
            let normal = v1.cross(v2).normalize_or_zero();

            // Accumulate normals for vertices (for smooth-ish look if shared)
            // For now just assign to first vertex of face or accumulate
            let n_arr = normal.to_array();
            for idx in &[i0, i1, i2] {
                let v = &mut mesh_vertices[*idx];
                v.normal[0] += n_arr[0];
                v.normal[1] += n_arr[1];
                v.normal[2] += n_arr[2];
            }
        }

        // Normalize the accumulated normals
        for v in &mut mesh_vertices {
            let n = glam::Vec3::from_slice(&v.normal).normalize_or_zero();
            v.normal = n.to_array();
        }

        all_vertices.extend(mesh_vertices);

        for f in faces {
            all_indices.push(base_idx + f[0] as u32);
            all_indices.push(base_idx + f[1] as u32);
            all_indices.push(base_idx + f[2] as u32);
        }
    }
}
