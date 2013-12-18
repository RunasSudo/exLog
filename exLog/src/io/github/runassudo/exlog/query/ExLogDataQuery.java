package io.github.runassudo.exlog.query;

import io.github.runassudo.exlog.ExLogEntry;

public abstract class ExLogDataQuery {
	public abstract boolean matches(ExLogEntry entry);
}
