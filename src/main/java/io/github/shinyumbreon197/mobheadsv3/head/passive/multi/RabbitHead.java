package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class RabbitHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.RABBIT;
    private static final ItemStack lootItem = buildRabbitBundle();

    private static final Sound interactSound = Sound.ENTITY_RABBIT_AMBIENT;
    //private static final Panda.Gene v0 = Panda.Gene.NORMAL;
    //private static final Panda.Gene v1 = Panda.Gene.BROWN;
    private static final Map< , String> headNameMap = Map.of(
            v0," Head",
            v1," Head",
            v2," Head",
            v3," Head",
            v4," Head",
            v5," Head"
    );
    private static final Map< , UUID> headUUIDMap = Map.of(
            v0, UUID.fromString(""),
            v1, UUID.fromString(""),
            v2, UUID.fromString(""),
            v3, UUID.fromString(""),
            v4, UUID.fromString(""),
            v5, UUID.fromString("")
    );

    public static void initialize(){
        Map< , URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL(""),
                    v1, new URL(""),
                    v2, new URL(""),
                    v3, new URL(""),
                    v4, new URL(""),
                    v5, new URL("")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map< , ItemStack> headItemMap = Map.of(
                v0, HeadUtil.customHead(headNameMap.get(v0), headUUIDMap.get(v0), textureURLMap.get(v0)),
                v1, HeadUtil.customHead(headNameMap.get(v1), headUUIDMap.get(v1), textureURLMap.get(v1)),
                v2, HeadUtil.customHead(headNameMap.get(v2), headUUIDMap.get(v2), textureURLMap.get(v2)),
                v3, HeadUtil.customHead(headNameMap.get(v3), headUUIDMap.get(v3), textureURLMap.get(v3)),
                v4, HeadUtil.customHead(headNameMap.get(v4), headUUIDMap.get(v4), textureURLMap.get(v4)),
                v5, HeadUtil.customHead(headNameMap.get(v5), headUUIDMap.get(v5), textureURLMap.get(v5))
        );
        List< > types = List.of(v0, v1, v2, v3, v4, v5);
        for ( type:types){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }

    private static ItemStack buildRabbitBundle(){
        ItemStack bundle = new ItemStack(Material.BUNDLE, 1);

        return bundle;
    }

}
