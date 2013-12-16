package io.github.runassudo.exlog;

import java.util.ArrayList;
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
		getLogger().log(Level.CONFIG,
				"Using " + getDataProvider().getName() + " as Data Provider.");

		try {
			data = getDataProvider().readData();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Failed to read data.", e);
		}
	}

	@Override
	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("exlog")
				|| cmd.getName().equalsIgnoreCase("el") && args.length > 0) {
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
			}
		}
		return false;
	}

	// ----------------------------------------------------------------
	// EXLOG STUFF
	// ----------------------------------------------------------------
	private ArrayList<ExLogEntry> data;

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
	
	public ArrayList<ExLogEntry> getData() {
		return data;
	}
}
