package com.uname1337.uclock;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class UClockListener implements Listener {

    private final UClockPlugin plugin;

    public UClockListener(UClockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!plugin.isSelecting(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        Selection selection = plugin.getSelection(player.getUniqueId());
        if (selection == null) {
            selection = new Selection();
            plugin.setSelection(player.getUniqueId(), selection);
        }

        if (!selection.hasFirst()) {
            selection.setFirst(event.getClickedBlock().getLocation());
            player.sendMessage("§aТочка 1 установлена. Нажмите ещё раз по второй точке.");
            return;
        }
        if (!selection.hasSecond()) {
            selection.setSecond(event.getClickedBlock().getLocation());
            player.sendMessage("§aТочка 2 установлена. Теперь используйте /uclock set <id> selection");
            return;
        }

        selection.clear();
        selection.setFirst(event.getClickedBlock().getLocation());
        player.sendMessage("§eВыбор сброшен. Точка 1 установлена.");
    }
}
