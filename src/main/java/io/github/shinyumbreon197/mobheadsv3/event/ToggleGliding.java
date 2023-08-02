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
        MobHead mobHead = MobHead.getMobHeadWornByEntity(target);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        boolean isGliding = etge.isGliding();

        switch (headType){
            case ENDER_DRAGON, GOAT -> {
                if (debug) System.out.println("onToggleGliding: isGliding: " + isGliding); //debug
                if (isGliding){
                    CreatureEvents.addToCreatureWasGliding(target);
                }else{
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            CreatureEvents.removeFromCreatureWasGliding(target);
                        }
                    }.runTaskLater(MobHeadsV3.getPlugin(),1);
                }
                if (isGliding && target instanceof Player){
                    if (headType.equals(EntityType.ENDER_DRAGON)) CreatureEvents.enderDragonElytraTakeoff((Player) target,false);
                    if (headType.equals(EntityType.GOAT)) CreatureEvents.goatTakeOff((Player) target, false);
                }
            }
        }
    }

}
