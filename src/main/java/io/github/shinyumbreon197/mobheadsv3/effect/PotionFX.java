package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionFX {

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
                PotionEffectType.SLOW, dur,
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

}

