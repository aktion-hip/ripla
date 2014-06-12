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

package org.ripla.web.controllers;

import org.ripla.web.interfaces.IForwardingMapper;
import org.ripla.web.interfaces.IPluggable;

/**
 * Base class for forwarding mapping classes.
 * 
 * @author lbenno
 */
public abstract class AbstractForwardingMapper implements IForwardingMapper {
	private Class<? extends IPluggable> target;

	@Override
	public void setTarget(final Class<? extends IPluggable> inTarget) {
		target = inTarget;
	}

	@Override
	public Class<? extends IPluggable> getTarget() {
		return target;
	}
}
