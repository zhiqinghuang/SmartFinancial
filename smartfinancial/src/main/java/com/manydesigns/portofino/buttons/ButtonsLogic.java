package com.manydesigns.portofino.buttons;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.buttons.annotations.Buttons;
import com.manydesigns.portofino.buttons.annotations.Guard;
import com.manydesigns.portofino.buttons.annotations.Guards;
import ognl.OgnlContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ButtonsLogic {

	public static class ButtonComparatorByOrder implements Comparator<ButtonInfo> {
		public int compare(ButtonInfo o1, ButtonInfo o2) {
			return Double.compare(o1.getButton().order(), o2.getButton().order());
		}
	}

	public static List<ButtonInfo> getButtonsForClass(Class<?> someClass, String list) {
		try {
			return classButtons.get(new MCKey(someClass, list));
		} catch (ExecutionException e) {
			throw new Error(e);
		}
	}

	public static List<ButtonInfo> computeButtonsForClass(Class<?> someClass, String list) {
		List<ButtonInfo> buttons = new ArrayList<ButtonInfo>();
		for (Method method : someClass.getMethods()) {
			if (method.isBridge() || method.isSynthetic()) {
				continue;
			}
			Button button = getButtonForMethod(method, list);
			if (button != null) {
				ButtonInfo buttonInfo = new ButtonInfo(button, method, someClass);
				buttons.add(buttonInfo);
			}
		}
		Collections.sort(buttons, new ButtonComparatorByOrder());
		// Group together buttons of the same group
		for (int i = 0; i < buttons.size() - 1; i++) {
			ButtonInfo info = buttons.get(i);
			String group = info.getButton().group();
			if (!StringUtils.isBlank(group)) {
				int count = 1;
				for (int j = i + 1; j < buttons.size(); j++) {
					ButtonInfo info2 = buttons.get(j);
					if (info2.getButton().group().equals(group)) {
						buttons.remove(j);
						buttons.add(i + count, info2);
						count++;
					}
				}
			}
		}
		return buttons;
	}

	protected static class MCKey {
		public final Class<?> theClass;
		public final String list;

		public MCKey(Class<?> theClass, String list) {
			this.theClass = theClass;
			this.list = list;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			MCKey mcKey = (MCKey) o;

			if (!list.equals(mcKey.list))
				return false;
			if (!theClass.equals(mcKey.theClass))
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = theClass.hashCode();
			result = 31 * result + list.hashCode();
			return result;
		}
	}

	protected static LoadingCache<MCKey, List<ButtonInfo>> classButtons = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<MCKey, List<ButtonInfo>>() {
		@Override
		public List<ButtonInfo> load(MCKey key) throws Exception {
			return computeButtonsForClass(key.theClass, key.list);
		}
	});

	public static Button getButtonForMethod(Method method, String list) {
		Button button = method.getAnnotation(Button.class);
		if (button != null && list.equals(button.list())) {
			return button;
		} else {
			Buttons buttons = method.getAnnotation(Buttons.class);
			if (buttons != null) {
				for (Button b : buttons.value()) {
					if (list.equals(b.list())) {
						return b;
					}
				}
			}
		}
		return null;
	}

	public static boolean doGuardsPass(Object actionBean, Method method) {
		return doGuardsPass(actionBean, method, null);
	}

	public static boolean doGuardsPass(Object actionBean, Method method, @Nullable GuardType type) {
		List<Guard> guards = getGuards(method, type);
		boolean pass = true;
		OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
		for (Guard guard : guards) {
			Object result = OgnlUtils.getValueQuietly(guard.test(), ognlContext, actionBean);
			pass &= result instanceof Boolean && ((Boolean) result);
		}
		return pass;
	}

	public static List<Guard> getGuards(Method method, GuardType type) {
		List<Guard> guardList = new ArrayList<Guard>();
		Guard guard = method.getAnnotation(Guard.class);
		if (guard != null && (type == null || type == guard.type())) {
			guardList.add(guard);
		} else {
			Guards guards = method.getAnnotation(Guards.class);
			if (guards != null) {
				for (Guard g : guards.value()) {
					if (type == null || type == g.type()) {
						guardList.add(g);
					}
				}
			}
		}
		return guardList;
	}

}