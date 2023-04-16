package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class WornHeadMechanics {

    public static void runHeadTickMechanics(List<LivingEntity> headed){
        for (LivingEntity livingEntity:headed){
            MobHead mobHead = HeadUtil.getMobHeadFromEntity(livingEntity);
            if (mobHead != null) executeTickMechanics(livingEntity, mobHead);
        }
    }

    private static void executeTickMechanics(LivingEntity livingEntity, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case PLAYER -> {}

            case ZOMBIE -> {}
            case SKELETON -> {}
            case CREEPER -> {}
            case WITHER_SKELETON -> {}
            case ENDER_DRAGON -> {}

            case COW -> {}
            case PIG -> {}
            case CHICKEN -> {}
            case WOLF -> {}
            case DONKEY -> {}
            case MULE -> {}
            case DOLPHIN -> {regenerateAir(livingEntity);}
            case COD, SALMON, TROPICAL_FISH -> {regenerateAir(livingEntity);}
            case PUFFERFISH -> {regenerateAir(livingEntity);}
            case TURTLE -> {regenerateAir(livingEntity);}
            case STRIDER -> {}
            case GOAT -> {}
            case SQUID -> {regenerateAir(livingEntity);}
            case BEE -> {}
            case BAT -> {}
            case OCELOT -> {}
            case SNOWMAN -> {}
            case POLAR_BEAR -> {}
            case SKELETON_HORSE -> {regenerateAir(livingEntity);}
            case ZOMBIE_HORSE -> {}
            case WANDERING_TRADER -> {}
            case IRON_GOLEM -> {}
            case GLOW_SQUID -> {regenerateAir(livingEntity);}
            case ALLAY -> {}
            case TADPOLE -> {regenerateAir(livingEntity);}

            case SILVERFISH -> {}
            case STRAY -> {}
            case SHULKER -> {}
            case PHANTOM -> {}
            case HUSK -> {}
            case DROWNED -> {regenerateAir(livingEntity);}
            case HOGLIN -> {}
            case ZOGLIN -> {}
            case PIGLIN -> {}
            case PIGLIN_BRUTE -> {}
            case WITCH -> {}
            case GUARDIAN -> {}
            case RAVAGER -> {}
            case VEX -> {}
            case EVOKER -> {}
            case SPIDER -> {}
            case ENDERMAN -> {}
            case GHAST -> {}
            case BLAZE -> {}
            case CAVE_SPIDER -> {}
            case MAGMA_CUBE -> {}
            case ZOMBIFIED_PIGLIN -> {}
            case SLIME -> {}
            case ENDERMITE -> {}
            case PILLAGER -> {}
            case VINDICATOR -> {}
            case ILLUSIONER -> {}

            case ELDER_GUARDIAN -> {}
            case WITHER -> {}
            case WARDEN -> {}

            case RABBIT -> {}
            case AXOLOTL -> {regenerateAir(livingEntity);}
            case CAT -> {}
            case HORSE -> {}
            case LLAMA -> {}
            case TRADER_LLAMA -> {}
            case PARROT -> {}
            case FOX -> {}
            case PANDA -> {}
            case SHEEP -> {}
            case MUSHROOM_COW -> {}
            case FROG -> {regenerateAir(livingEntity);}
            case VILLAGER -> {}
            case ZOMBIE_VILLAGER -> {}
        }
    }

    private static void regenerateAir(LivingEntity livingEntity){
        int maxAir = livingEntity.getMaximumAir();
        int airRemaining = livingEntity.getRemainingAir();

        int newAir = airRemaining + 8;
        if (newAir > maxAir) newAir = maxAir;

        livingEntity.setRemainingAir(newAir);
    }






}
