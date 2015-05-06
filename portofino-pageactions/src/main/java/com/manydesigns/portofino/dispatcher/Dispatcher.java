package com.manydesigns.portofino.dispatcher;

import com.manydesigns.elements.servlet.ServletUtils;
import com.manydesigns.portofino.pages.ChildPage;
import com.manydesigns.portofino.pages.Page;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher {

	public static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

	protected final Configuration configuration;
	protected final File pagesDirectory;
	protected final Map<String, Dispatch> cache = new ConcurrentHashMap<String, Dispatch>();

	public Dispatcher(Configuration configuration, File pagesDirectory) {
		this.configuration = configuration;
		this.pagesDirectory = pagesDirectory;
	}

	public Dispatch getDispatch(String path) {
		if (path.endsWith(".jsp")) {
			logger.debug("Path is a JSP page ({}), not dispatching.", path);
			return null;
		}

		path = normalizePath(path);

		Dispatch dispatch = cache.get(path);
		if (dispatch != null) {
			return dispatch;
		}

		int index;
		String subPath = path;
		while ((index = subPath.lastIndexOf('/')) != -1) {
			subPath = path.substring(0, index);
			dispatch = cache.get(subPath);
			if (dispatch != null) {
				break;
			}
		}
		if (dispatch == null) {
			List<PageInstance> pagePath = new ArrayList<PageInstance>();
			String[] fragments = StringUtils.split(path, '/');

			List<String> fragmentsAsList = Arrays.asList(fragments);
			ListIterator<String> fragmentsIterator = fragmentsAsList.listIterator();

			File rootDir = pagesDirectory;
			Page rootPage;
			try {
				rootPage = DispatcherLogic.getPage(rootDir);
			} catch (Exception e) {
				logger.error("Cannot load root page", e);
				return null;
			}

			PageInstance rootPageInstance = new PageInstance(null, rootDir, rootPage, null);
			pagePath.add(rootPageInstance);

			dispatch = getDispatch(pagePath, fragmentsIterator);
			if (dispatch != null) {
				cache.put(path, dispatch);
			}
			return dispatch;
		} else {
			List<PageInstance> pagePath = new ArrayList<PageInstance>(Arrays.asList(dispatch.getPageInstancePath()));
			String[] fragments = StringUtils.split(path.substring(subPath.length()), '/');

			List<String> fragmentsAsList = Arrays.asList(fragments);
			ListIterator<String> fragmentsIterator = fragmentsAsList.listIterator();

			dispatch = getDispatch(pagePath, fragmentsIterator);
			if (dispatch != null) {
				cache.put(path, dispatch);
			}
			return dispatch;
		}
	}

	protected Dispatch getDispatch(List<PageInstance> initialPath, ListIterator<String> fragmentsIterator) {
		try {
			makePageInstancePath(initialPath, fragmentsIterator);
		} catch (PageNotActiveException e) {
			logger.debug("Page not active, not creating dispatch");
			return null;
		} catch (Exception e) {
			logger.error("Couldn't create dispatch", e);
			return null;
		}

		if (fragmentsIterator.hasNext()) {
			logger.debug("Not all fragments matched");
			return null;
		}

		// check path contains root page and some child page at least
		if (initialPath.size() <= 1) {
			return null;
		}

		PageInstance[] pageArray = new PageInstance[initialPath.size()];
		initialPath.toArray(pageArray);

		Dispatch dispatch = new Dispatch(pageArray);
		return dispatch;
		// return checkDispatch(dispatch);
	}

	protected void makePageInstancePath(List<PageInstance> pagePath, ListIterator<String> fragmentsIterator) throws Exception {
		PageInstance parentPageInstance = pagePath.get(pagePath.size() - 1);
		File currentDirectory = parentPageInstance.getChildrenDirectory();
		boolean params = !parentPageInstance.getParameters().isEmpty();
		while (fragmentsIterator.hasNext()) {
			String nextFragment = fragmentsIterator.next();
			File childDirectory = new File(currentDirectory, nextFragment);
			if (childDirectory.isDirectory() && !PageInstance.DETAIL.equals(childDirectory.getName())) {
				ChildPage childPage = null;
				for (ChildPage candidate : parentPageInstance.getLayout().getChildPages()) {
					if (candidate.getName().equals(childDirectory.getName())) {
						childPage = candidate;
						break;
					}
				}
				if (childPage == null) {
					throw new PageNotActiveException();
				}

				Page page = DispatcherLogic.getPage(childDirectory);
				Class<? extends PageAction> actionClass = DispatcherLogic.getActionClass(configuration, childDirectory);
				PageInstance pageInstance = new PageInstance(parentPageInstance, childDirectory, page, actionClass);
				pagePath.add(pageInstance);
				makePageInstancePath(pagePath, fragmentsIterator);
				return;
			} else {
				if (!params) {
					currentDirectory = new File(currentDirectory, PageInstance.DETAIL);
					params = true;
				}
				parentPageInstance = parentPageInstance.copy();
				parentPageInstance.getParameters().add(nextFragment);
				pagePath.set(pagePath.size() - 1, parentPageInstance);
			}
		}
	}

	protected static String normalizePath(String originalPath) {
		String path = ServletUtils.removePathParameters(originalPath);
		path = ServletUtils.removeRedundantTrailingSlashes(path);
		return path;
	}
}