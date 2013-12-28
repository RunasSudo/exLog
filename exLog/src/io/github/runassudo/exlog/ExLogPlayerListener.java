package io.github.runassudo.exlog;

import io.github.runassudo.exlog.query.LocationDataQuery;
import io.github.runassudo.exlog.util.ExLogDataHelper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ExLogPlayerListener implements Listener {
	private Material STICK = Material.valueOf(ExLogPlugin.getInstance()
			.getConfig().getString("stickTool"));
	private Material BONE = Material.valueOf(ExLogPlugin.getInstance()
			.getConfig().getString("boneTool"));

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {
			if (event.getItem().getType() == STICK) {
				if (event.getPlayer().hasPermission("exlog.query.stick")) {
					Block block = event.getClickedBlock();

					LocationDataQuery query = new LocationDataQuery(
							block.getX(), block.getY(), block.getZ(), block
									.getWorld().getName());

					ExLogDataHelper.performQuery(query, event.getPlayer());
				} else {
					event.getPlayer().sendMessage(
							ChatColor.RED + "No permission.");
				}
			}
			if (event.getItem().getType() == BONE) {
				if (event.getPlayer().hasPermission("exlog.query.bone")) {
					Block block = event.getClickedBlock().getRelative(
							event.getBlockFace());

					LocationDataQuery query = new LocationDataQuery(
							block.getX(), block.getY(), block.getZ(), block
									.getWorld().getName());

					ExLogDataHelper.performQuery(query, event.getPlayer());
				} else {
					event.getPlayer().sendMessage(
							ChatColor.RED + "No permission.");
				}
			}
		}
	}
}
