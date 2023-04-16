package io.github.shinyumbreon197.mobheadsv3.head.hostile;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.Trophies;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class VexHead {

    private static final EntityType entityType = EntityType.VEX;
    private static final ItemStack lootItem = Trophies.getVexSword();
    private static final Sound interactSound = Sound.ENTITY_VEX_AMBIENT;
    private static final String headName = "Enlarged Vex Head";
    private static final UUID headUUID = UUID.fromString("19413f16-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        HeadData.addMobHead(mobHead);
    }

}
