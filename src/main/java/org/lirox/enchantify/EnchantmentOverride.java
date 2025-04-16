package org.lirox.enchantify;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import static org.lirox.enchantify.Enchantify.*;

public class EnchantmentOverride {
    public static NamespacedKey enchantmentsKey = new NamespacedKey(PLUGIN_ID, ENCHANTMENTS_ID);

    static Random random = new Random();

    public static TextColor DEFAULT_COLOR = TextColor.color(150, 150, 150);
    public static TextColor CURSE_COLOR = TextColor.color(150, 0, 50);
    public static TextColor COMBAT_COLOR = TextColor.color(255, 10, 10);
    public static TextColor MOBILITY_COLOR = TextColor.color(150, 150, 255);
    public static TextColor DEFENCE_COLOR = TextColor.color(50, 255, 50);
    public static TextColor MINING_COLOR = TextColor.color(255, 150, 50);

    public static String VANILLA_BEHAVIOUR = "vanilla";
    public static String CUSTOM_BEHAVIOUR = "custom";

    public static Set<Material> SWORDS = Set.of(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD);
    public static Set<Material> BOWS = Set.of(Material.BOW, Material.CROSSBOW);
    public static Set<Material> AXES = Set.of(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
    public static Set<Material> SHOVELS = Set.of(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL);
    public static Set<Material> PICKAXES = Set.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);
    public static Set<Material> HOES = Set.of(Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE);
    public static Set<Material> INSTRUMENTS = new HashSet<>() {{
        addAll(EnchantmentOverride.AXES);
        addAll(EnchantmentOverride.SHOVELS);
        addAll(EnchantmentOverride.PICKAXES);
        addAll(EnchantmentOverride.HOES);
        addAll(Set.of(Material.SHEARS, Material.FLINT_AND_STEEL, Material.CARROT_ON_A_STICK, Material.WARPED_FUNGUS_ON_A_STICK, Material.BRUSH));
    }};
    public static Set<Material> HELMETS = Set.of(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, Material.CHAINMAIL_HELMET, Material.TURTLE_HELMET);
    public static Set<Material> CHESTPLATES = Set.of(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE);
    public static Set<Material> LEGGINGS = Set.of(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.CHAINMAIL_LEGGINGS);
    public static Set<Material> BOOTS = Set.of(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS, Material.CHAINMAIL_BOOTS);
    public static Set<Material> ARMOR = new HashSet<>() {{
        addAll(EnchantmentOverride.HELMETS);
        addAll(EnchantmentOverride.CHESTPLATES);
        addAll(EnchantmentOverride.LEGGINGS);
        addAll(EnchantmentOverride.BOOTS);
    }};
    public static Set<Material> BOOTS_N_ELYTRA = new HashSet<>() {{
        addAll(EnchantmentOverride.BOOTS);
        add(Material.ELYTRA);
    }};
    public static Set<Material> EVERYTHING = Set.of(Material.AIR);

    private static final Map<String, Integer> MAX_LEVELS = new HashMap<>();
    private static final Map<String, String> NAMES = new HashMap<>();
    private static final Map<String, String> DESCRIPTIONS = new HashMap<>();
    private static final Map<String, TextColor> COLORS = new HashMap<>();
    private static final Map<String, Set<Material>> APPLIES = new HashMap<>();
    private static final Map<String, Set<String>> INCOMPATIBILITY = new HashMap<>();

    private static Logger LOG = null;

    public static void setName(String enchantment, String name) {
        if (LOG != null) LOG.info("- changed name of '" + enchantment + "' to " + name);
        NAMES.put(enchantment, name);
    }

    public static String getName(String enchantment) {
        return NAMES.getOrDefault(enchantment, enchantment);
    }

    public static void setDescription(String enchantment, String desc) {
        if (LOG != null) LOG.info("- changed description of '" + enchantment + "' to " + desc);
        DESCRIPTIONS.put(enchantment, desc);
    }

    public static String getDescription(String enchantment) {
        return DESCRIPTIONS.getOrDefault(enchantment, enchantment);
    }

    public static void setMaxLevel(String enchantment, int maxLevel) {
        if (LOG != null) LOG.info("- changed max level of '" + enchantment + "' to " + maxLevel);
        MAX_LEVELS.put(enchantment, maxLevel);
    }

    public static int getMaxLevel(String enchantment) {
        return MAX_LEVELS.getOrDefault(enchantment, 0);
    }

    public static boolean isLevelAllowed(String enchantment, int level) {
        return level <= getMaxLevel(enchantment);
    }

