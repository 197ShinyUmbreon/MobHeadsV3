package io.github.shinyumbreon197.mobheadsv3.tool;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Serializer {

    public static String serializeItemStack(ItemStack itemStack){
        String encodedItem;
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);
            boos.writeObject(itemStack);
            boos.flush();

            byte[] serializedObject = baos.toByteArray();

            encodedItem = new String(Base64.getEncoder().encode(serializedObject));

            //debug
            //System.out.println("Encoded String: "+"'"+encodedItem+"'");
            //debug
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return encodedItem;
    }

    public static ItemStack deserializeItemStack(String stringItem){
        ItemStack newItem;
        try{
            byte[] serializedObject = Base64.getDecoder().decode(stringItem);

            ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);

            newItem = (ItemStack) bois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return newItem;
    }

}
