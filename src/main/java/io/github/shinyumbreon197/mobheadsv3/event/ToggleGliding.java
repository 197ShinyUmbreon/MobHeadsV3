package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class ToggleGliding implements Listener {

    @EventHandler
    public static void onToggleGliding(EntityToggleGlideEvent etge){
        LivingEntity target = (LivingEntity) etge.getEntity();
        boolean isGliding = etge.isGliding();
        if (debug) System.out.println("onToggleGliding: isGliding: " + isGliding); //debug

        if (isGliding){
            CreatureEvents.chestedInterruptGliding(target);
            CreatureEvents.addToCreatureWasGliding(target);
            if (debug) System.out.println("Added to CreatureWasGliding"); //debug
        }else{
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!target.isGliding()){
                        if (debug) System.out.println("Removed from CreatureWasGliding"); //debug
                        CreatureEvents.removeFromCreatureWasGliding(target);
                    }
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),2);
        }

        MobHead mobHead = MobHead.getMobHeadWornByEntity(target);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();

        switch (headType){
            case FOX -> {
                if (!isGliding && target instanceof Player && CreatureEvents.foxIsPlayerPouncing((Player) target)){
                    //if (debug) System.out.println("Pouncing fox that was gliding attempted to have it's flight toggled!");
                    etge.setCancelled(true);
                }
            }
            case ENDER_DRAGON, GOAT -> {
                if (debug) System.out.println("onToggleGliding: isGliding: " + isGliding); //debug
                if (isGliding && target instanceof Player){
                    if (headType.equals(EntityType.ENDER_DRAGON)) CreatureEvents.enderDragonElytraTakeoff((Player) target,false);
                    if (headType.equals(EntityType.GOAT)) CreatureEvents.goatTakeOff((Player) target, false);
                }
            }
            case BREEZE -> {
                if (isGliding) CreatureEvents.breezeElytraWindCharge(target);
            }
        }
    }

}
