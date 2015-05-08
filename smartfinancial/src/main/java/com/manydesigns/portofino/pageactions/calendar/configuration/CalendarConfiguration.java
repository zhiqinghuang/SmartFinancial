package com.manydesigns.portofino.pageactions.calendar.configuration;

import com.manydesigns.elements.annotations.FieldSet;
import com.manydesigns.elements.annotations.LabelI18N;
import com.manydesigns.elements.annotations.MinIntValue;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.portofino.dispatcher.PageActionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.NONE)
public class CalendarConfiguration implements PageActionConfiguration {
	protected int maxEventsPerCellInMonthView = 3;
	protected int estimateEventsPerPageInAgendaView = 10;

	public static final Logger logger = LoggerFactory.getLogger(CalendarConfiguration.class);

	public CalendarConfiguration() {
		super();
	}

	public void init() {
	}

	@XmlAttribute
	@Required
	@FieldSet("month.view")
	@LabelI18N("max.events.per.day")
	@MinIntValue(1)
	public int getMaxEventsPerCellInMonthView() {
		return maxEventsPerCellInMonthView;
	}

	public void setMaxEventsPerCellInMonthView(int maxEventsPerCellInMonthView) {
		this.maxEventsPerCellInMonthView = maxEventsPerCellInMonthView;
	}

	@XmlAttribute
	@Required
	@FieldSet("agenda.view")
	@LabelI18N("events.per.page")
	@MinIntValue(1)
	public int getEstimateEventsPerPageInAgendaView() {
		return estimateEventsPerPageInAgendaView;
	}

	public void setEstimateEventsPerPageInAgendaView(int estimateEventsPerPageInAgendaView) {
		this.estimateEventsPerPageInAgendaView = estimateEventsPerPageInAgendaView;
	}
}