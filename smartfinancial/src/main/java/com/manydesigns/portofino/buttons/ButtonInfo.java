package com.manydesigns.portofino.buttons;

import com.manydesigns.portofino.buttons.annotations.Button;

import java.lang.reflect.Method;

public class ButtonInfo {
	private final Method method;
	private final Button button;
	private final Class fallbackClass;

	public ButtonInfo(Button button, Method method, Class fallbackClass) {
		this.button = button;
		this.method = method;
		this.fallbackClass = fallbackClass;
	}

	public Button getButton() {
		return button;
	}

	public Method getMethod() {
		return method;
	}

	public Class getFallbackClass() {
		return fallbackClass;
	}
}