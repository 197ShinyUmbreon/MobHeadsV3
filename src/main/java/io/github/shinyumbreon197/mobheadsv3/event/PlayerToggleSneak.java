package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerToggleSneak implements Listener {


    @EventHandler
    public static void onPlayerToggleSneak(PlayerToggleSneakEvent ptse){
        Player player = ptse.getPlayer();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        boolean sneaking = ptse.isSneaking();
        boolean inWater = player.isInWater();
        if (debug) System.out.println("onPlayerToggleSneak() sneaking: " + sneaking); //debug
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        switch (headType){
            case SHULKER -> {if (sneaking) CreatureEvents.startShulkerLevitation(player);}
            case CHICKEN -> {if (sneaking) CreatureEvents.startChickenIncubation(player);}
            case OCELOT, CAT -> {CreatureEvents.catJumpFive(player, sneaking);}
            case GUARDIAN, ELDER_GUARDIAN -> {if (sneaking) CreatureEvents.guardianToggleLockOn(player);}
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, DONKEY, MULE -> {
                CreatureEvents.equineSneakRemoveStepHeight(player, headType, sneaking);
                CreatureEvents.equineChargeJump(player, sneaking, inWater);
            }
        }
    }

}
