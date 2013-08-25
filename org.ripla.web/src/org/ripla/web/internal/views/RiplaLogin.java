/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.web.internal.views;

import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.LoginException;
import org.ripla.interfaces.IAppConfiguration;
import org.ripla.interfaces.IAuthenticator;
import org.ripla.interfaces.IMessages;
import org.ripla.web.Activator;
import org.ripla.web.RiplaApplication;
import org.ripla.web.util.LabelHelper;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * The application's login view.<br />
 * Subclasses may override
 * <code>Button.ClickListener {@link #getListener(RiplaLogin inLogin, IAuthenticator inAuthenticator, RiplaApplication inApplication, UserAdmin inUserAdmin)}</code>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaLogin extends CustomComponent {
	private static final String NAME_USERID = "userid"; //$NON-NLS-1$
	private static final String NAME_PASSWORD = "password"; //$NON-NLS-1$

	private final IAuthenticator authenticator;
	private final RiplaApplication application;
	private final UserAdmin userAdmin;

	private static TextField userid;
	private static PasswordField password;
	private Button loginButton;

	private Item loginItem;

	/**
	 * RiplaLogin constructor.
	 * 
	 * @param inConfiguration
	 *            {@link IAppConfiguration} the application's configuration
	 * @param inApplication
	 *            {@link RiplaApplication} the application instance
	 * @param inUserAdmin
	 *            {@link UserAdmin} the application's user administration
	 */
	public RiplaLogin(final IAppConfiguration inConfiguration,
			final RiplaApplication inApplication, final UserAdmin inUserAdmin) {
		super();
		authenticator = inConfiguration.getLoginAuthenticator();
		application = inApplication;
		userAdmin = inUserAdmin;

		final VerticalLayout lLayout = new VerticalLayout();
		setCompositionRoot(lLayout);
		createForm(lLayout, inConfiguration);
	}

	private void createForm(final VerticalLayout inLayout,
			final IAppConfiguration inConfiguration) {
		final IMessages lMessages = Activator.getMessages();

		final FormLayout lLayout = new FormLayout();
		lLayout.setStyleName("ripla-login-form"); //$NON-NLS-1$
		lLayout.setWidth(400, Unit.PIXELS);
		inLayout.addComponent(lLayout);
		inLayout.setComponentAlignment(lLayout, Alignment.TOP_CENTER);

		lLayout.addComponent(LabelHelper.createLabel(
				inConfiguration.getWelcome(), "ripla-welcome"));

		userid = new TextField(String.format(
				"%s:", lMessages.getMessage("login.field.user"))); //$NON-NLS-1$ //$NON-NLS-2$
		lLayout.addComponent(userid);
		userid.focus();

		password = new PasswordField(String.format(
				"%s:", lMessages.getMessage("login.field.pass"))); //$NON-NLS-1$ //$NON-NLS-2$
		lLayout.addComponent(password);

		loginButton = new Button(lMessages.getMessage("login.button")); //$NON-NLS-1$
		lLayout.addComponent(loginButton);

		loginItem = createLoginItem();
		final FieldGroup lBinding = new FieldGroup(loginItem);
		lBinding.bindMemberFields(this);
		lBinding.setBuffered(false);
	}

	/**
	 * Creates the listener for the login form's submit button event.<br />
	 * The listener has to give access to authenticated users.<br />
	 * Subclasses may override.
	 * <p>
	 * This implementation return an instance of {@link LoginButtonListener}.
	 * </p>
	 * 
	 * @param inAuthenticator
	 *            {@link IAuthenticator}
	 * @param inApplication
	 *            {@link RiplaApplication}
	 * @param inUserAdmin
	 *            {@link UserAdmin}
	 * @return {@link ClickListener}
	 */
	protected Button.ClickListener getListener(
			final IAuthenticator inAuthenticator,
			final RiplaApplication inApplication, final UserAdmin inUserAdmin) {
		return new LoginButtonListener(this, inAuthenticator, inApplication,
				inUserAdmin);
	}

	/**
	 * Notifies all contained components that the container is attached to a
	 * window.
	 * <p>
	 * This implementation adds the {@link LoginButtonListener} to the login
	 * button and sets the input focus on the login form.
	 * </p>
	 */
	@Override
	public void attach() {
		super.attach();
		loginButton.addClickListener(getListener(authenticator, application,
				userAdmin));
		loginButton.setClickShortcut(KeyCode.ENTER);
		userid.focus();
	}

	private Item createLoginItem() {
		final PropertysetItem outProperties = new PropertysetItem();
		outProperties.addItemProperty(NAME_USERID, new ObjectProperty<String>(
				""));
		outProperties.addItemProperty(NAME_PASSWORD,
				new ObjectProperty<String>(""));
		return outProperties;
	}

	protected Item getItem() {
		return loginItem;
	}

	// ---

	private static class LoginButtonListener implements Button.ClickListener {
		private final RiplaLogin login;
		private final IAuthenticator authenticator;
		private final RiplaApplication application;
		private final UserAdmin userAdmin;

		protected LoginButtonListener(final RiplaLogin inLogin,
				final IAuthenticator inAuthenticator,
				final RiplaApplication inApplication,
				final UserAdmin inUserAdmin) {
			login = inLogin;
			authenticator = inAuthenticator;
			application = inApplication;
			userAdmin = inUserAdmin;
		}

		@Override
		public void buttonClick(final ClickEvent inEvent) {
			try {
				final Item lData = login.getItem();
				final User lUser = authenticator.authenticate(lData
						.getItemProperty(RiplaLogin.NAME_USERID).getValue()
						.toString(),
						lData.getItemProperty(RiplaLogin.NAME_PASSWORD)
								.getValue().toString(), userAdmin);
				application.showAfterLogin(lUser);
			}
			catch (final LoginException exc) {
				Notification.show(exc.getMessage(),
						Notification.Type.WARNING_MESSAGE);
			}
		}
	}

}
