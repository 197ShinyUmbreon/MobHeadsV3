package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrogHead {

    public static NamespacedKey frogFoodKey = new NamespacedKey(MobHeadsV3.getPlugin(), "frogfood");

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.FROG;
    private static final ItemStack lootItem = new ItemStack(Material.SLIME_BALL, 8);

    private static final Sound interactSound = Sound.ENTITY_FROG_AMBIENT;
    private static final Frog.Variant v0 = Frog.Variant.TEMPERATE;
    private static final Frog.Variant v1 = Frog.Variant.WARM;
    private static final Frog.Variant v2 = Frog.Variant.COLD;
    private static final Map<Frog.Variant, String> headNameMap = Map.of(
            v0,"Temperate Frog Head",
            v1,"Warm Frog Head",
            v2,"Cold Frog Head"
    );
    private static final Map<Frog.Variant , UUID> headUUIDMap = Map.of(
            v0, UUID.fromString("766c6792-5fe3-11ed-9b6a-0242ac120002"),
            v1, UUID.fromString("766c697c-5fe3-11ed-9b6a-0242ac120002"),
            v2, UUID.fromString("766c7318-5fe3-11ed-9b6a-0242ac120002")
    );

    public static void initialize(){
        Map<Frog.Variant , URL> textureURLMap;
        try{
            textureURLMap = Map.of(
                    v0, new URL("http://textures.minecraft.net/texture/1f3e29dd947a177895f6121d2331b65ac3f896fda4bdd1151491e40b804952a7"),
                    v1, new URL("http://textures.minecraft.net/texture/1e9312b5b2bab9ad51ea4b6a407d6d390bb5043408757b976a7556898ac43de0"),
                    v2, new URL("http://textures.minecraft.net/texture/27bcccc125a4110434a85c40ada039d050f14ef7db34a3444067310f8ce69606")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<Frog.Variant , ItemStack> headItemMap = Map.of(
                v0, HeadUtil.customHead(headNameMap.get(v0), headUUIDMap.get(v0), textureURLMap.get(v0)),
                v1, HeadUtil.customHead(headNameMap.get(v1), headUUIDMap.get(v1), textureURLMap.get(v1)),
                v2, HeadUtil.customHead(headNameMap.get(v2), headUUIDMap.get(v2), textureURLMap.get(v2))
        );
        List<Frog.Variant> types = List.of(v0, v1, v2);
        for (Frog.Variant type:types){
            UUID uuid = headUUIDMap.get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap.get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        Data.addMobHeads(mobHeads);
    }


}
