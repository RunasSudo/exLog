package io.github.runassudo.exlog;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ExLogDataProvider extends JavaPlugin {
	public abstract ArrayList<ExLogEntry> readData() throws Exception;

	public abstract void writeData(ArrayList<ExLogEntry> data) throws Exception;
}
