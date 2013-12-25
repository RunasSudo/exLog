package io.github.runassudo.exlog.defaults;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.query.ExLogDataQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.logging.Level;

public class ExLogSQLiteDataProvider extends ExLogDataProvider {
	@Override
	public void onEnable() {
		super.onEnable();
	}

	private Connection sqlConnection;

	private Connection getConnection() {
		if (sqlConnection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				sqlConnection = DriverManager.getConnection("jdbc:sqlite:"
						+ getConfig().getString("dataFile"));
			} catch (Exception e) {
				getLogger().log(Level.SEVERE, "Unable to open database.", e);
			}
		}

		return sqlConnection;
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
