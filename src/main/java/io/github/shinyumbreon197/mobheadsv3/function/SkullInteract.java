package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class SkullInteract {

    public static void skullInteract(PlayerInteractEvent pie){
        assert pie.getClickedBlock() != null;
        BlockState blockState = pie.getClickedBlock().getState();
        //if (debug) System.out.println("blockState: " + blockState); //debug
        if (!(blockState instanceof Skull))return;
        Skull skull = (Skull) blockState;
        PersistentDataContainer data = skull.getPersistentDataContainer();
        //if (debug) System.out.println("data: " + data); //debug
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return;
        String uuidString = data.get(key, PersistentDataType.STRING);
        if (debug) System.out.println("uuidString: " +uuidString); //debug
        if (uuidString == null)return;
        MobHead mobHead = MobHead.getMobHeadFromUUID(UUID.fromString(uuidString));
        //if (debug) System.out.println("mobHead: " + mobHead); //debug
        if (mobHead == null)return;
        if (debug) System.out.println("mobHead: displayName " + mobHead.getHeadName()); //debug
        WorldEvents.mobHeadSkullInteract(mobHead,pie);
        pie.getPlayer().swingMainHand();
    }
}
