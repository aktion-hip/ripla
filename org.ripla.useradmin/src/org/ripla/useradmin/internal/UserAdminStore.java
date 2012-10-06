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

package org.ripla.useradmin.internal;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.ripla.useradmin.admin.RiplaGroup;
import org.ripla.useradmin.admin.RiplaRole;
import org.ripla.useradmin.admin.RiplaUser;
import org.ripla.useradmin.admin.RiplaUserAdmin;
import org.ripla.useradmin.interfaces.IUserAdminStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Store for roles, users and groups using the preferences service to persist
 * the data.
 * 
 * @author Luthiger
 */
public class UserAdminStore implements IUserAdminStore { // NOPMD by Luthiger on 07.09.12 00:32
	private static final Logger LOG = LoggerFactory
			.getLogger(UserAdminStore.class);

	private static final String PREFS_USER_ROOT = "userAdmin";
	private static final String PREFS_NODE_PROPERTIES = "properties";
	private static final String PREFS_NODE_CREDENTIALS = "credentials";
	private static final String PREFS_NODE_MEMBERS = "members";
	private static final String PREFS_NODE_TYPES = "types";
	private static final String STRING_TYPE = "type";
	private static final String STRING_BASIC = "basic";
	private static final String STRING_REQUIRED = "required";

	private transient PreferencesService preferences;
	private final transient RiplaUserAdmin userAdmin;
	private transient Preferences rootNode;

	public UserAdminStore(final PreferencesService inPreferences,
			final RiplaUserAdmin inUserAdmin) {
		preferences = inPreferences;
		userAdmin = inUserAdmin;
	}

	/**
	 * Initializes the store, i.e. loads the persisted roles.
	 * 
	 * @throws BackingStoreException
	 */
	@Override
	public void initialize() throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							rootNode = preferences
									.getUserPreferences(PREFS_USER_ROOT);
							loadRoles();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error during init().", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	// --- roles ---

	@Override
	public void addRole(final Role inRole) throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lNode = rootNode.node(inRole
									.getName());
							lNode.putInt(STRING_TYPE, inRole.getType());
							lNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while adding a role.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	@Override
	public void removeRole(final Role inRole) throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lNode = rootNode.node(inRole
									.getName());
							lNode.removeNode();
							rootNode.node("").flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while removing a role.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	// --- properties ---

	@Override
	public void clearProperties(final Role inRole) throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lPropertyNode = rootNode
									.node(inRole.getName() + "/"
											+ PREFS_NODE_PROPERTIES);
							lPropertyNode.clear();
							if (lPropertyNode.nodeExists(PREFS_NODE_TYPES)) {
								lPropertyNode.node(PREFS_NODE_TYPES)
										.removeNode();
							}
							lPropertyNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while clearing properties.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	@Override
	public void addProperty(final Role inRole, final String inKey,
			final Object inValue) throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lPropertyNode = rootNode
									.node(inRole.getName() + "/"
											+ PREFS_NODE_PROPERTIES);
							final Preferences lTypesNode = lPropertyNode
									.node(PREFS_NODE_TYPES);
							if (inValue instanceof String) {
								lPropertyNode.put(inKey, (String) inValue);
								lTypesNode.putBoolean(inKey, true);
							} else {
								lPropertyNode.putByteArray(inKey,
										(byte[]) inValue);
								lTypesNode.putBoolean(inKey, false);
							}
							lPropertyNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while adding property.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	@Override
	public void removeProperty(final Role inRole, final String inKey)
			throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lPropertyNode = rootNode
									.node(inRole.getName() + "/"
											+ PREFS_NODE_PROPERTIES);
							lPropertyNode.remove(inKey);
							if (lPropertyNode.nodeExists(PREFS_NODE_TYPES)) {
								lPropertyNode.node(PREFS_NODE_TYPES).remove(
										inKey);
							}
							lPropertyNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while removing property.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	// --- credentials ---

	@Override
	public void clearCredentials(final Role inRole)
			throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lCredentialNode = rootNode
									.node(inRole.getName() + "/"
											+ PREFS_NODE_CREDENTIALS);
							lCredentialNode.clear();
							if (lCredentialNode.nodeExists(PREFS_NODE_TYPES)) {
								lCredentialNode.node(PREFS_NODE_TYPES)
										.removeNode();
							}
							lCredentialNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while clearing credentials.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	@Override
	public void addCredential(final Role inRole, final String inKey,
			final Object inValue) throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lCredentialNode = rootNode
									.node(inRole.getName() + "/"
											+ PREFS_NODE_CREDENTIALS);
							final Preferences lTypesNode = lCredentialNode
									.node(PREFS_NODE_TYPES);
							if (inValue instanceof String) {
								lCredentialNode.put(inKey, (String) inValue);
								lTypesNode.putBoolean(inKey, true);
							} else {
								lCredentialNode.putByteArray(inKey,
										(byte[]) inValue);
								lTypesNode.putBoolean(inKey, false);
							}
							lCredentialNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while adding credential.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	@Override
	public void removeCredential(final Role inRole, final String inKey)
			throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lCredentialNode = rootNode
									.node(inRole.getName() + "/"
											+ PREFS_NODE_CREDENTIALS);
							lCredentialNode.remove(inKey);
							if (lCredentialNode.nodeExists(PREFS_NODE_TYPES)) {
								lCredentialNode.node(PREFS_NODE_TYPES).remove(
										inKey);
							}
							lCredentialNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while removing credential.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	// --- members ---

