package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.*;
import io.github.shinyumbreon197.mobheadsv3.data.Data;
import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.tool.Serializer;
import io.github.shinyumbreon197.mobheadsv3.tool.StringBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.function.PotionEffectManager.*;

public class CreatureEvents {

    private static final MobHeadsV3 plugin = MobHeadsV3.getPlugin();

    // Global ----------------------------------------------------------------------------------------------------------
    private static final Set<LivingEntity> creatureWasGliding = new HashSet<>();
    public static boolean wasCreatureGliding(LivingEntity livingEntity){
        return creatureWasGliding.contains(livingEntity);
    }
    public static void removeFromCreatureWasGliding(LivingEntity livingEntity){
        creatureWasGliding.remove(livingEntity);
    }
    public static void addToCreatureWasGliding(LivingEntity livingEntity){
        creatureWasGliding.add(livingEntity);
    }
    private static final Set<EntityDamageEvent.DamageCause> fireDamageTypes = Set.of(
            EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.LAVA
    );
    private static final Set<EntityType> fireDamageResistantTypes = Set.of(
            EntityType.MAGMA_CUBE, EntityType.BLAZE, EntityType.WITHER_SKELETON,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.GHAST, EntityType.STRIDER, EntityType.ZOGLIN
    );
    public static void damageTypeResistance(EntityType headType, EntityDamageEvent ede){
        LivingEntity damaged = (LivingEntity) ede.getEntity();
        if (debug) System.out.println("noDamageTicks: " + damaged.getNoDamageTicks()); //debug
//        if (damaged.getNoDamageTicks() > 0){
//            ede.setCancelled(true);
//            return;
//        }
        boolean canceled = false;
        EntityDamageEvent.DamageCause damageCause = ede.getCause();
        if (Util.hasTakenAbilityDamage(damaged)) damageCause = EntityDamageEvent.DamageCause.CUSTOM;
        double incomingDamage = ede.getDamage();

        if (debug) System.out.println("damageTypeResistance() mobHead: " + headType + "\ndamageCause: " + damageCause); //debug
        switch (damageCause){
            case FIRE,FIRE_TICK,LAVA,PROJECTILE -> {
                if (fireDamageResistantTypes.contains(headType)){
                    if (damageCause.equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                        if (ede instanceof EntityDamageByEntityEvent){
                            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) ede;
                            Projectile projectile = (Projectile) edbee.getDamager();
                            if (!projectile.getType().equals(EntityType.SMALL_FIREBALL))return;
                        }
                    }
                    double damage = netherMobFireResistance(damaged, ede.getDamage());
                    if (damage == 0){
                        canceled = true;
                    }else ede.setDamage(damage);
                }
            }
        }
        switch (headType){
            case WITHER, WITHER_SKELETON -> {if (damageCause.equals(EntityDamageEvent.DamageCause.WITHER)) canceled = true;}
            case CAVE_SPIDER -> {if (damageCause.equals(EntityDamageEvent.DamageCause.POISON)) canceled = true;}
            case SLIME, MAGMA_CUBE -> {
                switch (damageCause){
                    case FALL -> {
                        double damage = slimeFallDamage((LivingEntity) ede.getEntity(), ede.getDamage());
                        if (damage == 0){
                            canceled = true;
                        }else ede.setDamage(damage);
                    }
                    case FLY_INTO_WALL -> {
                        canceled = true;
                        if (ede.getEntity() instanceof Player){
                            double damage = ede.getDamage();
                            slimeRicochet((Player) ede.getEntity(), damage, false);
                        }
                    }
                }
            }
            case FROG -> {
                if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){
                    double damage = (ede.getDamage() - 4) * 0.5;
                    if (damage <= 0){
                        canceled = true;
                    }else ede.setDamage(damage);
                }
            }
            case ENDER_DRAGON, GOAT -> {
                if (damageCause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)){
                    double damage = ede.getDamage() * 0.5;
                     if (damage < 1){
                    canceled = true;
                    }else ede.setDamage(damage);
                }else if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){
                    boolean wasGliding = wasCreatureGliding(damaged);
                    if (debug) System.out.println("wasGliding: " + wasGliding); //debug
                    if (wasGliding){
                        double damage = ede.getDamage() * 0.25;
                        if (damage < 1){
                            canceled = true;
                        }else ede.setDamage(damage);
                    }
                }
            }
            case CAT, OCELOT -> {
                if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){
                    double newDamage = incomingDamage * 0.2;
                    if (newDamage < 1){
                        canceled = true;
                    }else ede.setDamage(newDamage);
                }
            }
            case RABBIT -> {
                if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){
                    double damage = ede.getDamage() * 0.5;
                    if (damage < 1){
                        ede.setCancelled(true);
                    }else ede.setDamage(damage);
                }
            }
            case ENDERMITE -> {
                if (damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && ede instanceof EntityDamageByEntityEvent
                        && ((EntityDamageByEntityEvent)ede).getDamager() instanceof Enderman){
                    double damage = ede.getDamage()*0.5;
                    if (damage < 1){
                        ede.setCancelled(true);
                    }else ede.setDamage(damage);
                }
            }
            case FOX -> {
                if (damageCause.equals(EntityDamageEvent.DamageCause.FALL) || damageCause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)){
                    if (ede.getEntity() instanceof Player && CreatureEvents.foxIsPlayerPouncing((Player) ede.getEntity())) canceled = true;
                }else if (damageCause.equals(EntityDamageEvent.DamageCause.CONTACT)){
                    canceled = foxIsInBerryBush((LivingEntity) ede.getEntity());
                }
            }
        }
        ede.setCancelled(canceled);
    }
    public static void damageTypeReactions(EntityType damagedHeadType, EntityDamageEvent ede){
        LivingEntity target = (LivingEntity) ede.getEntity();
        EntityDamageEvent.DamageCause cause = ede.getCause();
        if (debug) System.out.println("damageCause: " + cause.toString()); //debug
        List<EntityDamageEvent.DamageCause> attackCauses = List.of(
                EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
                EntityDamageEvent.DamageCause.PROJECTILE, EntityDamageEvent.DamageCause.DRAGON_BREATH,
                EntityDamageEvent.DamageCause.CONTACT, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.MAGIC, EntityDamageEvent.DamageCause.SONIC_BOOM,
                EntityDamageEvent.DamageCause.THORNS
        );
        if (Util.hasTakenAbilityDamage(target)) cause = EntityDamageEvent.DamageCause.CUSTOM;
        switch (damagedHeadType){
            case RABBIT -> {
                if (attackCauses.contains(cause)) {
                    CreatureEvents.rabbitSpeed(target);
                }
            }
            case GOAT -> {
                if (!CreatureEvents.wasCreatureGliding(target) || !(target instanceof Player))return;
                Player player = (Player) target;
                if (cause.equals(EntityDamageEvent.DamageCause.FALL)){
                    goatBreakBlocks(player,BlockFace.DOWN,player.getLocation().getBlock(),null);
                }else if (cause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)){
                    Block targetBlock = player.getLocation().getBlock();
                    goatBreakBlocks(player,player.getFacing(),targetBlock,null);
                }
            }
            case AXOLOTL -> {if (attackCauses.contains(cause)) CreatureEvents.axolotlRegeneration(target);}
            case ENDERMAN -> {
                if ((cause.equals(EntityDamageEvent.DamageCause.CUSTOM) || attackCauses.contains(cause))){
                    CreatureEvents.endermanTeleportNearby(target,false);
                }
            }
        }
    }
    public static void applyAfflictionsToTarget(EntityType attackerType, LivingEntity target, boolean projectile){
        List<PotionEffect> afflictionEffects = getAfflictionPotionEffects(attackerType, projectile);
        runAfflictions(attackerType, target);
        addEffectsToEntity(target, afflictionEffects);
    }
    private static void runAfflictions(EntityType attackerType, LivingEntity target){
        switch (attackerType){
            case ELDER_GUARDIAN -> {if (target instanceof Player){runElderGuardianAfflictionFX(target);}}
            case SHULKER -> {AVFX.playShulkerAfflictionEffect(target.getLocation(), target.getEyeHeight(),false);}
            case STRAY -> {}
        }
    }

    public static void applyRetaliationEffects(EntityType defenderType, LivingEntity damaged, LivingEntity damager, boolean projectile){
        List<PotionEffect> retaliationEffects = getRetaliationPotionEffects(defenderType, projectile);
        addEffectsToEntity(damager, retaliationEffects);

        runRetaliations(defenderType, damaged, damager, projectile);
    }
    private static void runRetaliations(EntityType defenderType, LivingEntity damaged, LivingEntity damager, boolean projectile){
        switch (defenderType){
            case LLAMA, TRADER_LLAMA -> {llamaRetaliationSpit(damaged, damager);}
            case EVOKER -> evokerRetaliation(damaged, damager);
        }
    }

    public static void nearbyTargetImposter(Mob targeter, LivingEntity targeted, EntityType targetedHeadType){
        List<Entity> nearby = targeter.getNearbyEntities(10,5,10)
                .stream().filter(entity -> entity instanceof Mob).collect(Collectors.toList()
        );
        List<Mob> nearbyNeutral = new ArrayList<>(List.of(targeter));
        for (Entity ent:nearby){
            if (ent.equals(targeter))continue;
            boolean ng = Groups.neutralTarget(ent.getType(), targetedHeadType);
            if (!ng)continue;
            nearbyNeutral.add((Mob) ent);
        }
        for (Mob mob:nearbyNeutral){
            if (mob.getTarget() != null)continue;
            if (debug) System.out.println("for nearbyNeutral: " + mob.getName() + " target: " + targeted.getName()); //debug
            PersistentDataContainer mobData = mob.getPersistentDataContainer();
            if (mobData.has(Key.lastHeadedTarget, PersistentDataType.STRING)) continue;
            mobData.set(Key.lastHeadedTarget, PersistentDataType.STRING, targeted.getUniqueId().toString());
            mob.setTarget(targeted);
            AVFX.playImpostorParticles(mob.getEyeLocation());
        }
    }

    // Summons ---------------------------------------------------------------------------------------------------------
    public static void spawnSummon(EntityType headType, EntityDamageByEntityEvent edbee){
        Entity damaged = edbee.getEntity();
        if (!(damaged instanceof LivingEntity))return;
        LivingEntity damagedLivEnt = (LivingEntity) edbee.getEntity();
        Entity attacker = Util.getTrueAttacker(edbee.getDamager());
        if (!(attacker instanceof LivingEntity))return;
        if (damaged.equals(attacker))return;
        //Summon.spawnSummon(damagedLivEnt.getLocation(),headType,damagedLivEnt,attacker);
        Summon.createNewSummon(damagedLivEnt, (LivingEntity) attacker, headType);
    }

    // Horses (Horse, Donkey, Mule, Skeleton Horse, Zombie Horse) ------------------------------------------------------
    public static void horseStepUp(Player horse){
        // Attribute will be added in 1.20.5
    }

    // Evoker ----------------------------------------------------------------------------------------------------------
    private static final int evokerCooldown = 2*20;
    private static final Map<LivingEntity, Integer> evokerRetaliationCooldownMap = new HashMap<>();
    private static boolean evokerIsOnCooldown(LivingEntity evoker){
        return evokerRetaliationCooldownMap.containsKey(evoker);
    }
    public static void evokerRetaliation(LivingEntity evoker, LivingEntity target){
        if (evokerIsOnCooldown(evoker))return;
        evokerRetaliationCooldownMap.put(evoker, evokerCooldown);
        boolean player = evoker instanceof Player;
        new BukkitRunnable(){ // HUD
            @Override
            public void run() {
                int timer = evokerRetaliationCooldownMap.getOrDefault(evoker, 0);
                if (!evokerIsOnCooldown(evoker) || timer == 0){
                    cancel();
                    evokerRetaliationCooldownMap.remove(evoker);
                    if (player) Hud.progressBarEnd((Player) evoker);
                    return;
                }
                timer--;
                evokerRetaliationCooldownMap.put(evoker, timer);
                if (player) Hud.progressBar(
                        (Player) evoker, evokerCooldown, timer, true, "Fangs Cooldown:", false
                );
            }
        }.runTaskTimer(plugin,0,1);
        Vector origin = evoker.getLocation().toVector();
        double x = origin.getX();
        double y = origin.getY();
        double z = origin.getZ();
        Vector destination = target.getLocation().toVector();
        Vector direction = destination.clone().subtract(origin).normalize();//.multiply(0.5);
        double xDir = direction.getX();
        double yDir = direction.getY();
        double zDir = direction.getZ();
        if (debug) System.out.println("direction: " + direction);
        Vector position = origin.clone();
        double distance = origin.clone().setY(0).distance(destination.clone().setY(0)) + 3;
        if (debug) System.out.println("distance: " + distance); //debug
        if (distance > 20) distance = 20;
        List<Vector> points = new ArrayList<>();
        for (double i = 0; i <= distance; i = i + 1.2) {
            if (i == 0) points.add(position);
            double multiplier = i + 1;
            points.add(new Vector(x + (xDir * multiplier), destination.getY(), z + (zDir * multiplier)));
        }
        if (debug) System.out.println("points.size(): " + points.size()); //debug
        if (debug) System.out.println("points: " + points); //debug
        World world = evoker.getWorld();
        int delay = 0;
        for (Vector point:points){
            new BukkitRunnable(){
                @Override
                public void run() {
                    Block nearestSurface = Util.getNearestVerticalSurface(
                            new Location(world, point.getX(), point.getY(), point.getZ()),10, true, true
                    );
                    if (nearestSurface == null){
                        if (debug) System.out.println("nearestSurface == null");
                        return;
                    }
                    Location location = new Location(world,point.getX(), nearestSurface.getY() + 1, point.getZ());
                    if (debug) System.out.println("fang spawn location: " + location); //debug
                    EvokerFangs fangs = (EvokerFangs) world.spawnEntity(location,EntityType.EVOKER_FANGS);
                    fangs.setOwner(evoker);
                }
            }.runTaskLater(plugin, delay);
            delay = delay + 2;
        }
    }

    // Chested Animals (Donkey, Mule, Llama, Trader-Llama) -------------------------------------------------------------
    private static final Set<EntityType> chestedEntityTypes = Set.of(
            EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA, EntityType.TRADER_LLAMA
    );
    private static final Set<Material> chestedWhitelist = Set.of(
            Material.CHEST, Material.TRAPPED_CHEST, Material.BARREL//, Material.SHULKER_BOX
    );
    private static final NamespacedKey chestedKey = new NamespacedKey(plugin, Key.chestedKeyString);
    private static final Set<LivingEntity> chestedHolders = new HashSet<>();
    private static void chestedAffectRiddenVehicle(LivingEntity chested){
        Entity entity = chested.getVehicle();
        if (!(entity instanceof Vehicle))return;
        Vehicle vehicle = (Vehicle) entity;
        EntityType vehicleType = vehicle.getType();
        if (vehicle instanceof LivingEntity){
            LivingEntity livingVehicle = (LivingEntity) vehicle;
            chestedAddHolder(livingVehicle);
        }else{
            vehicle.setVelocity(new Vector(0,-0.2,0));
        }
    }
    public static void chestedInterruptGliding(LivingEntity chested){
        if (!chestedHolders.contains(chested))return;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!chested.isGliding() || !chestedHolders.contains(chested) || chestedAmountCarried(chested) == 0)return;
                chested.setGliding(false);
                if (chested instanceof Player){
                    MobHeadsV3.messagePlayer((Player)chested, ChatColor.RED + "You are too heavy to glide.");
                }
                AVFX.playChestedItemExplode(chested.getEyeLocation());
            }
        }.runTaskLater(plugin, 10);
    }
    public static void chestedWatchDroppedContainer(Item containerItem){
        new BukkitRunnable(){
            @Override
            public void run() {
                int time = containerItem.getTicksLived();
                boolean sound = time == 3;
                AVFX.playChestedItemSizzle(containerItem.getLocation().add(0,0.3,0), sound);
                if (time >= 80){
                    chestedContainerItemExplode(containerItem);
                    cancel();
                }
                if (containerItem.isDead()){
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0,1);
    }
    public static void chestedContainerItemExplode(Item containerItem){
        AVFX.playChestedItemExplode(containerItem.getLocation());
        ItemStack containerItemStack = containerItem.getItemStack();
        List<ItemStack> items = new ArrayList<>();
        if (containerItemStack.getType().equals(Material.BUNDLE)){
            items.addAll(Util.getBundleContents(containerItemStack));
        }else{
            ItemStack[] toAddArray = chestedItemGetContents(containerItem.getItemStack());
            if (toAddArray != null) for (ItemStack itemStack:toAddArray) items.add(itemStack);
        }
        ItemStack containerTypeItem = new ItemStack(containerItemStack.getType());
        World world = containerItem.getWorld();
        Location location = containerItem.getLocation();
        containerItem.remove();
        world.dropItemNaturally(location, containerTypeItem);
        for (ItemStack item:items){
            if (item == null)continue;
            world.dropItemNaturally(location, item);
        }
    }
    public static void chestedWatchHolders(){
        Set<LivingEntity> toRemove = new HashSet<>();
        for (LivingEntity holder:chestedHolders){
            MobHead mobHead = MobHead.getMobHeadWornByEntity(holder);
            EntityType headType;
            if (mobHead == null){
                headType = null;
            }else headType = mobHead.getEntityType();
            int carried = chestedAmountCarried(holder);
            if (carried != 0){
                chestedAffectRiddenVehicle(holder);
            }
            if (carried == 0 && headType == null || carried == 0 && !chestedEntityTypes.contains(headType)){
                toRemove.add(holder);
            }
            chestedHandlePotionEffects(holder, headType);
        }
        if (toRemove.size() != 0){
            for (LivingEntity holder:toRemove){
                chestedRemoveHolder(holder);
            }
        }
    }
    public static void chestedAddHolder(LivingEntity holder){
        chestedHolders.add(holder);
    }
    public static void chestedRemoveHolder(LivingEntity holder){
        chestedHolders.remove(holder);
        PotionEffectManager.removeInfinitePotionEffects(holder);
    }
    private static final Map<EntityType, Integer> chestedSpeedMap = Map.of(
            EntityType.DONKEY, 1,
            EntityType.MULE, 2
    );
    private static final Map<EntityType, Double> chestedMultiplerMap = Map.of(
            EntityType.LLAMA, 0.5,
            EntityType.TRADER_LLAMA, 0.5
    );
    public static void chestedHandlePotionEffects(LivingEntity livingEntity, EntityType headType){
        List<PotionEffect> chestedEffects = chestedPotionEffects(livingEntity, headType);
        Map<PotionEffectType, PotionEffect> chestedEffectMap = new HashMap<>();
        for (PotionEffect potionEffect:chestedEffects) chestedEffectMap.put(potionEffect.getType(), potionEffect);
        if (chestedEffectMap.containsKey(PotionEffectType.SLOW)){
            if (livingEntity.hasPotionEffect(PotionEffectType.SPEED)){
                PotionEffect speed = livingEntity.getPotionEffect(PotionEffectType.SPEED);
                assert speed != null;
                if (speed.getDuration() != -1) livingEntity.removePotionEffect(PotionEffectType.SPEED);
            }
        }else if (chestedEffectMap.containsKey(PotionEffectType.SPEED)){
            if (livingEntity.hasPotionEffect(PotionEffectType.SLOW)){
                PotionEffect slow = livingEntity.getPotionEffect(PotionEffectType.SLOW);
                assert slow != null;
                if (slow.getDuration() != -1) livingEntity.removePotionEffect(PotionEffectType.SLOW);
            }
        }
        if (livingEntity.hasPotionEffect(PotionEffectType.SLOW)){
            PotionEffect slow = livingEntity.getPotionEffect(PotionEffectType.SLOW);
            assert slow != null;
            if (!chestedEffectMap.containsKey(PotionEffectType.SLOW)){
                if (slow.getDuration() == -1) livingEntity.removePotionEffect(PotionEffectType.SLOW);
            }else{
                PotionEffect newSlow = chestedEffectMap.get(PotionEffectType.SLOW);
                if (!slow.equals(newSlow)) livingEntity.removePotionEffect(PotionEffectType.SLOW);
            }
        }
        if (livingEntity.hasPotionEffect(PotionEffectType.SPEED)){
            PotionEffect speed = livingEntity.getPotionEffect(PotionEffectType.SPEED);
            assert speed != null;
            if (!chestedEffectMap.containsKey(PotionEffectType.SPEED)){
                if (speed.getDuration() == -1) livingEntity.removePotionEffect(PotionEffectType.SPEED);
            }else{
                PotionEffect newSpeed = chestedEffectMap.get(PotionEffectType.SPEED);
                if (!speed.equals(newSpeed)) livingEntity.removePotionEffect(PotionEffectType.SPEED);
            }
        }
        if (livingEntity.hasPotionEffect(PotionEffectType.WITHER)){
            PotionEffect wither = livingEntity.getPotionEffect(PotionEffectType.WITHER);
            assert wither != null;
            if (!chestedEffectMap.containsKey(PotionEffectType.WITHER)){
                if (wither.getDuration() == -1) livingEntity.removePotionEffect(PotionEffectType.WITHER);
            }else{
                PotionEffect newWither = chestedEffectMap.get(PotionEffectType.WITHER);
                if (!wither.equals(newWither)) livingEntity.removePotionEffect(PotionEffectType.WITHER);
            }
        }
        addEffectsToEntity(livingEntity, chestedEffects);
    }
    private static List<PotionEffect> chestedMaxPenalty(){
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(PotionEffectManager.headEffect(PotionEffectType.SLOW, 5, -1, false));
        effects.add(PotionEffectManager.headEffect(PotionEffectType.WITHER, 1, -1, true));
        return effects;
    }
    public static List<PotionEffect> chestedPotionEffects(LivingEntity livingEntity, EntityType headType){
        int containers = chestedAmountCarried(livingEntity);
        if (headType == null || !chestedEntityTypes.contains(headType)){
            if (containers == 0){
                return new ArrayList<>();
            }else return chestedMaxPenalty();
        }
        Integer speed = chestedSpeedMap.get(headType);
        if (speed == null) speed = 0;
        Double multiplier = chestedMultiplerMap.get(headType);
        if (multiplier == null) multiplier = 1.0;
        containers = (int) Math.floor(containers * multiplier);
        speed = speed - containers;
        List<PotionEffect> effects = new ArrayList<>();
        if (speed == 0){
            return effects;
        }else if (speed > 0){
            effects.add(PotionEffectManager.headEffect(PotionEffectType.SPEED,speed,-1,false));
            return effects;
        }else{
            if (speed >= -5){
                effects.add(PotionEffectManager.headEffect(PotionEffectType.SLOW, speed * -1, -1, false));
                return effects;
            }else{
                return chestedMaxPenalty();
            }
        }
    }
    public static boolean chestedItemIsContainer(ItemStack itemStack){
        if (itemStack == null)return false;
        if (!chestedWhitelist.contains(itemStack.getType()))return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)return false;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        return data.has(chestedKey, PersistentDataType.INTEGER);
    }
    public static boolean chestedItemContainsContainer(ItemStack itemStack){
        if (itemStack == null || !itemStack.getType().equals(Material.BUNDLE))return false;
        return chestedItemStackArrayContainsContainer(Util.getBundleContents(itemStack));
    }
    private static int chestedAmountCarried(LivingEntity chested){
        int carried = 0;
        List<Entity> toCheck = new ArrayList<>(chested.getPassengers());
        toCheck.add(chested);
        for (Entity entity:toCheck){
            if (!(entity instanceof InventoryHolder))return 0;
            InventoryHolder holder = (InventoryHolder) entity;
            ItemStack[] contentArray = holder.getInventory().getContents();
            List<ItemStack> contents = new ArrayList<>();
            for (ItemStack itemStack:contentArray) contents.add(itemStack);
            int held = chestedAmountInItemStackArray(contents);
            int subtraction = 0;
            double multiplier = 1;
            switch (chested.getType()){
                case DONKEY -> subtraction = 1;
                case MULE -> subtraction = 2;
                case LLAMA, TRADER_LLAMA -> multiplier = 0.5;
            }
            held = held - subtraction;
            if (held < 0) held = 0;
            held = (int) Math.floor(held * multiplier);
            carried = carried + held;
        }
        return carried;
    }
    public static int chestedAmountInItemStackArray(List<ItemStack> contents){
        int count = 0;
        for (ItemStack item:contents){
            if (item == null)continue;
            if (chestedItemIsContainer(item)) count++;
            if (item.getType().equals(Material.BUNDLE)){
                List<ItemStack> bundleContents = Util.getBundleContents(item);
                if (bundleContents == null)continue;
                int nestedCount = chestedAmountInItemStackArray(bundleContents);
                count = count + nestedCount;
            }
        }
        return count;
    }
    public static boolean chestedItemStackArrayContainsContainer(List<ItemStack> contents){
        return chestedAmountInItemStackArray(contents) != 0;
    }
    public static void chestedPickUpContainer(Player chested, Block interacted){
        Material containerType = interacted.getType();
        if (!chestedWhitelist.contains(containerType))return;
        ItemStack mainHand = chested.getInventory().getItemInMainHand();
        if (mainHand.getType() != Material.AIR)return;
        if (debug) System.out.println("Valid Container. " + interacted.getType()); //debug

        Container container = (Container) interacted.getState();

        PersistentDataContainer containerData = container.getPersistentDataContainer();
        if (containerData.getKeys().size() != 0){
            MobHeadsV3.messagePlayer(chested, ChatColor.RED + "This container has custom data and cannot be picked up.");
            return;
        }
//        if (containerType.equals(Material.SHULKER_BOX)){
//            ShulkerBox shulkerBox = (ShulkerBox) container;
//            return;
//        }
        ItemStack[] contents;
        if (container.getInventory() instanceof DoubleChestInventory){
            Location interactedLoc = interacted.getLocation();
            DoubleChest doubleChest = ((DoubleChestInventory) container.getInventory()).getHolder();
            assert doubleChest != null;
            Chest chestLeft = (Chest) doubleChest.getLeftSide();
            assert chestLeft != null;
            Chest chestRight = (Chest) doubleChest.getRightSide();
            assert chestRight != null;
            Location leftLoc = chestLeft.getLocation();
            boolean left = interactedLoc.equals(leftLoc);
            if (left){
                contents = chestLeft.getBlockInventory().getContents();
                chestLeft.getBlockInventory().clear();
                chestLeft.update(true);
            }else{
                contents = chestRight.getBlockInventory().getContents();
                chestRight.getBlockInventory().clear();
                chestRight.update(true);
            }

        }else{
            contents = container.getInventory().getContents();
            container.update(true);
        }

        ItemStack containerItem = new ItemStack(containerType);
        ItemMeta containerMeta = containerItem.getItemMeta();
        assert  containerMeta != null;
        PersistentDataContainer itemData = containerMeta.getPersistentDataContainer();

        String containerItemName = "§6§l" + "Picked Up " + StringBuilder.friendlyStringConversion(containerType.toString());
        List<String> lore = new ArrayList<>(List.of("§4§n" + "Dropping will spill contents!"));

        int i = 0;
        int items = 0;
        itemData.set(chestedKey, PersistentDataType.INTEGER,new Random().nextInt());
        for (ItemStack item:contents){
            String itemString;
            if (item == null){
                itemString = "null";
            }else{
                itemString = Serializer.serializeItemStack(item);
                if (items <= 5){
                    int count = item.getAmount();
                    boolean isHeadItem = MobHead.skullItemIsMobHead(item);
                    String color = "§f";
                    String name = StringBuilder.friendlyStringConversion(item.getType().toString());
                    if (isHeadItem && item.getItemMeta() != null) name = item.getItemMeta().getDisplayName();
                    if (item.getType().equals(Material.ENCHANTED_BOOK) || isHeadItem){
                        color = "§e";
                    }else if (item.getItemMeta() != null && item.getItemMeta().hasEnchants()) color = "§b";
                    lore.add(color + count + "x " + name);
                }
                items++;
            }
            itemData.set(new NamespacedKey(plugin,Key.chestedKeyString + i), PersistentDataType.STRING, itemString);
            i++;
        }
        if (items > 5){
            lore.add("§f" + "    ...");
        }
        if (items == 0){
            chested.getInventory().setItemInMainHand(new ItemStack(containerType));
        }else{
            containerMeta.setDisplayName(containerItemName);
            containerMeta.setLore(lore);
            containerItem.setItemMeta(containerMeta);
            chested.getInventory().setItemInMainHand(containerItem);
            chestedAddHolder(chested);
        }
        if (containerType.equals(Material.TRAPPED_CHEST)){
            Chest trappedChest = (Chest) container;
            chested.openInventory(trappedChest.getInventory());
            chested.closeInventory();
        }
        AVFX.playChestedPickup(interacted.getLocation().add(0.5,0.5,0.5));
        interacted.setType(Material.AIR);
    }
    public static void chestedPlaceContainer(Player chested, Block block, ItemStack containerItem){
        if (!chestedWhitelist.contains(containerItem.getType()))return;
        if (!chestedWhitelist.contains(block.getType()))return;
        EquipmentSlot hand;
        if (chested.getInventory().getItemInMainHand().equals(containerItem)){
            hand = EquipmentSlot.HAND;
        }else if (chested.getInventory().getItemInOffHand().equals(containerItem)){
            hand = EquipmentSlot.OFF_HAND;
        }else return;
        ItemStack[] items = chestedItemGetContents(containerItem);
        if (items == null)return;
        Container container = (Container) block.getState();
        container.getSnapshotInventory().setContents(items);
        container.setCustomName(null);
        container.update(true);
        containerItem.setAmount(containerItem.getAmount() - 1);
        if (hand.equals(EquipmentSlot.HAND)){
            chested.getInventory().setItemInMainHand(containerItem);
        }else chested.getInventory().setItemInOffHand(containerItem);

    }
    private static ItemStack[] chestedItemGetContents(ItemStack containerItem){
        ItemMeta containerMeta = containerItem.getItemMeta();
        if (containerMeta == null)return null;
        PersistentDataContainer data = containerMeta.getPersistentDataContainer();
        if (!chestedItemIsContainer(containerItem))return null;
        ItemStack[] items = new ItemStack[27];
        for (int i = 0; i < 27; i++) {
            String itemString = data.get(new NamespacedKey(plugin, Key.chestedKeyString + i), PersistentDataType.STRING);
            if (itemString == null || itemString.matches("null")){
                items[i] = null;
            }else items[i] = Serializer.deserializeItemStack(itemString);
        }
        return items;
    }


    // Nether Mobs (Fire-Resistant) ------------------------------------------------------------------------------------
    public static double netherMobFireResistance(LivingEntity target, double damage){
        double reduced = Math.floor(damage * 0.5);
        if (reduced != 0){
            PotionEffect fireResist = PotionEffectManager.buildSimpleEffect(PotionEffectType.FIRE_RESISTANCE,1,5*20);
            PotionEffectManager.addEffectToEntity(target,fireResist);
        }
        return reduced;
    }

    // Skeletons (Skeleton, Wither, Wither Skeleton, Skeleton Horse, Stray) --------------------------------------------
    private static final List<PotionEffect> skeletonMilkEffects = List.of(
            PotionEffectManager.buildSimpleEffect(PotionEffectType.REGENERATION,1,5*60*20),
            PotionEffectManager.buildSimpleEffect(PotionEffectType.SPEED,1,5*60*20),
            PotionEffectManager.buildSimpleEffect(PotionEffectType.DAMAGE_RESISTANCE,1,5*60*20)
    );
    public static void skeletonDrinkMilk(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                PotionEffectManager.addEffectsToEntity(player,skeletonMilkEffects);
            }
        }.runTaskLater(plugin,1);
    }

    // Fish (Cod, Salmon, Tropical, Puffer) ----------------------------------------------------------------------------

