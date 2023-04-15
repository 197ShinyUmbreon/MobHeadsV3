package io.github.shinyumbreon197.mobheadsv3.head.vanilla;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EnderDragonHead {

    private static MobHead mobHead;
    private static final EntityType entityType = EntityType.ENDER_DRAGON;
    private static final ItemStack lootItem = new ItemStack(Material.END_CRYSTAL, 4);
    private static final Sound interactSound = Sound.ENTITY_ENDER_DRAGON_AMBIENT;
    private static final UUID headUUID = UUID.fromString("766caacc-5fe3-11ed-9b6a-0242ac120002");
    private static final ItemStack headItem = new ItemStack(Material.DRAGON_HEAD);

    public static void initialize(){
        mobHead = new MobHead(entityType, null, null, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

    public static void onTest(PlayerInteractAtEntityEvent e) {
        e.getPlayer().getInventory().addItem(headItem);
    }

}
