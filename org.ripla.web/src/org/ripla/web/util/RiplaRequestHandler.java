/*******************************************************************************
 * Copyright (c) 2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.web.util;

import java.io.IOException;

import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.web.Constants;
import org.ripla.web.interfaces.IBodyComponent;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.services.UseCaseRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;

/**
 * Request handler to handle request parameters:
 * 
 * <pre>
 * http://my.host.org/ripla?request=parameter
 * </pre>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaRequestHandler implements RequestHandler {
	private static final Logger LOG = LoggerFactory
			.getLogger(RiplaRequestHandler.class);

	private static final String TMPL_REQUEST_URL = "%s?%s=%s&%s=%s"; //$NON-NLS-1$

	private IRequestParameter requestParameter;

	private final String requestURL;

	/**
	 * RequestHandler constructor.
	 */
	public RiplaRequestHandler() {
		requestURL = VaadinServlet.getCurrent().getServletContext()
				.getContextPath()
				+ VaadinServletService.getCurrentServletRequest()
						.getServletPath();
	}

	@Override
	public boolean handleRequest(final VaadinSession inSession,
			final VaadinRequest inRequest, final VaadinResponse inResponse)
			throws IOException {
		final String lParameter = inRequest
				.getParameter(Constants.KEY_REQUEST_PARAMETER);
		if (lParameter != null) {
			requestParameter = createRequestParameter(lParameter);
			requestParameter.handleParameters(inSession, inRequest, inResponse);
			LOG.trace("Handling request parameter '{}'.", lParameter);
		}
		return false;
	}

	/**
	 * Displays the requested view in the main view.
	 * 
	 * @param inBody
	 *            {@link IBodyComponent}
	 * @return boolean <code>true</code> if the parameter request has been
	 *         processed successfully, <code>false</code> if no parameter
	 *         request provided.
	 */
	public boolean process(final IBodyComponent inBody) {
		if (requestParameter == null) {
			return false;
		}

		try {
			final String lControllerName = requestParameter.getControllerName();
			requestParameter = null; // NOPMD by Luthiger on 10.09.12 00:21
			inBody.setContentView(UseCaseRegistry.INSTANCE
					.getControllerManager().getContent(lControllerName));
			return true;
		}
		catch (final NoControllerFoundException exc) { // NOPMD by Luthiger
			// intentionally left empty
		}
		return false;
	}

	/**
	 * Creates the URL to the view of the specified task, e.g.
	 * <code>http://localhost:8084/demo?request=org.ripla.web.demo.config/org.ripla.web.demo.config.controller.SkinSelectController&key=value</code>
	 * .
	 * 
	 * @param inTask
	 *            {@link IPluggableTask} the task that is called in the request
	 * @param inIsForum
	 *            boolean <code>true</code> if the requested url should call the
	 *            forum application, <code>false</code> for the admin
	 *            application
	 * @param inKey
	 *            String the additional parameter's key
	 * @param inValue
	 *            Long the additional parameter's value
	 * @return the bookmarkable URL to the view of the specified task
	 */
	public String createRequestedURL(final Class<? extends IPluggable> inTask,
			final boolean inIsForum, final String inKey, final Long inValue) {
		return String.format(TMPL_REQUEST_URL, getRequestURL(),
				Constants.KEY_REQUEST_PARAMETER,
				UseCaseHelper.createFullyQualifiedControllerName(inTask),
				inKey, inValue.toString());
	}

	/**
	 * @return String e.g. <code>http://localhost:8084/demo</code>
	 */
	public String getRequestURL() {
		return requestURL;
	}

	/**
	 * Factory method, subclasses may override.
	 * <p>
	 * This implementation returns an instance of {@link DftRequestParameter}.
	 * </p>
	 * 
	 * @param inControllerName
	 *            String we assume as parameter the name of the controller to
	 *            display the view in the main window.
	 * 
	 * @return {@link IRequestParameter}
	 */
	protected IRequestParameter createRequestParameter(
			final String inControllerName) {
		return new DftRequestParameter(inControllerName);
	}

	// ---

	/**
	 * Interface for classes wrapping the servlet's request parameter.
	 * 
	 * @author Luthiger
	 */
	public interface IRequestParameter {
		/**
		 * Do something with the parameters passed in the servlet request, e.g.
		 * store them locally.
		 * 
		 * @param inSession
		 *            {@link VaadinSession}
		 * @param inRequest
		 *            {@link VaadinRequest}
		 * @param inResponse
		 *            {@link VaadinResponse}
		 */
		void handleParameters(final VaadinSession inSession,
				final VaadinRequest inRequest, final VaadinResponse inResponse);

		/**
		 * Returns the name of the controller the request is calling.
		 * 
		 * @return String controller name
		 */
		String getControllerName();
	}

	protected static class DftRequestParameter implements IRequestParameter {
		private final String controllerName;

		public DftRequestParameter(final String inControllerName) {
			controllerName = inControllerName;
		}

		@Override
		public void handleParameters(final VaadinSession inSession,
				final VaadinRequest inRequest, final VaadinResponse inResponse) {
			// do nothing
		}

		@Override
		public String getControllerName() {
			return controllerName;
		}
	}

}
