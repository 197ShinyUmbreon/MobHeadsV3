package io.github.shinyumbreon197.mobheadsv3.head;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadItem;
import io.github.shinyumbreon197.mobheadsv3.tool.Recipes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class FoxHead {

    private static final EntityType entityType = EntityType.FOX;
    private static final ItemStack lootItem = new ItemStack(Material.SWEET_BERRIES, 16);
    private static final Sound hurtSound = Sound.ENTITY_FOX_HURT;
    private static final Sound deathSound = Sound.ENTITY_FOX_DEATH;
    private static Map<Fox.Type, URL> textureURLMap;
    private static Map<Fox.Type, ItemStack> headItemMap;
    private static final Map<Fox.Type, String> headNameMap = Map.of(
            Fox.Type.RED,"Red Fox Head",
            Fox.Type.SNOW, "Arctic Fox Head"
    );
    private static final Map<Fox.Type, UUID> headUUIDMap = Map.of(
            Fox.Type.RED, UUID.fromString("766c27be-5fe3-11ed-9b6a-0242ac120002"),
            Fox.Type.SNOW, UUID.fromString("766c299e-5fe3-11ed-9b6a-0242ac120002")
    );
    private static Sound randomInteractSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 49){
            return Sound.ENTITY_FOX_AMBIENT;
        }else if (i < 79){
            return Sound.ENTITY_FOX_SNIFF;
        }else return Sound.ENTITY_FOX_SCREECH;
    }

    public static void initialize() throws MalformedURLException {
        textureURLMap = Map.of(
                Fox.Type.RED, new URL("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"),
                Fox.Type.SNOW, new URL("http://textures.minecraft.net/texture/ddcd0db8cbe8f1e0ab1ec0a9385fb9288da84d3202c1c397da76ee1035e608b0")
        );
        headItemMap = Map.of(
                Fox.Type.RED, HeadItem.customHead(headNameMap.get(Fox.Type.RED), headUUIDMap.get(Fox.Type.RED), textureURLMap.get(Fox.Type.RED)),
                Fox.Type.SNOW, HeadItem.customHead(headNameMap.get(Fox.Type.SNOW), headUUIDMap.get(Fox.Type.SNOW), textureURLMap.get(Fox.Type.SNOW))
        );
        for (Fox.Type type:Fox.Type.values()){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            HeadData.entityTypeLookupMap.put(uuid, entityType);
            HeadData.headItemLookupMap.put(uuid, head);
            HeadData.uuidFromNameLookupMap.put(name, uuid);
            HeadData.variantLookupMap.put(uuid, type.toString());
            Recipes.registerHeadRecipe(head, lootItem);
        }
        HeadData.uuidFromEntityTypeMap.put(entityType, new ArrayList<>(headUUIDMap.values()));
        HeadData.entityTypes.add(entityType);
    }

    public static void onTest(PlayerInteractAtEntityEvent e) {
        Fox fox = (Fox) e.getRightClicked();
        e.getPlayer().getInventory().addItem(headItemMap.get(fox.getFoxType()));
    }

    private static void playInteractEffect(Location origin){
        World world = origin.getWorld();
        assert world != null;
        world.playSound(origin, randomInteractSound(), 0.6F, 1.0F);
    }

    public static void onHeadInteractEvent(PlayerInteractEvent e){
        assert e.getClickedBlock() != null;
        playInteractEffect(e.getClickedBlock().getLocation().add(0.5,0.5,0.5));
    }
}
