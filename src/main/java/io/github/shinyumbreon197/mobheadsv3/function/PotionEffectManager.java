package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PotionEffectManager {

    public static void addHeadRemovalPotionEffects(LivingEntity target, MobHead mobHead){
        if (debug) System.out.println("addHeadRemovalEffects(" + target.getName() + " " + mobHead + ")"); //debug
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        List<PotionEffect> effects = null;
        if (Groups.isZombie(headType)) effects = List.of(buildSimpleEffect(PotionEffectType.HUNGER,1, 5*20));
        switch (headType){
            case WITHER_SKELETON -> effects = List.of(buildSimpleEffect(PotionEffectType.WITHER, 1, 4*20));
            case WITHER -> effects = List.of(buildSimpleEffect(PotionEffectType.WITHER, 2, 3*20));
            case CAVE_SPIDER -> effects = List.of(buildSimpleEffect(PotionEffectType.POISON, 1, 3*20));
            case BAT -> effects = List.of(buildSimpleEffect(PotionEffectType.BLINDNESS,1,20));
            case WARDEN -> effects = List.of(
                    buildSimpleEffect(PotionEffectType.BLINDNESS,1,20),
                    buildSimpleEffect(PotionEffectType.NIGHT_VISION,1,20)
            );
        }
        if (debug) System.out.println("effects: " + effects); //debug
        if (effects == null)return;
        applyPotionEffectsToEntity(target,effects);
    }

    public static List<PotionEffect> getAfflictionPotionEffects(EntityType attackerType, boolean projectile){
        if (Groups.isZombie(attackerType) && !projectile) return List.of(buildSimpleEffect(PotionEffectType.HUNGER,1, 10*20));

        switch (attackerType){
            case WITHER_SKELETON -> {if (!projectile) return List.of(buildSimpleEffect(PotionEffectType.WITHER,2,4*20));}
            case WITHER -> {if (!projectile) return List.of(buildSimpleEffect(PotionEffectType.WITHER,3,3*20));}
            case CAVE_SPIDER -> {if (!projectile) return List.of(buildSimpleEffect(PotionEffectType.POISON,1,10*20));}
            case BEE -> {if (!projectile) return List.of(buildSimpleEffect(PotionEffectType.POISON,1,5*20));}
            case WARDEN -> {
                return List.of(buildSimpleEffect(PotionEffectType.DARKNESS,1,10*20));
            }
            case SHULKER -> {
                int ticks = 40;
                if (projectile) ticks = 80;
                return List.of(buildSimpleEffect(PotionEffectType.LEVITATION,1, ticks));
            }
            case STRAY -> {return List.of(buildSimpleEffect(PotionEffectType.SLOW,1,5*20));}
            case ELDER_GUARDIAN -> {return List.of(buildSimpleEffect(PotionEffectType.SLOW_DIGGING, 4, 5*20));}
        }
        return new ArrayList<>();
    }

    public static List<PotionEffect> getRetaliationPotionEffects(EntityType defenderType, boolean projectile){
        switch (defenderType){
            case PUFFERFISH -> {if (!projectile) return List.of(buildSimpleEffect(PotionEffectType.POISON,1,3*20));}
        }
        return new ArrayList<>();
    }

    // Public ----------------------------------------------------------------------------------------------------------
    // Void --------------------------------------------------------------------------------
    public static void addEffectsToEntity(Entity target, List<PotionEffect> effects){
        if (!(target instanceof LivingEntity))return;
        for (PotionEffect effect:effects) addEffectToEntity(target, effect);
    }
    public static void addEffectToEntity(Entity target, PotionEffect effect){
        if (!(target instanceof LivingEntity))return;
        LivingEntity livingTarget = (LivingEntity) target;
        applyPotionEffectsToEntity(livingTarget,List.of(effect));
    }
    public static void removeInfinitePotionEffects(LivingEntity target){
        List<PotionEffectType> toRemove = new ArrayList<>();
        for (PotionEffect effect: target.getActivePotionEffects()){
            if (effect.isInfinite()) toRemove.add(effect.getType());
        }
        for (PotionEffectType type:toRemove){
            target.removePotionEffect(type);
        }
    }
    public static void removeExactEffect(LivingEntity target, PotionEffect potionEffect){
        PotionEffectType potionEffectType = potionEffect.getType();
        int amp = potionEffect.getAmplifier();
        int dur = potionEffect.getDuration();
        if (!target.hasPotionEffect(potionEffectType))return;
        PotionEffect targetEffect = target.getPotionEffect(potionEffectType);
        if (targetEffect == null)return;
        int tarAmp = targetEffect.getAmplifier();
        int tarDur = targetEffect.getDuration();
        if (amp != tarAmp || dur != tarDur)return;
        target.removePotionEffect(potionEffectType);
    }
    public static void updateEffects(LivingEntity target, UUID headID){
        MobHead mobHead = MobHead.getMobHeadFromUUID(headID);
        if (mobHead == null)return;
        EntityType entityType = mobHead.getEntityType();
        updateEffects(target, entityType);
    }
    public static void updateEffects(LivingEntity target, EntityType headType){
        List<List<PotionEffect>> lists = getHeadPotionEffects(headType, target);
        List<PotionEffect> addEffects = lists.get(0);
        List<PotionEffect> removeEffects = lists.get(1);
        removePotionEffectsFromEntity(target,removeEffects);
        applyPotionEffectsToEntity(target,addEffects);
    }

    // Object ---------------------------------------------------------------------------
    public static PotionEffect buildSimpleEffect(PotionEffectType type, int lv, int ticks){
        if (lv == 0) lv = 1;
        return new PotionEffect(type,ticks,lv-1,false,true,true);
    }

    // Private ---------------------------------------------------------------------------------------------------------
    // Void -----------------------------------------------------------------------------
    private static void removePotionEffectsFromEntity(LivingEntity target, List<PotionEffect> removeEffects){
        List<PotionEffect> currentEffects = new ArrayList<>(target.getActivePotionEffects());
        if (currentEffects.size() == 0)return;
        List<PotionEffectType> currentEffectTypes = new ArrayList<>();
        for (PotionEffect pe:currentEffects) currentEffectTypes.add(pe.getType());
        List<PotionEffectType> toRemove = new ArrayList<>();
        for (PotionEffect removeEffect:removeEffects){
            PotionEffectType removeType = removeEffect.getType();
            int remAmp = removeEffect.getAmplifier();
            boolean remIsInfinite = removeEffect.getDuration() == -1;
            if (!currentEffectTypes.contains(removeType))continue;
            PotionEffect currentEffect = target.getPotionEffect(removeType);
            if (currentEffect == null)continue;
            int curAmp = currentEffect.getAmplifier();
            boolean curIsInfinite = currentEffect.isInfinite();
            if (curAmp != remAmp || (!curIsInfinite && remIsInfinite))continue;
            toRemove.add(removeType);
        }
        if (toRemove.size() == 0)return;
        for (PotionEffectType type:toRemove){
            target.removePotionEffect(type);
        }
    }

    private static void applyPotionEffectsToEntity(LivingEntity target, List<PotionEffect> newEffects){
        if (newEffects.size() == 0)return;
        List<PotionEffect> currentEffects = new ArrayList<>(target.getActivePotionEffects());
        List<PotionEffectType> currentEffectTypes = new ArrayList<>();
        for (PotionEffect pe:currentEffects) currentEffectTypes.add(pe.getType());
        List<PotionEffect> toApply = new ArrayList<>();
        for (PotionEffect newEffect:newEffects){
            PotionEffectType newType = newEffect.getType();
            int newAmp = newEffect.getAmplifier();
            int newDur = newEffect.getDuration();
            boolean newDurIsInfinite = newDur == -1;
            if (currentEffectTypes.contains(newType)){
                PotionEffect currentEffect = target.getPotionEffect(newType);
                if (currentEffect != null) {
                    int curAmp = currentEffect.getAmplifier();
                    int curDur = currentEffect.getDuration();
                    boolean curDurIsInfinite = curDur == -1;

                    boolean greaterAmp = newAmp > curAmp;
                    boolean sameAmp = newAmp == curAmp;
                    boolean longerDur = newDur > curDur || newDurIsInfinite && !curDurIsInfinite;
                    boolean skip = true;
                    if (greaterAmp) skip = false;
                    if (skip && sameAmp && longerDur) skip = false;
                    if (skip)continue;
                }
            }
            toApply.add(newEffect);
        }
        if (toApply.size() == 0)return;
        for (PotionEffect pe:toApply){
            PotionEffectType pet = pe.getType();
            target.removePotionEffect(pet);
            target.addPotionEffect(pe);
        }
    }

    // Object ---------------------------------------------------------------------------
    public static PotionEffect headEffect(PotionEffectType type, int lv, int dur, boolean particles){
        return new PotionEffect(type,dur,lv - 1,false,particles,true);
    }
    private static List<List<PotionEffect>> getHeadPotionEffects(EntityType headType, LivingEntity target){
        List<PotionEffect> add = new ArrayList<>();
        List<PotionEffect> remove = new ArrayList<>();

        boolean exposedToWater = Util.isExposedToWater(target);
        boolean exposedToSnow = Util.isExposedToSnowfall(target);

        switch (headType){
            case DONKEY, MULE, LLAMA, TRADER_LLAMA -> {
                CreatureEvents.chestedAddHolder(target);
            }
            case SQUID, COD, SALMON, TROPICAL_FISH, PUFFERFISH, TADPOLE -> {
                PotionEffect effect = headEffect(PotionEffectType.SPEED,1,-1,false);
                if (exposedToWater){
                    add.add(effect);
                }else remove.add(effect);
            }
            case GLOW_SQUID -> {
                PotionEffect effect0 = headEffect(PotionEffectType.SPEED,1,-1,false);
                PotionEffect effect1 = headEffect(PotionEffectType.NIGHT_VISION, 1, -1, false);
                if (exposedToWater){
                    add.addAll(List.of(effect0, effect1));
                }else remove.addAll(List.of(effect0, effect1));
            }
            case DROWNED -> {
                add.add(headEffect(PotionEffectType.HUNGER, 1,-1, false));
                PotionEffect effect0 = headEffect(PotionEffectType.SLOW, 1, -1, false);
                if (exposedToWater){
                    remove.add(effect0);
                }else add.add(effect0);
            }
            case GUARDIAN, ELDER_GUARDIAN -> {
                PotionEffect effect0 = headEffect(PotionEffectType.DAMAGE_RESISTANCE,1,-1,false);
                if (exposedToWater){
                    add.add(effect0);
                }else remove.add(effect0);
            }
            case AXOLOTL -> {
                PotionEffect effect0 = headEffect(PotionEffectType.INCREASE_DAMAGE, 1, -1, false);
                if (exposedToWater){
                    add.add(effect0);
                }else remove.add(effect0);
            }
            case TURTLE -> {
                add.add(headEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, -1, false));
                PotionEffect effect0 = headEffect(PotionEffectType.SLOW,1, -1, false);
                if (exposedToWater){
                    remove.add(effect0);
                }else add.add(effect0);
            }
            case SNOWMAN -> {
                PotionEffect effect0 = headEffect(PotionEffectType.REGENERATION,2,-1,false);
                PotionEffect effect1 = headEffect(PotionEffectType.WITHER,1,-1,false);
                if (exposedToSnow){
                    add.add(effect0);
                    remove.add(effect1);
                }else if (exposedToWater){
                    add.add(effect1);
                    remove.add(effect0);
                }else{
                    remove.add(effect0);
                    remove.add(effect1);
                }
            }
            case HUSK -> {
                PotionEffect effect0 = headEffect(PotionEffectType.SLOW,1,-1,false);
                if (Util.isWalkingOnSandyBlock(target)){
                    remove.add(effect0);
                }else add.add(effect0);
                add.add(headEffect(PotionEffectType.HUNGER, 1, -1, false));
            }
            case CAMEL -> {
                PotionEffect effect0 = headEffect(PotionEffectType.SPEED,1,-1,false);
                if (Util.isWalkingOnSandyBlock(target)){
                    add.add(effect0);
                }else remove.add(effect0);
            }
            case DOLPHIN -> {add.add(headEffect(PotionEffectType.DOLPHINS_GRACE, 1, -1, false));}
            case ZOMBIE, ZOMBIE_VILLAGER -> {
                add.add(headEffect(PotionEffectType.HUNGER, 1, -1, false));
                add.add(headEffect(PotionEffectType.SLOW, 1, -1, false));
            }
            case ZOMBIFIED_PIGLIN -> {
                add.add(headEffect(PotionEffectType.HUNGER, 1, -1, false));
                add.add(headEffect(PotionEffectType.SLOW, 1, -1, false));
            }
            case WITHER_SKELETON -> {add.add(headEffect(PotionEffectType.WITHER, 1, -1, false));}
            case WITHER -> {add.add(headEffect(PotionEffectType.WITHER, 2, -1, false));}
            case CHICKEN, PARROT -> {add.add(headEffect(PotionEffectType.SLOW_FALLING, 1, -1, false));}
            //case STRIDER -> {add.add(headEffect(PotionEffectType.FIRE_RESISTANCE, 1, -1, false));}
            case BAT -> {
                add.add(headEffect(PotionEffectType.BLINDNESS, 1, -1, false));
                add.add(headEffect(PotionEffectType.SPEED,1,-1,false));
                add.add(headEffect(PotionEffectType.NIGHT_VISION,1,-1,false));
            }
            case WARDEN -> {
                //add.add(headEffect(PotionEffectType.SPEED,2,-1,false));
                add.add(headEffect(PotionEffectType.DARKNESS,1,-1,false));
                add.add(headEffect(PotionEffectType.NIGHT_VISION,1,-1,false));
            }
            case ZOMBIE_HORSE -> {
                add.add(headEffect(PotionEffectType.HUNGER, 1, -1, false));
                add.add(headEffect(PotionEffectType.SPEED, 1, -1, false));
            }
            case IRON_GOLEM -> {
                add.add(headEffect(PotionEffectType.DAMAGE_RESISTANCE,2,-1,false));
                add.add(headEffect(PotionEffectType.INCREASE_DAMAGE, 1, -1, false));
                add.add(headEffect(PotionEffectType.SLOW, 3, -1, false));
            }
            case PHANTOM -> {add.add(headEffect(PotionEffectType.NIGHT_VISION,1,-1,false));}
            case ZOGLIN -> {
                add.add(headEffect(PotionEffectType.HUNGER, 1, -1, false));
                add.add(headEffect(PotionEffectType.SLOW, 1, -1, false));
                add.add(headEffect(PotionEffectType.INCREASE_DAMAGE, 1, -1, false));
            }
            case HOGLIN -> {add.add(headEffect(PotionEffectType.INCREASE_DAMAGE, 1, -1, false));}
            case GHAST, BLAZE -> {
                add.add(headEffect(PotionEffectType.SLOW_FALLING, 1, -1, false));
            }
            case SLIME, FROG -> {add.add(headEffect(PotionEffectType.JUMP, 1, -1, false));}
            case MAGMA_CUBE -> {
                //add.add(headEffect(PotionEffectType.FIRE_RESISTANCE, 1, -1, false));
                add.add(headEffect(PotionEffectType.JUMP, 1, -1, false));
            }
            case CAT, OCELOT -> {
                add.add(headEffect(PotionEffectType.SPEED, 1, -1, false));
                //add.add(headEffect(PotionEffectType.JUMP, 1, -1, false));
            }
            case HORSE, SKELETON_HORSE -> {add.add(headEffect(PotionEffectType.SPEED,2,-1,false));}
            case RABBIT -> {add.add(headEffect(PotionEffectType.JUMP,2,-1,false));}
            case CAVE_SPIDER -> {add.add(headEffect(PotionEffectType.POISON,1,-1,false));}
        }

        return List.of(add,remove);
    }

    // Constants ---------------------------------------------------------------------

}
