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

package org.ripla.useradmin.admin;

import java.util.ArrayList;
import java.util.Vector;

import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;

/**
 * <p>The <tt>Authorization</tt> interface encapsulates an authorization context on which bundles
 * can base authorization decisions, where appropriate.
 * </p><p>
 * Bundles associate the privilege to access restricted resources or
 * operations with roles. Before granting access to a restricted resource
 * or operation, a bundle will check if the <tt>Authorization</tt> object passed
 * to it possesses the required role, by calling its <tt>hasRole</tt> method.
 * </p>
 * <p>
 * Authorization contexts are instantiated by calling the 
 * {@link RiplaUserAdmin.#getAuthorization(org.osgi.service.useradmin.User)} method.</p>
 * <p><i>Trusting Authorization objects</i>
 * </p><p>There are no restrictions regarding the creation of <tt>Authorization</tt> objects.
 * Hence, a service must only accept <tt>Authorization</tt> objects from bundles that
 * has been authorized to use the service using code based (or Java 2)
 * permissions.
 *
 * </p><p>In some cases it is useful to use <tt>ServicePermission</tt> to do the code based
 * access control. A service basing user access control on <tt>Authorization</tt>
 * objects passed to it, will then require that a calling bundle has the
 * <tt>ServicePermission</tt> to get the service in question. This is the most
 * convenient way. The OSGi environment will do the code based permission check
 * when the calling bundle attempts to get the service from the service
 * registry.
 * </p><p>
 * Example: A servlet using a service on a user's behalf. The bundle with the
 * servlet must be given the <tt>ServicePermission</tt> to get the Http Service.
 * </p><p>
 * However, in some cases the code based permission checks need to be more
 * fine-grained. A service might allow all bundles to get it, but
 * require certain code based permissions for some of its methods.
 * </p><p>
 * Example: A servlet using a service on a user's behalf, where some
 * service functionality is open to anyone, and some is restricted by code
 * based permissions. When a restricted method is called
 * (e.g., one handing over
 * an <tt>Authorization</tt> object), the service explicitly checks that the calling
 * bundle has permission to make the call.
 * </p>
 * <p>Authorization example:</p>
 * <pre>if (!userAdmin.getAuthorization(user).hasRole("my.role")) {
 *     throw new NotAuthorizedException("Not authorized to ...!");
 *}
 * </pre>
 * 
 * @author Luthiger
 */
public class RiplaAuthorization implements Authorization {

	private RiplaUserAdmin userAdmin;
	private Role user;
	private String name;

	/**
	 * @param inUser
	 * @param inRiplaUserAdmin
	 */
	public RiplaAuthorization(RiplaUser inUser, RiplaUserAdmin inUserAdmin) {
		userAdmin = inUserAdmin;
		if (inUser == null) {
			//anonymous user
			user = inUserAdmin.getRole(Role.USER_ANYONE);
			name = null;
		}
		else {
			user = inUser;
			name = user.getName();
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.Authorization#getName()
	 */
	@Override
	public String getName() {
		userAdmin.checkAlive();
		return name;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.Authorization#hasRole(java.lang.String)
	 */
	@Override
	public boolean hasRole(String inName) {
		userAdmin.checkAlive();
		synchronized (userAdmin) {
			RiplaRole lRole = (RiplaRole) userAdmin.getRole(inName);
			if (lRole == null) {
				return false;
			}
			return lRole.isImpliedBy(user, new ArrayList<String>());
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.Authorization#getRoles()
	 */
	@Override
	public String[] getRoles() {
		userAdmin.checkAlive();
		synchronized (userAdmin) {
			int lLength = userAdmin.roles.size();
			Vector<String> lResult = new Vector<String>(lLength);
			for (Role lRole : userAdmin.roles) {
				if (((RiplaRole)lRole).isImpliedBy(user, new ArrayList<String>())) {
					String lRoleName = lRole.getName();
					if (!lRoleName.equals(Role.USER_ANYONE)) {
						lResult.add(lRoleName);
					}
				}
			}
			int lSize = lResult.size();
			if (lSize == 0) {
				return null;
			}
			String[] out = new String[lSize];
			lResult.copyInto(out);
			return out;
		}
	}

}
