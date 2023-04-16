package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class RabbitHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.RABBIT;
    private static final ItemStack lootItem = buildRabbitBundle();

    private static final Sound interactSound = Sound.ENTITY_RABBIT_AMBIENT;
    private static final Rabbit.Type v0 = Rabbit.Type.BROWN;
    private static final Rabbit.Type v1 = Rabbit.Type.WHITE;
    private static final Rabbit.Type v2 = Rabbit.Type.BLACK;
    private static final Rabbit.Type v3 = Rabbit.Type.BLACK_AND_WHITE;
    private static final Rabbit.Type v4 = Rabbit.Type.GOLD;
    private static final Rabbit.Type v5 = Rabbit.Type.SALT_AND_PEPPER;
    private static final Map<Rabbit.Type, String> headNameMap = Map.of(
            v0,"Brown Rabbit Head",
            v1,"White Rabbit Head",
            v2,"Black Rabbit Head",
            v3,"Black & White Rabbit Head",
            v4,"Golden Rabbit Head",
            v5,"Salt & Pepper Rabbit Head"
    );
    private static final Map<Rabbit.Type , UUID> headUUIDMap = Map.of(
            v0, UUID.fromString("19415794-5fac-11ed-9b6a-0242ac120002"),
            v1, UUID.fromString("19415b54-5fac-11ed-9b6a-0242ac120002"),
            v2, UUID.fromString("19415cee-5fac-11ed-9b6a-0242ac120002"),
            v3, UUID.fromString("19415e4c-5fac-11ed-9b6a-0242ac120002"),
            v4, UUID.fromString("19415f96-5fac-11ed-9b6a-0242ac120002"),
            v5, UUID.fromString("1941627a-5fac-11ed-9b6a-0242ac120002")
    );

    public static void initialize(){
        Map<Rabbit.Type , URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL("http://textures.minecraft.net/texture/7d1169b2694a6aba826360992365bcda5a10c89a3aa2b48c438531dd8685c3a7"),
                    v1, new URL("http://textures.minecraft.net/texture/b4dcfed6897a18a7ab995a66134d41a1ca821b69bcb7d14cf269b4a98df49a8"),
                    v2, new URL("http://textures.minecraft.net/texture/72c58116a147d1a9a26269224a8be184fe8e5f3f3df9b61751369ad87382ec9"),
                    v3, new URL("http://textures.minecraft.net/texture/cb8cff4b15b8ca37e25750f345718f289cb22c5b3ad22627a71223faccc"),
                    v4, new URL("http://textures.minecraft.net/texture/c977a3266bf3b9eaf17e5a02ea5fbb46801159863dd288b93e6c12c9cb"),
                    v5, new URL("http://textures.minecraft.net/texture/ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<Rabbit.Type , ItemStack> headItemMap = Map.of(
                v0, HeadUtil.customHead(headNameMap.get(v0), headUUIDMap.get(v0), textureURLMap.get(v0)),
                v1, HeadUtil.customHead(headNameMap.get(v1), headUUIDMap.get(v1), textureURLMap.get(v1)),
                v2, HeadUtil.customHead(headNameMap.get(v2), headUUIDMap.get(v2), textureURLMap.get(v2)),
                v3, HeadUtil.customHead(headNameMap.get(v3), headUUIDMap.get(v3), textureURLMap.get(v3)),
                v4, HeadUtil.customHead(headNameMap.get(v4), headUUIDMap.get(v4), textureURLMap.get(v4)),
                v5, HeadUtil.customHead(headNameMap.get(v5), headUUIDMap.get(v5), textureURLMap.get(v5))
        );
        List<Rabbit.Type> types = List.of(v0, v1, v2, v3, v4, v5);
        for (Rabbit.Type type:types){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }

    private static ItemStack buildRabbitBundle(){
        ItemStack bundle = new ItemStack(Material.BUNDLE);
        BundleMeta bundleMeta = (BundleMeta) bundle.getItemMeta();
        ItemStack hide = new ItemStack(Material.RABBIT_HIDE);
        ItemStack foot = new ItemStack(Material.RABBIT_FOOT, 2);
        ItemStack body = new ItemStack(Material.RABBIT);
        List<ItemStack> contents = Arrays.asList(hide, foot, foot, body);
        assert bundleMeta != null;
        bundleMeta.setItems(contents);
        bundle.setItemMeta(bundleMeta);
        return bundle;
    }

}
