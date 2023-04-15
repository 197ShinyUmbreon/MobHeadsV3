package io.github.shinyumbreon197.mobheadsv3.head.hostile;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftTippedArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class StrayHead {

    private static final EntityType entityType = EntityType.STRAY;
    private static final ItemStack lootItem = buildTippedArrows();
    private static final Sound interactSound = Sound.ENTITY_STRAY_AMBIENT;
    private static final String headName = "Stray Head";
    private static final UUID headUUID = UUID.fromString("19412d50-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/2c5097916bc0565d30601c0eebfeb287277a34e867b4ea43c63819d53e89ede7");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

    private static ItemStack buildTippedArrows(){
        ItemStack arrows = new ItemStack(Material.TIPPED_ARROW, 16);
        PotionMeta potionMeta = (PotionMeta) arrows.getItemMeta();
        assert potionMeta != null;
        PotionData potionData = new PotionData(PotionType.SLOWNESS,false,false);
        potionMeta.setBasePotionData(potionData);
        arrows.setItemMeta(potionMeta);
        return arrows;
    }

}
