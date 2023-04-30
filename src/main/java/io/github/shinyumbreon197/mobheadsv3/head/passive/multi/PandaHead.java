package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PandaHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.PANDA;
    private static final ItemStack lootItem = new ItemStack(Material.BAMBOO, 16);
    private static final Sound interactSound = Sound.ENTITY_PANDA_AMBIENT;
    private static final Panda.Gene v0 = Panda.Gene.NORMAL;
    private static final Panda.Gene v1 = Panda.Gene.BROWN;
    private static final Map<Panda.Gene, String> headNameMap = Map.of(
            v0,"Panda Head",
            v1, "Brown Panda Head"
    );
    private static final Map<Panda.Gene, UUID> headUUIDMap = Map.of(
            v0, UUID.fromString("19411a04-5fac-11ed-9b6a-0242ac120002"),
            v1, UUID.fromString("766cac7a-5fe3-11ed-9b6a-0242ac120002")
    );

    public static void initialize(){
        Map<Panda.Gene, URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL("http://textures.minecraft.net/texture/dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166"),
                    v1, new URL("http://textures.minecraft.net/texture/b4f7c73fda6a34cf8be4c7907dd0f5f0865dd77fd882fc633563649c57517cae")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<Panda.Gene, ItemStack> headItemMap = Map.of(
                v0, HeadUtil.customHead(headNameMap.get(v0), headUUIDMap.get(v0), textureURLMap.get(v0)),
                v1, HeadUtil.customHead(headNameMap.get(v1), headUUIDMap.get(v1), textureURLMap.get(v1))
        );
        List<Panda.Gene> types = List.of(v0, v1);
        for (Panda.Gene type:types){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        Data.addMobHeads(mobHeads);
    }

}
