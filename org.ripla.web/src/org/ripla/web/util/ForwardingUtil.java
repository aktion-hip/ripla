/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.web.util;

import java.util.HashMap;
import java.util.Map;

import org.ripla.web.interfaces.IForwardingMapper;
import org.ripla.web.interfaces.IPluggable;

/**
 * Helper class to facilitate the implementation of the forwarding functionality
 * (i.e. the forwarding registry).
 * 
 * @author lbenno
 */
public class ForwardingUtil {

	private final Map<String, IForwardingMapper> mapperRegistry;

	/**
	 * ForwardingUtil constructor.
	 * 
	 * @param inSize
	 *            int the registry size
	 */
	public ForwardingUtil(final int inSize) {
		mapperRegistry = new HashMap<String, IForwardingMapper>(inSize);
	}

	/**
	 * Puts the specified forwarding mapper to the registry.
	 * 
	 * @param inAlias
	 *            String the mapper's alias
	 * @param inMapper
	 *            {@link IForwardingMapper}
	 */
	public void put(final String inAlias, final IForwardingMapper inMapper) {
		mapperRegistry.put(inAlias, inMapper);
	}

	public IForwardingMapper get(final String inAlias) {
		return mapperRegistry.get(inAlias);
	}

	/**
	 * Registers the specified target class with the specified alias.
	 * 
	 * @param inAlias
	 *            String
	 * @param inTarget
	 *            IPluggableTask class
	 */
	public void registerTarget(final String inAlias,
			final Class<? extends IPluggable> inTarget) {
		final IForwardingMapper mapping = mapperRegistry.get(inAlias);
		if (mapping != null) {
			mapping.setTarget(inTarget);
		}
	}

	/**
	 * Unregisters the target task with the specified alias.
	 * 
	 * @param inAlias
	 *            String
	 */
	public void unregisterTarget(final String inAlias) {
		final IForwardingMapper mapping = mapperRegistry.get(inAlias);
		if (mapping != null) {
			mapping.setTarget(null);
		}
	}

	/**
	 * Returns the target mapped to the specified alias.
	 * 
	 * @param inAlias
	 *            String the forward's alias
	 * @return String the fully qualified name of the target controller
	 */
	public String getTargetOf(final IForwardingMapper inAlias) {
		final IForwardingMapper mapped = mapperRegistry.get(inAlias.getAlias());
		// TODO: handle mapped = null, e.g. by returning the
		// no-controller-found-Controller
		return UseCaseHelper.createFullyQualifiedControllerName(mapped
				.getTarget());
	}

}
