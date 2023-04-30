package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class UseItemEvents implements Listener {

    @EventHandler
    public static void onWearerUseItem(PlayerInteractEvent e){
        Player player = e.getPlayer();
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(player);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        ItemStack usedItem = e.getItem();
        Material usedMat = null;
        if (usedItem != null) usedMat = usedItem.getType();
        Action action = e.getAction();
        Event.Result resultItem = e.useItemInHand();
        Event.Result resultBlock = e.useInteractedBlock();
        Block clickedBlock = e.getClickedBlock();
        Material blockMat = null;
        if (clickedBlock != null) blockMat = clickedBlock.getType();

        switch (headType){
            default -> {}

        }

    }



}
