package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Summon {

    private static final NamespacedKey ownerNSK = new NamespacedKey(MobHeadsV3.getPlugin(),"owner");
    private static final NamespacedKey targetNSK = new NamespacedKey(MobHeadsV3.getPlugin(),"target");

    private static final List<Entity> summons = new ArrayList<>();
    private static final Map<Entity, Integer> summonLifeTimerMap = new HashMap<>();
    private static final Map<LivingEntity, Integer> summonCooldownTimerMap = new HashMap<>();

    public static void manageSummons(){
        List<LivingEntity> toRemoveFromCooldown = new ArrayList<>();
        for (LivingEntity owner:summonCooldownTimerMap.keySet()){
            int timer = summonCooldownTimerMap.get(owner);
            timer = timer - 10;
            if (timer <= 0){
                toRemoveFromCooldown.add(owner);
            }else summonCooldownTimerMap.put(owner, timer);
        }
        for (LivingEntity toRemove:toRemoveFromCooldown){
            summonCooldownTimerMap.remove(toRemove);
        }
        List<Entity> summonsToBeRemoved = new ArrayList<>();
        for (Entity summon:summons){
            if (summonLifeTimerMap.containsKey(summon)){
                int timer = summonLifeTimerMap.get(summon);
                timer = timer - 10;
                if (timer <= 0 || (summon instanceof LivingEntity && summon.isDead())){
                    summonLifeTimerMap.remove(summon);
                    summonsToBeRemoved.add(summon);
                }else summonLifeTimerMap.put(summon, timer);
            }
            PersistentDataContainer data = summon.getPersistentDataContainer();
            String ownerUUIDString = data.get(ownerNSK,PersistentDataType.STRING);
            String targetUUIDString = data.get(targetNSK,PersistentDataType.STRING);
            if (ownerUUIDString == null || targetUUIDString == null){
                summonsToBeRemoved.add(summon);
                continue;
            }
            UUID ownerUUID = UUID.fromString(ownerUUIDString);
            UUID targetUUID = UUID.fromString(targetUUIDString);
            Entity owner = Bukkit.getEntity(ownerUUID);
            LivingEntity target = (LivingEntity) Bukkit.getEntity(targetUUID);
            if (owner == null){
                summonsToBeRemoved.add(summon);
                continue;
            }
            if (target == null || target.isDead()){
                //Get nearby, check if any have target as owner, set as target, rewrite target data, else remove
            }
            EntityType summonType = summon.getType();
            switch (summonType){
                case WOLF -> {
                    Wolf wolf = (Wolf) summon;
                    wolf.setTarget(target);
                }
            }
        }
        for (Entity toRemove:summonsToBeRemoved){
            summons.remove(toRemove);
            AVFX.playSummonEffect(toRemove.getLocation());
            toRemove.remove();
        }
    }

    public static Wolf wolfSummon(LivingEntity owner, Location location, LivingEntity target){
        if (summonCooldownTimerMap.get(owner) != null)return null;
        World world = location.getWorld();
        if (world == null || target == null || owner == null)return null;
        Wolf wolf = (Wolf) world.spawnEntity(location, EntityType.WOLF);

        summons.add(wolf);
        summonLifeTimerMap.put(wolf, 60*20);
        summonCooldownTimerMap.put(owner, 10*20);

        PersistentDataContainer data = wolf.getPersistentDataContainer();
        data.set(ownerNSK, PersistentDataType.STRING, owner.getUniqueId().toString());
        data.set(targetNSK, PersistentDataType.STRING, target.getUniqueId().toString());

        wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20,0,false));
        wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20,0,false));
        wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20,0,false));

        wolf.setAngry(true);
        wolf.setCollarColor(DyeColor.BLACK);
        wolf.setTarget(target);

        AVFX.playWolfSummonEffect(location);

        return wolf;
    }

}
