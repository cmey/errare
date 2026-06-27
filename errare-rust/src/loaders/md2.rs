use std::io::{Read, Seek, SeekFrom};
use crate::mesh::{Vertex, AnimatedMesh};
use std::collections::HashMap;

pub struct MD2Header {
    pub magic: i32,
    pub version: i32,
    pub skin_width: i32,
    pub skin_height: i32,
    pub frame_size: i32,
    pub num_skins: i32,
    pub num_vertices: i32,
    pub num_st: i32,
    pub num_tris: i32,
    pub num_glcmds: i32,
    pub num_frames: i32,
    pub offset_skins: i32,
    pub offset_st: i32,
    pub offset_tris: i32,
    pub offset_frames: i32,
    pub offset_glcmds: i32,
    pub offset_end: i32,
}

// Precomputed normal table for MD2
const MD2_NORMALS: [[f32; 3]; 173] = [
    [-0.525731, 0.000000, 0.850651], [-0.442285, 0.285888, 0.849931],
    [-0.295242, 0.477747, 0.827466], [-0.094031, 0.581235, 0.808214],
    [0.127431, 0.589414, 0.797711], [0.334051, 0.510629, 0.791502],
    [0.499781, 0.363091, 0.783151], [0.597961, 0.170591, 0.783111],
    [0.618671, -0.043519, 0.784231], [0.563141, -0.252069, 0.787751],
    [0.439161, -0.428519, 0.788581], [0.263591, -0.551609, 0.791532],
    [0.056121, -0.612059, 0.788881], [-0.158782, -0.604249, 0.780181],
    [-0.354052, -0.523039, 0.771231], [-0.505501, -0.379469, 0.774561],
    [-0.588881, -0.197029, 0.778741], [-0.713501, 0.276413, 0.643792],
    [-0.675001, 0.473523, 0.566112], [-0.569441, 0.623103, 0.535352],
    [-0.420651, 0.719602, 0.552252], [-0.231261, 0.776512, 0.586112],
    [-0.012351, 0.796332, 0.604592], [0.210451, 0.772092, 0.600862],
    [0.410291, 0.704252, 0.579482], [0.558361, 0.600982, 0.573932],
    [0.645061, 0.457192, 0.612062], [0.676661, 0.285223, 0.675001],
    [0.642591, 0.103091, 0.759531], [0.540101, -0.063171, 0.838891],
    [0.379511, -0.207869, 0.900951], [0.183491, -0.315729, 0.930921],
    [-0.027001, -0.370539, 0.928421], [-0.235552, -0.369529, 0.898861],
    [-0.418721, -0.311309, 0.856001], [-0.543451, -0.201729, 0.814981],
    [-0.608321, -0.065831, 0.791161], [-0.852431, 0.263152, 0.451952],
    [-0.791532, 0.455073, 0.407482], [-0.686521, 0.608312, 0.395672],
    [-0.536961, 0.730242, 0.421712], [-0.347592, 0.812322, 0.467472],
    [-0.137451, 0.850451, 0.508532], [0.082981, 0.846501, 0.525732],
    [0.301371, 0.797202, 0.521872], [0.490801, 0.704382, 0.514782],
    [0.638531, 0.570222, 0.518682], [0.735491, 0.410931, 0.538562],
    [0.771231, 0.233664, 0.592182], [0.743141, 0.052641, 0.666991],
    [0.655211, -0.116529, 0.746061], [0.513731, -0.260599, 0.817441],
    [0.334051, -0.369809, 0.866841], [0.134051, -0.429409, 0.893111],
    [-0.071721, -0.443049, 0.893631], [-0.263112, -0.406939, 0.874741],
    [-0.426211, -0.329439, 0.841361], [-0.548811, -0.219509, 0.803361],
    [-0.612731, -0.087791, 0.784961], [-0.941651, 0.222952, 0.252062],
    [-0.884502, 0.402632, 0.241512], [-0.781841, 0.549212, 0.289412],
    [-0.632941, 0.672642, 0.380482], [-0.439811, 0.760772, 0.473522],
    [-0.222381, 0.810622, 0.541452], [0.005181, 0.814382, 0.580212],
    [0.232351, 0.773822, 0.589132], [0.441861, 0.686562, 0.573932],
    [0.609461, 0.562722, 0.556212], [0.728561, 0.410931, 0.541602],
    [0.787751, 0.243554, 0.565862], [0.762311, 0.070621, 0.643031],
    [0.668541, -0.093241, 0.738121], [0.525541, -0.241389, 0.811561],
    [0.354051, -0.353389, 0.861751], [0.158782, -0.425119, 0.890691],
    [-0.043519, -0.450419, 0.891731], [-0.243519, -0.430489, 0.871141],
    [-0.420651, -0.366479, 0.830201], [-0.560611, -0.272119, 0.781841],
    [-0.643031, -0.155459, 0.751241], [-0.988221, 0.151771, 0.027001],
    [-0.941142, 0.322962, 0.103091], [-0.835491, 0.473523, 0.271162],
    [-0.672621, 0.597961, 0.437792], [-0.467472, 0.688841, 0.552452],
    [-0.240741, 0.742391, 0.625472], [0.000000, 0.751242, 0.660021],
    [0.240741, 0.742391, 0.625472], [0.467472, 0.688841, 0.552452],
    [0.672621, 0.597961, 0.437792], [0.835491, 0.473523, 0.271162],
    [0.941142, 0.322962, 0.103091], [0.988221, 0.151771, 0.027001],
    [0.963161, -0.027001, 0.266221], [0.867901, -0.201729, 0.455072],
    [0.709321, -0.354519, 0.609461], [0.504261, -0.474668, 0.724771],
    [0.271161, -0.553958, 0.786961], [0.038161, -0.584748, 0.810051],
    [-0.197029, -0.575088, 0.793831], [-0.418721, -0.523829, 0.741001],
    [-0.611142, -0.433859, 0.659351], [-0.760772, -0.315149, 0.561571],
    [-0.857641, -0.170461, 0.477741], [-0.899751, -0.019511, 0.435882],
    [-0.999742, 0.020551, -0.009361], [-0.947231, 0.201732, -0.263152],
    [-0.835151, 0.363092, -0.410422], [-0.660851, 0.485903, -0.574672],
    [-0.443049, 0.575082, -0.687441], [-0.207869, 0.623102, -0.754091],
    [0.038161, 0.624792, -0.780182], [0.285888, 0.584742, -0.764501],
    [0.510621, 0.505502, -0.700142], [0.697841, 0.407482, -0.590152],
    [0.838891, 0.285882, -0.455072], [0.930921, 0.155452, -0.334051],
    [0.963161, 0.020551, -0.266221], [0.930921, -0.116528, -0.334052],
    [0.838891, -0.252069, -0.455071], [0.697841, -0.370539, -0.590151],
    [0.510621, -0.463339, -0.700142], [0.285888, -0.523039, -0.764501],
    [0.038161, -0.548819, -0.780182], [-0.207869, -0.543459, -0.754091],
    [-0.443049, -0.505509, -0.687442], [-0.660851, -0.430489, -0.574672],
    [-0.835151, -0.322969, -0.410421], [-0.947231, -0.183491, -0.263151],
    [-0.999742, -0.043511, -0.009361], [-0.852431, 0.243552, -0.467472],
    [-0.781841, 0.410933, -0.466542], [-0.642591, 0.549212, -0.535352],
    [-0.467471, 0.638531, -0.612062], [-0.272111, 0.686561, -0.672622],
    [-0.071721, 0.700141, -0.710721], [0.127431, 0.680071, -0.713502],
    [0.315721, 0.630012, -0.711862], [0.490801, 0.551602, -0.676662],
    [0.638531, 0.455072, -0.619142], [0.746061, 0.347592, -0.560612],
    [0.817441, 0.232352, -0.525542], [0.841361, 0.103091, -0.543452],
    [0.814981, -0.027001, -0.584742], [0.746061, -0.151779, -0.643032],
    [0.638531, -0.263119, -0.713501], [0.490801, -0.354519, -0.784232],
    [0.315721, -0.426219, -0.841362], [0.127431, -0.473529, -0.876792],
    [-0.071721, -0.490809, -0.884502], [-0.272111, -0.474669, -0.867902],
    [-0.467471, -0.428519, -0.771231], [-0.642591, -0.354519, -0.676661],
    [-0.781841, -0.260599, -0.573931], [-0.852431, -0.158781, -0.485901],
    [-0.525731, 0.000000, -0.850651], [-0.442285, -0.285888, -0.849931],
    [-0.295242, -0.477747, -0.827466], [-0.094031, -0.581235, -0.808214],
    [0.127431, -0.589414, -0.797711], [0.334051, -0.510629, -0.791502],
    [0.499781, -0.363091, -0.783151], [0.597961, -0.170591, -0.783111],
    [0.618671, 0.043519, -0.784231], [0.563141, 0.252069, -0.787751],
    [0.439161, 0.428519, -0.788581], [0.263591, 0.551609, -0.791532],
    [0.056121, 0.612059, -0.788881], [-0.158782, 0.604249, -0.780181],
    [-0.354052, 0.523039, -0.771231], [-0.505501, 0.379469, -0.774561],
    [-0.588881, 0.197029, -0.778741],
];

