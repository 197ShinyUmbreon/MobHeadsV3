package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.Decollation;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Data;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class HeadItemDrop {

    private static final List<EntityType> bossMonsters = List.of(
            EntityType.WITHER, EntityType.ENDER_DRAGON, EntityType.ELDER_GUARDIAN, EntityType.WARDEN
    );

    public static void creatureDeath(EntityDeathEvent eDeathe, EntityDamageEvent eDamagee){
        if (eDamagee == null || eDamagee.isCancelled())return;
        boolean success = false;
        boolean decollation;
        LivingEntity killed = eDeathe.getEntity();
        if (debug) System.out.println("creatureDeath()\n    killed: " + killed.getName()); //debug
        if (killed.getType().equals(EntityType.WITHER_SKELETON)){
            success = witherSkeletonHead(eDeathe);
        }
        if (bossMonsters.contains(killed.getType())) success = true;
        Entity killer;
        if (eDamagee instanceof EntityDamageByEntityEvent){
            killer = Util.getTrueAttacker(((EntityDamageByEntityEvent)eDamagee).getDamager());
        }else killer = killed.getKiller();
        if (killer == null)return;
        if (debug) System.out.println("    killer: " + killer.getName()); //debug
        ItemStack weapon = null;
        int weaponLocation = 0;
        UUID weaponUUID = null;
        // 0 == Main Hand
        // 1 == Off Hand
        // 2 == Trident Entity
        // 3 == Head lol
        if (!success){
            if (!(killer instanceof LivingEntity)){
                return;
            }else if (killer.getType().equals(EntityType.CREEPER)){
                Creeper creeper = (Creeper) killer;
                boolean chargedKill = !Data.vanillaHeadEntTypes.contains(killed.getType()) && creeper.isPowered() && creeper.getFuseTicks() == creeper.getMaxFuseTicks();
                if (chargedKill){
                    success = true;
                }else return;
            }else if (!(killer instanceof Player)){
                LivingEntity livingKiller = (LivingEntity) killer;
                boolean isSummon = Summon.entityIsSummon(livingKiller);
                if (!isSummon) return;
            }else{
                Player killingPlayer = (Player) killer;
                if (killingPlayer != killed && killed.getType().equals(EntityType.PLAYER)) success = true;
                weapon = killingPlayer.getInventory().getItemInMainHand();
                ItemStack offHand = killingPlayer.getInventory().getItemInOffHand();
                EntityDamageEvent lastDamageEvent = killed.getLastDamageCause();
                if (lastDamageEvent instanceof EntityDamageByEntityEvent){
                    EntityDamageByEntityEvent lastDamageByEntityEvent = (EntityDamageByEntityEvent) lastDamageEvent;
                    EntityDamageEvent.DamageCause cause = lastDamageByEntityEvent.getCause();
                    if (Util.hasTakenAbilityDamage(killed)) cause = EntityDamageEvent.DamageCause.CUSTOM;
                    if (debug) System.out.println("damageCause: " + cause); //debug
                    if (cause.equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                        Entity damager = lastDamageByEntityEvent.getDamager();
                        if (damager instanceof Trident){
                            Trident trident = (Trident) damager;
                            weapon = trident.getItem();
                            weaponLocation = 2;
                            weaponUUID = trident.getUniqueId();
                        }else{
                            Material mainHandMat = weapon.getType();
                            Material offHandMat = offHand.getType();
                            List<Material> projectileWeapons = List.of(Material.BOW, Material.CROSSBOW, Material.TRIDENT);
                            if (!projectileWeapons.contains(mainHandMat) && projectileWeapons.contains(offHandMat)){
                                weapon = offHand;
                                weaponLocation = 1;
                            }
                        }
                    }else if (cause.equals(EntityDamageEvent.DamageCause.CUSTOM)){
                        weapon = null;
                        if (killingPlayer.getEquipment() != null){
                            weapon = killingPlayer.getEquipment().getHelmet();
                            weaponLocation = 3;
                        }
                    }
                }
                if (debug) System.out.println("weapon:" + weapon); //debug
                decollation = Decollation.hasDecollation(weapon);
                if (Decollation.isDecollationPearlItem(weapon)) decollation = false;
                if (debug)System.out.println("decollation: " + decollation); //debug
                if (decollation){
                    success = true;
                    ItemStack newWeapon = Decollation.removeDecollation(weapon);
                    if (newWeapon == null) newWeapon = weapon;
                    if (weaponLocation == 0){
                        killingPlayer.getInventory().setItemInMainHand(newWeapon);
                    }else if (weaponLocation == 1){
                        killingPlayer.getInventory().setItemInOffHand(newWeapon);
                    }else if (weaponLocation == 2){
                        Entity bukkitEnt = Bukkit.getEntity(weaponUUID);
                        if (bukkitEnt instanceof Trident){
                            Trident trident = (Trident) bukkitEnt;
                            trident.setItem(newWeapon);
                        }
                    }
                }
            }
        }
        if (!success) success = dropSuccess(weapon);
        if (!success) return;
        eDeathe.getDrops().add(MobHead.getHeadItemOfEntity(killed));
        AVFX.playHeadDropEffect(killed.getEyeLocation());
    }

    private static boolean dropSuccess(ItemStack wielding){
        int max = 200;
        int target = 5;
        boolean guaranteed = false;
        if (wielding != null && wielding.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)){
            int lv = wielding.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
            target = target + lv*2;
        }
        Random random = new Random();
        int roll = random.nextInt(0,max);
        return guaranteed || roll <= target;
    }

    public static void dropHead(Location dropLoc, Entity target){
        if (!(target instanceof LivingEntity))return;
        LivingEntity livTarget = (LivingEntity)target;

        ItemStack headItem = MobHead.getHeadItemOfEntity(livTarget);
        if (headItem == null)return;

        World world = dropLoc.getWorld();
        if (world == null)return;
        world.dropItemNaturally(dropLoc,headItem);
        AVFX.playHeadDropEffect(dropLoc);
    }

    private static boolean witherSkeletonHead(EntityDeathEvent deathEvent){
        boolean skull = false;
        for (ItemStack drop:deathEvent.getDrops()){
            if (drop.getType().equals(Material.WITHER_SKELETON_SKULL)){
                skull = true;
                break;
            }
        }
        if (skull){
            List<ItemStack> drops = new ArrayList<>();
            for (ItemStack drop:deathEvent.getDrops()){
                if (!drop.getType().equals(Material.WITHER_SKELETON_SKULL)) drops.add(drop);
            }
            deathEvent.getDrops().clear();
            deathEvent.getDrops().addAll(drops);
            return true;
        }
        return false;
    }

}
