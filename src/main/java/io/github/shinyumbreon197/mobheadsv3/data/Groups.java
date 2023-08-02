package io.github.shinyumbreon197.mobheadsv3.data;

import org.bukkit.entity.EntityType;

import java.util.List;

public class Groups {

    public static boolean sameGroup(EntityType type0, EntityType type1){
        if (type0.equals(type1))return true;
        if (isBoss(type0) && isBoss(type1))return true;
        if (isSkeletal(type0) && isSkeletal(type1))return true;
        if (isZombie(type0) && isZombie(type1))return true;
        if (isAquatic(type0) && isAquatic(type1))return true;
        if (isLlama(type0) && isLlama(type1))return true;
        if (isVillager(type0) && isVillager(type1))return true;
        if (isWinged(type0) && isWinged(type1))return true;
        if (isCow(type0) && isCow(type1))return true;
        if (isPillager(type0) && isPillager(type1))return true;
        if (isFeline(type0) && isFeline(type1))return true;
        if (isSlimeBodied(type0) && isSlimeBodied(type1))return true;
        if (isEquine(type0) && isEquine(type1))return true;
        if (isPiglin(type0) && isPiglin(type1))return true;
        if (isSpider(type0) && isSpider(type1))return true;
        if (isArthropod(type0) && isArthropod(type1))return true;
        if (isEndCreature(type0) && isEndCreature(type1))return true;
        if (isFlameBodied(type0) && isFlameBodied(type1))return true;
        if (isNetherCreature(type0) && isNetherCreature(type1))return true;
        if (isPixie(type0) && isPixie(type1))return true;
        if (isSonarSight(type0) && isSonarSight(type1))return true;
        if (isUndead(type0) && isUndead(type1))return true;
        if (isSquid(type0) && isSquid(type1))return true;
        if (isWithered(type0) && isWithered(type1))return true;
        if (isEnderman(type0) && isEnderman(type1))return true;
        return false;
    }

    public static boolean neutralTarget(EntityType type0, EntityType type1){
        if (type0.equals(type1))return true;
        if (isSkeletal(type0) && isSkeletal(type1))return true;
        if (isZombie(type0) && isZombie(type1))return true;
        if (isPillager(type0) && isPillager(type1))return true;
        if (isSlimeBodied(type0) && isSlimeBodied(type1))return true;
        if (isPiglin(type0) && isPiglin(type1))return true;
        if (isSpider(type0) && isSpider(type1))return true;
        if (isWithered(type0) && isWithered(type1))return true;
        if (isEnderman(type0) && isEnderman(type1))return true;
        return false;
    }

