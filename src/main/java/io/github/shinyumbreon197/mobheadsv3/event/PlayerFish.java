package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Set;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerFish implements Listener {

    private static final Set<EntityType> fishTypes = Set.of(EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH);
    @EventHandler
    public static void onPlayerFish(PlayerFishEvent pfe){
        if (debug) System.out.println("PlayerFishEvent"); //debug
        Player player = pfe.getPlayer();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        if (!fishTypes.contains(headType))return;
        CreatureEvents.fishAutoFish(player, pfe);
    }

}
