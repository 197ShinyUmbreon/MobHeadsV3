package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.Packets;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public static void onMove(PlayerMoveEvent pme){

        //Packets.autoFish(pme.getPlayer());

        Player player = pme.getPlayer();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        if (mobHead == null)return;
        boolean grounded = player.isOnGround();
        boolean gliding = player.isGliding();
        boolean sneaking = player.isSneaking();
        boolean inWater = player.isInWater();;
        switch (mobHead.getEntityType()){
            case SHULKER -> {if (grounded || inWater) CreatureEvents.resetShulkerLevitationTime(player);}
            case BLAZE -> {if (gliding) AVFX.playBlazeGlideParticles(player.getLocation());}
            case CHICKEN -> {if (gliding) AVFX.playFeatheredGlideParticles(player.getLocation());}
            case PARROT -> {if (gliding) AVFX.playFeatheredGlideParticles(player.getLocation());}
            case GHAST -> {
                if (!gliding && !grounded && sneaking) CreatureEvents.ghastFloat(player);
                if (grounded) CreatureEvents.removeFromGhastsFloating(player);
            }
            case SLIME, MAGMA_CUBE -> {
                CreatureEvents.slimeAirborne(player);
            }
        }
    }

}
