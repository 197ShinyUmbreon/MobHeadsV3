package io.github.shinyumbreon197.mobheadsv3.head.passive;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class TropicalFishHead {

    private static final EntityType entityType = EntityType.TROPICAL_FISH;
    private static final ItemStack lootItem = new ItemStack(Material.BONE_MEAL, 16);
    private static final Sound interactSound = Sound.ENTITY_TROPICAL_FISH_FLOP;
    private static final String headName = "Bloated Tropical Fish";
    private static final UUID headUUID = UUID.fromString("19410e06-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/12510b301b088638ec5c8747e2d754418cb747a5ce7022c9c712ecbdc5f6f065");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
