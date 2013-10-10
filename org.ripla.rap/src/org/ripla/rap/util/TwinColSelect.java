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
package org.ripla.rap.util;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items. The middle column contains two action buttons to
 * move the selected items from the one side to the other<br/>
 * The width and height of this widget can be set by the methods
 * <code>setListHeight()</code> and <code>setListWidth()</code> methods. The
 * component's width is calculated by <code>2*listWidth + 80px</code> (used for
 * the middle column).
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class TwinColSelect extends Composite {

	private final List listOrig;
	private final List listTarget;

	/**
	 * TwinColSelect constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 */
	public TwinColSelect(final Composite inParent) {
		super(inParent, SWT.NONE);

		final GridLayout lLayout = new GridLayout(3, false);
		lLayout.marginWidth = 0;
		setLayout(lLayout);
		setLayoutData(GridLayoutHelper.createFillLayoutData());

		final Composite lCol1 = createColumns(this);
		final Composite lCol2 = createColumns(this);
		final Composite lCol3 = createColumns(this);
		lCol3.setLayoutData(GridLayoutHelper.createFillLayoutData());

		listOrig = new List(lCol1, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		listOrig.setLayoutData(GridLayoutHelper.createFillLayoutData());
		listOrig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent inEvent) {
				final String[] lSelection = ((List) inEvent.widget)
						.getSelection();
				if (lSelection != null && lSelection.length > 0) {
					itemMover(listOrig, listTarget);
				}
			}
		});

		final Composite lButtons = new Composite(lCol2, SWT.NONE);
		lButtons.setLayout(createRowLayout());
		final Button lAdd = new Button(lButtons, SWT.PUSH);
		lAdd.setText("»"); // >>
		lAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				itemMover(listOrig, listTarget);
			}
		});
		final Button lRemove = new Button(lButtons, SWT.PUSH);
		lRemove.setText("«"); // <<
		lRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				itemMover(listTarget, listOrig);
			}
		});

		listTarget = new List(lCol3, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		listTarget.setLayoutData(GridLayoutHelper.createFillLayoutData());
		listTarget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent inEvent) {
				final String[] lSelection = ((List) inEvent.widget)
						.getSelection();
				if (lSelection != null && lSelection.length > 0) {
					itemMover(listTarget, listOrig);
				}
			}
		});
	}

	private void itemMover(final List inFrom, final List inTo) {
		inTo.setItems(addSorted(inTo.getItems(), inFrom.getSelection()));
		inFrom.remove(inFrom.getSelectionIndices());
	}

	/**
	 * Sorting method made protected (instead of private) for testing purposes.
	 * 
	 * @param inSorted
	 *            String[] a (long) list of items, must be sorted
	 * @param inToMerge
	 *            String[] a list of items to be merged into the sorted list
	 * @return String[] the sorted array
	 */
	protected String[] addSorted(final String[] inSorted,
			final String[] inToMerge) {
		final String[] out = new String[inSorted.length + inToMerge.length];
		int lCursor = 0;
		int lUsed = 0;
		for (final String lToAdd : inToMerge) {
			for (int i = lCursor - lUsed; i < inSorted.length; i++) {
				final int lComparison = lToAdd.compareTo(inSorted[i]);
				if (lComparison < 0) {
					out[lCursor] = lToAdd;
					lCursor++;
					lUsed++;
					break;
				} else if (lComparison == 0) {
					out[lCursor] = inSorted[i];
					lCursor++;
					break;
				} else if (lComparison > 0) {
					out[lCursor] = inSorted[i];
					lCursor++;
				}
			}
		}
		// process remaining in sorted input
		for (int i = lCursor - lUsed; i < inSorted.length; i++) {
			out[lCursor] = inSorted[i];
			lCursor++;
		}
		// process remaining in input to add
		for (int i = lUsed; i < inToMerge.length; i++) {
			out[lCursor] = inToMerge[i];
			lCursor++;
		}
		return out;
	}

	private Composite createColumns(final Composite inColumns) {
		final Composite outColumn = new Composite(inColumns, SWT.NONE);
		final GridLayout lLayout = GridLayoutHelper.createGridLayout();
		lLayout.marginRight = 15;
		outColumn.setLayout(lLayout);
		outColumn.setLayoutData(GridLayoutHelper.createFillLayoutData());
		return outColumn;
	}

	private RowLayout createRowLayout() {
		final RowLayout outLayout = new RowLayout(SWT.VERTICAL);
		outLayout.marginTop = 0;
		outLayout.marginLeft = 0;
		outLayout.marginRight = 0;
		outLayout.marginBottom = 0;
		outLayout.spacing = 10;
		outLayout.fill = true;
		outLayout.wrap = false;
		return outLayout;
	}

	/**
	 * Sets the receiver's items to be the given array of items.
	 * 
	 * @param inItems
	 *            String[]
	 */
	public void setItems(final String[] inItems) {
		final java.util.List<String> lItems = Arrays.asList(inItems);
		Collections.sort(lItems);
		listOrig.setItems(lItems.toArray(new String[] {}));
	}

	/**
	 * Sets the height of the control.
	 * 
	 * @param inHeight
	 *            int height of list in pixels
	 */
	public void setListHeight(final int inHeight) {
		((GridData) listOrig.getLayoutData()).heightHint = inHeight;
		((GridData) listTarget.getLayoutData()).heightHint = inHeight;
	}

	/**
	 * Sets the width of the list controls. <br/>
	 * <b>Note</b>: both lists have equal width. The component's width is
	 * calculated by <code>2*listWidth + 80px</code> (used for the middle
	 * column).
	 * 
	 * @param inWidth
	 *            int width of list in pixels
	 */
	public void setListWidth(final int inWidth) {
		((GridData) listOrig.getLayoutData()).widthHint = inWidth;
		((GridData) listTarget.getLayoutData()).widthHint = inWidth;
	}

	/**
	 * Returns an array of <code>String</code>s that are currently selected in
	 * the receiver. An empty array indicates that no items are selected.
	 * 
	 * @return String[] an array representing the selection
	 */
	public String[] getSelection() {
		return listTarget.getSelection();
	}

}
