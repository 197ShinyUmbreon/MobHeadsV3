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

public class SalmonHead {

    private static final EntityType entityType = EntityType.SALMON;
    private static final ItemStack lootItem = new ItemStack(Material.BONE_MEAL, 16);
    private static final Sound interactSound = Sound.ENTITY_SALMON_FLOP;
    private static final String headName = "Bloated Salmon";
    private static final UUID headUUID = UUID.fromString("19410bae-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/8aeb21a25e46806ce8537fbd6668281cf176ceafe95af90e94a5fd84924878");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
