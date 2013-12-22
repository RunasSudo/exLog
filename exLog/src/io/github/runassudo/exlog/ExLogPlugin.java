package io.github.runassudo.exlog;

import io.github.runassudo.exlog.query.JSONDataQuery;
import io.github.runassudo.exlog.util.ExLogDataHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
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
				sender.sendMessage("Refer to BukkitDev or Github for more info.");
				return true;
			}
			if (args[0].equalsIgnoreCase("info")) {
				sender.sendMessage("exLog: The lightweight extensible Bukkit logging platform.");
				sender.sendMessage("Version " + getDescription().getVersion());
				sender.sendMessage("Created by RunasSudo.");
				return true;
			}
			if (args[0].equalsIgnoreCase("test")) {
				try {
					ExLogEntry entry = new ExLogEntry();
					entry.date = System.currentTimeMillis();
					entry.x = 1;
					entry.y = 2;
					entry.z = 3;
					entry.dimension = 4;
					entry.player = "Player1";
					entry.otherData.put("data1", "hello");
					getDataProvider().appendData(entry);
					sender.sendMessage("Added dummy entry.");
				} catch (Exception e) {
					getLogger().log(Level.SEVERE, "Failed to add entry.", e);
					sender.sendMessage("Failed to add entry. Check logs.");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("query")) {
				String queryString = join(" ",
						Arrays.copyOfRange(args, 1, args.length));
				JSONObject queryObject = new JSONObject(queryString);
				ExLogDataHelper.performQuery(new JSONDataQuery(queryObject),
						sender);
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
}
