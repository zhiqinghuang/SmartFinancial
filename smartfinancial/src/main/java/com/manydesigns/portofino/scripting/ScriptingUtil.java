package com.manydesigns.portofino.scripting;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.elements.util.RandomUtil;
import com.manydesigns.portofino.modules.BaseModule;
import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import ognl.OgnlContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ScriptingUtil {
	public static final Logger logger = LoggerFactory.getLogger(ScriptingUtil.class);

	public static final String GROOVY_FILE_NAME_PATTERN = "{0}.groovy";

	public static Object runScript(String script, String scriptLanguage, Object root) throws Exception {
		OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
		if ("ognl".equals(scriptLanguage)) {
			return OgnlUtils.getValueQuietly(script, ognlContext, root);
		} else if ("groovy".equals(scriptLanguage)) {
			ognlContext.put("root", root);
			Binding binding = new Binding(ognlContext);
			GroovyShell shell = new GroovyShell(binding);
			return shell.evaluate(script);
		} else {
			String msg = String.format("Unrecognised script language: %s", scriptLanguage);
			throw new IllegalArgumentException(msg);
		}
	}

	public static GroovyObject getGroovyObject(File file) throws IOException, ScriptException, ResourceException {
		if (!file.exists()) {
			return null;
		}

		Class groovyClass = getGroovyClass(file);

		try {
			return (GroovyObject) groovyClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static File getGroovyScriptFile(File storageDir, String pageId) {
		return RandomUtil.getCodeFile(storageDir, GROOVY_FILE_NAME_PATTERN, pageId);
	}

	public static Class<?> getGroovyClass(File scriptFile) throws IOException, ScriptException, ResourceException {
		if (!scriptFile.exists()) {
			return null;
		}
		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) ElementsThreadLocals.getServletContext().getAttribute(BaseModule.GROOVY_SCRIPT_ENGINE);
		return scriptEngine.loadScriptByName(scriptFile.toURI().toString());
	}
}