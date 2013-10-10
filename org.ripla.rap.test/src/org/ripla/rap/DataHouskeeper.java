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

package org.ripla.rap;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.client.ClientSelector;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointRegistration;
import org.eclipse.rap.rwt.internal.lifecycle.IUIThreadHolder;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rap.rwt.internal.protocol.ClientMessage;
import org.eclipse.rap.rwt.internal.protocol.ProtocolUtil;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.service.ServiceContext;
import org.eclipse.rap.rwt.internal.service.ServiceStore;
import org.eclipse.rap.rwt.internal.service.UISessionBuilder;
import org.eclipse.rap.rwt.internal.service.UISessionImpl;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.swt.internal.widgets.DisplaysHolder;
import org.ripla.rap.ThemeManagerHelper.TestThemeManager;

/**
 * 
 * @author Luthiger
 */
@SuppressWarnings("restriction")
public class DataHouskeeper {

	private static TestServletContext servletContext;

	public static ServiceContext createServiceContext() {
		ThemeManagerHelper.resetThemeManager();

		final TestRequest request = new TestRequest();
		request.setBody("createEmptyMessage()");
		final TestResponse response = new TestResponse();

		servletContext = new TestServletContext();
		final HttpSession session = createTestSession();
		request.setSession(session);

		return createNewServiceContext(request, response, createAppContext());
	}

	private static ApplicationContextImpl createAppContext() {
		final ApplicationContextImpl outContext = mock(ApplicationContextImpl.class);
		when(outContext.isActive()).thenReturn(true);
		//
		final EntryPointRegistration registration = mock(EntryPointRegistration.class);
		final EntryPointManager epManager = mock(EntryPointManager.class);
		when(epManager.getRegistrationByPath(anyString())).thenReturn(
				registration);
		when(outContext.getEntryPointManager()).thenReturn(epManager);
		//
		final ClientSelector clientSelector = mock(ClientSelector.class);
		when(outContext.getClientSelector()).thenReturn(clientSelector);
		//
		when(outContext.getDisplaysHolder()).thenReturn(new DisplaysHolder());
		//
		final ThemeManager themeManager = new TestThemeManager();
		themeManager.initialize();
		themeManager.activate();
		when(outContext.getThemeManager()).thenReturn(themeManager);

		return outContext;
	}

	private static ServiceContext createNewServiceContext(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final ApplicationContextImpl inApplicationContext) {
		ContextProvider.disposeContext();
		final ServiceContext outServiceContext = new ServiceContext(request,
				response, inApplicationContext);
		outServiceContext.setServiceStore(new ServiceStore());
		ContextProvider.setContext(outServiceContext);
		ensureUISession(outServiceContext);
		return outServiceContext;
	}

	private static TestSession createTestSession() {
		final TestSession result = new TestSession();
		if (servletContext != null) {
			result.setServletContext(servletContext);
		}
		return result;
	}

	private static void ensureUISession(final ServiceContext serviceContext) {
		final HttpSession httpSession = serviceContext.getRequest().getSession(
				true);
		UISessionImpl uiSession = UISessionImpl.getInstanceFromSession(
				httpSession, null);
		if (uiSession == null) {
			uiSession = new UISessionBuilder(serviceContext).buildUISession();
			// mock IUIThreadHolder
			final ClientMessage clientMsg = mock(ClientMessage.class);
			when(clientMsg.getLastSetOperationFor(anyString(), anyString()))
					.thenReturn(null);
			ProtocolUtil.setClientMessage(clientMsg);

			final IUIThreadHolder threadHolder = mock(IUIThreadHolder.class);
			when(threadHolder.getThread()).thenReturn(Thread.currentThread());
			LifeCycleUtil.setUIThread(uiSession, threadHolder);
		}
		serviceContext.setUISession(uiSession);
	}

}
