package io.github.runassudo.exlog.query;

import java.text.ParseException;

import org.json.JSONObject;

import io.github.runassudo.exlog.ExLogEntry;
import io.github.runassudo.exlog.ExLogPlugin;

public class JSONDataQuery extends ExLogDataQuery {
	JSONObject json;

	long dateMin, dateMax;

	public JSONDataQuery(JSONObject json) throws ParseException {
		this.json = json;

		if (json.has("dateMin"))
			dateMin = ExLogPlugin.getInstance().getDateFormat()
					.parse(json.getString("dateMin")).getTime();
		if (json.has("dateMax"))
			dateMax = ExLogPlugin.getInstance().getDateFormat()
					.parse(json.getString("dateMax")).getTime();
	}

	@Override
	public boolean matches(ExLogEntry entry) {
		if (json.has("origin")
				&& !json.getString("origin").equals(entry.origin))
			return false;
		if (json.has("x") && json.getInt("x") != entry.x)
			return false;
		if (json.has("y") && json.getInt("y") != entry.y)
			return false;
		if (json.has("z") && json.getInt("z") != entry.z)
			return false;
		if (json.has("world") && !json.getString("world").equals(entry.world))
			return false;
		if (json.has("player")
				&& !json.getString("player").equals(entry.player))
			return false;
		if (json.has("rolledBack")
				&& json.getBoolean("rolledBack") != entry.rolledBack)
			return false;

		if (json.has("date") && json.getLong("date") != entry.date)
			return false;
		if (json.has("dateMin") && entry.date < dateMin)
			return false;
		if (json.has("dateMax") && entry.date > dateMax)
			return false;

		if (json.has("otherData")) {
			JSONObject otherData = json.getJSONObject("otherData");
			for (Object k : otherData.keySet()) {
				String key = (String) k;
				if (!entry.otherData.get(key).equals(otherData.getString(key)))
					return false;
			}
		}

		return true;
	}
}
