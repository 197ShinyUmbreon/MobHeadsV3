package io.github.shinyumbreon197.mobheadsv3.file;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.PlayerHead;
import io.github.shinyumbreon197.mobheadsv3.tool.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerRegistry {

    private static List<String> serializedPlayerRegistry = new ArrayList<>();

    private static final MobHeadsV3 plugin = MobHeadsV3.getPlugin();

    private FileConfiguration playerRegistry = null;

    private File playerRegistryFile = null;

    public void reloadPlayerRegistry() {
        if (playerRegistryFile == null) {
            playerRegistryFile = new File(plugin.getDataFolder(), "player_registry.yml");

        }
        playerRegistry = YamlConfiguration.loadConfiguration(playerRegistryFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(plugin.getResource("player_registry.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            playerRegistry.setDefaults(defConfig);
        }
    }

    public FileConfiguration getPlayerRegistry() {
        if (playerRegistry == null) {
            reloadPlayerRegistry();
        }
        return playerRegistry;
    }

    public void savePlayerRegistry() {
        if (playerRegistry == null || playerRegistryFile == null) {
            return;
        }
        try {
            getPlayerRegistry().save(playerRegistryFile);
        } catch (IOException ex) {
            System.out.println(ex);
            //getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultPlayerRegistry() {
        if (playerRegistryFile == null) {
            playerRegistryFile = new File(plugin.getDataFolder(), "player_registry.yml");
        }
        if (!playerRegistryFile.exists()) {
            plugin.saveResource("player_registry.yml", false);
        }
    }

    public void updateRegistryFromFile(){
        reloadPlayerRegistry();
        serializedPlayerRegistry = getPlayerRegistry().getStringList("playerheads");
    }

    public List<String> getRegistry(){
        return serializedPlayerRegistry;
    }

    public void replaceRegistry(List<String> registryList){
        serializedPlayerRegistry = registryList;
        reloadPlayerRegistry();
        getPlayerRegistry().set("playerheads", registryList);
        savePlayerRegistry();
    }

    public void addToRegistry(String serializedPlayerHead){
        serializedPlayerRegistry.add(serializedPlayerHead);
        replaceRegistry(serializedPlayerRegistry);
    }

    public void removeFromRegistry(MobHead oldHead){
        String oldName = oldHead.getHeadName();
        for (int i = 0; i < serializedPlayerRegistry.size(); i++) {
            String string = serializedPlayerRegistry.get(i);
            MobHead scannedHead = PlayerHead.rebuildPlayerHead(Serializer.deserializeItemStack(string));
            String scannedName = scannedHead.getHeadName();
            if (scannedName.matches(oldName)){
                serializedPlayerRegistry.remove(i);
                if (debug) System.out.println("Found old head, removing..."); //debug
                return;
            }
        }
        MobHeadsV3.cOut("!!! Could not find old head. Possible duplicate listing !!!");
    }

    public void addToRegistry(ItemStack playerHeadItem){
        addToRegistry(Serializer.serializeItemStack(playerHeadItem));
    }

}
