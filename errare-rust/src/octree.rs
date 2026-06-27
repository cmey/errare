use glam::Vec3;
use crate::frustum::{Frustum, Intersection};

#[derive(Clone, Debug, PartialEq)]
pub struct Triangle {
    pub p0: Vec3,
    pub p1: Vec3,
    pub p2: Vec3,
    pub normal: Vec3,
}

impl Triangle {
    /// Returns true if the query point (x, z) projects inside this triangle on the XZ plane,
    /// and calculates the exact height Y of the triangle surface at (x, z).
    pub fn get_height_at(&self, x: f32, z: f32) -> Option<f32> {
        // Form 2D vectors for barycentric coordinates on XZ plane
        let v0 = Vec2::new(self.p2.x - self.p0.x, self.p2.z - self.p0.z);
        let v1 = Vec2::new(self.p1.x - self.p0.x, self.p1.z - self.p0.z);
        let v2 = Vec2::new(x - self.p0.x, z - self.p0.z);

        let dot00 = v0.dot(v0);
        let dot01 = v0.dot(v1);
        let dot02 = v0.dot(v2);
        let dot11 = v1.dot(v1);
        let dot12 = v1.dot(v2);

        let denom = dot00 * dot11 - dot01 * dot01;
        if denom.abs() < 1e-6 {
            return None;
        }

        let inv_denom = 1.0 / denom;
        let u = (dot11 * dot02 - dot01 * dot12) * inv_denom;
        let v = (dot00 * dot12 - dot01 * dot02) * inv_denom;

        // u >= 0, v >= 0, u + v <= 1 with tiny epsilon tolerance
        let eps = 1e-4;
        if u >= -eps && v >= -eps && (u + v) <= 1.0 + eps {
            // Solve plane equation using barycentric coordinates
            let y = self.p0.y + u * (self.p2.y - self.p0.y) + v * (self.p1.y - self.p0.y);
            Some(y)
        } else {
            None
        }
    }

