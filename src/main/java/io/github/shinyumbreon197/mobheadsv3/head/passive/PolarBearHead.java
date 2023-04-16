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

public class PolarBearHead {

    private static final EntityType entityType = EntityType.POLAR_BEAR;
    private static final ItemStack lootItem = new ItemStack(Material.COD, 1);
    private static final Sound interactSound = Sound.ENTITY_POLAR_BEAR_AMBIENT;
    private static final String headName = "Polar Bear Head";
    private static final UUID headUUID = UUID.fromString("19411b26-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
