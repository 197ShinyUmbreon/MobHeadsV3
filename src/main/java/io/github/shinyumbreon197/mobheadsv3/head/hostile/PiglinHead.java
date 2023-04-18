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

public class PiglinHead {

    private static final EntityType entityType = EntityType.PIGLIN;
    private static final ItemStack lootItem = new ItemStack(Material.NETHER_GOLD_ORE, 1);
    private static final Sound interactSound = Sound.ENTITY_PIGLIN_AMBIENT;
    private static final String headName = "Piglin Head";
    private static final UUID headUUID = UUID.fromString("19413908-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/d71b3aee182b9a99ed26cbf5ecb47ae90c2c3adc0927dde102c7b30fdf7f4545");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
