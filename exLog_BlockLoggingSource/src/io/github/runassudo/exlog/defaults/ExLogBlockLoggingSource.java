package io.github.runassudo.exlog.defaults;

import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogLoggingSource;

public class ExLogBlockLoggingSource extends ExLogLoggingSource {
	@Override
	public String formatEntry(ExLogEntry entry) {
		String blockId = entry.otherData.get("blockId");

		if (blockId == null)
			return super.formatEntry(entry);

		if (blockId.equals("0"))
			return entry.player + " destroyed @ " + entry.dimension + "("
					+ entry.x + "," + entry.y + "," + entry.z + ")";
		else
			return entry.player + " placed " + blockId + " @ "
					+ entry.dimension + "(" + entry.x + "," + entry.y + ","
					+ entry.z + ")";
	}
}
