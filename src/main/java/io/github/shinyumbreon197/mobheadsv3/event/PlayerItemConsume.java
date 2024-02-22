package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Set;

public class PlayerItemConsume implements Listener {

    @EventHandler
    public static void onDrinkMilk(PlayerItemConsumeEvent pice){
        MobHead mobHead = MobHead.getMobHeadWornByEntity(pice.getPlayer());
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        Player player = pice.getPlayer();
        Material consumedType = pice.getItem().getType();
        switch (consumedType){
            case MILK_BUCKET -> {
                if (!Groups.isSkeletal(headType))return;
                CreatureEvents.skeletonDrinkMilk(player);
            }
        }

    }

}
