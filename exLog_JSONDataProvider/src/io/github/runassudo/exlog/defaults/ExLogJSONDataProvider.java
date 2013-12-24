package io.github.runassudo.exlog.defaults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

import org.json.JSONObject;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogPlugin;
import io.github.runassudo.exlog.query.ExLogDataQuery;

/**
 * Class to read and write data from a JSON data file. Recommended for use on
 * small servers.
 * 
 * @author runassudo
 * 
 */
public class ExLogJSONDataProvider extends ExLogDataProvider {
	public static final int DATA_VERSION = 2;
	private File dataFile;

	@Override
	public void onEnable() {
		super.onEnable();

		dataFile = new File(getConfig().getString("dataFile"));
		if (!dataFile.exists()) {
			getLogger().log(Level.WARNING,
					"No existing data file. An empty one will be created.");
			try {
				createFile();
			} catch (Exception e) {
				getLogger().log(Level.SEVERE, "Unable to create new file.", e);
			}
		}

		try (BufferedReader rdr = new BufferedReader(new FileReader(dataFile))) {
			int fileVersion = Integer.parseInt(rdr.readLine());
			rdr.close();

			if (fileVersion != DATA_VERSION) {
				getLogger().log(
						Level.WARNING,
						"Data file has unsupported version " + fileVersion
								+ ". Making backup.");

				try {
					File backupFile = new File(getConfig()
							.getString("dataFile") + ".bak");
					if (backupFile.exists())
						backupFile.delete();
					Files.copy(dataFile.toPath(), backupFile.toPath());
				} catch (Exception e) {
					getLogger().log(Level.WARNING,
							"Unable to backup data file.", e);
				}
			}

		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to read data file.", e);
		}
	}

	@Override
	public synchronized ArrayList<ExLogEntry> readData(ExLogDataQuery query)
			throws Exception {
		ArrayList<ExLogEntry> data = new ArrayList<ExLogEntry>();
		try (BufferedReader rdr = new BufferedReader(new FileReader(dataFile))) {
			String read = null;
			while ((read = rdr.readLine()) != null) {
				ExLogEntry entry = ExLogPlugin
						.JSONtoEntry(new JSONObject(read));

				if (query.matches(entry)) {
					data.add(entry);
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to read data file.", e);
			throw e;
		}

		Collections.sort(data, new Comparator<ExLogEntry>() {
			@Override
			public int compare(ExLogEntry arg0, ExLogEntry arg1) {
				return (int) (arg0.date - arg1.date);
			}
		});

		return data;
	}

	@Override
	public synchronized void appendData(ArrayList<ExLogEntry> data)
			throws Exception {
		try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile, true))) {
			for (ExLogEntry entry : data) {
				JSONObject jsonEntry = ExLogPlugin.entryToJSON(entry);

				jsonEntry.write(pw);
				pw.println();
			}
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to write data file.", e);
			throw e;
		}
	}

	@Override
	public synchronized void removeData(ExLogDataQuery query) throws Exception {
		try (BufferedReader rdr = new BufferedReader(new FileReader(dataFile))) {
			try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile,
					false))) {
				String read = null;
				while ((read = rdr.readLine()) != null) {
					ExLogEntry entry = ExLogPlugin.JSONtoEntry(new JSONObject(
							read));

					if (!query.matches(entry)) {
						pw.println(read);
					}
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to read/write data file.", e);
			throw e;
		}
	}

	private void createFile() throws Exception {
		try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile, true))) {
			pw.println(DATA_VERSION);
		}
	}
}
