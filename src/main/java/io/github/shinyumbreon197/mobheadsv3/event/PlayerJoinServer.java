package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.PlayerHead;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PlayerJoinServer implements Listener {

    @EventHandler
    public void onPlayerJoinServer(org.bukkit.event.player.PlayerJoinEvent e){
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!MobHead.isUUIDRegistered(uuid)){
            PlayerHead.writeNewPlayerToFile(player);
        }
        MobHeadsV3.messagePlayer(player,
                ChatColor.YELLOW + "This server has MobHeads enabled. Use /mobheads to see all of them."
        );
    }

}
