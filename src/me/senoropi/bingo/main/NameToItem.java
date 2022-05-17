package me.senoropi.bingo.main;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NameToItem {
    public static ItemStack fromName(String Name) throws NullPointerException, NumberFormatException {
        if (Material.matchMaterial(Name) != null) {
            Material m = Material.matchMaterial(Name);
            ItemStack item = new ItemStack(m, 1);
            return item;
        }
        return null;
    }
}
