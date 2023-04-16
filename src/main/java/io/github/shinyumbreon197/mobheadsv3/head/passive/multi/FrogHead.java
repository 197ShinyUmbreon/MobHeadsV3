package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrogHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.AXOLOTL;
    private static final ItemStack lootItem = buildRegenerationPotion();

    private static final Sound interactSound = Sound.ENTITY_RABBIT_AMBIENT;
    private static final Axolotl.Variant v0 = Axolotl.Variant.LUCY;
    private static final Axolotl.Variant v1 = Axolotl.Variant.WILD;
    private static final Axolotl.Variant v2 = Axolotl.Variant.GOLD;
    private static final Axolotl.Variant v3 = Axolotl.Variant.CYAN;
    private static final Axolotl.Variant v4 = Axolotl.Variant.BLUE;
    private static final Map<Axolotl.Variant, String> headNameMap = Map.of(
            v0,"Brown Axolotl Head",
            v1,"White Axolotl Head",
            v2,"Black Axolotl Head",
            v3,"Black & White Axolotl Head",
            v4,"Golden Axolotl Head"
    );
    private static final Map<Axolotl.Variant , UUID> headUUIDMap = Map.of(
            v0, UUID.fromString(""),
            v1, UUID.fromString(""),
            v2, UUID.fromString(""),
            v3, UUID.fromString(""),
            v4, UUID.fromString("")
    );

    public static void initialize(){
        Map<Axolotl.Variant , URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL(""),
                    v1, new URL(""),
                    v2, new URL(""),
                    v3, new URL(""),
                    v4, new URL("")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<Axolotl.Variant , ItemStack> headItemMap = Map.of(
                v0, HeadUtil.customHead(headNameMap.get(v0), headUUIDMap.get(v0), textureURLMap.get(v0)),
                v1, HeadUtil.customHead(headNameMap.get(v1), headUUIDMap.get(v1), textureURLMap.get(v1)),
                v2, HeadUtil.customHead(headNameMap.get(v2), headUUIDMap.get(v2), textureURLMap.get(v2)),
                v3, HeadUtil.customHead(headNameMap.get(v3), headUUIDMap.get(v3), textureURLMap.get(v3)),
                v4, HeadUtil.customHead(headNameMap.get(v4), headUUIDMap.get(v4), textureURLMap.get(v4))
        );
        List<Axolotl.Variant> types = List.of(v0, v1, v2, v3, v4);
        for (Axolotl.Variant type:types){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }


}