    private static final List<EntityType> boss = List.of(
            EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.ELDER_GUARDIAN, EntityType.WARDEN
    );
    public static boolean isBoss(EntityType type){
        return boss.contains(type);
    }
    private static final List<EntityType> skeletal = List.of(
            EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.WITHER_SKELETON, EntityType.STRAY, EntityType.WITHER
    );
    public static boolean isSkeletal(EntityType type){
        return skeletal.contains(type);
    }
    private static final List<EntityType> zombie = List.of(
            EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIFIED_PIGLIN,
            EntityType.ZOGLIN, EntityType.DROWNED, EntityType.HUSK, EntityType.GIANT
    );
    public static boolean isZombie(EntityType type){
        return zombie.contains(type);
    }
    private static final List<EntityType> aquatic = List.of(
            EntityType.COD, EntityType.SALMON, EntityType.PUFFERFISH, EntityType.TROPICAL_FISH, EntityType.SQUID, EntityType.GLOW_SQUID,
            EntityType.TADPOLE, EntityType.FROG, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.DOLPHIN,
            EntityType.DROWNED, EntityType.AXOLOTL, EntityType.TURTLE
    );
    public static boolean isAquatic(EntityType type){
        return aquatic.contains(type);
    }
    private static final List<EntityType> llama = List.of(EntityType.LLAMA, EntityType.LLAMA_SPIT);
    public static boolean isLlama(EntityType type){
        return llama.contains(type);
    }
    private static final List<EntityType> villager = List.of(EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER, EntityType.WANDERING_TRADER);
    public static boolean isVillager(EntityType type){
        return villager.contains(type);
    }
    private static final List<EntityType> winged = List.of(
            EntityType.CHICKEN, EntityType.PARROT, EntityType.PHANTOM, EntityType.ENDER_DRAGON
    );
    public static boolean isWinged(EntityType type){
        return winged.contains(type);
    }
    private static final List<EntityType> cow = List.of(EntityType.COW, EntityType.MUSHROOM_COW);
    public static boolean isCow(EntityType type){
        return cow.contains(type);
    }
    private static final List<EntityType> pillager = List.of(
            EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.VEX, EntityType.RAVAGER,
            EntityType.WITCH, EntityType.EVOKER
    );
    public static boolean isPillager(EntityType type){
        return pillager.contains(type);
    }
    private static final List<EntityType> feline = List.of(EntityType.CAT, EntityType.OCELOT);
    public static boolean isFeline(EntityType type){
        return feline.contains(type);
    }
    private static final List<EntityType> slimeBodied = List.of(EntityType.SLIME, EntityType.MAGMA_CUBE);
    public static boolean isSlimeBodied(EntityType type){
        return slimeBodied.contains(type);
    }
    private static final List<EntityType> equine = List.of(
            EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE
    );
    public static boolean isEquine(EntityType type){
        return equine.contains(type);
    }
    private static final List<EntityType> piglin = List.of(
            EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIG
    );
    public static boolean isPiglin(EntityType type){
        return piglin.contains(type);
    }
    private static final List<EntityType> spider = List.of(EntityType.SPIDER, EntityType.CAVE_SPIDER);
    public static boolean isSpider(EntityType type){
        return spider.contains(type);
    }
    private static final List<EntityType> arthropod = List.of(
            EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH, EntityType.ENDERMITE, EntityType.BEE
    );
    public static boolean isArthropod(EntityType type){
        return arthropod.contains(type);
    }
    private static final List<EntityType> endCreature = List.of(
            EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER, EntityType.ENDER_DRAGON
    );
    public static boolean isEndCreature(EntityType type){
        return endCreature.contains(type);
    }
    private static final List<EntityType> flameBodied = List.of(EntityType.MAGMA_CUBE, EntityType.BLAZE);
    public static boolean isFlameBodied(EntityType type){
        return flameBodied.contains(type);
    }
    private static final List<EntityType> netherCreature = List.of(
            EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN, EntityType.MAGMA_CUBE,
            EntityType.GHAST, EntityType.STRIDER, EntityType.WITHER_SKELETON, EntityType.HOGLIN, EntityType.ZOGLIN,
            EntityType.ENDERMAN
    );
    public static boolean isNetherCreature(EntityType type){
        return netherCreature.contains(type);
    }
    private static final List<EntityType> pixie = List.of(EntityType.ALLAY, EntityType.VEX);
    public static boolean isPixie(EntityType type){
        return pixie.contains(type);
    }
    private static final List<EntityType> sonarSight = List.of(EntityType.BAT, EntityType.WARDEN);
    public static boolean isSonarSight(EntityType type){
        return sonarSight.contains(type);
    }
    private static final List<EntityType> undead = List.of(
            EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_VILLAGER,
            EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.WITHER_SKELETON, EntityType.WITHER,
            EntityType.STRAY, EntityType.PHANTOM, EntityType.DROWNED, EntityType.HUSK, EntityType.ZOGLIN
    );
    public static boolean isUndead(EntityType type){
        return undead.contains(type);
    }
    private static final List<EntityType> squid = List.of(EntityType.SQUID, EntityType.GLOW_SQUID);
    public static boolean isSquid(EntityType type){
        return squid.contains(type);
    }
    private static final List<EntityType> withered = List.of(EntityType.WITHER, EntityType.WITHER_SKELETON);
    public static boolean isWithered(EntityType type){return withered.contains(type);}
    private static final List<EntityType> enderman = List.of(EntityType.ENDERMAN, EntityType.ENDER_DRAGON);
    public static boolean isEnderman(EntityType type){return enderman.contains(type);}
    


}
