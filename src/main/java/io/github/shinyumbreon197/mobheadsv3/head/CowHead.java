package io.github.shinyumbreon197.mobheadsv3.head;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class CowHead {

    private static final EntityType entityType = EntityType.COW;
    private static final ItemStack lootItem = new ItemStack(Material.LEATHER, 12);
    private static final Sound hurtSound = Sound.ENTITY_COW_HURT;
    private static final Sound deathSound = Sound.ENTITY_COW_DEATH;
    private static final Sound interactSound = Sound.ENTITY_COW_AMBIENT;
    private static final String headName = "Cow Head";
    private static final UUID headUUID = UUID.fromString("1940fbd2-5fac-11ed-9b6a-0242ac120002");
    private static URL textureURL;
    private static ItemStack headItem;

    public static void initialize() throws MalformedURLException {
        textureURL = new URL("http://textures.minecraft.net/texture/7dfa0ac37baba2aa290e4faee419a613cd6117fa568e709d90374753c032dcb0");
        headItem = HeadItem.customHead(headName, headUUID, textureURL);
        HeadData.entityTypeLookupMap.put(headUUID, entityType);
        HeadData.headItemLookupMap.put(headUUID, headItem);
        HeadData.uuidFromNameLookupMap.put(headName, headUUID);
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
