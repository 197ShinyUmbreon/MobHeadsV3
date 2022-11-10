package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.head.FoxHead;
import io.github.shinyumbreon197.mobheadsv3.head.CowHead;
import io.github.shinyumbreon197.mobheadsv3.head.ZombieHead;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class TestEvents implements Listener {

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        if (!e.getHand().equals(EquipmentSlot.HAND))return;

        EntityType entityType = e.getRightClicked().getType();
        if (entityType.equals(EntityType.COW)){
            CowHead.onTest(e);
        }else if (entityType.equals(EntityType.FOX)){
            FoxHead.onTest(e);
        }else if (entityType.equals(EntityType.ZOMBIE)){
            ZombieHead.onTest(e);
        }
    }

}
