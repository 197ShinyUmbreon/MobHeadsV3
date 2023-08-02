package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickUpItem implements Listener {

    @EventHandler
    public static void onPickUpItem(EntityPickupItemEvent epie){
        LivingEntity target = epie.getEntity();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(target);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        switch (headType){
            case CHICKEN -> {
                if (target instanceof Player)CreatureEvents.chickenPickUpEgg(epie);
            }
        }
    }

}
