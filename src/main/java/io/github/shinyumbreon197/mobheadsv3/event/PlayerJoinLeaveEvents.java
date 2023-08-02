package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.head.PlayerHead;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvents implements Listener {

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent e){
        if (PlayerHead.isNewPlayer(e.getPlayer())){
            PlayerHead.writeNewPlayerToFile(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLeaveServer(PlayerQuitEvent e){
        //PlayerHead.unregisterPlayer(e.getPlayer());
    }



}
