package io.github.runassudo.exlog;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ExLogLoggingSource extends JavaPlugin {
	public String formatEntry(ExLogEntry entry) {
		return entry.player + " @ " + entry.dimension + "(" + entry.x + ","
				+ entry.y + "," + entry.z + ")";
	}

	@Override
	public void onEnable() {
		File configFile = new File(this.getDataFolder() + "/config.yml");
		if (!configFile.exists()) {
			this.saveDefaultConfig();
		}
	}
}
