/*******************************************************************************
* Copyright (c) 2012 RelationWare, Benno Luthiger
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* RelationWare, Benno Luthiger
******************************************************************************/
package org.ripla.web.util;

import java.util.Map;

import org.ripla.web.Constants;
import org.ripla.web.exceptions.NoControllerFoundException;
import org.ripla.web.interfaces.IBodyComponent;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.services.UseCaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.ParameterHandler;

/**
 * Parameter handler to handle request parameters:<pre>
 * http://my.host.org/ripla?request=parameter</pre>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RequestHandler implements ParameterHandler {
	private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);
	
	private static final String TMPL_REQUEST_URL = "%s?%s=%s&%s=%s"; //$NON-NLS-1$
	
	private IRequestParameter requestParameter;

	private String requestURL;
	private UseCaseManager useCaseManager;


	/**
	 * RequestHandler constructor.
	 * 
	 * @param inRequestURL String the application's request url
	 * @param inUseCaseManager {@link UseCaseManager}
	 */
	public RequestHandler(String inRequestURL, UseCaseManager inUseCaseManager) {
		requestURL = inRequestURL;
		useCaseManager = inUseCaseManager;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
	 */
	@Override
	public void handleParameters(Map<String, String[]> inParameters) {
		if (inParameters.containsKey(Constants.KEY_REQUEST_PARAMETER)) {
			requestParameter = createRequestParameter(inParameters.get(Constants.KEY_REQUEST_PARAMETER)[0]);
			requestParameter.handleParameters(inParameters);
			LOG.trace("Handling request parameter '{}'.", requestParameter);
		}
	}

	/**
	 * Displays the requested view in the main view.
	 * 
	 * @param inBody {@link IBodyComponent}
	 * @return boolean <code>true</code> if the parameter request has been processed successfully, 
	 * <code>false</code> if no parameter request provided.
	 */
	public boolean process(IBodyComponent inBody) {
		if (requestParameter == null) return false;
		
		try {
			String lControllerName = requestParameter.getControllerName();
			requestParameter = null;
			inBody.setContentView(useCaseManager.getControllerManager().getContent(lControllerName));
			return true;
		} 
		catch (NoControllerFoundException exc) {
			// intentionally left empty
		}
		return false;
	}
	
	/**
	 * Creates the URL to the view of the specified task, e.g.
	 * <code>http://localhost:8084/forum?request=org.hip.vif.forum.groups/org.hip.vif.groups.tasks.RequestsListTask&key=value</code>. 
	 * 
	 * @param inTask {@link IPluggableTask} the task that is called in the request
	 * @param inIsForum boolean <code>true</code> if the requested url should call the forum application, <code>false</code> for the admin application
	 * @param inKey String the additional parameter's key
	 * @param inValue Long the additional parameter's value
	 * @return the bookmarkable URL to the view of the specified task 
	 */
	public String createRequestedURL(Class<? extends IPluggable> inTask, boolean inIsForum, String inKey, Long inValue) {		
		return String.format(TMPL_REQUEST_URL, getRequestURL(), 
				Constants.KEY_REQUEST_PARAMETER,
				UseCaseHelper.createFullyQualifiedControllerName(inTask),
				inKey, inValue.toString());
	}
	
	/**
	 * @return String e.g. <code>http://localhost:8084/forum</code>
	 */
	public  String getRequestURL() {
		return requestURL;
	}
	
	/**
	 * Factory method, subclasses may override.
	 * @param inControllerName 
	 * 
	 * @return {@link IRequestParameter}
	 */
	protected IRequestParameter createRequestParameter(String inControllerName) {
		return new DftRequestParameter(inControllerName);
	}
	
// ---
	
	/**
	 * Interface for classes wrapping the servlet's request parameter.
	 * 
	 * @author Luthiger
	 */
	public static interface IRequestParameter {
		/**
		 * Do something with the parameters passed in the servlet request, e.g. store the locally.
		 * 
		 * @param inParameters Map&lt;String, String[]> the parameter map containing the keys and values
		 */
		void handleParameters(Map<String, String[]> inParameters);
		
		/**
		 * Returns the name of the controller the request is calling.
		 * 
		 * @return String controller name
		 */
		String getControllerName();
	}
	
	protected static class DftRequestParameter implements IRequestParameter {
		private String controllerName;

		public DftRequestParameter(String inControllerName) {
			controllerName = inControllerName;
		}

		/* (non-Javadoc)
		 * @see org.ripla.web.util.RequestHandler.IRequestParameter#handleParameters(java.util.Map)
		 */
		@Override
		public void handleParameters(Map<String, String[]> inParameters) {
			// do nothing
		}

		/* (non-Javadoc)
		 * @see org.ripla.web.util.RequestHandler.IRequestParameter#getControllerName()
		 */
		@Override
		public String getControllerName() {
			return controllerName;
		}
	}
	
}