	@Override
	public void addMember(final Group inGroup, final Role inRole)
			throws BackingStoreException {
		addMember(inGroup, inRole, STRING_BASIC);
	}

	@Override
	public void addRequiredMember(final Group inGroup, final Role inRole)
			throws BackingStoreException {
		addMember(inGroup, inRole, STRING_REQUIRED);
	}

	protected void addMember(final Group inGroup, final Role inRole,
			final String inMemberType) throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lMemberNode = rootNode
									.node(inGroup.getName() + "/"
											+ PREFS_NODE_MEMBERS);
							lMemberNode.put(inRole.getName(), inMemberType);
							lMemberNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while adding member.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	@Override
	public void removeMember(final Group inGroup, final Role inRole)
			throws BackingStoreException {
		try {
			AccessController
					.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws BackingStoreException {
							final Preferences lMemberNode = rootNode
									.node(inGroup.getName() + "/"
											+ PREFS_NODE_MEMBERS);
							lMemberNode.remove(inRole.getName());
							lMemberNode.flush();
							return null;
						}
					});
		}
		catch (final PrivilegedActionException exc) {
			LOG.error("Error while removing member.", exc);
			throw (BackingStoreException) exc.getException();
		}
	}

	protected void loadRoles() throws BackingStoreException {
		synchronized (this) {
			createAnonymousRole();

			final String[] lChildren = rootNode.node("").childrenNames();
			for (int i = 0; i < lChildren.length; i++) {
				if (userAdmin.getRole(lChildren[i]) == null) {
					loadRole(rootNode.node(lChildren[i]), null);
				}
			}
		}
	}

	protected void loadRole(final Preferences inNode, Role inRole) // NOPMD by Luthiger on 07.09.12 00:32
			throws BackingStoreException {
		final int lType = inNode.getInt(STRING_TYPE, Integer.MIN_VALUE);
		if (lType == Integer.MIN_VALUE) {
			final String lErrorMsg = String.format(
					"Unable to load role \"%s\"!", inNode.name());
			LOG.error(lErrorMsg);
			throw new BackingStoreException(lErrorMsg);
		}

		if (inRole == null) {
			inRole = userAdmin.createRole(inNode.name(), lType, false);
		}
		final Preferences lPropertiesNode = inNode.node(PREFS_NODE_PROPERTIES);
		String[] lKeys = lPropertiesNode.keys();
		final AbstractUserAdminHashtable lProperties = (AbstractUserAdminHashtable) inRole
				.getProperties();

		// load properties
		Object lValue;
		final Preferences lTypesNode = lPropertiesNode.node(PREFS_NODE_TYPES);
		for (int i = 0; i < lKeys.length; i++) {
			if (lTypesNode.getBoolean(lKeys[i], true)) {
				lValue = lPropertiesNode.get(lKeys[i], null);
			} else {
				lValue = lPropertiesNode.getByteArray(lKeys[i], null);
			}
			lProperties.put(lKeys[i], lValue, inRole, false);
		}

		// load credentials
		if (lType == Role.GROUP || lType == Role.USER) {
			Object lCredentialValue;
			final Preferences lCredentialNode = inNode
					.node(PREFS_NODE_CREDENTIALS);
			final Preferences lCredentialTypesNode = lCredentialNode
					.node(STRING_TYPE);
			lKeys = lCredentialNode.keys();
			final CredentialsHashtable lCredentials = (CredentialsHashtable) ((RiplaUser) inRole)
					.getCredentials();
			for (int i = 0; i < lKeys.length; i++) {
				if (lCredentialTypesNode.getBoolean(lKeys[i], true)) {
					lCredentialValue = lCredentialNode.get(lKeys[i], null);
				} else {
					lCredentialValue = lCredentialNode.getByteArray(lKeys[i],
							null);
				}
				lCredentials.put(lKeys[i], lCredentialValue, inRole, false);
			}
		}

		// load group members
		if (lType == Role.GROUP) {
			final Preferences lMemberNode = inNode.node(PREFS_NODE_MEMBERS);
			lKeys = lMemberNode.keys();
			for (int i = 0; i < lKeys.length; i++) {
				lValue = lMemberNode.get(lKeys[i], null);
				Role lMember = userAdmin.getRole(lKeys[i]);
				if (lMember == null) {
					// then we have not loaded this one yet, so load it
					loadRole(rootNode.node(lKeys[i]), null);
					lMember = userAdmin.getRole(lKeys[i]);
				}
				if (lValue.equals(STRING_REQUIRED)) {
					((RiplaGroup) inRole).addRequiredMember(lMember, false);
				} else {
					((RiplaGroup) inRole).addMember(lMember, false);
				}
			}
		}
	}

	private void createAnonymousRole() throws BackingStoreException {
		RiplaRole lRole = null;
		if (!rootNode.nodeExists(Role.USER_ANYONE)) {
			// If the user.anyone role is not present, create it
			lRole = (RiplaRole) userAdmin.createRole(Role.USER_ANYONE,
					Role.ROLE, true);
		}
		/* modified to solve defect 95982 */
		if (lRole == null) {
			loadRole(rootNode.node(Role.USER_ANYONE), null);
		} else {
			loadRole(rootNode.node(Role.USER_ANYONE), lRole);
		}
	}

	@Override
	public void destroy() {
		try {
			rootNode.flush();
			rootNode = null; // NOPMD by Luthiger on 07.09.12 00:31
			preferences = null; // NOPMD by Luthiger on 07.09.12 00:31
		}
		catch (final BackingStoreException exc) {
			LOG.error("Error while destroying the user admin store!", exc);
		}
	}

}
