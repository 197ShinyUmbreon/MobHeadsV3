package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.HeadStorage;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
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
        CreatureEvents.chestedAddHolder(player);
        HeadStorage.loadUserHeadStorageFromFile(player);
        UUID uuid = player.getUniqueId();
        MobHead mobHead = MobHead.getMobHeadFromUUID(uuid);
        if (mobHead != null){
            PlayerHead.updatePlayerFile(player, mobHead);
        }else{
            PlayerHead.writeNewPlayerToFile(player);
        }
        MobHeadsV3.messagePlayer(player,
                ChatColor.YELLOW + "This server has MobHeads enabled. Use /mobheads to see all of them."
        );
    }

}
