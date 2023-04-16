package io.github.shinyumbreon197.mobheadsv3.head.hostile.multi;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ZombieVillagerHead {

    private static final List<MobHead> mobHeads = new ArrayList<>();
    private static final EntityType entityType = EntityType.ZOMBIE_VILLAGER;
    private static final ItemStack lootItem = buildWeaknessPotion();

    private static final Sound interactSound = Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    private static final Villager.Type v0 = Villager.Type.DESERT;
    private static final Villager.Type v1 = Villager.Type.JUNGLE;
    private static final Villager.Type v2 = Villager.Type.PLAINS;
    private static final Villager.Type v3 = Villager.Type.SAVANNA;
    private static final Villager.Type v4 = Villager.Type.SNOW;
    private static final Villager.Type v5 = Villager.Type.SWAMP;
    private static final Villager.Type v6 = Villager.Type.TAIGA;

    private static Map<Villager.Type, String> headNameMap(){
        Map<Villager.Type, String> map = new HashMap<>();
        map.put(v0,"Desert Zombie Villager Head");
        map.put(v1,"Jungle Zombie Villager Head");
        map.put(v2,"Plains Zombie Villager Head");
        map.put(v3,"Savanna Zombie Villager Head");
        map.put(v4,"Snow Zombie Villager Head");
        map.put(v5,"Swamp Zombie Villager Head");
        map.put(v6,"Taiga Zombie Villager Head");
        return map;
    }

    private static Map<Villager.Type , UUID> headUUIDMap(){
        Map<Villager.Type, UUID> map = new HashMap<>();
        map.put(v0, UUID.fromString("766c8a9c-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v1, UUID.fromString("766c8cd6-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v2, UUID.fromString("766c8eac-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v3, UUID.fromString("766c90a0-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v4, UUID.fromString("766c9276-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v5, UUID.fromString("766c95dc-5fe3-11ed-9b6a-0242ac120002"));
        map.put(v6, UUID.fromString("766c9c3a-5fe3-11ed-9b6a-0242ac120002"));
        return map;
    }

    public static void initialize(){
        Map<Villager.Type , URL> textureURLMap = new HashMap<>();
        try{
            textureURLMap.put(v0, new URL("https://textures.minecraft.net/texture/41225ae82e4918eb84f4687fe97a83b291515e7a56e55499c8b046aed2d6e182"));
            textureURLMap.put(v1, new URL("https://textures.minecraft.net/texture/649a46275dec0c247df986dfb4b351d289f0242b5fcd620daae113725720c7c9"));
            textureURLMap.put(v2, new URL("https://textures.minecraft.net/texture/c45c11e0327035649ca0600ef938900e25fd1e38017422bc9740e4cda2cba892"));
            textureURLMap.put(v3, new URL("https://textures.minecraft.net/texture/ab24ec3998250f9508361039182266687103b894ec8b94883feacd3c351db506"));
            textureURLMap.put(v4, new URL("https://textures.minecraft.net/texture/ef12b7b53001ef851719fdf8c088de03b11d4ae43f6ff53ac5a267d24df1dc60"));
            textureURLMap.put(v5, new URL("https://textures.minecraft.net/texture/22cca2d9f0aef42cc3086415e1419560f34334b167bebfefecc43492cb141789"));
            textureURLMap.put(v6, new URL("https://textures.minecraft.net/texture/279862124fcf95aece708624d40a4fe4658351113e436524eb5f20f8fe5e66b6"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Map<Villager.Type, ItemStack> headItemMap = new HashMap<>();
        headItemMap.put(v0, HeadUtil.customHead(headNameMap().get(v0), headUUIDMap().get(v0), textureURLMap.get(v0)));
        headItemMap.put(v1, HeadUtil.customHead(headNameMap().get(v1), headUUIDMap().get(v1), textureURLMap.get(v1)));
        headItemMap.put(v2, HeadUtil.customHead(headNameMap().get(v2), headUUIDMap().get(v2), textureURLMap.get(v2)));
        headItemMap.put(v3, HeadUtil.customHead(headNameMap().get(v3), headUUIDMap().get(v3), textureURLMap.get(v3)));
        headItemMap.put(v4, HeadUtil.customHead(headNameMap().get(v4), headUUIDMap().get(v4), textureURLMap.get(v4)));
        headItemMap.put(v5, HeadUtil.customHead(headNameMap().get(v5), headUUIDMap().get(v5), textureURLMap.get(v5)));
        headItemMap.put(v6, HeadUtil.customHead(headNameMap().get(v6), headUUIDMap().get(v6), textureURLMap.get(v6)));

        List<Villager.Type> types = List.of(v0, v1, v2, v3, v4, v5, v6);
        for (Villager.Type type:types){
            UUID uuid = headUUIDMap().get(type);
            ItemStack head = headItemMap.get(type);
            String name = headNameMap().get(type);
            MobHead mobHead = new MobHead(entityType, type.toString(), name, head, lootItem, uuid, interactSound);
            mobHeads.add(mobHead);
        }
        HeadData.addMobHeads(mobHeads);
    }

    private static ItemStack buildWeaknessPotion(){
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionData potionData = new PotionData(PotionType.WEAKNESS, false, false);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(potionData);
        potion.setItemMeta(potionMeta);
        return potion;
    }

}
