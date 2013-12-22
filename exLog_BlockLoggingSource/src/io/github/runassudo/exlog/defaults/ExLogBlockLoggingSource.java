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
	// TODO: Use player UUIDs for 1.7

	@Override
	public String formatEntry(ExLogEntry entry) {
		String blockType = entry.otherData.get("blockType");

		if (entry.otherData.get("type").equals("0"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " break "
					+ blockType + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		if (entry.otherData.get("type").equals("1"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " place "
					+ blockType + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		if (entry.otherData.get("type").equals("2"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " scoop "
					+ blockType + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		if (entry.otherData.get("type").equals("3"))
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " empty "
					+ blockType + " @ " + entry.dimension + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";

		return ExLogLoggingSource.defaultFormatEntry(entry);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlock());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "0");
		entry.otherData.put("blockType", event.getBlock().getType().name());

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlock());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "1");
		entry.otherData.put("blockType", event.getBlock().getType().name());

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlockClicked());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "2");
		entry.otherData.put("blockType", event.getBlockClicked().getType()
				.name());

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		ExLogEntry entry = new ExLogEntry();
		entry.date = System.currentTimeMillis();
		entry.origin = getName();
		ExLogEntry.populate(entry, event.getBlockClicked());
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "3");
		entry.otherData.put("blockType", event.getBlockClicked().getType()
				.name());

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}
}
