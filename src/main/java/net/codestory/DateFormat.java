package net.codestory;

import org.joda.time.format.*;

import java.util.*;

import static org.joda.time.format.DateTimeFormat.forPattern;

public final class DateFormat {
	private static final DateTimeFormatter FORMAT = forPattern("dd/MM/yyyy");

	private DateFormat() {
		// static class
	}

	public static String format(Date date) {
		return FORMAT.print(date.getTime());
	}
}
