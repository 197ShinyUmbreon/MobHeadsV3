package io.github.shinyumbreon197.mobheadsv3.head;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ZombieHead {

    private static final EntityType entityType = EntityType.ZOMBIE;
    private static final ItemStack lootItem = new ItemStack(Material.ROTTEN_FLESH, 16);
    private static final Sound hurtSound = Sound.ENTITY_ZOMBIE_HURT;
    private static final Sound deathSound = Sound.ENTITY_ZOMBIE_DEATH;
    private static final Sound interactSound = Sound.ENTITY_ZOMBIE_AMBIENT;
    private static final ItemStack headItem = new ItemStack(Material.ZOMBIE_HEAD);

    private void initialize(){

    }

    public static void onTest(PlayerInteractAtEntityEvent e) {
        e.getPlayer().getInventory().addItem(headItem);
    }

    private static void playInteractEffect(Location origin){
        World world = origin.getWorld();
        assert world != null;
        world.playSound(origin, interactSound, 0.5F, 1.0F);
    }

    public static void onHeadInteractEvent(PlayerInteractEvent e){
        assert e.getClickedBlock() != null;
        playInteractEffect(e.getClickedBlock().getLocation().add(0.5,0.5,0.5));
    }

}
