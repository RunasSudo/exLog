package io.github.runassudo.exlog;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ExLogLoggingSource extends JavaPlugin implements Listener {
	public String formatEntry(ExLogEntry entry) {
		return ExLogLoggingSource.defaultFormatEntry(entry);
	}

	public final static String defaultFormatEntry(ExLogEntry entry) {
		return defaultFormatEntry(entry, "");
	}

	public final static String defaultFormatEntry(ExLogEntry entry,
			String action) {
		return (entry.rolledBack ? "[X] " : "") + ChatColor.BLUE + entry.player
				+ ChatColor.RESET + action + " @ " + "(" + entry.x + ","
				+ entry.y + "," + entry.z + ")";
	}

	/**
	 * Attempts to rollback the specified entry.
	 * 
	 * @param entry
	 *            The entry to rollback.
	 * @return <code>true</code> if rollback was successful, <code>false</code>
	 *         otherwise.
	 */
	public boolean rollbackEntry(ExLogEntry entry) {
		return false;
	}

	@Override
	public void onEnable() {
		File configFile = new File(this.getDataFolder() + "/config.yml");
		if (!configFile.exists()) {
			this.saveDefaultConfig();
		}

		if (ExLogPlugin.getInstance().getConfig()
				.getStringList("loggingSources").contains(getName())) {
			Bukkit.getPluginManager().registerEvents(this, this);

			getLogger().log(Level.INFO,
					"Registered " + getName() + " as Logging Source.");
		}
	}
}
