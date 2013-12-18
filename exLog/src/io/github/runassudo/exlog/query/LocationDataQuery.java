package io.github.runassudo.exlog.query;

import io.github.runassudo.exlog.ExLogEntry;

public class LocationDataQuery extends ExLogDataQuery {
	int x, y, z, dimension;

	public LocationDataQuery(int x, int y, int z, int dimension) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}

	@Override
	public boolean matches(ExLogEntry entry) {
		return (entry.x == x && entry.y == y && entry.z == z && entry.dimension == dimension);
	}
}
