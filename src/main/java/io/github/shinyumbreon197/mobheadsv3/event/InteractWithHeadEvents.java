package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.CowHead;
import io.github.shinyumbreon197.mobheadsv3.head.EnderDragonHead;
import io.github.shinyumbreon197.mobheadsv3.head.FoxHead;
import io.github.shinyumbreon197.mobheadsv3.head.ZombieHead;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractWithHeadEvents implements Listener {

    private final Map<Player, Block> interactCooldownMap = new HashMap<>();

    @EventHandler
    public void onPlayerInteractWithMobHead(PlayerInteractEvent e){
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND))return;
        Block clickedBlock = e.getClickedBlock();
        Player player = e.getPlayer();
        if (clickedBlock == null)return;
        if (!HeadData.headBlockMats.contains(clickedBlock.getType()))return;
        EntityType entityType;
        if (HeadData.playerHeadMats.contains(clickedBlock.getType())){
            Skull skull = (Skull) clickedBlock.getState();
            PersistentDataContainer data = skull.getPersistentDataContainer();
            if (!data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING))return;
            String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
            if (uuidString == null)return;
            UUID uuid = UUID.fromString(uuidString);
            if (!HeadData.entityTypeLookupMap.containsKey(uuid))return;
            entityType = HeadData.entityTypeLookupMap.get(uuid);
        }else{
            entityType = HeadData.vanillaMatEntTypeMap().get(clickedBlock.getType());
        }
        if (entityType == null)return;
        if (interactCooldownMap.containsKey(player) && interactCooldownMap.get(player).equals(clickedBlock))return;
        interactCooldown(player, clickedBlock);
        playInteractEffects(entityType, e);
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

    private void playInteractEffects(EntityType entityType, PlayerInteractEvent e){
        switch (entityType){
            default -> {}
            case COW -> CowHead.onHeadInteractEvent(e);
            case FOX -> FoxHead.onHeadInteractEvent(e);
            case ENDER_DRAGON -> EnderDragonHead.onHeadInteractEvent(e);
            case ZOMBIE -> ZombieHead.onHeadInteractEvent(e);
        }
    }

}
