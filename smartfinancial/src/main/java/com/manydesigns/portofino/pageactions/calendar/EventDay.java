package com.manydesigns.portofino.pageactions.calendar;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;

public class EventDay {
	protected final DateMidnight day;
	protected final List<Event> events = new ArrayList<Event>();

	public EventDay(DateMidnight day, Event event) {
		this.day = day;
		events.add(event);
	}

	public List<Event> getEvents() {
		return events;
	}

	public DateMidnight getDay() {
		return day;
	}

}