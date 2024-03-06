package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnload implements Listener {

    @EventHandler
    public static void onChunkUnload(ChunkUnloadEvent cue){
        Entity[] unloadingEntities = cue.getChunk().getEntities();
        for (Entity entity:unloadingEntities){
            Summon summon = Summon.getSummonFromEntity(entity);
            if (summon != null) Summon.removeSummon(summon);
        }
    }

}
