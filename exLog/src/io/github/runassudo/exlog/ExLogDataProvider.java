package io.github.runassudo.exlog;

import io.github.runassudo.exlog.query.ExLogDataQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a Data Provider for exLog. Implementations should override at
 * least the <code>readData</code> and <code>appendData</code> methods.
 * 
 * @author runassudo
 * 
 */
public abstract class ExLogDataProvider extends JavaPlugin {
	/**
	 * Reads the data source and returns a list of all <code>ExLogEntr</code>ies
	 * matching the specified <code>ExLogDataQuery</code>. To return a list of
	 * all entries, use <code>AllowAllDataQuery</code>.
	 * 
	 * @param query
	 *            The <code>ExLogDataQuery</code> to match entries against.
	 * @return A list of all <code>ExLogEntr</code>ies matching the specified
	 *         <code>ExLogDataQuery</code>.
	 * @throws Exception
	 */
	public abstract ArrayList<ExLogEntry> readData(ExLogDataQuery query)
			throws Exception;

	/**
	 * Adds the specified list of <code>ExLogEntr</code>ies to the data source.
	 * Implementations should avoid reading the entire file into memory and
	 * writing the entire file out each time.
	 * 
	 * @param data
	 *            The <code>ExLogEntr</code>ies to add to the data source.
	 * @throws Exception
	 */
	public abstract void appendData(ArrayList<ExLogEntry> data)
			throws Exception;

	public abstract void removeData(ExLogDataQuery query) throws Exception;

	/**
	 * Convenience method to add a single <code>ExLogEntry</code> to the data
	 * source.
	 * 
	 * @param entry
	 *            The <code>ExLogEntry</code> to add to the data source.
	 * @throws Exception
	 */
	public void appendData(ExLogEntry entry) throws Exception {
		ArrayList<ExLogEntry> data = new ArrayList<ExLogEntry>();
		data.add(entry);
		appendData(data);
	}

	/**
	 * Flushes the data source, ensuring that all data is saved.
	 * 
	 * @throws Exception
	 */
	public void flush() throws Exception {
	}

	@Override
	public void onEnable() {
		File configFile = new File(this.getDataFolder() + "/config.yml");
		if (!configFile.exists()) {
			this.saveDefaultConfig();
		}
	}

	@Override
	public void onDisable() {
		try {
			flush();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to flush data file.", e);
		}
	}
}
