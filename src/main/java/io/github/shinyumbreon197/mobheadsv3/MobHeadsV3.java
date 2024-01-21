package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.command.OpenHeadSpawnGUI;
import io.github.shinyumbreon197.mobheadsv3.command.SpawnHeadedEntity;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.event.*;
import io.github.shinyumbreon197.mobheadsv3.event.main.MainThread;
import io.github.shinyumbreon197.mobheadsv3.event.world.Furnace;
import io.github.shinyumbreon197.mobheadsv3.file.PlayerRegistry;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import io.github.shinyumbreon197.mobheadsv3.tool.StringBuilder;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MobHeadsV3 extends JavaPlugin {

    public static boolean debug = false;

    private static String version;
    public static String getVersion(){return version;}
    private static MobHeadsV3 plugin;
    public static MobHeadsV3 getPlugin(){return plugin;}
    public static PlayerRegistry playerRegistry;
    private static final String pluginName = "[MobHeadsV3] ";
    public static String getPluginName(){return pluginName;}
    public static String getPluginNameColored(){return ChatColor.YELLOW+pluginName+ChatColor.RESET+"";}
    public static boolean protocolLibEnabled = false;

    @Override
    public void onEnable() {
        plugin = this;
        version = defineVersion();
        if (debug) cOut("Server Version: "+getVersion());
        initConfig();
        playerRegistry = new PlayerRegistry();
        playerRegistry.saveDefaultPlayerRegistry();
        registerCommands();
        registerEvents();
        MobHead.initialize();
        registerRecipes();

        initPtcLib();

        Summon.startSummonThread();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, MainThread::on5Ticks,0, 5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initConfig(){
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void initPtcLib(){
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null){
            protocolLibEnabled = true;
            cOut("ProtocolLib Found!");
            Packets.initialize();
        }else{
            System.out.println("\n"+
                    "/////////////////////////////////////\n"+
                    "ProtocolLib is not installed.\n" +
                    "Some functionality will be missing.\n"+
                    "/////////////////////////////////////\n"
            );
        }
    }

    private void registerCommands(){
        getCommand("mobheads").setExecutor(new OpenHeadSpawnGUI());
        if (debug) getCommand("summonheaded").setExecutor(new SpawnHeadedEntity());
        //if (debug) getCommand("center").setExecutor(new TestCommands());
    }

    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new MobHeadGUI(),this);
        pm.registerEvents(new Decollation(),this);
        //pm.registerEvents(new Summon(), this);

        pm.registerEvents(new MobHead(),this);
        pm.registerEvents(new PlayerJoinServer(),this);
        pm.registerEvents(new EntityDeath(),this);
        pm.registerEvents(new EntityDamage(),this);
        pm.registerEvents(new EntityTargetLivingEntity(),this);
        pm.registerEvents(new PlayerHungerSaturation(),this);
        pm.registerEvents(new PlayerInteractEvents(),this);
        pm.registerEvents(new BlockPlaceAndBreak(),this);
        pm.registerEvents(new ItemSpawn(),this);
        pm.registerEvents(new InventoryEvents(),this);
        pm.registerEvents(new ProjectileLand(),this);
        pm.registerEvents(new IncrementStatistic(),this);
        pm.registerEvents(new PlayerMove(),this);
        pm.registerEvents(new PlayerToggleSneak(),this);
        pm.registerEvents(new PickUpItem(),this);
        pm.registerEvents(new PrepareCraft(),this);
        pm.registerEvents(new ToggleGliding(),this);
        pm.registerEvents(new ChunkUnload(),this);
        pm.registerEvents(new Furnace(),this);
        pm.registerEvents(new PlayerFish(), this);

        //pm.registerEvents(new Packets(), this);
    }

    private String defineVersion() {
        String version = this.getServer().getVersion();
        List<Character> chars = new ArrayList<>();
        boolean hit = false;
        for (char c : version.toCharArray()) {
            if (c == '(') {
                hit = true;
                continue;
            } else if (c == ')') continue;
            if (hit) {
                List<Character> ex = List.of('M', 'C', ':', ' ');
                if (!ex.contains(c)) chars.add(c);
            }
        }
        char[] charArray = new char[chars.size()];
        for (int i = 0; i < chars.size(); i++) {
            charArray[i] = chars.get(i);
        }
        return String.copyValueOf(charArray);
    }

    private void registerRecipes(){
        Decollation.registerSmithingRecipes();
        for (MobHead mobHead: MobHead.getMobHeads()){
            ItemStack lootItem = mobHead.getHeadLootItemStack();
            if (lootItem == null) continue;
            ItemStack headItem = mobHead.getHeadItemStack();
            RecipeChoice.ExactChoice rc = new RecipeChoice.ExactChoice(headItem);
            String name = mobHead.getHeadName();
            if (name == null) name = headItem.getType().name();
            NamespacedKey nsk = new NamespacedKey(MobHeadsV3.getPlugin(), StringBuilder.toSimplifiedString(name));
            ShapedRecipe recipe = new ShapedRecipe(nsk, lootItem);
            recipe.shape("H");
            recipe.setIngredient('H', rc);
            //if (debug) System.out.println("registerRecipes() " + name); //debug
            getServer().addRecipe(recipe);
        }
    }

    public static String namePlain(String output){
        return getPluginName()+output;
    }

    public static String nameColored(String output){
        return ChatColor.YELLOW+getPluginName()+ChatColor.RESET+""+output;
    }

    public static void messagePlayer(Player player, String message){
        player.sendMessage(ChatColor.YELLOW + getPluginName() + "" + ChatColor.RESET + message);
    }

    public static void cOut(String message){
        System.out.println(pluginName + message);
    }

}
