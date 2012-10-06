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

import org.eclipse.osgi.framework.eventmgr.CopyOnWriteIdentityMap;
import org.eclipse.osgi.framework.eventmgr.EventDispatcher;
import org.eclipse.osgi.framework.eventmgr.EventManager;
import org.eclipse.osgi.framework.eventmgr.ListenerQueue;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.UserAdmin;
import org.osgi.service.useradmin.UserAdminEvent;
import org.osgi.service.useradmin.UserAdminListener;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserAdminEventProducer is responsible for sending out UserAdminEvents to all
 * UserAdminListeners.
 * 
 * @author Luthiger
 */
public class UserAdminEventProducer extends ServiceTracker implements
		EventDispatcher {
	private static final Logger LOG = LoggerFactory
			.getLogger(UserAdminEventProducer.class);

	static protected final String USER_ADMIN_LISTENER_CLASS = "org.osgi.service.useradmin.UserAdminListener"; //$NON-NLS-1$

	protected transient ServiceReference<? extends UserAdmin> userAdmin;
	/** List of UserAdminListeners */
	protected transient CopyOnWriteIdentityMap<ServiceReference<UserAdmin>, ServiceReference<UserAdmin>> listeners;
	/** EventManager for event delivery. */
	protected transient EventManager eventManager;

	public UserAdminEventProducer(
			final ServiceReference<? extends UserAdmin> inUserAdminRef,
			final BundleContext inContext) {
		super(inContext, USER_ADMIN_LISTENER_CLASS, null);

		userAdmin = inUserAdminRef;

		final ThreadGroup eventGroup = new ThreadGroup("Ripla User Admin"); //$NON-NLS-1$ // NOPMD by Luthiger on 07.09.12 00:26
		eventGroup.setDaemon(true);

		eventManager = new EventManager(
				"UserAdmin Event Dispatcher", eventGroup); //$NON-NLS-1$
		listeners = new CopyOnWriteIdentityMap<ServiceReference<UserAdmin>, ServiceReference<UserAdmin>>();

		open();
	}

	@Override
	public void close() {
		super.close();
		listeners.clear();
		eventManager.close();
		userAdmin = null; // NOPMD by Luthiger on 07.09.12 00:25
	}

	public void generateEvent(final int inType, final Role inRole) {
		if (userAdmin != null) {
			final UserAdminEvent lEvent = new UserAdminEvent(userAdmin, inType,
					inRole);

			/* queue to hold set of listeners */
			final ListenerQueue<ServiceReference<UserAdmin>, ServiceReference<UserAdmin>, UserAdminEvent> queue = new ListenerQueue<ServiceReference<UserAdmin>, ServiceReference<UserAdmin>, UserAdminEvent>(
					eventManager);

			/* add set of UserAdminListeners to queue */
			queue.queueListeners(listeners.entrySet(), this);

			/* dispatch event to set of listeners */
			queue.dispatchEventAsynchronous(0, lEvent);
		}
	}

	/**
	 * A service is being added to the <tt>ServiceTracker</tt> object.
	 * 
	 * <p>
	 * This method is called before a service which matched the search
	 * parameters of the <tt>ServiceTracker</tt> object is added to it. This
	 * method should return the service object to be tracked for this
	 * <tt>ServiceReference</tt> object. The returned service object is stored
	 * in the <tt>ServiceTracker</tt> object and is available from the
	 * <tt>getService</tt> and <tt>getServices</tt> methods.
	 * 
	 * @param inReference
	 *            Reference to service being added to the
	 *            <tt>ServiceTracker</tt> object.
	 * @return The service object to be tracked for the
	 *         <tt>ServiceReference</tt> object or <tt>null</tt> if the
	 *         <tt>ServiceReference</tt> object should not be tracked.
	 */
	public UserAdmin addingService(final ServiceReference<UserAdmin> inReference) {
		final UserAdmin outService = (UserAdmin) super
				.addingService(inReference);

		listeners.put(inReference, inReference);

		return outService;
	}

	/**
	 * A service tracked by the <tt>ServiceTracker</tt> object has been removed.
	 * 
	 * <p>
	 * This method is called after a service is no longer being tracked by the
	 * <tt>ServiceTracker</tt> object.
	 * 
	 * @param inReference
	 *            Reference to service that has been removed.
	 * @param inService
	 *            The service object for the removed service.
	 */
	@Override
	public void removedService(final ServiceReference inReference,
			final Object inService) {
		listeners.remove(inService);

		super.removedService(inReference, inService);
	}

	/**
	 * This method is the call back that is called once for each listener. This
	 * method must cast the EventListener object to the appropriate listener
	 * class for the event type and call the appropriate listener method.
	 * 
	 * @param inListener
	 *            This listener must be cast to the appropriate listener class
	 *            for the events created by this source and the appropriate
	 *            listener method must then be called.
	 * @param inListenerObject
	 *            This is the optional object that was passed to
	 *            ListenerList.addListener when the listener was added to the
	 *            ListenerList.
	 * @param inEventAction
	 *            This value was passed to the EventQueue object via one of its
	 *            dispatchEvent* method calls. It can provide information (such
	 *            as which listener method to call) so that this method can
	 *            complete the delivery of the event to the listener.
	 * @param inEventObject
	 *            This object was passed to the EventQueue object via one of its
	 *            dispatchEvent* method calls. This object was created by the
	 *            event source and is passed to this method. It should contain
	 *            all the necessary information (such as what event object to
	 *            pass) so that this method can complete the delivery of the
	 *            event to the listener.
	 */
	@Override
	public void dispatchEvent(final Object inListener,
			final Object inListenerObject, final int inEventAction,
			final Object inEventObject) {
		if (userAdmin == null) {
			return;
		}

		final UserAdminListener lUserAdminListener = (UserAdminListener) inListener;
		try {
			lUserAdminListener.roleChanged((UserAdminEvent) inEventObject);
		}
		catch (final Throwable t) { // NOPMD by Luthiger on 07.09.12 00:25
			LOG.error("Error encountered while dispatching the event!", t);
		}
	}

}