package io.github.runassudo.exlog;

import io.github.runassudo.exlog.query.ExLogDataQuery;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ExLogDataProvider extends JavaPlugin {
	public abstract ArrayList<ExLogEntry> readData(ExLogDataQuery query) throws Exception;

	public abstract void appendData(ArrayList<ExLogEntry> data) throws Exception;
	
	public void appendData(ExLogEntry entry) throws Exception {
		ArrayList<ExLogEntry> data = new ArrayList<ExLogEntry>();
		data.add(entry);
		appendData(data);
	}
}
