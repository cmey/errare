use glam::Vec3;
use crate::mesh::AnimatedMesh;
use crate::octree::{Octree, Triangle, AABox};

#[derive(Clone)]
pub struct Item {
    pub id: String,
    pub name: String,
    pub icon_path: String,
}

pub struct Inventory {
    pub bags: Vec<Vec<Option<Item>>>, // 9 bags of 15 slots
    pub equipment: Vec<Option<Item>>, // 20 slots
    pub show_inventory: bool,
    pub show_equipment: bool,
}

impl Inventory {
    pub fn new() -> Self {
        let mut bags = Vec::new();
        for _ in 0..9 {
            let mut bag = Vec::new();
            for _ in 0..15 {
                bag.push(None);
            }
            bags.push(bag);
        }

        Self {
            bags,
            equipment: vec![None; 20],
            show_inventory: false,
            show_equipment: false,
        }
    }
}

pub struct Player {
    pub position: Vec3,
    pub rotation_y: f32,
    pub animated_mesh: AnimatedMesh,
    
    // Stats from Hero.java / GameCharacter.java
    pub name: String,
    pub life: f32,
    pub max_life: f32,
    pub mana: f32,
    pub max_mana: f32,
    pub experience: f32,
    pub level: i32,

    pub inventory: Inventory,
    pub velocity_y: f32,
    pub in_the_air: bool,
}

impl Player {
    pub fn new(name: &str, animated_mesh: AnimatedMesh) -> Self {
        let mut inv = Inventory::new();
        // Add a starting item (Sword 1501)
        inv.bags[0][0] = Some(Item {
            id: "1501".to_string(),
            name: "Beginner Sword".to_string(),
            icon_path: "assets/gui/1501.png".to_string(),
        });
        // Add a potion (0101)
        inv.bags[0][1] = Some(Item {
            id: "0101".to_string(),
            name: "Health Potion".to_string(),
            icon_path: "assets/gui/0101.png".to_string(),
        });

        Self {
            position: Vec3::new(0.0, 0.0, 0.0),
            rotation_y: 0.0,
            animated_mesh,
            name: name.to_string(),
            life: 100.0,
            max_life: 100.0,
            mana: 50.0,
            max_mana: 50.0,
            experience: 0.0,
            level: 1,
            inventory: inv,
            velocity_y: 0.0,
            in_the_air: true,
        }
    }

    pub fn update(&mut self, dt: f32) {
        // Animation speed (e.g. 10 frames per second)
        self.animated_mesh.current_frame += dt * 10.0;
        if self.animated_mesh.current_frame >= self.animated_mesh.num_frames as f32 {
            self.animated_mesh.current_frame = 0.0;
        }
    }

    pub fn update_physics(
        &mut self,
        octree: &Octree<Triangle>,
        desired_move_horizontal: Vec3,
        jump_requested: bool,
        dt: f32,
    ) {
        // Step 1: Evaluate jump
        if jump_requested && !self.in_the_air {
            self.velocity_y = 12.0;
            self.in_the_air = true;
        }

        // Step 2: Apply horizontal slide movement
        let mut move_vec = desired_move_horizontal;
        for _ in 0..3 {
            if move_vec.length_squared() < 1e-6 {
                break;
            }

            let old_pos = self.position;
            self.position += move_vec;

            // Define the player's bounding box around their position
            let player_box = AABox::new(
                self.position + Vec3::new(-0.5, 0.0, -0.5),
                self.position + Vec3::new(0.5, 1.8, 0.5),
            );

            // Query octree for triangles overlapping the player box
            let mut overlapping = Vec::new();
            octree.query_aabb(&player_box, &mut overlapping);

            let mut intersected = false;
            let mut first_collision_tri = None;

            for tri in overlapping {
                // Ignore terrain ground layers to allow smooth walking / steep threshold (normal.y < 0.7)
                if tri.normal.y < 0.7 {
                    if tri.intersects_aabb(&player_box) {
                        intersected = true;
                        first_collision_tri = Some(tri);
                        break;
                    }
                }
            }

            if intersected {
                // Return to old position and slide along the wall normal
                self.position = old_pos;
                if let Some(tri) = first_collision_tri {
                    let n = tri.normal;
                    let dot = move_vec.dot(n);
                    if dot < 0.0 {
                        // Projected sliding vector
                        move_vec = move_vec - dot * n;
                        move_vec.y = 0.0; // Restrict slide to the horizontal plane
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        // Step 3: Apply gravity downward step with fast-fall steps
        self.velocity_y -= 40.0 * dt;
        let dy = self.velocity_y * dt;

        if dy > 0.0 {
            self.position.y += dy;
            self.in_the_air = true;
        } else {
            // Falling step. Query the column under the player.
            let new_y = self.position.y + dy;
            let mut ground_height: Option<f32> = None;
            let mut found_ground = false;

            let column_box = AABox::new(
                self.position + Vec3::new(-0.5, dy - 2.0, -0.5),
                self.position + Vec3::new(0.5, 2.0, 0.5),
            );

            let mut column_tris = Vec::new();
            octree.query_aabb(&column_box, &mut column_tris);

            for tri in column_tris {
                if tri.normal.y >= 0.5 { // Valid walkable surface
                    if let Some(h) = tri.get_height_at(self.position.x, self.position.z) {
                        // Ensure the surface is below the current feet elevation plus a small step-up margin
                        if h <= self.position.y + 0.8 {
                            if ground_height.is_none() || h > ground_height.unwrap() {
                                ground_height = Some(h);
                            }
                        }
                    }
                }
            }

            if let Some(g_h) = ground_height {
                // If running on ground, allow snapping to slightly lower ground (riding a slope down smoothly)
                let snap_margin = if !self.in_the_air { 0.3 } else { 0.0 };
                if new_y <= g_h + snap_margin {
                    self.position.y = g_h;
                    self.velocity_y = 0.0;
                    self.in_the_air = false;
                    found_ground = true;
                }
            }

            if !found_ground {
                self.position.y = new_y;
                self.in_the_air = true;
            }
        }
    }

    pub fn move_player(&mut self, forward: f32, right: f32, dt: f32) {
        let speed = 20.0;
        let rotation_speed = 3.0;

        self.rotation_y += right * rotation_speed * dt;
        
        let direction = Vec3::new(
            self.rotation_y.sin(),
            0.0,
            self.rotation_y.cos(),
        );
        self.position += direction * forward * speed * dt;
    }

    pub fn receive_damage(&mut self, amount: f32) {
        self.life -= amount;
        if self.life < 0.0 {
            self.life = 0.0;
        }
    }

    pub fn add_experience(&mut self, amount: f32) {
        self.experience += amount;
        if self.experience >= (self.level * 100) as f32 {
            self.level_up();
        }
    }

    fn level_up(&mut self) {
        self.level += 1;
        self.max_life += 20.0;
        self.life = self.max_life;
        self.max_mana += 10.0;
        self.mana = self.max_mana;
        self.experience = 0.0;
        println!("Level up! {} is now level {}", self.name, self.level);
    }
}
