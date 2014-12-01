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
import org.ripla.web.internal.services.UseCaseRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;

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

	@Override
	public boolean handleRequest(final VaadinSession inSession,
			final VaadinRequest inRequest, final VaadinResponse inResponse)
			throws IOException {
		final String lParameter = inRequest
				.getParameter(Constants.KEY_REQUEST_PARAMETER);
		if (lParameter != null) {
			createRequestParameter(lParameter).handleParameters(inSession,
					inRequest, inResponse);
			LOG.trace("Handling request parameter '{}'.", lParameter);
		}
		return false;
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

	/**
	 * @return the {@link IRequestParameter} instance stored in the session, may
	 *         be <code>null</code>
	 */
	public static IRequestParameter getParameterFromSession() {
		IRequestParameter out = null;
		try {
			VaadinSession.getCurrent().getLockInstance().lock();
			out = VaadinSession.getCurrent().getAttribute(
					IRequestParameter.class);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
		return out;
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

		/**
		 * We let the parameter instance do the processing.
		 * 
		 * @param inBody
		 *            {@link IBodyComponent}
		 * @return boolean <code>true</code> if the parameter has been
		 *         successfully able to process the request, else, the request
		 *         handling should be done by the application (e.g. by calling
		 *         the default view).
		 */
		boolean process(final IBodyComponent inBody);
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

		/**
		 * Subclasses may call this method from
		 * <code>IRequestParameter.handleParameters()</code>.
		 * 
		 * @param inSession
		 *            {@link VaadinSession}
		 */
		protected void setParameterToSession(VaadinSession inSession) {
			try {
				inSession.getLockInstance().lock();
				inSession.setAttribute(IRequestParameter.class, this);
			} finally {
				inSession.getLockInstance().unlock();
			}
		}

		@Override
		public String getControllerName() {
			return controllerName;
		}

		/**
		 * Subclasses may call this method form
		 * <code>IRequestParameter.process()</code>, e.g.
		 * <code>inBody.setContentView(getComponent(getControllerName()));</code>
		 * .
		 * 
		 * @param inControllerName
		 *            String
		 * @return {@link Component} the component created using the passed
		 *         controller, may be <code>null</code>
		 */
		protected Component getComponent(String inControllerName) {
			try {
				return UseCaseRegistry.INSTANCE.getControllerManager()
						.getContent(inControllerName);
			} catch (final NoControllerFoundException exc) {
				// intentionally left empty
			}
			return null;
		}

		@Override
		public boolean process(IBodyComponent inBody) {
			// do nothing
			return false;
		}
	}

}
