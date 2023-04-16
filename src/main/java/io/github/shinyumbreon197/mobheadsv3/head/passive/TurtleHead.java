package io.github.shinyumbreon197.mobheadsv3.head.passive;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class TurtleHead {

    private static final EntityType entityType = EntityType.TURTLE;
    private static final ItemStack lootItem = new ItemStack(Material.SEAGRASS, 4);
    private static final Sound interactSound = Sound.ENTITY_TURTLE_AMBIENT_LAND;
    private static final String headName = "Turtle Head";
    private static final UUID headUUID = UUID.fromString("19410f28-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
