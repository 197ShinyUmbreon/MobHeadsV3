package io.github.shinyumbreon197.mobheadsv3;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class Packets {

    //private static final WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    private static ProtocolManager pm;
    private static final boolean enabled = MobHeadsV3.protocolLibEnabled;
    public static void initialize(){
        pm = ProtocolLibrary.getProtocolManager();
    }

//    public static void makePlayerJump(Player player){
//        PacketContainer packet = pm.createPacket(PacketType.Play.Client.ENTITY_ACTION);
//
//    }

    // Creaking Particles & Entity Spawning ----------------------------------------------------------------------------
    public static void creakingHeartGlowBlock(Player player, Block block){
        if (!enabled)return;
        Location origin = block.getLocation().add(0.5,0.5,0.5);
        PacketContainer packet0 = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        int entityId = new Random().nextInt();
        packet0.getIntegers().write(0, entityId);
        packet0.getUUIDs().write(0, UUID.randomUUID());
        packet0.getEntityTypeModifier().write(0, EntityType.BLOCK_DISPLAY);
        packet0.getDoubles()
                .write(0,origin.getX())
                .write(1,origin.getY())
                .write(2,origin.getZ())
        ;



        pm.sendServerPacket(player,packet0);

        PacketContainer packet1 = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet1.getIntegers().write(0,entityId);
        WrappedBlockData blockData = WrappedBlockData.createData(Material.AIR);
        //packet1.getBlockData().write(0, blockData);
        var serializer = WrappedDataWatcher.Registry.getBlockDataSerializer(false);
        WrappedDataValue blockDataObject = new WrappedDataValue(23, serializer, BukkitConverters.getWrappedBlockDataConverter().getGeneric(blockData));
        packet1.getDataValueCollectionModifier().write(0, List.of(blockDataObject));
        Vector3F scale = new com.comphenix.protocol.wrappers.Vector3F(0.8f, 0.8f, 0.8f);
        WrappedDataValue blockDataObject2 = new WrappedDataValue(12, WrappedDataWatcher.Registry.get(Vector3F.getMinecraftClass()), Vector3F.getConverter().getGeneric(scale));
        packet1.getDataValueCollectionModifier().write(0, List.of(blockDataObject));

        //packet1.getVectors().write(1, new Vector(0.8, 0.8,0.8));
        packet1.getIntegers().write(6,1);
        pm.sendServerPacket(player,packet1);
    }
    // Suspicious Particles --------------------------------------------------------------------------------------------
    private static Color susColor(int type){
        if (type == 0){ // sus block
            return Color.fromRGB(255,60,10);
        }else if (type == 1){ // Monster spawner
            return Color.fromRGB(60,60,60);
        }else if (type == 2){ // Unlooted Chest
            return Color.fromRGB(181, 101, 29);
        }else return Color.BLACK;
    }
    public static void susParticles(Player player, Location origin, int type){
        if (!enabled)return;
        for (int i = 1; i < 5; i++) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    Random random = new Random();
                    PacketContainer packet = pm.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                    packet.getNewParticles().write(0, WrappedParticle.create(Particle.ENTITY_EFFECT, susColor(type)));
                    packet.getIntegers().write(0,0);
                    packet.getDoubles()
                            .write(0, origin.getX() + random.nextDouble(-0.8,0.8)) // Loc X
                            .write(1, origin.getY()+ random.nextDouble(-0.8,0.2)) // Loc Y
                            .write(2, origin.getZ() + random.nextDouble(-0.8,0.8)) // Loc Z
                    ;
                    pm.sendServerPacket(player,packet);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(), i * 4);
        }
    }
    public static void susTrailParticles(Player player, Location origin, Location destination, int type){
        if (!enabled)return;
        Vector originPoint = origin.toVector();
        Vector destPoint = destination.toVector();
        if (debug){
            System.out.println("originPoint: " + originPoint);
            System.out.println("destPoint: " + destPoint);
        }
        double step = 1.2;
        Vector direction = destPoint.clone().subtract(originPoint).normalize().multiply(step);
        double distance = originPoint.distance(destPoint);
        if (debug) System.out.println("Sus distance: " + distance);
        float size = (float) ((100 - (distance * 5)) * 0.01);
        if (debug) System.out.println("Sus Size: " + size);
        if (size > 1f){
            size = 1f;
        }else if (size < 0.1f){
            size = 0.1f;
        }
        if (debug) System.out.println("Sus Size Modified: " + size);
        Vector current = originPoint.clone();
        for (double i = 0; i < distance; i = i + step) {
            current = current.clone().add(direction);
            PacketContainer packet = pm.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
            packet.getNewParticles().write(0, WrappedParticle.create(Particle.DUST, new Particle.DustOptions(susColor(type), size)));
            packet.getIntegers().write(0,0);
            packet.getDoubles()
                    .write(0, current.getX()) // Loc X
                    .write(1, current.getY()) // Loc Y
                    .write(2, current.getZ()); // Loc Z
//            packet.getFloat()
//                    .write(0, (float) 100/255) // Offset X // Red
//                    .write(1, (float) 100/255) // Offset Y // Green
//                    .write(2, (float) 100/255) // Offset Z // Blue
//                    .write(3, 1f); // Scale // Clamped between 0.01 and 4
            pm.sendServerPacket(player,packet);
        }
    }

    // Entity Glow -----------------------------------------------------------------------------------------------------
    private static WrappedDataValue entityDataClearAll;
    private static WrappedDataValue getEntityDataClearAll(){
        if (entityDataClearAll == null){
            entityDataClearAll = new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x00);
        }
        return entityDataClearAll;
    }
    private static WrappedDataValue entityDataGlow;
    private static WrappedDataValue getEntityDataGlow(){
        if (entityDataGlow == null){
            entityDataGlow = new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x40);
        }
        return entityDataGlow;
    }

    private static final Map<Player, List<Entity>> glowingEnts = new HashMap<>();
    private static final Set<EntityType> glowingExclusions = Set.of(
            EntityType.ITEM_FRAME, EntityType.ITEM_DISPLAY, EntityType.GLOW_ITEM_FRAME, EntityType.BLOCK_DISPLAY,
            EntityType.INTERACTION, EntityType.MARKER, EntityType.PAINTING, EntityType.TEXT_DISPLAY
    );
    public static void nearbyGlow(Player player, int radius){
        if (!enabled)return;
        List<Entity> wereNearby;
        if (glowingEnts.containsKey(player)){
            wereNearby = glowingEnts.get(player);
        }else wereNearby = new ArrayList<>();
        List<Entity> nearbyEnts = player.getNearbyEntities(radius,radius,radius);
        nearbyEnts.remove(player);
        for (Entity nearby:nearbyEnts){
            if (glowingExclusions.contains(nearby.getType()))continue;
            toggleGlow(player,nearby,true);
        }
        wereNearby.removeAll(nearbyEnts);
        for (Entity were:wereNearby){
            toggleGlow(player,were,false);
        }
        glowingEnts.put(player,nearbyEnts);
    }
    public static void removeGlow(Player player){
        if (!enabled)return;
        if (!glowingEnts.containsKey(player))return;
        for (Entity remove:glowingEnts.get(player)){
            toggleGlow(player,remove,false);
        }
        glowingEnts.remove(player);
    }
    public static void toggleGlow(Player player, Entity target, boolean glowEnabled){
        if (!enabled)return;
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataValue value;
        if (glowEnabled){
            value = getEntityDataGlow();
        }else value = getEntityDataClearAll();
        packet.getDataValueCollectionModifier().write(0, Collections.singletonList(value));
        packet.getIntegers().write(0, target.getEntityId());
        pm.sendServerPacket(player,packet);
    }

    // Auto Fishing ----------------------------------------------------------------------------------------------------
