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

package org.ripla.rap.demo.widgets.views;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.data.CountryBean;
import org.ripla.rap.demo.widgets.data.CountryTree.TreeObject;
import org.ripla.rap.util.AbstractRiplaView;
import org.ripla.rap.util.GridLayoutHelper;
import org.ripla.rap.util.LabelHelper;
import org.ripla.rap.util.Popup;
import org.ripla.rap.util.Popup.PopupButtons;

/**
 * The view to display the RAP tree widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class TreeWidgetsView extends AbstractRiplaView {
	private static final IMessages MESSAGES = Activator.getMessages();

	/**
	 * TreeWidgetsView constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inTree
	 *            {@link TreeObject}
	 */
	public TreeWidgetsView(final Composite inParent, final TreeObject inTree) {
		super(inParent);
		createTitle(MESSAGES.getMessage("widgets.title.page.tree"));

		LabelHelper.createLabel(this,
				MESSAGES.getMessage("widgets.view.tree.title"), "ripla-font");

		final TreeViewer lViewer = new TreeViewer(this, SWT.V_SCROLL
				| SWT.H_SCROLL);
		final Tree lTree = lViewer.getTree();
		lTree.setLayout(GridLayoutHelper.createGridLayout());
		lTree.setData(RWT.CUSTOM_VARIANT, "ripla-font");
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		lLayoutData.heightHint = 430;
		lTree.setLayoutData(lLayoutData);

		lViewer.setContentProvider(new CountryTreeContentProvider());
		lViewer.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(final ViewerCell inCell) {
				final TreeObject lCountry = (TreeObject) inCell.getElement();
				inCell.setText(lCountry.getLabel());
			}
		});
		lViewer.expandAll();
		lViewer.setInput(inTree);
		lViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent inEvent) {
				final TreeObject lTreeObject = (TreeObject) ((IStructuredSelection) inEvent
						.getSelection()).getFirstElement();
				if (lTreeObject.hasChildren()) {
					if (lViewer.getExpandedState(lTreeObject)) {
						lViewer.collapseToLevel(lTreeObject,
								AbstractTreeViewer.ALL_LEVELS);
					} else {
						lViewer.expandToLevel(lTreeObject, 1);
					}
				} else {
					popup(lTreeObject.getCountry());
				}
			}
		});

		lTree.forceFocus();
		lTree.select(lTree.getItem(0));
	}

	private void popup(final CountryBean inCountry) {
		final Popup lPopup = new Popup(getShell(),
				MESSAGES.getMessage("widgets.table.popup.title"),
				inCountry.toHtml(), Popup.DFT_WIDTH, 500);
		lPopup.setButtons(PopupButtons.CANCEL);
		lPopup.open();
	}

	// ---

	private static class CountryTreeContentProvider implements
			ITreeContentProvider {

		@Override
		public Object[] getElements(final Object inParent) {
			return getChildren(inParent);
		}

		@Override
		public Object[] getChildren(final Object inParent) {
			if (inParent instanceof TreeObject) {
				return ((TreeObject) inParent).getChidren();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(final Object inChild) {
			if (inChild instanceof TreeObject) {
				return ((TreeObject) inChild).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(final Object inParent) {
			if (inParent instanceof TreeObject) {
				return ((TreeObject) inParent).hasChildren();
			}
			return false;
		}

		@Override
		public void inputChanged(final Viewer inViewer,
				final Object inOldInput, final Object inNewInput) {
			// nothing to do
		}

		@Override
		public void dispose() {
			// nothing to do
		}
	}

}
