package io.github.runassudo.exlog;

import io.github.runassudo.exlog.query.JSONDataQuery;
import io.github.runassudo.exlog.util.ExLogDataHelper;
import io.github.runassudo.exlog.util.ExLogQueryCallback;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class ExLogPlugin extends JavaPlugin {
	// ----------------------------------------------------------------
	// GENERAL PLUGIN STUFF
	// ----------------------------------------------------------------
	@Override
	public void onEnable() {
		File configFile = new File(this.getDataFolder() + "/config.yml");
		if (!configFile.exists()) {
			this.saveDefaultConfig();
		}

		dateFormat = new SimpleDateFormat(getConfig().getString("dateFormat"));

		getLogger().log(Level.INFO,
				"Using " + getDataProvider().getName() + " as Data Provider.");

		ExLogPlayerListener listener = new ExLogPlayerListener();
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((cmd.getName().equalsIgnoreCase("exlog") || cmd.getName()
				.equalsIgnoreCase("el")) && args.length > 0) {
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("Usage: /exlog [command]");
				sender.sendMessage("Commands:");
				sender.sendMessage(" help");
				sender.sendMessage(" info");
				sender.sendMessage(" query [JSON query]");
				sender.sendMessage(" (rollback|rb) (y)? [JSON query]");
				return true;
			}
			if (args[0].equalsIgnoreCase("info")) {
				sender.sendMessage("exLog: The lightweight extensible Bukkit logging platform.");
				sender.sendMessage("Version " + getDescription().getVersion());
				sender.sendMessage("Created by RunasSudo.");
				return true;
			}

			// Privileged Commands
			if (args[0].equalsIgnoreCase("query")) {
				if (sender.hasPermission("exlog.command.query")) {
					if (args.length < 2)
						return false;

					String queryString = join(" ",
							Arrays.copyOfRange(args, 1, args.length));
					JSONObject queryObject = new JSONObject(queryString);
					try {
						ExLogDataHelper.performQuery(new JSONDataQuery(
								queryObject), sender);
					} catch (ParseException e) {
						sender.sendMessage(ChatColor.RED
								+ "Invalid date format.");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "No permission.");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("rollback")
					|| args[0].equalsIgnoreCase("rb")) {
				if (sender.hasPermission("exlog.command.rollback")) {
					if (args.length < 2)
						return false;

					if (args[1].equalsIgnoreCase("y")) {
						String queryString = join(" ",
								Arrays.copyOfRange(args, 2, args.length));

						JSONObject queryObject = new JSONObject(queryString);
						try {
							ExLogDataHelper.performQuery(new JSONDataQuery(
									queryObject), new RollbackCallback(sender));
						} catch (ParseException e) {
							sender.sendMessage(ChatColor.RED
									+ "Invalid date format.");
						}
					} else {
						String queryString = join(" ",
								Arrays.copyOfRange(args, 1, args.length));

						JSONObject queryObject = new JSONObject(queryString);
						try {
							ExLogDataHelper.performQuery(new JSONDataQuery(
									queryObject), sender);
							sender.sendMessage("If happy with the results, use /exlog rollback y [query] to perform rollback.");
						} catch (ParseException e) {
							sender.sendMessage(ChatColor.RED
									+ "Invalid date format.");
						}
					}
				} else {
					sender.sendMessage(ChatColor.RED + "No permission.");
				}
				return true;
			}
		}
		return false;
	}

	private static String join(String glue, String... data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(data[i]);
			if (i != data.length - 1)
				sb.append(glue);
		}
		return sb.toString();
	}

	// ----------------------------------------------------------------
	// EXLOG STUFF
	// ----------------------------------------------------------------

	private SimpleDateFormat dateFormat;

	/**
	 * Convenience method to get instance of <code>ExLogPlugin</code>.
	 * 
	 * @return The loaded instance of <code>ExLogPlugin</code>.
	 */
	public static ExLogPlugin getInstance() {
		return (ExLogPlugin) Bukkit.getPluginManager().getPlugin("ExLog");
	}

	/**
	 * Gets the currently loaded Data Provider.
	 * 
	 * @return The loaded implementation of <code>ExLogDataProvider</code>.
	 */
	public ExLogDataProvider getDataProvider() {
		return (ExLogDataProvider) Bukkit.getPluginManager().getPlugin(
				getConfig().getString("dataProvider"));
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public static JSONObject entryToJSON(ExLogEntry entry) {
		JSONObject jsonEntry = new JSONObject();

		jsonEntry.put("origin", entry.origin);
		jsonEntry.put("date", entry.date);
		jsonEntry.put("x", entry.x);
		jsonEntry.put("y", entry.y);
		jsonEntry.put("z", entry.z);
		jsonEntry.put("world", entry.world);
		jsonEntry.put("player", entry.player);
		jsonEntry.put("rolledBack", entry.rolledBack);

		if (entry.otherData.size() > 0) {
			JSONObject jsonOtherData = new JSONObject();

			for (String key : entry.otherData.keySet()) {
				jsonOtherData.put(key, entry.otherData.get(key));
			}

			jsonEntry.put("otherData", jsonOtherData);
		}

		return jsonEntry;
	}

	public static ExLogEntry JSONtoEntry(JSONObject jsonEntry) {
		ExLogEntry entry = new ExLogEntry();

		entry.origin = jsonEntry.getString("origin");
		entry.date = jsonEntry.getLong("date");
		entry.x = jsonEntry.getInt("x");
		entry.y = jsonEntry.getInt("y");
		entry.z = jsonEntry.getInt("z");
		entry.world = jsonEntry.getString("world");
		entry.player = jsonEntry.getString("player");
		entry.rolledBack = jsonEntry.getBoolean("rolledBack");

		if (jsonEntry.has("otherData")) {
			JSONObject jsonOtherData = jsonEntry.getJSONObject("otherData");

			for (Object objectKey : jsonOtherData.keySet()) {
				String key = (String) objectKey;
				entry.otherData.put(key, jsonOtherData.getString(key));
			}
		}

		return entry;
	}

	// The anonymous class just got too tabbed-out...
	class RollbackCallback extends ExLogQueryCallback {
		CommandSender sender;

		public RollbackCallback(CommandSender sender) {
			this.sender = sender;
		}

		@Override
		public void success(ArrayList<ExLogEntry> results) throws Exception {
			for (ExLogEntry entry : results) {
				if (!entry.rolledBack) {
					ExLogLoggingSource originPlugin = (ExLogLoggingSource) Bukkit
							.getPluginManager().getPlugin(entry.origin);

					if (originPlugin != null) {
						boolean successful = originPlugin.rollbackEntry(entry);
						if (!successful)
							sender.sendMessage(ChatColor.RED
									+ "Could not roll back entry at "
									+ entry.date + ".");
					}

					getDataProvider().removeData(
							new JSONDataQuery(entryToJSON(entry)));
					entry.rolledBack = true;
					getDataProvider().appendData(entry);
				}
			}
		}

		@Override
		public void failed(Exception e) {
			sender.sendMessage(ChatColor.RED + "Unable to perform query.");
		}
	}
}
