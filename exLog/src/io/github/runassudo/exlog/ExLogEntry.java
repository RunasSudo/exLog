package io.github.runassudo.exlog;

import java.util.HashMap;

import org.bukkit.block.Block;

public class ExLogEntry {
	public String origin = "";
	public int x = 0, y = 0, z = 0;
	public String world = "";
	public String player = "";
	public long date = -1;
	public boolean rolledBack = false;

	public final HashMap<String, String> otherData = new HashMap<String, String>();

	public final static void populate(ExLogEntry entry, Block block) {
		entry.x = block.getX();
		entry.y = block.getY();
		entry.z = block.getZ();
		entry.world = block.getWorld().getName();
	}
}
