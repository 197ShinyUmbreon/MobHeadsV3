package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private static final FileConfiguration config = MobHeadsV3.getPlugin().getConfig();


    // Major Features --------------------------------------------------------------------------------------------------
    public static final boolean headsDrop = config.getBoolean("heads_drop", true);
    public static final boolean playerHeadsDrop = config.getBoolean("player_heads_drop", true);
    public static final boolean headEffects = config.getBoolean("head_effects", true);

    // Minor Features --------------------------------------------------------------------------------------------------
    public static final boolean headCraftLoot = config.getBoolean("head_craft_loot", true);
    public static final boolean chargedCreeperExplosionsDropHeads = config.getBoolean("charged_creeper_explosions_drop_heads", true);
    public static final boolean headDecollation = config.getBoolean("head_decollation", true);

    // Individual Heads ------------------------------------------------------------------------------------------------
    public static final boolean pig = config.getBoolean("pig", true);
    public static final boolean chicken = config.getBoolean("chicken", true);
    public static final boolean cow = config.getBoolean("cow", true);
    public static final boolean mooshroom = config.getBoolean("mooshroom", true);
    public static final boolean sheep = config.getBoolean("sheep", true);
    public static final boolean wolf = config.getBoolean("wolf", true);
    public static final boolean allay = config.getBoolean("allay", true);
    public static final boolean snowGolem = config.getBoolean("snow_golem", true);
    public static final boolean ironGolem = config.getBoolean("iron_golem", true);
    public static final boolean horse = config.getBoolean("horse", true);
    public static final boolean donkey = config.getBoolean("donkey", true);
    public static final boolean mule = config.getBoolean("mule", true);
    public static final boolean skeletonHorse = config.getBoolean("skeleton_horse", true);
    public static final boolean zombifiedHorse = config.getBoolean("zombified_horse", true);
    public static final boolean llama = config.getBoolean("llama", true);
    public static final boolean traderLlama = config.getBoolean("trader_llama", true);
    public static final boolean bee = config.getBoolean("bee", true);
    public static final boolean bat = config.getBoolean("bat", true);
    public static final boolean rabbit = config.getBoolean("rabbit", true);
    public static final boolean fox = config.getBoolean("fox", true);
    public static final boolean goat = config.getBoolean("goat", true);
    public static final boolean cat = config.getBoolean("cat", true);
    public static final boolean ocelot = config.getBoolean("ocelot", true);
    public static final boolean parrot = config.getBoolean("parrot", true);
    public static final boolean camel = config.getBoolean("camel", true);
    public static final boolean sniffer = config.getBoolean("sniffer", true);
    public static final boolean polarBear = config.getBoolean("polar_bear", true);
    public static final boolean panda = config.getBoolean("panda", true);
    public static final boolean villager = config.getBoolean("villager", true);
    public static final boolean wanderingTrader = config.getBoolean("wandering_trader", true);

    public static final boolean cod = config.getBoolean("cod", true);
    public static final boolean salmon = config.getBoolean("salmon", true);
    public static final boolean pufferfish = config.getBoolean("pufferfish", true);
    public static final boolean tropicalFish = config.getBoolean("tropical_fish", true);
    public static final boolean squid = config.getBoolean("squid", true);
    public static final boolean glowSquid = config.getBoolean("glow_squid", true);
    public static final boolean dolphin = config.getBoolean("dolphin", true);
    public static final boolean turtle = config.getBoolean("turtle", true);
    public static final boolean tadpole = config.getBoolean("tadpole", true);
    public static final boolean frog = config.getBoolean("frog", true);
    public static final boolean axolotl = config.getBoolean("axolotl", true);
    public static final boolean guardian = config.getBoolean("guardian", true);

    public static final boolean creeper = config.getBoolean("creeper", true);
    public static final boolean skeleton = config.getBoolean("skeleton", true);
    public static final boolean stray = config.getBoolean("stray", true);
    public static final boolean zombie = config.getBoolean("zombie", true);
    public static final boolean drowned = config.getBoolean("drowned", true);
    public static final boolean husk = config.getBoolean("husk", true);
    public static final boolean zombieVillager = config.getBoolean("zombie_villager", true);
    public static final boolean spider = config.getBoolean("spider", true);
    public static final boolean caveSpider = config.getBoolean("cave_spider", true);
    public static final boolean slime = config.getBoolean("slime", true);
    public static final boolean phantom = config.getBoolean("phantom", true);

    public static final boolean pillager = config.getBoolean("pillager", true);
    public static final boolean vindicator = config.getBoolean("vindicator", true);
    public static final boolean witch = config.getBoolean("witch", true);
    public static final boolean evoker = config.getBoolean("evoker", true);
    public static final boolean vex = config.getBoolean("vex", true);
    public static final boolean illusioner = config.getBoolean("illusioner", true);
    public static final boolean ravager = config.getBoolean("ravager", true);

    public static final boolean strider = config.getBoolean("strider", true);

    public static final boolean magmaCube = config.getBoolean("magma_cube", true);
    public static final boolean witherSkeleton = config.getBoolean("wither_skeleton", true);
    public static final boolean piglin = config.getBoolean("piglin", true);
    public static final boolean piglinBrute = config.getBoolean("piglin_brute", true);
    public static final boolean zombiePiglin = config.getBoolean("zombie_piglin", true);
    public static final boolean hoglin = config.getBoolean("hoglin", true);
    public static final boolean zoglin = config.getBoolean("zoglin", true);
    public static final boolean blaze = config.getBoolean("blaze", true);
    public static final boolean ghast = config.getBoolean("ghast", true);

    public static final boolean endermite = config.getBoolean("endermite", true);
    public static final boolean enderman = config.getBoolean("enderman", true);
    public static final boolean shulker = config.getBoolean("shulker", true);

    public static final boolean enderDragon = config.getBoolean("ender_dragon", true);
    public static final boolean wither = config.getBoolean("wither", true);
    public static final boolean elderGuardian = config.getBoolean("elder_guardian", true);
    public static final boolean warden = config.getBoolean("warden", true);

}
