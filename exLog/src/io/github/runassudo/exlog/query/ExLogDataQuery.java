package io.github.runassudo.exlog.query;

import io.github.runassudo.exlog.ExLogEntry;

public abstract class ExLogDataQuery {
	/**
	 * Checks if the specified <code>ExLogEntry</code> matches this Query.
	 * 
	 * @param entry
	 *            The <code>ExLogEntry</code> to compare against this Query.
	 * @return <code>true</code> if the specified Entry matches the Query.
	 */
	public abstract boolean matches(ExLogEntry entry);
}
