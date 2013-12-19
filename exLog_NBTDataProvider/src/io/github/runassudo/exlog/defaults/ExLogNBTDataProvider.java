package io.github.runassudo.exlog.defaults;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.ListTag;
import org.jnbt.LongTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.StringTag;
import org.jnbt.Tag;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.query.AllowAllDataQuery;
import io.github.runassudo.exlog.query.ExLogDataQuery;

/**
 * Class to read and write data from an NBT data file. Very poor implementation,
 * stores data in memory when writing file. <b>Not recommended for production
 * use.</b>
 * 
 * @author runassudo
 * 
 */
public class ExLogNBTDataProvider extends ExLogDataProvider {
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

		Map<String, Tag> root;
		NBTInputStream nis = null;
		try {
			nis = new NBTInputStream(new FileInputStream(dataFile));
			root = ((CompoundTag) nis.readTag()).getValue();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to read data file.", e);
			throw e;
		} finally {
			nis.close();
		}

		int fileVersion = ((IntTag) root.get("version")).getValue().intValue();
		if (fileVersion != DATA_VERSION) {
			getLogger().log(Level.WARNING,
					"Data file has unsupported version " + fileVersion + ".");
		}

		ArrayList<ExLogEntry> data = new ArrayList<ExLogEntry>();
		List<Tag> nbtData = ((ListTag) root.get("entries")).getValue();

		for (Tag tag : nbtData) {
			ExLogEntry entry = new ExLogEntry();
			Map<String, Tag> nbtEntry = ((CompoundTag) tag).getValue();

			entry.origin = ((StringTag) nbtEntry.get("origin")).getValue();
			entry.date = ((LongTag) nbtEntry.get("date")).getValue();
			entry.x = ((IntTag) nbtEntry.get("x")).getValue().intValue();
			entry.y = ((IntTag) nbtEntry.get("y")).getValue().intValue();
			entry.z = ((IntTag) nbtEntry.get("z")).getValue().intValue();
			entry.dimension = ((IntTag) nbtEntry.get("dimension")).getValue()
					.intValue();
			entry.player = ((StringTag) nbtEntry.get("player")).getValue();

			if (nbtEntry.get("otherData") != null) {
				Map<String, Tag> nbtOtherData = ((CompoundTag) nbtEntry
						.get("otherData")).getValue();

				for (String key : nbtOtherData.keySet()) {
					entry.otherData.put(key,
							((StringTag) nbtOtherData.get(key)).getValue());
				}
			}

			if (query.matches(entry)) {
				data.add(entry);
			}
		}

		return data;
	}

	@Override
	public void appendData(ArrayList<ExLogEntry> data) throws Exception {
		ArrayList<ExLogEntry> wholeData = readData(new AllowAllDataQuery());
		wholeData.addAll(data);

		int index = 0;
		List<Tag> nbtData = new ArrayList<Tag>();
		for (ExLogEntry entry : wholeData) {
			Map<String, Tag> nbtEntry = new HashMap<String, Tag>();

			nbtEntry.put("origin", new StringTag("player", entry.origin));
			nbtEntry.put("date", new LongTag("date", entry.date));
			nbtEntry.put("x", new IntTag("x", entry.x));
			nbtEntry.put("y", new IntTag("y", entry.y));
			nbtEntry.put("z", new IntTag("z", entry.z));
			nbtEntry.put("dimension", new IntTag("dimension", entry.dimension));
			nbtEntry.put("player", new StringTag("player", entry.player));

			if (entry.otherData.size() > 0) {
				Map<String, Tag> nbtOtherData = new HashMap<String, Tag>();

				for (String key : entry.otherData.keySet()) {
					nbtOtherData.put(key,
							new StringTag(key, entry.otherData.get(key)));
				}

				nbtEntry.put("otherData", new CompoundTag("otherData",
						nbtOtherData));
			}

			nbtData.add(new CompoundTag("entry" + index, nbtEntry));
			index++;
		}

		ListTag nbtDataTag = new ListTag("entries", CompoundTag.class, nbtData);
		Map<String, Tag> root = new HashMap<String, Tag>();
		root.put("version", new IntTag("version", DATA_VERSION));
		root.put("entries", nbtDataTag);

		File dataFile = new File(getConfig().getString("dataFile"));
		NBTOutputStream nos = null;
		try {
			nos = new NBTOutputStream(new FileOutputStream(dataFile));
			nos.writeTag(new CompoundTag("root", root));
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to write data file.", e);
			throw e;
		} finally {
			nos.close();
		}
	}

	private void createFile() throws Exception {
		List<Tag> nbtData = new ArrayList<Tag>();
		ListTag nbtDataTag = new ListTag("entries", CompoundTag.class, nbtData);
		Map<String, Tag> root = new HashMap<String, Tag>();
		root.put("version", new IntTag("version", DATA_VERSION));
		root.put("entries", nbtDataTag);

		File dataFile = new File(getConfig().getString("dataFile"));
		NBTOutputStream nos = null;
		try {
			nos = new NBTOutputStream(new FileOutputStream(dataFile));
			nos.writeTag(new CompoundTag("root", root));
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Unable to write data file.", e);
			throw e;
		} finally {
			nos.close();
		}
	}
}
