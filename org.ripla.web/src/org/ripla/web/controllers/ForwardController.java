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
package org.ripla.web.controllers;

import org.ripla.exceptions.RiplaException;
import org.ripla.web.Activator;
import org.ripla.web.internal.services.UseCaseRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Proxy controller that forwards to a specific controller provided by bundles.
 * 
 * @author Luthiger
 */
public abstract class ForwardController extends AbstractController {
	private static final Logger LOG = LoggerFactory
			.getLogger(ForwardController.class);

	@Override
	protected String needsPermission() {
		return ""; //$NON-NLS-1$
	}

	@Override
	protected Component runChecked() throws RiplaException {
		final String lTargetControllerName = getTargetControllerName(getAlias());
		if (lTargetControllerName == null) {
			LOG.error(
					"Configuration error: no target task provided for \"{}\" forward.", getAlias()); //$NON-NLS-1$
			return new Label(Activator.getMessages().getMessage(
					"errmsg.error.configuration")); //$NON-NLS-1$
		}
		return UseCaseRegistry.INSTANCE.getControllerManager().getContent(
				lTargetControllerName);
	}

	/**
	 * @return String the alias, i.e. the key the target task has been
	 *         registered with.
	 */
	abstract protected String getAlias();

	/**
	 * @param inAlias
	 *            String the alias name
	 * @return String the name of the target controller, i.e. the controller to
	 *         run when the alias is requested.
	 */
	abstract protected String getTargetControllerName(String inAlias);

}