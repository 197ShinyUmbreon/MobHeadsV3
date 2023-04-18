package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InteractWithHeadEvents implements Listener {

    private final Map<Player, Block> interactCooldownMap = new HashMap<>();

    @EventHandler
    public void onPlayerInteractWithMobHead(PlayerInteractEvent e){
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND))return;
        Block clickedBlock = e.getClickedBlock();
        Player player = e.getPlayer();
        if (clickedBlock == null)return;
        Material material = e.getClickedBlock().getType();
        if (interactCooldownMap.containsKey(player) && interactCooldownMap.get(player).equals(clickedBlock))return;
        if (!Data.headBlockMats.contains(material))return;
        MobHead mobHead;
        if (Data.vanillaHeadMats.contains(material)){
            EntityType entityType = Data.vanillaMatEntTypeMap().get(material);
            UUID uuid = Data.vanillaHeadUUIDs.get(entityType);
            mobHead = Data.mobHeadByUUID.get(uuid);
        }else{
            Skull skull = (Skull) clickedBlock.getState();
            PersistentDataContainer data = skull.getPersistentDataContainer();
            if (!data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING))return;
            String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
            if (uuidString == null)return;
            UUID uuid = UUID.fromString(uuidString);
            mobHead = Data.mobHeadByUUID.get(uuid);
        }
        interactCooldown(player, clickedBlock);
        interactEffects(mobHead, e);
    }

    private void interactCooldown(Player player, Block block){
        interactCooldownMap.put(player, block);
        new BukkitRunnable(){
            @Override
            public void run() {
                interactCooldownMap.remove(player, block);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),10);
    }


    public static void interactEffects(MobHead mobHead, PlayerInteractEvent e){
        EntityType entityType = mobHead.getEntityType();
        Block block = e.getClickedBlock();
        assert block != null;
        Location locCenter = e.getClickedBlock().getLocation().add(0.5,0.5,0.5);
        World world = locCenter.getWorld();
        assert world != null;
        AVFX.playHeadInteractSound(locCenter, mobHead);
    }

}
