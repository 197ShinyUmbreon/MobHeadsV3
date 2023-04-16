package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
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
        map.put(DyeColor.LIGHT_BLUE, new ItemStack(Material.LIGHT_BLUE_WOOL, 16));
        map.put(DyeColor.YELLOW, new ItemStack(Material.YELLOW_WOOL, 16));
        map.put(DyeColor.LIME, new ItemStack(Material.LIME_WOOL, 16));
        map.put(DyeColor.PINK, new ItemStack(Material.PINK_WOOL, 16));
        map.put(DyeColor.GRAY, new ItemStack(Material.GRAY_WOOL, 16));
        map.put(DyeColor.LIGHT_GRAY, new ItemStack(Material.LIGHT_GRAY_WOOL, 16));
        map.put(DyeColor.CYAN, new ItemStack(Material.CYAN_WOOL, 16));
        map.put(DyeColor.PURPLE, new ItemStack(Material.PURPLE_WOOL, 16));
        map.put(DyeColor.BLUE, new ItemStack(Material.BLUE_WOOL, 16));
        map.put(DyeColor.BROWN, new ItemStack(Material.BROWN_WOOL, 16));
        map.put(DyeColor.GREEN, new ItemStack(Material.GREEN_WOOL, 16));
        map.put(DyeColor.RED, new ItemStack(Material.RED_WOOL, 16));
        map.put(DyeColor.BLACK, new ItemStack(Material.BLACK_WOOL, 16));
        return map;
    }

    private static final Sound interactSound = Sound.ENTITY_SHEEP_AMBIENT;
    private static final DyeColor v0 = DyeColor.WHITE;
    private static final DyeColor v1 = DyeColor.ORANGE;
    private static final DyeColor v2 = DyeColor.MAGENTA;
    private static final DyeColor v3 = DyeColor.LIGHT_BLUE;
    private static final DyeColor v4 = DyeColor.YELLOW;
    private static final DyeColor v5 = DyeColor.LIME;
    private static final DyeColor v6 = DyeColor.PINK;
    private static final DyeColor v7 = DyeColor.GRAY;
    private static final DyeColor v8 = DyeColor.LIGHT_GRAY;
    private static final DyeColor v9 = DyeColor.CYAN;
    private static final DyeColor v10 = DyeColor.PURPLE;
    private static final DyeColor v11 = DyeColor.BLUE;
    private static final DyeColor v12 = DyeColor.BROWN;
    private static final DyeColor v13 = DyeColor.GREEN;
    private static final DyeColor v14 = DyeColor.RED;
    private static final DyeColor v15 = DyeColor.BLACK;
    private static Map<DyeColor, String> headNameMap(){
        Map<DyeColor, String> map = new HashMap<>();
        map.put(v0,"White Sheep Head");
        map.put(v1,"Orange Sheep Head");
        map.put(v2,"Magenta Sheep Head");
        map.put(v3,"Light-Blue Sheep Head");
        map.put(v4,"Yellow Sheep Head");
        map.put(v5,"Lime Sheep Head");
        map.put(v6,"Pink Sheep Head");
        map.put(v7,"Gray Sheep Head");
        map.put(v8,"Light-Gray Sheep Head");
        map.put(v9,"Cyan Sheep Head");
        map.put(v10,"Purple Sheep Head");
        map.put(v11,"Blue Sheep Head");
        map.put(v12,"Brown Sheep Head");
        map.put(v13,"Green Sheep Head");
        map.put(v14,"Red Sheep Head");
        map.put(v15,"Black Sheep Head");
        return map;
    }

    private static Map<DyeColor , UUID> headUUIDMap(){
        Map<DyeColor, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("766c30d8-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("766c32b8-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("766c345c-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("766c37f4-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v4, UUID.fromString("766c3c36-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v5, UUID.fromString("766c3e2a-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v6, UUID.fromString("766c442e-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v7, UUID.fromString("766c4672-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v8, UUID.fromString("766c492e-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v9, UUID.fromString("766c4b7c-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v10, UUID.fromString("766c4d66-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v11, UUID.fromString("766c51a8-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v12, UUID.fromString("766c539c-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v13, UUID.fromString("766c581a-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v14, UUID.fromString("766c5d1a-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v15, UUID.fromString("766c6080-5fe3-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<DyeColor , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70"));
            textureURLMap.put(v1, new URL("http://textures.minecraft.net/texture/f098397a270b4c3d2b1e574b8cfd3cc4ea3409066cefe31ea993633c9d576"));
            textureURLMap.put(v2, new URL("http://textures.minecraft.net/texture/1836565c7897d49a71bc18986d1ea6561321a0bbf711d41a56ce3bb2c217e7a"));
            textureURLMap.put(v3, new URL("http://textures.minecraft.net/texture/9a624f5966bedd6e67f654b59e9249b2ecf307d903339bc199923977f4c8c"));
            textureURLMap.put(v4, new URL("http://textures.minecraft.net/texture/26a4112df1e4bce2a5e28417f3aaff79cd66e885c3724554102cef8eb8"));
            textureURLMap.put(v5, new URL("http://textures.minecraft.net/texture/92a2448f58a491332434e85c45d786d874397e830a3a7894e6d92699c42b30"));
            textureURLMap.put(v6, new URL("http://textures.minecraft.net/texture/2ac74a2b9b91452e56fa1dda5db81077856e49f27c6e2de1e841e5c95a6fc5ab"));
            textureURLMap.put(v7, new URL("http://textures.minecraft.net/texture/4287eb501391f275389f166ec9febea75ec4ae951b88b38cae87df7e24f4c"));
            textureURLMap.put(v8, new URL("http://textures.minecraft.net/texture/ce1ac683993be35512e1be31d1f4f98e583edb1658a9e21192c9b23b5cccdc3"));
            textureURLMap.put(v9, new URL("http://textures.minecraft.net/texture/46f6c7e7fd514ce0acc68593229e40fcc4352b841646e4f0ebcccb0ce23d16"));
            textureURLMap.put(v10, new URL("http://textures.minecraft.net/texture/ae52867afef38bb14a26d1426c8c0f116ad34761acd92e7aae2c819a0d55b85"));
            textureURLMap.put(v11, new URL("http://textures.minecraft.net/texture/d9ec22818d1fbfc8167fbe36728b28240e34e16469a2929d03fdf511bf2ca1"));
            textureURLMap.put(v12, new URL("http://textures.minecraft.net/texture/a55ad6e5db5692d87f51511f4e09b39ff9ccb3de7b4819a7378fce8553b8"));
            textureURLMap.put(v13, new URL("http://textures.minecraft.net/texture/6de55a395a2246445b45f9a6d68872344bbea54f362d529fc5b0b857ea58326b"));
            textureURLMap.put(v14, new URL("http://textures.minecraft.net/texture/839af477eb627815f723a5662556ec9dfcbab5d494d338bd214232f23e446"));
            textureURLMap.put(v15, new URL("http://textures.minecraft.net/texture/32652083f28ed1b61f9b965df1abf010f234681c21435951c67d88364749822"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<DyeColor, ItemStack> headItemMap = new HashMap<>();
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
        headItemMap.put(v11, HeadUtil.customHead(headNameMap().get(v11), headUUIDMap().get(v11), textureURLMap.get(v11)));
        headItemMap.put(v12, HeadUtil.customHead(headNameMap().get(v12), headUUIDMap().get(v12), textureURLMap.get(v12)));
        headItemMap.put(v13, HeadUtil.customHead(headNameMap().get(v13), headUUIDMap().get(v13), textureURLMap.get(v13)));
        headItemMap.put(v14, HeadUtil.customHead(headNameMap().get(v14), headUUIDMap().get(v14), textureURLMap.get(v14)));
        headItemMap.put(v15, HeadUtil.customHead(headNameMap().get(v15), headUUIDMap().get(v15), textureURLMap.get(v15)));

        List<DyeColor> types = List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
        for (DyeColor type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            ItemStack lootItem = lootItemsMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }


}
