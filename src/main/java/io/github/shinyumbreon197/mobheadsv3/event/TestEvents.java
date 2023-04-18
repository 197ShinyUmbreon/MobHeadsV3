package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.Data;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Random;

public class TestEvents implements Listener {

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        if (!e.getHand().equals(EquipmentSlot.HAND))return;


    }

    @EventHandler
    public void giveSpawnedZombiesHead(org.bukkit.event.entity.CreatureSpawnEvent e){
        if (e.getEntity().getType().equals(EntityType.ZOMBIE)){
            Zombie zombie = (Zombie) e.getEntity();
            if (zombie.getEquipment() != null){
                Random random = new Random();
                int randInt = random.nextInt(Data.getMobHeads().size());
                zombie.getEquipment().setHelmet(Data.getMobHeads().get(randInt).getHeadItem());
            }

        }
    }



}
