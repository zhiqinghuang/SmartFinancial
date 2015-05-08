package com.manydesigns.portofino.pageactions.calendar;

import org.joda.time.Interval;

public class Event {
	protected final Calendar calendar;
	protected String id;
	protected String description;
	protected Interval interval;
	protected String readUrl;
	protected String editUrl;

	public Event(Calendar calendar) {
		this.calendar = calendar;
	}

	public Event(Calendar calendar, String id, String description, Interval interval, String readUrl, String editUrl) {
		this.calendar = calendar;
		this.id = id;
		this.description = description;
		this.interval = interval;
		this.readUrl = readUrl;
		this.editUrl = editUrl;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public String getReadUrl() {
		return readUrl;
	}

	public void setReadUrl(String readUrl) {
		this.readUrl = readUrl;
	}

	public String getEditUrl() {
		return editUrl;
	}

	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
}