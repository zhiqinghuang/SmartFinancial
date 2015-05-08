package com.manydesigns.portofino.pageactions.calendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class AgendaView {

	protected DateMidnight firstDay;
	protected final List<EventDay> events = new LinkedList<EventDay>();

	public AgendaView(DateTime referenceDateTime) {
		firstDay = new DateMidnight(referenceDateTime);
	}

	public int addEvent(Event event) {
		DateMidnight day = new DateMidnight(event.getInterval().getStart());
		DateTime end = event.getInterval().getEnd();
		int added = 0;
		while (end.minus(1).compareTo(day) >= 0) {
			if (addEvent(day, event)) {
				added++;
			}
			day = day.plusDays(1);
		}
		return added;
	}

	protected boolean addEvent(DateMidnight date, Event event) {
		if (date.isBefore(firstDay)) {
			return false;
		}
		int position = 0;
		for (EventDay eventDay : events) {
			int cmp = eventDay.getDay().compareTo(date);
			if (cmp == 0) {
				eventDay.getEvents().add(event);
				return true;
			} else if (cmp > 0) {
				EventDay newEventDay = new EventDay(date, event);
				events.add(position, newEventDay);
				return true;
			}
			position++;
		}
		EventDay newEventDay = new EventDay(date, event);
		events.add(newEventDay);
		return true;
	}

	public void sortEvents() {
		for (EventDay eventDay : events) {
			Collections.sort(eventDay.getEvents(), new Comparator<Event>() {
				public int compare(Event o1, Event o2) {
					return o1.getInterval().getStart().compareTo(o2.getInterval().getStart());
				}
			});
		}
	}

	public List<EventDay> getEvents() {
		return events;
	}

	public DateMidnight getFirstDay() {
		return firstDay;
	}
}