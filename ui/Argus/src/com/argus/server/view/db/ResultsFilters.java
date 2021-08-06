package com.argus.server.view.db;

import java.util.Date;

import com.sencha.gxt.core.client.util.DateWrapper;

public class ResultsFilters {
	static boolean isBooleanFiltered(String test, String comparison, String value) {
		if (value == null) {
			return true;
		}
		boolean t = Boolean.valueOf(test);
		boolean v = Boolean.parseBoolean(value);

		return t != v;
	}

	static boolean isDateFiltered(String test, String comparison, String value) {
		Date t = new Date(Long.valueOf(test));
		Date v = new Date(Long.valueOf(value));
		if (value == null) {
			return false;
		}
		if ("after".equals(comparison)) {
			return v.before(t);
		} else if ("before".equals(comparison)) {
			return v.after(t);
		} else if ("on".equals(comparison)) {
			t = new DateWrapper(t).resetTime().asDate();
			v = new DateWrapper(v).resetTime().asDate();
			return !v.equals(t);
		}
		return true;
	}

	static boolean isListFiltered(String test, String value) {
		String[] tests = test.split("::");
		for (int i = 0; i < tests.length; i++) {
			if (tests[i].equals(value)) {
				return false;
			}
		}
		return true;
	}

	static boolean isNumberFiltered(String test, String comparison, String value) {
		if (value == null) {
			return false;
		}
		double t = Double.valueOf(test);
		double v = Double.valueOf(value);

		if ("gt".equals(comparison)) {
			return t >= v;
		} else if ("lt".equals(comparison)) {
			return t <= v;
		} else if ("eq".equals(comparison)) {
			return t != v;
		}
		return false;
	}
}
