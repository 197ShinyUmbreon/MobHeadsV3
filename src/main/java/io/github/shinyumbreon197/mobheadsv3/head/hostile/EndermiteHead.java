package io.github.shinyumbreon197.mobheadsv3.head.hostile;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
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

public class EndermiteHead {

    private static final EntityType entityType = EntityType.ENDERMITE;
    private static final ItemStack lootItem = new ItemStack(Material.POPPED_CHORUS_FRUIT, 1);
    private static final Sound interactSound = Sound.ENTITY_ENDERMITE_AMBIENT;
    private static final String headName = "Enlarged Endermite Head";
    private static final UUID headUUID = UUID.fromString("19414e8e-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
