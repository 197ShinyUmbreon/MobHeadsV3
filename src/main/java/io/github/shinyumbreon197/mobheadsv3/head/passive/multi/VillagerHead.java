package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class VillagerHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.VILLAGER;
    private static final ItemStack lootItem = new ItemStack(Material.BREAD, 1);

    private static final Sound interactSound = Sound.ENTITY_VILLAGER_NO;
    private static final Villager.Type v0 = Villager.Type.DESERT;
    private static final Villager.Type v1 = Villager.Type.JUNGLE;
    private static final Villager.Type v2 = Villager.Type.PLAINS;
    private static final Villager.Type v3 = Villager.Type.SAVANNA;
    private static final Villager.Type v4 = Villager.Type.SNOW;
    private static final Villager.Type v5 = Villager.Type.SWAMP;
    private static final Villager.Type v6 = Villager.Type.TAIGA;

    private static Map<Villager.Type, String> headNameMap(){
        Map<Villager.Type, String> map = new HashMap<>();
        map.put(v0,"Desert Villager Head");
        map.put(v1,"Jungle Villager Head");
        map.put(v2,"Plains Villager Head");
        map.put(v3,"Savanna Villager Head");
        map.put(v4,"Snow Villager Head");
        map.put(v5,"Swamp Villager Head");
        map.put(v6,"Taiga Villager Head");
        return map;
    }

    private static Map<Villager.Type , UUID> headUUIDMap(){
        Map<Villager.Type, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("766c74e4-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("766c77c8-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("766c799e-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("766c7c82-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v4, UUID.fromString("766c7e62-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v5, UUID.fromString("766c80a6-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v6, UUID.fromString("766c884e-5fe3-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Villager.Type , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("https://textures.minecraft.net/texture/10bf6df37dac6ca6089d2ba04135f223d4d850df9f09c7ec4eaf8c50764cbc50"));
            textureURLMap.put(v1, new URL("https://textures.minecraft.net/texture/44b062a9f8399dccb6251a74e618647342a3c0240ca56f34614d52f60a3fecec"));
            textureURLMap.put(v2, new URL("https://textures.minecraft.net/texture/d14bff1a38c9154e5ec84ce5cf00c58768e068eb42b2d89a6bbd29787590106b"));
            textureURLMap.put(v3, new URL("https://textures.minecraft.net/texture/14f05fd4215ea2a43244e832c723f65f05c2562abfe0bdf336f50293e683789d"));
            textureURLMap.put(v4, new URL("https://textures.minecraft.net/texture/20c641e3d3764ed1c1f1907c4334e2b1303e2152b13d1eb0c605763f97fb258a"));
            textureURLMap.put(v5, new URL("https://textures.minecraft.net/texture/9ad7a9e8fe2bdfea03bb1f9fabe45fb10cf69a72e3760e5fd9a70f3384c536ad"));
            textureURLMap.put(v6, new URL("https://textures.minecraft.net/texture/61e897719b54b844fa059f04817e13db8abd97e6bdb0624093032b4512f7a1c6"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<Villager.Type, ItemStack> headItemMap = new HashMap<>();
        headItemMap.put(v0, HeadUtil.customHead(headNameMap().get(v0), headUUIDMap().get(v0), textureURLMap.get(v0)));
        headItemMap.put(v1, HeadUtil.customHead(headNameMap().get(v1), headUUIDMap().get(v1), textureURLMap.get(v1)));
        headItemMap.put(v2, HeadUtil.customHead(headNameMap().get(v2), headUUIDMap().get(v2), textureURLMap.get(v2)));
        headItemMap.put(v3, HeadUtil.customHead(headNameMap().get(v3), headUUIDMap().get(v3), textureURLMap.get(v3)));
        headItemMap.put(v4, HeadUtil.customHead(headNameMap().get(v4), headUUIDMap().get(v4), textureURLMap.get(v4)));
        headItemMap.put(v5, HeadUtil.customHead(headNameMap().get(v5), headUUIDMap().get(v5), textureURLMap.get(v5)));
        headItemMap.put(v6, HeadUtil.customHead(headNameMap().get(v6), headUUIDMap().get(v6), textureURLMap.get(v6)));

        List<Villager.Type> types = List.of(v0, v1, v2, v3, v4, v5, v6);
        for (Villager.Type type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }


}
