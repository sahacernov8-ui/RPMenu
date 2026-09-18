package ru.rp.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class RPMenu extends JavaPlugin implements Listener {

    private static final String MAIN_TITLE = "§8Главное меню";
    private static final String PROFILE_TITLE = "§8Профиль игрока";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("RPMenu enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("RPMenu disabled.");
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!command.getName().equalsIgnoreCase("menu")) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эта команда доступна только игроку.");
            return true;
        }

        openMainMenu(player);
        return true;
    }

    // =========================
    // ГЛАВНОЕ МЕНЮ
    // =========================

    private void openMainMenu(Player player) {

        Inventory inv = Bukkit.createInventory(
                null,
                27,
                MAIN_TITLE
        );

        inv.setItem(
                10,
                menuItem(
                        Material.PLAYER_HEAD,
                        "§bПрофиль",
                        "§7Ваш профиль",
                        "profile"
                )
        );

        inv.setItem(
                12,
                menuItem(
                        Material.IRON_BLOCK,
                        "§6Фракции",
                        "§7Управление фракцией",
                        "factions"
                )
        );

        inv.setItem(
                14,
                menuItem(
                        Material.COMPASS,
                        "§aНавигатор",
                        "§7Навигация",
                        "navigator"
                )
        );

        inv.setItem(
                16,
                menuItem(
                        Material.CHEST,
                        "§cМагазин",
                        "§7Магазин сервера",
                        "shop"
                )
        );

        inv.setItem(
                20,
                menuItem(
                        Material.BOOK,
                        "§dПравила",
                        "§7Правила сервера",
                        "rules"
                )
        );

        inv.setItem(
                24,
                menuItem(
                        Material.CRAFTING_TABLE,
                        "§7Настройки",
                        "§7Настройки игрока",
                        "settings"
                )
        );

        player.openInventory(inv);
    }

    // =========================
    // ПРОФИЛЬ
    // =========================

    private void openProfile(Player player) {

        Inventory inv = Bukkit.createInventory(
                null,
                27,
                PROFILE_TITLE
        );

        // Голова игрока
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();

        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName("§b" + player.getName());

        head.setItemMeta(skullMeta);

        inv.setItem(4, head);

        // Основная информация
        inv.setItem(
                10,
                normalItem(
                        Material.NAME_TAG,
                        "§eНикнейм",
                        "§f" + player.getName()
                )
        );

        inv.setItem(
                11,
                normalItem(
                        Material.PLAYER_HEAD,
                        "§bUUID",
                        "§7" + player.getUniqueId()
                )
        );

        // Дата первого входа
        Date firstPlayed = new Date(player.getFirstPlayed());

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd.MM.yyyy HH:mm");

        inv.setItem(
                13,
                normalItem(
                        Material.CLOCK,
                        "§6Первый вход",
                        "§f" + dateFormat.format(firstPlayed)
                )
        );


        // Уровень
        inv.setItem(
                15,
                normalItem(
                        Material.EXPERIENCE_BOTTLE,
                        "§aУровень",
                        "§f" + player.getLevel()
                )
        );

        // Баланс
        inv.setItem(
                16,
                normalItem(
                        Material.GOLD_INGOT,
                        "§6Баланс",
                        "§f0$"
                )
        );

        // Фракция
        inv.setItem(
                20,
                normalItem(
                        Material.IRON_SWORD,
                        "§cФракция",
                        "§7Не состоит во фракции"
                )
        );

        // Статус
        inv.setItem(
                22,
                normalItem(
                        Material.LIME_DYE,
                        "§aСтатус",
                        "§fОнлайн"
                )
        );

        // Назад
        inv.setItem(
                24,
                normalItem(
                        Material.ARROW,
                        "§c← Назад",
                        "§7Вернуться в главное меню"
                )
        );

        player.openInventory(inv);
    }

    // =========================
    // ПРЕДМЕТЫ
    // =========================

    private ItemStack menuItem(
            Material material,
            String name,
            String lore,
            String model
    ) {

        ItemStack stack = new ItemStack(material);

        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(List.of(lore));

        NamespacedKey key = new NamespacedKey(
                "rpmenu",
                model
        );

        meta.setItemModel(key);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack normalItem(
            Material material,
            String name,
            String lore
    ) {

        ItemStack stack = new ItemStack(material);

        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(List.of(lore));

        stack.setItemMeta(meta);

        return stack;
    }

    // =========================
    // ОБРАБОТКА КЛИКОВ
    // =========================

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        String title = event.getView().getTitle();

        if (!title.equals(MAIN_TITLE)
                && !title.equals(PROFILE_TITLE)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int slot = event.getRawSlot();

        // Главное меню
        if (title.equals(MAIN_TITLE)) {

            switch (slot) {

                case 10 ->
                        openProfile(player);

                case 12 ->
                        player.sendMessage(
                                ChatColor.GOLD +
                                "Фракции пока находятся в разработке."
                        );

                case 14 ->
                        player.sendMessage(
                                ChatColor.GREEN +
                                "Навигатор пока находится в разработке."
                        );

                case 16 ->
                        player.sendMessage(
                                ChatColor.RED +
                                "Магазин пока находится в разработке."
                        );

                case 20 ->
                        player.sendMessage(
                                ChatColor.LIGHT_PURPLE +
                                "Правила пока находятся в разработке."
                        );

                case 24 ->
                        player.sendMessage(
                                ChatColor.GRAY +
                                "Настройки пока находятся в разработке."
                        );
            }

            return;
        }

        // Профиль
        if (title.equals(PROFILE_TITLE)) {

            if (slot == 24) {
                openMainMenu(player);
            }
        }
    }
}
