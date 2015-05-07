package com.manydesigns.portofino.calendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

public class DefaultMonthView extends AbstractMonthView<DefaultWeek> {
	public DefaultMonthView(DateTime referenceDateTime) {
		super(referenceDateTime);
	}

	public DefaultMonthView(DateTime referenceDateTime, int firstDayOfWeek) {
		super(referenceDateTime, firstDayOfWeek);
	}

	@Override
	protected DefaultWeek[] createWeeksArray(int size) {
		return new DefaultWeek[size];
	}

	@Override
	protected DefaultWeek createWeek(DateMidnight weekStart, DateMidnight weekEnd) {
		return new DefaultWeek(weekStart, weekEnd);
	}
}