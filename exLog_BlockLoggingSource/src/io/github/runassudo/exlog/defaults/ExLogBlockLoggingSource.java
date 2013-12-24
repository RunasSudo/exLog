package io.github.runassudo.exlog.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

		switch (entry.otherData.get("type")) {
		case "0":
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " break "
					+ blockType + " @ " + entry.world + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		case "1":
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " place "
					+ blockType + " @ " + entry.world + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		case "2":
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " scoop "
					+ blockType + " @ " + entry.world + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		case "3":
			return ChatColor.BLUE + entry.player + ChatColor.RESET + " empty "
					+ blockType + " @ " + entry.world + "(" + entry.x + ","
					+ entry.y + "," + entry.z + ")";
		}

		return ExLogLoggingSource.defaultFormatEntry(entry);
	}

	@Override
	public boolean rollbackEntry(ExLogEntry entry) {
		switch (entry.otherData.get("type")) {
		case "0":
			Bukkit.getWorld(entry.world)
					.getBlockAt(entry.x, entry.y, entry.z)
					.setType(Material.valueOf(entry.otherData.get("blockType")));
			return true;
		case "1":
			Bukkit.getWorld(entry.world).getBlockAt(entry.x, entry.y, entry.z)
					.setType(Material.AIR);
			return true;
		case "2":
			Bukkit.getWorld(entry.world).getBlockAt(entry.x, entry.y, entry.z)
					.setType(Material.WATER);
			return true;
		case "3":
			Bukkit.getWorld(entry.world).getBlockAt(entry.x, entry.y, entry.z)
					.setType(Material.AIR);
			return true;
		}
		return false;
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
		ExLogEntry.populate(entry,
				event.getBlockClicked().getRelative(event.getBlockFace()));
		entry.player = event.getPlayer().getName();
		entry.otherData.put("type", "3");
		entry.otherData.put("blockType", event.getBucket().name());

		ExLogDataHelper.performWrite(entry, new ExLogWriteCallback());
	}
}
