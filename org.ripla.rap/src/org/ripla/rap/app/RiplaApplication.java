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
package org.ripla.rap.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.interfaces.IAuthenticator;
import org.ripla.rap.Constants;
import org.ripla.rap.interfaces.IRapConfiguration;
import org.ripla.rap.internal.services.ConfigManager;
import org.ripla.rap.internal.services.PermissionHelper;
import org.ripla.rap.internal.services.SkinRegistry;
import org.ripla.rap.internal.services.UseCaseRegistry;
import org.ripla.rap.util.LoaderUtil;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.services.IPermissionEntry;
import org.ripla.util.PreferencesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The configuration of a Ripla-RAP application.<br />
 * This class is a singleton, i.e. it's instantiated only once during the
 * application's lifetime.
 * 
 * @author Luthiger
 */
public class RiplaApplication implements ApplicationConfiguration {
	private static final Logger LOG = LoggerFactory
			.getLogger(RiplaApplication.class);

	static final String AN_PREFS = "ra.preferences";
	static final String AN_CONFIG = "ra.config";
	static final String AN_APP_CONFIG = "ra.app.config";
	static final String AN_PERMISSION = "ra.permission";

	private static final String APP_NAME = "Ripla";

	private final PreferencesHelper preferences = createPreferencesHelper();
	private final ConfigManager configManager = new ConfigManager();
	private final PermissionHelper permissionHelper = new PermissionHelper();

	@Override
	public final void configure(final Application inApplication) {
		final IRapConfiguration lAppConfig = getAppConfiguration();

		final Map<String, String> lProperties = new HashMap<String, String>();
		lProperties.put(WebClient.PAGE_TITLE, lAppConfig.getAppName());
		lProperties.put(WebClient.FAVICON, Constants.FAVICON_PATH);
		lProperties
				.put(WebClient.BODY_HTML, LoaderUtil.readTextFromResource(
						"resources/body.html", "UTF-8"));

		inApplication.addEntryPoint(lAppConfig.getPath(),
				lAppConfig.getEntryPointType(), lProperties);
		inApplication.setOperationMode(OperationMode.SWT_COMPATIBILITY);
		inApplication.addResource(Constants.FAVICON_PATH,
				lAppConfig.getFaviconLoader());
		inApplication.addResource(Constants.IMG_LOADING,
				LoaderUtil.createResourceLoader(Constants.IMG_LOADING));

		// prepare skin management
		final String lSkinID = preferences.get(PreferencesHelper.KEY_SKIN,
				lAppConfig.getDftSkinID());
		SkinRegistry.INSTANCE.setPreferences(preferences);
		SkinRegistry.INSTANCE.setApplication(inApplication);
		SkinRegistry.INSTANCE.changeSkin(lSkinID);

		UseCaseRegistry.INSTANCE.registerContextMenus();

		// set attributes to application context
		inApplication.setAttribute(AN_PREFS, preferences);
		inApplication.setAttribute(AN_CONFIG, configManager);
		inApplication.setAttribute(AN_PERMISSION, permissionHelper);
		inApplication.setAttribute(AN_APP_CONFIG, lAppConfig);
	}

	/**
	 * Subclasses may override to provide their own
	 * <code>PreferencesHelper</code>.
	 * 
	 * @return PreferencesHelper
	 */
	protected PreferencesHelper createPreferencesHelper() {
		return new PreferencesHelper();
	}

