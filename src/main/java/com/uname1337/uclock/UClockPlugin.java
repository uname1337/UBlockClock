package com.uname1337.uclock;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UClockPlugin extends org.bukkit.plugin.java.JavaPlugin {

    private static UClockPlugin instance;
    private final Map<UUID, Selection> selections = new HashMap<>();
    private final Map<String, ClockRegion> clocks = new HashMap<>();
    private final ClockRenderer renderer = new ClockRenderer();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadClocks();

        getCommand("uclock").setExecutor(new UClockCommand(this));
        Bukkit.getPluginManager().registerEvents(new UClockListener(this), this);

        Bukkit.getScheduler().runTaskTimer(this, renderer::renderAll, 20L, 20L);
        getLogger().info("UBlockClock enabled");
    }

    @Override
    public void onDisable() {
        saveClocks();
        getLogger().info("UBlockClock disabled");
    }

    public static UClockPlugin getInstance() {
        return instance;
    }

    public Map<String, ClockRegion> getClocks() {
        return clocks;
    }

    public void setSelection(UUID uuid, Selection selection) {
        selections.put(uuid, selection);
    }

    public Selection getSelection(UUID uuid) {
        return selections.get(uuid);
    }

    public boolean isSelecting(UUID uuid) {
        return selections.containsKey(uuid);
    }

    public String createClockFromSelection(Player player, String id) {
        Selection selection = selections.get(player.getUniqueId());
        if (selection == null || !selection.isComplete()) {
            return "§cСначала выберите область: /uclock select и нажмите ПКМ по двум точкам.";
        }
        if (id == null || id.isBlank()) {
            return "§cУкажите ID часов.";
        }

        var first = selection.getFirst();
        var second = selection.getSecond();

        int minX = Math.min(first.getBlockX(), second.getBlockX());
        int maxX = Math.max(first.getBlockX(), second.getBlockX());
        int minY = Math.min(first.getBlockY(), second.getBlockY());
        int maxY = Math.max(first.getBlockY(), second.getBlockY());
        int minZ = Math.min(first.getBlockZ(), second.getBlockZ());
        int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());

        ClockRegion region = new ClockRegion(id, player.getWorld().getName(), minX, minY, minZ, maxX, maxY, maxZ);
        clocks.put(id, region);
        saveClocks();
        selection.clear();
        return "§aЧасы \"" + id + "\" сохранены.";
    }

    public void removeClock(String id) {
        clocks.remove(id);
        saveClocks();
    }

    private void saveClocks() {
        getConfig().set("clocks", null);
        for (Map.Entry<String, ClockRegion> entry : clocks.entrySet()) {
            ClockRegion region = entry.getValue();
            getConfig().set("clocks." + entry.getKey() + ".world", region.worldName());
            getConfig().set("clocks." + entry.getKey() + ".minX", region.minX());
            getConfig().set("clocks." + entry.getKey() + ".minY", region.minY());
            getConfig().set("clocks." + entry.getKey() + ".minZ", region.minZ());
            getConfig().set("clocks." + entry.getKey() + ".maxX", region.maxX());
            getConfig().set("clocks." + entry.getKey() + ".maxY", region.maxY());
            getConfig().set("clocks." + entry.getKey() + ".maxZ", region.maxZ());
        }
        saveConfig();
    }

    private void loadClocks() {
        ConfigurationSection section = getConfig().getConfigurationSection("clocks");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            String worldName = section.getString(key + ".world");
            if (worldName == null) {
                continue;
            }
            clocks.put(key, new ClockRegion(
                    key,
                    worldName,
                    section.getInt(key + ".minX"),
                    section.getInt(key + ".minY"),
                    section.getInt(key + ".minZ"),
                    section.getInt(key + ".maxX"),
                    section.getInt(key + ".maxY"),
                    section.getInt(key + ".maxZ")
            ));
        }
    }
}
