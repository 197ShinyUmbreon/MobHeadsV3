package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.SkullBreakPlace;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import static io.github.shinyumbreon197.mobheadsv3.MobHead.skullItemIsMobHead;

public class BlockPlaceAndBreak implements Listener {

    @EventHandler
    public static void blockPlace(BlockPlaceEvent bpe){
        ItemStack itemInHand = bpe.getItemInHand();
        if (skullItemIsMobHead(itemInHand)){
            SkullBreakPlace.placeMobHeadSkull(bpe);
        }else if (CreatureEvents.chestedItemIsContainer(itemInHand)){
            CreatureEvents.chestedPlaceContainer(bpe.getPlayer(), bpe.getBlockPlaced(), bpe.getItemInHand());
        }

    }

    @EventHandler
    public static void blockBreak(BlockBreakEvent bbe){

    }

    @EventHandler
    public static void blockDropItem(BlockDropItemEvent bdie){
        Player player = bdie.getPlayer();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        boolean isSneaking = player.isSneaking();
        Location dropLoc = bdie.getBlock().getLocation().add(0.5,0.5,0.5);
        switch (headType){
            case BLAZE -> {
                if (!isSneaking){
                    CreatureEvents.blazeBreakBlock(player, dropLoc, bdie.getItems());
                    bdie.setCancelled(true);
                }

            }
        }
    }

}
