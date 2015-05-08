package com.manydesigns.portofino.calendar;

import org.joda.time.DateMidnight;

public class DefaultMonth extends AbstractMonth<DefaultDay> {
	public DefaultMonth(DateMidnight referenceDateMidnight) {
		super(referenceDateMidnight);
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