    /// Checks for intersection with an AABox using the 13 separating axes of SAT.
    pub fn intersects_aabb(&self, aabb: &AABox) -> bool {
        // 1. Translate vertices relative to the AABB center so the box is at the origin
        let center = (aabb.min + aabb.max) * 0.5;
        let v0 = self.p0 - center;
        let v1 = self.p1 - center;
        let v2 = self.p2 - center;

        // Compute box half-extents
        let h = (aabb.max - aabb.min) * 0.5;

        // 2. Test standard coordinate axes (AABB face normals)
        // Axis X
        let mut min = v0.x.min(v1.x).min(v2.x);
        let mut max = v0.x.max(v1.x).max(v2.x);
        if min > h.x || max < -h.x {
            return false;
        }

        // Axis Y
        min = v0.y.min(v1.y).min(v2.y);
        max = v0.y.max(v1.y).max(v2.y);
        if min > h.y || max < -h.y {
            return false;
        }

        // Axis Z
        min = v0.z.min(v1.z).min(v2.z);
        max = v0.z.max(v1.z).max(v2.z);
        if min > h.z || max < -h.z {
            return false;
        }

        // 3. Compute triangle edge vectors
        let e0 = v1 - v0;
        let e1 = v2 - v1;
        let e2 = v0 - v2;

        // 4. Test 9 cross-product axes: Edge_i x Unit_j
        // --- Edge 0 ---
        let fex0 = e0.x.abs();
        let fey0 = e0.y.abs();
        let fez0 = e0.z.abs();

        // Axis a00 = e0 x u0 = (0, -e0.z, e0.y)
        let mut p0 = e0.z * v0.y - e0.y * v0.z;
        let mut p2 = e0.z * v2.y - e0.y * v2.z;
        let mut rad = fez0 * h.y + fey0 * h.z;
        if p0.min(p2) > rad || p0.max(p2) < -rad {
            return false;
        }

        // Axis a01 = e0 x u1 = (e0.z, 0, -e0.x)
        p0 = -e0.z * v0.x + e0.x * v0.z;
        p2 = -e0.z * v2.x + e0.x * v2.z;
        rad = fez0 * h.x + fex0 * h.z;
        if p0.min(p2) > rad || p0.max(p2) < -rad {
            return false;
        }

        // Axis a02 = e0 x u2 = (-e0.y, e0.x, 0)
        let p1 = e0.y * v1.x - e0.x * v1.y;
        p2 = e0.y * v2.x - e0.x * v2.y;
        rad = fey0 * h.x + fex0 * h.y;
        if p1.min(p2) > rad || p1.max(p2) < -rad {
            return false;
        }

        // --- Edge 1 ---
        let fex1 = e1.x.abs();
        let fey1 = e1.y.abs();
        let fez1 = e1.z.abs();

        // Axis a10 = e1 x u0 = (0, -e1.z, e1.y)
        p0 = e1.z * v0.y - e1.y * v0.z;
        p2 = e1.z * v2.y - e1.y * v2.z;
        rad = fez1 * h.y + fey1 * h.z;
        if p0.min(p2) > rad || p0.max(p2) < -rad {
            return false;
        }

        // Axis a11 = e1 x u1 = (e1.z, 0, -e1.x)
        p0 = -e1.z * v0.x + e1.x * v0.z;
        p2 = -e1.z * v2.x + e1.x * v2.z;
        rad = fez1 * h.x + fex1 * h.z;
        if p0.min(p2) > rad || p0.max(p2) < -rad {
            return false;
        }

        // Axis a12 = e1 x u2 = (-e1.y, e1.x, 0)
        p0 = e1.y * v0.x - e1.x * v0.y;
        let mut p1 = e1.y * v1.x - e1.x * v1.y;
        rad = fey1 * h.x + fex1 * h.y;
        if p0.min(p1) > rad || p0.max(p1) < -rad {
            return false;
        }

        // --- Edge 2 ---
        let fex2 = e2.x.abs();
        let fey2 = e2.y.abs();
        let fez2 = e2.z.abs();

        // Axis a20 = e2 x u0 = (0, -e2.z, e2.y)
        p0 = e2.z * v0.y - e2.y * v0.z;
        p1 = e2.z * v1.y - e2.y * v1.z;
        rad = fez2 * h.y + fey2 * h.z;
        if p0.min(p1) > rad || p0.max(p1) < -rad {
            return false;
        }

        // Axis a21 = e2 x u1 = (e2.z, 0, -e2.x)
        p0 = -e2.z * v0.x + e2.x * v0.z;
        p1 = -e2.z * v1.x + e2.x * v1.z;
        rad = fez2 * h.x + fex2 * h.z;
        if p0.min(p1) > rad || p0.max(p1) < -rad {
            return false;
        }

        // Axis a22 = e2 x u2 = (-e2.y, e2.x, 0)
        p1 = e2.y * v1.x - e2.x * v1.y;
        p2 = e2.y * v2.x - e2.x * v2.y;
        rad = fey2 * h.x + fex2 * h.y;
        if p1.min(p2) > rad || p1.max(p2) < -rad {
            return false;
        }

        // 5. Test intersection of AABB with the plane of the triangle
        let normal = e0.cross(e1);
        let d = -normal.dot(v0);
        let rad = h.x * normal.x.abs() + h.y * normal.y.abs() + h.z * normal.z.abs();

        if d.abs() > rad {
            return false;
        }

        true // Overlaps along all 13 axes
    }
}

use glam::Vec2;

#[derive(Clone, Copy, Debug)]
pub struct AABox {
    pub min: Vec3,
    pub max: Vec3,
}

impl AABox {
    pub fn new(min: Vec3, max: Vec3) -> Self {
        Self { min, max }
    }

    pub fn center(&self) -> Vec3 {
        (self.min + self.max) * 0.5
    }

    pub fn radius(&self) -> f32 {
        (self.max - self.min).length() * 0.5
    }
}

pub struct Octree<T> {
    pub bounding_box: AABox,
    pub children: Option<Box<[Octree<T>; 8]>>,
    pub content: Vec<T>,
}