//    private static final Set<Player> fishingPlayers = new HashSet<>();
//    private static void fishSetFishing(Player player){
//        fishingPlayers.add(player);
//    }
//    private static void fishRemoveFishing(Player player){
//        fishingPlayers.remove(player);
//    }
//    private static boolean fishIsFishing(Player player){
//        return fishingPlayers.contains(player);
//    }
    public static void fishAutoFish(Player player, PlayerFishEvent pfe){
        if (debug) System.out.println("fishAutoFish()"); //debug



    }

    // Fox -------------------------------------------------------------------------------------------------------------
    private static boolean foxIsInBerryBush(LivingEntity livingEntity){
        Block source = livingEntity.getLocation().getBlock();
        if (source.getType().equals(Material.SWEET_BERRY_BUSH)){
            return true;
        }else{
            List<Block> surround = List.of(
                    source.getRelative(BlockFace.NORTH), source.getRelative(BlockFace.SOUTH),
                    source.getRelative(BlockFace.EAST), source.getRelative(BlockFace.WEST),
                    source.getRelative(BlockFace.NORTH_EAST), source.getRelative(BlockFace.SOUTH_EAST),
                    source.getRelative(BlockFace.SOUTH_WEST), source.getRelative(BlockFace.NORTH_WEST)
            );
            List<Block> berryBushes = new ArrayList<>();
            for (Block block:surround){if (block.getType().equals(Material.SWEET_BERRY_BUSH))berryBushes.add(block);}
            if (debug) System.out.println("Fox: berryBushes.size() " + berryBushes.size()); //debug
            if (berryBushes.size() == 0)return false;
            Location entLoc = livingEntity.getLocation();
            double entX = entLoc.getX();
            double entZ = entLoc.getZ();
            double contactDist = 0.3125;
            for (Block bush:berryBushes){
                double bushX = bush.getX();
                double bushZ = bush.getZ();
                double xDist = Math.abs(entX - bushX);
                double zDist = Math.abs(entZ - bushZ);
                double xOffDist = Math.abs(entX - (bushX + 1));
                double zOffDist = Math.abs(entZ - (bushZ + 1));
                if (debug) System.out.println(
                        "xDist: " + xDist + "\nzDist: " + zDist + "\nxOffDist: " + xOffDist + "\nzOffDist: " + zOffDist
                ); //debug
                if (xDist < contactDist || zDist < contactDist || xOffDist < contactDist || zOffDist < contactDist){
                    return true;
                }
            }
        }
        return false;
    }
    private static final Map<Player, List<Double>> foxPounceDataMap = new HashMap<>(); // 0 = last y | 1 = highest y
    private static double foxGetLastY(Player player){
        List<Double> list = foxPounceDataMap.get(player);
        Double i = list.get(0);
        if (i == null) i = -64.0;
        return i;
    }
    private static void foxSetLastY(Player player, double i){
        List<Double> list = foxPounceDataMap.get(player);
        list.set(0, i);
        foxPounceDataMap.put(player, list);
    }
    private static double foxGetMaxPounce(Player player){
        List<Double> list = foxPounceDataMap.get(player);
        Double i = list.get(1);
        if (i == null) i = -64.0;
        return i;
    }
    private static void foxSetMaxPounce(Player player, double i){
        List<Double> list = foxPounceDataMap.get(player);
        list.set(1, i);
        foxPounceDataMap.put(player, list);
    }
    private static int foxGetFrame(Player player){
        List<Double> list = foxPounceDataMap.get(player);
        Double i = list.get(2);
        if (i == null) i = 0d;
        return i.intValue();
    }
    private static void foxSetFrame(Player player, int frame){
        List<Double> list = foxPounceDataMap.get(player);
        list.set(2, (double) frame);
        foxPounceDataMap.put(player, list);
    }
    private static float foxGetStartingAngle(Player player){
        List<Double> list = foxPounceDataMap.get(player);
        Double i = list.get(3);
        if (i == null) i = 0d;
        return i.floatValue();
    }
    private static void foxSetStartingAngle(Player player, float angle){
        List<Double> list = foxPounceDataMap.get(player);
        list.set(3, (double) angle);
        foxPounceDataMap.put(player, list);
    }
    public static boolean foxIsPlayerPouncing(Player player){
        return foxPounceDataMap.containsKey(player);
    }
    private static void foxLockCamera(Player player, int frame){
        float pitch = -90;
        Vector velocity = player.getVelocity();
        Location location = player.getLocation();
        if (frame <= 10){
            location.setPitch(pitch + (frame * 18));
            player.teleport(location);
            player.setVelocity(velocity);
        }else{
            float pPitch = player.getLocation().getPitch();
            if (pPitch < 70){
                location.setPitch(90f);
                player.teleport(location);
                player.setVelocity(velocity);
            }
        }
    }
    private static void foxResetCamera(Player player){
        Location location = player.getLocation();
        location.setPitch(foxGetStartingAngle(player));
        player.teleport(location);
    }
    private static void foxWatchPounce(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                player.setGliding(true);
                boolean canceled = false;
                int frame = foxGetFrame(player);
                double y = player.getLocation().getY();
                double lastY = foxGetLastY(player);
                double maxY = foxGetMaxPounce(player);
                if (y > maxY){
                    maxY = y;
                }
                double bpt = y - lastY;
                boolean grounded = player.isOnGround();
                int distance = (int) Math.floor(Math.abs(maxY - y));
                int hudValue = distance;
                if (hudValue > 30) hudValue = 30;
                Hud.progressBar(player,30d,hudValue,true, "Pounce Power: ", false);
                if (debug) System.out.println("Fox Blocks per tick: " + bpt); //debug
                if (debug) System.out.println("Max Y: " + maxY); //debug

                Material currentMat = player.getLocation().getBlock().getType();
                if (currentMat.equals(Material.WATER) || currentMat.equals(Material.LAVA) || currentMat.equals(Material.COBWEB))canceled = true;

                if (grounded){
                    canceled = true;
                    if (debug) System.out.println("Fox Pounce Distance: " + distance + " blocks."); //debug
                    foxPounceSplashEffect(player, distance);
                    foxResetCamera(player);
                }else{
                    foxSetMaxPounce(player, maxY);
                    foxSetLastY(player, y);
                    foxLockCamera(player, frame);
                    frame++;
                    foxSetFrame(player,frame);
                }
                if (canceled){
                    Hud.progressBarEnd(player);
                    cancel();
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            foxPounceDataMap.remove(player);
                        }
                    }.runTaskLater(plugin,1);
                }
            }
        }.runTaskTimer(plugin,1,1);
    }
    private static void foxPounceSplashEffect(Player player, int power){
        power = power + 4;
        if (power > 30) power = 30;
        Block block = player.getLocation().getBlock();
        if (block.getType().toString().contains("AIR")) block = block.getRelative(BlockFace.DOWN);
        AVFX.playFoxPounceLandEffect(player.getLocation(), block);
        if (power >= 20) AVFX.playFoxPounceLandExplosionEffect(player.getLocation());
        List<LivingEntity> nearby = player.getNearbyEntities(10,6,10).stream()
                .filter(entity -> !entity.equals(player))
                .filter(player::hasLineOfSight)
                .filter(entity -> entity instanceof LivingEntity)
                .map(LivingEntity.class::cast)
                .collect(Collectors.toList()
        );
        for (LivingEntity entity:nearby){
            //if (!player.hasLineOfSight(entity))continue;
            Vector eLocVec = entity.getLocation().toVector();
            Vector pLocVec = player.getLocation().toVector();
            int distance = (int) Math.floor(eLocVec.distance(pLocVec));
            int finalDam = power - distance;
            if (finalDam < 1) continue;
            entity.damage(finalDam, player);
            Vector direction = eLocVec.subtract(pLocVec).normalize(); direction.setY(0); direction.multiply(1.5);
            Vector eVelocity = entity.getVelocity();
            entity.setVelocity(eVelocity.add(direction).add(new Vector(0,0.2 + (power * 0.01), 0)));
            AVFX.playFoxPounceLandHitEffect(entity.getLocation());
        }
    }
    public static void foxPounce(Player player){
        if (foxIsPlayerPouncing(player))return;
        float pitch = player.getLocation().getPitch();
        foxPounceDataMap.put(player, new ArrayList<>(List.of(-64.0, -64.0, 0d, (double) pitch)));
        Vector pVelocity = player.getVelocity();
        pVelocity.setY(0.0);
        Vector facing = player.getLocation().getDirection().multiply(0.4);
        player.setVelocity(pVelocity.add(new Vector(facing.getX(), 0.6, facing.getZ())));
        AVFX.playFoxPounceEffect(player.getEyeLocation());
        foxWatchPounce(player);
    }

    // Cat / Ocelot ----------------------------------------------------------------------------------------------------
    public static void catJumpFive(Player player, boolean sneaking){
        PotionEffect jumpFive = PotionEffectManager.buildSimpleEffect(PotionEffectType.JUMP,5,-1);
        if (sneaking){
            PotionEffectManager.addEffectToEntity(player, jumpFive);
        }else{
            PotionEffectManager.removeExactEffect(player, jumpFive);
        }
    }

    // Spider / Cave Spider --------------------------------------------------------------------------------------------
    public static void spiderClimb(Player player){

    }

    // Rabbit ----------------------------------------------------------------------------------------------------------
    private static final PotionEffect rabbitSpeedEffect = PotionEffectManager.buildSimpleEffect(PotionEffectType.SPEED,2,200);
    private static void rabbitSpeed(LivingEntity target){
        PotionEffectManager.addEffectToEntity(target,rabbitSpeedEffect);
    }
    public static void rabbitLeap(Player player){
        Vector velocity = player.getVelocity();
        Vector facing = player.getLocation().getDirection();
        double yVel = facing.getY() * 0.25 + 0.6;
        if (yVel < 0.1){
            yVel = 0.1;
        }else if (yVel > 0.6) yVel = 0.6;
        double xVel = facing.getX() * 0.35;
        double zVel = facing.getZ() * 0.35;
        Vector jumpVel = new Vector(xVel,yVel,zVel);
        player.setVelocity(velocity.add(jumpVel));
    }

    // Frog ------------------------------------------------------------------------------------------------------------
    public static void frogLeap(Player player){
        Vector velocity = player.getVelocity();
        Vector facing = player.getLocation().getDirection();
        double yVel = facing.getY() * 0.3 + 0.2;
        if (yVel < 0.1){
            yVel = 0.1;
        }else if (yVel > 0.5) yVel = 0.5;
        Vector jumpVel = facing.setY(yVel);
        player.setVelocity(velocity.add(jumpVel));
        AVFX.playFrogJumpEffect(player.getLocation());
    }

    private static final double frogEdiblePercent = 0.2;
    private static final Set<EntityType> frogEatBlacklist = Set.of(
            EntityType.ENDER_DRAGON, EntityType.WARDEN, EntityType.ELDER_GUARDIAN
    );
    private static final Set<EntityType> frogAlwaysEdible = Set.of(
            EntityType.SHULKER_BULLET, EntityType.COD, EntityType.SALMON, EntityType.PUFFERFISH,
            EntityType.TROPICAL_FISH, EntityType.RABBIT, EntityType.BAT, EntityType.FIREBALL, EntityType.DRAGON_FIREBALL
    );
    private static final Set<EntityType> frogInstantRemove = Set.of(
            EntityType.SHULKER_BULLET, EntityType.FIREBALL, EntityType.DRAGON_FIREBALL
    );
    private static final Set<Player> frogsOnCooldown = new HashSet<>();
    private static boolean isOnFrogCooldown(Player player){
        return frogsOnCooldown.contains(player);
    }
    private static void addToFrogCooldown(Player player){
        frogsOnCooldown.add(player);
        new BukkitRunnable(){
            @Override
            public void run() {
                frogsOnCooldown.remove(player);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),10);
    }
    public static Location getLocationInFrontOfFace(LivingEntity livingEntity){
        Vector facing = livingEntity.getLocation().getDirection().multiply(0.25);
        return livingEntity.getEyeLocation().add(facing);
    }
    private static boolean frogIsBlacklisted(Entity eaten){
        boolean blackListed = frogEatBlacklist.contains(eaten.getType());
        boolean tamed = eaten instanceof Tameable && ((Tameable)eaten).isTamed();
        if (eaten instanceof Camel && ((Camel)eaten).getInventory().getSaddle() == null) tamed = false;
        if (eaten instanceof Villager && ((Villager)eaten).getVillagerExperience() != 0) tamed = true;
        return blackListed || tamed;
    }
    public static void frogEatCreature(Player player, Entity eaten){ // Only active when sneaking!
        if (isOnFrogCooldown(player))return;
        if (frogIsBlacklisted(eaten)){
            MobHeadsV3.messagePlayer(player, ChatColor.DARK_RED + "This creature cannot be eaten.");
            return;
        }
        addToFrogCooldown(player);
        if (!frogIsEntityEdible(eaten)){
            Vector velocity = Util.getDirection(eaten.getLocation().toVector(),player.getEyeLocation().toVector()).multiply(0.2);
            eaten.setVelocity(eaten.getVelocity().add(velocity));
        }else{
            List<Object> restoration = frogGetRestoration(eaten);
            int hungerRestored = (Integer) restoration.get(0);
            float saturationRestored = (Float) restoration.get(1);
            int airRestored = (Integer) restoration.get(2);
            List<PotionEffect> gainedEffects = frogGetEffects(eaten);
            if (eaten.getType().equals(EntityType.COW)) gainedEffects.clear();
            Util.addAbilityDamageData(eaten, EntityType.FROG);
            if (eaten instanceof Damageable){
                if (eaten instanceof Player){
                    eaten.teleport(player.getLocation().add(0,0.2,0));
                }
                ((Damageable)eaten).damage(0,player);
                Vector velocity = Util.getDirection(eaten.getLocation().toVector(),player.getEyeLocation().add(0,-0.5,0).toVector());
                eaten.setVelocity(velocity);
                ((Damageable)eaten).setHealth(0);
            }else if (frogInstantRemove.contains(eaten.getType())){
                eaten.remove();
            }
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (gainedEffects.size() != 0) PotionEffectManager.addEffectsToEntity(player,gainedEffects);
                    int hunger = player.getFoodLevel() + hungerRestored;
                    if (hunger > 20) hunger = 20;
                    float saturation = player.getSaturation() + saturationRestored;
                    if (saturation > 20f) saturation = 20f;
                    int air = player.getRemainingAir() + airRestored;
                    int maxAir = player.getMaximumAir();
                    if (air > maxAir) air = maxAir;
                    player.setFoodLevel(hunger);
                    player.setSaturation(saturation);
                    player.setRemainingAir(air);
                    Location inFront = getLocationInFrontOfFace(player);
                    switch (eaten.getType()){
                        case COW -> {
                            for (PotionEffect effect:player.getActivePotionEffects()){
                                PotionEffectType type = effect.getType();
                                if (!type.equals(PotionEffectType.JUMP)) player.removePotionEffect(type);
                            }
                            AVFX.playFrogEatCowEffect(inFront);
                        }
                        case CREEPER -> {creeperExplosion(player);}
                        case SNOWMAN -> {player.setFreezeTicks(player.getMaxFreezeTicks());}
                        case SHULKER_BULLET -> {AVFX.playShulkerAfflictionEffect(player.getLocation(),player.getEyeHeight(),false);}
                        case FIREBALL, DRAGON_FIREBALL -> {
                            EntityType ballType = eaten.getType();
                            Fireball fireball = (Fireball) player.getWorld().spawnEntity(inFront,ballType);
                            fireball.setVelocity(player.getLocation().getDirection());
                            if (player.getFireTicks() == 0) player.setFireTicks(20);
                            AVFX.playFrogFireballSpit(inFront);
                        }
                    }
                    if (eaten instanceof LivingEntity) AVFX.playFrogEatenSounds(inFront,eaten.getType());
                    eaten.remove();
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),8);
        }
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
            player.swingMainHand();
        }
        AVFX.playFrogTongueSound(eaten.getLocation());
    }
    private static final Map<Player,List<Entity>> frogClockRunningMap = new HashMap<>();
    public static void frogStartNearbyEdibleClock(Player player){
        if (frogClockRunningMap.containsKey(player))return;
        frogClockRunningMap.put(player, new ArrayList<>());
        new BukkitRunnable(){
            @Override
            public void run() {
                MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
                boolean correctHead = mobHead != null && mobHead.getEntityType().equals(EntityType.FROG);
                if (!correctHead){
                    for (Entity entity:frogClockRunningMap.get(player)){
                        Packets.toggleGlow(player,entity,false);
                    }
                    frogClockRunningMap.remove(player);
                    cancel();
                    return;
                }
                List<Entity> wasNearby = frogClockRunningMap.get(player);
                List<Entity> nearby = player.getNearbyEntities(18,12,18);
                List<Entity> validNearby = new ArrayList<>();
                for (Entity entity:nearby){
                    if (!frogIsEntityEdible(entity))continue;
                    if (player.hasLineOfSight(entity)) validNearby.add(entity);
                }
                for (Entity entity:validNearby){
                    Packets.toggleGlow(player,entity,true);
                }
                frogClockRunningMap.put(player,nearby);
                wasNearby.removeAll(validNearby);
                if (wasNearby.size() == 0)return;
                for (Entity entity:wasNearby) Packets.toggleGlow(player,entity,false);
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,5);
    }
    private static boolean frogIsEntityEdible(Entity eaten){
        if (eaten == null || eaten.isDead()) return false;
        if (frogIsBlacklisted(eaten)) return false;
        if (eaten instanceof Player){
            Player eatenPlayer = (Player) eaten;
            if (!eatenPlayer.getGameMode().equals(GameMode.SURVIVAL)) return false;
            if (!eatenPlayer.getWorld().getPVP()) return false;
        }
        if (frogAlwaysEdible.contains(eaten.getType())) return true;
        if (!(eaten instanceof LivingEntity)) return false;
        if (eaten instanceof Slime && ((Slime)eaten).getSize() == 1) return true;
        if (eaten instanceof MagmaCube && ((MagmaCube)eaten).getSize() == 1) return true;
        if (eaten instanceof Slime && ((Slime)eaten).getSize() != 1) return false;
        if (eaten instanceof MagmaCube && ((MagmaCube)eaten).getSize() != 1) return false;
        LivingEntity livingEaten = (LivingEntity) eaten;
        double health = livingEaten.getHealth();
        AttributeInstance maxHealthAttribute = livingEaten.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute == null) return false;
        //if (debug) System.out.println("health: " + health + " maxHealth: " + maxHealthAttribute.getValue()); //debug
        double maxHealth = maxHealthAttribute.getValue();
        if (maxHealth == 0) return false;
        return health <= 4 || health / maxHealth <= frogEdiblePercent;
    }
    private static List<PotionEffect> frogGetEffects(Entity eaten){
        List<PotionEffect> effects = new ArrayList<>();
        List<PotionEffect> eatenEffects;
        if (eaten instanceof LivingEntity && !Summon.isEntitySummon(eaten)){
            eatenEffects = new ArrayList<>(((LivingEntity)eaten).getActivePotionEffects());
        }else eatenEffects = new ArrayList<>();
        for (PotionEffect eatenEffect:eatenEffects){
            PotionEffectType type = eatenEffect.getType();
            int amp = eatenEffect.getAmplifier();
            int dur = eatenEffect.getDuration();
            if (dur == -1 || dur > 6000) dur = 6000;
            effects.add(new PotionEffect(type,dur,amp,false,true,true));
        }
        EntityType eatenType = eaten.getType();
        if (Groups.isZombie(eatenType)) effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.HUNGER,1,600));
        switch (eatenType){
            case STRIDER -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.FIRE_RESISTANCE,1,3600));
            }
            case MAGMA_CUBE -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.FIRE_RESISTANCE,1,3600));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.JUMP,2,3600));
            }
            case GHAST, BLAZE -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.FIRE_RESISTANCE,1,3600));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW_FALLING,1,3600));
            }
            case RABBIT, FROG, SLIME -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.JUMP,2,3600));
            }
            case CHICKEN, PARROT -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW_FALLING,1,3600));
            }
            case GLOW_SQUID, PHANTOM, BAT -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.NIGHT_VISION,1,3600));
            }
            case DOLPHIN -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.DOLPHINS_GRACE,1,3600));
            }
            case IRON_GOLEM -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW,2,3600));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.DAMAGE_RESISTANCE,2,3600));
            }
            case RAVAGER -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW,1,3600));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.DAMAGE_RESISTANCE,1,3600));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.INCREASE_DAMAGE,1,3600));
            }
            case PUFFERFISH, BEE, CAVE_SPIDER -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.POISON,1,300));
            }
            case GIANT -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW,4,1200));
            }
            case WITHER_SKELETON -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.WITHER,1,300));
            }
            case SHULKER -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.LEVITATION,1,200));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW_FALLING,1,2400));
            }
            case SHULKER_BULLET -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.LEVITATION,1,100));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW_FALLING,1,600));
            }
            case HORSE,SKELETON_HORSE,ZOMBIE_HORSE -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SPEED,2,3600));
            }
            case CAT,OCELOT -> {
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.SPEED,1,3600));
                effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.JUMP,2,3600));
            }
            case TURTLE,GUARDIAN -> {effects.add(PotionEffectManager.buildSimpleEffect(PotionEffectType.DAMAGE_RESISTANCE,1,3600));}
        }
        return effects;
    }
    private static List<Object> frogGetRestoration(Entity eaten){
        int hunger = 0;
        float saturation = 0;
        int air = 0;
        EntityType eatenType = eaten.getType();
        if (eaten instanceof WaterMob) air = air + 100;
        switch (eatenType){
            case DROWNED -> air = air + 120;
            case GUARDIAN -> air = air + 160;
        }
        double maxHealth = 0;
        if (eaten instanceof LivingEntity){
            AttributeInstance maxHealthAttribute = ((LivingEntity)eaten).getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute != null) maxHealth = maxHealthAttribute.getValue();
        }
        hunger = (int) Math.ceil(maxHealth * 0.3);
        saturation = (float) Math.ceil(maxHealth * 0.15);

        return List.of(hunger,saturation,air);
    }
    public static void frogGetDrops(Player player, List<ItemStack> drops){
        new BukkitRunnable(){
            @Override
            public void run() {
                boolean hadDrops = false;
                List<ItemStack> overflowList = new ArrayList<>();
                for (ItemStack drop:drops){
                    if (debug) System.out.println("frogGetDrops() -> run() drop: " + drop); //debug
                    if (Data.getFoodMats().contains(drop.getType()))continue;
                    hadDrops = true;
                    Map<Integer,ItemStack> overflow = player.getInventory().addItem(drop);
                    if (overflow.size() == 0)continue;
                    overflowList.addAll(overflow.values());
                }
                if (debug) System.out.println("overflowList.size(): " + overflowList.size()); //debug
                if (hadDrops) AVFX.playFrogGetDropsSound(getLocationInFrontOfFace(player), false);
                if (overflowList.size() == 0)return;
                AVFX.playFrogGetDropsSound(getLocationInFrontOfFace(player), true);
                for (ItemStack droppedItem:overflowList) player.getWorld().dropItem(player.getLocation(),droppedItem);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),8);
    }

    // Sniffer ---------------------------------------------------------------------------------------------------------
    private static final Set<Material> snifferSusMaterials = Set.of(Material.SUSPICIOUS_GRAVEL,Material.SUSPICIOUS_SAND);
    public static void snifferHighlightSus(Player player){
        List<Block> nearbySus = new ArrayList<>();
        //List<Block> nearbyBlocks = Util.getNearbyBlocks(player.getLocation(),4,4,4);
        List<Block> nearbyBlocks = Util.getFirstFromSkyBlocks(player.getLocation(),10);
        for (Block block:nearbyBlocks){
            if (!snifferSusMaterials.contains(block.getType()))continue;
            int difference = block.getY() - player.getLocation().getBlockY();
            if (difference > 15 || difference < -20){
                if (debug) System.out.println("Sus block too high/low!"); //debug
                continue;
            }
            nearbySus.add(block);
        }
        if (debug) System.out.println("nearbySus.size(): " + nearbySus.size()); //debug
        if (nearbySus.size() == 0)return;
        for (Block sus:nearbySus){
            Location origin = sus.getLocation().add(0.5,0.5,0.5);
            Packets.susParticles(player, origin);
        }
    }

    // Camel -----------------------------------------------------------------------------------------------------------
    private static final Map<Player, Integer> camelsDashing = new HashMap<>();
    private static final int camelDashCooldown = 55; // Same as vanilla
    private static boolean isCamelDashing(Player player){
        return camelsDashing.containsKey(player);
    }
    private static void updateCamelsDashing(Player player, int timer){
        camelsDashing.put(player, timer);
    }
    private static void removeFromCamelsDashing(Player player){
        camelsDashing.remove(player);
    }
    private static Integer getCamelDashTimer(Player player){
        return camelsDashing.get(player);
    }
    private static void startCamelDashClock(Player player){
        updateCamelsDashing(player,camelDashCooldown);
        new BukkitRunnable(){
            @Override
            public void run() {
                Integer timer = getCamelDashTimer(player);
                if (timer == null || timer == 0){
                    Hud.progressBarEnd(player);
                    removeFromCamelsDashing(player);
                    AVFX.playCamelRefreshSound(player.getLocation());
                    cancel();
                    return;
                }
                timer--;
                Hud.progressBar(player,camelDashCooldown,timer,false,"Dash:",false);
                updateCamelsDashing(player,timer);
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);
    }
    public static void camelDash(Player player){ // Jump 12 Blocks.
        if (isCamelDashing(player))return;
        Vector facing = player.getLocation().getDirection();
        double multiplier = 1.6;
        double x = facing.getX() * multiplier;
        double z = facing.getZ() * multiplier;
        Vector velocity = new Vector(x, 0.3, z);
        player.setVelocity(velocity);
        AVFX.playCamelDashSound(player.getLocation());
        startCamelDashClock(player);
    }

    // Endermite -------------------------------------------------------------------------------------------------------
    public static void endermiteEndermanAggro(LivingEntity target){
        List<Enderman> nearbyEndermen = target.getNearbyEntities(16,16,16).stream()
                .filter(entity -> entity instanceof Enderman)
                .map(Enderman.class::cast)
                .collect(Collectors.toList()
        );
        if (nearbyEndermen.size() == 0)return;
        for (Enderman enderman:nearbyEndermen){
            if (!enderman.hasLineOfSight(target))continue;
            if (enderman.getTarget() != null && enderman.getTarget().equals(target))continue;
            enderman.setTarget(target);
        }
    }

    // Enderman --------------------------------------------------------------------------------------------------------
    private static void endermanTeleport(LivingEntity target, Location destination){
        AVFX.playEndermanTeleportSound(target.getLocation());
        target.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN);
        AVFX.playEndermanTeleportSound(destination);
    }
    public static void endermanTeleportNearby(LivingEntity target, boolean instant){
        if (debug) System.out.println("endermanTeleportNearby()"); //debug
        Location destination = Util.getSafeTeleportLoc(target.getLocation(), 10, 10, 10);
        if (destination.equals(target.getLocation())){
            // failure message
            return;
        }
        if (instant){
            endermanTeleport(target,destination);
        }else{
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (target.isDead())return;
                    endermanTeleport(target,destination);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),1);
        }
    }

    // Creeper ---------------------------------------------------------------------------------------------------------
    private static final List<Player> creepersOnCooldown = new ArrayList<>();
    private static boolean isCreeperOnCooldown(Player player){
        return creepersOnCooldown.contains(player);
    }
    private static void addToCreepersOnCooldown(Player player){
        if (creepersOnCooldown.contains(player))return;
        creepersOnCooldown.add(player);
        new BukkitRunnable(){
            @Override
            public void run() {
                creepersOnCooldown.remove(player);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),10);
    }
    private static final int explosionDamage = 8;
    private static void creeperExplosion(Player player){
        player.setVelocity(new Vector(0,0.8,0));
        player.damage(2, player);
        AVFX.playCreeperExplosionEffect(player.getLocation().add(0,player.getEyeHeight()*0.5, 0));
        List<Damageable> damageables = player.getNearbyEntities(8,6,8).stream()
                .filter(entity -> entity instanceof Damageable)
                .filter(player::hasLineOfSight)
                .map(Damageable.class::cast)
                .collect(Collectors.toList()
        );
        damageables.remove(player);
        if (damageables.size() == 0)return;
        Vector vecLoc = player.getLocation().toVector();
        for (Damageable damageable:damageables){
            if (debug) System.out.println(damageable.getType()); //debug
            Vector damVecLoc = damageable.getLocation().toVector();
            Vector direction = Util.getDirection(vecLoc,damVecLoc);
            int distance = (int) Math.floor(vecLoc.distance(damVecLoc));
            int damage = explosionDamage - distance;
            if (damage < 1)continue;
            damageable.damage(damage,player);
            Vector velocity = direction.multiply(0.2*damage);
            damageable.setVelocity(damageable.getVelocity().add(velocity));
        }
    }
    public static void creeperExplodeGunpowder(PlayerInteractEvent pie){
        Block block = pie.getClickedBlock();
        if (block != null && block.getType().isInteractable())return;
        Player player = pie.getPlayer();
        if (isCreeperOnCooldown(player))return;
        PlayerInventory inv = player.getInventory();
        EquipmentSlot hand = pie.getHand();
        if (hand == null)return;
        ItemStack gunPowder;
        if (hand.equals(EquipmentSlot.HAND)){
            gunPowder = inv.getItemInMainHand();
        }else gunPowder = inv.getItemInOffHand();
        if (!gunPowder.getType().equals(Material.GUNPOWDER))return;
        addToCreepersOnCooldown(player);
        int count = gunPowder.getAmount();
        count--;
        gunPowder.setAmount(count);
        if (hand.equals(EquipmentSlot.HAND)){
            inv.setItemInMainHand(gunPowder);
        }else inv.setItemInOffHand(gunPowder);
       creeperExplosion(player);
    }

    // Axolotl ---------------------------------------------------------------------------------------------------------
    private static final PotionEffect axolotlRegenEffect = PotionEffectManager.buildSimpleEffect(PotionEffectType.REGENERATION,1,200);
    private static void axolotlRegeneration(LivingEntity target){
        PotionEffectManager.addEffectToEntity(target,axolotlRegenEffect);
    }

    // Slime / Magma Cube ----------------------------------------------------------------------------------------------
    public static Map<LivingEntity,Double> slimeLastYPos = new HashMap<>();
    private static final Map<LivingEntity,Integer> slimeJumps = new HashMap<>();
    private static final Set<LivingEntity> slimeJumpCooldown = new HashSet<>();
    private static boolean isOnSlimeJumpCooldown(LivingEntity slime){
        return slimeJumpCooldown.contains(slime);
    }
    private static void addToSlimeJumpCooldown(LivingEntity slime){
        if (isOnSlimeJumpCooldown(slime))return;
        slimeJumpCooldown.add(slime);
        new BukkitRunnable(){
            @Override
            public void run() {
                slimeJumpCooldown.remove(slime);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),5);
    }
    //private static final double slimeVelocityMultiplier = 1.4;
    public static void slimeAirborne(LivingEntity slime){
        //if (debug) System.out.println("slimeAirborne() slimeLastYPos: " + slimeLastYPos.get(slime)); //debug
        if (isOnSlimeJumpCooldown(slime))return;
        boolean player = slime instanceof Player;
        boolean grounded = slime.isOnGround() || slime.isInWater();
        double y = slime.getLocation().getY();
        int jumps = slimeJumps.getOrDefault(slime, 0);
        //if (debug) System.out.println("jumps: " + jumps); //debug
        if (jumps > 4) jumps = 4;
        if (!grounded){
            slimeLastYPos.put(slime,y);
            if (jumps != 0 && player) Hud.progressBar((Player) slime,4,jumps,true, "Jump Speed:",false);
        }else{
            double difference = 0;
            if (slimeLastYPos.containsKey(slime)){
                Double lastY = slimeLastYPos.get(slime);
                difference = y - lastY;
            }
            double mpt = Math.abs(difference);
//            if (debug) System.out.println(
//                    "\nsneaking: " + slime.isSneaking() +"\nOn cooldown: " + isOnSlimeJumpCooldown(slime) +
//                    "\nlastYPos!containPlayer: " + !slimeLastYPos.containsKey(slime) + "\nmpt < 0.4: " + (mpt < 0.4)
//            ); //debug
            if (player && ((Player)slime).isSneaking() || !slimeLastYPos.containsKey(slime) || mpt < 0.5 || slime.isInWater()){
                slimeReset(slime);
                return;
            }
            addToSlimeJumpCooldown(slime);
            Vector looking = slime.getLocation().getDirection();
            Vector velocity = slime.getVelocity();
            double multiplier = (jumps + 2) * 0.3;
            velocity.add(new Vector(looking.getX()*multiplier,0,looking.getZ()*multiplier));
            velocity.setY(mpt * 1.15);
            slime.setVelocity(velocity);
            if (wasCreatureGliding(slime)) slime.setGliding(true);
            //if (debug) System.out.println("velocity: " + velocity); //debug
            jumps++;
            slimeJumps.put(slime,jumps);
            MobHead mobHead = MobHead.getMobHeadWornByEntity(slime);
            EntityType entityType = EntityType.SLIME;
            if (mobHead != null) entityType = mobHead.getEntityType();
            AVFX.playSlimeBounceEffect(slime.getLocation(), entityType);
            //if (debug) System.out.println("difference: " + difference + " mpt: " + mpt); //debug
        }
    }
    public static void slimeReset(LivingEntity slime){
        //if (debug) System.out.println("Slimejump reset"); //debug
        if (slime instanceof Player && slimeLastYPos.containsKey(slime)) Hud.progressBarEnd((Player)slime);
        slimeLastYPos.remove(slime);
        slimeJumps.remove(slime);
    }
    public static void slimeJump(Player player){
        Vector facing = player.getLocation().getDirection();
        Vector velocity = player.getVelocity();
        Vector jumpVel = facing.multiply(0.3).setY(0.4);
        player.setVelocity(velocity.add(jumpVel));
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        EntityType entityType = EntityType.SLIME;
        if (mobHead != null) entityType = mobHead.getEntityType();
        AVFX.playSlimeJumpEffect(player.getLocation(), entityType);
    }

    public static void slimeRicochet(LivingEntity slime, double strength, boolean fall){
        if (strength < 1) return;
        if (slime instanceof Player && ((Player)slime).isSneaking())return;
        Vector wasFacing;
        if (fall){
            wasFacing = new Vector(0,125,0);
        }else wasFacing = slime.getLocation().getDirection();
        double x = wasFacing.getX() * -1;
        double y = wasFacing.getY();
        double z = wasFacing.getZ() * -1;
        Vector facing = new Vector(x,y,z).multiply(0.1 * strength);
        Location location = slime.getLocation().setDirection(facing);
        slime.teleport(location);
        slime.setVelocity(facing);
        slime.setGliding(true);
        MobHead mobHead = MobHead.getMobHeadWornByEntity(slime);
        EntityType entityType = EntityType.SLIME;
        if (mobHead != null) entityType = mobHead.getEntityType();
        AVFX.playSlimeBounceEffect(slime.getLocation(),entityType);
    }
    private static double slimeFallDamage(LivingEntity slime, double damage){
        if (wasCreatureGliding(slime)){
            if (debug) System.out.println("Slime was gliding"); //debug
            slimeRicochet(slime,damage, true);
        }
        return Math.floor(damage * 0.2); // 80% Reduction
    }

    // Ghast -----------------------------------------------------------------------------------------------------------
    private static final Map<Player, Integer> ghastsFloating = new HashMap<>();
    private static boolean isAGhastFloating(Player player){
        return ghastsFloating.containsKey(player);
    }
    private static void updateGhastsFloating(Player player, int timer){
        ghastsFloating.put(player,timer);
    }
    public static void removeFromGhastsFloating(Player player){
        ghastsFloating.remove(player);
    }
    private static Integer getGhastGloatingTimer(Player player){
        return ghastsFloating.get(player);
    }
    private static final int ghastFloatTime = 200;
    private static final PotionEffect ghastFloatEffect = new PotionEffect(PotionEffectType.LEVITATION,-1,2,false,false,true);
    public static void ghastFloat(Player player){
        if (isAGhastFloating(player))return;
        updateGhastsFloating(player,ghastFloatTime);
        new BukkitRunnable(){
            @Override
            public void run() {
                MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
                boolean wearingGhastHead = mobHead != null && mobHead.getEntityType().equals(EntityType.GHAST);
                Integer timer = getGhastGloatingTimer(player);
                boolean grounded = player.isOnGround();
                if (!wearingGhastHead || timer == null || timer == 0 || grounded){
                    PotionEffectManager.removeExactEffect(player,ghastFloatEffect);
                    Hud.progressBarEnd(player);
                    cancel();
                    return;
                }
                boolean sneaking = player.isSneaking();
                if (sneaking){
                    PotionEffectManager.addEffectToEntity(player, ghastFloatEffect);
                    AVFX.playGhastFlightParticles(player.getLocation());
                    timer--;
                    updateGhastsFloating(player,timer);
                }else{
                    PotionEffectManager.removeExactEffect(player,ghastFloatEffect);
                }
                Hud.progressBar(player,ghastFloatTime,timer,true,"Float Time:",true);
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),1,1);
    }

    // Panda -----------------------------------------------------------------------------------------------------------
    private static final List<Player> pandaMunchCooldown = new ArrayList<>();
    private static boolean isOnPandaMunchCooldown(Player player){
        return pandaMunchCooldown.contains(player);
    }
    private static void addToPandaMunchCooldown(Player player){
        pandaMunchCooldown.add(player);
        new BukkitRunnable(){
            @Override
            public void run() {
                pandaMunchCooldown.remove(player);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),40);
    }
    public static void pandaSnackBamboo(Player player){ //Tick. Main thread
        if (isOnPandaMunchCooldown(player))return;
        int foodLv = player.getFoodLevel();
        if (foodLv > 18)return;
        PlayerInventory inv = player.getInventory();
        ItemStack[] contents = inv.getStorageContents();
        int count = 0;
        for (ItemStack itemStack:contents){
            if (itemStack == null || !itemStack.isSimilar(new ItemStack(Material.BAMBOO)))continue;
            count = count + itemStack.getAmount();
        }
        if (count <= 1)return;
        boolean eaten = false;
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (itemStack == null || !itemStack.isSimilar(new ItemStack(Material.BAMBOO)))continue;
            int amount = itemStack.getAmount();
            amount--;
            if (amount == 0){
                itemStack = null;
            }else{
                itemStack.setAmount(amount);
            }
            contents[i] = itemStack;
            eaten = true;
            break;
        }
        if (!eaten)return;
        addToPandaMunchCooldown(player);
        inv.setStorageContents(contents);
        foodLv++;
        player.setFoodLevel(foodLv);
        float saturationLv = player.getSaturation();
        saturationLv = saturationLv + 0.75f;
        player.setSaturation(saturationLv);
        AVFX.playPandaMunchBambooEffect(player.getEyeLocation().add(0,-0.25,0).add(player.getLocation().getDirection().multiply(0.4)));
    }

    // Llama / Trader Llama --------------------------------------------------------------------------------------------
    private static void llamaRetaliationSpit(LivingEntity source, LivingEntity target){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (source == null || source.isDead() || target == null || target.isDead())return;
                Location originLoc = source.getLocation().add(0,source.getEyeHeight()*0.8,0);
                Vector origin = originLoc.toVector();
                Vector destination = target.getLocation().add(0,target.getEyeHeight()*0.5,0).toVector();
                double distance = origin.distance(destination);
                if (distance < 2){
                    distance = 2;
                }else if (distance > 10) distance = 10;
                Vector direction = Util.getDirection(origin,destination);
                originLoc.add(direction.multiply(0.5));
                LlamaSpit llamaSpit = (LlamaSpit) source.getWorld().spawnEntity(originLoc,EntityType.LLAMA_SPIT);
                PersistentDataContainer data = llamaSpit.getPersistentDataContainer();
                data.set(Key.abilityProjectile,PersistentDataType.STRING, "LLAMA");
                llamaSpit.setShooter(source);
                llamaSpit.setVelocity(direction.multiply(distance*0.5));
                AVFX.playLlamaSpitSound(originLoc);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),10);
    }

    // Allay -----------------------------------------------------------------------------------------------------------
    public static void allayAttractNearbyItems(LivingEntity target){ // Tick. Main Thread.
        EntityEquipment equip = target.getEquipment();
        if (equip == null)return;
        ItemStack itemStack0 = equip.getItemInMainHand();
        ItemStack itemStack1 = equip.getItemInOffHand();
        boolean emptyHand0 = itemStack0.getType().equals(Material.AIR);
        boolean emptyHand1 = itemStack1.getType().equals(Material.AIR);
        if (emptyHand0 && emptyHand1)return;
        List<Item> nearbyItems = target.getNearbyEntities(24,32,24).stream()
                .filter(entity -> entity instanceof Item)
                .map(Item.class::cast)
                .collect(Collectors.toList()
        );
        if (nearbyItems.size() == 0)return;
        Vector vecLoc = target.getLocation().toVector();
        for (Item item:nearbyItems){
            if (item.getPickupDelay() > 1)continue;
            ItemStack itemStack = item.getItemStack();
            if (!itemStack.isSimilar(itemStack0) && !itemStack.isSimilar(itemStack1))continue;
            Vector direction = Util.getDirection(item.getLocation().toVector(),vecLoc);
            if (direction.getY() >= 0 && direction.getY() < 0.3) direction.setY(0.3);
            item.setVelocity(direction.multiply(0.8));
            AVFX.playAllayAttractEffect(item.getLocation().add(0,0.2,0));
        }
    }

    // Pig -------------------------------------------------------------------------------------------------------------
    public static void pigGobble(PlayerInteractEvent pie){
        if (debug) System.out.println("pigGobble()"); //debug
        ItemStack foodItem = pie.getItem();
        assert foodItem != null;
        Player player = pie.getPlayer();
        int hunger = player.getFoodLevel();
        float saturation = player.getSaturation();

        if (debug) System.out.println("This function does not work yet");
    }

    // Goat ------------------------------------------------------------------------------------------------------------
    private static final List<Material> goatBreakable = Arrays.asList(
            Material.DIRT, Material.DIRT_PATH, Material.COARSE_DIRT, Material.ROOTED_DIRT, Material.GRASS_BLOCK,
            Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.AZALEA_LEAVES, Material.JUNGLE_LEAVES, Material.FLOWERING_AZALEA_LEAVES,
            Material.DARK_OAK_LEAVES, Material.MANGROVE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.CHERRY_LEAVES,
            Material.STONE, Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.REDSTONE_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.GRAVEL, Material.SAND,
            Material.SANDSTONE, Material.RED_SAND, Material.RED_SANDSTONE, Material.CACTUS, Material.SUGAR_CANE, Material.ICE,
            Material.FROSTED_ICE, Material.MUSHROOM_STEM, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.NETHERRACK, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.RAW_IRON_BLOCK, Material.RAW_COPPER_BLOCK,
            Material.RAW_GOLD_BLOCK, Material.SOUL_SAND, Material.SOUL_SOIL, Material.BASALT, Material.BAMBOO, Material.END_STONE,
            Material.CHORUS_PLANT, Material.CHORUS_FRUIT, Material.CHORUS_FLOWER, Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK,
            Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM, Material.ANDESITE, Material.GRANITE, Material.DIORITE, Material.TUFF,
            Material.CALCITE, Material.BLACKSTONE, Material.CACTUS, Material.PUMPKIN, Material.MELON, Material.CLAY, Material.DRIPSTONE_BLOCK,
            Material.POINTED_DRIPSTONE, Material.MOSS_BLOCK, Material.SMOOTH_BASALT, Material.SHROOMLIGHT, Material.AZALEA,
            Material.FLOWERING_AZALEA, Material.SCULK, Material.SCULK_VEIN, Material.SCULK_CATALYST, Material.TERRACOTTA,
            Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.LIME_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.ORANGE_TERRACOTTA,
            Material.PINK_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.RED_TERRACOTTA, Material.WHITE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA
    );
    private static final double goatMaximumChargeSpeed = 2.5;
    private static final double goatMinimumChargeSpeed = 0.3;
    private static final double goatMinimumBlockDamageChargeSpeed = 0.8;
    private static final List<Player> goatsCharging = new ArrayList<>();
    private static boolean isGoatCharging(Player player){
        return goatsCharging.contains(player);
    }
    private static void addToGoatsCharging(Player player){
        if (!goatsCharging.contains(player)) goatsCharging.add(player);
    }
    private static void removeFromGoatsCharging(Player player){
        goatsCharging.remove(player);
    }
    public static void goatBreakBlocks(Player player, BlockFace direction, Block originBlock, @Nullable Double speed){
        Block damagedBlock = originBlock.getRelative(direction);
        if (!goatBreakable.contains(damagedBlock.getType()))return;
        if (speed != null){
            double damage = Math.ceil(speed*0.8);
            player.damage(damage);
            //player.setNoDamageTicks(5);
            player.setVelocity(player.getVelocity().multiply(0.1));
        }
        List<Block> blocksToBreak = Util.getBlockAndNeighbors(damagedBlock,direction);
        List<Material> brokenBlockMats = new ArrayList<>();
        List<Location> brokenBlockLocs = new ArrayList<>();
        for (Block block:blocksToBreak){
            if (goatBreakable.contains(block.getType())){
                brokenBlockMats.add(block.getType());
                brokenBlockLocs.add(block.getLocation().add(0.5,0.5,0.5));
                block.breakNaturally(); //debug
            }
        }
        Location origin = originBlock.getRelative(direction.getOppositeFace()).getLocation().add(0.5,0.5,0.5);
        AVFX.playGoatBreakBlockSound(origin,brokenBlockMats,brokenBlockLocs);
    }
    private static List<LivingEntity> nearbyLivingEnts(Location origin, double x, double y, double z){
        World world = origin.getWorld();
        if (world == null)return new ArrayList<>();
        return world.getNearbyEntities(origin,x,y,z).stream()
                .filter(entity -> entity instanceof LivingEntity).map(LivingEntity.class::cast)
                .collect(Collectors.toList()
        );
    }
    private static final Map<LivingEntity,UUID> goatRecentlyHitMap = new HashMap<>();
    public static boolean isOnGoatRecentlyHit(LivingEntity livingEntity){
        return goatRecentlyHitMap.containsKey(livingEntity);
    }
    private static void addToGoatRecentlyHit(LivingEntity livingEntity){
        UUID uuid = UUID.randomUUID();
        goatRecentlyHitMap.put(livingEntity,uuid);
        new BukkitRunnable(){
            @Override
            public void run() {
                UUID pulledUUID = goatRecentlyHitMap.get(livingEntity);
                if (pulledUUID == null || !pulledUUID.equals(uuid))return;
                goatRecentlyHitMap.remove(livingEntity);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),20);
    }
    public static final List<Player> goatPhysicalAttackImmune = new ArrayList<>();
    public static boolean isGoatPhysicalAttackImmune(Entity target){
        if (!(target instanceof Player))return false;
        Player player = (Player) target;
        return goatPhysicalAttackImmune.contains(player);
    }
    public static void addToGoatPhysicalAttackImmune(Player player){
        if (!goatPhysicalAttackImmune.contains(player)) goatPhysicalAttackImmune.add(player);
    }
    public static void removeFromGoatPhysicalAttackImmune(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                goatPhysicalAttackImmune.remove(player);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),10);
    }
    private static void startGoatChargeClock(Player player){
        if (isGoatCharging(player))return;
        addToGoatsCharging(player);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (debug) System.out.println("startGoatChargeClock() -> run()");
                boolean isGliding = player.isGliding();
                Vector velocity = player.getVelocity();
                if (debug) System.out.println("velocity: " + velocity); //debug
                double vx = velocity.getX();
                if (vx == 0) vx = 0.001;
                double vy = velocity.getY();
                if (vy == 0) vy = 0.001;
                double vz = velocity.getZ();
                if (vz == 0) vz = 0.001;
                double avx = Math.abs(vx);
                double avy = Math.abs(vy);
                double avz = Math.abs(vz);
                BlockFace direction = BlockFace.SELF;
                if (avx > avy && avx > avz){ // X has the highest absolute value
                    if (vx > 0){ // X is positive bound
                        direction = BlockFace.EAST;
                    }else direction = BlockFace.WEST; // X is negative bound
                }else if (avz > avx && avz > avy){ // Z has the highest absolute value
                    if (vz > 0){ // Z is positive bound
                        direction = BlockFace.SOUTH;
                    }else direction = BlockFace.NORTH; // Z is negative bound
                }else if (avy > avx && avy > avz && isGliding){ // Y has the highest absolute value (must be gliding)
                    if (vy > 0){ // Y is positive bound
                        direction = BlockFace.UP;
                    } // Y is negative bound, do not update
                }
                double speed = avx + avy + avz;
                if (speed == 0) speed = 0.001;
                Vector pointing;
                double x;
                double y;
                double z;
                if (isGliding){
                    x = (vx)/speed;
                    y = (vy)/speed;
                    z = (vz)/speed;
                }else{
                    x = (vx*0.5)/speed;
                    y = (vy*0.1)/speed;
                    z = (vz*0.5)/speed;
                }
                pointing = new Vector(x,y,z);
                Location damageOrigin;
                if (isGliding){
                    damageOrigin = player.getEyeLocation().add(pointing);
                }else damageOrigin = player.getLocation().add(0,player.getEyeHeight()*0.8,0).add(pointing);
                boolean tooSlow = false;
                if (speed < goatMinimumChargeSpeed){
                    tooSlow = true;
                    speed = 0;
                }else if (speed > goatMaximumChargeSpeed){
                    speed = goatMaximumChargeSpeed;
                }
                MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
                if ((mobHead == null || !mobHead.getEntityType().equals(EntityType.GOAT)) || tooSlow && player.isOnGround()){
                    if (debug) System.out.println("isOnGround"); //debug
                    removeFromGoatsCharging(player);
                    removeFromGoatPhysicalAttackImmune(player);
                    cancel();
                    return;
                }
//                if (debug){
//                    System.out.println("speed: " + speed); //debug
//                    player.getWorld().spawnParticle(
//                            Particle.SMALL_FLAME, damageOrigin,
//                            1,0,0,0,0
//                    );
//                }
                if (!tooSlow){
                    addToGoatPhysicalAttackImmune(player);
                    AVFX.playGoatRamTrail(player.getLocation());
                    List<LivingEntity> hitEnts = nearbyLivingEnts(damageOrigin,0.75,1.5,0.75);
                    if (hitEnts.size() != 0){
                        boolean hit = false;
                        for (LivingEntity livingEntity:hitEnts){
                            if (livingEntity.equals(player))continue;
                            if (isOnGoatRecentlyHit(livingEntity))continue;
                            hit = true;
                            addToGoatRecentlyHit(livingEntity);
                            AVFX.playGoatRamHitSound(livingEntity.getLocation());
                            double damage = Math.ceil(speed*2);
                            if (debug) System.out.println("hitEntDamage: " + damage); //debug
                            Util.addAbilityDamageData(livingEntity, EntityType.GOAT);
                            livingEntity.damage(damage,player);
                            Vector hitVel = player.getVelocity().multiply(3).setY(0.5);
                            livingEntity.setVelocity(hitVel);
                        }
                        if (hit) player.setVelocity(player.getVelocity().multiply(0.8));
                    }
                }else removeFromGoatPhysicalAttackImmune(player);
                if (!isGliding && (speed > goatMinimumBlockDamageChargeSpeed)){
                    Block originBlock = damageOrigin.getBlock();
                    //Block damagedBlock = originBlock.getRelative(direction);
                    goatBreakBlocks(player,direction,originBlock,speed);
                }
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);
    }
    private static final Map<Player, Integer> goatsOnChargeCooldown = new HashMap<>();
    private static boolean isOnGoatChargeCooldown(Player player){
        return goatsOnChargeCooldown.containsKey(player);
    }
    private static void addToGoatChargeCooldown(Player player){
        if (isOnGoatChargeCooldown(player))return;
        int cooldownMax = 20;
        goatsOnChargeCooldown.put(player,cooldownMax);
        new BukkitRunnable(){
            @Override
            public void run() {
                Integer timer = goatsOnChargeCooldown.get(player);
                if (timer == null){
                    if (debug) System.out.println("goat cooldown timer == null!");
                    timer = 0;
                }

                Hud.progressBar(player,cooldownMax,timer,false,"Charge:",false);

                timer--;
                if (timer == 0){
                    Hud.progressBarEnd(player);
                    goatsOnChargeCooldown.remove(player);
                    cancel();
                    return;
                }
                goatsOnChargeCooldown.put(player,timer);
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);
    }
    public static void goatTakeOff(Player player, boolean grounded){ //Sneak-Jump event
        if (isOnGoatChargeCooldown(player))return;
        ItemStack chest = null;
        if (player.getEquipment() != null) chest = player.getEquipment().getChestplate();
        boolean elytra = false;
        if (chest != null) elytra = chest.getType().equals(Material.ELYTRA);
        if (grounded){
            addToGoatChargeCooldown(player);
            Vector looking = player.getLocation().getDirection().multiply(0.8);
            double lx = looking.getX();
            double ly = looking.getY();
            if (ly < 0){
                ly = 0;
            }else if (ly > 0.25) ly = 0.25;
            double lz = looking.getZ();
            double yAdd = 0.25;
            if (elytra){
                yAdd = 0.2;
                player.setGliding(true);
                addToCreatureWasGliding(player);
            }
            player.setVelocity(new Vector(lx,ly + yAdd ,lz));
            AVFX.playGoatRamBeginEffect(player.getEyeLocation());
        }
        startGoatChargeClock(player);
    }

    // Ender Dragon ----------------------------------------------------------------------------------------------------
    private static final Map<Player,Integer> enderDragonBoostMap = new HashMap<>();
    private static Map<Player,Integer> getEnderDragonBoostMap(){
        return enderDragonBoostMap;
    }
    private static final int enderDragonMaxBoost = 600; //ticks. 30 seconds.
    private static boolean isOnEnderDragonBoostMap(Player player){
        return getEnderDragonBoostMap().containsKey(player);
    }
    private static void updateEnderDragonBoostMap(Player player, int boost){
        getEnderDragonBoostMap().put(player,boost);
    }
    private static void removeFromEnderDragonBoostMap(Player player){
        getEnderDragonBoostMap().remove(player);
    }
    private static int getEnderDragonBoostValue(Player player){
        Integer boost = getEnderDragonBoostMap().get(player);
        if (boost == null) boost = -1;
        return boost;
    }
    private static boolean enderDragonBoostAboveMinimum(Player player){
        return getEnderDragonBoostValue(player) > 100;
    }
    private static void enderDragonAddSpeedEffect(Player player){
        PotionEffect previousSpeed = player.getPotionEffect(PotionEffectType.SPEED);
        PotionEffect newSpeed = new PotionEffect(PotionEffectType.SPEED,-1,0,false,false,true);
        if (previousSpeed != null){
            int dur = previousSpeed.getDuration();
            int amp = previousSpeed.getAmplifier();
            if (amp >= 0 || dur > -1)return;
        }
        player.addPotionEffect(newSpeed);
    }
    private static void enderDragonRemoveSpeedEffect(Player player){
        PotionEffect speed = player.getPotionEffect(PotionEffectType.SPEED);
        if (speed == null)return;
        int dur = speed.getDuration();
        int amp = speed.getAmplifier();
        if (amp != 0 || dur != -1)return;
        player.removePotionEffect(PotionEffectType.SPEED);
    }
    private static final List<Player> enderDragonContinuousBoost = new ArrayList<>();
    private static void enderDragonBoostClock(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                boolean gliding = player.isGliding() && !player.isInWater();
                boolean sneaking = player.isSneaking();
                boolean continuous = enderDragonContinuousBoost.contains(player);
                boolean aboveMinimum = enderDragonBoostAboveMinimum(player);
                int boost = getEnderDragonBoostValue(player);
                boolean boostEmpty = boost == 0;
                MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
                boolean hasHead = mobHead != null && mobHead.getEntityType().equals(EntityType.ENDER_DRAGON);

                boolean allowBoost = (gliding && sneaking && !boostEmpty && hasHead) && (aboveMinimum || continuous);

                if (hasHead) Hud.progressBar(player,enderDragonMaxBoost,boost,true,"Boost Charge:",true);
                boost++;
                if (boost > enderDragonMaxBoost) boost = enderDragonMaxBoost;
                if (!isOnEnderDragonBoostMap(player) || (!gliding && boost == enderDragonMaxBoost)){
                    removeFromEnderDragonBoostMap(player);
                    Hud.progressBarEnd(player);
                    cancel();
                    return;
                }else{
                    updateEnderDragonBoostMap(player,boost);
                }
                if (allowBoost){
                    if (!continuous) playEnderDragonBoostSound(player);
                    if (!enderDragonContinuousBoost.contains(player)) enderDragonContinuousBoost.add(player);
                    enderDragonAddSpeedEffect(player);
                    enderDragonElytraBoost(player,false);
                }else{
                    enderDragonContinuousBoost.remove(player);
                    enderDragonRemoveSpeedEffect(player);
                }
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);
    }
    private static final List<Player> enderDragonFXCooldown = new ArrayList<>();
    private static boolean isOnEnderDragonFXCooldown(Player player){
        return enderDragonFXCooldown.contains(player);
    }
    private static void addToEnderDragonFXCooldown(Player player){
        if (!isOnEnderDragonFXCooldown(player)) enderDragonFXCooldown.add(player);
    }
    private static void removeFromEnderDragonFXCooldown(Player player){
        enderDragonFXCooldown.remove(player);
    }
    private static void playEnderDragonBoostSound(Player player){
        if (!isOnEnderDragonFXCooldown(player)){
            AVFX.playEnderDragonBoostSound(player.getEyeLocation());
            addToEnderDragonFXCooldown(player);
            new BukkitRunnable(){
                @Override
                public void run() {
                    removeFromEnderDragonFXCooldown(player);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),20);
        }
    }
    private static void enderDragonElytraBoost(Player player, boolean takeoff){
        if (takeoff){
            playEnderDragonBoostSound(player);
            if (!isOnEnderDragonBoostMap(player)){
                updateEnderDragonBoostMap(player, enderDragonMaxBoost);
                enderDragonBoostClock(player);
            }
        }else{
            int boost = getEnderDragonBoostValue(player);
            Vector velocity = player.getVelocity().multiply(1.05);
            // target speed of 1.4
            double facingY = player.getLocation().getDirection().getY();
            double newY = velocity.getY();
            double y = Math.abs(velocity.getY());
            if (facingY > 0.9){
                newY = y * 2;
            }else if (facingY > 0.5) newY = y * facingY * 1.3;
            if (newY > 1.4) newY = 1.4;
            velocity.setY(newY);
            if (debug) System.out.println("facingY: " + facingY); //debug
            double x = Math.abs(velocity.getX());
            y = Math.abs(velocity.getY());
            double z = Math.abs(velocity.getZ());
            double speed = x + y + z;
            if (speed < 1.4) player.setVelocity(velocity);
            if (debug) System.out.println("enderDragonElytraBoost()\nboost: " + boost + "\nspeed: " + speed); //debug
            if (debug) System.out.println("yVelocity: " + y); //debug

            boost = boost - 5;
            if (boost < 0) boost = 0;
            updateEnderDragonBoostMap(player,boost);
            AVFX.playEnderDragonBoostParticles(player.getLocation());
        }
    }
    public static void enderDragonElytraTakeoff(Player player, boolean grounded){
        if (player.getEquipment() == null)return;
        ItemStack chest = player.getEquipment().getChestplate();
        if (chest == null || !chest.getType().equals(Material.ELYTRA))return;
        int boost = getEnderDragonBoostValue(player);
        if (isOnEnderDragonBoostMap(player)){
            if (!enderDragonBoostAboveMinimum(player)){
                return;
            }else updateEnderDragonBoostMap(player, boost - 50);
        }
        if (grounded){
            player.setGliding(true);
            addToCreatureWasGliding(player);
            Vector facing = player.getLocation().getDirection();
            double facingY = facing.getY() * 0.1;
            facing = facing.multiply(0.7).setY(facingY);
            Vector velocity = player.getVelocity().add(facing).add(new Vector(0,0.25,0));
            player.setVelocity(velocity);
            if (!enderDragonContinuousBoost.contains(player)) enderDragonContinuousBoost.add(player);
        }
        enderDragonElytraBoost(player,true);
    }

    // Chicken ---------------------------------------------------------------------------------------------------------
    public static void chickenPickUpEgg(EntityPickupItemEvent epie){
        Player player = (Player) epie.getEntity();
        if (!player.isSneaking())return;
        Item item = epie.getItem();
        ItemStack itemStack = item.getItemStack();
        if (itemStack.getType().equals(Material.EGG)){
            epie.setCancelled(true);
        }
    }
    public static void startChickenIncubation(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!player.isSneaking()){
                    cancel();
                    return;
                }
                List<Item> eggs = player.getNearbyEntities(1,0,1)
                        .stream().filter(entity -> entity instanceof Item)
                        .filter(entity -> ((Item)entity).getItemStack().getType().equals(Material.EGG))
                        .map(Item.class::cast)
                        .collect(Collectors.toList()
                );
                if (eggs.size() == 0)return;
                Random random = new Random();
                int chance;
                List<Item> toRemove = new ArrayList<>();
                for (Item egg:eggs){
                    boolean hay = egg.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.HAY_BLOCK);
                    if (debug) System.out.println("startChickenIncubation() -> run() hay: " + hay); //debug
                    if (hay){
                        chance = 8;
                    }else chance = 4;
                    if (random.nextInt(0,10) == 0) AVFX.playChickenIncubateParticle(egg.getLocation());
                    if (hay && random.nextInt(0,20) == 0) AVFX.playChickenIncubateHayParticle(egg.getLocation());
                    boolean hit = false;
                    egg.setTicksLived(1);
                    ItemStack eggItemStack = egg.getItemStack();
                    int stackSize = eggItemStack.getAmount();
                    for (int i = 0; i < stackSize; i++) {
                        if (random.nextInt(0,10000) <= chance){
                            hit = true;
                            break;
                        }
                    }
                    if (!hit)continue;

                    Chicken spawnedChicken = (Chicken) egg.getWorld().spawnEntity(egg.getLocation(), EntityType.CHICKEN);
                    spawnedChicken.setBaby();

                    AVFX.playChickenHatchEffect(egg.getLocation());

                    stackSize--;
                    if (stackSize == 0){
                        toRemove.add(egg);
                    }else{
                        eggItemStack.setAmount(stackSize);
                        egg.setItemStack(eggItemStack);
                    }
                    if (toRemove.size() == 0)return;
                    for (Item remove:toRemove)remove.remove();
                }
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);
    }

    // Shulker ---------------------------------------------------------------------------------------------------------
    private static final Map<Player, Integer> levitationMap = new HashMap<>();
    private static Map<Player,Integer> getLevitationMap(){
        return levitationMap;
    }
    public static boolean isOnLevitationMap(Player player){
        return levitationMap.containsKey(player);
    }
    private static int getLevitationValue(Player player){
        if (isOnLevitationMap(player)) return getLevitationMap().get(player);
        return -1;
    }
    private static void removeFromLevitationMap(Player player){
        levitationMap.remove(player);
    }
    private static void putInLevitationMap(Player player, int timer){
        levitationMap.put(player, timer);
    }
    public static void resetShulkerLevitationTime(Player player){
        removeFromLevitationMap(player);
    }
    private static final int shulkerLevitationTime = 400;
    public static void startShulkerLevitation(Player player){
        //boolean isOnMap = isOnLevitationMap(player);
        boolean grounded = player.isOnGround() || player.isInWater();
        int timer = getLevitationValue(player);
        if (grounded || timer == 0)return;
        AVFX.playShulkerAfflictionEffect(player.getLocation(),player.getEyeHeight(),true);
        new BukkitRunnable(){
            @Override
            public void run() {
                int timer = getLevitationValue(player);
                if (debug) System.out.println("startShulkerLevitation() -> run() timer: " + timer); //debug
                if (timer == -1) timer = shulkerLevitationTime;
                boolean grounded = player.isOnGround() || player.isInWater();
                boolean sneaking = player.isSneaking();
                MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
                boolean correctHead = mobHead != null && mobHead.getEntityType().equals(EntityType.SHULKER);
                if (timer == 0 || grounded || !sneaking || !correctHead){
                    Hud.progressBarEnd(player);
                    PotionEffect levitationEffect = player.getPotionEffect(PotionEffectType.LEVITATION);
                    if (levitationEffect != null) {
                        int amp = levitationEffect.getAmplifier();
                        int dur = levitationEffect.getDuration();
                        if (amp == 2 && dur == -1) player.removePotionEffect(PotionEffectType.LEVITATION);
                    }
                    PotionEffect speedEffect = player.getPotionEffect(PotionEffectType.SPEED);
                    if (speedEffect != null) {
                        int amp = speedEffect.getAmplifier();
                        int dur = speedEffect.getDuration();
                        if (amp == 0 && dur == -1) player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    if (!correctHead) resetShulkerLevitationTime(player);
                    cancel();
                }else{
                    AVFX.shulkerLevitateParticles(player.getLocation());
                    addEffectToEntity(player,
                            new PotionEffect(PotionEffectType.LEVITATION, -1,2,false,false,true)
                    );
                    addEffectToEntity(player,
                            new PotionEffect(PotionEffectType.SPEED, -1,0,false,false,true)
                    );
                    timer--;
                    putInLevitationMap(player,timer);
                    Hud.progressBar(player,shulkerLevitationTime,timer,true,"Levitation Time:",true);
                }
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);

    }

    // Elder Guardian --------------------------------------------------------------------------------------------------
    private static void runElderGuardianAfflictionFX(LivingEntity target){
        boolean hasFatigue = target.hasPotionEffect(PotionEffectType.SLOW_DIGGING);
        if (!hasFatigue) AVFX.playElderGuardianFatigueSound(target.getEyeLocation());
    }

    // Parrot ----------------------------------------------------------------------------------------------------------
    private static final List<LivingEntity> parrotCooldown = new ArrayList<>();
    private static boolean isOnParrotCooldown(LivingEntity target){
        return parrotCooldown.contains(target);
    }
    private static void addToParrotCooldown(LivingEntity target){
        if (!isOnParrotCooldown(target)) parrotCooldown.add(target);
    }
    private static void removeFromParrotCooldown(LivingEntity target){
        parrotCooldown.remove(target);
    }
    public static void parrotTargeted(LivingEntity targeted, Mob targeter){
        if (isOnParrotCooldown(targeted))return;
        Sound warnSound;
        try{
            warnSound = Sound.valueOf("ENTITY_PARROT_IMITATE_" + targeter.getType().toString());
        }catch (IllegalArgumentException e){
            return;
        }

        AVFX.playParrotWarnSound(targeted.getEyeLocation(), targeter.getEyeLocation(), warnSound);
        addToParrotCooldown(targeted);
        new BukkitRunnable(){
            @Override
            public void run() {
                removeFromParrotCooldown(targeted);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),10);
    }

    // Sheep -----------------------------------------------------------------------------------------------------------
    private static final Set<Material> sheepEdibleList = Set.of(
            Material.GRASS_BLOCK, Material.GRASS, Material.TALL_GRASS, Material.PODZOL, Material.MYCELIUM,
            Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM, Material.NETHER_SPROUTS, Material.CRIMSON_ROOTS,
            Material.WARPED_ROOTS, Material.HANGING_ROOTS
    );
    private static final Map<Material,Integer> sheepEatHungerValueMap = Map.of( // default 1
            Material.PODZOL, 2,
            Material.MYCELIUM,2,
            Material.CRIMSON_NYLIUM,2,
            Material.WARPED_NYLIUM,2
    );
    private static final Map<Material,Material> sheepEatTransformMaterialMap = Map.of( // default air
            Material.GRASS_BLOCK, Material.DIRT,
            Material.PODZOL, Material.DIRT,
            Material.MYCELIUM, Material.DIRT,
            Material.CRIMSON_NYLIUM, Material.NETHERRACK,
            Material.WARPED_NYLIUM, Material.NETHERRACK
    );
    private static final List<Material> sheepEatRequiredTopBlockFace = List.of( // default false
            Material.GRASS_BLOCK, Material.PODZOL, Material.MYCELIUM, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM
    );
    private static final Map<Material,PotionEffect> sheepEatEffectMap = Map.of( // default none
            Material.PODZOL, PotionEffectManager.buildSimpleEffect(PotionEffectType.SLOW,1,10*20),
            Material.MYCELIUM, PotionEffectManager.buildSimpleEffect(PotionEffectType.CONFUSION,1,10*20),
            Material.CRIMSON_ROOTS, PotionEffectManager.buildSimpleEffect(PotionEffectType.FIRE_RESISTANCE,1,120*20),
            Material.WARPED_ROOTS, PotionEffectManager.buildSimpleEffect(PotionEffectType.FIRE_RESISTANCE,1,60*20),
            Material.HANGING_ROOTS, PotionEffectManager.buildSimpleEffect(PotionEffectType.REGENERATION,1,30*20)
    );
    private static final Map<Material,Double> sheepEatEffectOffset = Map.of( // default 0.5
            Material.GRASS_BLOCK, 1.0,
            Material.PODZOL,1.0,
            Material.MYCELIUM,1.0,
            Material.CRIMSON_NYLIUM,1.0,
            Material.WARPED_NYLIUM,1.0
    );
    private static final List<Player> sheepEatCooldown = new ArrayList<>();
    private static boolean isOnSheepEatCooldown(Player player){
        return sheepEatCooldown.contains(player);
    }
    private static void addToSheepCooldown(Player player){
        sheepEatCooldown.add(player);
    }
    private static void removeFromSheepCooldown(Player player){
        sheepEatCooldown.remove(player);
    }
    public static void sheepEatGrass(Player player, Block block, BlockFace blockFace){
        if (isOnSheepEatCooldown(player))return;
        Material eatMat = block.getType();
        if (!sheepEdibleList.contains(eatMat))return;
        boolean requiredTopFace = sheepEatRequiredTopBlockFace.contains(eatMat);
        if (requiredTopFace && blockFace != BlockFace.UP)return;
        int hunger = player.getFoodLevel();
        if (hunger == 20)return;

        Integer hungerRestored = sheepEatHungerValueMap.get(eatMat);
        if (hungerRestored == null)hungerRestored = 1;
        float saturationRestored = (float) hungerRestored;
        PotionEffect eatEffect = sheepEatEffectMap.get(eatMat);
        Material transformMaterial = sheepEatTransformMaterialMap.get(eatMat);
        if (transformMaterial == null)transformMaterial = Material.AIR;

        int hungerSum = hunger + hungerRestored;
        if (hungerSum > 20) hungerSum = 20;
        float saturationSum = player.getSaturation() + saturationRestored;
        if (saturationSum > 20f) saturationSum = 20f;

        player.setFoodLevel(hungerSum);
        player.setSaturation(saturationSum);
        if (eatEffect != null) PotionEffectManager.addEffectToEntity(player,eatEffect);

        Double offset = sheepEatEffectOffset.get(eatMat);
        if (offset == null) offset = 0.5;
        Location blockLoc = block.getLocation().add(0.5,offset,0.5);
        Location playerMouthLoc = player.getEyeLocation().add(0.0,-0.25,0.0).add(player.getLocation().getDirection().multiply(0.5));
        AVFX.playSheepEatEffect(blockLoc,block,playerMouthLoc);

        block.setType(transformMaterial);

        addToSheepCooldown(player);
        new BukkitRunnable(){
            @Override
            public void run() {
                removeFromSheepCooldown(player);
            }
        }.runTaskLater(plugin,10);
    }

    // Zombie (Group) --------------------------------------------------------------------------------------------------
    public static void zombifiedHunger(EntityType headType, FoodLevelChangeEvent flce){
        List<EntityType> types = List.of(
                EntityType.HUSK, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIFIED_PIGLIN, EntityType.DROWNED, EntityType.ZOGLIN
        );
        if (!types.contains(headType))return;
        Player player = (Player) flce.getEntity();
        //if (e.getItem() != null)return;
        int currentLv = player.getFoodLevel();
        int newLv = flce.getFoodLevel();
        if (debug) System.out.println("zombifiedHunger()\ncurrentLv: " + currentLv + "\nnewLv " + newLv); //debug
        if (newLv < currentLv && currentLv < 20) flce.setCancelled(true);
    }

    // Cow (Group) -----------------------------------------------------------------------------------------------------
    private static final List<Player> milkingCooldownList = new ArrayList<>();
    private static boolean isOnMilkingCooldown(Player milker){
        return milkingCooldownList.contains(milker);
    }
    private static void addToMilkingCooldown(Player milker){
        milkingCooldownList.add(milker);
    }
    private static void removeFromMilkingCooldown(Player milker){
        milkingCooldownList.remove(milker);
    }
    private static void runMilkingCooldown(Player milker){
        addToMilkingCooldown(milker);
        new BukkitRunnable(){
            @Override
            public void run() {
                removeFromMilkingCooldown(milker);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),20);
    }
    private static final Set<Material> cowHarvestMats = Set.of(Material.BUCKET, Material.BOWL);
    public static void milkCows(Player milkingPlayer, LivingEntity milkedEnt, MobHead mobHead){
        if (isOnMilkingCooldown(milkingPlayer))return;
        ItemStack mainHand = milkingPlayer.getInventory().getItemInMainHand();
        ItemStack offHand = milkingPlayer.getInventory().getItemInOffHand();
        EquipmentSlot hand = EquipmentSlot.HAND;
        ItemStack harvestItem = milkingPlayer.getItemInUse();
        if (cowHarvestMats.contains(offHand.getType())){
            hand = EquipmentSlot.OFF_HAND;
            harvestItem = offHand;
        }
        if (cowHarvestMats.contains(mainHand.getType())){
            hand = EquipmentSlot.HAND;
            harvestItem = mainHand;
        }
        boolean self = milkingPlayer.equals(milkedEnt);
        boolean openHand = harvestItem == null;
        if (self && openHand)return;
        Location location = milkedEnt.getLocation();
        EntityType cowType = mobHead.getEntityType();
        boolean stew = false;
        boolean sus = false;
        boolean sneaking = milkingPlayer.isSneaking();
        if (self && !sneaking)return;
        if (cowType.equals(EntityType.MUSHROOM_COW)) stew = true;
        if (mobHead.getVariant() != null && mobHead.getVariant().equals("BROWN")) sus = true;
        List<PotionEffect> addedEffects = new ArrayList<>();
        if (sus) addedEffects = getBrownMooshroomEffects(milkedEnt);
        if (openHand){
            if (stew && !sneaking){
                int hunger = milkingPlayer.getFoodLevel();
                float saturation = milkingPlayer.getSaturation();
                if (hunger >= 20 && !sus)return;
                hunger = hunger + 2;
                if (hunger > 20) hunger = 20;
                saturation = saturation + 1f;
                if (saturation > 20f) saturation = 20f;
                milkingPlayer.setFoodLevel(hunger);
                milkingPlayer.setSaturation(saturation);
                addEffectsToEntity(milkingPlayer, addedEffects);
                runMilkingCooldown(milkingPlayer);
                AVFX.playMooshroomSoupingSounds(location,false);
            }else{
                List<PotionEffectType> toRemove = new ArrayList<>();
                for (PotionEffect effect:milkingPlayer.getActivePotionEffects())toRemove.add(effect.getType());
                for (PotionEffectType type:toRemove) milkingPlayer.removePotionEffect(type);
                MobHead milkingHead = MobHead.getMobHeadWornByEntity(milkingPlayer);
                if (milkingHead != null && Groups.isSkeletal(milkingHead.getEntityType())) skeletonDrinkMilk(milkingPlayer);
                runMilkingCooldown(milkingPlayer);
                AVFX.playCowMilkingSounds(location,false);
            }
        }else{
            if (harvestItem.getType().equals(Material.BUCKET)){
                giveMilkBucket(milkingPlayer, hand);
                runMilkingCooldown(milkingPlayer);
                AVFX.playCowMilkingSounds(location,true);
            }else if (stew && harvestItem.getType().equals(Material.BOWL)){
                giveSoupBowl(milkingPlayer, hand, addedEffects);
                runMilkingCooldown(milkingPlayer);
                AVFX.playMooshroomSoupingSounds(location,true);
            }
        }
    }
    private static void giveMilkBucket(Player milker, EquipmentSlot hand){
        PlayerInventory inv = milker.getInventory();
        ItemStack bucket;
        if (hand.equals(EquipmentSlot.HAND)){
            bucket = inv.getItemInMainHand();
        }else bucket = inv.getItemInOffHand();
        if (!bucket.getType().equals(Material.BUCKET))return;
        int stackSize = bucket.getAmount();
        stackSize--;
        bucket.setAmount(stackSize);
        Map<Integer,ItemStack> overflow = inv.addItem(new ItemStack(Material.MILK_BUCKET));
        if (overflow.size() > 0){
            World world = milker.getWorld();
            for (ItemStack itemStack: overflow.values()){
                world.dropItem(milker.getLocation(),itemStack);
            }
        }
    }
    private static void giveSoupBowl(Player souper, EquipmentSlot hand, List<PotionEffect> effects){
        PlayerInventory inv = souper.getInventory();
        ItemStack bowl;
        if (hand.equals(EquipmentSlot.HAND)){
            bowl = inv.getItemInMainHand();
        }else bowl = inv.getItemInOffHand();
        if (!bowl.getType().equals(Material.BOWL))return;
        int stackSize = bowl.getAmount();
        stackSize--;
        bowl.setAmount(stackSize);
        ItemStack stew;
        if(effects.size() == 0){
            stew = new ItemStack(Material.MUSHROOM_STEW);
        }else{
            stew = new ItemStack(Material.SUSPICIOUS_STEW);
            SuspiciousStewMeta meta = (SuspiciousStewMeta) stew.getItemMeta();
            assert meta != null;
            List<String> lore = new ArrayList<>();
            for (PotionEffect potionEffect:effects){
                meta.addCustomEffect(potionEffect,true);
                String name = StringBuilder.friendlyStringConversion(potionEffect.getType().getName());
                double seconds = (int) Math.floor(potionEffect.getDuration() * 0.05);
                if (seconds == 0) seconds = potionEffect.getDuration() * 0.05;
                String time = String.valueOf(seconds);
                int period = 0;
                for (int i = 0; i < time.length(); i++) {
                    char c = time.charAt(i);
                    if (c == '.'){
                        period = i;
                        break;
                    }
                }
                time = time.substring(0, period + 2);
                if (time.endsWith(".0")) time = time.substring(0,time.length()-2);
                lore.add(name + " for " + time + " seconds.");
            }
            meta.setLore(lore);
            stew.setItemMeta(meta);
        }
        Map<Integer,ItemStack> overflow = inv.addItem(stew);
        if (overflow.size() > 0){
            World world = souper.getWorld();
            for (ItemStack itemStack: overflow.values()){
                world.dropItem(souper.getLocation(),itemStack);
            }
        }
    }
    private static List<PotionEffect> getBrownMooshroomEffects(LivingEntity cow){
        List<Block> nearby = new ArrayList<>();
        Location origin = cow.getLocation();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                for (int y = 0; y < 2; y++) {
                    Location loc = origin.clone().add(x,y,z);
                    nearby.add(loc.getBlock());
                    if (debug){
                        Location debugLoc = loc.clone().getBlock().getLocation().add(0.5,0.5,0.5);
                        origin.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE,debugLoc,1,0,0,0,0,null);
                    }
                }
            }
        }
        Set<Material> validTypes = getBrownMushroomFlowerTypes();
        List<Material> flowers = new ArrayList<>();
        for (Block block:nearby){
            Material blockType = block.getType();
            if (validTypes.contains(blockType)){
                flowers.add(blockType);
            }
        }
        if (cow instanceof Player){
            List<Material> heldItems = new ArrayList<>();
            PlayerInventory inv = ((Player)cow).getInventory();
            heldItems.add(inv.getItemInMainHand().getType());
            heldItems.add(inv.getItemInOffHand().getType());
            for (Material held:heldItems){
                if (validTypes.contains(held)){
                    flowers.add(held);
                }
            }
        }
        Map<PotionEffectType, Integer> effectTypeMultiplierMap = new HashMap<>();
        List<PotionEffect> effects = new ArrayList<>();
        for (Material flower:flowers){
            PotionEffectType type = brownMushroomFlowerEffectMap().get(flower);
            if (effectTypeMultiplierMap.containsKey(type)){
                int count = effectTypeMultiplierMap.get(type);
                effectTypeMultiplierMap.put(type, count + 1);
            }else effectTypeMultiplierMap.put(type, 1);
        }
        for (PotionEffectType type:effectTypeMultiplierMap.keySet()){
            int multiplier = effectTypeMultiplierMap.get(type);
            int duration = brownMushroomFlowerTimeMap().get(type) * multiplier;
            effects.add(new PotionEffect(type,duration,0,false,true,true));
        }
        return effects;
    }
    private static Set<Material> getBrownMushroomFlowerTypes(){
        return brownMushroomFlowerEffectMap().keySet();
    }
    private static Map<Material, PotionEffectType> brownMushroomFlowerEffectMap(){
        Map<Material, PotionEffectType> map = new HashMap<>();
        map.put(Material.ALLIUM, PotionEffectType.FIRE_RESISTANCE);
        map.put(Material.AZURE_BLUET, PotionEffectType.BLINDNESS);
        map.put(Material.BLUE_ORCHID, PotionEffectType.SATURATION);
        map.put(Material.DANDELION, PotionEffectType.SATURATION);
        map.put(Material.CORNFLOWER, PotionEffectType.JUMP);
        map.put(Material.LILY_OF_THE_VALLEY, PotionEffectType.POISON);
        map.put(Material.OXEYE_DAISY, PotionEffectType.REGENERATION);
        map.put(Material.POPPY, PotionEffectType.NIGHT_VISION);
        map.put(Material.TORCHFLOWER, PotionEffectType.NIGHT_VISION);
        map.put(Material.ORANGE_TULIP, PotionEffectType.WEAKNESS);
        map.put(Material.PINK_TULIP, PotionEffectType.WEAKNESS);
        map.put(Material.RED_TULIP, PotionEffectType.WEAKNESS);
        map.put(Material.WHITE_TULIP, PotionEffectType.WEAKNESS);
        map.put(Material.WITHER_ROSE, PotionEffectType.WITHER);
        return map;
    }
    private static Map<PotionEffectType, Integer> brownMushroomFlowerTimeMap(){ //Integer is Time in Ticks
        Map<PotionEffectType, Integer> map = new HashMap<>();
        map.put(PotionEffectType.FIRE_RESISTANCE, 4*20);
        map.put(PotionEffectType.BLINDNESS, 8*20);
        map.put(PotionEffectType.SATURATION, 7);
        map.put(PotionEffectType.JUMP, 6*20);
        map.put(PotionEffectType.POISON, 12*20);
        map.put(PotionEffectType.REGENERATION, 8*20);
        map.put(PotionEffectType.NIGHT_VISION, 5*20);
        map.put(PotionEffectType.WEAKNESS, 9*20);
        map.put(PotionEffectType.WITHER, 8*20);
        return map;
    }

}
