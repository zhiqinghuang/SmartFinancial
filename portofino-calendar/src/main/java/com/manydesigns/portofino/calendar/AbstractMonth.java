package com.manydesigns.portofino.calendar;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMonth<T extends AbstractDay> {
	final DateMidnight referenceDateMidnight;

	final DateMidnight monthStart;
	final DateMidnight monthEnd;
	final Interval monthInterval;
	final int daysCount;

	protected final T[] days;

	public static final Logger logger = LoggerFactory.getLogger(AbstractMonthView.class);

	public AbstractMonth(DateMidnight referenceDateMidnight) {
		logger.debug("Initializing month");
		this.referenceDateMidnight = referenceDateMidnight;
		logger.debug("Reference date midnight: {}", referenceDateMidnight);

		monthStart = referenceDateMidnight.withDayOfMonth(1);
		monthEnd = monthStart.plusMonths(1);
		monthInterval = new Interval(monthStart, monthEnd);
		logger.debug("Month interval: {}", monthInterval);

		daysCount = Days.daysIn(monthInterval).getDays();
		logger.debug("Initializing {} days", daysCount);

		days = createDaysArray(daysCount);
		DateMidnight dayStart = monthStart;
		for (int i = 0; i < daysCount; i++) {
			DateMidnight dayEnd = dayStart.plusDays(1);
			days[i] = createDay(dayStart, dayEnd);

			// advance to next day
			dayStart = dayEnd;
		}
	}

	protected abstract T[] createDaysArray(int size);

	protected abstract T createDay(DateMidnight dayStart, DateMidnight dayEnd);

	public T findDayByDate(@NotNull DateMidnight dateMidnight) {
		if (!monthInterval.contains(dateMidnight)) {
			logger.debug("Date not in month interval: {}", dateMidnight);
			return null;
		}
		for (T current : days) {
			if (current.getDayInterval().contains(dateMidnight)) {
				return current;
			}
		}
		throw new InternalError("Date in month but not in month's days: " + dateMidnight);
	}

	public DateMidnight getReferenceDateMidnight() {
		return referenceDateMidnight;
	}

	public DateMidnight getMonthStart() {
		return monthStart;
	}

	public DateMidnight getMonthEnd() {
		return monthEnd;
	}

	public Interval getMonthInterval() {
		return monthInterval;
	}

	public int getDaysCount() {
		return daysCount;
	}

	public T getDay(int index) {
		return days[index];
	}
}