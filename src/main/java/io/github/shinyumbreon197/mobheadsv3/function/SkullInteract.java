package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class SkullInteract {

    public static void skullInteract(PlayerInteractEvent pie){
        assert pie.getClickedBlock() != null;
        BlockState blockState = pie.getClickedBlock().getState();
        //if (debug) System.out.println("blockState: " + blockState); //debug
        if (!(blockState instanceof Skull))return;
        Skull skull = (Skull) blockState;
        if (!isSkullValid(skull)){
            skullRepairOnInteract(skull);
        }
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

    private static boolean isSkullValid(Skull skull){
        return skull.getOwnerProfile() != null;
    }

    private static void skullRepairOnInteract(Skull skull){
        PersistentDataContainer data = skull.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return;
        String stringUUID = data.get(key, PersistentDataType.STRING);
        if (stringUUID == null)return;
        UUID uuid = UUID.fromString(stringUUID);
        MobHead mobHead = MobHead.getMobHeadFromUUID(uuid);
        if (mobHead == null)return;
        ItemStack headItem = mobHead.getHeadItemStack();
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        assert skullMeta != null;
        PlayerProfile pp = skullMeta.getOwnerProfile();
        skull.setOwnerProfile(pp);
        skull.update(true);
    }
}
