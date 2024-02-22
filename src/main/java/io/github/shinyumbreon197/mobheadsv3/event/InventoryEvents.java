package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class InventoryEvents implements Listener {
    // InventoryClickEvent ---------------------------------------------------------------------------------------------
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent ice){
        Inventory clickedInv = ice.getClickedInventory();
        InventoryView inventoryView = ice.getView();
        List<HumanEntity> viewers = ice.getViewers();
        Player player = null;
        if (viewers.size() != 0){
            player = (Player) ice.getViewers().get(0);
        }
        ClickType clickType = ice.getClick();
        ItemStack currentItem = ice.getCurrentItem();
        InventoryAction action = ice.getAction();
        int slot = ice.getSlot();
        ItemStack cursor = ice.getCursor();
        int hotbarButton = ice.getHotbarButton();
        if (player != null && clickType.equals(ClickType.CREATIVE)) onCreativeInventoryClick(player); // Bugged, will transmutate head if looking at another while in inv
//        if (cursor != null && cursor.getType().equals(Material.BUNDLE)){
//            ice.setCancelled(CreatureEvents.chestedItemIsContainer(currentItem));
//        }else if (currentItem != null && currentItem.getType().equals(Material.BUNDLE)){
//            ice.setCancelled(CreatureEvents.chestedItemIsContainer(cursor));
//        }else
        if (CreatureEvents.chestedItemIsContainer(currentItem) || CreatureEvents.chestedItemContainsContainer(currentItem)){
            if (debug) System.out.println("InventoryType: " + inventoryView.getType());
            ice.setCancelled(chestedCancelInventoryEvent(inventoryView.getType()));
        }
    }
    private static boolean chestedCancelInventoryEvent(InventoryType type){
        if (!type.equals(InventoryType.CRAFTING) && !type.equals(InventoryType.CREATIVE))return true;
        return false;
    }
    private static void onCreativeInventoryClick(Player player){
        if (!player.getGameMode().equals(GameMode.CREATIVE))return;
        Block target = player.getTargetBlockExact(6);
        if (target == null)return;
        new BukkitRunnable(){
            @Override
            public void run() {
                ItemStack pickedItem = player.getInventory().getItemInMainHand();
                if (!(pickedItem.getItemMeta() instanceof SkullMeta))return;
                MobHead mobHead = MobHead.getMobHeadFromBlock(target);
                if (mobHead == null)return;
                player.getInventory().setItemInMainHand(mobHead.getHeadItemStack());
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),2);
    }

    // InventoryPickupItemEvent ----------------------------------------------------------------------------------------
    @EventHandler
    public static void onInventoryPickupItem(InventoryPickupItemEvent ipie){
        ItemStack itemStack = ipie.getItem().getItemStack();

        ipie.setCancelled(CreatureEvents.chestedItemIsContainer(itemStack));
    }




}
