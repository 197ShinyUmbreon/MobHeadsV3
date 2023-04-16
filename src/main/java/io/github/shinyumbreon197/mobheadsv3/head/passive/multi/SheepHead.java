package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.*;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class SheepHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.SHEEP;
    private static Map<DyeColor, ItemStack> lootItemsMap(){
        Map<DyeColor, ItemStack> map = new HashMap<>();
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.ORANGE, new ItemStack(Material.ORANGE_WOOL, 16));
        map.put(DyeColor.MAGENTA, new ItemStack(Material.MAGENTA_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
        map.put(DyeColor.WHITE, new ItemStack(Material.WHITE_WOOL, 16));
    }

    private static final Sound interactSound = Sound.ENTITY_CAT_STRAY_AMBIENT;
    private static final Cat.Type v0 = Cat.Type.TABBY;
    private static final Cat.Type v1 = Cat.Type.ALL_BLACK;
    private static final Cat.Type v2 = Cat.Type.RED;
    private static final Cat.Type v3 = Cat.Type.SIAMESE;
    private static final Cat.Type v4 = Cat.Type.BRITISH_SHORTHAIR;
    private static final Cat.Type v5 = Cat.Type.CALICO;
    private static final Cat.Type v6 = Cat.Type.PERSIAN;
    private static final Cat.Type v7 = Cat.Type.RAGDOLL;
    private static final Cat.Type v8 = Cat.Type.WHITE;
    private static final Cat.Type v9 = Cat.Type.JELLIE;
    private static final Cat.Type v10 = Cat.Type.BLACK;
    private static Map<Cat.Type, String> headNameMap(){
        Map<Cat.Type, String> map = new HashMap<>();
        map.put(v0," Cat Head");
        map.put(v1," Cat Head");
        map.put(v2," Cat Head");
        map.put(v3," Cat Head");
        map.put(v4," Cat Head");
        map.put(v5," Cat Head");
        map.put(v6," Cat Head");
        map.put(v7," Cat Head");
        map.put(v8," Cat Head");
        map.put(v9," Cat Head");
        map.put(v10," Cat Head");
        return map;
    }

    private static Map<Cat.Type , UUID> headUUIDMap(){
        Map<Cat.Type, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString(""));
        map.put(v1, UUID.fromString(""));
        map.put(v2, UUID.fromString(""));
        map.put(v3, UUID.fromString(""));
        map.put(v4, UUID.fromString(""));
        map.put(v5, UUID.fromString(""));
        map.put(v6, UUID.fromString(""));
        map.put(v7, UUID.fromString(""));
        map.put(v8, UUID.fromString(""));
        map.put(v9, UUID.fromString(""));
        map.put(v10, UUID.fromString(""));
        return map;
    }

    public static void initialize(){
        Map<Cat.Type , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL(""));
            textureURLMap.put(v1, new URL(""));
            textureURLMap.put(v2, new URL(""));
            textureURLMap.put(v3, new URL(""));
            textureURLMap.put(v4, new URL(""));
            textureURLMap.put(v5, new URL(""));
            textureURLMap.put(v6, new URL(""));
            textureURLMap.put(v7, new URL(""));
            textureURLMap.put(v8, new URL(""));
            textureURLMap.put(v9, new URL(""));
            textureURLMap.put(v10, new URL(""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<Cat.Type, ItemStack> headItemMap = new HashMap<>();
        headItemMap.put(v0, HeadUtil.customHead(headNameMap().get(v0), headUUIDMap().get(v0), textureURLMap.get(v0)));
        headItemMap.put(v1, HeadUtil.customHead(headNameMap().get(v1), headUUIDMap().get(v1), textureURLMap.get(v1)));
        headItemMap.put(v2, HeadUtil.customHead(headNameMap().get(v2), headUUIDMap().get(v2), textureURLMap.get(v2)));
        headItemMap.put(v3, HeadUtil.customHead(headNameMap().get(v3), headUUIDMap().get(v3), textureURLMap.get(v3)));
        headItemMap.put(v4, HeadUtil.customHead(headNameMap().get(v4), headUUIDMap().get(v4), textureURLMap.get(v4)));
        headItemMap.put(v5, HeadUtil.customHead(headNameMap().get(v5), headUUIDMap().get(v5), textureURLMap.get(v5)));
        headItemMap.put(v6, HeadUtil.customHead(headNameMap().get(v6), headUUIDMap().get(v6), textureURLMap.get(v6)));
        headItemMap.put(v7, HeadUtil.customHead(headNameMap().get(v7), headUUIDMap().get(v7), textureURLMap.get(v7)));
        headItemMap.put(v8, HeadUtil.customHead(headNameMap().get(v8), headUUIDMap().get(v8), textureURLMap.get(v8)));
        headItemMap.put(v9, HeadUtil.customHead(headNameMap().get(v9), headUUIDMap().get(v9), textureURLMap.get(v9)));
        headItemMap.put(v10, HeadUtil.customHead(headNameMap().get(v10), headUUIDMap().get(v10), textureURLMap.get(v10)));

        List<Cat.Type> types = List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
        for (Cat.Type type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }


}
