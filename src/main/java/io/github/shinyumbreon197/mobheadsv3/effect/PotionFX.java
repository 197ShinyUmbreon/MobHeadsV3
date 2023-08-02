package io.github.shinyumbreon197.mobheadsv3.effect;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionFX {

    public static void applyPotionEffect(LivingEntity livingEntity, PotionEffect potionEffect){
        applyPotionEffect(livingEntity, potionEffect.getType(), potionEffect.getDuration(), potionEffect.getAmplifier(), potionEffect.hasParticles());
    }
    public static void applyPotionEffect(LivingEntity livingEntity, PotionEffectType pet, int duration, int amplifier, boolean showBubbles){
        applyPotionEffect(livingEntity, pet, duration, amplifier, showBubbles, false);
    }

    public static void applyPotionEffect(LivingEntity livingEntity, PotionEffectType pet, int duration, int amplifier, boolean showBubbles, boolean hardReplace){
        if (livingEntity.isDead())return;
        boolean needsEffectRefresh = false;
        boolean hasEffect = false;
        if (!hardReplace){
            PotionEffect oldEffect = livingEntity.getPotionEffect(pet);
            if (oldEffect != null && oldEffect.getDuration() > 20){
                hasEffect = true;
                if (oldEffect.getAmplifier() > amplifier)return;
                if (oldEffect.getDuration() > duration)return;
            }
            if (hasEffect){
                if (oldEffect.getDuration() < duration*0.5 || oldEffect.getDuration() < 20) needsEffectRefresh = true; //half of duration
            }
        }else{
            needsEffectRefresh = true;
        }
        if (!hasEffect || needsEffectRefresh){
            double health = livingEntity.getHealth();
            PotionEffect pEffect = new PotionEffect(pet, duration, amplifier, false, showBubbles);
            if (pet.equals(PotionEffectType.HEALTH_BOOST)){
                health = health + 4*(amplifier+1);
            }
            livingEntity.removePotionEffect(pet);
            livingEntity.addPotionEffect(pEffect);
            if (pet.equals(PotionEffectType.HEALTH_BOOST)){
                if (livingEntity instanceof Player){
                    Player player = (Player) livingEntity;
                    player.setHealth(health);
                }
            }
        }
    }

    public static PotionEffect hunger(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.HUNGER, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect slow(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.SLOW, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect nightVision(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.NIGHT_VISION, dur,
                amp,false, particles, true
        );
    }

    public static PotionEffect wither(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.WITHER, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect strength(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.INCREASE_DAMAGE, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect haste(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.FAST_DIGGING, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect slowFall(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.SLOW_FALLING, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect dolphinsGrace(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.DOLPHINS_GRACE, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect speed(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.SPEED, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect jump(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.JUMP, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect regen(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.REGENERATION, dur,
                amp,false, particles, true
        );
    }
    public static PotionEffect darkness(int dur, int amp, boolean particles){
        return new PotionEffect(
                PotionEffectType.DARKNESS, dur,
                amp,false, particles, true
        );
    }

}

