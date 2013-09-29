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

package org.ripla.web.demo.config.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.ripla.services.ISkinService;
import org.ripla.web.demo.exp.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The configuration bundle's skin registry.
 * 
 * @author Luthiger
 */
public enum SkinConfigRegistry {
	INSTANCE;

	private static final Logger LOG = LoggerFactory
			.getLogger(SkinConfigRegistry.class);

	private final Collection<ISkinService> skins = new ArrayList<ISkinService>();
	private ConfigurationAdmin configAdmin;

	/**
	 * @param inSkin
	 */
	public void register(final ISkinService inSkin) {
		skins.add(inSkin);
	}

	/**
	 * @param inSkin
	 */
	public void unregister(final ISkinService inSkin) {
		skins.remove(inSkin);
	}

	/**
	 * @param inConfigAdmin
	 */
	public void setConfigAdmin(final ConfigurationAdmin inConfigAdmin) {
		configAdmin = inConfigAdmin;
	}

	/**
	 * @return List&lt;SkinBean> list of registered skins
	 */
	public List<SkinBean> getSkins() {
		final List<SkinBean> out = new ArrayList<SkinBean>(skins.size());
		for (final ISkinService lSkin : skins) {
			out.add(new SkinBean(lSkin.getSkinID(), lSkin.getSkinName())); // NOPMD
		}
		Collections.sort(out);
		return out;
	}

	/**
	 * Changes the actual skin to the skin with the specified id.
	 * 
	 * @param inSkinId
	 *            String the selected skin's id
	 */
	public void changeSkin(final String inSkinId) {
		if (configAdmin != null) {
			try {
				final Configuration lConfiguration = configAdmin
						.getConfiguration(Constants.COMPONENT_NAME, null);
				final Dictionary<String, Object> lProperties = new Hashtable<String, Object>();
				lProperties.put(Constants.KEY_CONFIG_SKIN, inSkinId);
				lConfiguration.update(lProperties);
			}
			catch (final IOException exc) {
				LOG.error(
						"Error encounteres while updating the OSGi configuration admin!",
						exc);
			}
		}
	}

}
