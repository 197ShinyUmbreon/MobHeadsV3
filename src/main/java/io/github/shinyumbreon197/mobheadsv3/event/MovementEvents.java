package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.util.Vector;

public class MovementEvents implements Listener {
    @EventHandler //PLAYER ONLY
    public static void onMove(PlayerMoveEvent e){
        if (e.getPlayer().getInventory().getHelmet() == null)return;
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(e.getPlayer());
        if (mobHead == null)return;
        Player player = e.getPlayer();
        switch (mobHead.getEntityType()){
            default -> {}

        }
    }

    @EventHandler
    public static void onStatisticIncrement(PlayerStatisticIncrementEvent e){
        if (e.getPlayer().getInventory().getHelmet() == null)return;
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(e.getPlayer());
        if (mobHead == null)return;
        Player player = e.getPlayer();
        switch (e.getStatistic()){
            default -> {}
            case JUMP -> {
                switch (mobHead.getEntityType()){
                    default -> {}
                    case FROG -> {WornMechanics.frogJump(player);}
                }
            }
        }

    }

}
