package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractWithEntityEvent implements Listener {

    @EventHandler
    public static void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        Player player = e.getPlayer();
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(player);
        if (mobHead == null)return;
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case FROG -> {WornMechanics.interactAtEntityMechanicFrog(e,mobHead);}
        }
    }

}
