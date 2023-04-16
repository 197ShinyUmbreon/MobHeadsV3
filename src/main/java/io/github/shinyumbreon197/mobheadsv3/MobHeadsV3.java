package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.command.openHeadSpawnGUI;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import io.github.shinyumbreon197.mobheadsv3.command.SpawnHeadedEntity;
import io.github.shinyumbreon197.mobheadsv3.event.*;
import io.github.shinyumbreon197.mobheadsv3.file.PlayerRegistry;
import io.github.shinyumbreon197.mobheadsv3.head.*;
import io.github.shinyumbreon197.mobheadsv3.head.hostile.*;
import io.github.shinyumbreon197.mobheadsv3.head.hostile.ElderGuardianHead;
import io.github.shinyumbreon197.mobheadsv3.head.hostile.WardenHead;
import io.github.shinyumbreon197.mobheadsv3.head.hostile.WitherHead;
import io.github.shinyumbreon197.mobheadsv3.head.passive.*;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.*;
import io.github.shinyumbreon197.mobheadsv3.head.vanilla.*;
import io.github.shinyumbreon197.mobheadsv3.tool.StringBuilder;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobHeadsV3 extends JavaPlugin {

    private static MobHeadsV3 plugin;
    public static MobHeadsV3 getPlugin(){return plugin;}

    public static PlayerRegistry playerRegistry;
    private static NamespacedKey pluginNSK;
    public static NamespacedKey getPluginNSK(){return pluginNSK;}
    private static final String pluginName = "[MobHeadsV3] ";
    public static String getPluginName(){return pluginName;}
    public static String getPluginNameColored(){return ChatColor.YELLOW+pluginName+ChatColor.RESET+"";}

    @Override
    public void onEnable() {
        plugin = this;
        pluginNSK = new NamespacedKey(plugin,"mobheadsv3");
        playerRegistry = new PlayerRegistry();
        playerRegistry.saveDefaultPlayerRegistry();
        registerCommands();
        registerEvents();
        initializeHeads();
        HeadData.initialize();
        registerRecipes();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, ScheduledEvents::run10TickEvents,0, 10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands(){
        getCommand("mobheads").setExecutor(new openHeadSpawnGUI());
        getCommand("summonheaded").setExecutor(new SpawnHeadedEntity());
    }

    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvents(new TestEvents(), this); //debug
        pm.registerEvents(new MobHeadGUI(), this);
        pm.registerEvents(new PlayerJoinLeaveEvents(), this);
        pm.registerEvents(new PlaceAndBreakHeadEvents(), this);
        pm.registerEvents(new InteractWithHeadEvents(), this);
        pm.registerEvents(new MobHeadDropEvents(), this);
        pm.registerEvents(new DecollationSmith(), this);
        pm.registerEvents(new AttackDamageDeathEvents(), this);
        pm.registerEvents(new HungerChangeEvent(), this);
    }

    private void initializeHeads(){
        //Player Heads -------------------------------------------------------------------------------------------------
        PlayerHead.registerPlayerHistory();
        PlayerHead.registerOnlinePlayers();
        //Vanilla Mob Heads --------------------------------------------------------------------------------------------
        ZombieHead.initialize();
        SkeletonHead.initialize();
        CreeperHead.initialize();
        WitherSkeletonHead.initialize();
        EnderDragonHead.initialize();
        //Single-skin Passives -----------------------------------------------------------------------------------------
        CowHead.initialize();
        PigHead.initialize();
        ChickenHead.initialize();
        WolfHead.initialize();
        DonkeyHead.initialize();
        MuleHead.initialize();
        DolphinHead.initialize();
        CodHead.initialize();
        SalmonHead.initialize();
        PufferfishHead.initialize();
        TropicalFishHead.initialize();
        TurtleHead.initialize();
        StriderHead.initialize();
        GoatHead.initialize();
        SquidHead.initialize();
        BeeHead.initialize();
        BatHead.initialize();
        OcelotHead.initialize();
        SnowmanHead.initialize();
        PolarBearHead.initialize();
        SkeletonHorseHead.initialize();
        ZombieHorseHead.initialize();
        WanderingTraderHead.initialize();
        IronGolemHead.initialize();
        GlowSquidHead.initialize();
        AllayHead.initialize();
        TadpoleHead.initialize();
        //Single-skin Hostiles -----------------------------------------------------------------------------------------
        SilverfishHead.initialize();
        StrayHead.initialize();
        ShulkerHead.initialize();
        PhantomHead.initialize();
        HuskHead.initialize();
        DrownedHead.initialize();
        HoglinHead.initialize();
        ZoglinHead.initialize();
        PiglinHead.initialize();
        PiglinBruteHead.initialize();
        WitchHead.initialize();
        GuardianHead.initialize();
        RavagerHead.initialize();
        VexHead.initialize();
        EvokerHead.initialize();
        SpiderHead.initialize();
        EndermanHead.initialize();
        GhastHead.initialize();
        BlazeHead.initialize();
        CaveSpiderHead.initialize();
        MagmaCubeHead.initialize();
        ZombifiedPiglinHead.initialize();
        SlimeHead.initialize();
        EndermiteHead.initialize();
        PillagerHead.initialize();
        VindicatorHead.initialize();
        IllusionerHead.initialize();
        //Boss Mobs ----------------------------------------------------------------------------------------------------
        ElderGuardianHead.initialize();
        WitherHead.initialize();
        WardenHead.initialize();
        //Multi-skin Passives ------------------------------------------------------------------------------------------
        CatHead.initialize();
        HorseHead.initialize();
        LlamaHead.initialize();
        TraderLlamaHead.initialize();
        ParrotHead.initialize();

        FoxHead.initialize();
        PandaHead.initialize();

        //Multi-skin Hostiles ------------------------------------------------------------------------------------------

        

    }

    private void registerRecipes(){
        DecollationSmith.registerSmithingRecipes();
        for (MobHead mobHead:HeadData.getMobHeads()){
            ItemStack lootItem = mobHead.getLootItem();
            if (lootItem == null) continue;
            ItemStack headItem = mobHead.getHeadItem();
            RecipeChoice.ExactChoice rc = new RecipeChoice.ExactChoice(headItem);
            String name = mobHead.getName();
            if (name == null) name = headItem.getType().name();
            NamespacedKey nsk = new NamespacedKey(MobHeadsV3.getPlugin(), StringBuilder.toSimplifiedString(name));
            ShapedRecipe recipe = new ShapedRecipe(nsk, lootItem);
            recipe.shape("H");
            recipe.setIngredient('H', rc);
            getServer().addRecipe(recipe);
        }
    }

    public static String namePlain(String output){
        return getPluginName()+output;
    }

    public static String nameColored(String output){
        return ChatColor.YELLOW+getPluginName()+ChatColor.RESET+""+output;
    }

}
