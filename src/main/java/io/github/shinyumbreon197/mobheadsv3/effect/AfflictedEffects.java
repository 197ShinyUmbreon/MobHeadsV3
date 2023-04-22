package io.github.shinyumbreon197.mobheadsv3.effect;

import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class AfflictedEffects {

    public static List<PotionEffect> getPotionEffects(EntityType entityType){
        if (entityType != null){
            switch (entityType){
                default -> {}
                case ZOMBIE -> {return zombieEffects();}
                case WITHER_SKELETON -> {return witherSkeletonEffects();}
                case HUSK -> {return zombieEffects();}
                case DROWNED -> {return zombieEffects();}
                case ZOGLIN -> {return zombieEffects();}
                case ZOMBIFIED_PIGLIN -> {return zombieEffects();}
                case WITHER -> {return witherAfflictionEffects();}
                case WARDEN -> {} //List.of() Darkness, Lv1, 5 Sec.
            }
        }
        return new ArrayList<>();
    }

    private static List<PotionEffect> zombieEffects(){
        List<PotionEffect> effects = new ArrayList<>();
        PotionEffect effect0 = new PotionEffect(
                PotionEffectType.HUNGER, 10*20,
                0,false, true, true
        );
        effects.add(effect0);
        return effects;
    }

    private static List<PotionEffect> witherSkeletonEffects(){
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
