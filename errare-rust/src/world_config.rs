use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize)]
#[serde(rename_all = "lowercase")]
pub struct WorldConfig {
    #[serde(rename = "object", default)]
    pub objects: Vec<WorldObject>,
}

#[derive(Debug, Serialize, Deserialize)]
#[serde(rename_all = "lowercase")]
pub struct WorldObject {
    #[serde(rename = "@name")]
    pub name: String,
    #[serde(rename = "@type")]
    pub obj_type: String,
    #[serde(rename = "@mainchar", default)]
    pub mainchar: Option<bool>,
    pub physics: Option<PhysicsConfig>,
    pub graphics: GraphicsConfig,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct PhysicsConfig {
    pub x: f32,
    pub y: f32,
    pub z: f32,
    pub xsize: f32,
    pub ysize: f32,
    pub zsize: f32,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct GraphicsConfig {
    pub path: String,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct KeysConfig {
    pub gui: GuiKeys,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct GuiKeys {
    pub chat: u32,
    pub equipement: u32,
    pub inventory: u32,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct InventoryConfig {
    pub position_x: i32,
    pub position_y: i32,
    pub open: bool,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct OptionsConfig {
    pub difficulty: u32,
    pub gamespeed: u32,
    pub mousespeed: u32,
    pub loudness: u32,
    pub screenwidth: u32,
    pub screenheight: u32,
    pub bitcolor: u32,
    pub frequency: u32,
    pub gamma: u32,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct EquipementConfig {
    pub position_x: i32,
    pub position_y: i32,
    pub open: bool,
}

#[cfg(test)]
mod tests {
    use super::*;
    use quick_xml::de::from_str;

    #[test]
    fn test_parse_keys_xml() {
        let xml = r#"
        <keys>
            <gui>
                <chat>67</chat>
                <equipement>69</equipement>
                <inventory>73</inventory>
            </gui>
        </keys>
        "#;
        let config: KeysConfig = from_str(xml).unwrap();
        assert_eq!(config.gui.chat, 67);
        assert_eq!(config.gui.equipement, 69);
        assert_eq!(config.gui.inventory, 73);
    }

    #[test]
    fn test_parse_options_xml() {
        let xml = r#"
        <options>
            <difficulty>2</difficulty>
            <gamespeed>2</gamespeed>
            <mousespeed>100</mousespeed>
            <loudness>100</loudness>
            <screenwidth>1024</screenwidth>
            <screenheight>768</screenheight>
            <bitcolor>32</bitcolor>
            <frequency>70</frequency>
            <gamma>3</gamma>
        </options>
        "#;
        let config: OptionsConfig = from_str(xml).unwrap();
        assert_eq!(config.gamespeed, 2);
        assert_eq!(config.screenwidth, 1024);
        assert_eq!(config.gamma, 3);
    }

    #[test]
    fn test_parse_world_xml() {
        let xml = r#"
        <world>
            <object name="001" type="character" mainchar="true">
                <physics>
                    <x>0</x>
                    <y>100</y>
                    <z>0</z>
                    <xsize>30</xsize>
                    <ysize>30</ysize>
                    <zsize>30</zsize>
                </physics>
                <graphics>
                    <path>data/md2/hobgoblin/hobgoblin.md2</path>
                </graphics>
            </object>
            <object name="002" type="setting">
                <graphics>
                    <path>pathtoterrain.3ds</path>
                </graphics>
            </object>
        </world>
        "#;
        let world: WorldConfig = from_str(xml).unwrap();
        assert_eq!(world.objects.len(), 2);
        assert_eq!(world.objects[0].name, "001");
        assert_eq!(world.objects[0].obj_type, "character");
        assert_eq!(world.objects[0].graphics.path, "data/md2/hobgoblin/hobgoblin.md2");
    }
}
