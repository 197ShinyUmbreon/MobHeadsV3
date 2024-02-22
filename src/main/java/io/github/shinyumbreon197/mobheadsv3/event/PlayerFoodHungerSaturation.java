package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerFoodHungerSaturation implements Listener {

    @EventHandler
    public static void onFoodLevelChange(FoodLevelChangeEvent flce){
        if (debug) System.out.println(
                "FoodLevelChangeEvent " + flce.getEntity().getName() +" newHunger: " + flce.getFoodLevel()
                + " saturation: " + flce.getEntity().getSaturation()
        ); //debug
        if (!(flce.getEntity() instanceof Player))return;
        Player player = (Player) flce.getEntity();
        if (!MobHead.isWearingHead(player))return;
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        if (mobHead == null)return;
        headedPlayerHungerChange(mobHead,flce);
    }

    private static void headedPlayerHungerChange(MobHead mobHead, FoodLevelChangeEvent flce){
        CreatureEvents.zombifiedHunger(mobHead.getEntityType(),flce);
    }

}
