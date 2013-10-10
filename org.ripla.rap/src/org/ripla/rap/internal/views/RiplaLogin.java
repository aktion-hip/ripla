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
package org.ripla.rap.internal.views;

import java.text.Normalizer.Form;

import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.LoginException;
import org.ripla.interfaces.IAppConfiguration;
import org.ripla.interfaces.IAuthenticator;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.Activator;
import org.ripla.rap.app.RiplaApplication;
import org.ripla.rap.interfaces.IBodyComponent;
import org.ripla.rap.util.AbstractRiplaForm;
import org.ripla.rap.util.AbstractRiplaView;
import org.ripla.rap.util.GridLayoutHelper;

/**
 * The application's login view.<br />
 * Subclasses may override
 * <code>Button.ClickListener {@link #getListener(Form inForm, IAuthenticator inAuthenticator, RiplaApplication inApplication, UserAdmin inUserAdmin, Window inWindow)}</code>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaLogin extends AbstractRiplaView {
	private static final int WIDTH = 400;

	private final IAuthenticator authenticator;
	private final UserAdmin userAdmin;

	/**
	 * RiplaLogin constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inConfiguration
	 *            {@link IAppConfiguration}
	 * @param inUserAdmin
	 *            {@link UserAdmin}
	 * @param inBody
	 *            {@link IBodyComponent}
	 */
	public RiplaLogin(final Composite inParent,
			final IAppConfiguration inConfiguration,
			final UserAdmin inUserAdmin, final IBodyComponent inBody) {
		super(inParent);
		setData(RWT.CUSTOM_VARIANT, "ripla-login-view");

		authenticator = inConfiguration.getLoginAuthenticator();
		userAdmin = inUserAdmin;

		final GridLayout lLayout = GridLayoutHelper.createGridLayout();
		lLayout.marginTop = 120;
		setLayout(lLayout);
		final GridData lData = new GridData(SWT.CENTER, SWT.TOP, true, false);
		lData.widthHint = WIDTH;
		setLayoutData(lData);

		final Label lTitle = createTitle(inConfiguration.getWelcome());
		lTitle.setLayoutData(new GridData(WIDTH, SWT.DEFAULT));

		final LoginForm lLoginForm = new LoginForm(this);
		lLoginForm.create();

		final IMessages lMessages = Activator.getMessages();
		final Button lLoginButton = lLoginForm
				.createFormProcessButton(lMessages.getMessage("login.button"));
		getShell().setDefaultButton(lLoginButton);
		lLoginButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				if (lLoginForm.checkStatus()) {
					try {
						final User lUser = authenticator.authenticate(
								lLoginForm.getUserId(),
								lLoginForm.getPassword(), userAdmin);
						inBody.showAfterLogin(lUser);
					} catch (final LoginException exc) {
						lLoginForm.setFeedback(lMessages
								.getMessage("login.failed"));
					}
				}
			}
		});

	}

	private static class LoginForm extends AbstractRiplaForm {
		private final static IMessages MESSAGES = Activator.getMessages();
		private static final int FIELD_WITDH = 170;
		private Label feedback;
		private Text user;
		private Text pass;

		protected LoginForm(final Composite inParent) {
			super(inParent, SWT.DEFAULT);
			setStyle("ripla-login-form");
		}

		@Override
		protected void createControls() {
			setNumColums(2);

			feedback = createLabel("", 2);

			createLabel(MESSAGES.getMessage("login.field.user"));
			user = createText("", 1, FIELD_WITDH);
			user.setFocus();

			createLabel(MESSAGES.getMessage("login.field.pass"));
			pass = createText("", 1, FIELD_WITDH, SWT.BORDER | SWT.SINGLE
					| SWT.PASSWORD);
		}

		public void setFeedback(final String inMessage) {
			feedback.setText(inMessage);
			user.setFocus();
		}

		public boolean checkStatus() {
			if (user.getText().trim().length() * pass.getText().trim().length() == 0) {
				feedback.setText(MESSAGES.getMessage("login.check.no"));
				user.setFocus();
				return false;
			}
			feedback.setText("");
			return true;
		}

		public String getUserId() {
			return user.getText();
		}

		public String getPassword() {
			return pass.getText();
		}

	}

}
