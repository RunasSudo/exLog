package io.github.runassudo.exlog.query;

import io.github.runassudo.exlog.ExLogEntry;

public class LocationDataQuery extends ExLogDataQuery {
	int x, y, z;
	String world;

	public LocationDataQuery(int x, int y, int z, String world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}

	@Override
	public boolean matches(ExLogEntry entry) {
		return (entry.x == x && entry.y == y && entry.z == z && entry.world == world);
	}
}
