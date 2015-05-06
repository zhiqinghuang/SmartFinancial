package com.manydesigns.portofino.calendar;

import org.joda.time.DateMidnight;
import org.joda.time.Interval;

public class AbstractDay {
	final DateMidnight dayStart;
	final DateMidnight dayEnd;
	final Interval dayInterval;

	public AbstractDay(DateMidnight dayStart, DateMidnight dayEnd) {
		this.dayStart = dayStart;
		this.dayEnd = dayEnd;
		dayInterval = new Interval(dayStart, dayEnd);
		AbstractMonthView.logger.debug("Day interval: {}", dayInterval);
	}

	public DateMidnight getDayStart() {
		return dayStart;
	}

	public DateMidnight getDayEnd() {
		return dayEnd;
	}

	public Interval getDayInterval() {
		return dayInterval;
	}
}