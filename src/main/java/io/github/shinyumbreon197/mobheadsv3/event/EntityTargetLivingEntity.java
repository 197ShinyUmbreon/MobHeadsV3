package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class EntityTargetLivingEntity implements Listener {

    private static final NamespacedKey lastHeadedTargetKey = Key.lastHeadedTarget;

    @EventHandler
    public static void entityTargetLivingEntity(EntityTargetLivingEntityEvent ete){
        Entity targeted = ete.getTarget();
        Entity targeting = ete.getEntity();
        MobHead targetedHead = MobHead.getMobHeadWornByEntity(targeted);
        MobHead targetingHead = MobHead.getMobHeadWornByEntity(targeting);

        if (targetedHead != null) entityTargetHeadedEntity(targetedHead,ete);
        if (targetingHead != null) headedEntityTargetEntity(targetingHead,ete);

    }
    private static void entityTargetHeadedEntity(MobHead targetedHead, EntityTargetLivingEntityEvent ete){
        if (debug) System.out.println("entityTargetHeadedEntity() "); //debug
        if (!(ete.getEntity() instanceof Mob))return;
        Mob targeting = (Mob) ete.getEntity();
        LivingEntity targeted = ete.getTarget();
        if (targeted == null)return;
        if (CreatureEvents.hasMobBeenAttacked(targeted, targeting)){
            if (debug) System.out.println("mobHasBeenAttacked() -> true; Targeted: " + targeted.getName() + " Mob: " + targeting.getName());
            return;
        }
        boolean monster = targeting instanceof Monster;
        EntityType targetingType = targeting.getType();
        EntityType targetedHeadType = targetedHead.getEntityType();
        EntityTargetLivingEntityEvent.TargetReason reason = ete.getReason();
        if (debug) System.out.println("reason: " + reason); //debug
        PersistentDataContainer targetingData = targeting.getPersistentDataContainer();

        if (Groups.neutralTarget(targetingType, targetedHeadType)){
            boolean lastHeadedTargetIsTarget = false;
            if (targetingData.has(lastHeadedTargetKey, PersistentDataType.STRING)){
                String lastHeadedTargetUUIDString = targetingData.get(lastHeadedTargetKey,PersistentDataType.STRING);
                assert lastHeadedTargetUUIDString != null;
                UUID lastHeadedTargetUUID = UUID.fromString(lastHeadedTargetUUIDString);
                Entity lastHeadedTarget = Bukkit.getEntity(lastHeadedTargetUUID);
                if (lastHeadedTarget != null && lastHeadedTarget.equals(targeted)) lastHeadedTargetIsTarget = true;
            }

            boolean neutralResponse = !reason.equals(EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) && !lastHeadedTargetIsTarget;
            if (debug) System.out.println("neutralGroup: " + "\nneutralResponse: " + neutralResponse); //debug
            if (neutralResponse){
                if (debug) System.out.println("Neutral Response");
                ete.setCancelled(true);
            }else{
                if (debug) System.out.println("Hostile Response, recruiting nearby.");
                //CreatureEvents.nearbyTargetImposter(targeting, targeted, targetedHeadType);
            }
        }

        if (monster){
            switch (targetedHeadType){
                case PARROT -> {
                    if (!reason.equals(EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY)){
                        CreatureEvents.parrotTargeted(targeted, targeting);
                    }
                }
            }
        }else{

        }

    }

    private static void headedEntityTargetEntity(MobHead targetingHead, EntityTargetLivingEntityEvent ete){

    }

}
