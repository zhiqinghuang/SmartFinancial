package com.manydesigns.portofino.pageactions.calendar;

import com.manydesigns.elements.util.StringComparator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.util.Comparator;

public class EventWeekComparator implements Comparator<EventWeek> {

	final DateTimeComparator dateTimeComparator;
	final StringComparator stringComparator;

	public EventWeekComparator() {
		dateTimeComparator = DateTimeComparator.getInstance();
		stringComparator = new StringComparator();
	}

	public int compare(EventWeek eventWeek1, EventWeek eventWeek2) {
		Event event1 = eventWeek1.getEvent();
		DateTime startDateTime1 = event1.getInterval().getStart();

		Event event2 = eventWeek2.getEvent();
		DateTime startDateTime2 = event2.getInterval().getStart();

		int result = dateTimeComparator.compare(startDateTime1, startDateTime2);
		if (result == 0) {
			return stringComparator.compare(event1.getId(), event2.getId());
		} else {
			return result;
		}
	}
}