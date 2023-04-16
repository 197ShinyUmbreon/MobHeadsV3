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

public class AllayHead {

    private static final EntityType entityType = EntityType.ALLAY;
    private static final ItemStack lootItem = new ItemStack(Material.MUSIC_DISC_11, 1);
    private static final Sound interactSound = Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM;
    private static final String headName = "Allay Head";
    private static final UUID headUUID = UUID.fromString("194124d6-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/e1c59dccde4b8535500dcf6794ca450663f607290e2510f6d8eb1e5eb71da5af");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