impl<T> Octree<T> {
    pub fn new(bounding_box: AABox, depth: u32) -> Self {
        let mut children = None;
        if depth > 0 {
            let mid = bounding_box.center();
            let min = bounding_box.min;
            let max = bounding_box.max;

            children = Some(Box::new([
                // Bottom-Front-Left
                Octree::new(AABox::new(min, mid), depth - 1),
                // Bottom-Front-Right
                Octree::new(AABox::new(Vec3::new(mid.x, min.y, min.z), Vec3::new(max.x, mid.y, mid.z)), depth - 1),
                // Bottom-Back-Left
                Octree::new(AABox::new(Vec3::new(min.x, min.y, mid.z), Vec3::new(mid.x, mid.y, max.z)), depth - 1),
                // Bottom-Back-Right
                Octree::new(AABox::new(Vec3::new(mid.x, min.y, mid.z), Vec3::new(max.x, mid.y, max.z)), depth - 1),
                // Top-Front-Left
                Octree::new(AABox::new(Vec3::new(min.x, mid.y, min.z), Vec3::new(mid.x, max.y, mid.z)), depth - 1),
                // Top-Front-Right
                Octree::new(AABox::new(Vec3::new(mid.x, mid.y, min.z), Vec3::new(max.x, max.y, mid.z)), depth - 1),
                // Top-Back-Left
                Octree::new(AABox::new(Vec3::new(min.x, mid.y, mid.z), Vec3::new(mid.x, max.y, max.z)), depth - 1),
                // Top-Back-Right
                Octree::new(AABox::new(mid, max), depth - 1),
            ]));
        }

        Self {
            bounding_box,
            children,
            content: Vec::new(),
        }
    }

    pub fn insert(&mut self, item: T, position: Vec3) -> bool {
        if !self.contains(position) {
            return false;
        }

        if let Some(ref mut children) = self.children {
            let mut moved_item = Some(item);
            for child in children.iter_mut() {
                if child.contains(position) {
                    if let Some(actual_item) = moved_item.take() {
                        if child.insert(actual_item, position) {
                            return true;
                        }
                    }
                }
            }
            if let Some(actual_item) = moved_item {
                self.content.push(actual_item);
                return true;
            }
        } else {
            self.content.push(item);
            return true;
        }

        false
    }

    fn contains(&self, p: Vec3) -> bool {
        p.x >= self.bounding_box.min.x && p.x <= self.bounding_box.max.x &&
        p.y >= self.bounding_box.min.y && p.y <= self.bounding_box.max.y &&
        p.z >= self.bounding_box.min.z && p.z <= self.bounding_box.max.z
    }

    pub fn query_visible<'a>(&'a self, frustum: &Frustum, visible_items: &mut Vec<&'a T>) {
        let intersection = frustum.aabb_in_frustum(self.bounding_box.min, self.bounding_box.max);
        
        match intersection {
            Intersection::Outside => return,
            Intersection::Inside => {
                // Everything in this node and children is visible
                self.add_all_recursive(visible_items);
            }
            Intersection::Intersect => {
                // Check this node's content
                for item in &self.content {
                    visible_items.push(item);
                }
                // Recurse into children
                if let Some(ref children) = self.children {
                    for child in children.iter() {
                        child.query_visible(frustum, visible_items);
                    }
                }
            }
        }
    }

    fn add_all_recursive<'a>(&'a self, visible_items: &mut Vec<&'a T>) {
        for item in &self.content {
            visible_items.push(item);
        }
        if let Some(ref children) = self.children {
            for child in children.iter() {
                child.add_all_recursive(visible_items);
            }
        }
    }

    pub fn query_aabb<'a>(&'a self, query_box: &AABox, items: &mut Vec<&'a T>) {
        if !self.intersects_aabb(query_box) {
            return;
        }
        
        for item in &self.content {
            items.push(item);
        }
        
        if let Some(ref children) = self.children {
            for child in children.iter() {
                child.query_aabb(query_box, items);
            }
        }
    }

    fn intersects_aabb(&self, other: &AABox) -> bool {
        self.bounding_box.min.x <= other.max.x && self.bounding_box.max.x >= other.min.x &&
        self.bounding_box.min.y <= other.max.y && self.bounding_box.max.y >= other.min.y &&
        self.bounding_box.min.z <= other.max.z && self.bounding_box.max.z >= other.min.z
    }
}
