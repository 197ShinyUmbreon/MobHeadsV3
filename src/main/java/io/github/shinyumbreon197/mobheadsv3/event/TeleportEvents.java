package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportEvents implements Listener {

    @EventHandler
    public static void onWearerTeleport(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(player);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        PlayerTeleportEvent.TeleportCause cause = e.getCause();
        Location fromLoc = e.getFrom();
        Location toLoc = e.getTo();
        if (toLoc == null)return;
        switch (headType){
            default -> {}
            case ENDERMAN -> {
                if (cause.equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)){
                    e.setCancelled(true);
                    player.teleport(toLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    AVFX.playEndermanTeleportSound(fromLoc);
                    AVFX.playEndermanTeleportSound(toLoc);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            WornMechanics.endermanRegeneratePearl(player);
                        }
                    }.runTaskLater(MobHeadsV3.getPlugin(), 5);
                }
            }
        }
    }

}
