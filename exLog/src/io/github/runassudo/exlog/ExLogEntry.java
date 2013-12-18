package io.github.runassudo.exlog;

import java.util.HashMap;

public class ExLogEntry {
	public int x = 0, y = 0, z = 0, dimension = -100;
	public String player = "";
	public long date = -1;
	
	public HashMap<String, String> otherData = new HashMap<String, String>();
}
