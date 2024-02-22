package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.SkullBreakPlace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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

}
