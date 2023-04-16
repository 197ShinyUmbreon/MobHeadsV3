package io.github.shinyumbreon197.mobheadsv3.head.passive;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class PufferfishHead {

    private static final EntityType entityType = EntityType.PUFFERFISH;
    private static final Sound interactSound = Sound.ENTITY_PUFFER_FISH_BLOW_UP;
    private static final String headName = "Bloated Pufferfish";
    private static final UUID headUUID = UUID.fromString("19410cd0-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        ItemStack lootItem = buildPotion();
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

    private static ItemStack buildPotion(){
        ItemStack potion = new ItemStack(Material.POTION);
        PotionData potionData = new PotionData(PotionType.POISON, false, false);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(potionData);
        potion.setItemMeta(potionMeta);
        return potion;
    }

}
