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
import java.util.UUID;

public class ZoglinHead {

    private static final EntityType entityType = EntityType.ZOGLIN;
    private static final ItemStack lootItem = new ItemStack(Material.ROTTEN_FLESH, 16);
    private static final Sound interactSound = Sound.ENTITY_ZOGLIN_AMBIENT;
    private static final String headName = "Zoglin Head";
    private static final UUID headUUID = UUID.fromString("194134f8-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/3c8c7c5d0556cd6629716e39188b21e7c0477479f242587bf19e0bc76b322551");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
