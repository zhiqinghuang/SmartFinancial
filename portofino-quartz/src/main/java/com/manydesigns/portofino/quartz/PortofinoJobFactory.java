package com.manydesigns.portofino.quartz;

import com.manydesigns.portofino.di.Injections;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.servlet.ServletContext;

public class PortofinoJobFactory extends SimpleJobFactory {

	private final ServletContext servletContext;

	public PortofinoJobFactory(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
		Job job = super.newJob(bundle, scheduler);
		Injections.inject(job, servletContext, null);
		return job;
	}
}