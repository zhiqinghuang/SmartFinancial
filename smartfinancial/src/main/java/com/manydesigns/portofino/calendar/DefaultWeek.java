package com.manydesigns.portofino.calendar;

import org.joda.time.DateMidnight;

public class DefaultWeek extends AbstractWeek<DefaultDay> {

	public DefaultWeek(DateMidnight weekStart, DateMidnight weekEnd) {
		super(weekStart, weekEnd);
	}

	@Override
	protected DefaultDay[] createDaysArray(int size) {
		return new DefaultDay[size];
	}

	@Override
	protected DefaultDay createDay(DateMidnight dayStart, DateMidnight dayEnd) {
		return new DefaultDay(dayStart, dayEnd);
	}
}