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
		if (json.containsKey("x") && !json.get("x").equals(entry.x))
			return false;
		if (json.containsKey("y") && !json.get("y").equals(entry.y))
			return false;
		if (json.containsKey("z") && !json.get("z").equals(entry.z))
			return false;
		if (json.containsKey("world") && !json.get("world").equals(entry.world))
			return false;
		if (json.containsKey("player")
				&& !json.get("player").equals(entry.player))
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
}
