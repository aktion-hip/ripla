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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Dictionary;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 * @author Benno
 */
public class MockBundleContext implements BundleContext {
	public static final Properties DEFAULT_PROPERTIES = null;
//	public static final Properties DEFAULT_PROPERTIES = new DefaultBundleContextProperties();

	private Bundle bundle;

	private Properties properties;

	protected Set serviceListeners, bundleListeners;
	
	public MockBundleContext() {
		this(null, null);
	}
	public MockBundleContext(Bundle bundle) {
		this(bundle, null);
	}
	public MockBundleContext(Bundle bundle, Properties props) {
//		this.bundle = (bundle == null ? new MockBundle(this) : bundle);
		properties = new Properties(DEFAULT_PROPERTIES);
		if (props != null)
			properties.putAll(props);

		// make sure the order is preserved
		this.serviceListeners = new LinkedHashSet(2);
		this.bundleListeners = new LinkedHashSet(2);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String inKey) {
		return properties.getProperty(inKey);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getBundle()
	 */
	@Override
	public Bundle getBundle() {
		return bundle;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#installBundle(java.lang.String, java.io.InputStream)
	 */
	@Override
	public Bundle installBundle(String inLocation, InputStream inInput) throws BundleException {
		try {
			inInput.close();
		}
		catch (IOException ex) {
			throw new BundleException("cannot close stream", ex);
		}
		return installBundle(inLocation);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#installBundle(java.lang.String)
	 */
	@Override
	public Bundle installBundle(String inLocation) throws BundleException {
		return null;
//		MockBundle bundle = new MockBundle();
//		bundle.setLocation(location);
//		return bundle;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getBundle(long)
	 */
	@Override
	public Bundle getBundle(long inId) {
		return bundle;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getBundles()
	 */
	@Override
	public Bundle[] getBundles() {
		return new Bundle[] { bundle };
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#addServiceListener(org.osgi.framework.ServiceListener, java.lang.String)
	 */
	@Override
	public void addServiceListener(ServiceListener inListener, String inFilter) throws InvalidSyntaxException {
		if (inListener == null)
			throw new IllegalArgumentException("non-null listener required");
		this.serviceListeners.add(inListener);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#addServiceListener(org.osgi.framework.ServiceListener)
	 */
	@Override
	public void addServiceListener(ServiceListener inListener) {
		try {
			addServiceListener(inListener, null);
		}
		catch (InvalidSyntaxException ex) {
			throw new IllegalStateException("exception should not occur");
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#removeServiceListener(org.osgi.framework.ServiceListener)
	 */
	@Override
	public void removeServiceListener(ServiceListener inListener) {
		serviceListeners.remove(inListener);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#addBundleListener(org.osgi.framework.BundleListener)
	 */
	@Override
	public void addBundleListener(BundleListener inListener) {
		bundleListeners.add(inListener);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#removeBundleListener(org.osgi.framework.BundleListener)
	 */
	@Override
	public void removeBundleListener(BundleListener inListener) {
		bundleListeners.remove(inListener);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#addFrameworkListener(org.osgi.framework.FrameworkListener)
	 */
	@Override
	public void addFrameworkListener(FrameworkListener inListener) {
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#removeFrameworkListener(org.osgi.framework.FrameworkListener)
	 */
	@Override
	public void removeFrameworkListener(FrameworkListener inListener) {
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#registerService(java.lang.String[], java.lang.Object, java.util.Dictionary)
	 */
	@Override
	public ServiceRegistration<?> registerService(String[] inClazzes, Object inService, Dictionary<String, ?> inProperties) {
//		MockServiceRegistration reg = new MockServiceRegistration(properties);
//		return reg;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#registerService(java.lang.String, java.lang.Object, java.util.Dictionary)
	 */
	@Override
	public ServiceRegistration<?> registerService(String inClazz, Object inService, Dictionary<String, ?> inProperties) {
		return registerService(new String[] { inClazz }, inService, inProperties);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#registerService(java.lang.Class, java.lang.Object, java.util.Dictionary)
	 */
	@Override
	public <S> ServiceRegistration<S> registerService(Class<S> inClazz, S inService, Dictionary<String, ?> inProperties) {
		return registerService(inClazz, inService, inProperties);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getServiceReferences(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceReference<?>[] getServiceReferences(String inClazz, String inFilter) throws InvalidSyntaxException {
		return null;
//		return new MockServiceReference(getBundle(), new String[] { clazz });
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getAllServiceReferences(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceReference<?>[] getAllServiceReferences(String inClazz, String inFilter) throws InvalidSyntaxException {
		return new ServiceReference[] {};
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getServiceReference(java.lang.String)
	 */
	@Override
	public ServiceReference<?> getServiceReference(String inClazz) {
		return null;
//		return new MockServiceReference(getBundle(), new String[] { clazz });
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getServiceReference(java.lang.Class)
	 */
	@Override
	public <S> ServiceReference<S> getServiceReference(Class<S> inClazz) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getServiceReferences(java.lang.Class, java.lang.String)
	 */
	@Override
	public <S> Collection<ServiceReference<S>> getServiceReferences(Class<S> inClazz, String inFilter) throws InvalidSyntaxException {
		return null;
		// Some jiggery-pokery to get round the fact that we don't ever use the
		// clazz
//		if (clazz == null)
//			if (filter != null) {
//				{
//					int i = filter.indexOf(Constants.OBJECTCLASS + "=");
//					if (i > 0) {
//						clazz = filter.substring(i + Constants.OBJECTCLASS.length() + 1);
//						clazz = clazz.substring(0, clazz.indexOf(")"));
//					}
//				}
//			}
//			else
//				clazz = Object.class.getName();
//		return new ServiceReference[] { new MockServiceReference(getBundle(), new String[] { clazz }) };
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public <S> S getService(ServiceReference<S> inReference) {
		return (S) new Object();
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#ungetService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public boolean ungetService(ServiceReference<?> inReference) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getDataFile(java.lang.String)
	 */
	@Override
	public File getDataFile(String inFilename) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#createFilter(java.lang.String)
	 */
	@Override
	public Filter createFilter(String inFilter) throws InvalidSyntaxException {
		return null;
//		return new MockFilter(filter);
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleContext#getBundle(java.lang.String)
	 */
	@Override
	public Bundle getBundle(String inLocation) {
		return bundle;
	}

}
