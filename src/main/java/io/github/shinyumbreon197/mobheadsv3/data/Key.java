package io.github.shinyumbreon197.mobheadsv3.data;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Key {

    private static final MobHeadsV3 plugin = MobHeadsV3.getPlugin();

    // Keys ------------------------------------------------------------------------------------------------------------
    public static NamespacedKey master = new NamespacedKey(plugin,"mobheadsv3");

    // Targeting -------------------------------------------------------------------------------------------------------
    public static NamespacedKey lastHeadedTarget = new NamespacedKey(plugin,"lastHeadedTarget");

    // Decoy -----------------------------------------------------------------------------------------------------------
    public static NamespacedKey decoy = new NamespacedKey(plugin,"decoy");

    // Damage Tracking -------------------------------------------------------------------------------------------------
    public static NamespacedKey abilityProjectile = new NamespacedKey(plugin,"abilityProjectile");
    public static NamespacedKey tookAbilityDamage = new NamespacedKey(plugin,"tookAbilityDamage");

    // Potion Effect Keys ----------------------------------------------------------------------------------------------
    public static NamespacedKey potionType = new NamespacedKey(plugin, "potionType");
    public static NamespacedKey potionStrength = new NamespacedKey(plugin, "potionStrength");
    public static NamespacedKey potionDuration = new NamespacedKey(plugin, "potionDuration");

    // Head ID ---------------------------------------------------------------------------------------------------------
    public static NamespacedKey headUUID = new NamespacedKey(plugin, "headUUID");
    public static NamespacedKey getHeadIDStorageType(PersistentDataContainer data){
        if (data.has(headUUID, PersistentDataType.STRING))return headUUID;
        if (data.has(master,PersistentDataType.STRING))return master;
        return null;
    }

    // Decollation -----------------------------------------------------------------------------------------------------
    public static final NamespacedKey decollation = new NamespacedKey(MobHeadsV3.getPlugin(), "decollation");

    // Summon ----------------------------------------------------------------------------------------------------------
    public static NamespacedKey summoner = new NamespacedKey(plugin, "summoner");
    public static NamespacedKey summonLife = new NamespacedKey(plugin, "summonLife");
    public static NamespacedKey summonTarget = new NamespacedKey(plugin, "summonTarget");

    // EntityType Specific ---------------------------------------------------------------------------------------------
    // Chested Animal ------------------------------------------------
    public static String chestedKeyString = "chested";
    // Frog ----------------------------------------------------------
    public static NamespacedKey frogFood = new NamespacedKey(plugin, "frogfood");

}
