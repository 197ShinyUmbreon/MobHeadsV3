package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.SkullBreakPlace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemSpawnDespawnEvents implements Listener {

    @EventHandler
    public static void itemSpawn(ItemSpawnEvent ise){
        Item item = ise.getEntity();
        ItemStack itemStack = ise.getEntity().getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof SkullMeta){
            SkullBreakPlace.getMobHeadSkullFromBrokenSkull(ise);
        }
        if (CreatureEvents.chestedItemIsContainer(itemStack) || CreatureEvents.chestedItemContainsContainer(itemStack)){
            CreatureEvents.chestedWatchDroppedContainer(item);
        }
    }

    @EventHandler
    public static void itemDespawn(ItemDespawnEvent ide){
        Item item = ide.getEntity();
        ItemStack itemStack = item.getItemStack();

//        if (CreatureEvents.chestedItemIsContainer(itemStack)){
//            ide.setCancelled(true);
//            CreatureEvents.chestedContainerItemExplode(item);
//            item.remove();
//        }

    }

}
