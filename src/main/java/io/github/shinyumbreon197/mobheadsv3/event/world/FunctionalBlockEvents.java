package io.github.shinyumbreon197.mobheadsv3.event.world;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.WorldEvents;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FunctionalBlockEvents implements Listener {
    // General Helper Functions ----------------------------------------------------------------------------------------
    private static List<Block> getBlocksInRadius(Location origin, int radius){
        List<Block> blocks = new ArrayList<>();
        World world = origin.getWorld();
        if (world == null) return blocks;
        int originX = origin.getBlockX();
        int originZ = origin.getBlockZ();
        int originY = origin.getBlockY();
        List<Vector> points = new ArrayList<>();
        points.add(new Vector(0,0,0));
        int negPoint = -(radius - 1);
        for (int y = negPoint; y < radius; y++) {
            for (int z = negPoint; z < radius; z++) {
                for (int x = negPoint; x < radius; x++) {
                    Vector point = new Vector(x,y,z);
                    if (points.contains(point))continue;
                    points.add(point);
                }
            }
        }
        for (Vector point:points){
            blocks.add(new Location(world, originX + point.getX(), originY + point.getY(), originZ + point.getZ()).getBlock());
        }
        return blocks;
    }
    private static Block searchForSkullBlock(Block funcBlock, EntityType headType){
        for (Block block:getBlocksInRadius(funcBlock.getLocation(),2)){
            MobHead mobHead = MobHead.getMobHeadFromBlock(block);
            if (mobHead == null || !mobHead.getEntityType().equals(headType))continue;
            return block;
        }
        return null;
    }

    // Brewing Stand ---------------------------------------------------------------------------------------------------
    @EventHandler
    public static void onBrewingStandStartBrewing(BrewingStartEvent bse){
        Block brewingStand = bse.getBlock();
        Block witchHead = searchForSkullBlock(brewingStand, EntityType.WITCH);
        if (witchHead == null)return;
        WorldEvents.brewingStandStartBrewing(bse, witchHead);
    }

    // Furnace ---------------------------------------------------------------------------------------------------------
    @EventHandler
    public static void onFurnaceStartSmelting(FurnaceStartSmeltEvent e){
        Block blazeHead = searchForSkullBlock(e.getBlock(), EntityType.BLAZE);
        if (blazeHead == null)return;
        WorldEvents.furnaceStartCooking(e, blazeHead);
    }

    @EventHandler
    public static void onFurnaceBurnFuel(FurnaceBurnEvent e){
        Block blazeHead = searchForSkullBlock(e.getBlock(), EntityType.BLAZE);
        if (blazeHead == null)return;
        WorldEvents.furnaceBurnFuel(e, blazeHead);
    }




    
}
