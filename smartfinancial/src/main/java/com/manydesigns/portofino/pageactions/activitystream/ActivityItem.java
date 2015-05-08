package com.manydesigns.portofino.pageactions.activitystream;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.util.Util;
import com.manydesigns.elements.xml.XhtmlBuffer;
import com.manydesigns.elements.xml.XhtmlFragment;
import org.apache.commons.lang.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityItem implements XhtmlFragment {
	FastDateFormat dateFormat;

	final Locale locale;
	final Date timestamp;
	final String imageSrc;
	final String imageHref;
	final String imageAlt;
	final String message;
	final String key;
	final List<Arg> args = new ArrayList<Arg>();

	boolean fullUrls = false;

	public ActivityItem(Locale locale, Date timestamp, String imageSrc, String imageHref, String imageAlt, String message, String key) {
		this.locale = locale;
		this.timestamp = timestamp;
		this.imageSrc = imageSrc;
		this.imageHref = imageHref;
		this.imageAlt = imageAlt;
		this.message = message;
		this.key = key;

		dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale);
	}

	@Override
	public void toXhtml(@NotNull XhtmlBuffer xb) {
		xb.openElement("div");
		xb.addAttribute("class", "media");

		writeImage(xb);

		writeBody(xb);

		xb.closeElement("div");
	}

	public void writeImage(XhtmlBuffer xb) {
		String absoluteSrc = Util.getAbsoluteUrl(imageSrc, fullUrls);
		xb.openElement("div");
		xb.addAttribute("class", "media-left");
		if (imageHref != null) {
			xb.openElement("a");
			xb.addAttribute("href", imageHref);
		}
		xb.openElement("img");
		xb.addAttribute("class", "media-object");
		xb.addAttribute("alt", imageAlt);
		xb.addAttribute("src", absoluteSrc);
		xb.closeElement("img");

		if (imageHref != null) {
			xb.closeElement("a");
		}
		xb.closeElement("div");
	}

	public void writeBody(XhtmlBuffer xb) {
		xb.openElement("div");
		xb.addAttribute("class", "media-body");

		writeData(xb);
		writeTimestamp(xb);
		writeMessage(xb);

		xb.closeElement("div");
	}

	public void writeData(XhtmlBuffer xb) {

		List<String> formattedArgs = new ArrayList<String>(args.size());
		for (Arg arg : args) {
			String formattedArg = arg.format();
			formattedArgs.add(formattedArg);
		}

		String text = ElementsThreadLocals.getText(key, formattedArgs.toArray());

		xb.openElement("div");
		xb.writeNoHtmlEscape(text);
		xb.closeElement("div");
	}

	public void writeTimestamp(XhtmlBuffer xb) {
		xb.openElement("div");
		xb.openElement("small");
		xb.addAttribute("class", "text-muted");
		xb.write(dateFormat.format(timestamp));
		xb.closeElement("small");
		xb.closeElement("div");
	}

	public void writeMessage(XhtmlBuffer xb) {
		if (message != null) {
			xb.openElement("div");
			xb.addAttribute("class", "activity-item-message");
			xb.writeNoHtmlEscape(message);
			xb.closeElement("div");
		}
	}

	public boolean isFullUrls() {
		return fullUrls;
	}

	public void setFullUrls(boolean fullUrls) {
		this.fullUrls = fullUrls;
	}

	public void addArg(String text, String url) {
		Arg arg = new Arg(text, url);
		args.add(arg);
	}

	public List<Arg> getArgs() {
		return args;
	}

	public class Arg {
		final String text;
		final String href;

		public Arg(String text, String href) {
			this.text = text;
			this.href = href;
		}

		public String getText() {
			return text;
		}

		public String getHref() {
			return href;
		}

		public String format() {
			XhtmlBuffer argXb = new XhtmlBuffer();
			argXb.openElement("strong");
			if (href == null) {
				argXb.write(text);
			} else {
				String absoluteHref = Util.getAbsoluteUrl(href, fullUrls);
				argXb.writeAnchor(absoluteHref, text);
			}
			argXb.closeElement("strong");
			return argXb.toString();
		}
	}
}