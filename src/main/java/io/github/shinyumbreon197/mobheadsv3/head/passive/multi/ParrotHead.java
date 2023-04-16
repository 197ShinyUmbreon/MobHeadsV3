package io.github.shinyumbreon197.mobheadsv3.head.passive.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ParrotHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.PARROT;
    private static final ItemStack lootItem = new ItemStack(Material.FEATHER, 4);
    private static final Sound interactSound = Sound.ENTITY_PARROT_AMBIENT;
    private static final Parrot.Variant v0 = Parrot.Variant.RED;
    private static final Parrot.Variant v1 = Parrot.Variant.BLUE;
    private static final Parrot.Variant v2 = Parrot.Variant.GREEN;
    private static final Parrot.Variant v3 = Parrot.Variant.CYAN;
    private static final Parrot.Variant v4 = Parrot.Variant.GRAY;
    private static Map<Parrot.Variant, String> headNameMap(){
        Map<Parrot.Variant, String> map = new HashMap<>();
        map.put(v0,"Ballooned Red Parrot");
        map.put(v1,"Ballooned Blue Parrot");
        map.put(v2,"Ballooned Green Parrot");
        map.put(v3,"Ballooned Cyan Parrot");
        map.put(v4,"Ballooned Gray Parrot");
        return map;
    }

    private static Map<Parrot.Variant , UUID> headUUIDMap(){
        Map<Parrot.Variant, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("1941a06e-5fac-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("1941a1cc-5fac-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("1941a302-5fac-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("1941a438-5fac-11ed-9b6a-0242ac120002"));
        map.put(v4, UUID.fromString("766c2476-5fe3-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Parrot.Variant , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("http://textures.minecraft.net/texture/c38796f62db5f93949ae26a2f7a3c5f797a31d2694bce4c48ee843ee85f7"));
            textureURLMap.put(v1, new URL("http://textures.minecraft.net/texture/dcb934e4a5d51c2c958dd2c9f03e1915a4722487bcd8bcd71eddc53377622d9d"));
            textureURLMap.put(v2, new URL("http://textures.minecraft.net/texture/9fbb3deb3d8adeea9914acb7a073ca566c3fec7f58fd63d6197af52fbdbf8780"));
            textureURLMap.put(v3, new URL("http://textures.minecraft.net/texture/a9df6dd4f9434d44c97dbac4fa98591f1e37506355b9e4406f716bec7dd248e"));
            textureURLMap.put(v4, new URL("http://textures.minecraft.net/texture/efe08d511499a247146128e55ab6547ecd967d4dbcf803f7ceea2658737c9fa"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<Parrot.Variant, ItemStack> headItemMap = new HashMap<>();
        headItemMap.put(v0, HeadUtil.customHead(headNameMap().get(v0), headUUIDMap().get(v0), textureURLMap.get(v0)));
        headItemMap.put(v1, HeadUtil.customHead(headNameMap().get(v1), headUUIDMap().get(v1), textureURLMap.get(v1)));
        headItemMap.put(v2, HeadUtil.customHead(headNameMap().get(v2), headUUIDMap().get(v2), textureURLMap.get(v2)));
        headItemMap.put(v3, HeadUtil.customHead(headNameMap().get(v3), headUUIDMap().get(v3), textureURLMap.get(v3)));
        headItemMap.put(v4, HeadUtil.customHead(headNameMap().get(v4), headUUIDMap().get(v4), textureURLMap.get(v4)));

        List<Parrot.Variant> types = List.of(v0, v1, v2, v3, v4);
        for (Parrot.Variant type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }

    private static List<Sound> parrotSounds;
    public static List<Sound> getParrotSounds(){
        if (parrotSounds == null) parrotSounds = buildParrotSounds();
        return parrotSounds;
    }
    private static List<Sound> buildParrotSounds(){
        List<Sound> sounds = new ArrayList<>();
        for (Sound sound:Sound.values()){
            if (sound.toString().contains("PARROT_IMITATE")) sounds.add(sound);
        }
        return sounds;
    }


}
