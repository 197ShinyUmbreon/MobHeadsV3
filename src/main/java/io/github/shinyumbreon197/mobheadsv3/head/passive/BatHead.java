package io.github.shinyumbreon197.mobheadsv3.head.passive;

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

public class BatHead {

    private static final EntityType entityType = EntityType.BAT;
    private static final ItemStack lootItem = new ItemStack(Material.DEEPSLATE_COAL_ORE, 6);
    private static final Sound interactSound = Sound.ENTITY_BAT_AMBIENT;
    private static final String headName = "Enlarged Bat Head";
    private static final UUID headUUID = UUID.fromString("1941166c-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/6ffd808f8127b4ad458d9d2e181c690adf489a6ad32ee2aa4acfa6341fe842");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
