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

public class IllusionerHead {

    private static final EntityType entityType = EntityType.ILLUSIONER;
    private static final ItemStack lootItem = new ItemStack(, );
    private static final Sound interactSound = Sound.ENTITY_ILLUSIONER_AMBIENT;
    private static final String headName = "Illusioner Head";
    private static final UUID headUUID = UUID.fromString("1941524e-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/4639d325f4494258a473a93a3b47f34a0c51b3fceaf59fee87205a5e7ff31f68");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
