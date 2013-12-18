package io.github.runassudo.exlog;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ExLogPlugin extends JavaPlugin {
	// ----------------------------------------------------------------
	// GENERAL PLUGIN STUFF
	// ----------------------------------------------------------------
	@Override
	public void onEnable() {
		getLogger().log(Level.INFO,
				"Using " + getDataProvider().getName() + " as Data Provider.");
	}

	@Override
	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((cmd.getName().equalsIgnoreCase("exlog") || cmd.getName()
				.equalsIgnoreCase("el")) && args.length > 0) {
			switch (args[0]) {
			case "help":
				sender.sendMessage("Usage: /exlog [command]");
				sender.sendMessage("Refer to BukkitDev or Github for more info.");
				return true;
			case "info":
				sender.sendMessage("exLog: The lightweight extensible Bukkit logging platform.");
				sender.sendMessage("Version " + getDescription().getVersion());
				sender.sendMessage("Created by RunasSudo.");
				return true;
			case "test":
				try {
					ExLogEntry entry = new ExLogEntry();
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
		}
		return false;
	}

	// ----------------------------------------------------------------
	// EXLOG STUFF
	// ----------------------------------------------------------------

	/**
	 * Convenience method to get instance of ExLogPlugin.
	 * 
	 * @return the loaded instance of ExLogPlugin
	 */
	public static ExLogPlugin getInstance() {
		return (ExLogPlugin) Bukkit.getPluginManager().getPlugin("ExLogPlugin");
	}

	public ExLogDataProvider getDataProvider() {
		return (ExLogDataProvider) Bukkit.getPluginManager().getPlugin(
				getConfig().getString("dataProvider"));
	}
}
