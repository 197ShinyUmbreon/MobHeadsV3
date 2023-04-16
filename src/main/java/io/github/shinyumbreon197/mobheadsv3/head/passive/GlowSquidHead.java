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

public class GlowSquidHead {

    private static final EntityType entityType = EntityType.GLOW_SQUID;
    private static final ItemStack lootItem = new ItemStack(Material.GLOW_INK_SAC, 8);
    private static final Sound interactSound = Sound.ENTITY_GLOW_SQUID_SQUIRT;
    private static final String headName = "Glow Squid Head";
    private static final UUID headUUID = UUID.fromString("194123be-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/2ecd0b5eb6b384db076d8446065202959dddff0161e0d723b3df0cc586d16bbd");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
