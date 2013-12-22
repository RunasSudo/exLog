package io.github.runassudo.exlog.defaults;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogLoggingSource;
import io.github.runassudo.exlog.util.ExLogDataHelper;
import io.github.runassudo.exlog.util.ExLogWriteCallback;

public class ExLogBlockLoggingSource extends ExLogLoggingSource {
	// TODO: Use block names for 1.7
	// TODO: Use player UUIDs for 1.7

	@Override
	public String formatEntry(ExLogEntry entry) {
		String blockId = entry.otherData.get("blockId");

		if (entry.otherData.get("type").equals("0"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " break "
					+ blockId + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		if (entry.otherData.get("type").equals("1"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " place "
					+ blockId + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		if (entry.otherData.get("type").equals("2"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " scoop "
					+ blockId + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		if (entry.otherData.get("type").equals("3"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " empty "
					+ blockId + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";

		return ExLogLoggingSource.defaultFormatEntry(entry);
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

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
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

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlockClicked());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "2");
		entry.otherData
				.put("blockId", event.getBlockClicked().getTypeId() + "");

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlockClicked());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "3");
		entry.otherData
				.put("blockId", event.getBlockClicked().getTypeId() + "");

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}
}
