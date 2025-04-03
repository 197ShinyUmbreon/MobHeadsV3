package io.github.shinyumbreon197.mobheadsv3.data;


import io.github.shinyumbreon197.mobheadsv3.Config;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.itemStack.HeadItemStack;
import io.github.shinyumbreon197.mobheadsv3.itemStack.HeadLootItemStack;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HeadData {

    private static final boolean writeLore = Config.headEffects;

    public static List<MobHead> getAllHeads(){
        return allHeads();
    }
    private static List<MobHead> allHeads(){
        List<MobHead> list = new ArrayList<>();
        // Overworld ----------------
        // Passive
        list.addAll(Pigs());
        list.addAll(Chickens());
        list.addAll(Cows());
        list.addAll(Mooshrooms());
        list.addAll(Sheepies());
        list.addAll(Wolves());
        list.add(Allay());
        list.add(Snowman());
        list.add(IronGolem());
        list.addAll(Horses());
        list.add(Donkey());
        list.add(Mule());
        list.add(SkeletonHorse());
        list.add(ZombieHorse());
        list.addAll(Llamas());
        list.addAll(TraderLlamas());
        list.add(Bee());
        list.add(Armadillo());
        list.add(Bat());
        list.addAll(Rabbits());
        list.addAll(Foxes());
        list.add(Goat());
        list.addAll(Cats());
        list.add(Ocelot());
        list.addAll(Parrots());
        list.add(Camel());
        list.add(Sniffer());
        list.add(PolarBear());
        list.addAll(Pandas());
        list.addAll(Villagers());
        list.add(WanderingTrader());

        // Aquatic
        list.add(Cod());
        list.add(Salmon());
        list.add(Pufferfish());
        list.add(TropicalFish());
        list.add(Squid());
        list.add(GlowSquid());
        list.add(Dolphin());
        list.add(Turtle());
        list.add(Tadpole());
        list.addAll(Frogs());
        list.addAll(Axolotls());
        list.add(Guardian());

        // Hostile
        list.add(Creeper());
        list.add(Skeleton());
        list.add(Stray());
        list.add(Bogged());
        list.add(Zombie());
        list.add(Drowned());
        list.add(Husk());
        list.addAll(ZombieVillagers());
        list.add(Spider());
        list.add(CaveSpider());
        list.add(Breeze());
        list.add(Silverfish());
        list.add(Slime());
        list.add(Phantom());
        list.add(Creaking());

        // Pillagers
        list.add(Pillager());
        list.add(Vindicator());
        list.add(Witch());
        list.add(Evoker());
        list.add(Vex());
        list.add(Illusioner());
        list.add(Ravager());

        // Nether ---------------
        // Passive
        list.add(Strider());

        // Hostile
        list.add(MagmaCube());
        list.add(WitherSkeleton());
        list.add(Piglin());
        list.add(PiglinBrute());
        list.add(ZombifiedPiglin());
        list.add(Hoglin());
        list.add(Zoglin());
        list.add(Blaze());
        list.add(Ghast());

        // The End ----------------
        list.add(Endermite());
        list.add(Enderman());
        list.add(Shulker());

        // Bosses -----------------
        list.add(EnderDragon());
        list.add(Wither());
        list.add(ElderGuardian());
        list.add(Warden());

        return list;
    }


    // Vanilla ---------------------------------------------------------------------------------------------------------
    public static MobHead Creeper(){
        UUID uuid = UUID.fromString("766ca6e4-5fe3-11ed-9b6a-0242ac120002");
        EntityType entityType = EntityType.CREEPER;
        List<String> lore = List.of(
                "Right-Click while holding gunpowder", "to create an explosion that", "damages and sends nearby",
                "creatures flying!"
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.BLAST_PROTECTION, 4);
        ItemStack head = HeadItemStack.customVanillaHead(entityType, lore, uuid, enchants);
        return new MobHead(
                uuid,
                null,
                entityType,
                head,
                new ItemStack(Material.TNT, 8),
                lore,
                null,
                enchants
        );
    }
    public static MobHead EnderDragon(){
        UUID uuid = UUID.fromString("766caacc-5fe3-11ed-9b6a-0242ac120002");
        EntityType entityType = EntityType.ENDER_DRAGON;
        List<String> lore = List.of(
                "Enhanced Elytra Flight.", "Sneak-Jump while grounded to", "get a boost into the air.",
                "Hold Sneak while gliding to", "get a boost of speed.",
                "Right-Click with a Bottle of Dragon's Breath", "to unleash several damaging bursts",
                "of breath in front of you.", "Sneak-Right-Click with an empty bottle",
                "to fill it with Dragon's Breath."
        );
        ItemStack head = HeadItemStack.customVanillaHead(entityType, lore, uuid);
        return new MobHead(
                uuid,
                null,
                entityType,
                head,
                new ItemStack(Material.END_CRYSTAL, 4),
                lore
        );
    }
    public static MobHead Skeleton(){
        UUID uuid = UUID.fromString("766ca39c-5fe3-11ed-9b6a-0242ac120002");
        EntityType entityType = EntityType.SKELETON;
        List<String> lore = List.of(
                "Drink Milk to gain Regeneration I,", "Speed I, & Resistance I", "for 5 minutes."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.PROJECTILE_PROTECTION, 3);
        ItemStack head = HeadItemStack.customVanillaHead(entityType, lore, uuid,enchants);
        return new MobHead(
                uuid,
                null,
                entityType,
                head,
                new ItemStack(Material.BONE, 16),
                lore,
                null,
                enchants
        );
    }
    public static MobHead WitherSkeleton(){
        UUID uuid = UUID.fromString("766ca90a-5fe3-11ed-9b6a-0242ac120002");
        EntityType entityType = EntityType.WITHER_SKELETON;
        List<String> lore = List.of(
                "Gain Wither I.", "Immune to Wither damage.", "Afflict Wither II for", "4 seconds on melee attack.",
                "Half-Damage from all fire damage sources.", "Gain Fire Resistance I for", "5 seconds upon taking fire damage.",
                "Drink Milk to gain Regeneration I,", "Speed I, & Resistance I", "for 5 minutes."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customVanillaHead(entityType, lore, uuid, enchants);
        return new MobHead(
                uuid,
                null,
                entityType,
                head,
                new ItemStack(Material.WITHER_ROSE, 1),
                lore,
                null,
                enchants
        );
    }
    public static MobHead Zombie(){
        UUID uuid = UUID.fromString("766c9f82-5fe3-11ed-9b6a-0242ac120002");
        EntityType entityType = EntityType.ZOMBIE;
        List<String> lore = List.of(
                "Gain Slowness I and Hunger I.", "Your hunger will never", "drop below 19/20."
        );
        ItemStack head = HeadItemStack.customVanillaHead(entityType, lore, uuid);
        return new MobHead(
                uuid,
                null,
                entityType,
                head,
                new ItemStack(Material.ROTTEN_FLESH, 16),
                lore
        );
    }
    public static MobHead Piglin(){
        UUID uuid = UUID.fromString("19413908-5fac-11ed-9b6a-0242ac120002");
        List<String> lore = List.of(
                ChatColor.RED + "[NYI]"
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customVanillaHead(EntityType.PIGLIN,lore,uuid,enchants);
        return new MobHead(uuid, null, EntityType.PIGLIN, head, new ItemStack(Material.GILDED_BLACKSTONE, 4), lore,null,enchants);
    }

    // Single-Skin Passives --------------------------------------------------------------------------------------------
    public static MobHead Allay(){
        UUID uuid = UUID.fromString("194124d6-5fac-11ed-9b6a-0242ac120002");
        String name = "Allay Head";
        EntityType entityType = EntityType.ALLAY;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/e1c59dccde4b8535500dcf6794ca450663f607290e2510f6d8eb1e5eb71da5af");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Pulls items of the same type", "and data as you are holding", "towards you over a wide distance."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.MUSIC_DISC_11, 1), lore, noteblockSound);
    }
    public static MobHead Armadillo(){
        UUID uuid = UUID.fromString("254451d6-031f-45b3-b6fa-cc262782d127");
        String name = "Compact Armadillo";
        EntityType entityType = EntityType.ARMADILLO;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/9164ed0e0ef69b0ce7815e4300b4413a4828fcb0092918543545a418a48e0c3c");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Haste I.", "When hit, Gain Resistance II,", "Slowness I and 100% Knockback",
                "Resistance for 10 seconds", "once every 20 seconds."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, EntityType.ARMADILLO, head, new ItemStack(Material.ARMADILLO_SCUTE, 8), lore, null, enchants, noteblockSound);
    }
    public static MobHead Bat(){
        UUID uuid = UUID.fromString("1941166c-5fac-11ed-9b6a-0242ac120002");
        String name = "Bat Head";
        EntityType entityType = EntityType.BAT;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/6de75a2cc1c950e82f62abe20d42754379dfad6f5ff546e58f1c09061862bb92");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Blindness I, Speed I.", "& Night Vision I.", "See Creatures and Items through", "walls for a moderate distance."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.DEEPSLATE_COAL_ORE, 6), lore, noteblockSound);
    }
    public static MobHead Bee(){
        UUID uuid = UUID.fromString("19411540-5fac-11ed-9b6a-0242ac120002");
        String name = "Bee Head";
        EntityType entityType = EntityType.BEE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/59ac16f296b461d05ea0785d477033e527358b4f30c266aa02f020157ffca736");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Afflict Poison I for 5 seconds", "on melee attack.",
                "Summon a Bee to fight for", "you when attacked.", "Summon Cooldown: 2 Seconds.",
                "Summon Max Lifetime: 30 Seconds.", "Summon Affliction: Poison I, 5 seconds."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.HONEYCOMB, 4), lore, noteblockSound);
    }
    public static MobHead Cod(){
        UUID uuid = UUID.fromString("19410a82-5fac-11ed-9b6a-0242ac120002");
        String name = "Bloated Cod";
        EntityType entityType = EntityType.COD;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I when wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore, enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.BONE_MEAL, 16), lore, null, enchants, noteblockSound);
    }
    public static MobHead Dolphin(){
        UUID uuid = UUID.fromString("19410942-5fac-11ed-9b6a-0242ac120002");
        String name = "Dolphin Head";
        EntityType entityType = EntityType.DOLPHIN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/8e9688b950d880b55b7aa2cfcd76e5a0fa94aac6d16f78e833f7443ea29fed3");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of("Gain Dolphin's Grace I");
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 2);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.COD, 4), lore,null, enchants, noteblockSound);
    }
    public static MobHead Donkey(){
        UUID uuid = UUID.fromString("19410442-5fac-11ed-9b6a-0242ac120002");
        String name = "Donkey Head";
        EntityType entityType = EntityType.DONKEY;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/dfb6c3c052cf787d236a2915f8072b77c547497715d1d2f8cbc9d241d88a");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I.", "Step-Up One-Block high.", "Charge a Jump by Sneaking.",
                "Storage Containers can be picked up", "with Shift-Right-Click, imposing",
                "a level of Slowness for each container."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.LEATHER, 4), lore, noteblockSound);
    }
    public static MobHead GlowSquid(){
        UUID uuid = UUID.fromString("194123be-5fac-11ed-9b6a-0242ac120002");
        String name = "Glow Squid Head";
        EntityType entityType = EntityType.GLOW_SQUID;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/2ecd0b5eb6b384db076d8446065202959dddff0161e0d723b3df0cc586d16bbd");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I & Night Vision I when wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.GLOW_INK_SAC, 8), lore,null,enchants,noteblockSound);
    }
    public static MobHead Goat(){
        UUID uuid = UUID.fromString("19411176-5fac-11ed-9b6a-0242ac120002");
        String name = "Goat Head";
        EntityType entityType = EntityType.GOAT;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/f03330398a0d833f53ae8c9a1cb393c74e9d31e18885870e86a2133d44f0c63c");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Sneak-Jump to ram and damage", "the blocks and creatures", "in front of you.", "Enhanced by Elytra."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.MUTTON, 6), lore, noteblockSound);
    }
    public static MobHead IronGolem(){
        UUID uuid = UUID.fromString("19412292-5fac-11ed-9b6a-0242ac120002");
        String name = "Iron Golem Head";
        EntityType entityType = EntityType.IRON_GOLEM;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/fcecba31f26919d92a3d6420cd2fa9112f8e108ac04e3fc71da7329cd10fe5ca");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Resistance II and Slowness II.", "Defeating a Monster nearby a Villager",
                "grants Hero of the Village for 2 minutes."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.POPPY, 1), lore, noteblockSound);
    }
    public static MobHead Mule(){
        UUID uuid = UUID.fromString("19410596-5fac-11ed-9b6a-0242ac120002");
        String name = "Mule Head";
        EntityType entityType = EntityType.MULE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/46dcda265e57e4f51b145aacbf5b59bdc6099ffd3cce0a661b2c0065d80930d8");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed II.", "Step-Up One-Block high.", "Charge a Jump by Sneaking.",
                "Storage Containers can be picked up", "with Shift-Right-Click, imposing",
                "a level of Slowness for each container."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.LEATHER, 4), lore, noteblockSound);
    }
    public static MobHead Ocelot(){
        UUID uuid = UUID.fromString("194117a2-5fac-11ed-9b6a-0242ac120002");
        String name = "Ocelot Head";
        EntityType entityType = EntityType.OCELOT;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/51f07e3f2e5f256bfade666a8de1b5d30252c95e98f8a8ecc6e3c7b7f67095");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of("Gain Speed I", "Gain Jump V when crouched.", "80% Fall damage reduction.");
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.COCOA_BEANS, 6), lore, noteblockSound);
    }
    public static MobHead PolarBear(){
        UUID uuid = UUID.fromString("19411b26-5fac-11ed-9b6a-0242ac120002");
        String name = "Polar Bear Head";
        EntityType entityType = EntityType.POLAR_BEAR;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Strength I.", "When in cold biomes or on Ice or Snow", "gain Speed I. [NYI]."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.RESPIRATION, 2);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.COD, 1), lore, null, enchants, noteblockSound);
    }
    public static MobHead Pufferfish(){
        UUID uuid = UUID.fromString("19410cd0-5fac-11ed-9b6a-0242ac120002");
        String name = "Bloated Pufferfish";
        EntityType entityType = EntityType.PUFFERFISH;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I when wet.", "Attackers are afflicted with", "Poison I for 3 seconds."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = HeadLootItemStack.pufferfishLoot();
        return new MobHead(uuid, name, entityType, head, loot, lore,null,enchants, noteblockSound);
    }
    public static MobHead Salmon(){
        UUID uuid = UUID.fromString("19410bae-5fac-11ed-9b6a-0242ac120002");
        String name = "Bloated Salmon";
        EntityType entityType = EntityType.SALMON;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/8aeb21a25e46806ce8537fbd6668281cf176ceafe95af90e94a5fd84924878");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I when wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.BONE_MEAL, 16), lore,null,enchants, noteblockSound);
    }
    public static MobHead SkeletonHorse(){
        UUID uuid = UUID.fromString("19411efa-5fac-11ed-9b6a-0242ac120002");
        String name = "Skeleton Horse Head";
        EntityType entityType = EntityType.SKELETON_HORSE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed II.", "Step-Up One-Block high.",
                "Drink Milk to gain", "Regeneration I & Resistance I", "for 5 minutes."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.BONE_BLOCK, 16), lore, null,enchants,noteblockSound);
    }
    public static MobHead Snowman(){
        UUID uuid = UUID.fromString("194118ce-5fac-11ed-9b6a-0242ac120002");
        String name = "Snowman Head";
        EntityType entityType = EntityType.SNOW_GOLEM;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/1fdfd1f7538c040258be7a91446da89ed845cc5ef728eb5e690543378fcf4");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Regeneration II in the snow, Wither I when wet,",
                "Speed I in cold biomes, and Slow I in hot biomes.",
                "Snowballs inflict 2 damage and", "3 seconds of Slowness I & Freezing",
                "Harvest Snowballs from your body by", "Sneak-Right-Clicking with a Shovel.",
                "A Snow Golem is summoned when damaged by an enemy.",
                "Summon Cooldown: 5 Seconds.", "Summon Max Lifetime: 30 Seconds.", "Summon Affliction: Slow I & Freezing, 3 seconds."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.SNOW_BLOCK, 3), lore, noteblockSound);
    }
    public static MobHead Squid(){
        UUID uuid = UUID.fromString("1941140a-5fac-11ed-9b6a-0242ac120002");
        String name = "Squid Head";
        EntityType entityType = EntityType.SQUID;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I when wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.INK_SAC, 8), lore,null,enchants,noteblockSound);
    }
    public static MobHead Strider(){
        UUID uuid = UUID.fromString("19411054-5fac-11ed-9b6a-0242ac120002");
        String name = "Strider Head";
        EntityType entityType = EntityType.STRIDER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/e245e4760abf10f2900626914cf42f80440cd53099ae5529534f59824067dad6");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Half-Damage from all fire damage sources.", "Gain Fire Resistance I for", "5 seconds upon taking fire damage.",
                "Traverse across lava!", "Hold Sneak to descend into lava."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.LAVA_BUCKET, 1), lore,null,enchants, noteblockSound);
    }
    public static MobHead Tadpole(){
        UUID uuid = UUID.fromString("19412602-5fac-11ed-9b6a-0242ac120002");
        String name = "Bloated Tadpole";
        EntityType entityType = EntityType.TADPOLE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/6cc9b9740bd3adeba52e0ce0a77b3dfdef8d3a40555a4e8bb67d200cd62770d0");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I when wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.FROGSPAWN, 1), lore, null,enchants, noteblockSound);
    }
    public static MobHead TropicalFish(){
        UUID uuid = UUID.fromString("19410e06-5fac-11ed-9b6a-0242ac120002");
        String name = "Bloated Tropical Fish";
        EntityType entityType = EntityType.TROPICAL_FISH;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/12510b301b088638ec5c8747e2d754418cb747a5ce7022c9c712ecbdc5f6f065");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Speed I when wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.BONE_MEAL, 16), lore,null,enchants, noteblockSound);
    }
    public static MobHead Turtle(){
        UUID uuid = UUID.fromString("19410f28-5fac-11ed-9b6a-0242ac120002");
        String name = "Turtle Head";
        EntityType entityType = EntityType.TURTLE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Resistance I.", "Gain Slowness I when not wet."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.SEAGRASS, 4), lore,null,enchants, noteblockSound);
    }
    public static MobHead WanderingTrader(){
        UUID uuid = UUID.fromString("19412170-5fac-11ed-9b6a-0242ac120002");
        String name = "Wandering Trader Head";
        EntityType entityType = EntityType.WANDERING_TRADER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of();
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.DEEPSLATE_EMERALD_ORE, 4), lore, noteblockSound);
    }
    public static MobHead ZombieHorse(){
        UUID uuid = UUID.fromString("1941204e-5fac-11ed-9b6a-0242ac120002");
        String name = "Zombie Horse Head";
        EntityType entityType = EntityType.ZOMBIE_HORSE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/d22950f2d3efddb18de86f8f55ac518dce73f12a6e0f8636d551d8eb480ceec");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Hunger I & Speed I", "Step-Up One-Block high.", "Your hunger will never", "drop below 19/20."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.ROTTEN_FLESH, 32), lore, noteblockSound);
    }
    public static MobHead Sniffer(){
        UUID uuid = UUID.fromString("2dacfa00-20f2-11ee-be56-0242ac120002");
        String name = "Sniffer Head";
        EntityType entityType = EntityType.SNIFFER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Sniff out Suspicious blocks, Spawners", "and unopened loot Chests."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.TORCHFLOWER_SEEDS, 4), lore, noteblockSound);
    }
    public static MobHead Camel(){
        UUID uuid = UUID.fromString("2dacfe06-20f2-11ee-be56-0242ac120002");
        String name = "Camel Head";
        EntityType entityType = EntityType.CAMEL;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/ba4c95bfa0b61722255389141b505cf1a38bad9b0ef543de619f0cc9221ed974");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Sneak-Jump to Dash.", "Gain Speed I when on a sandy block."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.SAND, 16), lore, noteblockSound);
    }

    // Boss Monsters ---------------------------------------------------------------------------------------------------
    public static MobHead ElderGuardian(){
        UUID uuid = UUID.fromString("19415370-5fac-11ed-9b6a-0242ac120002");
        String name = "Elder Guardian Head";
        EntityType entityType = EntityType.ELDER_GUARDIAN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/4adc4a6f53afa116027b51d6f2e433ee7afa5d59b2ffa04780be464fa5d61a");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Inflict Mining Fatigue IV for 10 seconds.",
                "Lock-On to a target by looking at them and Sneaking.", "Beam inflicts 7 Hearts of damage after 3 seconds.",
                "Sneak again to release your Lock-On.", "Breaking Line of Sight will release your Lock-On."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3,Enchantment.THORNS,3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.WET_SPONGE, 1), lore,null,enchants, noteblockSound);
    }
    public static MobHead Warden(){
        UUID uuid = UUID.fromString("19415668-5fac-11ed-9b6a-0242ac120002");
        String name = "Warden Head";
        EntityType entityType = EntityType.WARDEN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/6cf3674b2ddc0ef7c39e3b9c6b58677de5cf377d2eb073f2f3fe50919b1ca4c9");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Darkness I & Night Vision I.", "See Creatures and Items through", "walls for a great distance.",
                "Afflict targets with Darkness I", "for 10 seconds."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.SCULK_SHRIEKER, 1), lore, noteblockSound);
    }
    public static MobHead Wither(){
        UUID uuid = UUID.fromString("194154ec-5fac-11ed-9b6a-0242ac120002");
        String name = "Wither Head";
        EntityType entityType = EntityType.WITHER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/cdf74e323ed41436965f5c57ddf2815d5332fe999e68fbb9d6cf5c8bd4139f");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Wither II.", "Immune to Wither damage.", "Afflict Wither III for", "3 seconds on melee attack.",
                "Drink Milk to gain Regeneration I,", "Speed I, & Resistance I", "for 5 minutes."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.WITHER_ROSE, 8), lore, noteblockSound);
    }

    // Single-Skin Hostiles --------------------------------------------------------------------------------------------
    public static MobHead Blaze(){
        UUID uuid = UUID.fromString("194146b4-5fac-11ed-9b6a-0242ac120002");
        String name = "Blaze Core";
        EntityType entityType = EntityType.BLAZE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Feather Falling I.", "Half-Damage from all fire damage sources.",
                "Gain Fire Resistance I for", "5 seconds upon taking fire damage.",
                "Melee and ranged attacks have Fire Aspect.",
                "Broken blocks drop smelted.",
                "Right-Clicking with a Blaze Rod", "will shoot a small fireball, or",
                "Sneak-Right-Clicking a block will", "set it on fire.",
                "Right-Click with Blaze Powder while gliding", "to get a speed boost!",
                "Place next to any type of Furnace to", "double their cooking speed."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.BLAZE_ROD, 8), lore,null,enchants, noteblockSound);
    }
    public static MobHead CaveSpider(){
        UUID uuid = UUID.fromString("194147d6-5fac-11ed-9b6a-0242ac120002");
        String name = "Cave Spider Head";
        EntityType entityType = EntityType.CAVE_SPIDER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Poison I. Immune to Poison.", "Afflict Poison I for 10 seconds."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.MILK_BUCKET, 1), lore, noteblockSound);
    }
    public static MobHead Breeze(){
        UUID uuid = UUID.fromString("417c2d09-09e6-4960-8ab1-6458678c2736");
        String name = "Breeze Core";
        EntityType entityType = EntityType.BREEZE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(EntityType.BREEZE);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/cd6e602f76f80c0657b5aed64e267eeea702b31e6dae86346c8506f2535ced02");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Fall and Kinetic damage reduced by 90%.", //"Afflict Wind Charged for 5 seconds on hit.",
                "Projectiles are reflected", "back towards their source.", //"Burst away direct attackers.",
                "Sneak-Jump or engage an Elytra", "to burst into the air.", "Thrown Wind Charges have an 80%",
                "chance of being refunded."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.PROJECTILE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, EntityType.BREEZE, head, new ItemStack(Material.WIND_CHARGE, 32), lore,null,enchants, noteblockSound);
    }
    public static MobHead Drowned(){
        UUID uuid = UUID.fromString("194132b4-5fac-11ed-9b6a-0242ac120002");
        String name = "Drowned Head";
        EntityType entityType = EntityType.DROWNED;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/c84df79c49104b198cdad6d99fd0d0bcf1531c92d4ab6269e40b7d3cbbb8e98c");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Hunger I.", "Gain Slowness I when not wet.", "Your hunger will never", "drop below 19/20."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.SEA_PICKLE, 6), lore,null,enchants, noteblockSound);
    }
    public static MobHead Enderman(){
        UUID uuid = UUID.fromString("1941445c-5fac-11ed-9b6a-0242ac120002");
        String name = "Enderman Head";
        EntityType entityType = EntityType.ENDERMAN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Susceptible to all forms of water.", "If attacked or exposed to water,", "teleport to a nearby location.",
                "Immune to all projectiles.", "Immune to Ender Pearl damage.", "Thrown pearls have a 75%",
                "chance of being refunded."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.ENDER_PEARL, 8), lore, noteblockSound);
    }
    public static MobHead Endermite(){
        UUID uuid = UUID.fromString("19414e8e-5fac-11ed-9b6a-0242ac120002");
        String name = "Endermite Head";
        EntityType entityType = EntityType.ENDERMITE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Thrown pearls are refunded upon landing.", "Nearby Endermen will target you on sight.", "You take half damage from Endermen."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.POPPED_CHORUS_FRUIT, 16), lore, noteblockSound);
    }
    public static MobHead Evoker(){
        UUID uuid = UUID.fromString("1941406a-5fac-11ed-9b6a-0242ac120002");
        String name = "Evoker Head";
        EntityType entityType = EntityType.EVOKER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Sends a line of Evoker Fangs", "towards the enemies that hit you."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.EMERALD_BLOCK, 1), lore, noteblockSound);
    }
    public static MobHead Ghast(){
        UUID uuid = UUID.fromString("19414588-5fac-11ed-9b6a-0242ac120002");
        String name = "Ghast Head";
        EntityType entityType = EntityType.GHAST;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Feather Falling I",
                "Half-Damage from all fire damage sources.", "Gain Fire Resistance I for", "5 seconds upon taking fire damage.",
                "Hold Sneak while airborne to gain",
                "Levitation III for up to 10 seconds."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.GHAST_TEAR, 4), lore,null,enchants, noteblockSound);
    }
    public static MobHead Guardian(){
        UUID uuid = UUID.fromString("19413cbe-5fac-11ed-9b6a-0242ac120002");
        String name = "Guardian Head";
        EntityType entityType = EntityType.GUARDIAN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/932c24524c82ab3b3e57c2052c533f13dd8c0beb8bdd06369bb2554da86c123");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Lock-On to a target by looking at them and Sneaking.", "Beam inflicts 4 Hearts of damage after 2 seconds.",
                "Sneak again to release your Lock-On.", "Breaking Line of Sight will release your Lock-On."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3,Enchantment.THORNS,2);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = new ItemStack(Material.PRISMARINE,16);
        return new MobHead(uuid, name, entityType, head, loot, lore,null,enchants, noteblockSound);
    } // FIX LOOT
    public static MobHead Hoglin(){
        UUID uuid = UUID.fromString("194133e0-5fac-11ed-9b6a-0242ac120002");
        String name = "Hoglin Head";
        EntityType entityType = EntityType.HOGLIN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Strength I."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.PORKCHOP, 8), lore,null,enchants, noteblockSound);
    }
    public static MobHead Husk(){
        UUID uuid = UUID.fromString("19413192-5fac-11ed-9b6a-0242ac120002");
        String name = "Husk Head";
        EntityType entityType = EntityType.HUSK;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Hunger I.", "Gain Slowness I if not", "walking on a sandy block.", "Your hunger will never", "drop below 19/20."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 1);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.CHISELED_SANDSTONE, 16), lore,null,enchants, noteblockSound);
    }
    public static MobHead Illusioner(){
        UUID uuid = UUID.fromString("1941524e-5fac-11ed-9b6a-0242ac120002");
        String name = "Illusioner Head";
        EntityType entityType = EntityType.ILLUSIONER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/4639d325f4494258a473a93a3b47f34a0c51b3fceaf59fee87205a5e7ff31f68");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                ChatColor.RED + "[NYI]"
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        ItemStack loot = new ItemStack(Material.ARROW);
        return new MobHead(uuid, name, entityType, head, loot, lore, noteblockSound);
    } // FIX LOOT
    public static MobHead MagmaCube(){
        UUID uuid = UUID.fromString("19414902-5fac-11ed-9b6a-0242ac120002");
        String name = "Crystallized Magma Cube";
        EntityType entityType = EntityType.MAGMA_CUBE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Half-Damage from all fire damage sources.",
                "Gain Fire Resistance I for", "5 seconds upon taking fire damage.",
                "80% Reduction from Fall damage.",
                "Bouncy! Hold Sneak to stop bouncing.", "Sneak-Jump to jump high into the air."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.MAGMA_CREAM, 8), lore,null,enchants, noteblockSound);
    }
    public static MobHead Phantom(){
        UUID uuid = UUID.fromString("19413070-5fac-11ed-9b6a-0242ac120002");
        String name = "Phantom Head";
        EntityType entityType = EntityType.PHANTOM;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of("Gain Night Vision I");
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.PHANTOM_MEMBRANE, 6), lore, noteblockSound);
    }
    public static MobHead PiglinBrute(){
        UUID uuid = UUID.fromString("19413a70-5fac-11ed-9b6a-0242ac120002");
        String name = "Piglin Brute Head";
        EntityType entityType = EntityType.PIGLIN_BRUTE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Strength I."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = new ItemStack(Material.GOLDEN_AXE); // PLACEHOLDER
        return new MobHead(uuid, name, entityType, head, loot, lore,null,enchants, noteblockSound);
    } // FIX LOOT
    public static MobHead Pillager(){
        UUID uuid = UUID.fromString("19414fe2-5fac-11ed-9b6a-0242ac120002");
        String name = "Pillager Head";
        EntityType entityType = EntityType.PILLAGER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                ChatColor.RED + "[NYI]"
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.EMERALD, 8), lore, noteblockSound);
    }
    public static MobHead Ravager(){
        UUID uuid = UUID.fromString("19413dea-5fac-11ed-9b6a-0242ac120002");
        String name = "Ravager Head";
        EntityType entityType = EntityType.RAVAGER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Strength I, Resistance I,", "and Slowness II."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, EntityType.RAVAGER, head, new ItemStack(Material.SADDLE), lore, noteblockSound);
    }
    public static MobHead Shulker(){
        UUID uuid = UUID.fromString("19412f26-5fac-11ed-9b6a-0242ac120002");
        String name = "Shrunken Shulker";
        EntityType entityType = EntityType.SHULKER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Sneak while airborne to gain", "Levitation III & Speed I for", "up to 20 seconds.", "Attacking a creature afflicts",
                "them with Levitation I,", "melee for 2 seconds,", "projectile for 4 seconds."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.PROTECTION,2);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = HeadLootItemStack.shulkerLoot();
        return new MobHead(uuid, name, entityType, head, loot, lore,null,enchants, noteblockSound);
    }
    public static MobHead Silverfish(){
        UUID uuid = UUID.fromString("1941272e-5fac-11ed-9b6a-0242ac120002");
        String name = "Silverfish Head";
        EntityType entityType = EntityType.SILVERFISH;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Summons 8 Silverfish to fight for", "you when attacked.", "Summon Cooldown: 15 Seconds.", "Summon Max Lifetime: 20 Seconds."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.INFESTED_STONE, 8), lore, noteblockSound);
    }
    public static MobHead Slime(){
        UUID uuid = UUID.fromString("19414b32-5fac-11ed-9b6a-0242ac120002");
        String name = "Crystallized Slime";
        EntityType entityType = EntityType.SLIME;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "90% Reduction from Fall damage.", "Bouncy! Hold Sneak to stop bouncing.", "Sneak-Jump to bounce high into the air."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.SLIME_BLOCK, 4), lore, noteblockSound);
    }
    public static MobHead Spider(){
        UUID uuid = UUID.fromString("194141aa-5fac-11ed-9b6a-0242ac120002");
        String name = "Spider Head";
        EntityType entityType = EntityType.SPIDER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                ChatColor.RED + "[NYI]"
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.COBWEB, 8), lore, noteblockSound);
    }
    public static MobHead Stray(){
        UUID uuid = UUID.fromString("19412d50-5fac-11ed-9b6a-0242ac120002");
        String name = "Stray Head";
        EntityType entityType = EntityType.STRAY;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/2c5097916bc0565d30601c0eebfeb287277a34e867b4ea43c63819d53e89ede7");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Afflict other creatures with", "Slowness I for 5 seconds.",
                "Drink Milk to gain Regeneration I,", "Speed I, & Resistance I", "for 5 minutes."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.PROJECTILE_PROTECTION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = HeadLootItemStack.strayLoot();
        return new MobHead(uuid, name, entityType, head, loot, lore,null,enchants, noteblockSound);
    }
    public static MobHead Bogged(){
        UUID uuid = UUID.fromString("191e395c-1862-4817-b86d-d312bf53cff2");
        String name = "Bogged Skull";
        EntityType entityType = EntityType.BOGGED;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(EntityType.BOGGED);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/483474a813a0389e52fc5aebd5e69e09a7254164288153b13bd05963bf239611");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Poison I. Immune to Poison damage.",
                "Afflict other creatures with", "Poison I for 4 seconds.",
                "Drink Milk to gain Regeneration I,", "Speed I, & Resistance I", "for 5 minutes."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.PROJECTILE_PROTECTION, 3);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = HeadLootItemStack.boggedLoot();
        return new MobHead(uuid, name, EntityType.BOGGED, head, loot, lore,null,enchants, noteblockSound);
    }
    public static MobHead Vex(){
        UUID uuid = UUID.fromString("19413f16-5fac-11ed-9b6a-0242ac120002");
        String name = "Vex Head";
        EntityType entityType = EntityType.VEX;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/b663134d7306bb604175d2575d686714b04412fe501143611fcf3cc19bd70abe");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Summons 2 Vex to fight for", "you when attacked.", "Summon Cooldown: 10 Seconds.",
                "Summon Max Lifetime: 30 Seconds."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        ItemStack loot = new ItemStack(Material.IRON_SWORD);
        return new MobHead(uuid, name, entityType, head, loot, lore, noteblockSound);
    } // FIX LOOT
    public static MobHead Vindicator(){
        UUID uuid = UUID.fromString("1941510e-5fac-11ed-9b6a-0242ac120002");
        String name = "Vindicator Head";
        EntityType entityType = EntityType.VINDICATOR;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                ChatColor.RED + "[NYI]"
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        ItemStack loot = new ItemStack(Material.IRON_AXE);
        return new MobHead(uuid, name, entityType, head, loot, lore, noteblockSound);
    } // FIX LOOT
    public static MobHead Witch(){
        UUID uuid = UUID.fromString("19413b92-5fac-11ed-9b6a-0242ac120002");
        String name = "Witch Head";
        EntityType entityType = EntityType.WITCH;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/91f1508590291bee0562d32ad7544f628aead89ea3caa54dc875120ac95b1fa");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain temporary resistances from", "various types of incoming damage.",
                "Place next to a Brewing Stand", "to double its brewing speed."
        );
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.EXPERIENCE_BOTTLE, 13), lore, noteblockSound);
    }
    public static MobHead Zoglin(){
        UUID uuid = UUID.fromString("194134f8-5fac-11ed-9b6a-0242ac120002");
        String name = "Zoglin Head";
        EntityType entityType = EntityType.ZOGLIN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/3c8c7c5d0556cd6629716e39188b21e7c0477479f242587bf19e0bc76b322551");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Slowness I, Hunger I & Strength I.", "Your hunger will never", "drop below 19/20.",
                "Half-Damage from all fire damage sources.", "Gain Fire Resistance I for", "5 seconds upon taking fire damage."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        return new MobHead(uuid, name, entityType, head, new ItemStack(Material.ROTTEN_FLESH, 16), lore,null,enchants, noteblockSound);
    }
    public static MobHead ZombifiedPiglin(){
        UUID uuid = UUID.fromString("19414a1a-5fac-11ed-9b6a-0242ac120002");
        String name = "Zombified Piglin Head";
        EntityType entityType = EntityType.ZOMBIFIED_PIGLIN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
                "Gain Slowness I and Hunger I.", "Your hunger will never", "drop below 19/20.",
                "Half-Damage from all fire damage sources.", "Gain Fire Resistance I for", "5 seconds upon taking fire damage.",
                "Summon another Zombified Piglin to", "fight for you when attacked.", "Summon Cooldown: 5 Seconds.", "Summon Max Lifetime: 30 Seconds."
        );
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack loot = new ItemStack(Material.GOLDEN_SWORD);
        return new MobHead(uuid, name, entityType, head, loot, lore,null,enchants, noteblockSound);
    } // FIX LOOT
    public static MobHead Creaking(){
        UUID uuid = UUID.fromString("8368c742-75a9-4af2-bb6e-5d4d95e84216");
        String name = "Creaking Head";
        EntityType entityType = EntityType.CREAKING;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        URL texture = null;
        try {
            texture = new URL("http://textures.minecraft.net/texture/aef009d86fcc420361a68cbb8bfa85a7422bfe9e2f306247be1e1b5d20fc52b1");
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> lore = List.of(
        );
        //Map<Enchantment, Integer> enchants = Map.of(Enchantment.FIRE_PROTECTION, 4);
        //ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
        ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
        ItemStack loot = new ItemStack(Material.CREAKING_HEART);
        return new MobHead(uuid, name, entityType, head, loot, lore,null,null, noteblockSound);
    }

    // Multi-Skin Passives ---------------------------------------------------------------------------------------------
    public static List<MobHead> Axolotls(){
        EntityType entityType = EntityType.AXOLOTL;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("19416428-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941673e-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194168c4-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19416c8e-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19416dd8-5fac-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Axolotl Head",
                "Wild Axolotl Head",
                "Golden Axolotl Head",
                "Cyan Axolotl Head",
                "Blue Axolotl Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/5c138f401c67fc2e1e387d9c90a9691772ee486e8ddbf2ed375fc8348746f936"));
            textures.add(new URL("http://textures.minecraft.net/texture/4d7efe02012cf31ae2708e7d7df079726575c7ee8504328175fe544708187dce"));
            textures.add(new URL("http://textures.minecraft.net/texture/7f80cc1492e44668cccdb40178c3a6689e8dfc0d234e98553fb7debc26fcaeac"));
            textures.add(new URL("http://textures.minecraft.net/texture/e1c2d0c3b96ad45b466388e028b247aafe36b26b12c411ecb72e9b50ea21e52c"));
            textures.add(new URL("http://textures.minecraft.net/texture/eef630657e4a279b0b7ea0f67905920af365f9c84ca9f34a32b53343ff629910"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Axolotl.Variant.LUCY.toString(),
                Axolotl.Variant.WILD.toString(),
                Axolotl.Variant.GOLD.toString(),
                Axolotl.Variant.CYAN.toString(),
                Axolotl.Variant.BLUE.toString()
        );
        ItemStack loot = HeadLootItemStack.axolotlLoot();
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 3);
        List<String> lore = List.of(
                "Gain Strength I when wet.", "When attacked, gain Restoration I", "for 10 seconds."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,enchants,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Cats(){
        EntityType entityType = EntityType.CAT;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("19416f2c-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194170d0-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941721a-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417346-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417472-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194175b2-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417a8a-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417bd4-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417cf6-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417e7c-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19417fb2-5fac-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
            "Tabby Cat Head",
                "Black Cat Head",
                "Orange Cat Head",
                "Siamese Cat Head",
                "British Shorthair Cat Head",
                "Calico Cat Head",
                "Persian Cat Head",
                "Ragdoll Cat Head",
                "White Cat Head",
                "Jellie Cat Head",
                "Tuxedo Cat Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/de28d30db3f8c3fe50ca4f26f3075e36f003ae8028135a8cd692f24c9a98ae1b"));
            textures.add(new URL("http://textures.minecraft.net/texture/22c1e81ff03e82a3e71e0cd5fbec607e11361089aa47f290d46c8a2c07460d92"));
            textures.add(new URL("http://textures.minecraft.net/texture/2113dbd3c6a078a17b4edb78ce07d836c38dace5027d4b0a83fd60e7ca7a0fcb"));
            textures.add(new URL("http://textures.minecraft.net/texture/d5b3f8ca4b3a555ccb3d194449808b4c9d783327197800d4d65974cc685af2ea"));
            textures.add(new URL("http://textures.minecraft.net/texture/5389e0d5d3e81f84b570e2978244b3a73e5a22bcdb6874b44ef5d0f66ca24eec"));
            textures.add(new URL("http://textures.minecraft.net/texture/340097271bb680fe981e859e8ba93fea28b813b1042bd277ea3329bec493eef3"));
            textures.add(new URL("http://textures.minecraft.net/texture/ff40c746260ef91c96b27159795e87191ae7ce3d5f767bf8c74faad9689af25d"));
            textures.add(new URL("http://textures.minecraft.net/texture/dc7a45d25889e3fdf7797cb258e26d4e94f5bc13eef00795dafef2e83e0ab511"));
            textures.add(new URL("http://textures.minecraft.net/texture/21d15ac9558e98b89aca89d3819503f1c5256c2197dd3c34df5aac4d72e7fbed"));
            textures.add(new URL("http://textures.minecraft.net/texture/a0db41376ca57df10fcb1539e86654eecfd36d3fe75e8176885e93185df280a5"));
            textures.add(new URL("http://textures.minecraft.net/texture/4fd10c8e75f67398c47587d25fc146f311c053cc5d0aeab8790bce36ee88f5f8"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Cat.Type.TABBY.getKeyOrThrow().getKey(),
                Cat.Type.ALL_BLACK.getKeyOrThrow().getKey(),
                Cat.Type.RED.getKeyOrThrow().getKey(),
                Cat.Type.SIAMESE.getKeyOrThrow().getKey(),
                Cat.Type.BRITISH_SHORTHAIR.getKeyOrThrow().getKey(),
                Cat.Type.CALICO.getKeyOrThrow().getKey(),
                Cat.Type.PERSIAN.getKeyOrThrow().getKey(),
                Cat.Type.RAGDOLL.getKeyOrThrow().getKey(),
                Cat.Type.WHITE.getKeyOrThrow().getKey(),
                Cat.Type.JELLIE.getKeyOrThrow().getKey(),
                Cat.Type.BLACK.getKeyOrThrow().getKey()
        );
        ItemStack loot = new ItemStack(Material.STRING, 1);
        List<MobHead> heads = new ArrayList<>();
        List<String> lore = List.of("Gain Speed I.", "Gain Jump V when crouched.", "80% Fall damage reduction.");
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Foxes(){
        EntityType entityType = EntityType.FOX;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("766c27be-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c299e-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Red Fox Head",
                "Arctic Fox Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"));
            textures.add(new URL("http://textures.minecraft.net/texture/ddcd0db8cbe8f1e0ab1ec0a9385fb9288da84d3202c1c397da76ee1035e608b0"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Fox.Type.RED.toString(),
                Fox.Type.SNOW.toString()
        );
        ItemStack loot = new ItemStack(Material.SWEET_BERRIES, 16);
        List<String> lore = List.of(
                "Immune to Berry Bush pricking.",//, "Sneak-Jump to pounce and make a shockwave!", ChatColor.RED + "[WIP]"
                "Summons 3 Foxes with", "Knockback Swords to fight for you.",
                "Summon Cooldown: 10 Seconds.", "Summon Lifespan: 30 Seconds."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Frogs(){
        EntityType entityType = EntityType.FROG;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("766c6792-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c697c-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c7318-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Temperate Frog Head",
                "Warm Frog Head",
                "Cold Frog Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/1f3e29dd947a177895f6121d2331b65ac3f896fda4bdd1151491e40b804952a7"));
            textures.add(new URL("http://textures.minecraft.net/texture/1e9312b5b2bab9ad51ea4b6a407d6d390bb5043408757b976a7556898ac43de0"));
            textures.add(new URL("http://textures.minecraft.net/texture/27bcccc125a4110434a85c40ada039d050f14ef7db34a3444067310f8ce69606"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Frog.Variant.TEMPERATE.getKeyOrThrow().getKey(),
                Frog.Variant.WARM.getKeyOrThrow().getKey(),
                Frog.Variant.COLD.getKeyOrThrow().getKey()
        );
        ItemStack loot = new ItemStack(Material.SLIME_BALL, 8);
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.AQUA_AFFINITY, 1, Enchantment.RESPIRATION, 2);
        List<String> lore = List.of(
                "Gain Jump Boost I.",
                "Sneak-Jump to leap towards where you are looking.", "Sneak-Right-Clicking on a creature will attempt",
                "to eat them.", "Eating creatures can restore your Hunger, Saturation", "and Air, as well as gain you various Potion effects.",
                "Nearby creatures that can be eaten will Glow.", "Reduced fall damage."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,enchants,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,enchants,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Horses(){
        EntityType entityType = EntityType.HORSE;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("194180d4-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194181f6-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194184da-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19418782-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194188fe-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19418aac-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19418bec-5fac-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "White Horse Head",
                "Creamy Horse Head",
                "Chestnut Horse Head",
                "Brown Horse Head",
                "Black Horse Head",
                "Gray Horse Head",
                "Dark-Brown Horse Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/9f4bdd59d4f8f1d5782e0fee4bd64aed100627f188a91489ba37eeadededd827"));
            textures.add(new URL("http://textures.minecraft.net/texture/a6dae0ade0e0dafb6dbc7786ce4241242b6b6df527a0f7af0a42184c93fd646b"));
            textures.add(new URL("http://textures.minecraft.net/texture/9717d71025f7a62c90a333c51663ffeb385a9a0d92af68083c5b045c0524b23f"));
            textures.add(new URL("http://textures.minecraft.net/texture/25e397def0af06feef22421860088186639732aa0a5eb5756e0aa6b03fd092c8"));
            textures.add(new URL("http://textures.minecraft.net/texture/3efb0b9857d7c8d295f6df97b605f40b9d07ebe128a6783d1fa3e1bc6e44117"));
            textures.add(new URL("http://textures.minecraft.net/texture/8f0d955889b0378d4933c956398567e770103ae9eff0f702d0d53d52e7f6a83b"));
            textures.add(new URL("http://textures.minecraft.net/texture/156b7bc1a4836eb428ea8925eceb5e01dfbd30c7deff6c9482689823203cfd2f"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Horse.Color.WHITE.toString(),
                Horse.Color.CREAMY.toString(),
                Horse.Color.CHESTNUT.toString(),
                Horse.Color.BROWN.toString(),
                Horse.Color.BLACK.toString(),
                Horse.Color.GRAY.toString(),
                Horse.Color.DARK_BROWN.toString()
        );
        ItemStack loot = new ItemStack(Material.HAY_BLOCK, 1);
        List<MobHead> heads = new ArrayList<>();
        List<String> lore = List.of("Gain Speed II.", "Step-Up One-Block high.", "Charge a Jump by holding Sneak.");
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Llamas(){
        EntityType entityType = EntityType.LLAMA;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("19418d04-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19419286-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194193ee-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19419510-5fac-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Creamy Llama Head",
                "White Llama Head",
                "Brown Llama Head",
                "Gray Llama Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/2a5f10e6e6232f182fe966f501f1c3799d45ae19031a1e4941b5dee0feff059b"));
            textures.add(new URL("http://textures.minecraft.net/texture/83d9b5915912ffc2b85761d6adcb428a812f9b83ff634e331162ce46c99e9"));
            textures.add(new URL("http://textures.minecraft.net/texture/c2b1ecff77ffe3b503c30a548eb23a1a08fa26fd67cdff389855d74921368"));
            textures.add(new URL("http://textures.minecraft.net/texture/cf24e56fd9ffd7133da6d1f3e2f455952b1da462686f753c597ee82299a"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Llama.Color.CREAMY.toString(),
                Llama.Color.WHITE.toString(),
                Llama.Color.BROWN.toString(),
                Llama.Color.GRAY.toString()
        );
        ItemStack loot = new ItemStack(Material.CHEST, 2);
        List<String> lore = List.of(
                "Spit a nasty projectile at", "anything that attacks you.", "Deals 1.5 Hearts of damage.",
                "Storage Containers can be picked up", "with Shift-Right-Click, imposing",
                "a level of Slowness for every other container."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Mooshrooms(){
        EntityType entityType = EntityType.MOOSHROOM;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("766c63c8-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c65bc-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Mooshroom Head",
                "Brown Mooshroom Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/2b52841f2fd589e0bc84cbabf9e1c27cb70cac98f8d6b3dd065e55a4dcb70d77"));
            textures.add(new URL("http://textures.minecraft.net/texture/b6d5fc7031acc95beeb52875f15408e979a0a9c391b6db7ecee7e400072de5c4"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                MushroomCow.Variant.RED.toString(),
                MushroomCow.Variant.BROWN.toString()
        );
        List<ItemStack> loots = List.of(
                new ItemStack(Material.RED_MUSHROOM_BLOCK, 32),
                new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 32)
        );
        List<List<String>> lores = List.of(
                List.of(
                        "Other players can milk you", "for both Mushroom Stew and Milk",
                        "by Right-Clicking with a bowl,", "bucket, or empty hand!",
                        "Sneak-Right-Click to milk yourself", "with a bowl or bucket."
                ),
                List.of(
                        "Other players can milk you", "for both Mushroom Stew and Milk",
                        "by Right-Clicking with a bowl,", "bucket, or empty hand!",
                        "The stew can be sus if you",
                        "are milked holding or are", "nearby certain flowers.",
                        "They can also get some", "straight from the source.",
                        "Sneak-Right-Click to milk yourself", "with a bowl or bucket."
                )

        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            List<String> lore = lores.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            ItemStack loot = loots.get(i);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Pandas(){
        EntityType entityType = EntityType.PANDA;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("19411a04-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("766cac7a-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Panda Head",
                "Brown Panda Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166"));
            textures.add(new URL("http://textures.minecraft.net/texture/b4f7c73fda6a34cf8be4c7907dd0f5f0865dd77fd882fc633563649c57517cae"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Panda.Gene.NORMAL.toString(),
                Panda.Gene.BROWN.toString()
        );
        ItemStack loot = new ItemStack(Material.BAMBOO, 16);
        List<String> lore = List.of(
                "Eat Bamboo out of your", "inventory like potato chips!", "Will always leave one", "for replanting."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Parrots(){
        EntityType entityType = EntityType.PARROT;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("1941a06e-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941a1cc-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941a302-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941a438-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c2476-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Ballooned Red Parrot",
                "Ballooned Blue Parrot",
                "Ballooned Green Parrot",
                "Ballooned Cyan Parrot",
                "Ballooned Gray Parrot"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/c38796f62db5f93949ae26a2f7a3c5f797a31d2694bce4c48ee843ee85f7"));
            textures.add(new URL("http://textures.minecraft.net/texture/dcb934e4a5d51c2c958dd2c9f03e1915a4722487bcd8bcd71eddc53377622d9d"));
            textures.add(new URL("http://textures.minecraft.net/texture/9fbb3deb3d8adeea9914acb7a073ca566c3fec7f58fd63d6197af52fbdbf8780"));
            textures.add(new URL("http://textures.minecraft.net/texture/a9df6dd4f9434d44c97dbac4fa98591f1e37506355b9e4406f716bec7dd248e"));
            textures.add(new URL("http://textures.minecraft.net/texture/efe08d511499a247146128e55ab6547ecd967d4dbcf803f7ceea2658737c9fa"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Parrot.Variant.RED.toString(),
                Parrot.Variant.BLUE.toString(),
                Parrot.Variant.GREEN.toString(),
                Parrot.Variant.CYAN.toString(),
                Parrot.Variant.GRAY.toString()
        );
        ItemStack loot = new ItemStack(Material.FEATHER, 4);
        List<MobHead> heads = new ArrayList<>();
        List<String> lore = List.of(
                "Gain Feather Falling I.", "When targeted by a hostile creature,",
                "their sound plays in the direction", "of the hostile creature."
        );
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Rabbits(){
        EntityType entityType = EntityType.RABBIT;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("19415794-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19415b54-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19415cee-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19415e4c-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19415f96-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941627a-5fac-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Brown Rabbit Head",
                "White Rabbit Head",
                "Black Rabbit Head",
                "Black & White Rabbit Head",
                "Golden Rabbit Head",
                "Salt & Pepper Rabbit Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/7d1169b2694a6aba826360992365bcda5a10c89a3aa2b48c438531dd8685c3a7"));
            textures.add(new URL("http://textures.minecraft.net/texture/b4dcfed6897a18a7ab995a66134d41a1ca821b69bcb7d14cf269b4a98df49a8"));
            textures.add(new URL("http://textures.minecraft.net/texture/72c58116a147d1a9a26269224a8be184fe8e5f3f3df9b61751369ad87382ec9"));
            textures.add(new URL("http://textures.minecraft.net/texture/cb8cff4b15b8ca37e25750f345718f289cb22c5b3ad22627a71223faccc"));
            textures.add(new URL("http://textures.minecraft.net/texture/c977a3266bf3b9eaf17e5a02ea5fbb46801159863dd288b93e6c12c9cb"));
            textures.add(new URL("http://textures.minecraft.net/texture/ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Rabbit.Type.BROWN.toString(),
                Rabbit.Type.WHITE.toString(),
                Rabbit.Type.BLACK.toString(),
                Rabbit.Type.BLACK_AND_WHITE.toString(),
                Rabbit.Type.GOLD.toString(),
                Rabbit.Type.SALT_AND_PEPPER.toString()
        );
        ItemStack loot = HeadLootItemStack.rabbitLoot();
        List<String> lore = List.of(
                "Gain Jump II.", "Gain Speed II for 10 seconds", "when attacked.", "Sneak-Jump to shoot into the air.",
                "Half-damage from Falling."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Sheepies(){
        EntityType entityType = EntityType.SHEEP;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("766c30d8-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c32b8-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c345c-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c37f4-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c3c36-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c3e2a-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c442e-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c4672-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c492e-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c4b7c-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c4d66-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c51a8-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c539c-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c581a-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c5d1a-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c6080-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "White Sheep Head",
                "Orange Sheep Head",
                "Magenta Sheep Head",
                "Light-Blue Sheep Head",
                "Yellow Sheep Head",
                "Lime Sheep Head",
                "Pink Sheep Head",
                "Gray Sheep Head",
                "Light-Gray Sheep Head",
                "Cyan Sheep Head",
                "Purple Sheep Head",
                "Blue Sheep Head",
                "Brown Sheep Head",
                "Green Sheep Head",
                "Red Sheep Head",
                "Black Sheep Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70"));
            textures.add(new URL("http://textures.minecraft.net/texture/f098397a270b4c3d2b1e574b8cfd3cc4ea3409066cefe31ea993633c9d576"));
            textures.add(new URL("http://textures.minecraft.net/texture/1836565c7897d49a71bc18986d1ea6561321a0bbf711d41a56ce3bb2c217e7a"));
            textures.add(new URL("http://textures.minecraft.net/texture/9a624f5966bedd6e67f654b59e9249b2ecf307d903339bc199923977f4c8c"));
            textures.add(new URL("http://textures.minecraft.net/texture/26a4112df1e4bce2a5e28417f3aaff79cd66e885c3724554102cef8eb8"));
            textures.add(new URL("http://textures.minecraft.net/texture/92a2448f58a491332434e85c45d786d874397e830a3a7894e6d92699c42b30"));
            textures.add(new URL("http://textures.minecraft.net/texture/2ac74a2b9b91452e56fa1dda5db81077856e49f27c6e2de1e841e5c95a6fc5ab"));
            textures.add(new URL("http://textures.minecraft.net/texture/4287eb501391f275389f166ec9febea75ec4ae951b88b38cae87df7e24f4c"));
            textures.add(new URL("http://textures.minecraft.net/texture/ce1ac683993be35512e1be31d1f4f98e583edb1658a9e21192c9b23b5cccdc3"));
            textures.add(new URL("http://textures.minecraft.net/texture/46f6c7e7fd514ce0acc68593229e40fcc4352b841646e4f0ebcccb0ce23d16"));
            textures.add(new URL("http://textures.minecraft.net/texture/ae52867afef38bb14a26d1426c8c0f116ad34761acd92e7aae2c819a0d55b85"));
            textures.add(new URL("http://textures.minecraft.net/texture/d9ec22818d1fbfc8167fbe36728b28240e34e16469a2929d03fdf511bf2ca1"));
            textures.add(new URL("http://textures.minecraft.net/texture/a55ad6e5db5692d87f51511f4e09b39ff9ccb3de7b4819a7378fce8553b8"));
            textures.add(new URL("http://textures.minecraft.net/texture/6de55a395a2246445b45f9a6d68872344bbea54f362d529fc5b0b857ea58326b"));
            textures.add(new URL("http://textures.minecraft.net/texture/839af477eb627815f723a5662556ec9dfcbab5d494d338bd214232f23e446"));
            textures.add(new URL("http://textures.minecraft.net/texture/32652083f28ed1b61f9b965df1abf010f234681c21435951c67d88364749822"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                DyeColor.WHITE.toString(),
                DyeColor.ORANGE.toString(),
                DyeColor.MAGENTA.toString(),
                DyeColor.LIGHT_BLUE.toString(),
                DyeColor.YELLOW.toString(),
                DyeColor.LIME.toString(),
                DyeColor.PINK.toString(),
                DyeColor.GRAY.toString(),
                DyeColor.LIGHT_GRAY.toString(),
                DyeColor.CYAN.toString(),
                DyeColor.PURPLE.toString(),
                DyeColor.BLUE.toString(),
                DyeColor.BROWN.toString(),
                DyeColor.GREEN.toString(),
                DyeColor.RED.toString(),
                DyeColor.BLACK.toString()
        );
        List<ItemStack> loots = List.of(
                new ItemStack(Material.WHITE_WOOL, 16),
                new ItemStack(Material.ORANGE_WOOL, 16),
                new ItemStack(Material.MAGENTA_WOOL, 16),
                new ItemStack(Material.LIGHT_BLUE_WOOL, 16),
                new ItemStack(Material.YELLOW_WOOL, 16),
                new ItemStack(Material.LIME_WOOL, 16),
                new ItemStack(Material.PINK_WOOL, 16),
                new ItemStack(Material.GRAY_WOOL, 16),
                new ItemStack(Material.LIGHT_GRAY_WOOL, 16),
                new ItemStack(Material.CYAN_WOOL, 16),
                new ItemStack(Material.PURPLE_WOOL, 16),
                new ItemStack(Material.BLUE_WOOL, 16),
                new ItemStack(Material.BROWN_WOOL, 16),
                new ItemStack(Material.GREEN_WOOL, 16),
                new ItemStack(Material.RED_WOOL, 16),
                new ItemStack(Material.BLACK_WOOL, 16)
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            List<String> lore = List.of("Right-Click to eat Grass.", "Restores Hunger and Saturation", "Some grasses can give Potion Effects.");
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            ItemStack loot = loots.get(i);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> TraderLlamas(){
        EntityType entityType = EntityType.TRADER_LLAMA;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("19419632-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("1941975e-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("194198bc-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("19419b0a-5fac-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Creamy Trader Llama Head",
                "White Trader Llama Head",
                "Brown Trader Llama Head",
                "Gray Trader Llama Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/e89a2eb17705fe7154ab041e5c76a08d41546a31ba20ea3060e3ec8edc10412c"));
            textures.add(new URL("http://textures.minecraft.net/texture/7087a556d4ffa95ecd2844f350dc43e254e5d535fa596f540d7e77fa67df4696"));
            textures.add(new URL("http://textures.minecraft.net/texture/8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d"));
            textures.add(new URL("http://textures.minecraft.net/texture/be4d8a0bc15f239921efd8be3480ba77a98ee7d9ce00728c0d733f0a2d614d16"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Llama.Color.CREAMY.toString(),
                Llama.Color.WHITE.toString(),
                Llama.Color.BROWN.toString(),
                Llama.Color.GRAY.toString()
        );
        ItemStack loot = new ItemStack(Material.LEAD, 2);
        List<String> lore = List.of(
                "Spit a nasty projectile at", "anything that attacks you.", "Deals 1.5 Hearts of damage.",
                "Storage Containers can be picked up", "with Shift-Right-Click, imposing",
                "a level of Slowness for every other container."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Villagers(){
        EntityType entityType = EntityType.VILLAGER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("766c74e4-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c77c8-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c799e-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c7c82-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c7e62-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c80a6-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c884e-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Desert Villager Head",
                "Jungle Villager Head",
                "Plains Villager Head",
                "Savanna Villager Head",
                "Snow Villager Head",
                "Swamp Villager Head",
                "Taiga Villager Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("https://textures.minecraft.net/texture/10bf6df37dac6ca6089d2ba04135f223d4d850df9f09c7ec4eaf8c50764cbc50"));
            textures.add(new URL("https://textures.minecraft.net/texture/44b062a9f8399dccb6251a74e618647342a3c0240ca56f34614d52f60a3fecec"));
            textures.add(new URL("https://textures.minecraft.net/texture/d14bff1a38c9154e5ec84ce5cf00c58768e068eb42b2d89a6bbd29787590106b"));
            textures.add(new URL("https://textures.minecraft.net/texture/14f05fd4215ea2a43244e832c723f65f05c2562abfe0bdf336f50293e683789d"));
            textures.add(new URL("https://textures.minecraft.net/texture/20c641e3d3764ed1c1f1907c4334e2b1303e2152b13d1eb0c605763f97fb258a"));
            textures.add(new URL("https://textures.minecraft.net/texture/9ad7a9e8fe2bdfea03bb1f9fabe45fb10cf69a72e3760e5fd9a70f3384c536ad"));
            textures.add(new URL("https://textures.minecraft.net/texture/61e897719b54b844fa059f04817e13db8abd97e6bdb0624093032b4512f7a1c6"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Villager.Type.DESERT.getKeyOrThrow().getKey(),
                Villager.Type.JUNGLE.getKeyOrThrow().getKey(),
                Villager.Type.PLAINS.getKeyOrThrow().getKey(),
                Villager.Type.SAVANNA.getKeyOrThrow().getKey(),
                Villager.Type.SNOW.getKeyOrThrow().getKey(),
                Villager.Type.SWAMP.getKeyOrThrow().getKey(),
                Villager.Type.TAIGA.getKeyOrThrow().getKey()
        );
        ItemStack loot = new ItemStack(Material.BREAD, 1);
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            List<String> lore = List.of(
                    ChatColor.RED + "[NYI]"
            );
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Wolves(){
        EntityType entityType = EntityType.WOLF;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("194100dc-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("c363c344-f9ed-4734-984f-383211d175e8"),
                UUID.fromString("625c0f6f-d9d4-41ce-a136-fe86d7ad9128"),
                UUID.fromString("7ad284d3-b3a5-44ec-8dbc-80c58f4fac13"),
                UUID.fromString("fde9c51a-84a1-403e-8dcf-439d5fc3e465"),
                UUID.fromString("569993fa-fefe-499b-beec-d1df044d17fb"),
                UUID.fromString("1888531f-8aeb-4337-8acd-fb242b3dcef5"),
                UUID.fromString("6f4eeaab-f2ea-4d07-9012-8745d2ee2f8c"),
                UUID.fromString("995b8492-f789-4e76-a4f5-da832bd23311")
        );
        List<String> names = List.of(
                "Pale Wolf Head",
                "Rusty Wolf Head",
                "Black Wolf Head",
                "Striped Wolf Head",
                "Snowy Wolf Head",
                "Ashen Wolf Head",
                "Woods Wolf Head",
                "Spotted Wolf Head",
                "Chestnut Wolf Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("https://textures.minecraft.net/texture/549fa8831f07e9ca4feb087510379ea37c9c112259576b0d78d8713199292e42"));
            textures.add(new URL("https://textures.minecraft.net/texture/af904900ea732b77e696422266d4ce883b72695a707ba9ccb17925f6c37600e"));
            textures.add(new URL("https://textures.minecraft.net/texture/be454d963a9a1ec0a51368c3c7e908ebbbe152293e07b87fed13e61e7e852c0d"));
            textures.add(new URL("https://textures.minecraft.net/texture/3e0cda2200fda9fd07b319e1156a48cccf34ae657e5d7843995dd1fc7eed4265"));
            textures.add(new URL("https://textures.minecraft.net/texture/5e9702a29dd3464927ec6b452b0b24f95ab1fb161bed3814c4ab691e18adde37"));
            textures.add(new URL("https://textures.minecraft.net/texture/665f8d2cf8f830c3011615736da5a8624290dd490e199f3d09050e68e32b353c"));
            textures.add(new URL("https://textures.minecraft.net/texture/de929c290b21eea8662a1757ab93d627ab65503ff02e3cde15441e1cbb41f6cd"));
            textures.add(new URL("https://textures.minecraft.net/texture/cb5c6b95536d77e9570133266f3c620120e15c89073c683b3855a595d0a1f129"));
            textures.add(new URL("https://textures.minecraft.net/texture/ec99ecac21148139186bfb4da0169ad579a9bea39be021aaa9a848e4c7f8694"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Wolf.Variant.PALE.getKeyOrThrow().getKey(),
                Wolf.Variant.RUSTY.getKeyOrThrow().getKey(),
                Wolf.Variant.BLACK.getKeyOrThrow().getKey(),
                Wolf.Variant.STRIPED.getKeyOrThrow().getKey(),
                Wolf.Variant.SNOWY.getKeyOrThrow().getKey(),
                Wolf.Variant.ASHEN.getKeyOrThrow().getKey(),
                Wolf.Variant.WOODS.getKeyOrThrow().getKey(),
                Wolf.Variant.SPOTTED.getKeyOrThrow().getKey(),
                Wolf.Variant.CHESTNUT.getKeyOrThrow().getKey()
        );
        ItemStack loot = new ItemStack(Material.STICK, 1);
        List<String> lore = List.of(
                "Summons 2 Wolves to fight", "for you when attacked.", "Summon Cooldown: 10 Seconds.", "Summon Max Lifetime: 60 Seconds."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Pigs(){
        EntityType entityType = EntityType.PIG;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("1940fe48-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("6e6018ba-3ae9-4cdc-aca1-ed71ea8a40ec"),
                UUID.fromString("71f8637d-73ca-4659-9cca-7e0dc46bcfaf")
        );
        List<String> names = List.of(
                "Temperate Pig Head",
                "Warm Pig Head",
                "Cold Pig Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4"));
            textures.add(new URL("http://textures.minecraft.net/texture/5ef811b9dff0a3397545cbe699084f448193270da11cd9144e4666bbed8eb845"));
            textures.add(new URL("https://textures.minecraft.net/texture/6a7499f4d45cc05b3e349c55a369bffbb3a09341766dfd673c2ab47418b51a30"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Pig.Variant.TEMPERATE.getKeyOrThrow().toString(),
                Pig.Variant.WARM.getKeyOrThrow().toString(),
                Pig.Variant.COLD.getKeyOrThrow().toString()
        );
        ItemStack loot = new ItemStack(Material.PORKCHOP, 8);
        List<String> lore = List.of(
                ChatColor.RED + "[NYI]"
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Chickens(){
        EntityType entityType = EntityType.CHICKEN;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("1940ff9c-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("2b9f4346-b019-4702-89d2-b79141a35806"),
                UUID.fromString("00e60d8d-2670-4f88-bc9c-25850601fea3")
        );
        List<String> names = List.of(
                "Temperate Chicken Head",
                "Warm Chicken Head",
                "Cold Chicken Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/933b529b2db8d9d181c144ae14a5a7826e5dd2331e0d1020f45bb2bbacf71fc"));
            textures.add(new URL("http://textures.minecraft.net/texture/c414822a85c5e7073e207da133ce226859b0f74f238d584c3d3a42f223bb16bf"));
            textures.add(new URL("http://textures.minecraft.net/texture/69b26cc2036bd95467ca7bd9f82ebc3f8c2198d84e899834ef0643371c485881"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Chicken.Variant.TEMPERATE.getKeyOrThrow().toString(),
                Chicken.Variant.WARM.getKeyOrThrow().toString(),
                Chicken.Variant.COLD.getKeyOrThrow().toString()
        );
        List<String> lore = List.of(
                "Gain Slow Falling I.", "Crouch over Eggs to incubate them.", "Hay Bales are great for warmth."
        );
        ItemStack loot = new ItemStack(Material.FEATHER, 12);
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
    public static List<MobHead> Cows(){
        EntityType entityType = EntityType.COW;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("1940fbd2-5fac-11ed-9b6a-0242ac120002"),
                UUID.fromString("a17cc9bc-a482-48ae-8c4b-79690815f1ed"),
                UUID.fromString("675a7733-b924-46f7-ac80-ef31df6e8c33")
        );
        List<String> names = List.of(
                "Temperate Cow Head",
                "Warm Cow Head",
                "Cold Cow Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("http://textures.minecraft.net/texture/7dfa0ac37baba2aa290e4faee419a613cd6117fa568e709d90374753c032dcb0"));
            textures.add(new URL("http://textures.minecraft.net/texture/37eeb49335e03fe7911295075d6ae1e8a9b091fc6c4896fd2ccec8359c10b006"));
            textures.add(new URL("http://textures.minecraft.net/texture/e33221484d39f0d4ecdffa729567ce77fa8ce05b091db76dd5b079a20f7bf339"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Cow.Variant.TEMPERATE.getKeyOrThrow().toString(),
                Cow.Variant.WARM.getKeyOrThrow().toString(),
                Cow.Variant.COLD.getKeyOrThrow().toString()
        );
        List<String> lore = List.of(
                "Other Players can milk you", "with Right-Click!",
                "They can use a bucket, or get", "some straight from the source.",
                "Sneak-Right-Click to", "milk yourself with a bucket."
        );
        ItemStack loot = new ItemStack(Material.LEATHER, 12);
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }

    // Multi-Skin Hostiles ---------------------------------------------------------------------------------------------
    public static List<MobHead> ZombieVillagers(){
        EntityType entityType = EntityType.ZOMBIE_VILLAGER;
        Sound noteblockSound = Data.getEntityTypeNoteblockSound(entityType);
        List<UUID> uuids = List.of(
                UUID.fromString("766c8a9c-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c8cd6-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c8eac-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c90a0-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c9276-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c95dc-5fe3-11ed-9b6a-0242ac120002"),
                UUID.fromString("766c9c3a-5fe3-11ed-9b6a-0242ac120002")
        );
        List<String> names = List.of(
                "Desert Zombie Villager Head",
                "Jungle Zombie Villager Head",
                "Plains Zombie Villager Head",
                "Savanna Zombie Villager Head",
                "Snow Zombie Villager Head",
                "Swamp Zombie Villager Head",
                "Taiga Zombie Villager Head"
        );
        List<URL> textures = new ArrayList<>();
        try {
            textures.add(new URL("https://textures.minecraft.net/texture/41225ae82e4918eb84f4687fe97a83b291515e7a56e55499c8b046aed2d6e182"));
            textures.add(new URL("https://textures.minecraft.net/texture/649a46275dec0c247df986dfb4b351d289f0242b5fcd620daae113725720c7c9"));
            textures.add(new URL("https://textures.minecraft.net/texture/c45c11e0327035649ca0600ef938900e25fd1e38017422bc9740e4cda2cba892"));
            textures.add(new URL("https://textures.minecraft.net/texture/ab24ec3998250f9508361039182266687103b894ec8b94883feacd3c351db506"));
            textures.add(new URL("https://textures.minecraft.net/texture/ef12b7b53001ef851719fdf8c088de03b11d4ae43f6ff53ac5a267d24df1dc60"));
            textures.add(new URL("https://textures.minecraft.net/texture/22cca2d9f0aef42cc3086415e1419560f34334b167bebfefecc43492cb141789"));
            textures.add(new URL("https://textures.minecraft.net/texture/279862124fcf95aece708624d40a4fe4658351113e436524eb5f20f8fe5e66b6"));
        }catch (MalformedURLException e){
            System.out.println("Texture unable to load for " + entityType);
        }
        List<String> variants = List.of(
                Villager.Type.DESERT.getKeyOrThrow().getKey(),
                Villager.Type.JUNGLE.getKeyOrThrow().getKey(),
                Villager.Type.PLAINS.getKeyOrThrow().getKey(),
                Villager.Type.SAVANNA.getKeyOrThrow().getKey(),
                Villager.Type.SNOW.getKeyOrThrow().getKey(),
                Villager.Type.SWAMP.getKeyOrThrow().getKey(),
                Villager.Type.TAIGA.getKeyOrThrow().getKey()
        );
        ItemStack loot = HeadLootItemStack.villagerLoot();
        List<String> lore = List.of(
                "Gain Slowness I and Hunger I.", "Your hunger will never", "drop below 19/20."
        );
        List<MobHead> heads = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            UUID uuid = uuids.get(i);
            String name = names.get(i);
            URL texture = textures.get(i);
            String variant = variants.get(i);
            ItemStack head = HeadItemStack.customHead(name,uuid,texture,lore,entityType);
            heads.add(new MobHead(uuid,name,entityType,head,loot,lore,variant,null,noteblockSound));
        }
        return heads;
    }
}
