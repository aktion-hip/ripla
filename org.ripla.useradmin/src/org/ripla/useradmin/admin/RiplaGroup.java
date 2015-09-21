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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;

/**
 * <p>
 * A named grouping of roles (<tt>Role</tt> objects).
 * </p>
 * <p>
 * Whether or not a given <tt>Authorization</tt> context implies a
 * <tt>Group</tt> object depends on the members of that <tt>Group</tt> object.
 * </p>
 * <p>
 * A <tt>Group</tt> object can have two kinds of members: <i>basic</i> and
 * <i>required</i>. A <tt>Group</tt> object is implied by an
 * <tt>Authorization</tt> context if all of its required members are implied and
 * at least one of its basic members is implied.
 * </p>
 * <p>
 * A <tt>Group</tt> object must contain at least one basic member in order to be
 * implied. In other words, a <tt>Group</tt> object without any basic member
 * roles is never implied by any <tt>Authorization</tt> context.
 * </p>
 * <p>
 * A <tt>User</tt> object always implies itself.
 * </p>
 * <p>
 * No loop detection is performed when adding members to <tt>Group</tt> objects,
 * which means that it is possible to create circular implications. Loop
 * detection is instead done when roles are checked. The semantics is that if a
 * role depends on itself (i.e., there is an implication loop), the role is not
 * implied.
 * </p>
 * <p>
 * The rule that a <tt>Group</tt> object must have at least one basic member to
 * be implied is motivated by the following example:
 * </p>
 * 
 * <pre>
 * group foo
 *   required members: marketing
 *   basic members: alice, bob
 * </pre>
 * 
 * Privileged operations that require membership in "foo" can be performed only
 * by "alice" and "bob", who are in marketing.
 * <p>
 * If "alice" and "bob" ever transfer to a different department, anybody in
 * marketing will be able to assume the "foo" role, which certainly must be
 * prevented. Requiring that "foo" (or any <tt>Group</tt> object for that
 * matter) must have at least one basic member accomplishes that.
 * </p>
 * <p>
 * However, this would make it impossible for a <tt>Group</tt> object to be
 * implied by just its required members. An example where this implication might
 * be useful is the following declaration: "Any citizen who is an adult is
 * allowed to vote." An intuitive configuration of "voter" would be:
 * 
 * </p>
 * 
 * <pre>
 * group voter
 *   required members: citizen, adult
 *      basic members:
 * </pre>
 * 
 * <p>
 * However, according to the above rule, the "voter" role could never be assumed
 * by anybody, since it lacks any basic members. In order to address this issue
 * a predefined role named "user.anyone" can be specified, which is always
 * implied. The desired implication of the "voter" group can then be achieved by
 * specifying "user.anyone" as its basic member, as follows:
 * </p>
 * 
 * <pre>
 * group voter
 *   required members: citizen, adult
 *      basic members: user.anyone
 * </pre>
 * 
 * @author Luthiger
 */
public class RiplaGroup extends RiplaUser implements Group {

	private final transient RiplaUserAdmin userAdmin;

	private final transient Collection<Role> requiredMembers;
	private final transient Collection<Role> basicMembers;

	/**
	 * @param inName
	 * @param inType
	 */
	public RiplaGroup(final String inName, final RiplaUserAdmin inUserAdmin) {
		super(inName, inUserAdmin);
		userAdmin = inUserAdmin;
		requiredMembers = new ArrayList<Role>();
		basicMembers = new ArrayList<Role>();
	}

	@Override
	public boolean addMember(final Role inRole) {
		if (!checkPre(inRole)) {
			return false;
		}
		synchronized (userAdmin) {
			if (basicMembers.contains(inRole)) {
				return false;
			}
			return addMember(inRole, true);
		}
	}

	public boolean addMember(final Role inRole, final boolean inStore) {
		((RiplaRole) inRole).addImpliedRole(this);
		if (inStore) {
			try {
				userAdmin.getUserAdminStore().addMember(this, inRole);
			} catch (final BackingStoreException exc) {
				return false;
			}
		}
		basicMembers.add(inRole);
		return true;
	}

	@Override
	public boolean addRequiredMember(final Role inRole) {
		if (!checkPre(inRole)) {
			return false;
		}
		synchronized (userAdmin) {
			if (requiredMembers.contains(inRole)) {
				return false;
			}
			return addRequiredMember(inRole, true);
		}
	}

	public boolean addRequiredMember(final Role inRole, final boolean inStore) {
		((RiplaRole) inRole).addImpliedRole(this);
		if (inStore) {
			try {
				userAdmin.getUserAdminStore().addRequiredMember(this, inRole);
			} catch (final BackingStoreException exc) {
				return false;
			}
		}
		requiredMembers.add(inRole);
		return true;
	}

	@Override
	public boolean removeMember(final Role inRole) {
		if (!checkPre(inRole)) {
			return false;
		}
		synchronized (userAdmin) {
			try {
				userAdmin.getUserAdminStore().removeMember(this, inRole);
			} catch (final BackingStoreException exc) {
				return false;
			}
			((RiplaRole) inRole).removeImpliedRole(this);

			final boolean lRemoveBasic = basicMembers.remove(inRole);
			final boolean lRemoveRequired = requiredMembers.remove(inRole);
			return lRemoveBasic || lRemoveRequired;
		}
	}

	private boolean checkPre(final Role inRole) {
		userAdmin.checkAlive();
		userAdmin.checkAdminPermission();
		return inRole != null;
	}

	@Override
	public Role[] getMembers() {
		return getMemers(basicMembers);
	}

	@Override
	public Role[] getRequiredMembers() {
		return getMemers(requiredMembers);
	}

	private Role[] getMemers(final Collection<Role> inMembers) {
		userAdmin.checkAlive();
		synchronized (userAdmin) {
			if (inMembers.isEmpty()) {
				return null;
			}
			final Role[] outRoles = new Role[inMembers.size()];
			int i = 0; // NOPMD by Luthiger on 07.09.12 00:11
			for (final Role lRole : inMembers) {
				outRoles[i++] = lRole;
			}
			return outRoles;
		}
	}

	@Override
	public int getType() {
		userAdmin.checkAlive();
		return GROUP;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean isImpliedBy(final Role inRole, final List<String> inCheckLoop) {
		if (inCheckLoop.contains(getName())) {
			// we have a circular dependency
			return false;
		}
		if (getName().equals(inRole.getName())) // A User always implies itself.
												// A Group is a User.
		{
			return true;
		}
		inCheckLoop.add(getName());
		final ArrayList<String> lRequiredCheckLoop = (ArrayList<String>) ((ArrayList<String>) inCheckLoop).clone();
		final ArrayList<String> lBasicCheckLoop = (ArrayList<String>) ((ArrayList<String>) inCheckLoop).clone();

		// check to see if we imply all of the 0 or more required roles
		Iterator<Role> lRoles = requiredMembers.iterator();
		RiplaRole lRequiredRole;
		while (lRoles.hasNext()) {
			lRequiredRole = (RiplaRole) lRoles.next();
			if (!lRequiredRole.isImpliedBy(inRole, lRequiredCheckLoop)) {
				return false;
			}
		}
		// check to see if we imply any of the basic roles (there must be at
		// least one)
		lRoles = basicMembers.iterator();
		RiplaRole lBasicRole;
		while (lRoles.hasNext()) {
			lBasicRole = (RiplaRole) lRoles.next();
			if (lBasicRole.isImpliedBy(inRole, lBasicCheckLoop)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("Group: %s", getName());
	}

}
