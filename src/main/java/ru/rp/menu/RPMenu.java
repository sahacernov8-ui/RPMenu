package ru.rp.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RPMenu extends JavaPlugin implements Listener {
    private static final String TITLE = "§8Главное меню";

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("menu")) return false;
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эта команда доступна только игроку.");
            return true;
        }
        openMenu(player);
        return true;
    }

    private void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        inv.setItem(10, item(Material.PLAYER_HEAD, "§bПрофиль", "§7Ваш профиль"));
        inv.setItem(12, item(Material.IRON_BLOCK, "§6Фракции", "§7Управление фракцией"));
        inv.setItem(14, item(Material.COMPASS, "§aНавигатор", "§7Навигация"));
        inv.setItem(16, item(Material.CHEST, "§cМагазин", "§7Магазин сервера"));
        inv.setItem(20, item(Material.BOOK, "§dПравила", "§7Правила сервера"));
        inv.setItem(24, item(Material.CRAFTING_TABLE, "§7Настройки", "§7Настройки игрока"));

        player.openInventory(inv);
    }

    private ItemStack item(Material material, String name, String lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(TITLE)) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        switch (event.getRawSlot()) {
            case 10 -> player.sendMessage(ChatColor.AQUA + "Профиль пока находится в разработке.");
            case 12 -> player.sendMessage(ChatColor.GOLD + "Фракции пока находятся в разработке.");
            case 14 -> player.sendMessage(ChatColor.GREEN + "Навигатор пока находится в разработке.");
            case 16 -> player.sendMessage(ChatColor.RED + "Магазин пока находится в разработке.");
            case 20 -> player.sendMessage(ChatColor.LIGHT_PURPLE + "Правила пока находятся в разработке.");
            case 24 -> player.sendMessage(ChatColor.GRAY + "Настройки пока находятся в разработке.");
        }
    }
}
