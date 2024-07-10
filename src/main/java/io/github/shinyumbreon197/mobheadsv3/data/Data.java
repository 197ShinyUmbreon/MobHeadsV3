package io.github.shinyumbreon197.mobheadsv3.data;

import com.sun.jna.platform.unix.solaris.LibKstat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
        map.put(Material.PIGLIN_WALL_HEAD, EntityType.PIGLIN);
        map.put(Material.PIGLIN_HEAD, EntityType.PIGLIN);
        return map;
    }

    public static Map<EntityType, ItemStack> vanillaHeadMap(){
        Map<EntityType, ItemStack> map = new HashMap<>();
        map.put(EntityType.ZOMBIE, new ItemStack(Material.ZOMBIE_HEAD));
        map.put(EntityType.SKELETON, new ItemStack(Material.SKELETON_SKULL));
        map.put(EntityType.WITHER_SKELETON, new ItemStack(Material.WITHER_SKELETON_SKULL));
        map.put(EntityType.CREEPER, new ItemStack(Material.CREEPER_HEAD));
        map.put(EntityType.ENDER_DRAGON, new ItemStack(Material.DRAGON_HEAD));
        map.put(EntityType.PIGLIN, new ItemStack(Material.PIGLIN_HEAD));
        return map;
    }

    public static final Set<EntityType> vanillaHeadEntTypes = Set.of(
            EntityType.CREEPER, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER_SKELETON,
            EntityType.ENDER_DRAGON, EntityType.PIGLIN
    );

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
    private static Map<String,Wolf.Variant> wolfVariantMap;
    private static Map<String,Wolf.Variant> buildWolfVariantMap(){
        Map<String,Wolf.Variant> map = new HashMap<>();
        map.put("minecraft:pale", Wolf.Variant.PALE);
        map.put("minecraft:spotted", Wolf.Variant.SPOTTED);
        map.put("minecraft:snowy", Wolf.Variant.SNOWY);
        map.put("minecraft:black", Wolf.Variant.BLACK);
        map.put("minecraft:ashen", Wolf.Variant.ASHEN);
        map.put("minecraft:rusty", Wolf.Variant.RUSTY);
        map.put("minecraft:woods", Wolf.Variant.WOODS);
        map.put("minecraft:chestnut", Wolf.Variant.CHESTNUT);
        map.put("minecraft:striped", Wolf.Variant.STRIPED);
        return map;
    }
    public static Map<String,Wolf.Variant> getWolfVariantMap(){
        if (wolfVariantMap != null)return wolfVariantMap;
        wolfVariantMap = buildWolfVariantMap();
        return wolfVariantMap;
    }
    private static Map<EntityType,Sound> entityTypeNoteblockSoundMap;
    private static void buildEntityTypeNoteblockSoundMap(){
        Map<EntityType,Sound> map = new HashMap<>();

        map.put(EntityType.PLAYER, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

        map.put(EntityType.PIG, Sound.ENTITY_PIG_AMBIENT);
        map.put(EntityType.CHICKEN, Sound.ENTITY_CHICKEN_AMBIENT);
        map.put(EntityType.COW, Sound.ENTITY_COW_AMBIENT);
        map.put(EntityType.MOOSHROOM, Sound.ENTITY_MOOSHROOM_CONVERT);
        map.put(EntityType.SHEEP, Sound.ENTITY_SHEEP_AMBIENT);
        map.put(EntityType.WOLF, Sound.ENTITY_WOLF_AMBIENT);
        map.put(EntityType.ALLAY, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM);
        map.put(EntityType.SNOW_GOLEM, Sound.ENTITY_SNOW_GOLEM_AMBIENT);
        map.put(EntityType.IRON_GOLEM, Sound.ENTITY_IRON_GOLEM_REPAIR);
        map.put(EntityType.HORSE, Sound.ENTITY_HORSE_AMBIENT);
        map.put(EntityType.DONKEY, Sound.ENTITY_DONKEY_AMBIENT);
        map.put(EntityType.MULE, Sound.ENTITY_MULE_AMBIENT);
        map.put(EntityType.SKELETON_HORSE, Sound.ENTITY_SKELETON_HORSE_AMBIENT);
        map.put(EntityType.ZOMBIE_HORSE, Sound.ENTITY_ZOMBIE_HORSE_AMBIENT);
        map.put(EntityType.LLAMA, Sound.ENTITY_LLAMA_AMBIENT);
        map.put(EntityType.TRADER_LLAMA, Sound.ENTITY_LLAMA_AMBIENT);
        map.put(EntityType.BEE, Sound.ENTITY_BEE_POLLINATE);
        map.put(EntityType.ARMADILLO, Sound.ENTITY_ARMADILLO_AMBIENT);
        map.put(EntityType.BAT, Sound.ENTITY_BAT_TAKEOFF);
        map.put(EntityType.RABBIT, Sound.ENTITY_RABBIT_AMBIENT);
        map.put(EntityType.FOX, Sound.ENTITY_FOX_AMBIENT);
        map.put(EntityType.GOAT, Sound.ENTITY_GOAT_AMBIENT);
        map.put(EntityType.CAT, Sound.ENTITY_CAT_STRAY_AMBIENT);
        map.put(EntityType.OCELOT, Sound.ENTITY_OCELOT_AMBIENT);
        map.put(EntityType.PARROT, Sound.ENTITY_PARROT_AMBIENT);
        map.put(EntityType.CAMEL, Sound.ENTITY_CAMEL_AMBIENT);
        map.put(EntityType.SNIFFER, Sound.ENTITY_SNIFFER_IDLE);
        map.put(EntityType.POLAR_BEAR, Sound.ENTITY_POLAR_BEAR_AMBIENT);
        map.put(EntityType.PANDA, Sound.ENTITY_PANDA_AMBIENT);
        map.put(EntityType.VILLAGER, Sound.ENTITY_VILLAGER_AMBIENT);
        map.put(EntityType.WANDERING_TRADER, Sound.ENTITY_WANDERING_TRADER_AMBIENT);

        map.put(EntityType.COD, Sound.ENTITY_COD_FLOP);
        map.put(EntityType.SALMON, Sound.ENTITY_SALMON_FLOP);
        map.put(EntityType.PUFFERFISH, Sound.ENTITY_PUFFER_FISH_BLOW_UP);
        map.put(EntityType.TROPICAL_FISH, Sound.ENTITY_TROPICAL_FISH_FLOP);
        map.put(EntityType.SQUID, Sound.ENTITY_SQUID_SQUIRT);
        map.put(EntityType.GLOW_SQUID, Sound.ENTITY_GLOW_SQUID_SQUIRT);
        map.put(EntityType.DOLPHIN, Sound.ENTITY_DOLPHIN_PLAY);
        map.put(EntityType.TURTLE, Sound.ENTITY_TURTLE_AMBIENT_LAND);
        map.put(EntityType.TADPOLE, Sound.ENTITY_TADPOLE_FLOP);
        map.put(EntityType.FROG, Sound.ENTITY_FROG_AMBIENT);
        map.put(EntityType.AXOLOTL, Sound.ENTITY_AXOLOTL_IDLE_AIR);
        map.put(EntityType.GUARDIAN, Sound.ENTITY_GUARDIAN_AMBIENT);

        //Creeper Excluded
        //Skeleton Excluded
        map.put(EntityType.STRAY, Sound.ENTITY_STRAY_AMBIENT);
        map.put(EntityType.BOGGED, Sound.ENTITY_BOGGED_AMBIENT);
        //Zombie Excluded
        map.put(EntityType.DROWNED, Sound.ENTITY_DROWNED_AMBIENT);
        map.put(EntityType.HUSK, Sound.ENTITY_HUSK_AMBIENT);
        map.put(EntityType.ZOMBIE_VILLAGER, Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT);
        map.put(EntityType.SPIDER, Sound.ENTITY_SPIDER_AMBIENT);
        map.put(EntityType.CAVE_SPIDER, Sound.ENTITY_SPIDER_AMBIENT);
        map.put(EntityType.BREEZE, Sound.ENTITY_BREEZE_IDLE_GROUND);
        map.put(EntityType.SILVERFISH, Sound.ENTITY_SILVERFISH_AMBIENT);
        map.put(EntityType.SLIME, Sound.ENTITY_SLIME_SQUISH_SMALL);
        map.put(EntityType.PHANTOM, Sound.ENTITY_PHANTOM_AMBIENT);

        map.put(EntityType.PILLAGER, Sound.ENTITY_PILLAGER_AMBIENT);
        map.put(EntityType.VINDICATOR, Sound.ENTITY_VINDICATOR_AMBIENT);
        map.put(EntityType.WITCH, Sound.ENTITY_WITCH_DRINK);
        map.put(EntityType.EVOKER, Sound.ENTITY_EVOKER_CAST_SPELL);
        map.put(EntityType.VEX, Sound.ENTITY_VEX_CHARGE);
        map.put(EntityType.ILLUSIONER, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE);
        map.put(EntityType.RAVAGER, Sound.ENTITY_RAVAGER_ROAR);

        map.put(EntityType.STRIDER, Sound.ENTITY_STRIDER_AMBIENT);

        map.put(EntityType.MAGMA_CUBE, Sound.ENTITY_MAGMA_CUBE_SQUISH_SMALL);
        //Wither Skeleton Excluded
        //Piglin Excluded
        map.put(EntityType.PIGLIN_BRUTE, Sound.ENTITY_PIGLIN_BRUTE_AMBIENT);
        map.put(EntityType.ZOMBIFIED_PIGLIN, Sound.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT);
        map.put(EntityType.HOGLIN, Sound.ENTITY_HOGLIN_AMBIENT);
        map.put(EntityType.ZOGLIN, Sound.ENTITY_ZOGLIN_AMBIENT);
        map.put(EntityType.BLAZE, Sound.ENTITY_BLAZE_AMBIENT);
        map.put(EntityType.GHAST, Sound.ENTITY_GHAST_AMBIENT);

        map.put(EntityType.ENDERMITE, Sound.ENTITY_ENDERMITE_AMBIENT);
        map.put(EntityType.ENDERMAN, Sound.ENTITY_ENDERMAN_AMBIENT);
        map.put(EntityType.SHULKER, Sound.ENTITY_SHULKER_SHOOT);

        //Ender Dragon Excluded
        map.put(EntityType.WITHER, Sound.ENTITY_WITHER_AMBIENT);
        map.put(EntityType.ELDER_GUARDIAN, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT);
        map.put(EntityType.WARDEN, Sound.ENTITY_WARDEN_HEARTBEAT);

        entityTypeNoteblockSoundMap = map;
    }
    public static Map<EntityType,Sound> getEntityTypeNoteblockSoundMap(){
        if (entityTypeNoteblockSoundMap != null) return entityTypeNoteblockSoundMap;
        buildEntityTypeNoteblockSoundMap();
        return entityTypeNoteblockSoundMap;
    }
    public static Sound getEntityTypeNoteblockSound(EntityType entityType){
        return getEntityTypeNoteblockSoundMap().getOrDefault(entityType,Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }

}
