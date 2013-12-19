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
		return ChatColor.BLUE + entry.player + ChatColor.RESET + " @ "
				+ entry.dimension + "(" + entry.x + "," + entry.y + ","
				+ entry.z + ")";
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
