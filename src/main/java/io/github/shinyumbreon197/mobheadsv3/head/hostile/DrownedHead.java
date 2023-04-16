package io.github.shinyumbreon197.mobheadsv3.head.hostile;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DrownedHead {

    private static final EntityType entityType = EntityType.DROWNED;
    private static final ItemStack lootItem = new ItemStack(Material.SEA_PICKLE, 6);
    private static final Sound interactSound = getInteractSound();
    private static final String headName = "Drowned Head";
    private static final UUID headUUID = UUID.fromString("194132b4-5fac-11ed-9b6a-0242ac120002");

    private static Sound getInteractSound(){
        Random random = new Random();
        List<Sound> sounds = List.of(Sound.ENTITY_DROWNED_AMBIENT, Sound.ENTITY_DROWNED_AMBIENT_WATER);
        return sounds.get(random.nextInt(sounds.size()));
    }

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/c84df79c49104b198cdad6d99fd0d0bcf1531c92d4ab6269e40b7d3cbbb8e98c");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }


}
