package io.github.runassudo.exlog.query;

import java.text.ParseException;

import org.json.simple.JSONObject;

import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogPlugin;

public class JSONDataQuery extends ExLogDataQuery {
	JSONObject json;

	long dateMin, dateMax;

	public JSONDataQuery(JSONObject json) throws ParseException {
		this.json = json;

		if (json.containsKey("dateMin"))
			dateMin = ExLogPlugin.getInstance().getDateFormat()
					.parse((String) json.get("dateMin")).getTime();
		if (json.containsKey("dateMax"))
			dateMax = ExLogPlugin.getInstance().getDateFormat()
					.parse((String) json.get("dateMax")).getTime();
	}

	@Override
	public boolean matches(ExLogEntry entry) {
		if (json.containsKey("origin")
				&& !json.get("origin").equals(entry.origin))
			return false;

		if (!testInt("x", entry.x))
			return false;
		if (!testInt("y", entry.y))
			return false;
		if (!testInt("z", entry.z))
			return false;

		if (json.containsKey("world") && !json.get("world").equals(entry.world))
			return false;
		if (json.containsKey("player")
				&& !json.get("player").equals(entry.player))
			return false;
		if (json.containsKey("uuid")
				&& !json.get("uuid").equals(entry.uuid.toString()))
			return false;
		if (json.containsKey("rolledBack")
				&& !json.get("rolledBack").equals(entry.rolledBack))
			return false;

		if (json.containsKey("date") && !json.get("date").equals(entry.date))
			return false;
		if (json.containsKey("dateMin") && entry.date < dateMin)
			return false;
		if (json.containsKey("dateMax") && entry.date > dateMax)
			return false;

		if (json.containsKey("otherData")) {
			JSONObject otherData = (JSONObject) json.get("otherData");
			for (Object k : otherData.keySet()) {
				String key = (String) k;
				if (!entry.otherData.get(key).equals(otherData.get(key)))
					return false;
			}
		}

		return true;
	}

	private boolean testInt(String test, int value) {
		if (json.containsKey(test) && ((int) json.get(test)) != value)
			return false;
		if (json.containsKey(test + "Min")
				&& value < ((int) json.get(test + "Min")))
			return false;
		if (json.containsKey(test + "Max")
				&& value > ((int) json.get(test + "Max")))
			return false;

		return true;
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
		if (json.containsKey("rolledBack")) {
			append(sql, "rolledBack = "
					+ (json.get("rolledBack").equals(true) ? 1 : 0));
		}

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
