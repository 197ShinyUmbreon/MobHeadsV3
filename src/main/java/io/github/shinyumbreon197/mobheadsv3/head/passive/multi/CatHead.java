package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CatHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.CAT;
    private static final ItemStack lootItem = new ItemStack(Material.STRING, 1);

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
        map.put(v0,"Tabby Cat Head");
        map.put(v1,"Black Cat Head");
        map.put(v2,"Orange Cat Head");
        map.put(v3,"Siamese Cat Head");
        map.put(v4,"British Shorthair Cat Head");
        map.put(v5,"Calico Cat Head");
        map.put(v6,"Persian Cat Head");
        map.put(v7,"Ragdoll Cat Head");
        map.put(v8,"White Cat Head");
        map.put(v9,"Jellie Cat Head");
        map.put(v10,"Tuxedo Cat Head");
        return map;
    }

    private static Map<Cat.Type , UUID> headUUIDMap(){
        Map<Cat.Type, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("19416f2c-5fac-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("194170d0-5fac-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("1941721a-5fac-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("19417346-5fac-11ed-9b6a-0242ac120002"));
        map.put(v4, UUID.fromString("19417472-5fac-11ed-9b6a-0242ac120002"));
        map.put(v5, UUID.fromString("194175b2-5fac-11ed-9b6a-0242ac120002"));
        map.put(v6, UUID.fromString("19417a8a-5fac-11ed-9b6a-0242ac120002"));
        map.put(v7, UUID.fromString("19417bd4-5fac-11ed-9b6a-0242ac120002"));
        map.put(v8, UUID.fromString("19417cf6-5fac-11ed-9b6a-0242ac120002"));
        map.put(v9, UUID.fromString("19417e7c-5fac-11ed-9b6a-0242ac120002"));
        map.put(v10, UUID.fromString("19417fb2-5fac-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Cat.Type , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("http://textures.minecraft.net/texture/de28d30db3f8c3fe50ca4f26f3075e36f003ae8028135a8cd692f24c9a98ae1b"));
            textureURLMap.put(v1, new URL("http://textures.minecraft.net/texture/22c1e81ff03e82a3e71e0cd5fbec607e11361089aa47f290d46c8a2c07460d92"));
            textureURLMap.put(v2, new URL("http://textures.minecraft.net/texture/2113dbd3c6a078a17b4edb78ce07d836c38dace5027d4b0a83fd60e7ca7a0fcb"));
            textureURLMap.put(v3, new URL("http://textures.minecraft.net/texture/d5b3f8ca4b3a555ccb3d194449808b4c9d783327197800d4d65974cc685af2ea"));
            textureURLMap.put(v4, new URL("http://textures.minecraft.net/texture/5389e0d5d3e81f84b570e2978244b3a73e5a22bcdb6874b44ef5d0f66ca24eec"));
            textureURLMap.put(v5, new URL("http://textures.minecraft.net/texture/340097271bb680fe981e859e8ba93fea28b813b1042bd277ea3329bec493eef3"));
            textureURLMap.put(v6, new URL("http://textures.minecraft.net/texture/ff40c746260ef91c96b27159795e87191ae7ce3d5f767bf8c74faad9689af25d"));
            textureURLMap.put(v7, new URL("http://textures.minecraft.net/texture/dc7a45d25889e3fdf7797cb258e26d4e94f5bc13eef00795dafef2e83e0ab511"));
            textureURLMap.put(v8, new URL("http://textures.minecraft.net/texture/21d15ac9558e98b89aca89d3819503f1c5256c2197dd3c34df5aac4d72e7fbed"));
            textureURLMap.put(v9, new URL("http://textures.minecraft.net/texture/a0db41376ca57df10fcb1539e86654eecfd36d3fe75e8176885e93185df280a5"));
            textureURLMap.put(v10, new URL("http://textures.minecraft.net/texture/4fd10c8e75f67398c47587d25fc146f311c053cc5d0aeab8790bce36ee88f5f8"));
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
