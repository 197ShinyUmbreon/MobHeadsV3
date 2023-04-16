package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
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

public class LlamaHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.LLAMA;
    private static final ItemStack lootItem = new ItemStack(Material.CHEST, 2);
    private static final Sound interactSound = Sound.ENTITY_LLAMA_AMBIENT;
    private static final Llama.Color v0 = Llama.Color.CREAMY;
    private static final Llama.Color v1 = Llama.Color.WHITE;
    private static final Llama.Color v2 = Llama.Color.BROWN;
    private static final Llama.Color v3 = Llama.Color.GRAY;

    private static Map<Llama.Color, String> headNameMap(){
        Map<Llama.Color, String> map = new HashMap<>();
        map.put(v0,"Creamy Llama Head");
        map.put(v1,"White Llama Head");
        map.put(v2,"Brown Llama Head");
        map.put(v3,"Gray Llama Head");
        return map;
    }

    private static Map<Llama.Color , UUID> headUUIDMap(){
        Map<Llama.Color, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("19418d04-5fac-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("19419286-5fac-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("194193ee-5fac-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("19419510-5fac-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Llama.Color , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("http://textures.minecraft.net/texture/2a5f10e6e6232f182fe966f501f1c3799d45ae19031a1e4941b5dee0feff059b"));
            textureURLMap.put(v1, new URL("http://textures.minecraft.net/texture/83d9b5915912ffc2b85761d6adcb428a812f9b83ff634e331162ce46c99e9"));
            textureURLMap.put(v2, new URL("http://textures.minecraft.net/texture/c2b1ecff77ffe3b503c30a548eb23a1a08fa26fd67cdff389855d74921368"));
            textureURLMap.put(v3, new URL("http://textures.minecraft.net/texture/cf24e56fd9ffd7133da6d1f3e2f455952b1da462686f753c597ee82299a"));
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
        HeadData.addMobHeads(mobHeads);
    }


}
