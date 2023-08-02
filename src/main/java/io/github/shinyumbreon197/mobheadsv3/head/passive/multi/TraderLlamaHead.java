package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class TraderLlamaHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.TRADER_LLAMA;
    private static final ItemStack lootItem = new ItemStack(Material.LEAD, 2);
    private static final Sound interactSound = Sound.ENTITY_LLAMA_AMBIENT;
    private static final Llama.Color v0 = Llama.Color.CREAMY;
    private static final Llama.Color v1 = Llama.Color.WHITE;
    private static final Llama.Color v2 = Llama.Color.BROWN;
    private static final Llama.Color v3 = Llama.Color.GRAY;

    private static Map<Llama.Color, String> headNameMap(){
        Map<Llama.Color, String> map = new HashMap<>();
        map.put(v0,"Creamy Trader Llama Head");
        map.put(v1,"White Trader Llama Head");
        map.put(v2,"Brown Trader Llama Head");
        map.put(v3,"Gray Trader Llama Head");
        return map;
    }

    private static Map<Llama.Color , UUID> headUUIDMap(){
        Map<Llama.Color, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("19419632-5fac-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("1941975e-5fac-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("194198bc-5fac-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("19419b0a-5fac-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Llama.Color , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("http://textures.minecraft.net/texture/e89a2eb17705fe7154ab041e5c76a08d41546a31ba20ea3060e3ec8edc10412c"));
            textureURLMap.put(v1, new URL("http://textures.minecraft.net/texture/7087a556d4ffa95ecd2844f350dc43e254e5d535fa596f540d7e77fa67df4696"));
            textureURLMap.put(v2, new URL("http://textures.minecraft.net/texture/8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d"));
            textureURLMap.put(v3, new URL("http://textures.minecraft.net/texture/be4d8a0bc15f239921efd8be3480ba77a98ee7d9ce00728c0d733f0a2d614d16"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<Llama.Color, ItemStack> headItemMap = new HashMap<>();
        headItemMap.put(v0, HeadUtil.customHead(headNameMap().get(v0), headUUIDMap().get(v0), textureURLMap.get(v0)));
        headItemMap.put(v1, HeadUtil.customHead(headNameMap().get(v1), headUUIDMap().get(v1), textureURLMap.get(v1)));
        headItemMap.put(v2, HeadUtil.customHead(headNameMap().get(v2), headUUIDMap().get(v2), textureURLMap.get(v2)));
        headItemMap.put(v3, HeadUtil.customHead(headNameMap().get(v3), headUUIDMap().get(v3), textureURLMap.get(v3)));

        List<Llama.Color> types = List.of(v0, v1, v2, v3);
        for (Llama.Color type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        Data.addMobHeads(mobHeads);
    }


}
