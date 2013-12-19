package io.github.runassudo.exlog.defaults;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogLoggingSource;
import io.github.runassudo.exlog.ExLogPlugin;

public class ExLogBlockLoggingSource extends ExLogLoggingSource {
	@Override
	public String formatEntry(ExLogEntry entry) {
		String blockId = entry.otherData.get("blockId");

		if (entry.otherData.get("type").equals("0"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET
					+ " destroyed " + blockId + " @ " + entry.dimension + "("
					+ entry.x + "," + entry.y + "," + entry.z + ")";
		else
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " placed "
					+ blockId + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlock());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "0");
		entry.otherData.put("blockId", event.getBlock().getTypeId() + "");
		// TODO: Use Block names for 1.7

		try {
			ExLogPlugin.getInstance().getDataProvider().appendData(entry);
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Failed to add entry.", e);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlock());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "1");
		entry.otherData.put("blockId", event.getBlock().getTypeId() + "");
		// TODO: Use Block names for 1.7

		try {
			ExLogPlugin.getInstance().getDataProvider().appendData(entry);
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Failed to add entry.", e);
		}
	}
}
