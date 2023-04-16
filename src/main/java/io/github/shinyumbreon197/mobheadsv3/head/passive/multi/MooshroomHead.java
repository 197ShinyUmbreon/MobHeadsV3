package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MooshroomHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.AXOLOTL;
    private static Map<MushroomCow.Variant, ItemStack> lootItemMap(){
        Map<MushroomCow.Variant, ItemStack> map = new HashMap<>();
        map.put(MushroomCow.Variant.RED, new ItemStack(Material.RED_MUSHROOM_BLOCK, 32));
        map.put(MushroomCow.Variant.BROWN, new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 32));
        return map;
    }

    private static final Sound interactSound = Sound.ENTITY_COW_AMBIENT;
    private static final MushroomCow.Variant v0 = MushroomCow.Variant.RED;
    private static final MushroomCow.Variant v1 = MushroomCow.Variant.BROWN;

    private static final Map<MushroomCow.Variant, String> headNameMap = Map.of(
            v0,"Mooshroom Head",
            v1,"Brown Mooshroom Head"
    );
    private static final Map<MushroomCow.Variant , UUID> headUUIDMap = Map.of(
            v0, UUID.fromString("766c63c8-5fe3-11ed-9b6a-0242ac120002"),
            v1, UUID.fromString("766c65bc-5fe3-11ed-9b6a-0242ac120002")
    );

    public static void initialize(){
        Map<MushroomCow.Variant , URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL("http://textures.minecraft.net/texture/2b52841f2fd589e0bc84cbabf9e1c27cb70cac98f8d6b3dd065e55a4dcb70d77"),
                    v1, new URL("http://textures.minecraft.net/texture/b6d5fc7031acc95beeb52875f15408e979a0a9c391b6db7ecee7e400072de5c4")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<MushroomCow.Variant , ItemStack> headItemMap = Map.of(
                v0, HeadUtil.customHead(headNameMap.get(v0), headUUIDMap.get(v0), textureURLMap.get(v0)),
                v1, HeadUtil.customHead(headNameMap.get(v1), headUUIDMap.get(v1), textureURLMap.get(v1))
        );
        List<MushroomCow.Variant> types = List.of(v0, v1);
        for (MushroomCow.Variant type:types){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            ItemStack lootItem = lootItemMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }


}
