package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.effect.WorldMechanics;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

public class FurnaceEvents implements Listener {

    @EventHandler
    public void onFurnaceStartSmelting(FurnaceStartSmeltEvent e){
        World world = e.getBlock().getWorld();
        Block headBlock = world.getBlockAt(e.getBlock().getLocation().add(0, 1, 0));
        MobHead mobHead = HeadUtil.getHeadFromBlock(headBlock);
        if (mobHead == null)return;
        WorldMechanics.furnaceStartCooking(e, mobHead);
    }

    @EventHandler
    public void onFurnaceBurnFuel(FurnaceBurnEvent e){
        World world = e.getBlock().getWorld();
        Block headBlock = world.getBlockAt(e.getBlock().getLocation().add(0, 1, 0));
        MobHead mobHead = HeadUtil.getHeadFromBlock(headBlock);
        if (mobHead == null)return;
        WorldMechanics.furnaceBurnFuel(e, mobHead);
    }

    
}
