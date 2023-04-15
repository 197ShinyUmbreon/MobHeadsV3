package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import javax.management.monitor.MonitorMBean;
import java.util.*;

public class HeadData {

    public static void initialize(){
        populateHeadMatList();
        for (MobHead mobHead:mobHeads){
            registerHead(mobHead);
        }
    }

    public static void registerHead(MobHead mobHead){
        if (!mobHeads.contains(mobHead)) mobHeads.add(0, mobHead);
        mobHeadByUUID.put(mobHead.getUuid(), mobHead);
        if (!entityTypes.contains(mobHead.getEntityType())) entityTypes.add(mobHead.getEntityType());
        if (mobHead.getName() == null) vanillaHeadUUIDs.put(mobHead.getEntityType(), mobHead.getUuid());
    }

    private static final List<MobHead> mobHeads = new ArrayList<>();
    public static List<MobHead> getMobHeads(){
        return mobHeads;
    }
    public static void addMobHead(MobHead mobHead){
        mobHeads.add(mobHead);
    }
    public static void addMobHeads(List<MobHead> mobHeads){
        for (MobHead mobHead:mobHeads){
            addMobHead(mobHead);
        }
    }
    public static Map<UUID, MobHead> mobHeadByUUID = new HashMap<>();
    public static Map<EntityType, UUID> vanillaHeadUUIDs = new HashMap<>();
    public static List<EntityType> entityTypes = new ArrayList<>();

    public static List<Material> headBlockMats = new ArrayList<>();

    public static final List<Material> playerHeadMats = Arrays.asList(
            Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD
    );

    public static final List<Material> vanillaHeadMats = Arrays.asList(
            Material.CREEPER_HEAD, Material.CREEPER_WALL_HEAD, Material.ZOMBIE_HEAD,
            Material.ZOMBIE_WALL_HEAD, Material.SKELETON_SKULL, Material.SKELETON_WALL_SKULL, Material.WITHER_SKELETON_SKULL,
            Material.WITHER_SKELETON_WALL_SKULL, Material.DRAGON_HEAD, Material.DRAGON_WALL_HEAD
    );

    public static void populateHeadMatList(){
        List<Material> headMats = new ArrayList<>();
        headMats.addAll(playerHeadMats);
        headMats.addAll(vanillaHeadMats);
        headBlockMats = headMats;
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

    public static List<EntityType> entityTypesWithVariants = Arrays.asList(
            EntityType.RABBIT, EntityType.AXOLOTL, EntityType.CAT, EntityType.HORSE, EntityType.LLAMA,
            EntityType.TRADER_LLAMA, EntityType.PARROT, EntityType.FOX, EntityType.PANDA, EntityType.SHEEP,
            EntityType.MUSHROOM_COW, EntityType.FROG, EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER
    );

    public static List<EntityType> zombifiedTypes = Arrays.asList(
            EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN,
            EntityType.ZOGLIN, EntityType.DROWNED, EntityType.PHANTOM
    );





}
