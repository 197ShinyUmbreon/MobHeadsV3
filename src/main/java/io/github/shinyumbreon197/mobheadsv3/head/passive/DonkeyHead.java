package io.github.shinyumbreon197.mobheadsv3.head.passive;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class DonkeyHead {

    private static final EntityType entityType = EntityType.DONKEY;
    private static final ItemStack lootItem = new ItemStack(Material.LEATHER, 4);
    private static final Sound interactSound = Sound.ENTITY_DONKEY_AMBIENT;
    private static final String headName = "Donkey Head";
    private static final UUID headUUID = UUID.fromString("19410442-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/dfb6c3c052cf787d236a2915f8072b77c547497715d1d2f8cbc9d241d88a");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
