package io.github.runassudo.exlog.query;

import org.json.JSONObject;

import io.github.runassudo.exlog.ExLogEntry;

public class JSONDataQuery extends ExLogDataQuery {
	JSONObject json;

	public JSONDataQuery(JSONObject json) {
		this.json = json;
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
		if (json.has("dimension")
				&& json.getInt("dimension") != entry.dimension)
			return false;
		if (json.has("player")
				&& !json.getString("player").equals(entry.player))
			return false;
		if (json.has("date") && json.getLong("date") != entry.date)
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
