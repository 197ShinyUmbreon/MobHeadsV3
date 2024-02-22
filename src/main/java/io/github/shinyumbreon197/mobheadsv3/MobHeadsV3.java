package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.command.OpenHeadSpawnGUI;
import io.github.shinyumbreon197.mobheadsv3.command.SpawnHeadedEntity;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.event.*;
import io.github.shinyumbreon197.mobheadsv3.event.main.MainThread;
import io.github.shinyumbreon197.mobheadsv3.event.world.Furnace;
import io.github.shinyumbreon197.mobheadsv3.file.PlayerRegistry;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import io.github.shinyumbreon197.mobheadsv3.tool.StringBuilder;
import org.bukkit.Bukkit;
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
        resumeServices();
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
            cOut("/////////////////////////////////////");
            cOut("ProtocolLib is not installed.");
            cOut("Some functionality will be missing.");
            cOut("/////////////////////////////////////");
        }
    }

    private void registerCommands(){
        getCommand("mobheads").setExecutor(new OpenHeadSpawnGUI());
        getCommand("summonheaded").setExecutor(new SpawnHeadedEntity());
        //if (debug) getCommand("center").setExecutor(new TestCommands());
    }

    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();

        //pm.registerEvents(new Summon(), this);

        pm.registerEvents(new MobHeadGUI(),this);
        pm.registerEvents(new Decollation(),this);
        pm.registerEvents(new MobHead(),this);
        pm.registerEvents(new PlayerJoinServer(),this);
        pm.registerEvents(new EntityDeath(),this);
        pm.registerEvents(new EntityDamage(),this);
        if (Config.headEffects) pm.registerEvents(new EntityTargetLivingEntity(),this);
        if (Config.headEffects) pm.registerEvents(new PlayerFoodHungerSaturation(),this);
        pm.registerEvents(new PlayerInteractEvents(),this);
        pm.registerEvents(new BlockPlaceAndBreak(),this);
        pm.registerEvents(new ItemSpawnDespawnEvents(),this);
        pm.registerEvents(new InventoryEvents(),this);
        if (Config.headEffects) pm.registerEvents(new ProjectileLand(),this);
        if (Config.headEffects) pm.registerEvents(new IncrementStatistic(),this);
        if (Config.headEffects) pm.registerEvents(new PlayerMove(),this);
        if (Config.headEffects) pm.registerEvents(new PlayerToggleSneak(),this);
        if (Config.headEffects) pm.registerEvents(new PickUpItem(),this);
        if (Config.headCraftLoot) pm.registerEvents(new PrepareCraft(),this);
        if (Config.headEffects) pm.registerEvents(new ToggleGliding(),this);
        pm.registerEvents(new ChunkUnload(),this);
        if (Config.headEffects) pm.registerEvents(new Furnace(),this);
        if (Config.headEffects) pm.registerEvents(new PlayerFish(), this);
        if (Config.headEffects) pm.registerEvents(new PlayerItemConsume(), this);
        if (Config.headEffects) pm.registerEvents(new PlayerTeleport(), this);

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
        if (Config.headDecollation) Decollation.registerSmithingRecipes();
        if (!Config.headCraftLoot)return;
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
    private static void resumeServices(){
        for (Player player:Bukkit.getOnlinePlayers()){
            CreatureEvents.chestedAddHolder(player);
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
