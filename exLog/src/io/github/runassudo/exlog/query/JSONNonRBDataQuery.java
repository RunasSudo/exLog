package io.github.runassudo.exlog.query;

import java.text.ParseException;

import org.json.simple.JSONObject;

import io.github.runassudo.exlog.ExLogEntry;

public class JSONNonRBDataQuery extends JSONDataQuery {
	public JSONNonRBDataQuery(JSONObject json) throws ParseException {
		super(json);
	}

	@Override
	public boolean matches(ExLogEntry entry) {
		if (json.get("rolledBack").equals(true))
			return false;

		return super.matches(entry);
	}

	@Override
	public String toSQL() {
		StringBuilder sql = new StringBuilder();

		if (json.containsKey("origin")) {
			append(sql, "origin = '" + json.get("origin") + "'");
		}
		if (json.containsKey("x")) {
			append(sql, "x = " + json.get("x"));
		}
		if (json.containsKey("y")) {
			append(sql, "y = " + json.get("y"));
		}
		if (json.containsKey("z")) {
			append(sql, "z = " + json.get("z"));
		}
		if (json.containsKey("world")) {
			append(sql, "world = '" + json.get("world") + "'");
		}
		if (json.containsKey("player")) {
			append(sql, "player = '" + json.get("player") + "'");
		}
		if (json.containsKey("uuid")) {
			append(sql, "player = '" + json.get("uuid") + "'");
		}
		append(sql, "rolledBack = 0");

		if (json.containsKey("date")) {
			append(sql, "date = " + json.get("date"));
		}
		if (json.containsKey("dateMin")) {
			append(sql, "date > " + dateMin);
		}
		if (json.containsKey("dateMax")) {
			append(sql, "date < " + dateMax);
		}

		return sql.toString();
	}

	private void append(StringBuilder sql, String query) {
		if (sql.length() == 0)
			sql.append(" WHERE ");
		if (sql.length() != 7)
			sql.append(" AND ");
		sql.append(query);
	}
}
