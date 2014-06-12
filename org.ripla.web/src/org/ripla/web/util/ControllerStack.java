/*******************************************************************************
 * Copyright (c) 2012-2014 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.web.util;

import java.util.Stack;

import org.ripla.web.interfaces.IPluggable;

import com.vaadin.server.VaadinSession;

/**
 * A stack of <code>IPluggable</code> instances.
 * 
 * @author Luthiger
 */
public class ControllerStack {
	private static final int DFT_SIZE = 5;

	private final Stack<IPluggable> taskStack;
	private final int stackSize;

	/**
	 * Constructor with specified stack size.
	 * 
	 * @param inStackSize
	 *            int the stack size
	 */
	public ControllerStack(final int inStackSize) {
		taskStack = new Stack<IPluggable>();
		stackSize = inStackSize;
	}

	/**
	 * Constructor with default stack size.
	 */
	public ControllerStack() {
		this(DFT_SIZE);
	}

	/**
	 * Tests if this stack is empty.
	 * 
	 * @return boolean <code>true</code> it the stack is empty,
	 *         <code>false</code> otherwise
	 */
	public boolean empty() {
		return taskStack.empty();
	}

	/**
	 * Looks at the controller at the top of this stack without removing it from
	 * the stack.
	 * 
	 * @return {@link IPluggable}
	 */
	public IPluggable peek() {
		return taskStack.peek();
	}

	/**
	 * Removes the controller at the top of this stack and returns that object
	 * as the value of this function. If the stack is empty, an
	 * EmptyStackException is thrown.
	 * 
	 * @return {@link IPluggable}
	 */
	public IPluggable pop() {
		return taskStack.pop();
	}

	/**
	 * Pushes a controller onto the top of this stack. If the stack exceeds the
	 * specified size, the element at the stack bottom is thrown away. The
	 * controller is pushed to the stack only if it's not equal to the
	 * controller at the stack's top.
	 * 
	 * @param inController
	 *            {@link IPluggable}
	 * @return {@link IPluggable}
	 */
	public IPluggable push(final IPluggable inController) {
		if (!empty()) {
			if (inController.equals(taskStack.peek()))
				return inController;
		}

		final IPluggable out = taskStack.push(inController);
		if (taskStack.size() > stackSize) {
			taskStack.removeElementAt(0);
		}
		return out;
	}

	/**
	 * Removes all of the elements from this stack.
	 */
	public void clear() {
		taskStack.clear();
	}

	/**
	 * Convenience method to return the user's controller stack.
	 * 
	 * @return {@link ControllerStack} the user's unique controller stack
	 */
	public static ControllerStack getControllerStack() {
		try {
			VaadinSession.getCurrent().getLockInstance().lock();
			ControllerStack out = VaadinSession.getCurrent().getAttribute(
					ControllerStack.class);
			if (out == null) {
				out = new ControllerStack();
				VaadinSession.getCurrent().setAttribute(ControllerStack.class,
						out);
			}
			return out;
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}

}
