package org.ripla.web.demo.skin.stylish;

import org.ripla.web.util.AbstractWebMessages;

/**
 * Bundle specific messages.
 * 
 * @author Luthiger
 */
public class Messages extends AbstractWebMessages {
	private static final String BASE_NAME = "messages"; //$NON-NLS-1$

	@Override
	protected ClassLoader getLoader() {
		return getClass().getClassLoader(); // NOPMD by Luthiger
	}

	@Override
	protected String getBaseName() {
		return BASE_NAME;
	}

}
