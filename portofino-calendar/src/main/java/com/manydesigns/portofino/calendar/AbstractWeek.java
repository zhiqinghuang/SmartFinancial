package com.manydesigns.portofino.calendar;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWeek<U extends AbstractDay> {

	final DateMidnight weekStart;
	protected final DateMidnight weekEnd;
	protected final Interval weekInterval;
	protected final U[] days;

	public static final Logger logger = LoggerFactory.getLogger(AbstractWeek.class);

	public AbstractWeek(DateMidnight weekStart, DateMidnight weekEnd) {
		this.weekStart = weekStart;
		this.weekEnd = weekEnd;
		weekInterval = new Interval(weekStart, weekEnd);
		AbstractMonthView.logger.debug("Week interval: {}", weekInterval);

		AbstractMonthView.logger.debug("Initializing days");
		days = createDaysArray(7);
		DateMidnight dayStart = weekStart;
		for (int i = 0; i < 7; i++) {
			DateMidnight dayEnd = dayStart.plusDays(1);
			days[i] = createDay(dayStart, dayEnd);

			dayStart = dayEnd;
		}
	}

	protected abstract U[] createDaysArray(int size);

	protected abstract U createDay(DateMidnight dayStart, DateMidnight dayEnd);

	public U findDayByDateTime(@NotNull DateTime dateTime) {
		if (!weekInterval.contains(dateTime)) {
			logger.debug("DateTime not in week internal: {}", dateTime);
			return null;
		}
		for (U current : days) {
			if (current.getDayInterval().contains(dateTime)) {
				return current;
			}
		}
		throw new InternalError("DateTime in week but not in week's days: " + dateTime);
	}

	public DateMidnight getWeekStart() {
		return weekStart;
	}

	public DateMidnight getWeekEnd() {
		return weekEnd;
	}

	public Interval getWeekInterval() {
		return weekInterval;
	}

	public U getDay(int index) {
		return days[index];
	}
}