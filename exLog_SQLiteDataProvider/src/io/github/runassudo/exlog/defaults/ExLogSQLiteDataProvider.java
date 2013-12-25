package io.github.runassudo.exlog.defaults;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.query.ExLogDataQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class ExLogSQLiteDataProvider extends ExLogDataProvider {
	@Override
	public void onEnable() {
		super.onEnable();

		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"
					+ getConfig().getString("dataFile"));
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@Override
	public ArrayList<ExLogEntry> readData(ExLogDataQuery query)
			throws Exception {
		return null;
	}

	@Override
	public synchronized void appendData(ArrayList<ExLogEntry> data)
			throws Exception {
	}

	@Override
	public synchronized void removeData(ExLogDataQuery query) throws Exception {
	}
}
