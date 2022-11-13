package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.event.InteractWithHeadEvents;
import io.github.shinyumbreon197.mobheadsv3.event.MobHeadDropEvents;
import io.github.shinyumbreon197.mobheadsv3.event.PlaceAndBreakHeadEvents;
import io.github.shinyumbreon197.mobheadsv3.event.TestEvents;
import io.github.shinyumbreon197.mobheadsv3.head.EnderDragonHead;
import io.github.shinyumbreon197.mobheadsv3.head.FoxHead;
import io.github.shinyumbreon197.mobheadsv3.head.CowHead;
import io.github.shinyumbreon197.mobheadsv3.head.ZombieHead;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;

public final class MobHeadsV3 extends JavaPlugin {

    private static MobHeadsV3 plugin;
    public static MobHeadsV3 getPlugin(){return plugin;}
    private static NamespacedKey pluginNSK;
    public static NamespacedKey getPluginNSK(){return pluginNSK;}

    @Override
    public void onEnable() {
        plugin = this;
        pluginNSK = new NamespacedKey(plugin,"mobheadsv3");
        registerEvents();
        registerRecipes();
        try {
            initializeHeads();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new TestEvents(), this);
        pm.registerEvents(new PlaceAndBreakHeadEvents(), this);
        pm.registerEvents(new InteractWithHeadEvents(), this);
        pm.registerEvents(new MobHeadDropEvents(), this);
        pm.registerEvents(new DecollationSmith(), this);
    }

    private void initializeHeads() throws MalformedURLException {
        CowHead.initialize();
        FoxHead.initialize();
        EnderDragonHead.initialize();
        ZombieHead.initialize();
    }

    private void registerRecipes(){
        DecollationSmith.registerSmithingRecipes();
    }

}