    public static void setColor(String enchantment, TextColor color) {
        if (LOG != null) LOG.info("- changed color of '" + enchantment + "' to " + color);
        COLORS.put(enchantment, color);
    }

    public static TextColor getColor(String enchantment) {
        return COLORS.getOrDefault(enchantment, DEFAULT_COLOR);
    }

    public static void setApplies(String enchantment, Set<Material> appliesTo) {
        if (LOG != null) LOG.info("- changed appliesTo of '" + enchantment + "' to " + appliesTo);
        APPLIES.put(enchantment, appliesTo);
    }

    public static Set<Material> getApplies(String enchantment) {
        return APPLIES.getOrDefault(enchantment, EVERYTHING);
    }

    public static void setIncompatible(String enchantment, Set<String> incompatibleWith) {
        if (LOG != null) LOG.info("- changed incompatibility of '" + enchantment + "' to " + incompatibleWith);
        INCOMPATIBILITY.put(enchantment, incompatibleWith);
    }

    public static boolean areCompatible(String first, String second) {
        return !INCOMPATIBILITY.getOrDefault(first, Collections.emptySet()).contains(second) &&
                !INCOMPATIBILITY.getOrDefault(second, Collections.emptySet()).contains(first);
    }

    public static boolean canApplyOn(String enchantment, Material item) {
        return getApplies(enchantment).contains(item);
    }



    public static List<Component> generateDescription(ItemStack item) {
        List<Component> desc = new ArrayList<>();
        if (!item.hasItemMeta()) return desc;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer enchantContainer = container.get(enchantmentsKey, PersistentDataType.TAG_CONTAINER);
        if (enchantContainer == null) return desc;
        for (NamespacedKey enchantKey : enchantContainer.getKeys()) {
            String enchantName = enchantKey.getKey();
            int level = enchantContainer.getOrDefault(enchantKey, PersistentDataType.INTEGER, 1);
            Component row = Component.text(NAMES.getOrDefault(enchantName, enchantName), DEFAULT_COLOR);
            if (MAX_LEVELS.containsKey(enchantName) && level > 1) {
                int max = MAX_LEVELS.get(enchantName);
                row = row.append(Component.text(" " + level, DEFAULT_COLOR));
                if (level == max) row = row.append(Component.text(" ★", NamedTextColor.GOLD));
                else if (level > max) row = row.append(Component.text(" ◆", NamedTextColor.DARK_RED));
            }
            desc.add(row);
            desc.add(Component.text("- " + getDescription(enchantName)).color(NamedTextColor.DARK_GRAY));
        }
        return desc;
    }




    public static void setLogger(Logger log) {
        LOG = log;
    }

    public static String[] getAll() {
        return MAX_LEVELS.keySet().toArray(new String[0]);
    }

    public static String getRandom() {
        return getAll()[random.nextInt(MAX_LEVELS.size())];
    }

    public static Map<String, Integer> getEnchantments(ItemStack item) {
        Map<String, Integer> result = new HashMap<>();

        if (!item.hasItemMeta()) return result;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(enchantmentsKey, PersistentDataType.TAG_CONTAINER)) return result;
        PersistentDataContainer enchantContainer = container.get(enchantmentsKey, PersistentDataType.TAG_CONTAINER);

        for (NamespacedKey enchantKey : enchantContainer.getKeys()) {
            Integer level = enchantContainer.get(enchantKey, PersistentDataType.INTEGER);
            if (level != null) result.put(enchantKey.getKey(), level);
        }

        return result;
    }

    public static ItemStack generateBook(String enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer enchantContainer = container.getAdapterContext().newPersistentDataContainer();
        enchantContainer.set(new NamespacedKey(PLUGIN_ID, enchantment), PersistentDataType.INTEGER, level);

        container.set(enchantmentsKey, PersistentDataType.TAG_CONTAINER, enchantContainer);
        book.setItemMeta(meta);
        book.lore(generateDescription(book));
        return book;
    }

    public static void updateBooks(ItemStack item, Inventory inventory) {
        List<ItemStack> books = new ArrayList<>();
        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25
        };

        if (item == null || item.getType().isAir()) {
            for (int slot : slots) {
                inventory.setItem(slot, grayGlass);
            }
            return;
        }

        for (String enchantment : getAll()) {
            if (canApplyOn(enchantment, item.getType())) {
                books.add(generateBook(enchantment, 1));
            }
        }

        for (int i = 0; i < slots.length; i++) {
            if (i < books.size()) {
                inventory.setItem(slots[i], books.get(i));
            } else {
                inventory.setItem(slots[i], grayGlass);
            }
        }
    }


}

