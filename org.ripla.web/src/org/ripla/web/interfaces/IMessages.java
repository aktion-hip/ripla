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
package org.ripla.web.interfaces;

/**
 * Interface for all <code>Messages</code> classes to get access to the localized messages provided by the bundles.
 *
 * @author Luthiger
 * Created: 28.12.2007
 */
public interface IMessages {

	/**
	 * Returns the message for the specified key.
	 * 
	 * @param inKey String the message key.
	 * @return String the localized message.
	 */
	String getMessage(String inKey);

	/**
	 * Returns the message using the specified key and arguments. 
	 * This method expects messages of the form <pre>some text to display on %s.</pre>
	 * 
	 * @param inKey String the message key
	 * @param inArgs Object[]
	 * @return String the localized message
	 */
	String getFormattedMessage(String inKey, Object... inArgs);

}
