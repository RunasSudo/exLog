package io.github.runassudo.exlog;

import java.util.HashMap;

import org.bukkit.block.Block;

public class ExLogEntry {
	public String origin = "";
	public int x = 0, y = 0, z = 0, dimension = -100;
	public String player = "";
	public long date = -1;

	public final HashMap<String, String> otherData = new HashMap<String, String>();

	public final static void populate(ExLogEntry entry, Block block) {
		entry.x = block.getX();
		entry.y = block.getY();
		entry.z = block.getZ();
		switch (block.getWorld().getEnvironment()) {
		case NORMAL:
			entry.dimension = 0;
			break;
		case NETHER:
			entry.dimension = 1;
			break;
		case THE_END:
			entry.dimension = 2;
			break;
		}
	}
}
