package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.tool.EffectUtil;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class TestEvents implements Listener {

    @EventHandler
    public void showBlocksInFront(PlayerMoveEvent e){
        Player player = e.getPlayer();
        List<Block> blocks = EffectUtil.getFacingBlocks(player.getLocation(),player.getFacing(), 5,2);
        for (Block block:blocks){
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, block.getLocation().add(new Vector(0.5, 0.5, 0.5)),
                    1,0,0,0,0);
        }


    }

//    @EventHandler
//    public void giveSpawnedZombiesHead(org.bukkit.event.entity.CreatureSpawnEvent e){
//        if (e.getEntity().getType().equals(EntityType.ZOMBIE)){
//            Zombie zombie = (Zombie) e.getEntity();
//            if (zombie.getEquipment() != null){
//                Random random = new Random();
//                int randInt = random.nextInt(Data.getMobHeads().size());
//                zombie.getEquipment().setHelmet(Data.getMobHeads().get(randInt).getHeadItem());
//            }
//
//        }
//    }



}
