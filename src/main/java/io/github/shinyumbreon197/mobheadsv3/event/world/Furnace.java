package io.github.shinyumbreon197.mobheadsv3.event.world;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.WorldEvents;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

public class Furnace implements Listener {

    @EventHandler
    public void onFurnaceStartSmelting(FurnaceStartSmeltEvent e){
        World world = e.getBlock().getWorld();
        Block headBlock = world.getBlockAt(e.getBlock().getLocation().add(0, 1, 0));
        MobHead mobHead = MobHead.getMobHeadFromBlock(headBlock);
        if (mobHead == null)return;
        WorldEvents.furnaceStartCooking(e, mobHead);
    }

    @EventHandler
    public void onFurnaceBurnFuel(FurnaceBurnEvent e){
        World world = e.getBlock().getWorld();
        Block headBlock = world.getBlockAt(e.getBlock().getLocation().add(0, 1, 0));
        MobHead mobHead = MobHead.getMobHeadFromBlock(headBlock);
        if (mobHead == null)return;
        WorldEvents.furnaceBurnFuel(e, mobHead);
    }

    
}
