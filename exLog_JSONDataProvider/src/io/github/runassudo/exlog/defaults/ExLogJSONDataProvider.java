package io.github.runassudo.exlog.defaults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;

import org.json.JSONObject;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.query.ExLogDataQuery;

/**
 * Class to read and write data from an NBT data file. Very poor implementation,
 * stores data in memory when writing file. <b>Not recommended for production
 * use.</b>
 * 
 * @author runassudo
 * 
 */
public class ExLogJSONDataProvider extends ExLogDataProvider {
	public static final int DATA_VERSION = 1;

	@Override
	public ArrayList<ExLogEntry> readData(ExLogDataQuery query)
			throws Exception {
		File dataFile = new File(getConfig().getString("dataFile"));
		if (!dataFile.exists()) {
			getLogger().log(Level.WARNING,
					"No existing data file. An empty one will be created.");
			createFile();
		}

		ArrayList<ExLogEntry> data = new ArrayList<ExLogEntry>();
		try (BufferedReader rdr = new BufferedReader(new FileReader(dataFile))) {
			int fileVersion = Integer.parseInt(rdr.readLine());
			if (fileVersion != DATA_VERSION) {
				getLogger().log(
						Level.WARNING,
						"Data file has unsupported version " + fileVersion
								+ ".");
			}

			String read = null;
			while ((read = rdr.readLine()) != null) {
				ExLogEntry entry = new ExLogEntry();
				JSONObject jsonEntry = new JSONObject(read);

				entry.date = jsonEntry.getString("date");
				entry.x = (int) jsonEntry.getLong("x");
				entry.y = (int) jsonEntry.getLong("y");
				entry.z = (int) jsonEntry.getLong("z");
				entry.dimension = (int) jsonEntry.getLong("dimension");
				entry.player = jsonEntry.getString("player");

				if (jsonEntry.getJSONObject("otherData") != null) {
					JSONObject jsonOtherData = jsonEntry
							.getJSONObject("otherData");

					for (Object objectKey : jsonOtherData.keySet()) {
						String key = (String) objectKey;
						entry.otherData.put(key, jsonOtherData.getString(key));
					}
				}

				if (query.matches(entry)) {
					data.add(entry);
				}
			}
		}

		return data;
	}

	@Override
	public void appendData(ArrayList<ExLogEntry> data) throws Exception {
		File dataFile = new File(getConfig().getString("dataFile"));
		try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile, true))) {
			for (ExLogEntry entry : data) {
				JSONObject jsonEntry = new JSONObject();

				jsonEntry.put("date", entry.date);
				jsonEntry.put("x", entry.x);
				jsonEntry.put("y", entry.y);
				jsonEntry.put("z", entry.z);
				jsonEntry.put("dimension", entry.dimension);
				jsonEntry.put("player", entry.player);

				if (entry.otherData.size() > 0) {
					JSONObject jsonOtherData = new JSONObject();

					for (String key : entry.otherData.keySet()) {
						jsonOtherData.put(key, entry.otherData.get(key));
					}

					jsonEntry.put("otherData", jsonOtherData);
				}

				jsonEntry.write(pw);
				pw.println();
			}
		}
	}

	private void createFile() throws Exception {
		File dataFile = new File(getConfig().getString("dataFile"));
		try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile, true))) {
			pw.println(DATA_VERSION);
		}
	}
}
