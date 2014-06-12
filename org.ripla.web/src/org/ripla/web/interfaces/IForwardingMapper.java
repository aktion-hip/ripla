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

package org.ripla.web.interfaces;

/**
 * Interface for forwarding mappers. Instances implementing this interface map
 * an alias to an controller instance.
 * 
 * @author lbenno
 */
public interface IForwardingMapper {

	/**
	 * Sets the target controller class.
	 * 
	 * @param inTarget
	 *            Class<? extends IPluggable>
	 */
	void setTarget(final Class<? extends IPluggable> inTarget);

	/**
	 * Returns the target controller class.
	 * 
	 * @return Class<? extends IPluggable>
	 */
	Class<? extends IPluggable> getTarget();

	/**
	 * Returns the forward controller alias.
	 * 
	 * @return String the alias, i.e. the key the target task has been
	 *         registered with.
	 */
	String getAlias();

}
