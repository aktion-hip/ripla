package org.ripla.useradmin.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.osgi.service.useradmin.UserAdminEvent;
import org.osgi.service.useradmin.UserAdminPermission;
import org.ripla.useradmin.interfaces.IUserAdminStore;
import org.ripla.useradmin.internal.UserAdminEventProducer;
import org.ripla.useradmin.internal.UserAdminStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider of the OSGi <code>UserAdmin</code> service.
 * 
 * <p>
 * This interface is used to manage a database of named <tt>Role</tt> objects, which can
 *  be used for authentication and authorization purposes.
 *  </p>
 *  <p>This version of the User Admin service defines two types of <tt>Role</tt> objects: "User" and
 *  "Group". Each type of role is represented by an <tt>int</tt> constant and an
 *  interface. The range of positive integers is reserved for new types of
 *  roles that may be added in the future. When defining proprietary role
 *  types, negative constant values must be used.
 * 
 *  </p><p>Every role has a name and a type.
 * 
 *  </p><p>A {@link User} object can be configured with credentials (e.g., a password)
 *  and properties (e.g., a street address, phone number, etc.).
 *  </p><p>
 *  A {@link Group} object represents an aggregation of {@link User} and
 *  {@link Group} objects. 
 *  In other words, the members of a <tt>Group</tt> object are roles themselves.
 *  </p><p>
 *  Every User Admin service manages and maintains its own
 *  namespace of <tt>Role</tt> objects, in which each <tt>Role</tt> object has a unique name.
 * </p>
 * 
 * <p>This implementation of the <code>UserAdmin</code> service uses the OSGi preferences to persist
 * user and authorization information.</p>
 * <p>Subclasses may override {@link #createUserAdminStore()} to provide their own store for 
 * persisting user and authorization information.
 * </p>
 * 
 * @author Luthiger
 * @see http://eclipsesrc.appspot.com/jsrcs/org.eclipse.equinox.internal.useradmin.package.html
 */
public class RiplaUserAdmin implements UserAdmin {
	private static final Logger LOG = LoggerFactory.getLogger(RiplaUserAdmin.class);
	
	protected Collection<Role> roles;
	private Collection<User> users;
	
	protected boolean alive;
	private UserAdminPermission adminPermission;
	private IUserAdminStore userAdminStore;

	private PreferencesService preferences;
	private BundleContext context;
	private UserAdminEventProducer eventProducer;

	/**
	 * RiplaUserAdmin constructor.
	 * 
	 * @throws Exception
	 */
	public RiplaUserAdmin() throws Exception {
		roles = new ArrayList<Role>();
		users = new ArrayList<User>();
		alive = true;
		
		setUserAdminStore();
	}
	
	private void setUserAdminStore() throws BackingStoreException {
		if (userAdminStore == null && preferences != null) {			
			try {
				userAdminStore = createUserAdminStore();
				userAdminStore.initialize();
			} 
			catch (BackingStoreException exc) {
				LOG.error("Could not initialize the Ripla user admin store!", exc);
				throw exc;
			}
		}
	}

	/**
	 * Creates an instance of the class that is responsible for storing the entities managed by the user admin.<br />
	 * Subclasses may override to provide their own store.
	 * 
	 * @return {@link IUserAdminStore}
	 */
	protected IUserAdminStore createUserAdminStore() {
		return new UserAdminStore(preferences, this);
	}
	
	public IUserAdminStore getUserAdminStore() {
		return userAdminStore;
	}	
	
	/**
	 * Needed for internal uses.
	 * 
	 * @return {@link UserAdminEventProducer} this admin service's event producer
	 */
	public UserAdminEventProducer getEventProducer() {
		return eventProducer;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#createRole(java.lang.String, int)
	 */
	@Override
	public Role createRole(String inName, int inType) {
		checkAlive();
		checkAdminPermission();
		if (inName == null) {
			throw new IllegalArgumentException("The user name must not be null!");
		}
		if (inType != Role.GROUP  && inType != Role.USER) {
			throw new IllegalArgumentException("The type of the new role is illegal!");
		}
		
		//if the role already exists, return null
		if (getRole(inName) != null) {
			return null;
		}
		synchronized (this) {
			return createRole(inName, inType, true);
		}
	}
	
	/**
	 * Creates a Role object with the given name and of the given type. <br />
	 * Needed for internal uses.
	 * 
	 * @param inName String The <code>name</code> of the <code>Role</code> object to create.
	 * @param inType int The type of the <code>Role</code> object to create. Must be either a {@link Role.USER} type or {@link Role.GROUP} type. 
	 * @param inStore boolean <code>true</code> if the newly created <code>Role</code> instance has to be stored.
	 * @return {@link Role} The newly created <code>Role</code> object, or <code>null</code> if a role with the given name already exists. 
	 */
	public Role createRole(String inName, int inType, boolean inStore) {
		RiplaRole out = null;
		if (inType == Role.ROLE) {
			out = new RiplaRole(inName, this);
		}
		else if (inType == Role.USER) {
			out = new RiplaUser(inName, this);
		}
		else if (inType == Role.GROUP) {
			out = new RiplaGroup(inName, this);
		}
		else {
			return null;
		}
		
		if (inStore) {
			try {
				userAdminStore.addRole(out);
			} 
			catch (BackingStoreException exc) {
				return null;
			}
			if (eventProducer != null) {				
				eventProducer.generateEvent(UserAdminEvent.ROLE_CREATED, out);
			}
		}
		
		if (inType == Role.USER || inType == Role.GROUP) {
			users.add((RiplaUser) out);
		}
		roles.add(out);
		return out;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#removeRole(java.lang.String)
	 */
	@Override
	public boolean removeRole(String inName) {
		checkAlive();
		checkAdminPermission();
		if (inName.equals(Role.USER_ANYONE)) {
			//silently ignore
			return true;
		}
		synchronized (this) {
			RiplaRole lRole = (RiplaRole) getRole(inName);
			if (lRole != null) {
				try {
					userAdminStore.removeRole(lRole);
				}
				catch (BackingStoreException exc) {
					return false;
				}
				users.remove(lRole);
				roles.remove(lRole);
				lRole.destroy();
				if (eventProducer != null) {				
					eventProducer.generateEvent(UserAdminEvent.ROLE_REMOVED, lRole);
				}
				lRole = null;
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getRole(java.lang.String)
	 */
	@Override
	public Role getRole(String inName) {
		checkAlive();
		if (inName == null) {
			return null;
		}
		synchronized (this) {
			Iterator<Role> lRoles = roles.iterator();
			while (lRoles.hasNext()) {
				Role outRole = lRoles.next();
				if (outRole.getName().equals(inName)) {
					return outRole;
				}
			}
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getRoles(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Role[] getRoles(String inFilter) throws InvalidSyntaxException {
		checkAlive();
		Collection<Role> outRoles;
		synchronized (this) {
			if (inFilter == null) {
				outRoles = roles;
			}
			else {
				Filter lFilter = context.createFilter(inFilter);
				outRoles = new ArrayList<Role>();
				for (Role lRole : roles) {
					if (lFilter.match(lRole.getProperties())) {
						outRoles.add(lRole);
					}
				}
			}
			int lSize = outRoles.size();
			if (lSize == 0) {				
				return null;
			}
			Role[] out = new Role[lSize];
			int i = 0;
			for (Role lRole : outRoles) {
				out[i++] = lRole;
			}
			return out;
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getUser(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public User getUser(String inKey, String inValue) {
		checkAlive();
		if (inKey == null) {
			return null;
		}
		User lUser;
		User outUser = null;
		Dictionary lProperties;
		String lKeyValue;
		synchronized (this) {
			Iterator<User> lUsers = users.iterator();
			while (lUsers.hasNext()) {
				lUser = (User) lUsers.next();
				lProperties = lUser.getProperties();
				lKeyValue = (String) lProperties.get(inKey);
				if (lKeyValue != null && lKeyValue.equals(inValue)) {
					if (outUser != null) {
						return null;
					}
					outUser = lUser;
				}
			}
			return outUser;
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getAuthorization(org.osgi.service.useradmin.User)
	 */
	@Override
	public Authorization getAuthorization(User inUser) {
		checkAlive();
		return new RiplaAuthorization((RiplaUser) inUser, this);
	}
	
	/**
	 * Destroys this user admin instance and releases the resources.<br />
	 * Needed for internal uses.
	 */
	protected synchronized void destroy() {
		alive = false;
		eventProducer.close();
		userAdminStore.destroy();
	}
	
	/**
	 * Needed for internal uses.
	 */
	protected void checkAlive() {
		if (!alive) {
			throw new IllegalStateException("The user admin instance has gone out of operation!");
		}
	}
	
	/**
	 * Needed for internal uses.
	 */
	protected void checkAdminPermission() {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			if (adminPermission == null) {
				adminPermission = new UserAdminPermission(UserAdminPermission.ADMIN, null);
			}
			sm.checkPermission(adminPermission);
		}
	}
	
	/**
	 * Needed for internal uses.
	 * 
	 * @param inCredential String
	 */
	public void checkGetCredentialPermission(String inCredential) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new UserAdminPermission(inCredential, UserAdminPermission.GET_CREDENTIAL));
		}
	}

	/**
	 * Needed for internal uses.
	 * 
	 * @param inCredential String
	 */
	public void checkChangeCredentialPermission(String inCredential) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new UserAdminPermission(inCredential, UserAdminPermission.CHANGE_CREDENTIAL));
		}
	}

	/**
	 * Needed for internal uses.
	 * 
	 * @param inProperty String
	 */
	public void checkChangePropertyPermission(String inProperty) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new UserAdminPermission(inProperty, UserAdminPermission.CHANGE_PROPERTY));
		}
	}
	
	
// --- OSGi binding and activation methods --- 
	
	public void setPreferences(PreferencesService inPreferences) {
		preferences = inPreferences;
		try {
			setUserAdminStore();
		}
		catch (BackingStoreException exc) {
			// intentionally left empty
		}
	}
	
	public void unsetPreferences(PreferencesService inPreferences) {
		preferences = null;
	}
	
	@SuppressWarnings("unchecked")
	public void activate(ComponentContext inComponentContext, BundleContext inContext) {
		context = inContext;
		if (eventProducer == null) {
			eventProducer = new UserAdminEventProducer(inComponentContext.getServiceReference(), context);
		}
	}
	
	public void deactivate(BundleContext inContext) {
		context = null;
		eventProducer = null;
	}

}
