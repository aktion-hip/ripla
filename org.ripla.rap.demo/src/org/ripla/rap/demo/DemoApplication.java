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
package org.ripla.rap.demo;

import java.util.Dictionary;

import org.eclipse.rap.rwt.service.ResourceLoader;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.LoginException;
import org.ripla.interfaces.IAuthenticator;
import org.ripla.rap.app.RiplaApplication;
import org.ripla.rap.app.RiplaBase;
import org.ripla.rap.demo.exp.Constants;
import org.ripla.rap.interfaces.IRapConfiguration;
import org.ripla.rap.util.LoaderUtil;
import org.ripla.util.PreferencesHelper;

/**
 * The demo application using Ripla-RAP.
 * 
 * @author Luthiger
 */
public class DemoApplication extends RiplaApplication {

	@Override
	protected IRapConfiguration getAppConfiguration() {
		return new IRapConfiguration() {

			@Override
			public String getPath() {
				return Constants.APP_PATH;
			}

			@Override
			public Class<? extends RiplaBase> getEntryPointType() {
				return DemoEntryPoint.class;
			}

			@Override
			public IAuthenticator getLoginAuthenticator() {
				if (Boolean.parseBoolean(getPreferences().get(
						Constants.KEY_LOGIN, Boolean.FALSE.toString()))) {
					return new IAuthenticator() {
						@Override
						public User authenticate(final String inName,
								final String inPassword,
								final UserAdmin inUserAdmin)
								throws LoginException {
							final User outUser = inUserAdmin.getUser(
									Constants.KEY_USER, inName);
							if (outUser == null) {
								throw new LoginException(Activator
										.getMessages().getFormattedMessage(
												"login.failure.name", inName));
							}
							if (!outUser.hasCredential(Constants.KEY_PW,
									inPassword)) {
								throw new LoginException(Activator
										.getMessages().getMessage(
												"login.failure.credentials"));
							}
							return outUser;
						}
					};
				}
				return null;
			}

			@Override
			public String getWelcome() {
				return Activator.getMessages().getMessage("login.welcome");
			}

			@Override
			public String getDftSkinID() {
				return "org.ripla.rap.demo.skin";
			}

			@Override
			public String getAppName() {
				return "Ripla Demo Application";
			}

			@Override
			public ResourceLoader getFaviconLoader() {
				return LoaderUtil.getDftFavion();
			}

			@Override
			public String getMenuTagFilter() {
				return null;
			}
		};
	}

	@Override
	public void setUserAdmin(final UserAdmin inUserAdmin) {
		super.setUserAdmin(inUserAdmin);

		final User lAdmin = (User) inUserAdmin.createRole(
				Constants.USER_NAME_ADMIN, Role.USER);
		setNamePassword(lAdmin, Constants.USER_NAME_ADMIN,
				Constants.USER_PW_ADMIN);

		final User lUser = (User) inUserAdmin.createRole(
				Constants.USER_NAME_USER, Role.USER);
		setNamePassword(lUser, Constants.USER_NAME_USER, Constants.USER_PW_USER);

		final Group lAdministrators = (Group) inUserAdmin.createRole(
				Constants.ADMIN_GROUP_NAME, Role.GROUP);
		if (lAdministrators != null) {
			lAdministrators.addRequiredMember(lAdmin);
			lAdministrators.addMember(inUserAdmin.getRole(Role.USER_ANYONE));
		}
		initializePermissions();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setNamePassword(final User inUser, final String inName,
			final String inPass) {
		if (inUser == null) {
			return;
		}

		Dictionary lProperties = inUser.getProperties();
		lProperties.put(Constants.KEY_USER, inName);

		lProperties = inUser.getCredentials();
		lProperties.put(Constants.KEY_PW, inPass);
	}

	/**
	 * The service's activate method called when OSGi config admin is updated.
	 * 
	 * @param inContext
	 *            ComponentContext
	 * @throws ConfigurationException
	 */
	@SuppressWarnings("unchecked")
	public void activate(final ComponentContext inContext)
			throws ConfigurationException {
		final Dictionary<String, Object> lProperties = inContext
				.getProperties();
		if (lProperties != null) {
			setChecked(lProperties, Constants.KEY_CONFIG_SKIN,
					PreferencesHelper.KEY_SKIN);
			setChecked(lProperties, Constants.KEY_CONFIG_LANGUAGE,
					PreferencesHelper.KEY_LANGUAGE);
		}
	}

	private void setChecked(final Dictionary<String, Object> inProperties,
			final String inPropKey, final String inKey) {
		final Object lValue = inProperties.get(inPropKey);
		if (lValue != null) {
			getPreferences().set(inKey, (String) lValue);
		}
	}

}
