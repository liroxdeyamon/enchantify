package org.lirox.enchantify;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public final class Enchantify extends JavaPlugin implements Listener {
    public final Logger log = getLogger();

    Random random = new Random();

    public static ItemStack purpleGlass = ItemStack.of(Material.PURPLE_STAINED_GLASS_PANE);
    public static ItemStack grayGlass = ItemStack.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static ItemStack enchanting_table_unique = ItemStack.of(Material.ENCHANTING_TABLE);
    public static ItemStack brewing_stand_unique = ItemStack.of(Material.BREWING_STAND);
    public static ItemStack campfire_unique = ItemStack.of(Material.CAMPFIRE);
    public static Inventory enchanting_inv = Bukkit.createInventory(null, 54, "Стол зачарований");

    public static final String PLUGIN_ID = "enchantify";
    public static final String ENCHANTMENTS_ID = "over_enchanted";

    @Override
    public void onEnable() {
        enchanting_table_unique.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        brewing_stand_unique.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        campfire_unique.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        grayGlass.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        grayGlass.getItemMeta().itemName(Component.empty());
        purpleGlass.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        purpleGlass.getItemMeta().itemName(Component.empty());

        for (int i = 0; i < 54; i++) {
            enchanting_inv.setItem(i, grayGlass);
        }
        enchanting_inv.setItem(49, enchanting_table_unique);
        enchanting_inv.setItem(37, ItemStack.empty());

        getServer().getPluginManager().registerEvents(this, this);

//
//        Enchantment.
//
//        Enchantment.BREACH;
//        Enchantment.CHANNELING;
//        Enchantment.DENSITY;
//        Enchantment.EFFICIENCY;
//        Enchantment.FLAME;
//        Enchantment.FORTUNE;
//        Enchantment.IMPALING;
//        Enchantment.INFINITY;
//        Enchantment.KNOCKBACK;
//        Enchantment.LOOTING;
//        Enchantment.LOYALTY;
//        Enchantment.LUCK_OF_THE_SEA;
//        Enchantment.LURE;
//        Enchantment.MENDING;
//        Enchantment.MULTISHOT;
//        Enchantment.PIERCING;
//        Enchantment.POWER;
//        Enchantment.PUNCH;
//        Enchantment.QUICK_CHARGE;
//        Enchantment.RIPTIDE;
//        Enchantment.SILK_TOUCH;
//        Enchantment.VANISHING_CURSE;
//        Enchantment.WIND_BURST;

        EnchantmentOverride.setLogger(log);
        log.info("Patching enchantments...");


        EnchantmentOverride.setName("unbreaking", "Прочность");
        EnchantmentOverride.setDescription("unbreaking", "Понижает расход прочностия");
        EnchantmentOverride.setMaxLevel("unbreaking", 5);
        EnchantmentOverride.setIncompatible("unbreaking", Set.of("mending"));
        EnchantmentOverride.setApplies("unbreaking", EnchantmentOverride.EVERYTHING);

        EnchantmentOverride.setName("binding_curse", "Проклятье несьемности");
        EnchantmentOverride.setDescription("binding_curse", "Предмет нельзя снять");
        EnchantmentOverride.setMaxLevel("binding_curse", 1);


        // Protection
        EnchantmentOverride.setName("protection", "Защита");
        EnchantmentOverride.setDescription("protection", "Понижает входящий урон любого типа (кроме магического)");
        EnchantmentOverride.setMaxLevel("protection", 3);
        EnchantmentOverride.setIncompatible("protection", Set.of("blast_protection", "projectile_protection", "blade_protection", "thermal_protection", "magic_protection"));
        EnchantmentOverride.setApplies("protection", EnchantmentOverride.ARMOR);

        EnchantmentOverride.setName("blast_protection", "Взрывоустойчивость");
        EnchantmentOverride.setDescription("blast_protection", "Понижает входящий взрывной урон");
        EnchantmentOverride.setMaxLevel("blast_protection", 3);
        EnchantmentOverride.setIncompatible("blast_protection", Set.of("protection"));
        EnchantmentOverride.setApplies("blast_protection", EnchantmentOverride.ARMOR);

        EnchantmentOverride.setName("projectile_protection", "Защита от снарядов");
        EnchantmentOverride.setDescription("projectile_protection", "Понижает входящий урон от снарядов");
        EnchantmentOverride.setMaxLevel("projectile_protection", 3);
        EnchantmentOverride.setIncompatible("projectile_protection", Set.of("protection"));
        EnchantmentOverride.setApplies("projectile_protection", EnchantmentOverride.ARMOR);

        EnchantmentOverride.setName("blade_protection", "Защита от клинков");
        EnchantmentOverride.setDescription("blade_protection", "Понижает входящий урон от ближнего оружия");
        EnchantmentOverride.setMaxLevel("blade_protection", 3);
        EnchantmentOverride.setIncompatible("blade_protection", Set.of("protection"));
        EnchantmentOverride.setApplies("blade_protection", EnchantmentOverride.ARMOR);

        EnchantmentOverride.setName("kinetic_protection", "Кинетическая защита");
        EnchantmentOverride.setDescription("kinetic_protection", "Понижает входящий урон от столкновений и падения");
        EnchantmentOverride.setMaxLevel("kinetic_protection", 3);
        EnchantmentOverride.setIncompatible("kinetic_protection", Set.of("protection"));
        EnchantmentOverride.setApplies("kinetic_protection", EnchantmentOverride.BOOTS_N_ELYTRA);

        EnchantmentOverride.setName("thermal_protection", "Тепловая защита");
        EnchantmentOverride.setDescription("thermal_protection", "Понижает входящий урон от лавы, огня и холода");
        EnchantmentOverride.setMaxLevel("thermal_protection", 3);
        EnchantmentOverride.setIncompatible("thermal_protection", Set.of("protection"));
        EnchantmentOverride.setApplies("thermal_protection", EnchantmentOverride.ARMOR);

        EnchantmentOverride.setName("magic_protection", "Магическая защита");
        EnchantmentOverride.setDescription("magic_protection", "Понижает входящий урон зелий и магии");
        EnchantmentOverride.setMaxLevel("magic_protection", 3);
        EnchantmentOverride.setIncompatible("magic_protection", Set.of("protection"));
        EnchantmentOverride.setApplies("magic_protection", EnchantmentOverride.ARMOR);


        EnchantmentOverride.setName("depth_strider", "Покоритель глубин");
        EnchantmentOverride.setDescription("depth_strider", "Увеличивает скорость и количество кислорода под водой");
        EnchantmentOverride.setMaxLevel("depth_strider", 3);
        EnchantmentOverride.setIncompatible("depth_strider", Set.of("frost_walker"));
        EnchantmentOverride.setApplies("depth_strider", EnchantmentOverride.BOOTS);

        EnchantmentOverride.setName("frost_walker", "Ледоход");
        EnchantmentOverride.setDescription("frost_walker", "Замораживает воду во время ходьбы");
        EnchantmentOverride.setMaxLevel("frost_walker", 1);
        EnchantmentOverride.setIncompatible("frost_walker", Set.of("depth_strider"));
        EnchantmentOverride.setApplies("frost_walker", EnchantmentOverride.BOOTS);

        EnchantmentOverride.setName("swift_sneak", "Проворство");
        EnchantmentOverride.setDescription("swift_sneak", "Ускоряет ходьбу при подкрадывании");
        EnchantmentOverride.setMaxLevel("swift_sneak", 3);
        EnchantmentOverride.setIncompatible("swift_sneak", Set.of("depth_strider"));
        EnchantmentOverride.setApplies("swift_sneak", EnchantmentOverride.BOOTS);

        EnchantmentOverride.setName("thorns", "Шипы");
        EnchantmentOverride.setDescription("thorns", "При получении урона возвращает часть обидчику, тратит прочность");
        EnchantmentOverride.setMaxLevel("thorns", 2);
        EnchantmentOverride.setIncompatible("thorns", Set.of("depth_strider"));
        EnchantmentOverride.setApplies("thorns", EnchantmentOverride.CHESTPLATES);



        EnchantmentOverride.setName("sharpness", "Острота");
        EnchantmentOverride.setDescription("sharpness", "Увеличивает исходящий урон");
        EnchantmentOverride.setMaxLevel("sharpness", 3);
        EnchantmentOverride.setIncompatible("sharpness", Set.of("cursed_light", "smite", "bane_of_the_end"));
        EnchantmentOverride.setApplies("sharpness", EnchantmentOverride.SWORDS);

        EnchantmentOverride.setName("cursed_light", "Проклятый свет");
        EnchantmentOverride.setDescription("cursed_light", "Увеличивает исходящий урон в верхнем мире");
        EnchantmentOverride.setMaxLevel("cursed_light", 3);
        EnchantmentOverride.setIncompatible("cursed_light", Set.of("sharpness"));
        EnchantmentOverride.setApplies("cursed_light", EnchantmentOverride.SWORDS);

        EnchantmentOverride.setName("smite", "Небесная кара");
        EnchantmentOverride.setDescription("smite", "Увеличивает исходящий урон в нижнем мире");
        EnchantmentOverride.setMaxLevel("smite", 3);
        EnchantmentOverride.setIncompatible("smite", Set.of("sharpness"));
        EnchantmentOverride.setApplies("smite", EnchantmentOverride.SWORDS);

        EnchantmentOverride.setName("bane_of_the_end", "");
        EnchantmentOverride.setDescription("bane_of_the_end", "Увеличивает исходящий урон в крае");
        EnchantmentOverride.setMaxLevel("bane_of_the_end", 3);
        EnchantmentOverride.setIncompatible("bane_of_the_end", Set.of("sharpness"));
        EnchantmentOverride.setApplies("bane_of_the_end", EnchantmentOverride.SWORDS);

        EnchantmentOverride.setName("sweeping_edge", "Разящий клинок");
        EnchantmentOverride.setDescription("sweeping_edge", "Увеличивает исходящий урон по области");
        EnchantmentOverride.setMaxLevel("sweeping_edge", 3);
        EnchantmentOverride.setApplies("sweeping_edge", EnchantmentOverride.SWORDS);


        EnchantmentOverride.setName("fire_aspect", "Заговор огня");
        EnchantmentOverride.setDescription("fire_aspect", "Зажигает врага при ударе");
        EnchantmentOverride.setMaxLevel("fire_aspect", 2);
        EnchantmentOverride.setIncompatible("fire_aspect", Set.of("frostbite"));
        EnchantmentOverride.setApplies("fire_aspect", EnchantmentOverride.SWORDS);

        EnchantmentOverride.setName("frostbite", "Обморожение");
        EnchantmentOverride.setDescription("frostbite", "Замораживает врага при ударе");
        EnchantmentOverride.setMaxLevel("frostbite", 2);
        EnchantmentOverride.setIncompatible("frostbite", Set.of("fire_aspect"));
        EnchantmentOverride.setApplies("frostbite", EnchantmentOverride.SWORDS);

        EnchantmentOverride.setName("luminescence", "Люминисценция");
        EnchantmentOverride.setDescription("luminescence", "Подсвечивает врага при ударе");
        EnchantmentOverride.setMaxLevel("luminescence", 1);
        EnchantmentOverride.setApplies("luminescence", EnchantmentOverride.SWORDS);





        EnchantmentOverride.setMaxLevel("fortune", 1);
        log.info("All enchantments are patched.");






    }

    @Override
    public @NotNull Path getDataPath() {
        return super.getDataPath();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block == null) return;

        switch (block.getType()) {
            case ENCHANTING_TABLE -> {
                event.setCancelled(true);
                player.openInventory(enchanting_inv);
            }
            case CAMPFIRE -> {
                event.setCancelled(true);
                Inventory cooking_inv = Bukkit.createInventory(null, 27);
                player.openInventory(cooking_inv);
                // кастом готовка еды
            }
            case BREWING_STAND -> {
                event.setCancelled(true);
                Inventory brewing_inv = Bukkit.createInventory(null, 27);
                player.openInventory(brewing_inv);
                // кастом варка зелий
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        ItemStack click_item = event.getCurrentItem();
        ItemStack cursor_item = event.getCursor();
        ItemStack unique_item = event.getInventory().getItem(49);
        if (unique_item == null) return;
        if (event.getClickedInventory().getHolder() == player.getInventory().getHolder()) return;

        if (unique_item.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) return;
        switch (unique_item.getType()) {
            case ENCHANTING_TABLE -> {
                if (event.getSlot() != 37) event.setCancelled(true);
                EnchantmentOverride.updateBooks(event.getInventory().getItem(37), event.getInventory());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.updateInventory();
                        log.info("updated");
                    }
                }.runTaskLater(this, 0);
            }
            case BREWING_STAND -> {

            }
            case CAMPFIRE -> {

            }
        }

//        if (meta == null) return;
//
//        PersistentDataContainer container = meta.getPersistentDataContainer();
//        PersistentDataContainer enchantContainer = container.getOrDefault(enchantmentsKey, PersistentDataType.TAG_CONTAINER, container.getAdapterContext().newPersistentDataContainer());
//        String enc = EnchantmentOverride.getRandom();
//        enchantContainer.set(new NamespacedKey(PLUGIN_ID, enc), PersistentDataType.INTEGER, random.nextInt(EnchantmentOverride.getMaxLevel(enc)+2));
//        container.set(enchantmentsKey, PersistentDataType.TAG_CONTAINER, enchantContainer);
//        meta.lore(generateDescription(item).stream().toList().reversed());
//        item.setItemMeta(meta);
    }
}
