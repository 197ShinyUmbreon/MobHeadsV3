package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryEvents implements Listener {

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent ice){
        Inventory clickedInv = ice.getClickedInventory();
        InventoryView inventoryView = ice.getView();
        ClickType clickType = ice.getClick();
        ItemStack currentItem = ice.getCurrentItem();
        InventoryAction action = ice.getAction();
        int slot = ice.getSlot();
        ItemStack cursor = ice.getCursor();
        int hotbarButton = ice.getHotbarButton();


        if (clickType.isCreativeAction()) onCreativeInventoryClick(ice); //bugged. will overwrite head if clicking in inventory while looking at head

    }

    private static void onCreativeInventoryClick(InventoryClickEvent ice){
        Player player = (Player) ice.getViewers().get(0);
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

}
