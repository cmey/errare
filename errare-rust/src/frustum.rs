use glam::{Vec3, Vec4};

#[derive(Debug, Clone, Copy)]
pub struct Plane {
    pub normal: Vec3,
    pub distance: f32,
}

impl Plane {
    pub fn new(normal: Vec3, distance: f32) -> Self {
        let length = normal.length();
        Self {
            normal: normal / length,
            distance: distance / length,
        }
    }

    pub fn distance_to_point(&self, point: Vec3) -> f32 {
        self.normal.dot(point) + self.distance
    }
}

pub struct Frustum {
    pub planes: [Plane; 6],
}

#[derive(Debug, PartialEq)]
pub enum Intersection {
    Outside,
    Inside,
    Intersect,
}

impl Frustum {
    pub fn from_matrix(matrix: glam::Mat4) -> Self {
        let mut planes = [Plane { normal: Vec3::ZERO, distance: 0.0 }; 6];
        
        // Extract planes from the view-projection matrix
        // Columns of the matrix are used to extract the 6 clipping planes
        let row4 = matrix.row(3);
        let row1 = matrix.row(0);
        let row2 = matrix.row(1);
        let row3 = matrix.row(2);

        // Left
        planes[0] = Self::make_plane(row4 + row1);
        // Right
        planes[1] = Self::make_plane(row4 - row1);
        // Bottom
        planes[2] = Self::make_plane(row4 + row2);
        // Top
        planes[3] = Self::make_plane(row4 - row2);
        // Near
        planes[4] = Self::make_plane(row4 + row3);
        // Far
        planes[5] = Self::make_plane(row4 - row3);

        Self { planes }
    }

    fn make_plane(v: Vec4) -> Plane {
        Plane::new(Vec3::new(v.x, v.y, v.z), v.w)
    }

    pub fn sphere_in_frustum(&self, center: Vec3, radius: f32) -> Intersection {
        let mut fully_inside = true;
        for plane in &self.planes {
            let dist = plane.distance_to_point(center);
            if dist <= -radius {
                return Intersection::Outside;
            }
            if dist < radius {
                fully_inside = false;
            }
        }
        if fully_inside {
            Intersection::Inside
        } else {
            Intersection::Intersect
        }
    }

    pub fn aabb_in_frustum(&self, min: Vec3, max: Vec3) -> Intersection {
        let mut fully_inside = true;
        for plane in &self.planes {
            let mut p = min;
            if plane.normal.x >= 0.0 { p.x = max.x; }
            if plane.normal.y >= 0.0 { p.y = max.y; }
            if plane.normal.z >= 0.0 { p.z = max.z; }

            let mut n = max;
            if plane.normal.x >= 0.0 { n.x = min.x; }
            if plane.normal.y >= 0.0 { n.y = min.y; }
            if plane.normal.z >= 0.0 { n.z = min.z; }

            if plane.distance_to_point(n) < 0.0 {
                return Intersection::Outside;
            }
            if plane.distance_to_point(p) < 0.0 {
                fully_inside = false;
            }
        }
        if fully_inside {
            Intersection::Inside
        } else {
            Intersection::Intersect
        }
    }
}
