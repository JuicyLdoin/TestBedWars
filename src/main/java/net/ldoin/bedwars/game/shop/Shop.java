package net.ldoin.bedwars.game.shop;

import net.ldoin.bedwars.util.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Shop {

    private final Inventory inventory;
    private final Map<Integer, ItemStack> itemMap;

    public Shop(ConfigurationSection configurationSection) {
        inventory = Bukkit.createInventory(null, configurationSection.getInt("size", 9), "Магазин");
        itemMap = new HashMap<>();

        ConfigurationSection itemsSection = configurationSection.getConfigurationSection("items");
        if (itemsSection == null)
            return;

        for (String item : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(item);
            int slot = itemSection.getInt("slot", inventory.firstEmpty());
            Material material = Material.matchMaterial(itemSection.getString("price_material"));
            int price = itemSection.getInt("price", 1);

            ItemStack itemStack = ItemUtil.buildItemFromConfigSection(itemSection);
            itemMap.put(slot, itemStack.clone());

            net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound nbtTagCompound = nmsCopy.getTag();
            if (nbtTagCompound == null) {
                nbtTagCompound = new NBTTagCompound();
            }
            nbtTagCompound.setInt("price", price);
            nbtTagCompound.setString("price_material", material.name());

            inventory.setItem(slot, itemStack);
        }
    }

    public void spawn(Location location) {
        Villager shop = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        shop.setCustomName("Магазин");
        shop.setCustomNameVisible(true);
        ((CraftEntity) shop).getHandle().ai = false;
    }

    public ItemStack getItem(int slot) {
        return itemMap.get(slot);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public boolean isShop(Inventory inventory) {
        return inventory.equals(this.inventory);
    }
}