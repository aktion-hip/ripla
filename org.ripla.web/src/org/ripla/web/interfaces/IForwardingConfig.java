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
 * Interface defining the forwarding configuration.<br />
 * Classes implementing this interface a part of the forwarding infrastructure.
 * 
 * @author lbenno
 */
public interface IForwardingConfig {

	/**
	 * Returns the forward's alias.
	 * 
	 * @return String the forward alias, i.e. the name the forward is know to
	 *         the application.
	 */
	String getAlias();

	/**
	 * Returns the target controller provided by the bundle.
	 * 
	 * @return {@link IPluggable} class the target the task if forwarded to.
	 */
	Class<? extends IPluggable> getTarget();

}
