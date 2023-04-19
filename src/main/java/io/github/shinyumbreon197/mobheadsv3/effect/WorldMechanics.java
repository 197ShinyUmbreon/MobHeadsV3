package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

public class WorldMechanics {

    public static void furnaceStartCooking(FurnaceStartSmeltEvent e, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case BLAZE -> {e.setTotalCookTime(e.getTotalCookTime()/2);}
        }
    }

    public static void furnaceBurnFuel(FurnaceBurnEvent e, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case BLAZE -> {
                e.setBurnTime(e.getBurnTime()/2);
                Location headLoc = e.getBlock().getLocation().add(0.5, 1.1, 0.5);
                AVFX.playBlazeHeadFlameEffect(headLoc);
            }
        }
    }

}
