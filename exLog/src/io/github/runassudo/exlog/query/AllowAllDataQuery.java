package io.github.runassudo.exlog.query;

import io.github.runassudo.exlog.ExLogEntry;

public class AllowAllDataQuery extends ExLogDataQuery {
	@Override
	public boolean matches(ExLogEntry entry) {
		return true;
	}
}
