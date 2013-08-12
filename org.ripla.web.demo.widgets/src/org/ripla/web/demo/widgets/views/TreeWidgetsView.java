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

package org.ripla.web.demo.widgets.views;

import org.ripla.interfaces.IMessages;
import org.ripla.web.demo.widgets.Activator;
import org.ripla.web.demo.widgets.data.CountryBean;
import org.ripla.web.demo.widgets.data.CountryTree;
import org.ripla.web.util.Popup;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

/**
 * The view to display the Vaadin tree widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class TreeWidgetsView extends AbstractWidgetsView {

	/**
	 * @param inCountryContainer
	 */
	public TreeWidgetsView(final CountryTree inCountries) {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = initLayout(lMessages,
				"widgets.title.page.tree"); //$NON-NLS-1$

		final Tree lTree = new Tree(
				lMessages.getMessage("widgets.view.tree.title")); //$NON-NLS-1$
		lTree.setContainerDataSource(inCountries);
		lTree.setImmediate(true);
		lTree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(final ItemClickEvent inEvent) {
				final Object lItem = inEvent.getItemId();
				if (lItem instanceof CountryBean) {
					Popup.displayPopup(
							lMessages.getMessage("widgets.view.tree.popup"), createCountryPopup((CountryBean) lItem), 260, 685); //$NON-NLS-1$
				} else {
					if (lTree.isExpanded(lItem)) {
						lTree.collapseItem(lItem);
					} else {
						lTree.expandItem(lItem);
					}
				}
			}
		});

		lLayout.addComponent(lTree);
	}

}
