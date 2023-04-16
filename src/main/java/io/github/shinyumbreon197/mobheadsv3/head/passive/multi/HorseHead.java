package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HorseHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.HORSE;
    private static final ItemStack lootItem = new ItemStack(Material.HAY_BLOCK, 1);

    private static final Sound interactSound = Sound.ENTITY_HORSE_AMBIENT;
    private static final Horse.Color v0 = Horse.Color.WHITE;
    private static final Horse.Color v1 = Horse.Color.CREAMY;
    private static final Horse.Color v2 = Horse.Color.CHESTNUT;
    private static final Horse.Color v3 = Horse.Color.BROWN;
    private static final Horse.Color v4 = Horse.Color.BLACK;
    private static final Horse.Color v5 = Horse.Color.GRAY;
    private static final Horse.Color v6 = Horse.Color.DARK_BROWN;

    private static Map<Horse.Color, String> headNameMap(){
        Map<Horse.Color, String> map = new HashMap<>();
        map.put(v0,"White Horse Head");
        map.put(v1,"Creamy Horse Head");
        map.put(v2,"Chestnut Horse Head");
        map.put(v3,"Brown Horse Head");
        map.put(v4,"Black Horse Head");
        map.put(v5,"Gray Horse Head");
        map.put(v6,"Dark-Brown Horse Head");
        return map;
    }

    private static Map<Horse.Color , UUID> headUUIDMap(){
        Map<Horse.Color, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("194180d4-5fac-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("194181f6-5fac-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("194184da-5fac-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("19418782-5fac-11ed-9b6a-0242ac120002"));
        map.put(v4, UUID.fromString("194188fe-5fac-11ed-9b6a-0242ac120002"));
        map.put(v5, UUID.fromString("19418aac-5fac-11ed-9b6a-0242ac120002"));
        map.put(v6, UUID.fromString("19418bec-5fac-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Horse.Color , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("http://textures.minecraft.net/texture/9f4bdd59d4f8f1d5782e0fee4bd64aed100627f188a91489ba37eeadededd827"));
            textureURLMap.put(v1, new URL("http://textures.minecraft.net/texture/a6dae0ade0e0dafb6dbc7786ce4241242b6b6df527a0f7af0a42184c93fd646b"));
            textureURLMap.put(v2, new URL("http://textures.minecraft.net/texture/9717d71025f7a62c90a333c51663ffeb385a9a0d92af68083c5b045c0524b23f"));
            textureURLMap.put(v3, new URL("http://textures.minecraft.net/texture/25e397def0af06feef22421860088186639732aa0a5eb5756e0aa6b03fd092c8"));
            textureURLMap.put(v4, new URL("http://textures.minecraft.net/texture/3efb0b9857d7c8d295f6df97b605f40b9d07ebe128a6783d1fa3e1bc6e44117"));
            textureURLMap.put(v5, new URL("http://textures.minecraft.net/texture/8f0d955889b0378d4933c956398567e770103ae9eff0f702d0d53d52e7f6a83b"));
            textureURLMap.put(v6, new URL("http://textures.minecraft.net/texture/156b7bc1a4836eb428ea8925eceb5e01dfbd30c7deff6c9482689823203cfd2f"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<Horse.Color, ItemStack> headItemMap = new HashMap<>();
        headItemMap.put(v0, HeadUtil.customHead(headNameMap().get(v0), headUUIDMap().get(v0), textureURLMap.get(v0)));
        headItemMap.put(v1, HeadUtil.customHead(headNameMap().get(v1), headUUIDMap().get(v1), textureURLMap.get(v1)));
        headItemMap.put(v2, HeadUtil.customHead(headNameMap().get(v2), headUUIDMap().get(v2), textureURLMap.get(v2)));
        headItemMap.put(v3, HeadUtil.customHead(headNameMap().get(v3), headUUIDMap().get(v3), textureURLMap.get(v3)));
        headItemMap.put(v4, HeadUtil.customHead(headNameMap().get(v4), headUUIDMap().get(v4), textureURLMap.get(v4)));
        headItemMap.put(v5, HeadUtil.customHead(headNameMap().get(v5), headUUIDMap().get(v5), textureURLMap.get(v5)));
        headItemMap.put(v6, HeadUtil.customHead(headNameMap().get(v6), headUUIDMap().get(v6), textureURLMap.get(v6)));

        List<Horse.Color> types = List.of(v0, v1, v2, v3, v4, v5, v6);
        for (Horse.Color type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }


}
