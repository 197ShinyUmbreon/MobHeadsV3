package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.function.SkullBreakPlace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemSpawn implements Listener {

    @EventHandler
    public static void itemSpawn(ItemSpawnEvent ise){
        ItemStack itemStack = ise.getEntity().getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof SkullMeta){
            SkullBreakPlace.getMobHeadSkullFromBrokenSkull(ise);
        }
    }

}
