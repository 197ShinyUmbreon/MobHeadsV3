package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HeadData {

    public static final Map<UUID, EntityType> entityTypeLookupMap = new HashMap<>();
    public static final Map<UUID, String> variantLookupMap = new HashMap<>();
    public static final Map<UUID, ItemStack> headItemLookupMap = new HashMap<>();
    public static final Map<String, UUID> uuidFromNameLookupMap = new HashMap<>();

    public static final List<Material> headBlockMats = Arrays.asList(
            Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD, Material.CREEPER_HEAD, Material.CREEPER_WALL_HEAD, Material.ZOMBIE_HEAD,
            Material.ZOMBIE_WALL_HEAD, Material.SKELETON_SKULL, Material.SKELETON_WALL_SKULL, Material.WITHER_SKELETON_SKULL,
            Material.WITHER_SKELETON_WALL_SKULL, Material.DRAGON_HEAD, Material.DRAGON_WALL_HEAD
    );

    public static final List<Material> playerHeadMats = Arrays.asList(
            Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD
    );

    public static Map<Material, EntityType> vanillaMatEntTypeMap(){
        Map<Material, EntityType> map = new HashMap<>();
        map.put(Material.ZOMBIE_HEAD, EntityType.ZOMBIE);
        map.put(Material.CREEPER_HEAD, EntityType.CREEPER);
        map.put(Material.SKELETON_SKULL, EntityType.SKELETON);
        map.put(Material.WITHER_SKELETON_SKULL, EntityType.WITHER_SKELETON);
        map.put(Material.DRAGON_HEAD, EntityType.ENDER_DRAGON);
        map.put(Material.ZOMBIE_WALL_HEAD, EntityType.ZOMBIE);
        map.put(Material.CREEPER_WALL_HEAD, EntityType.CREEPER);
        map.put(Material.SKELETON_WALL_SKULL, EntityType.SKELETON);
        map.put(Material.WITHER_SKELETON_WALL_SKULL, EntityType.WITHER_SKELETON);
        map.put(Material.DRAGON_WALL_HEAD, EntityType.ENDER_DRAGON);
        return map;
    }





}
