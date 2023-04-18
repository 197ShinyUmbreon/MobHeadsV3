package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PlaceAndBreakHeadEvents implements Listener {

    @EventHandler
    public void onPlaceMobHead(BlockPlaceEvent e){
        ItemStack headItem = e.getItemInHand();
        if (!headItem.getType().equals(Material.PLAYER_HEAD))return;
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        if (skullMeta == null)return;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        if (!data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING))return;
        String uuidString = data.get(MobHeadsV3.getPluginNSK(),PersistentDataType.STRING);
        if (uuidString == null)return;
        UUID uuid = UUID.fromString(uuidString);
        if (!Data.mobHeadByUUID.containsKey(uuid))return;
        updateSkullBlockData(uuid, e.getBlock().getLocation());
    }

    private void updateSkullBlockData(UUID uuid, Location blockLocation){
        new BukkitRunnable(){
            @Override
            public void run() {
                Block headBlock = blockLocation.getBlock();
                if (!headBlock.getType().equals(Material.PLAYER_HEAD) && !headBlock.getType().equals(Material.PLAYER_WALL_HEAD)){
                    cancel();
                }else{
                    Skull head = (Skull) headBlock.getState();
                    PersistentDataContainer data = head.getPersistentDataContainer();
                    data.set(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING, uuid.toString());
                    head.update();
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 1);

    }

    @EventHandler
    public void onBlockDropHeadEvent(ItemSpawnEvent e){
        ItemStack itemStack = e.getEntity().getItemStack();
        if (!itemStack.getType().equals(Material.PLAYER_HEAD))return;
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (skullMeta == null)return;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        if (data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING))return;
        PlayerProfile pp = skullMeta.getOwnerProfile();
        if (pp == null)return;
        UUID uuid = pp.getUniqueId();
        if (Data.mobHeadByUUID.containsKey(uuid)){
            MobHead mobHead = Data.mobHeadByUUID.get(uuid);
            World world = e.getEntity().getWorld();
            Location spawnLoc = e.getLocation();
            Vector velocity = e.getEntity().getVelocity();
            int count = itemStack.getAmount();
            e.setCancelled(true);
            ItemStack head = mobHead.getHeadItem();
            head.setAmount(count);
            Item headItem = world.dropItem(spawnLoc, head);
            headItem.setVelocity(velocity);
        }
    }

}
