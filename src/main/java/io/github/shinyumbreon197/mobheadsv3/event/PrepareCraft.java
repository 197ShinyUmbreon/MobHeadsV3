package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareCraft implements Listener {

    @EventHandler
    public static void onPrepareCraft(PrepareItemCraftEvent pice){
        if (pice.getRecipe() != null)return;
        ItemStack[] grid = pice.getInventory().getMatrix();
        //if (debug) System.out.println("onPrepareCraft() grid: " + Arrays.toString(grid)); //debug
        ItemStack head = null;
        boolean anomoly = false;
        for (int i = 0; i < grid.length; i++) {
            ItemStack slotItem = grid[i];
            if (slotItem == null){
                continue;
            }else if (MobHead.skullItemIsMobHead(slotItem) && head == null){
                head = slotItem;
            }else anomoly = true;
        }
        //if (debug) System.out.println("onPrepareCraft() head: " + head); //debug
        //if (debug) System.out.println("onPrepareCraft() anomoly: " + anomoly); //debug
        if (anomoly || head == null)return;
        MobHead mobHead = MobHead.getMobHeadFromSkullItemStack(head);
        //if (debug) System.out.println("onPrepareCraft() mobHead: " + mobHead); //debug
        if (mobHead == null)return;
        ItemStack loot = mobHead.getHeadLootItemStack();
        //if (debug) System.out.println("onPrepareCraft() loot: " + loot); //debug
        pice.getInventory().setResult(loot);
    }

}
