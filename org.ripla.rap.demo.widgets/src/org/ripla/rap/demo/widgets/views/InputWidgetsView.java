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

import java.util.List;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.widget.ckedit.CkEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.data.Countries;
import org.ripla.rap.demo.widgets.data.CountryBean;
import org.ripla.rap.util.GridLayoutHelper;


/**
 * The view to display the RAP input widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class InputWidgetsView extends AbstractWidgetsView {
	private static final int WIDTH_FIELD = 300;
	private static final int WIDTH_AREA = 500;
	private static final int FILTER_WIDTH = 300;
	private static final int FILTER_HEIGHT = 400;
	private final CountriesFilter viewerFilter;
	private final TableViewer viewer;
	private List<CountryBean> countries;

	/**
	 * InputWidgetsView constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 */
	public InputWidgetsView(final Composite inParent) {
		super(inParent);
		final IMessages lMessages = Activator.getMessages();

		viewerFilter = new CountriesFilter();

		createTitle(lMessages.getMessage("widgets.title.page.input"));

		final Composite lColumns = new Composite(this, SWT.NONE);
		final GridLayout lLayout = new GridLayout(3, false);
		lLayout.marginWidth = 0;
		lColumns.setLayout(lLayout);

		final Composite lCol1 = createColumns(lColumns);
		final Composite lCol2 = createColumns(lColumns);
		final Composite lCol3 = createColumns(lColumns);
		lCol3.setLayoutData(GridLayoutHelper.createFillLayoutData());

		// classic input fields
		createSubTitle(lCol1,
				lMessages.getMessage("widgets.input.subtitle.input.normal"));
		final Text lTextField = new Text(lCol1, SWT.SINGLE | SWT.BORDER);
		lTextField.setLayoutData(createHorzFillData(WIDTH_FIELD));

		createSubTitle(lCol1,
				lMessages.getMessage("widgets.input.subtitle.input.prompt"));
		final Text lTextField2 = new Text(lCol1, SWT.SINGLE | SWT.BORDER);
		lTextField2.setLayoutData(createHorzFillData(WIDTH_FIELD));
		lTextField2.setMessage(lMessages
				.getMessage("widgets.input.input.prompt"));

		createSubTitle(lCol1,
				lMessages.getMessage("widgets.input.subtitle.input.password"));
		final Text lPassword = new Text(lCol1, SWT.SINGLE | SWT.BORDER
				| SWT.PASSWORD);
		lPassword.setLayoutData(createHorzFillData(WIDTH_FIELD));

		createSubTitle(lCol1,
				lMessages.getMessage("widgets.input.subtitle.date"));
		new DateTime(lCol1, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN | SWT.MEDIUM);

		// text areas
		createSubTitle(lCol2,
				lMessages.getMessage("widgets.input.subtitle.text.area"));
		final Text lArea = new Text(lCol2, SWT.MULTI | SWT.BORDER);
		GridData lLayoutData = createHorzFillData(WIDTH_AREA);
		lLayoutData.heightHint = 120;
		lArea.setLayoutData(lLayoutData);

		createSubTitle(lCol2,
				lMessages.getMessage("widgets.input.subtitle.rich.text"));
		final CkEditor lEditor = new CkEditor(lCol2, SWT.BORDER);
		lLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		lLayoutData.heightHint = 250;
		lEditor.setLayoutData(lLayoutData);
		lEditor.setBackground(new Color(inParent.getDisplay(), 247, 247, 247));

		// text input with filter
		createSubTitle(lCol3,
				lMessages.getMessage("widgets.input.subtitle.input.filter"));
		createTableFilter(lCol3);
		viewer = createTableViewer(lCol3);
	}

	private Text createTableFilter(final Composite inParent) {
		final Text outFilter = new Text(inParent, SWT.BORDER);
		outFilter.setLayoutData(createHorzFillData(FILTER_WIDTH));
		outFilter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent inEvent) {
				final Text text = (Text) inEvent.widget;
				viewerFilter.setText(text.getText());
				viewer.refresh();
			}
		});
		outFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent inEvent) {
				if (inEvent.keyCode == 13 || inEvent.keyCode == SWT.ESC
						|| inEvent.keyCode == SWT.ARROW_DOWN) {
					handleSelection(inEvent.keyCode == SWT.ARROW_DOWN);
					viewer.getTable().forceFocus();
				}
			}
		});
		return outFilter;
	}

	private void handleSelection(final boolean inReset) {
		if (viewer.getTable().getItemCount() > 0) {
			if (inReset || viewer.getSelection().isEmpty()) {
				viewer.getTable().select(0);
			}
			final int lIndex = viewer.getTable().getSelectionIndex();
			// NOTE : setSelection needed as it also sets focus index and
			// scrolls
			viewer.getTable().setSelection(lIndex);
		}
	}

	private TableViewer createTableViewer(final Composite inParent) {
		countries = Countries.getCountries();
		final TableViewer outViewer = new TableViewer(inParent, SWT.BORDER);
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		lLayoutData.heightHint = FILTER_HEIGHT;
		outViewer.getTable().setLayoutData(lLayoutData);
		ColumnViewerToolTipSupport.enableFor(outViewer);
		outViewer.setUseHashlookup(true);
		outViewer.setContentProvider(new CountryContentProvider());
		outViewer.setLabelProvider(new CountryLabelProvider());
		outViewer.getTable().setHeaderVisible(false);
		outViewer.getTable().setLinesVisible(true);
		outViewer.getTable().getLayoutData();

		outViewer.setInput(countries);
		outViewer.addFilter(viewerFilter);
		return outViewer;
	}

	private GridData createHorzFillData(final int inWidth) {
		final GridData out = new GridData(SWT.FILL, SWT.TOP, true, false);
		out.minimumWidth = inWidth;
		return out;
	}

	// ---

	private static final class CountriesFilter extends ViewerFilter {
		private String text;

		public void setText(final String inString) {
			this.text = inString;
		}

		@Override
		public boolean select(final Viewer inViewer,
				final Object inParentElement, final Object inCountry) {
			boolean outResult = true;
			final CountryBean lCountry = (CountryBean) inCountry;
			if (text != null && text.length() > 0) {
				final String lLowerCaseText = text.toLowerCase();
				final String lCountryName = lCountry.getName().toLowerCase();
				outResult = lCountryName.indexOf(lLowerCaseText) != -1;
			}
			return outResult;
		}

		@Override
		public boolean isFilterProperty(final Object element, final String prop) {
			return true;
		}
	}

	private final static class CountryContentProvider implements
			IStructuredContentProvider {
		Object[] providedCountries;

		@Override
		public Object[] getElements(final Object inInputElement) {
			return providedCountries;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void inputChanged(final Viewer inViewer,
				final Object inOldInput, final Object inNewInput) {
			if (inNewInput == null) {
				providedCountries = new Object[0];
			} else {
				providedCountries = ((List<CountryBean>) inNewInput).toArray();
			}
		}

		@Override
		public void dispose() {
			// do nothing
		}
	}

	private final static class CountryLabelProvider extends CellLabelProvider {

		@Override
		public void update(final ViewerCell inCell) {
			final CountryBean lCountry = (CountryBean) inCell.getElement();
			inCell.setText(lCountry.getName());
		}
	}

}
