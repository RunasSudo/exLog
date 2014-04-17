package io.github.runassudo.exlog.util;

import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogLoggingSource;
import io.github.runassudo.exlog.ExLogPlugin;
import io.github.runassudo.exlog.query.ExLogDataQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Helper class to perform asynchronous data reads and writes.
 * 
 * @author runassudo
 * 
 */
public class ExLogDataHelper {
	/**
	 * Performs a Data Query and calls the callback function when complete.
	 * 
	 * @param query
	 *            The Query to perform.
	 * @param callback
	 *            The function to call when complete.
	 */
	public static void performQuery(final ExLogDataQuery query,
			final ExLogQueryCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<ExLogEntry> results;

				try {
					results = ExLogPlugin.getInstance().getDataProvider()
							.readData(query);
				} catch (Exception e) {
					ExLogPlugin.getInstance().getLogger()
							.log(Level.SEVERE, "Unable to perform query.", e);
					callback.failed(e);
					return;
				}

				try {
					callback.success(results);
				} catch (Exception e) {
					ExLogPlugin
							.getInstance()
							.getLogger()
							.log(Level.SEVERE, "Unable to perform callback.", e);
				}
			}
		}).start();
	}

	/**
	 * Performs a data write and calls the callback function when complete.
	 * 
	 * @param data
	 *            The data to write.
	 * @param callback
	 *            The function to call when complete.
	 */
	public static void performWrite(final ArrayList<ExLogEntry> data,
			final ExLogWriteCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ExLogPlugin.getInstance().getDataProvider()
							.appendData(data);
				} catch (Exception e) {
					ExLogPlugin.getInstance().getLogger()
							.log(Level.SEVERE, "Unable to write data.", e);
					callback.failed(e);
					return;
				}

				try {
					callback.success();
				} catch (Exception e) {
					ExLogPlugin
							.getInstance()
							.getLogger()
							.log(Level.SEVERE, "Unable to perform callback.", e);
				}
			}
		}).start();
	}

	/**
	 * Performs a data write and calls the callback function when complete.
	 * 
	 * @param data
	 *            The data to write.
	 * @param callback
	 *            The function to call when complete.
	 */
	public static void performWrite(final ExLogEntry data,
			final ExLogWriteCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ExLogPlugin.getInstance().getDataProvider()
							.appendData(data);
				} catch (Exception e) {
					ExLogPlugin.getInstance().getLogger()
							.log(Level.SEVERE, "Unable to write data.", e);
					callback.failed(e);
					return;
				}

				try {
					callback.success();
				} catch (Exception e) {
					ExLogPlugin
							.getInstance()
							.getLogger()
							.log(Level.SEVERE, "Unable to perform callback.", e);
				}
			}
		}).start();
	}

	/**
	 * Performs a Data Query and sends the result to the player.
	 * 
	 * @param query
	 *            The Query to perform.
	 * @param sender
	 *            The player to relay the result back to.
	 */
	public static void performQuery(final ExLogDataQuery query,
			final CommandSender sender, final boolean useUUID) {
		performQuery(query, new ExLogQueryCallback() {
			@Override
			public void success(ArrayList<ExLogEntry> results) throws Exception {
				sender.sendMessage(ChatColor.GOLD + "=== QUERY RESULTS ===");

				for (ExLogEntry entry : results) {
					ExLogLoggingSource originPlugin = (ExLogLoggingSource) Bukkit
							.getPluginManager().getPlugin(entry.origin);

					String prettyDate = ExLogPlugin.getInstance()
							.getDateFormat().format(new Date(entry.date));
					String message = ExLogLoggingSource.defaultFormatEntry(
							entry, useUUID);

					if (originPlugin != null)
						message = originPlugin.formatEntry(entry, useUUID);

					sender.sendMessage(prettyDate + ": " + message);
				}
			}
		});
	}
}