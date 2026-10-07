package com.uname1337.uclock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UClockCommand implements CommandExecutor {

    private final UClockPlugin plugin;

    public UClockCommand(UClockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько игрок может использовать эту команду.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "select" -> {
                plugin.setSelection(player.getUniqueId(), new Selection());
                player.sendMessage("§aРежим выбора включён. Нажмите ПКМ по первой точке, затем по второй.");
                return true;
            }
            case "stopselect" -> {
                if (!plugin.isSelecting(player.getUniqueId())) {
                    player.sendMessage("§cРежим выбора уже выключен.");
                    return true;
                }
                plugin.removeSelection(player.getUniqueId());
                player.sendMessage("§aРежим выбора выключен.");
                return true;
            }
            case "set" -> {
                if (args.length < 3) {
                    player.sendMessage("§cИспользование: /uclock set <id> selection");
                    return true;
                }
                if (!"selection".equalsIgnoreCase(args[2])) {
                    player.sendMessage("§cИспользование: /uclock set <id> selection");
                    return true;
                }
                player.sendMessage(plugin.createClockFromSelection(player, args[1]));
                return true;
            }
            case "remove" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /uclock remove <id>");
                    return true;
                }
                String id = args[1];
                if (!plugin.getClocks().containsKey(id)) {
                    player.sendMessage("§cЧасы с ID \"" + id + "\" не существуют.");
                    return true;
                }
                plugin.removeClock(id);
                player.sendMessage("§aЧасы \"" + id + "\" удалены.");
                return true;
            }
            default -> {
                sendHelp(player);
                return true;
            }
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6UBlockClock");
        player.sendMessage("§7/uclock select §8- включить режим выделения");
        player.sendMessage("§7/uclock stopselect §8- выключить режим выделения");
        player.sendMessage("§7/uclock set <id> selection §8- сохранить часы на выделенной области");
        player.sendMessage("§7/uclock remove <id> §8- удалить часы");
    }
}
