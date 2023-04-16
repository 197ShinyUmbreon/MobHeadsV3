package io.github.shinyumbreon197.mobheadsv3.effect;

import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class AfflictedHeadEffects {

    public static List<PotionEffect> getPotionEffects(EntityType entityType){
        if (entityType != null){
            switch (entityType){
                default -> {}
                case PLAYER -> {}

                case ZOMBIE -> {return zombieAfflictionEffects();}
                case SKELETON -> {}
                case CREEPER -> {}
                case WITHER_SKELETON -> {return witherSkeletonAfflictionEffects();}
                case ENDER_DRAGON -> {}

                case COW -> {}
                case PIG -> {}
                case CHICKEN -> {}
                case WOLF -> {}
                case DONKEY -> {}
                case MULE -> {}
                case DOLPHIN -> {}
                case COD -> {}
                case SALMON -> {}
                case PUFFERFISH -> {}
                case TROPICAL_FISH -> {}
                case TURTLE -> {}
                case STRIDER -> {}
                case GOAT -> {}
                case SQUID -> {}
                case BEE -> {}
                case BAT -> {}
                case OCELOT -> {}
                case SNOWMAN -> {}
                case POLAR_BEAR -> {}
                case SKELETON_HORSE -> {}
                case ZOMBIE_HORSE -> {}
                case WANDERING_TRADER -> {}
                case IRON_GOLEM -> {}
                case GLOW_SQUID -> {}
                case ALLAY -> {}
                case TADPOLE -> {}

                case SILVERFISH -> {}
                case STRAY -> {}
                case SHULKER -> {}
                case PHANTOM -> {}
                case HUSK -> {return zombieAfflictionEffects();}
                case DROWNED -> {return zombieAfflictionEffects();}
                case HOGLIN -> {}
                case ZOGLIN -> {return zombieAfflictionEffects();}
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
                case ZOMBIFIED_PIGLIN -> {return zombieAfflictionEffects();}
                case SLIME -> {}
                case ENDERMITE -> {}
                case PILLAGER -> {}
                case VINDICATOR -> {}
                case ILLUSIONER -> {}

                case ELDER_GUARDIAN -> {}
                case WITHER -> {return witherAfflictionEffects();}
                case WARDEN -> {}

                case RABBIT -> {}
                case AXOLOTL -> {}
                case CAT -> {}
                case HORSE -> {}
                case LLAMA -> {}
                case TRADER_LLAMA -> {}
                case PARROT -> {}
                case FOX -> {}
                case PANDA -> {}
                case SHEEP -> {}
                case MUSHROOM_COW -> {}
                case FROG -> {}
                case VILLAGER -> {}
                case ZOMBIE_VILLAGER -> {}
            }
        }
        return new ArrayList<>();
    }

    private static List<PotionEffect> zombieAfflictionEffects(){
        List<PotionEffect> effects = new ArrayList<>();
        PotionEffect effect0 = new PotionEffect(
                PotionEffectType.HUNGER, 10*20,
                0,false, true, true
        );
        effects.add(effect0);
        return effects;
    }

    private static List<PotionEffect> witherSkeletonAfflictionEffects(){
        List<PotionEffect> effects = new ArrayList<>();
        PotionEffect effect0 = new PotionEffect(
                PotionEffectType.WITHER, 4*20,
                1,false, true, true
        );
        effects.add(effect0);
        return effects;
    }

    private static List<PotionEffect> witherAfflictionEffects(){
        List<PotionEffect> effects = new ArrayList<>();
        PotionEffect effect0 = new PotionEffect(
                PotionEffectType.WITHER, 3*20,
                2,false, true, true
        );
        effects.add(effect0);
        return effects;
    }

}
