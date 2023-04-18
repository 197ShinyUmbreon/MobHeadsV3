package io.github.shinyumbreon197.mobheadsv3.head.hostile;

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

public class WitherHead {

    private static final EntityType entityType = EntityType.WITHER;
    private static final ItemStack lootItem = new ItemStack(Material.END_CRYSTAL, 8);
    private static final Sound interactSound = Sound.ENTITY_WITHER_AMBIENT;
    private static final String headName = "Wither Head";
    private static final UUID headUUID = UUID.fromString("194154ec-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/cdf74e323ed41436965f5c57ddf2815d5332fe999e68fbb9d6cf5c8bd4139f");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