pub fn load_md2<R: Read + Seek>(mut reader: R) -> AnimatedMesh {
    let mut header_buf = [0u8; 68];
    reader.read_exact(&mut header_buf).unwrap();
    
    let header = MD2Header {
        magic: i32::from_le_bytes(header_buf[0..4].try_into().unwrap()),
        version: i32::from_le_bytes(header_buf[4..8].try_into().unwrap()),
        skin_width: i32::from_le_bytes(header_buf[8..12].try_into().unwrap()),
        skin_height: i32::from_le_bytes(header_buf[12..16].try_into().unwrap()),
        frame_size: i32::from_le_bytes(header_buf[16..20].try_into().unwrap()),
        num_skins: i32::from_le_bytes(header_buf[20..24].try_into().unwrap()),
        num_vertices: i32::from_le_bytes(header_buf[24..28].try_into().unwrap()),
        num_st: i32::from_le_bytes(header_buf[28..32].try_into().unwrap()),
        num_tris: i32::from_le_bytes(header_buf[32..36].try_into().unwrap()),
        num_glcmds: i32::from_le_bytes(header_buf[36..40].try_into().unwrap()),
        num_frames: i32::from_le_bytes(header_buf[40..44].try_into().unwrap()),
        offset_skins: i32::from_le_bytes(header_buf[44..48].try_into().unwrap()),
        offset_st: i32::from_le_bytes(header_buf[48..52].try_into().unwrap()),
        offset_tris: i32::from_le_bytes(header_buf[52..56].try_into().unwrap()),
        offset_frames: i32::from_le_bytes(header_buf[56..60].try_into().unwrap()),
        offset_glcmds: i32::from_le_bytes(header_buf[60..64].try_into().unwrap()),
        offset_end: i32::from_le_bytes(header_buf[64..68].try_into().unwrap()),
    };

    // load texture coordinates
    reader.seek(SeekFrom::Start(header.offset_st as u64)).unwrap();
    let mut st_data = Vec::new();
    for _ in 0..header.num_st {
        let mut buf = [0u8; 4];
        reader.read_exact(&mut buf).unwrap();
        let s = i16::from_le_bytes(buf[0..2].try_into().unwrap());
        let t = i16::from_le_bytes(buf[2..4].try_into().unwrap());
        st_data.push((s as f32 / header.skin_width as f32, t as f32 / header.skin_height as f32));
    }

    // load triangles
    reader.seek(SeekFrom::Start(header.offset_tris as u64)).unwrap();
    let mut tri_data = Vec::new();
    for _ in 0..header.num_tris {
        let mut buf = [0u8; 12];
        reader.read_exact(&mut buf).unwrap();
        let v_indices = [
            u16::from_le_bytes(buf[0..2].try_into().unwrap()),
            u16::from_le_bytes(buf[2..4].try_into().unwrap()),
            u16::from_le_bytes(buf[4..6].try_into().unwrap()),
        ];
        let t_indices = [
            u16::from_le_bytes(buf[6..8].try_into().unwrap()),
            u16::from_le_bytes(buf[8..10].try_into().unwrap()),
            u16::from_le_bytes(buf[10..12].try_into().unwrap()),
        ];
        tri_data.push((v_indices, t_indices));
    }

    // Build unique mapping
    let mut unique_vertex_pairs = Vec::new();
    let mut vertex_map = HashMap::new();
    let mut indices = Vec::new();

    for (v_indices, t_indices) in &tri_data {
        for i in 0..3 {
            let key = (v_indices[i], t_indices[i]);
            let idx = *vertex_map.entry(key).or_insert_with(|| {
                let id = unique_vertex_pairs.len() as u32;
                unique_vertex_pairs.push(key);
                id
            });
            indices.push(idx);
        }
    }

    let mut all_vertices = Vec::new();
    let vertices_per_frame = unique_vertex_pairs.len() as u32;

    for f in 0..header.num_frames {
        reader.seek(SeekFrom::Start((header.offset_frames + f * header.frame_size) as u64)).unwrap();
        
        let mut scale = [0.0f32; 3];
        let mut translate = [0.0f32; 3];
        let mut buf = [0u8; 4];
        for i in 0..3 {
            reader.read_exact(&mut buf).unwrap();
            scale[i] = f32::from_le_bytes(buf);
        }
        for i in 0..3 {
            reader.read_exact(&mut buf).unwrap();
            translate[i] = f32::from_le_bytes(buf);
        }
        let mut name = [0u8; 16];
        reader.read_exact(&mut name).unwrap();

        let mut raw_verts = Vec::with_capacity(header.num_vertices as usize);
        let mut raw_normals = Vec::with_capacity(header.num_vertices as usize);
        for _ in 0..header.num_vertices {
            let mut v_buf = [0u8; 4];
            reader.read_exact(&mut v_buf).unwrap();
            let x = v_buf[0] as f32 * scale[0] + translate[0];
            let y = v_buf[1] as f32 * scale[1] + translate[1];
            let z = v_buf[2] as f32 * scale[2] + translate[2];
            
            // Fix coordinate system (MD2 Z is up, we want Y up)
            raw_verts.push([x, z, -y]);
            
            let normal_idx = v_buf[3] as usize;
            if normal_idx < MD2_NORMALS.len() {
                let n = MD2_NORMALS[normal_idx];
                raw_normals.push([n[0], n[2], -n[1]]);
            } else {
                raw_normals.push([0.0, 0.0, 0.0]);
            }
        }

        for &(v_idx, t_idx) in &unique_vertex_pairs {
            let pos = raw_verts[v_idx as usize];
            let normal = raw_normals[v_idx as usize];
            let uv = st_data[t_idx as usize];
            all_vertices.push(Vertex {
                position: pos,
                tex_coords: [uv.0, uv.1],
                normal,
            });
        }
    }

    AnimatedMesh {
        all_vertices,
        indices,
        num_frames: header.num_frames as u32,
        vertices_per_frame,
        current_frame: 0.0,
    }
}