	/**
	 * Returns the configuration object to configure the application.<br />
	 * Subclasses may override.
	 * 
	 * @return {@link IRapConfiguration} the configuration object.
	 */
	protected IRapConfiguration getAppConfiguration() {
		return new IRapConfiguration() {
			@Override
			public String getWelcome() {
				return null;
			}

			@Override
			public String getDftSkinID() {
				return Constants.DFT_SKIN_ID;
			}

			@Override
			public IAuthenticator getLoginAuthenticator() {
				return null;
			}

			@Override
			public String getAppName() {
				return APP_NAME;
			}

			@Override
			public String getPath() {
				return Constants.APP_PATH;
			}

			@Override
			public Class<? extends RiplaBase> getEntryPointType() {
				return RiplaBase.class;
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

	/**
	 * Allows access to the application's preferences.
	 * 
	 * @return {@link PreferencesHelper}
	 */
	public PreferencesHelper getPreferences() {
		return preferences;
	}

	/**
	 * <p>
	 * Subclasses that want to make use of the OSGi user admin service
	 * functionality may trigger initialization of registered permissions.
	 * </p>
	 * 
	 * <p>
	 * This will create a permission group for each registered
	 * <code>IPermissionEntry</code> and add the members defined in those
	 * permission instances.
	 * </p>
	 * 
	 * <p>
	 * This requires that such member instances exist already. Thus, subclasses
	 * have to prepare groups (i.e. roles) that can act as members for the
	 * registered permission groups before the registered permission instances
	 * are processed.
	 * </p>
	 * <p>
	 * This method is best called in the subclass's
	 * <code>setUserAdmin(UserAdmin inUserAdmin)</code> method:
	 * 
	 * <pre>
	 * public void setUserAdmin(UserAdmin inUserAdmin) {
	 * 	super.setUserAdmin(inUserAdmin);
	 * 	Group lAdministrators = (Group) inUserAdmin.createRole(&quot;ripla.admin&quot;,
	 * 			Role.GROUP);
	 * 	initializePermissions();
	 * }
	 * </pre>
	 * 
	 * </p>
	 */
	protected void initializePermissions() {
		permissionHelper.initializePermissions();
	}

	// --- OSGi DS bind and unbind methods ---

	public void setPreferences(final PreferencesService inPreferences) {
		preferences.setPreferences(inPreferences);
		LOG.debug("The OSGi preferences service is made available.");
	}

	public void unsetPreferences(final PreferencesService inPreferences) {
		preferences.dispose();
		LOG.debug("Removed the OSGi preferences service.");
	}

	public void setUserAdmin(final UserAdmin inUserAdmin) {
		UseCaseRegistry.INSTANCE.setUserAdmin(inUserAdmin);
		permissionHelper.setUserAdmin(inUserAdmin);
		LOG.debug("The OSGi user admin service is made available.");
	}

	public void unsetUserAdmin(final UserAdmin inUserAdmin) {
		UseCaseRegistry.INSTANCE.setUserAdmin(null);
		permissionHelper.setUserAdmin(null);
		LOG.debug("Removed the OSGi user admin service is made available.");
	}

	public void registerMenuContribution(
			final IExtendibleMenuContribution inMenuContribution) {
		LOG.debug("Registered extendible menu contribution '{}'.",
				inMenuContribution.getExtendibleMenuID());
		UseCaseRegistry.INSTANCE.registerMenuContribution(inMenuContribution);
	}

	public void unregisterMenuContribution(
			final IExtendibleMenuContribution inMenuContribution) {
		LOG.debug("Unregistered extendible menu contribution '{}'.",
				inMenuContribution.getExtendibleMenuID());
		UseCaseRegistry.INSTANCE.unregisterMenuContribution(inMenuContribution);
	}

	public void registerPermission(final IPermissionEntry inPermission) {
		LOG.debug("Registered permission '{}'.",
				inPermission.getPermissionName());
		permissionHelper.addPermission(inPermission);
	}

	public void unregisterPermission(final IPermissionEntry inPermission) {
		LOG.debug("Unregistered permission '{}'.",
				inPermission.getPermissionName());
		permissionHelper.removePermission(inPermission);
	}

	public void setConfiAdmin(final ConfigurationAdmin inConfigAdmin) {
		configManager.setConfigAdmin(inConfigAdmin);
	}

	public void unsetConfiAdmin(final ConfigurationAdmin inConfigAdmin) {
		configManager.clearConfigAdmin();
	}

	// ---

	/**
	 * Convenience method: Triggers a restart of the browser session.
	 */
	public static void restart() {
		execute(new String(getRestartJS(RWT.getRequest())));
	}

	/**
	 * Convenience method: Triggers a restart of the browser session after the
	 * specified delay.
	 * 
	 * @param inDelay
	 *            int delay in milliseconds
	 */
	public static void restart(final int inDelay) {
		final StringBuilder lScript = new StringBuilder(
				"setTimeout(function(){");
		lScript.append(getRestartJS(RWT.getRequest())).append("}, ")
				.append(String.valueOf(inDelay)).append(");");
		execute(new String(lScript));
	}

	private static StringBuilder getRestartJS(final HttpServletRequest inRequest) {
		final StringBuilder outScript = new StringBuilder(
				"parent.window.location.href=\"");
		outScript.append(inRequest.getContextPath());
		outScript.append(inRequest.getServletPath());
		outScript.append("\";");
		return outScript;
	}

	/**
	 * Convenience method: Executes the specified JavaScript.
	 * 
	 * @param inScript
	 *            String
	 */
	public static void execute(final String inScript) {
		final JavaScriptExecutor lExecutor = RWT.getClient().getService(
				JavaScriptExecutor.class);
		if (lExecutor != null) {
			lExecutor.execute(inScript);
		}
	}

}
