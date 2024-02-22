package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
import org.bukkit.Location;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class WorldEvents {

    public static void furnaceStartCooking(FurnaceStartSmeltEvent e, Block blazeHead){
        e.setTotalCookTime(e.getTotalCookTime()/2);
        Location headLoc = blazeHead.getLocation().add(0.5, 0.1, 0.5);
        Location furnaceLoc = furnaceFXLocation(e.getBlock());
        BlockFace facing = getFurnaceFacing(e.getBlock());
        boolean facingX = facing != null && (facing.equals(BlockFace.EAST) || facing.equals(BlockFace.WEST));
        AVFX.playBlazeHeadBurnEffect(headLoc, false, false, facingX, false);
        AVFX.playBlazeHeadBurnEffect(furnaceLoc, false, true, facingX, false);
    }

    public static void furnaceBurnFuel(FurnaceBurnEvent e, Block blazeHead){
        boolean invalid = e.getBurnTime() == 0;
        e.setBurnTime(e.getBurnTime()/2);
        Location headLoc = blazeHead.getLocation().add(0.5, 0.1, 0.5);
        Location furnaceLoc = furnaceFXLocation(e.getBlock());
        BlockFace facing = getFurnaceFacing(e.getBlock());
        boolean facingX = facing != null && (facing.equals(BlockFace.EAST) || facing.equals(BlockFace.WEST));
        if (!invalid){
            AVFX.playBlazeHeadBurnEffect(headLoc, true, false, facingX, false);
            AVFX.playBlazeHeadBurnEffect(furnaceLoc, true, true, facingX, false);
        }else{
            AVFX.playBlazeHeadBurnEffect(furnaceLoc, true,true,facingX,true);
        }
    }

    private static Location furnaceFXLocation(Block furnace){
        BlockFace facing = getFurnaceFacing(furnace);
        if (facing == null) return furnace.getLocation().add(0.5,0.5,0.5);
        Vector offset;
        switch (facing){
            default -> offset = new Vector();
            case NORTH -> {offset = new Vector(0, -0.2, -0.5);} // Towards Negative Z
            case EAST -> {offset = new Vector(0.5, -0.2, 0);} // Towards Positive X
            case SOUTH -> {offset = new Vector(0, -0.2, 0.5);} // Towards Positive Z
            case WEST -> {offset = new Vector(-0.5, -0.2, 0);} // Towards Negative X
        }
        return furnace.getLocation().add(0.5,0.5,0.5).add(offset);
    }
    public static BlockFace getFurnaceFacing(Block furnace){
        if (!(furnace.getBlockData() instanceof Furnace))return null;
        return ((org.bukkit.block.data.type.Furnace) furnace.getBlockData()).getFacing();
    }

    private static final List<Player> skullInteractCooldown = new ArrayList<>();
    private static void addPlayerToSkullInteractCooldown(Player player){
        skullInteractCooldown.add(player);
    }
    private static void removePlayerFromSkullInteractCooldown(Player player){
        skullInteractCooldown.remove(player);
    }
    private static boolean isOnSkullInteractCooldown(Player player){
        return skullInteractCooldown.contains(player);
    }
    public static void mobHeadSkullInteract(MobHead mobHead, PlayerInteractEvent pie){
        if (debug) System.out.println("mobHeadSkullInteract()"); //debug
        Player player = pie.getPlayer();
        if (isOnSkullInteractCooldown(player))return;
        EntityType headType = mobHead.getEntityType();
        Block skullBlock = pie.getClickedBlock();
        assert skullBlock != null;
        pie.setUseItemInHand(Event.Result.DENY);
        pie.setUseInteractedBlock(Event.Result.DENY);
        Location blockCenter = skullBlock.getLocation().add(0.5,0.5,0.5);

        AVFX.playHeadInteractEffect(blockCenter,headType);
        addPlayerToSkullInteractCooldown(player);
        new BukkitRunnable(){
            @Override
            public void run() {
                removePlayerFromSkullInteractCooldown(player);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 10);
    }

}
