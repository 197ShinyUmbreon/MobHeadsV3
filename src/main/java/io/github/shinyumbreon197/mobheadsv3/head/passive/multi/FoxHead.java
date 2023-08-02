package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FoxHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.FOX;
    private static final ItemStack lootItem = new ItemStack(Material.SWEET_BERRIES, 16);

    private static final Sound interactSound = Sound.ENTITY_FOX_AMBIENT;
    private static final Map<Fox.Type, String> headNameMap = Map.of(
            Fox.Type.RED,"Red Fox Head",
            Fox.Type.SNOW, "Arctic Fox Head"
    );
    private static final Map<Fox.Type, UUID> headUUIDMap = Map.of(
            Fox.Type.RED, UUID.fromString("766c27be-5fe3-11ed-9b6a-0242ac120002"),
            Fox.Type.SNOW, UUID.fromString("766c299e-5fe3-11ed-9b6a-0242ac120002")
    );

    public static void initialize(){
        Map<Fox.Type, URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    Fox.Type.RED, new URL("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"),
                    Fox.Type.SNOW, new URL("http://textures.minecraft.net/texture/ddcd0db8cbe8f1e0ab1ec0a9385fb9288da84d3202c1c397da76ee1035e608b0")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<Fox.Type, ItemStack> headItemMap = Map.of(
                Fox.Type.RED, HeadUtil.customHead(headNameMap.get(Fox.Type.RED), headUUIDMap.get(Fox.Type.RED), textureURLMap.get(Fox.Type.RED)),
                Fox.Type.SNOW, HeadUtil.customHead(headNameMap.get(Fox.Type.SNOW), headUUIDMap.get(Fox.Type.SNOW), textureURLMap.get(Fox.Type.SNOW))
        );
        for (Fox.Type type:Fox.Type.values()){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        Data.addMobHeads(mobHeads);
    }

}
