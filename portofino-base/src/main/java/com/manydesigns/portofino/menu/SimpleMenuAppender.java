package com.manydesigns.portofino.menu;

import com.manydesigns.elements.ElementsThreadLocals;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMenuAppender implements MenuAppender {

	public static final Logger logger = LoggerFactory.getLogger(SimpleMenuAppender.class);

	public final String targetMenuGroupId;
	public final String id;
	public final String icon;
	public final String labelKey;
	public final String link;
	public final double order;

	protected SimpleMenuAppender(String targetMenuGroupId, String id, String icon, String labelKey, String link, double order) {
		this.targetMenuGroupId = targetMenuGroupId;
		this.id = id;
		this.icon = icon;
		this.labelKey = labelKey;
		this.link = link;
		this.order = order;
	}

	public static SimpleMenuAppender group(String id, @Nullable String icon, String labelKey, double order) {
		return new SimpleMenuAppender(null, id, icon, labelKey, null, order);
	}

	public static SimpleMenuAppender link(String id, @Nullable String icon, String labelKey, String link, double order) {
		return new SimpleMenuAppender(null, id, icon, labelKey, link, order);
	}

	public static SimpleMenuAppender link(String targetMenuGroupId, String id, @Nullable String icon, String labelKey, String link, double order) {
		return new SimpleMenuAppender(targetMenuGroupId, id, icon, labelKey, link, order);
	}

	@Override
	public void append(Menu menu) {
		String label = ElementsThreadLocals.getText(labelKey);
		if (this.link == null) {
			appendMenuGroup(menu, label);
		} else {
			appendMenuLink(menu, label);
		}
	}

	protected void appendMenuGroup(Menu menu, String label) {
		MenuGroup group = new MenuGroup(id, icon, label, order);
		menu.items.add(group);
	}

	protected void appendMenuLink(Menu menu, String label) {
		MenuLink link = new MenuLink(id, icon, label, this.link, order);
		MenuGroup targetGroup = null;
		if (targetMenuGroupId != null) {
			for (MenuItem item : menu.items) {
				if (targetMenuGroupId.equals(item.id)) {
					if (item instanceof MenuGroup) {
						targetGroup = (MenuGroup) item;
						break;
					} else {
						logger.warn("Could not add menu link " + id + " to " + targetMenuGroupId + " because it is a link, not a menu group");
					}
				}
			}
			if (targetGroup == null) {
				logger.warn("Could not find menu group " + targetMenuGroupId);
			}
		}
		if (targetGroup == null) {
			menu.items.add(link);
		} else {
			targetGroup.menuLinks.add(link);
		}
	}
}