//    public static void autoFish(Player player){
//        if (!enabled)return;
//        PacketContainer packet = pm.createPacket(PacketType.Play.Client.USE_ITEM);
//        packet.getHands().writeDefaults();
//        pm.sendServerPacket(player,packet);
//    }


//                  THIS CODE WORKS VVVVVVVVVVVVVV
//    public static void blindEffect(Player player, boolean enable){
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_EFFECT);
//        packet.getIntegers()
//                .write(0, player.getEntityId())
//                .write(1, -1);
//        packet.getEffectTypes().writeDefaults().write(0,PotionEffectType.BLINDNESS);
//        pm.sendServerPacket(player, packet);
//    }

//    @EventHandler
//    public static void test(PlayerDropItemEvent e){
//        Player player = e.getPlayer();
//        testPacket(player);
//    }

//    public  static void frogEdibleUpdatePacket(Player player, LivingEntity target){
//        int targetID = target.getEntityId();
//        MobEffect glowing = MobEffect.byId(24);
//        System.out.println("glowing = "+glowing); //debug
//        if (glowing == null)return;
//        MobEffectInstance mobEffectInstance = new MobEffectInstance(
//               glowing , -1, 0, false, true, false
//        );
//
//        ClientboundUpdateMobEffectPacket packet = new ClientboundUpdateMobEffectPacket(
//                targetID, mobEffectInstance
//        );
//        sendPacket(player, packet);
//    }
//
//    public static void frogEdibleUpdatePacket1(Player player, LivingEntity target){
//        int targetID = target.getEntityId();
//        CraftLivingEntity craftTarget = ((CraftLivingEntity) target);
//
//        SynchedEntityData data = new SynchedEntityData(craftTarget.getHandle());
//        //data.set(new EntityDataAccessor<>(targetID, ));
//
//        List<SynchedEntityData.DataValue<?>> dataValues = data.getNonDefaultValues();
//        System.out.println(dataValues); //debug
//
//        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
//                targetID, dataValues
//        );
//
//
//    }
//    public static void frogEdibleUpdatePacket2(Player player, LivingEntity target){
//        //((CraftPlayer) player).getHandle().;
//    }
//    public  static void frogEdibleRemovePacket(Player player, LivingEntity target){
//        for (PotionEffect potionEffect: target.getActivePotionEffects()){
//            if (potionEffect.getType().equals(PotionEffectType.GLOWING))return;
//        }
//        int targetID = target.getEntityId();
//        MobEffect glowing = MobEffect.byId(24);
//        if (glowing == null)return;
//
//        ClientboundRemoveMobEffectPacket packet = new ClientboundRemoveMobEffectPacket(targetID, glowing);
//        sendPacket(player, packet);
//    }
//
//    private static void testPacket(Player player){
//        ResourceLocation sound = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, Sound.BLOCK_BEACON_ACTIVATE.getKey().getKey());
//        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(sound);
//        Holder<SoundEvent> holder = Holder.direct(soundEvent);
//        SoundSource soundSource = SoundSource.AMBIENT;
//        int x = player.getLocation().getBlockX();
//        int y = player.getLocation().getBlockY();
//        int z = player.getLocation().getBlockZ();
//
//        Random random = new Random();
//        ClientboundSoundPacket packet = new ClientboundSoundPacket(holder, soundSource, x, y, z, 1.0f, 1.0f, random.nextLong());
//
//        sendPacket(player, packet);
//    }
//
//    private static void sendPacket(Player player, net.minecraft.network.protocol.Packet<?> packet){
//        ServerGamePacketListenerImpl listener = ((CraftPlayer)player).getHandle().connection;
//        listener.send(packet);
//    }

}
