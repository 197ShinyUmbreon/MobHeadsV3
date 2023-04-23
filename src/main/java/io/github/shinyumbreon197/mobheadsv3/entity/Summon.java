package io.github.shinyumbreon197.mobheadsv3.entity;

import com.sun.jna.platform.unix.solaris.LibKstat;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import org.bukkit.*;
import org.bukkit.entity.*;
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

    public static Entity getSummonTarget(Mob summon){
        if (!isSummon(summon))return null;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        String targetUUIDString = data.get(targetNSK, PersistentDataType.STRING);
        if (targetUUIDString == null)return null;
        UUID targetUUID = UUID.fromString(targetUUIDString);
        return Bukkit.getEntity(targetUUID);
    }

    public static Entity getSummonOwner(Mob summon){
        if (!isSummon(summon))return null;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        String ownerUUIDString = data.get(ownerNSK, PersistentDataType.STRING);
        if (ownerUUIDString == null)return null;
        UUID ownerUUID = UUID.fromString(ownerUUIDString);
        return Bukkit.getEntity(ownerUUID);
    }

    public static boolean isSummon(Entity summon){
        PersistentDataContainer data = summon.getPersistentDataContainer();
        return data.has(ownerNSK, PersistentDataType.STRING);
    }

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
            if (target == null || target.isDead() || target.equals(summon)){
                List<Entity> nearbyOwner = owner.getNearbyEntities(15, 5, 15);
                for (Entity nearby:nearbyOwner){
                    if (!(nearby instanceof Mob))continue;
                    Mob mobNearby = (Mob) nearby;
                    LivingEntity mobTarget = mobNearby.getTarget();
                    if (mobTarget != null && mobTarget.equals(owner)){
                        target = mobNearby;
                        data.set(targetNSK, PersistentDataType.STRING, mobNearby.getUniqueId().toString());
                        break;
                    }
                }
                if (target == null || target.isDead() || target.equals(summon)){
                    summonsToBeRemoved.add(summon);
                    continue;
                }
            }
            EntityType summonType = summon.getType();
            switch (summonType){
                default -> {
                    if (summon instanceof Mob){
                        Mob summonMob = (Mob) summon;
                        summonMob.setTarget(target);
                    }
                }
            }
        }
        for (Entity toRemove:summonsToBeRemoved){
            summons.remove(toRemove);
            AVFX.playSummonEffect(toRemove.getLocation());
            toRemove.remove();
        }
    }

    public static void summon(LivingEntity owner, LivingEntity target, EntityType summonType){
        List<EntityType> whitelistedSummons = List.of(EntityType.WOLF, EntityType.SILVERFISH, EntityType.BEE);
        if (!whitelistedSummons.contains(summonType))return;
        if (summonCooldownTimerMap.get(owner) != null)return;
        Random random = new Random();
        Location location = owner.getLocation().add(
                random.nextInt(-1, 1), 0, random.nextInt(-1, 1)
        );
        World world = location.getWorld();
        if (world == null || target == null)return;
        int lifeTimer = 60*20;
        int cooldownTimer = 10*20;
        switch (summonType){
            case BEE -> {location = owner.getEyeLocation(); lifeTimer = 30*20; cooldownTimer = 2*20;}
            case SILVERFISH -> {cooldownTimer = 4*20;}
        }
        Mob summon = (Mob) world.spawnEntity(location, summonType);

        summons.add(summon);
        summonLifeTimerMap.put(summon, lifeTimer);
        summonCooldownTimerMap.put(owner, cooldownTimer);

        PersistentDataContainer data = summon.getPersistentDataContainer();
        data.set(ownerNSK, PersistentDataType.STRING, owner.getUniqueId().toString());
        data.set(targetNSK, PersistentDataType.STRING, target.getUniqueId().toString());

        summon.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, lifeTimer, 0, false));
        switch (summonType){
            case BEE -> {
                Bee bee = (Bee) summon;
                bee.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30*20, 0, false));
                bee.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30*20,1,false));
                bee.setAnger(99);
                bee.setHealth(3.0);
            }
            case WOLF -> {
                Wolf wolf = (Wolf) summon;
                wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20,0,false));
                wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20,1,false));
                wolf.setAngry(true);
                wolf.setCollarColor(DyeColor.BLACK);
                AVFX.playWolfSummonEffect(location);
            }
            case SILVERFISH -> {
                summon.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20,0,false));
                summon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20,1,false));
            }
        }
        summon.setTarget(target);
    }

}
