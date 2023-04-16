package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.ParrotHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        if (!HeadData.headBlockMats.contains(material))return;
        MobHead mobHead;
        if (HeadData.vanillaHeadMats.contains(material)){
            EntityType entityType = HeadData.vanillaMatEntTypeMap().get(material);
            UUID uuid = HeadData.vanillaHeadUUIDs.get(entityType);
            mobHead = HeadData.mobHeadByUUID.get(uuid);
        }else{
            Skull skull = (Skull) clickedBlock.getState();
            PersistentDataContainer data = skull.getPersistentDataContainer();
            if (!data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING))return;
            String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
            if (uuidString == null)return;
            UUID uuid = UUID.fromString(uuidString);
            mobHead = HeadData.mobHeadByUUID.get(uuid);
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
        Block block = e.getClickedBlock();
        assert block != null;
        Location locCenter = e.getClickedBlock().getLocation().add(0.5,0.5,0.5);
        World world = locCenter.getWorld();
        assert world != null;
        Sound interactSound = mobHead.getInteractSound();
        float volume = 0.6F;
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case PLAYER -> {interactSound = playerSound(); volume = 0.4f;}
            case FOX -> {interactSound = foxSound(); volume = 0.8f;}
            case ENDER_DRAGON -> {volume = 0.5f;}
            case WOLF -> {interactSound = wolfSound(); volume = 0.4f;}
            case TURTLE -> {volume = 1.3f;}
            case BAT -> {volume = 0.05f;}
            case OCELOT -> {volume = 1.5f;}
            case PANDA -> {interactSound = pandaSound();}
            case PARROT -> {interactSound = parrotSound();}
            case RABBIT -> {volume = 1.6f;}
            case FROG -> {volume = 1.0f;}

        }
        if (interactSound != null) world.playSound(locCenter, interactSound, volume, 1.0F);
    }

    private static Sound parrotSound(){
        Random random = new Random();
        return ParrotHead.getParrotSounds().get(random.nextInt(ParrotHead.getParrotSounds().size()));
    }

    private static Sound foxSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 49){
            return Sound.ENTITY_FOX_AMBIENT;
        }else if (i < 79){
            return Sound.ENTITY_FOX_SNIFF;
        }else return Sound.ENTITY_FOX_SCREECH;
    }

    private static Sound wolfSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 69){
            return Sound.ENTITY_WOLF_AMBIENT;
        }else if (i < 89){
            return Sound.ENTITY_WOLF_GROWL;
        }else return Sound.ENTITY_WOLF_HOWL;
    }

    private static Sound pandaSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 59){
            return Sound.ENTITY_PANDA_AMBIENT;
        }else if (i < 89){
            return Sound.ENTITY_PANDA_EAT;
        }else return Sound.ENTITY_PANDA_SNEEZE;
    }

    private static Sound playerSound(){
        List<Sound> sounds = Arrays.asList(
                Sound.ENTITY_PLAYER_BURP, Sound.ENTITY_PLAYER_BIG_FALL, Sound.ENTITY_PLAYER_HURT,
                Sound.ENTITY_PLAYER_HURT_DROWN, Sound.ENTITY_PLAYER_HURT_FREEZE, Sound.ENTITY_PLAYER_HURT_ON_FIRE,
                Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, Sound.ENTITY_PLAYER_LEVELUP, Sound.ENTITY_PLAYER_SMALL_FALL,
                Sound.ITEM_ARMOR_EQUIP_GENERIC, Sound.ITEM_BUCKET_FILL, Sound.ITEM_BUCKET_FILL_LAVA,
                Sound.BLOCK_ANVIL_USE, Sound.BLOCK_BREWING_STAND_BREW, Sound.BLOCK_CHEST_OPEN, Sound.BLOCK_CHEST_CLOSE,
                Sound.BLOCK_ENCHANTMENT_TABLE_USE, Sound.BLOCK_ENDER_CHEST_OPEN, Sound.BLOCK_ENDER_CHEST_CLOSE,
                Sound.BLOCK_GRINDSTONE_USE, Sound.BLOCK_SMITHING_TABLE_USE, Sound.ENTITY_ITEM_BREAK,
                Sound.BLOCK_LADDER_STEP, Sound.ENTITY_EXPERIENCE_ORB_PICKUP
        );
        Random random = new Random();
        return sounds.get(random.nextInt(sounds.size()));
    }

}
