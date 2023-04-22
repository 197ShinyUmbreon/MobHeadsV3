package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.EffectUtil;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class WornEffects {

    public static void applyNewPotionEffects(List<LivingEntity> wearingList){
        for (LivingEntity livingEntity:wearingList){
            if (livingEntity.getEquipment() == null || HeadUtil.getMobHeadFromHeadItem(livingEntity.getEquipment().getHelmet()) == null){
                removeInfinitePotionEffects(List.of(livingEntity));
                continue;
            }
            MobHead mobHead = HeadUtil.getMobHeadFromHeadItem(livingEntity.getEquipment().getHelmet());
            assert mobHead != null;
            //System.out.println("Ready to apply effects to "+livingEntity.getEntityId()); //debug
            List<PotionEffect> oldEffects = new ArrayList<>(livingEntity.getActivePotionEffects());
            List<PotionEffect> newEffects = WornEffects.getPotionEffects(livingEntity, mobHead.getEntityType());
            if (newEffects.size() == 0){
                removeInfinitePotionEffects(List.of(livingEntity));
            }
            List<PotionEffectType> exclusions = new ArrayList<>();
            for (PotionEffect newEffect:newEffects){
                PotionEffectType newType = newEffect.getType();
                for (PotionEffect oldEffect:oldEffects){
                    if (oldEffect.isInfinite() && !newEffects.contains(oldEffect)){
                        livingEntity.removePotionEffect(oldEffect.getType());
                        continue;
                    }
                    PotionEffectType oldType = oldEffect.getType();
                    if (oldType.equals(newType) && (oldEffect.getAmplifier() >= newEffect.getAmplifier()) || oldEffect.isInfinite()){
                        exclusions.add(oldType);
                    }
                }
            }
            List<PotionEffect> finalEffects = new ArrayList<>();
            for (PotionEffect newEffect:newEffects){
                if (!exclusions.contains(newEffect.getType())) finalEffects.add(newEffect);
            }
            //System.out.println("Applying "+finalEffects+" to "+livingEntity.getEntityId()); //debug
            if (finalEffects.size() != 0) applyInfinitePotionEffects(livingEntity, finalEffects);
        }
    }

    private static void applyInfinitePotionEffects(LivingEntity livingEntity, List<PotionEffect> potionEffects){
        for (PotionEffect potionEffect:potionEffects){
            livingEntity.addPotionEffect(potionEffect);
        }
    }
    public static void removeInfinitePotionEffects(List<LivingEntity> livingEntities){
        for (LivingEntity livingEntity:livingEntities){
            for (PotionEffect potionEffect: livingEntity.getActivePotionEffects()){
                if (potionEffect.isInfinite()) livingEntity.removePotionEffect(potionEffect.getType());
            }
        }
    }

    public static List<PotionEffect> getPotionEffects(LivingEntity wearer, EntityType entityType){
        List<PotionEffect> effects = new ArrayList<>();
        switch (entityType){
            default -> {}
            case ZOMBIE, HUSK -> {effects.add(PotionFX.hunger(-1,0,false)); effects.add(PotionFX.slow(-1, 0, false));}
            case WITHER_SKELETON -> {effects.add(PotionFX.wither(-1, 0, false));}
            case CHICKEN -> {effects.add(PotionFX.slowFall(-1, 0, false));}
            case DOLPHIN -> {effects.addAll(dolphinEffects(wearer));}
            case COD, SALMON, PUFFERFISH, TROPICAL_FISH, SQUID, TADPOLE -> {effects.addAll(fishEffects(wearer));}
            case SKELETON_HORSE -> {effects.add(PotionFX.speed(-1, 0, false));}
            case ZOMBIE_HORSE -> {effects.add(PotionFX.hunger(-1, 0, false)); effects.add(PotionFX.speed(-1, 0, false));}
            case GLOW_SQUID -> {effects.addAll(glowSquidEffects(wearer));}
            case PHANTOM -> {effects.add(PotionFX.nightVision(-1, 0, false));}
            case DROWNED -> {effects.addAll(drownedEffects(wearer));}
            case ZOGLIN -> {effects.add(PotionFX.hunger(-1, 0, false));}
            case ZOMBIFIED_PIGLIN -> {effects.add(PotionFX.hunger(-1, 0, false));}
            case WITHER -> {effects.add(PotionFX.wither(-1, 1, false));}
            case HORSE -> {effects.add(PotionFX.speed(-1, 0, false));}
            case PARROT -> {effects.add(PotionFX.slowFall(-1, 0, false));}
        }
        return effects;
    }

    private static List<PotionEffect> drownedEffects(LivingEntity wearer){
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(PotionFX.hunger(-1, 0, false));
        if (EffectUtil.isExposedToWater(wearer)){
            effects.add(PotionFX.strength(-1, 0, false));
            effects.add(PotionFX.haste(-1, 0, false));
        }else effects.add(PotionFX.slow(-1,0,false));
        return effects;
    }

    private static List<PotionEffect> glowSquidEffects(LivingEntity wearer){
        List<PotionEffect> effects = new ArrayList<>();
        if (EffectUtil.isExposedToWater(wearer)){
            effects.add(PotionFX.nightVision(-1, 0, false));
        }
        return effects;
    }

    private static List<PotionEffect> dolphinEffects(LivingEntity wearer){
        List<PotionEffect> effects = new ArrayList<>();
        if (EffectUtil.isExposedToWater(wearer)){
            effects.add(PotionFX.dolphinsGrace(-1, 0, false));
            effects.add(PotionFX.speed(-1, 0, false));
        }
        return effects;
    }
    
    private static List<PotionEffect> fishEffects(LivingEntity wearer){
        List<PotionEffect> effects = new ArrayList<>();
        if (EffectUtil.isExposedToWater(wearer)){
            effects.add(PotionFX.speed(-1, 0, false));
        }
        return effects;
    }

}
