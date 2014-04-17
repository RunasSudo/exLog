package io.github.runassudo.exlog.defaults;

import io.github.runassudo.exlog.ExLogDataProvider;
import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.query.ExLogDataQuery;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ExLogSQLiteDataProvider extends ExLogDataProvider {
	// SQL Statements
	private static final String sqlCreateTable = "CREATE TABLE exlog ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "origin TEXT, "
			+ "x INTEGER, " + "y INTEGER, " + "z INTEGER, " + "world TEXT, "
			+ "player TEXT, " + "uuid TEXT, " + "date INTEGER, "
			+ "rolledBack BOOLEAN, " + "otherData TEXT" + ")";

	private static final String sqlAppendEntry = "INSERT INTO exlog ("
			+ "origin, x, y, z, world, player, uuid, date, rolledBack, otherData"
			+ ") VALUES(" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + ")";

	private static final String sqlReadEntry = "SELECT * FROM exlog";

	private static final String sqlDeleteEntry = "DELETE FROM exlog";

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

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ExLogEntry> readData(ExLogDataQuery query)
			throws Exception {
		Statement statement = getConnection().createStatement();
		ResultSet result = statement.executeQuery(sqlReadEntry + query.toSQL());
		ArrayList<ExLogEntry> data = new ArrayList<ExLogEntry>();

		while (result.next()) {
			ExLogEntry entry = new ExLogEntry();

			entry.origin = result.getString("origin");
			entry.x = result.getInt("x");
			entry.y = result.getInt("y");
			entry.z = result.getInt("z");
			entry.world = result.getString("world");
			entry.player = result.getString("player");
			entry.uuid = UUID.fromString(result.getString("uuid"));
			entry.date = result.getLong("date");
			entry.rolledBack = result.getBoolean("rolledBack");
			entry.otherData.putAll((JSONObject) new JSONParser().parse(result
					.getString("otherData"))); // nasty generics hack

			data.add(entry);
		}

		return data;
	}

	@Override
	public void appendData(ArrayList<ExLogEntry> data) throws Exception {
		if (!new File(getConfig().getString("dataFile")).exists()) {
			createTable();
		}

		PreparedStatement statement = getConnection().prepareStatement(
				sqlAppendEntry);
		for (ExLogEntry entry : data) {
			statement.setString(1, entry.origin);
			statement.setInt(2, entry.x);
			statement.setInt(3, entry.y);
			statement.setInt(4, entry.z);
			statement.setString(5, entry.world);
			statement.setString(6, entry.player);
			statement.setString(7, entry.uuid.toString());
			statement.setLong(8, entry.date);
			statement.setBoolean(9, entry.rolledBack);
			statement.setString(10, JSONObject.toJSONString(entry.otherData));
			statement.executeUpdate();
		}
	}

	@Override
	public void removeData(ExLogDataQuery query) throws Exception {
		Statement statement = getConnection().createStatement();
		statement.executeUpdate(sqlDeleteEntry + query.toSQL());
	}

	private void createTable() throws Exception {
		Statement statement = getConnection().createStatement();
		statement.executeUpdate(sqlCreateTable);
	}
}
