package io.github.shinyumbreon197.mobheadsv3.data;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class Data {

    public static final List<Material> playerHeadMats = Arrays.asList(
            Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD
    );

    public static final List<Material> vanillaHeadMats = Arrays.asList(
            Material.CREEPER_HEAD, Material.CREEPER_WALL_HEAD, Material.ZOMBIE_HEAD,
            Material.ZOMBIE_WALL_HEAD, Material.SKELETON_SKULL, Material.SKELETON_WALL_SKULL, Material.WITHER_SKELETON_SKULL,
            Material.WITHER_SKELETON_WALL_SKULL, Material.DRAGON_HEAD, Material.DRAGON_WALL_HEAD, Material.PIGLIN_HEAD,
            Material.PIGLIN_WALL_HEAD
    );

    private static List<Material> headBlockMats;
    public static List<Material> getHeadBlockMats(){
        if (headBlockMats != null)return headBlockMats;
        List<Material> list = new ArrayList<>();
        list.addAll(playerHeadMats);
        list.addAll(vanillaHeadMats);
        headBlockMats = list;
        return getHeadBlockMats();
    }

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

    public static Map<EntityType, ItemStack> vanillaHeadMap(){
        Map<EntityType, ItemStack> map = new HashMap<>();
        map.put(EntityType.ZOMBIE, new ItemStack(Material.ZOMBIE_HEAD));
        map.put(EntityType.SKELETON, new ItemStack(Material.SKELETON_SKULL));
        map.put(EntityType.WITHER_SKELETON, new ItemStack(Material.WITHER_SKELETON_SKULL));
        map.put(EntityType.CREEPER, new ItemStack(Material.CREEPER_HEAD));
        map.put(EntityType.ENDER_DRAGON, new ItemStack(Material.DRAGON_HEAD));
        return map;
    }

    private static List<Material> foodMats;
    public static List<Material> getFoodMats(){
        if (foodMats != null) return foodMats;
        foodMats = buildFoodMats();
        return getFoodMats();
    }
    private static List<Material> buildFoodMats(){
        List<Material> list = new ArrayList<>();
        for (Material material:Material.values()){
            if (material.isEdible()) list.add(material);
        }
        return list;
    }


}
