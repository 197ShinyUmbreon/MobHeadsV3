package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class WorldEvents {

    public static void furnaceStartCooking(FurnaceStartSmeltEvent e, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case BLAZE -> {
                e.setTotalCookTime(e.getTotalCookTime()/2);
                Location headLoc = e.getBlock().getLocation().add(0.5, 1.1, 0.5);
                AVFX.playBlazeHeadBurnEffect(headLoc, false);
            }
        }
    }

    public static void furnaceBurnFuel(FurnaceBurnEvent e, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case BLAZE -> {
                e.setBurnTime(e.getBurnTime()/2);
                Location headLoc = e.getBlock().getLocation().add(0.5, 1.1, 0.5);
                AVFX.playBlazeHeadBurnEffect(headLoc, true);
            }
        }
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
