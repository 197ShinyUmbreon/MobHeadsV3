package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class SkullBreakPlace {

    public static void placeMobHeadSkull(BlockPlaceEvent bpe){
        ItemStack headItem = bpe.getItemInHand();
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        if (skullMeta == null)return;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return;
        String uuidString = data.get(key, PersistentDataType.STRING);
        if (uuidString == null)return;
        UUID uuid = UUID.fromString(uuidString);
        updateMobHeadSkull(bpe.getBlock(), uuid);
    }

    private static void updateMobHeadSkull(Block skullBlock, UUID uuid){
        new BukkitRunnable(){
            @Override
            public void run() {
                BlockState state = skullBlock.getState();
                if (!(state instanceof Skull))return;
                Skull skull = (Skull) skullBlock.getState();
                PersistentDataContainer data = skull.getPersistentDataContainer();
                data.set(Key.headUUID, PersistentDataType.STRING, uuid.toString());
                skull.update();
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),1);
    }

    public static void getMobHeadSkullFromBrokenSkull(ItemSpawnEvent ise){
        ItemStack skullItem = ise.getEntity().getItemStack();
        MobHead mobHead = MobHead.getMobHeadFromBrokenSkullItem(skullItem);
        if (mobHead == null)return;
        Vector velocity = ise.getEntity().getVelocity();
        Location location = ise.getLocation();
        World world = ise.getEntity().getWorld();
        ise.getEntity().remove();
        Item newItem = world.dropItem(location, mobHead.getHeadItemStack());
        newItem.setVelocity(velocity);
    }

}
