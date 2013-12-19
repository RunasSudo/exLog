package io.github.runassudo.exlog;

import java.util.HashMap;

import org.bukkit.World;
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
		entry.dimension = worldToDimension(block.getWorld());
	}

	public final static int worldToDimension(World world) {
		switch (world.getEnvironment()) {
		case NORMAL:
			return 0;
		case NETHER:
			return 1;
		case THE_END:
			return 2;
		}
		return -100;
	}
}
