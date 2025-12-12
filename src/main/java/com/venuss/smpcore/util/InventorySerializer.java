package com.venuss.smpcore.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class InventorySerializer {

    /**
     * Serializes a player's entire inventory (Contents, Armor, Offhand) into a compressed byte array.
     */
    public static byte[] toCompressedBytes(PlayerInventory inventory) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipStream = new GZIPOutputStream(outputStream);
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(gzipStream)) {

            // Write the size and the contents
            // getContents() in modern versions includes armor and offhand, but for safety
            // and specific retrieval, we will write them explicitly to ensure order.

            ItemStack[] contents = inventory.getContents();
            dataOutput.writeInt(contents.length);

            for (ItemStack item : contents) {
                dataOutput.writeObject(item);
            }

            // Finish compression
            dataOutput.close();
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize inventory", e);
        }
    }

    /**
     * Deserializes the compressed byte array back into an ItemStack array.
     */
    public static ItemStack[] fromCompressedBytes(byte[] data) {
        if (data == null || data.length == 0) return new ItemStack[0];

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             GZIPInputStream gzipStream = new GZIPInputStream(inputStream);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(gzipStream)) {

            int size = dataInput.readInt();
            ItemStack[] items = new ItemStack[size];

            for (int i = 0; i < size; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to deserialize inventory", e);
        }
    }
}
