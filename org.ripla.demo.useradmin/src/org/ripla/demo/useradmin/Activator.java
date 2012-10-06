package org.ripla.demo.useradmin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The bundle's activator.
 * 
 * @author Luthiger
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	protected static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception { // NOPMD by Luthiger on 06.09.12 23:48
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception { // NOPMD by Luthiger on 06.09.12 23:48
		Activator.context = null; // NOPMD by Luthiger on 06.09.12 23:48
	}

}
