/*******************************************************************************
 * Copyright (c) 2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.interfaces;

/**
 * Interface for classes listening to steps of the application configuration
 * workflow.
 * 
 * @author Luthiger
 */
public interface IWorkflowListener {

	/**
	 * Event signaling the workflow exit.
	 * 
	 * @param inReturnCode
	 *            int the code for the workflow exit
	 * @param inMessage
	 *            String the exit message, in case of a workflow error
	 */
	void workflowExit(int inReturnCode, String inMessage);

}
