package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener {

    @EventHandler
    public static void onTeleport(PlayerTeleportEvent pte){
        MobHead mobHead = MobHead.getMobHeadWornByEntity(pte.getPlayer());
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();

        switch (headType){
            case SLIME, MAGMA_CUBE -> CreatureEvents.slimeReset(pte.getPlayer());
        }

    }

}
