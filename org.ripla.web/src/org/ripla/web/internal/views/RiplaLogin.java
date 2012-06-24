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

package org.ripla.web.internal.views;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.Activator;
import org.ripla.web.RiplaApplication;
import org.ripla.web.exceptions.LoginException;
import org.ripla.web.interfaces.IAppConfiguration;
import org.ripla.web.interfaces.IAuthenticator;
import org.ripla.web.interfaces.IMessages;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * The application's login view.<br />
 * Subclasses may override <code>Button.ClickListener {@link #getListener(Form inForm, IAuthenticator inAuthenticator, RiplaApplication inApplication, UserAdmin inUserAdmin, Window inWindow)}</code>.
 *
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaLogin extends CustomComponent {	
	private Button loginButton;
	private Form form;

	private IAuthenticator authenticator;
	private RiplaApplication application;
	private UserAdmin userAdmin;


	/**
	 * RiplaLogin constructor.
	 * 
	 * @param inConfiguration {@link IAppConfiguration} the application's configuration
	 * @param inApplication {@link RiplaApplication} the application instance
	 * @param inUserAdmin {@link UserAdmin} the application's user administration
	 */
	public RiplaLogin(IAppConfiguration inConfiguration, RiplaApplication inApplication, UserAdmin inUserAdmin) {
		authenticator = inConfiguration.getLoginAuthenticator();
		application = inApplication;
		userAdmin = inUserAdmin;
		
		VerticalLayout lLayout = new VerticalLayout();
		setCompositionRoot(lLayout);
		createForm(lLayout, inConfiguration, inApplication, inUserAdmin);
	}
	
	private void createForm(VerticalLayout inLayout, IAppConfiguration inConfiguration, RiplaApplication inApplication, final UserAdmin inUserAdmin) {
		IMessages lMessages = Activator.getMessages();
		form = new Form();
		form.setDescription(inConfiguration.getWelcome()); //$NON-NLS-1$
		form.setWidth(400, UNITS_PIXELS);
		form.setStyleName("ripla-login-form"); //$NON-NLS-1$
		
		loginButton = new Button(lMessages.getMessage("login.button")); //$NON-NLS-1$
		form.getFooter().addComponent(loginButton);
		
		Collection<String> lProperties = new ArrayList<String>();
		lProperties.add(LoginData.NAME_USERID);
		lProperties.add(LoginData.NAME_PASSWORD);		
		form.setFormFieldFactory(new LoginFieldFacory());
		form.setItemDataSource(new BeanItem<LoginData>(new LoginData()));
		form.setVisibleItemProperties(lProperties);
		form.setImmediate(false);

		inLayout.addComponent(form);
		inLayout.setComponentAlignment(form, Alignment.TOP_CENTER);
	}

	/**
	 * Creates the listener for the login form's submit button event.<br />
	 * The listener has to give access to authenticated users.<br />
	 * Subclasses may override.
	 * 
	 * @param inForm {@link Form} this login form
	 * @param inAuthenticator {@link IAuthenticator}
	 * @param inApplication {@link RiplaApplication}
	 * @param inUserAdmin {@link UserAdmin}
	 * @param inWindow {@link Window}
	 * @return {@link ClickListener}
	 */
	protected Button.ClickListener getListener(Form inForm, IAuthenticator inAuthenticator, RiplaApplication inApplication, UserAdmin inUserAdmin, Window inWindow) {
		return new LoginButtonListener(inForm, inAuthenticator, inApplication, inUserAdmin, inWindow);
	}
	

	/* (non-Javadoc)
	 * @see com.vaadin.ui.AbstractComponentContainer#attach()
	 */
	@Override
	public void attach() {
		loginButton.addListener(getListener(form, authenticator, application, userAdmin, getWindow()));
		loginButton.setClickShortcut(KeyCode.ENTER);
		form.focus();
	}

// ---
	
	private static class LoginButtonListener implements Button.ClickListener {
		private Form form;
		private IAuthenticator authenticator;
		private RiplaApplication application;
		private UserAdmin userAdmin;
		private Window window;

		protected LoginButtonListener(Form inForm, IAuthenticator inAuthenticator, RiplaApplication inApplication, UserAdmin inUserAdmin, Window inWindow) {
			form = inForm;
			authenticator = inAuthenticator;
			application = inApplication;
			userAdmin = inUserAdmin;
			window = inWindow;
		}

		@Override
		public void buttonClick(ClickEvent inEvent) {
			try {
				Item lData = form.getItemDataSource();
				User lUser = authenticator.authenticate(lData.getItemProperty(LoginData.NAME_USERID).getValue().toString(), 
						lData.getItemProperty(LoginData.NAME_PASSWORD).getValue().toString(), 
						userAdmin);
				application.showAfterLogin(lUser);
			}
			catch (LoginException exc) {
				form.focus();
				window.showNotification(exc.getMessage(), Notification.TYPE_WARNING_MESSAGE);
			}
		}
	}

// ---
	
	public static class LoginData {
		static final String NAME_USERID = "userid"; //$NON-NLS-1$
		static final String NAME_PASSWORD = "password"; //$NON-NLS-1$
		
		private String userid=""; //$NON-NLS-1$
		private String password=""; //$NON-NLS-1$
		
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getUserid() {
			return userid;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPassword() {
			return password;
		}
	}
	
	private class LoginFieldFacory implements FormFieldFactory {
		
		public Field createField(Item item, Object propertyId, Component uiContext) {
			IMessages lMessages = Activator.getMessages();
			Field lField = null;
			if (LoginData.NAME_USERID.equals(propertyId)) { //$NON-NLS-1$
				lField = new TextField(String.format("%s:", lMessages.getMessage("login.field.user"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (LoginData.NAME_PASSWORD.equals(propertyId)) { //$NON-NLS-1$
				lField = new PasswordField(String.format("%s:", lMessages.getMessage("login.field.pass"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
			lField.setRequired(false);
			return lField;
		}
	}

}
