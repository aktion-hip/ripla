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

package org.ripla.web.internal.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ripla.web.services.IToolbarItem;

/**
 * Singleton instance to register the use cases provided by usecase bundles. The
 * provided use cases are injected through the service consumer
 * <code>UseCaseComponent</code>.
 * 
 * @author Luthiger
 */
public enum ToolbarItemRegistry {
	INSTANCE;

	private final transient List<IToolbarItem> toolbarItems = Collections
			.synchronizedList(new ArrayList<IToolbarItem>());

	public void registerToolbarItem(final IToolbarItem inItem) {
		toolbarItems.add(inItem);
	}

	public void unregisterToolbarItem(final IToolbarItem inItem) {
		toolbarItems.remove(inItem);
	}

	/**
	 * @return Collection&lt;IToolbarItem> the sorted list of toolbar items.
	 */
	public Collection<IToolbarItem> getSortedItems() {
		Collections.sort(toolbarItems, new ItemComparator());
		return toolbarItems;
	}

	private static class ItemComparator implements Comparator<IToolbarItem> {
		@Override
		public int compare(final IToolbarItem inItem1,
				final IToolbarItem inItem2) {
			return inItem2.getPosition() - inItem1.getPosition();
		}
	}

}
