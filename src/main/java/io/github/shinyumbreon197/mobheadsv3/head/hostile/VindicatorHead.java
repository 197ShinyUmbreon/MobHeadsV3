package io.github.shinyumbreon197.mobheadsv3.head.hostile;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.Trophies;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class VindicatorHead {

    private static final EntityType entityType = EntityType.VINDICATOR;
    private static final ItemStack lootItem = Trophies.getVindicatorAxe();
    private static final Sound interactSound = Sound.ENTITY_VINDICATOR_AMBIENT;
    private static final String headName = "Vindicator Head";
    private static final UUID headUUID = UUID.fromString("1941510e-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
