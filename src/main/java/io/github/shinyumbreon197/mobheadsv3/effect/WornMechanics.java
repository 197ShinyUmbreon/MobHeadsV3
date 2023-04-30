package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.FrogHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.effect.PotionFX.applyPotionEffect;
import static io.github.shinyumbreon197.mobheadsv3.tool.EffectUtil.*;

public class WornMechanics {

    private static final Map<LivingEntity, MobHead> cooldownMap = new HashMap<>();

    private static boolean onCooldown(LivingEntity livingEntity, MobHead mobHead){
        return cooldownMap.containsKey(livingEntity) && cooldownMap.get(livingEntity).equals(mobHead);
    }
    private static void setCooldown(LivingEntity livingEntity, MobHead mobHead, int ticks){
        if (mobHead == null)return;
        cooldownMap.put(livingEntity, mobHead);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (cooldownMap.containsKey(livingEntity)){
                    cooldownMap.remove(livingEntity, mobHead);
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), ticks);
    }

    public static void runHeadTickMechanics(List<LivingEntity> headed){
        for (LivingEntity livingEntity:headed){
            MobHead mobHead = HeadUtil.getMobHeadFromEntity(livingEntity);
            if (mobHead == null)return;
            EntityType entityType = mobHead.getEntityType();
            switch (entityType){
                default -> {}
                case DOLPHIN -> {regenerateAir(livingEntity);}
                case COD, SALMON, TROPICAL_FISH -> {regenerateAir(livingEntity);}
                case PUFFERFISH -> {regenerateAir(livingEntity);}
                case TURTLE -> {regenerateAir(livingEntity);}
                case SQUID -> {regenerateAir(livingEntity);}
                case SKELETON_HORSE -> {regenerateAir(livingEntity);}
                case GLOW_SQUID -> {regenerateAir(livingEntity);}
                case TADPOLE -> {regenerateAir(livingEntity);}
                case DROWNED -> {regenerateAir(livingEntity);}
                case AXOLOTL -> {regenerateAir(livingEntity);}
                case FROG -> {regenerateAir(livingEntity); if (livingEntity instanceof Player)frogGlowEdible((Player) livingEntity);}
                case ENDERMAN -> {damageFromWater(livingEntity);}
                case GUARDIAN, ELDER_GUARDIAN -> {regenerateAir(livingEntity);}
            }
        }
    }

    //Tick Triggered ---------------------------------------------------------------------------------------
    private static void regenerateAir(LivingEntity livingEntity){
        int maxAir = livingEntity.getMaximumAir();
        int airRemaining = livingEntity.getRemainingAir();
        int newAir = airRemaining + 8;
        if (newAir > maxAir) newAir = maxAir;
        livingEntity.setRemainingAir(newAir);
    }
    private static void damageFromWater(LivingEntity livingEntity){
        if (isExposedToWater(livingEntity)) livingEntity.damage(1);
    }
    private static final Map<Player, List<LivingEntity>> frogEdibleMapList = new HashMap<>();
    private static void frogGlowEdible(Player player){
        //System.out.println("frogGlowEdible"); //debug
        List<Entity> nearby = player.getNearbyEntities(10,10,10);

        for (Entity entity:nearby){
            if (!entity.equals(player) && entity instanceof LivingEntity && frogIsEdible(entity)){
                //System.out.println("is edible!"); //debug
                //Packets.frogEdibleUpdatePacket1(player, (LivingEntity) entity);
            }
        }

//        List<LivingEntity> frogList;
//        if (frogEdibleMapList.containsKey(player)){
//            frogList = new ArrayList<>(frogEdibleMapList.get(player));
//        }else frogList = new ArrayList<>();
//
//        for (LivingEntity livingEntity:frogList){
//            if (!nearby.contains(livingEntity)){
//
//            }
//        }
//
//        for (Entity entity:nearby){
//            boolean onList = frogList.contains(entity);
//            if (!(entity instanceof LivingEntity) || entity.equals(player))continue;
//            if (!onList && frogIsEdible(entity)){
//                System.out.println("!onList && isEdible"); //debug
//                Packets.frogEdibleUpdatePacket(player, (LivingEntity) entity);
//                frogList.add((LivingEntity) entity);
//            }else {
//                Packets.frogEdibleRemovePacket(player, (LivingEntity) entity);
//                frogList.remove((LivingEntity) entity);
//            }
//        }
//        frogEdibleMapList.put(player,frogList);
    }

    //Event Triggered --------------------------------------------------------------------------------------
    //Generic ----------------------------------------------------------------------------------------------
    public static void endermanTeleport(Entity entity){
        if (entity instanceof LivingEntity && entity.isDead())return;
        Location eLoc = entity.getLocation();
        Location dest = randomTeleportLoc(eLoc, entity, 8, 1, true);
        if (dest != null){
            entity.teleport(dest, PlayerTeleportEvent.TeleportCause.COMMAND);
            entity.setVelocity(new Vector(0,0,0));
            World world = eLoc.getWorld();
            if (world == null)return;
            new BukkitRunnable(){
                @Override
                public void run() {
                    AVFX.playEndermanTeleportSound(eLoc);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(), 1);
        }
    }

    //PlayerTeleportEvent ---------------------------------------------------------------------------------------
    public static void endermanRegeneratePearl(Player player){
        Random random = new Random();
        if (player.getGameMode().equals(GameMode.SURVIVAL) && random.nextInt(0,10) != 0){
            Item pearlItem = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.ENDER_PEARL, 1));
            pearlItem.setPickupDelay(0);
            pearlItem.setThrower(player.getUniqueId());
            AVFX.playEndermanRegeneratePearlSound(player.getLocation());
        }
    }

    //EntityDamagedByEntityEvent --------------------------------------------------------------------------
    public static void gainEffectsOnDamagedByEntity(LivingEntity damaged, EntityType headType){
        if (damaged.isDead())return;
        switch(headType){
            default -> {}
            case RABBIT -> {PotionFX.applyPotionEffect(damaged,PotionEffectType.SPEED, 10*20,1, false);}
            case AXOLOTL -> {PotionFX.applyPotionEffect(damaged, PotionEffectType.REGENERATION, 5*20, 0, true);}
        }
    }

    public static void summonReinforcements(LivingEntity defender, LivingEntity attacker, EntityType summonType){
        if (defender.isDead())return;
        Summon.summon(defender, attacker, summonType);
    }

    public static void llamaSpit(LivingEntity defender, LivingEntity attacker){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (defender == null || attacker == null || defender.isDead() || attacker.isDead() || (defender instanceof Player && ((Player) defender).isSneaking()))return;
                //Vector direction = defender.getLocation().getDirection();
                Location origin = defender.getEyeLocation().add(0,-0.5, 0);
                World world = defender.getWorld();
                LlamaSpit llamaSpit = (LlamaSpit) world.spawnEntity(origin,EntityType.LLAMA_SPIT);
                PersistentDataContainer data = llamaSpit.getPersistentDataContainer();
                data.set(MobHeadsV3.getPluginNSK(),PersistentDataType.STRING, defender.getUniqueId().toString());
                double distance = origin.toVector().distance(attacker.getEyeLocation().toVector());
                if (distance < 2) distance = 2;
                llamaSpit.setVelocity(projectileVector(
                        defender.getEyeLocation().add(0,defender.getEyeHeight()*-0.3, 0),
                        attacker.getEyeLocation().add(0,attacker.getEyeHeight()*-0.3, 0),
                        distance*0.2)
                );
                llamaSpit.setShooter(defender);
                AVFX.playLlamaSpitSound(origin);
                world.playSound(origin, Sound.ENTITY_LLAMA_SPIT,0.8F, 1.0F);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),5);
    }

    public static boolean goatInvuln(EntityDamageEvent.DamageCause cause, LivingEntity damaged){
        if (!(damaged instanceof Player))return false;
        List<EntityDamageEvent.DamageCause> causes = List.of(EntityDamageEvent.DamageCause.CONTACT, EntityDamageEvent.DamageCause.ENTITY_ATTACK);
        Vector velocity = damaged.getVelocity();
        double speed = Math.abs(velocity.getX()) + Math.abs(velocity.getZ());
        boolean invulnerable = speed > 0.2 && causes.contains(cause) && goatRammingPlayers.contains((Player) damaged);
        if (invulnerable) AVFX.playGoatInvulnerableSound(damaged.getLocation());
        return invulnerable;
    }

    private static final Map<LivingEntity, List<Mob>> axolotlAttackMap = new HashMap<>();
    private static void addToAxolotlMap(LivingEntity damaged, Mob attacker){
        List<Mob> mobs = new ArrayList<>();
        if (isOnAxolotlMap(damaged, attacker)) mobs.addAll(axolotlAttackMap.get(damaged));
        if (!mobs.contains(attacker)) mobs.add(attacker);
        axolotlAttackMap.put(damaged, mobs);
        new BukkitRunnable(){
            @Override
            public void run() {
                removeFromAxolotlMap(damaged, attacker);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 2*20);
    }
    private static void removeFromAxolotlMap(LivingEntity damaged, Mob attacker){
        if (!isOnAxolotlMap(damaged, attacker))return;
        List<Mob> mobs = new ArrayList<>(axolotlAttackMap.get(damaged));
        mobs.remove(attacker);
        if (mobs.size() == 0){
            axolotlAttackMap.remove(damaged);
        }else axolotlAttackMap.put(damaged, mobs);
    }
    private static boolean isOnAxolotlMap(LivingEntity damaged, Mob attacker){
        if (!axolotlAttackMap.containsKey(damaged))return false;
        for (Mob mob:axolotlAttackMap.get(damaged)){
            if (mob.equals(attacker))return true;
        }
        return false;
    }
    public static void axolotlRemoveTarget(LivingEntity damaged, LivingEntity attacker){
        if (!(attacker instanceof Mob))return;
        Mob mob = (Mob) attacker;
        LivingEntity target = mob.getTarget();
        if (isOnAxolotlMap(damaged, mob))return;
        addToAxolotlMap(damaged, mob);
        if (target != null && target.equals(damaged)){
            mob.setTarget(null);
        }
    }

    //PlayerStatisticIncrementEvent -------------------------------------------------------------------------
    public static void frogJump(Player player){
        if (player == null)return;
        if (!player.isSneaking())return;
        Vector velocity = player.getVelocity();
        Vector direction = player.getLocation().getDirection();
        double x = velocity.getX() + (direction.getX() * 0.8);
        double y = (velocity.getY() * 2) + (direction.getY() * 0.1);
        double z = velocity.getZ() + (direction.getZ() * 0.8);
        velocity.setX(x);
        velocity.setY(y);
        velocity.setZ(z);
        player.setVelocity(velocity);
        AVFX.playFrogJumpEffect(player.getLocation());
    }

    private static final List<Player> goatRamCooldown = new ArrayList<>();
    private static final List<Player> goatRammingPlayers = new ArrayList<>();
    private static final Map<Player, List<LivingEntity>> goatRamHitMap = new HashMap<>();
    public static void goatRam(Player player){
        if (player == null || !player.isSneaking())return;
        boolean onList = goatRamCooldown.contains(player);
        if (!onList){
            goatRamCooldown.add(player);
            if (!goatRammingPlayers.contains(player)) goatRammingPlayers.add(player);
            new BukkitRunnable(){
                @Override
                public void run() {
                    goatRamCooldown.remove(player);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(), 20);
            ItemStack chest = player.getInventory().getChestplate();
            boolean hasElytra = chest != null && chest.getType().equals(Material.ELYTRA);
            player.setGliding(hasElytra);
            Vector velocity = player.getVelocity();
            Location location = player.getLocation();
            //if (player.isGliding()) location = location.add(location.getDirection().multiply(0.5));
            World world = location.getWorld();
            if (world == null)return;
            Vector direction = location.getDirection();
            location.setY(Math.floor(location.getY()));
            //player.teleport(location);
            double x = velocity.getX() + (direction.getX());
            double y;
            if (player.isGliding()){
                y = 0.3;
            }else y = 0.1;
            double z = velocity.getZ() + (direction.getZ());
            velocity.setX(x);
            velocity.setY(y);
            velocity.setZ(z);
            player.setVelocity(velocity);
            AVFX.playGoatRamBeginEffect(location);
            new BukkitRunnable(){
                @Override
                public void run() {
                    //System.out.println("tick.."+new Random().nextInt()); //debug
                    double speed = Math.abs(player.getVelocity().getX()) + Math.abs(player.getVelocity().getZ());
                    //System.out.println("speed: "+speed); //debug
                    if (!goatRamCooldown.contains(player) && !player.isGliding()){
                        goatRammingPlayers.remove(player);
                        cancel();
                        return;
                    }
                    Location scanLoc = player.getLocation();
                    double playerVelMult;
                    if (hasElytra){
                        playerVelMult = 0.9;
                    }else playerVelMult = 0.7;
                    Vector playerVelocity = player.getVelocity().multiply(playerVelMult);

                    Vector hitVelocity = player.getVelocity().setY(0.5);
                    if (speed > 0.3) AVFX.playGoatRamTrail(player.getLocation());

                    List<Entity> inFront = new ArrayList<>(world.getNearbyEntities(scanLoc, 1, 2, 1));
                    for (Entity entity:inFront){
                        if (entity.equals(player))continue;
                        if (!(entity instanceof LivingEntity))continue;
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity.isDead())continue;
                        if (goatRamHitMap.containsKey(player) && goatRamHitMap.get(player).contains(livingEntity))continue;

                        int velocityMult;
                        if (player.isGliding()){
                            velocityMult = 9;
                        }else velocityMult = 3;
                        hitVelocity.setX(hitVelocity.getX()*velocityMult).setZ(hitVelocity.getZ()*velocityMult);
                        livingEntity.setVelocity(hitVelocity);
                        player.setVelocity(playerVelocity);

                        double maxDamage;
                        if (player.isGliding()){
                            maxDamage = 8;
                        }else maxDamage = 4;
                        double damage = Math.floor(speed * 4.5);
                        if (damage > maxDamage) damage = maxDamage;
                        if (damage != 0) livingEntity.damage(damage, player);

                        List<LivingEntity> hits;
                        if (!goatRamHitMap.containsKey(player)){
                            hits = new ArrayList<>();
                        }else hits = goatRamHitMap.get(player);
                        if (!hits.contains(livingEntity)) hits.add(livingEntity);
                        goatRamHitMap.put(player, hits);

                        AVFX.playGoatRamHitSound(livingEntity.getLocation());
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                List<LivingEntity> list;
                                if (!goatRamHitMap.containsKey(player)){
                                    return;
                                }else list = goatRamHitMap.get(player);
                                list.removeAll(hits);
                                goatRamHitMap.put(player, list);
                            }
                        }.runTaskLater(MobHeadsV3.getPlugin(), 20);
                    }
                    int height;
                    if (player.isGliding()){
                        height = 2;
                    }else height = 3;
                    boolean hit = false;
                    List<Material> brokenMats = new ArrayList<>();
                    List<Location> brokenLocs = new ArrayList<>();
                    for (Block block: getFacingBlocks(player.getLocation(),player.getFacing(), height, 0)){
                        //System.out.println("block: "+block); //debug
                        if (Data.goatBreakable.contains(block.getType()) && speed > 0.3){
                            //System.out.println("isBreakable"); //debug
                            hit = true;
                            brokenMats.add(block.getType());
                            brokenLocs.add(block.getLocation().add(0.5, 0.5, 0.5));
                            block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                        }
                    }
                    if (hit){
                        player.setVelocity(playerVelocity.multiply(-0.5));
                        goatRammingPlayers.remove(player);
                        AVFX.playGoatBreakBlockSound(player.getLocation(), brokenMats, brokenLocs);
                        cancel();
                        return;
                    }
                }
            }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);
        }
    }

    //EntityDamageEvent ------------------------------------------------------------------------------------
    public static boolean frogFallDamage(EntityDamageEvent e){
        boolean frogFallDamage = e.getFinalDamage() > 3;
        if (frogFallDamage){
            double newDamage;
            newDamage = e.getFinalDamage() - 4;
            if (newDamage <= 0){return false;}
            e.setDamage(newDamage);
        }
        return frogFallDamage;
    }

    public static void endermanDamageEffect(LivingEntity livingEntity, EntityDamageEvent.DamageCause cause){
        if (livingEntity.isDead())return;
        List<EntityDamageEvent.DamageCause> damageCauses = List.of(
                EntityDamageEvent.DamageCause.SUICIDE, EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.FLY_INTO_WALL, EntityDamageEvent.DamageCause.POISON,
                EntityDamageEvent.DamageCause.STARVATION, EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.SUFFOCATION, EntityDamageEvent.DamageCause.VOID
        );
        if (damageCauses.contains(cause))return;
        endermanTeleport(livingEntity);
    }

    //ProjectileHitEvent --------------------------------------------------------------------------------
    public static void projectileHitWearer(ProjectileHitEvent e, MobHead mobHead){
        Entity hitEntity = e.getHitEntity();
        assert hitEntity != null;
        Projectile projectile = e.getEntity();
        ProjectileSource source = projectile.getShooter();
        PersistentDataContainer data = projectile.getPersistentDataContainer();
        EntityType headType = mobHead.getEntityType();
        switch (headType){
            default -> {}
            case ENDERMAN -> {
                e.setCancelled(true);
                endermanTeleport(hitEntity);
            }
            case LLAMA,TRADER_LLAMA -> {
                if (source != null && data.has(MobHeadsV3.getPluginNSK(),PersistentDataType.STRING)){
                    String stringUUID = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
                    if (stringUUID == null)return;
                    LivingEntity shooter = Bukkit.getPlayer(UUID.fromString(stringUUID));
                    e.setCancelled(source.equals(shooter));
                }
            }
        }
    }
    public static void wearerProjectileHitEntity(ProjectileHitEvent e, MobHead shooterMobHead, LivingEntity shooter, LivingEntity target){
        EntityType headType = shooterMobHead.getEntityType();
        switch (headType){
            case LLAMA -> {
                target.damage(4, shooter);
                PotionFX.applyPotionEffect(target,PotionEffectType.SLOW,5*20, 0, true);
            }
        }
    }

    //PlayerInteractAtEntityEvent --------------------------------------------------------------------------
    //Send the player glowing packets for every edible entity within a block radius
    public static void frogEatEntity(PlayerInteractAtEntityEvent e, MobHead mobHead){
        if (!e.getHand().equals(EquipmentSlot.HAND))return;
        Player player = e.getPlayer();
        if (player.isSneaking())return;
        Entity entity = e.getRightClicked();

        boolean cooldown = onCooldown(player, mobHead);
        if (cooldown)return;
        if (frogIsBlacklisted(entity)) return;
        setCooldown(player, mobHead, 20);

        boolean edible = frogIsEdible(entity);
        boolean onFire = false;
        int gainedHealth = 0;
        int gainedFoodLv = 0;
        float gainedSaturation = 0F;
        int gainedAir = 0;

        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            if (!edible){
                livingEntity.setVelocity(projectileVector(livingEntity.getLocation(),player.getLocation().add(0,0.5,0),0.4));
            }else{
                if (livingEntity instanceof WaterMob) gainedAir = 100;
                onFire = livingEntity.getFireTicks() > 0 || livingEntity.isVisualFire();
                AttributeInstance maxHealthAttribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if (maxHealthAttribute == null)return;
                double maxHealth = maxHealthAttribute.getValue();
                gainedHealth = (int) Math.ceil(maxHealth * 0.2);
                gainedFoodLv = (int) Math.ceil(maxHealth * 0.3);
                gainedSaturation = (float) Math.ceil(maxHealth * 0.5);
                PersistentDataContainer data = entity.getPersistentDataContainer();
                data.set(FrogHead.frogFoodKey, PersistentDataType.STRING, player.getUniqueId().toString());
                livingEntity.setVelocity(projectileVector(livingEntity.getLocation().add(0,1,0),player.getEyeLocation(),1.8));
                livingEntity.setHealth(0);
            }
        }
        if ((entity instanceof Projectile) && edible){
            Projectile projectile = (Projectile) entity;
            switch (projectile.getType()){
                default -> {}
                case SHULKER_BULLET -> {
                    gainedFoodLv = 2;
                    gainedSaturation = 4;
                }
            }
        }
        player.swingOffHand();
        AVFX.playFrogTongueSound(entity.getLocation());
        if (!edible)return;
        switch (entity.getType()){
            case CREEPER -> {
                assert entity instanceof Creeper;
                frogEatCreeper(player, (Creeper) entity);
            }
            case ENDERMITE -> {
                //fxMultipleTeleportSickness(player);
            }
        }
        List<PotionEffect> gainedEffects = frogEatenEffects(entity);
        if (gainedHealth > 0){
            double health = player.getHealth();
            AttributeInstance pMaxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (pMaxHealthAttribute == null)return;
            double pMaxHealth = pMaxHealthAttribute.getValue();
            health = health + gainedHealth;
            if (health > pMaxHealth) health = pMaxHealth;
            player.setHealth(health);
        }
        if (gainedFoodLv > 0){
            int foodLv = player.getFoodLevel();
            foodLv = foodLv + gainedFoodLv;
            if (foodLv > 20) foodLv = 20;
            player.setFoodLevel(foodLv);
        }
        if (gainedSaturation > 0F){
            float saturationLv = player.getSaturation();
            saturationLv = saturationLv + gainedSaturation;
            if (saturationLv > 20F) saturationLv = 20F;
            player.setSaturation(saturationLv);
        }
        if (gainedAir > 0){
            int air = player.getRemainingAir();
            air = air + gainedAir;
            if (air > player.getMaximumAir()) air = player.getMaximumAir(); //if > 300
            player.setRemainingAir(air);
        }
        for (PotionEffect pe:gainedEffects){
            applyPotionEffect(player,pe.getType(),pe.getDuration(), pe.getAmplifier(), true, true);
        }
        boolean finalOnFire = onFire;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (finalOnFire) player.setFireTicks(60);
                Sound deathSound = null;
                if (entity instanceof LivingEntity){
                    LivingEntity le = (LivingEntity) entity;
                    deathSound = le.getDeathSound();
                }
                AVFX.playFrogEatenSounds(player.getEyeLocation(), deathSound);
                entity.remove();
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 5);
    }
    private static boolean frogIsEdible(Entity entity){
        if (entity instanceof Projectile){
            Projectile projectile = (Projectile) entity;
            if (projectile.getType().equals(EntityType.SHULKER_BULLET)) return true;
        }
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            AttributeInstance maxHealthAttribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute == null)return false;
            double maxHealth = maxHealthAttribute.getValue();
            if (livingEntity.getHealth() <= 6 || livingEntity.getHealth() <= maxHealth*0.2) return true;
            switch (livingEntity.getType()){
                case SLIME -> {
                    assert entity instanceof Slime;
                    Slime slime = (Slime) entity;
                    if (slime.getSize() == 1) return true;
                }
                case MAGMA_CUBE -> {
                    assert entity instanceof MagmaCube;
                    MagmaCube magmaCube = (MagmaCube) entity;
                    if (magmaCube.getSize() == 1) return true;
                }
                case CREEPER -> {
                    assert entity instanceof Creeper;
                    Creeper creeper = (Creeper) entity;
                    if (creeper.getHealth() <= maxHealth*0.5) return true;
                }
                case COD, SALMON, TROPICAL_FISH, RABBIT, PUFFERFISH -> {return true;}
            }
        }
        return false;
    }
    private static List<PotionEffect> frogEatenEffects(Entity entity){
        List<PotionEffect> effects = new ArrayList<>();
        switch (entity.getType()){
            case PLAYER -> {
                Player eatenPlayer = (Player) entity;
                for (PotionEffect pe: eatenPlayer.getActivePotionEffects()){
                    int duration = pe.getDuration();
                    if (duration > 6000) duration = 6000;
                    effects.add(new PotionEffect(pe.getType(), duration, pe.getAmplifier(), false));
                }
            }
            case CHICKEN, PARROT -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
            }
            case MAGMA_CUBE, GHAST, STRIDER -> {
                effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
            }
            case GLOW_SQUID, PHANTOM -> {
                effects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 3000, 0, false));
            }
            case BLAZE -> {
                effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
            }
            case FROG, RABBIT -> {
                effects.add(new PotionEffect(PotionEffectType.JUMP, 3000, 1, false));
            }
            case DOLPHIN -> {
                effects.add(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 6000, 0, false));
            }
            case VEX -> {
                effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1, false));
                effects.add(new PotionEffect(PotionEffectType.SPEED, 2400, 0, false));
            }
            case IRON_GOLEM -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW, 600, 1, false));
                effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1, false));
            }
            case WARDEN -> {
                effects.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 3000, 0, false));
            }
            case RAVAGER -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW, 600, 0, false));
                effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false));
                effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0, false));
            }
            case PUFFERFISH, BEE -> {
                effects.add(new PotionEffect(PotionEffectType.POISON, 120, 0, false));
                effects.add(new PotionEffect(PotionEffectType.CONFUSION, 180, 0, false));
            }
            case GIANT -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW, 2400, 4, false));
            }
            case WITHER_SKELETON -> {
                effects.add(new PotionEffect(PotionEffectType.WITHER, 60, 0, false));
            }
            case SHULKER -> {
                effects.add(new PotionEffect(PotionEffectType.LEVITATION, 180, 0, false));
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
            }
            case SHULKER_BULLET -> {
                effects.add(new PotionEffect(PotionEffectType.LEVITATION, 40, 0, false));
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 1500, 0, false));
            }
        }
        return effects;
    }
    private static boolean frogIsBlacklisted(Entity entity){
        List<EntityType> blacklistedTypes = Arrays.asList(EntityType.ENDER_DRAGON, EntityType.WITHER);
        if (blacklistedTypes.contains(entity.getType()))return true;
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.isDead())return true;
            if (livingEntity instanceof Tameable){
                Tameable tameable = (Tameable) livingEntity;
                return tameable.isTamed();
            }
        }
        return false;
    }
    private static void frogEatCreeper(Player player, Creeper creeper){
        new BukkitRunnable(){
            @Override
            public void run() {
                AVFX.playFrogEatCreeperEffect(player.getLocation().add(0,1,0));
                List<Entity> nearby = player.getNearbyEntities(8, 5, 8);
                player.damage(2, creeper);
                player.setVelocity(player.getVelocity().add(new Vector(0,0.8,0)));
                for (Entity entity:nearby){
                    if (entity == null)continue;
                    if (entity instanceof LivingEntity){
                        LivingEntity le = (LivingEntity) entity;
                        if (le instanceof Tameable){
                            Tameable tameable = (Tameable) le;
                            if (tameable.isTamed()){
                                AnimalTamer tamer = tameable.getOwner();
                                if (tamer != null && tamer.equals(player))continue;
                            }
                        }
                        if (le.equals(player))continue;
                        if (le.isDead())continue;
                        le.setVelocity(projectileVector(player.getLocation(), le.getEyeLocation(),4));
                        le.damage(5, player);
                    }
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 5);
    }

    //EntityTargetLivingEntityEvent --------------------------------------------------------------------------
    private static final Map<UUID, Player> endermanAggroMap = new HashMap<>();
    public static void addEndermanAggroMap(UUID entID, Player player){
        endermanAggroMap.put(entID, player);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (endermanAggroMap.containsKey(entID) && endermanAggroMap.get(entID).equals(player)){
                    endermanAggroMap.remove(entID);
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 60*20);
    }

    public static void onTargetWearer(EntityTargetLivingEntityEvent e, LivingEntity wearer, Mob targeting, MobHead mobHead){
        EntityType headType = mobHead.getEntityType();
        boolean sameType = targeting.getType().equals(headType);
        EntityTargetEvent.TargetReason reason = e.getReason();
        //System.out.println("Targeting: "+targeting.getType()+" "+targeting.getEntityId()+" Targeted: "+wearer.getType()+" "+wearer.getEntityId()+" Reason: "+reason.toString()); //debug
        if (reason.equals(EntityTargetEvent.TargetReason.FORGOT_TARGET))return;
        List<EntityTargetEvent.TargetReason> closestTargetReasons = List.of(
                EntityTargetEvent.TargetReason.CLOSEST_ENTITY,
                EntityTargetEvent.TargetReason.CLOSEST_PLAYER,
                EntityTargetEvent.TargetReason.RANDOM_TARGET
        );
        if (headType.equals(EntityType.AXOLOTL)){
            e.setCancelled(isOnAxolotlMap(wearer, targeting));
        }
        if (sameType && headType.equals(EntityType.ENDERMAN) && wearer instanceof Player){
            Enderman enderman = (Enderman) targeting;
            boolean onMap = endermanAggroMap.containsKey(enderman.getUniqueId()) && endermanAggroMap.get(enderman.getUniqueId()).equals(wearer);
            if (!onMap){
                e.setCancelled(true);
            }
        }else if (closestTargetReasons.contains(reason) && sameType){
            e.setCancelled(true);
        }else if (reason.equals(EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) && sameType){
            List<Entity> nearby = targeting.getNearbyEntities(8, 5, 8);
            for (Entity entity:nearby){
                if (entity instanceof Mob && entity.getType().equals(targeting.getType())){
                    if (entity instanceof Tameable){
                        Tameable tameable = (Tameable) entity;
                        if (tameable.isTamed())continue;
                    }
                    Mob mob = (Mob) entity;
                    if (mob.getTarget() != null && mob.getTarget().equals(wearer))continue;
                    mob.setTarget(wearer);
                    AVFX.playImpostorParticles(mob.getEyeLocation());
                }
            }
        }

        //System.out.println("Canceled: "+e.isCancelled()); //debug
    }

}
