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
 * Interface defining the forwarding service. This is an OSGi DS definition.
 * 
 * @author lbenno
 */
public interface IForwarding {

	/**
	 * @return IForwardingConfig[] the forwarding configurations provided by the
	 *         forwarding service implementation.
	 */
	IForwardingConfig[] getForwardingConfigs();

}
