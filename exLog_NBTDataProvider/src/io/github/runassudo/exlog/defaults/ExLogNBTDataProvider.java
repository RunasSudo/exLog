package io.github.runassudo.exlog.defaults;

import java.util.ArrayList;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;

public class ExLogNBTDataProvider extends ExLogDataProvider {
	@Override
	public ArrayList<ExLogEntry> readData() throws Exception {
		return new ArrayList<ExLogEntry>();
	}

	@Override
	public void writeData(ArrayList<ExLogEntry> data) throws Exception {
		
	}
}
