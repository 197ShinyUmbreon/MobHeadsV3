package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerChangeEvent implements Listener {

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent e){
        if (!(e.getEntity() instanceof Player))return;
        Player player = (Player) e.getEntity();
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(player);
        e.setCancelled(
                mobHead != null &&
                HeadData.zombifiedTypes.contains(mobHead.getEntityType()) &&
                e.getItem() == null && player.getFoodLevel() <= 19
        );
    }

}
