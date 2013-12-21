package io.github.runassudo.exlog;

import io.github.runassudo.exlog.query.LocationDataQuery;
import io.github.runassudo.exlog.util.ExLogDataHelper;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ExLogPlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {
			if (event.getItem().getType() == Material.STICK) {
				Block block = event.getClickedBlock();

				LocationDataQuery query = new LocationDataQuery(block.getX(),
						block.getY(), block.getZ(),
						ExLogEntry.worldToDimension(block.getWorld()));

				ExLogDataHelper.performQuery(query, event.getPlayer());
			}
			if (event.getItem().getType() == Material.BONE) {
				Block block = event.getClickedBlock().getRelative(
						event.getBlockFace());

				LocationDataQuery query = new LocationDataQuery(block.getX(),
						block.getY(), block.getZ(),
						ExLogEntry.worldToDimension(block.getWorld()));

				ExLogDataHelper.performQuery(query, event.getPlayer());
			}
		}
	}
}
