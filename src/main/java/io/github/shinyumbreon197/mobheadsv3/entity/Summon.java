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

    public static boolean isSummon(Mob summon){
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
            if (target == null || target.isDead()){
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
                if (target == null || target.isDead()){
                    summonsToBeRemoved.add(summon);
                    continue;
                }
            }
            EntityType summonType = summon.getType();
            switch (summonType){
                case WOLF -> {
                    Wolf wolf = (Wolf) summon;
                    wolf.setTarget(target);
                }
                case SILVERFISH -> {
                    Silverfish silverfish = (Silverfish) summon;
                    silverfish.setTarget(target);
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

    public static Silverfish silverfishSummon(LivingEntity owner, Location location, LivingEntity target){
        if (summonCooldownTimerMap.get(owner) != null)return null;
        World world = location.getWorld();
        if (world == null || target == null || owner == null)return null;
        Silverfish silverfish = (Silverfish) world.spawnEntity(location, EntityType.SILVERFISH);

        summons.add(silverfish);
        summonLifeTimerMap.put(silverfish, 60*20);
        summonCooldownTimerMap.put(owner, 5*20);

        PersistentDataContainer data = silverfish.getPersistentDataContainer();
        data.set(ownerNSK, PersistentDataType.STRING, owner.getUniqueId().toString());
        data.set(targetNSK, PersistentDataType.STRING, target.getUniqueId().toString());

        silverfish.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20,0,false));
        silverfish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20,1,false));

        silverfish.setTarget(target);

        return silverfish;
    }

}
