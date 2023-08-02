package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Data;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.HeadEquip;
import io.github.shinyumbreon197.mobheadsv3.function.SkullInteract;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerInteractEvents implements Listener {

    private static final List<Player> swapping = new ArrayList<>();

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent pie){
        if (debug) System.out.println("onPlayerInteract: hand: " + pie.getHand()); //debug
        Player player = pie.getPlayer();
        boolean sneaking = pie.getPlayer().isSneaking();
        Block block = pie.getClickedBlock();
        boolean isBlock = block != null;
        ItemStack item = pie.getItem();
        boolean isItem = item != null && !item.getType().equals(Material.AIR);
        Action action = pie.getAction();
        EquipmentSlot hand = pie.getHand();
        boolean mainHand = hand != null && hand.equals(EquipmentSlot.HAND);
        boolean offHand = hand != null && hand.equals(EquipmentSlot.OFF_HAND);
        if (debug) System.out.println("block: " + block + "\nitem: " + item + "\naction: " + action); //debug

        if (!isBlock && isItem && swapping.contains(player)){
            if (debug) System.out.println("!isBlock && isItem && swapping.contains(player)"); //debug
            pie.setCancelled(true);
            pie.setUseItemInHand(Event.Result.DENY);
            return;
        }
//        if (isItem && !isBlock && !sneaking && action.equals(Action.RIGHT_CLICK_AIR) && MobHead.skullItemIsMobHead(item)){
//            swapping.add(player);
//            HeadEquip.clickEquipHead(pie); // BROKEN. CANNOT REPLACE WHEN WEARING HELMET! (Fires twice per tick, re-equips helmet)
//            new BukkitRunnable(){
//                @Override
//                public void run() {
//                    swapping.remove(player);
//                }
//            }.runTaskLater(MobHeadsV3.getPlugin(),2);
//        }
        if (isBlock && action.equals(Action.RIGHT_CLICK_BLOCK) && !sneaking && mainHand){
            SkullInteract.skullInteract(pie);
        }

        MobHead mobHead = MobHead.getMobHeadWornByEntity(pie.getPlayer());
        if (mobHead == null)return;

        if (isBlock) headedPlayerInteractBlock(mobHead, pie, isItem);
        if (isItem) headedPlayerInteractItem(mobHead, pie, isBlock);
    }

    @EventHandler
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent piee){
        MobHead interactedMobHead = MobHead.getMobHeadWornByEntity(piee.getRightClicked());
        MobHead interactorMobHead = MobHead.getMobHeadWornByEntity(piee.getPlayer());
        if (interactedMobHead == null && interactorMobHead == null)return;

        if (interactedMobHead != null) playerInteractHeadedEntity(interactedMobHead, piee);
        if (interactorMobHead != null) headedPlayerInteractEntity(interactorMobHead, piee);
    }

    private static void headedPlayerInteractItem(MobHead mobHead, PlayerInteractEvent pie, boolean blockInteract){
        EntityType headType = mobHead.getEntityType();
        ItemStack itemStack = pie.getItem();
        assert itemStack != null;
        Action action = pie.getAction();
        List<Action> rightClickActions = List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);


        switch (headType){
            case PIG -> {
                if (rightClickActions.contains(action) && Data.getFoodMats().contains(itemStack.getType())){
                    CreatureEvents.pigGobble(pie);
                }
            }
            case CREEPER -> {
                if (rightClickActions.contains(action) && itemStack.getType().equals(Material.GUNPOWDER)){
                    CreatureEvents.creeperExplodeGunpowder(pie);
                }
            }
        }

    }

    private static void headedPlayerInteractBlock(MobHead mobHead, PlayerInteractEvent pie, boolean itemInteract){
        if (debug) System.out.println("headedPlayerInteractBlock() mobHead: " + mobHead.getDisplayName());
        Player player = pie.getPlayer();
        boolean sneaking = player.isSneaking();
        EntityType headType = mobHead.getEntityType();
        Block block = pie.getClickedBlock();
        assert block != null;
        Action action = pie.getAction();
        BlockFace blockFace = pie.getBlockFace();

        switch (headType){
            case SHEEP -> {
                if (action.equals(Action.RIGHT_CLICK_BLOCK) && !sneaking){
                    CreatureEvents.sheepEatGrass(player, block, blockFace);
                }
            }
        }

    }

    private static void playerInteractHeadedEntity(MobHead mobHead, PlayerInteractEntityEvent piee){
        Player player = piee.getPlayer();
        Entity headedEnt = piee.getRightClicked();
        if (!(headedEnt instanceof LivingEntity))return;
        LivingEntity livHeadedEnt = (LivingEntity) headedEnt;
        EntityType headType = mobHead.getEntityType();

        switch (headType){
            case COW -> CreatureEvents.milkCow(player,livHeadedEnt);
            case MUSHROOM_COW -> CreatureEvents.soupMooshroom(player,livHeadedEnt);
        }
    }

    private static void headedPlayerInteractEntity(MobHead mobHead, PlayerInteractEntityEvent piee){
        Player player = piee.getPlayer();
        Entity clicked = piee.getRightClicked();
        EntityType headType = mobHead.getEntityType();
        EquipmentSlot hand = piee.getHand();
        boolean sneaking = player.isSneaking();

        switch (headType){
            case FROG -> {
                if (sneaking && hand.equals(EquipmentSlot.HAND)) CreatureEvents.frogEatCreature(player, clicked);
            }
        }

    }

}
