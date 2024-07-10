package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Decoy {

    private static final NamespacedKey key = Key.decoy;

    private static void applyKey(Entity decoy){
        PersistentDataContainer data = decoy.getPersistentDataContainer();
        data.set(key, PersistentDataType.STRING, "decoy");
    }
    private static void removeKey(Entity decoy){
        PersistentDataContainer data = decoy.getPersistentDataContainer();
        data.remove(key);
    }
    public static boolean isDecoy(Entity decoy){
        PersistentDataContainer data = decoy.getPersistentDataContainer();
        return data.has(key,PersistentDataType.STRING);
    }
    public static boolean hasDecoy(LivingEntity target){
        for (Entity passenger: target.getPassengers()){
            if (isDecoy(passenger))return true;
        }
        return false;
    }

    public static void addDecoyToCreature(LivingEntity target, EntityType decoyType){
        Entity decoy = target.getWorld().spawnEntity(target.getLocation(), decoyType);
        decoy.setInvulnerable(true);
        decoy.setPersistent(false);
        decoy.setSilent(true);
        if (decoy instanceof LivingEntity){
            LivingEntity livingDecoy = (LivingEntity) decoy;
            livingDecoy.setAI(false);
            livingDecoy.setCanPickupItems(false);
            livingDecoy.setCollidable(false);
            livingDecoy.setInvisible(true);
            livingDecoy.setRemoveWhenFarAway(true);
        }
        applyKey(decoy);
        target.addPassenger(decoy);
    }
    public static void removeDecoyFromCreature(LivingEntity target){
        for (Entity passenger: target.getPassengers()){
            if (isDecoy(passenger)){
                passenger.remove();
                return;
            }
        }
    }

}
