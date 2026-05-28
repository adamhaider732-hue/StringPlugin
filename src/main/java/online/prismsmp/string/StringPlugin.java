package online.prismsmp.string;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class StringPlugin extends JavaPlugin {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 30_000L;

    @Override
    public void onEnable() {
        getLogger().info("StringPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("StringPlugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("prismsmp.string")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(uuid)) {
            long remaining = COOLDOWN_MS - (now - cooldowns.get(uuid));
            if (remaining > 0) {
                long seconds = (remaining / 1000) + 1;
                player.sendMessage("§cYou must wait §e" + seconds + " seconds §cbefore using this again.");
                return true;
            }
        }

        cooldowns.put(uuid, now);

        int filled = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot == null || slot.getType() == Material.AIR) {
                player.getInventory().setItem(i, new ItemStack(Material.STRING, 64));
                filled++;
            }
        }

        if (filled == 0) {
            player.sendMessage("§cYour inventory is full!");
            cooldowns.remove(uuid);
        } else {
            player.sendMessage("§aYour inventory has been filled with §fString§a!");
        }

        return true;
    }
}
