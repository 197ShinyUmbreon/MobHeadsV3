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

public class EvokerHead {

    private static final EntityType entityType = EntityType.EVOKER;
    private static final ItemStack lootItem = new ItemStack(Material.EMERALD_BLOCK, 1);
    private static final Sound interactSound = Sound.ENTITY_EVOKER_AMBIENT;
    private static final String headName = "Evoker Head";
    private static final UUID headUUID = UUID.fromString("1941406a-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
