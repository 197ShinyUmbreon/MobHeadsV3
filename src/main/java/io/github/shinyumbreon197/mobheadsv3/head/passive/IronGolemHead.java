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

public class IronGolemHead {

    private static final EntityType entityType = EntityType.IRON_GOLEM;
    private static final ItemStack lootItem = new ItemStack(Material.POPPY, 1);
    private static final Sound interactSound = Sound.ENTITY_IRON_GOLEM_STEP;
    private static final String headName = "Iron Golem Head";
    private static final UUID headUUID = UUID.fromString("19412292-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/fcecba31f26919d92a3d6420cd2fa9112f8e108ac04e3fc71da7329cd10fe5ca");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
