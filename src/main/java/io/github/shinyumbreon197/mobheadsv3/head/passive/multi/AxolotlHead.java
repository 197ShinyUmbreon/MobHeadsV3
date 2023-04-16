package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AxolotlHead {

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
            v0, UUID.fromString("19416428-5fac-11ed-9b6a-0242ac120002"),
            v1, UUID.fromString("1941673e-5fac-11ed-9b6a-0242ac120002"),
            v2, UUID.fromString("194168c4-5fac-11ed-9b6a-0242ac120002"),
            v3, UUID.fromString("19416c8e-5fac-11ed-9b6a-0242ac120002"),
            v4, UUID.fromString("19416dd8-5fac-11ed-9b6a-0242ac120002")
    );

    public static void initialize(){
        Map<Axolotl.Variant , URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL("http://textures.minecraft.net/texture/5c138f401c67fc2e1e387d9c90a9691772ee486e8ddbf2ed375fc8348746f936"),
                    v1, new URL("http://textures.minecraft.net/texture/4d7efe02012cf31ae2708e7d7df079726575c7ee8504328175fe544708187dce"),
                    v2, new URL("http://textures.minecraft.net/texture/7f80cc1492e44668cccdb40178c3a6689e8dfc0d234e98553fb7debc26fcaeac"),
                    v3, new URL("http://textures.minecraft.net/texture/e1c2d0c3b96ad45b466388e028b247aafe36b26b12c411ecb72e9b50ea21e52c"),
                    v4, new URL("http://textures.minecraft.net/texture/eef630657e4a279b0b7ea0f67905920af365f9c84ca9f34a32b53343ff629910")
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

    private static ItemStack buildRegenerationPotion(){
        ItemStack potion = new ItemStack(Material.POTION);
        PotionData potionData = new PotionData(PotionType.REGEN, true, false);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(potionData);
        potion.setItemMeta(potionMeta);
        return potion;
    }

}
