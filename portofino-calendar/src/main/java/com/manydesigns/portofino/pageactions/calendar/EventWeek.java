package com.manydesigns.portofino.pageactions.calendar;

public class EventWeek {
	protected final Event event;
	protected int startDay;
	protected int endDay;
	protected boolean continues;

	public EventWeek(Event event) {
		this.event = event;
	}

	public EventWeek(Event event, int startDay, int endDay, boolean continues) {
		this.event = event;
		this.startDay = startDay;
		this.endDay = endDay;
		this.continues = continues;
	}

	public Event getEvent() {
		return event;
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public boolean isContinues() {
		return continues;
	}

	public void setContinues(boolean continues) {
		this.continues = continues;
	}
}