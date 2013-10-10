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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.data.Countries;
import org.ripla.rap.demo.widgets.data.CountryBean;
import org.ripla.rap.util.GridLayoutHelper;
import org.ripla.rap.util.NotificationHelper;
import org.ripla.rap.util.TwinColSelect;

/**
 * The view to display the RAP selection widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class SelectionWidgetsView extends AbstractWidgetsView {
	private static final int OPTION_SIZE = 10;

	/**
	 * SelectionWidgetsView constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 */
	public SelectionWidgetsView(final Composite inParent) {
		super(inParent);
		final IMessages lMessages = Activator.getMessages();
		final String[] lCountryNames = Countries.getCountryNames();

		createTitle(lMessages.getMessage("widgets.title.page.select"));

		final Composite lColumns = new Composite(this, SWT.NONE);
		final GridLayout lLayout = new GridLayout(4, false);
		lLayout.marginWidth = 0;
		lColumns.setLayout(lLayout);

		final Composite lCol1 = createColumns(lColumns);
		final Composite lCol2 = createColumns(lColumns);
		final Composite lCol3 = createColumns(lColumns);
		final Composite lCol4 = createColumns(lColumns);
		lCol4.setLayoutData(GridLayoutHelper.createFillLayoutData());

		// list and combox
		createSubTitle(lCol1,
				lMessages.getMessage("widgets.selection.subtitle.list"));
		final List lList1 = new List(lCol1, SWT.MULTI | SWT.V_SCROLL
				| SWT.BORDER);
		lList1.setItems(lCountryNames);
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		lLayoutData.heightHint = 300;
		lList1.setLayoutData(lLayoutData);
		lList1.addSelectionListener(new SelectionListener(this));

		createSubTitle(lCol1,
				lMessages.getMessage("widgets.selection.subtitle.combox"));
		final CCombo lCombo = new CCombo(lCol1, SWT.BORDER | SWT.READ_ONLY);
		lCombo.setItems(lCountryNames);
		lCombo.setText(lMessages.getMessage("widgets.selection.combox.prompt"));
		lCombo.setVisibleItemCount(4);
		lCombo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent inEvent) {
				if (inEvent.keyCode == 13) {
					lCombo.setListVisible(false);
					NotificationHelper.showNotificationCenter(lCombo.getText(),
							inParent);
				} else if (inEvent.keyCode > 47) {
					// in case we enter a char
					lCombo.setListVisible(true);
				}
			}
		});

		final java.util.List<CountryBean> lCountries = Countries.getCountries();
		// radio and checkbox groups
		createSubTitle(
				lCol2,
				lMessages
						.getMessage("widgets.selection.subtitle.options.single"));
		final Composite lRadioGroup = new Composite(lCol2, SWT.NONE);
		lRadioGroup.setLayout(createRowLayout());
		final Button[] lRadios = createButtonSet(
				getRandomSubset(lCountries, OPTION_SIZE,
						System.currentTimeMillis()), lRadioGroup, SWT.RADIO);
		lRadios[0].setSelection(true);
		SelectionAdapter lListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				final Button lRadio = (Button) inEvent.widget;
				if (lRadio.getSelection()) {
					NotificationHelper.showNotificationCenter(
							lMessages.getMessage("widgets.selection.feedback")
									+ lRadio.getText(), inParent);
				}
			}
		};
		for (final Button lRadio : lRadios) {
			lRadio.addSelectionListener(lListener);
		}

		createSubTitle(
				lCol3,
				lMessages
						.getMessage("widgets.selection.subtitle.options.multiple"));
		final Composite lCheckGroup = new Composite(lCol3, SWT.NONE);
		lCheckGroup.setLayout(createRowLayout());
		final Button[] lChecks = createButtonSet(
				getRandomSubset(lCountries, OPTION_SIZE,
						System.currentTimeMillis() + 2000), lCheckGroup,
				SWT.CHECK);
		lChecks[0].setSelection(true);
		lListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				boolean lFirst = true;
				final StringBuilder lFeedback = new StringBuilder(
						lMessages.getMessage("widgets.selection.feedback"))
						.append("[");
				for (final Button lCheck : lChecks) {
					if (lCheck.getSelection()) {
						if (!lFirst) {
							lFeedback.append(", ");
						}
						lFirst = false;
						lFeedback.append(lCheck.getText());
					}
				}
				lFeedback.append("]");
				NotificationHelper.showNotificationCenter(
						new String(lFeedback), inParent);
			}
		};
		for (final Button lCheck : lChecks) {
			lCheck.addSelectionListener(lListener);
		}

		// twin column select
		createSubTitle(lCol4,
				lMessages.getMessage("widgets.selection.subtitle.twin"));
		final TwinColSelect lCountrySelect = new TwinColSelect(lCol4);
		lCountrySelect.setItems(lCountryNames);
		lCountrySelect.setListHeight(350);
		lCountrySelect.setListWidth(180);
	}

	private Button[] createButtonSet(final java.util.List<String> inCountries,
			final Composite inHolder, final int inStyle) {
		final Button[] out = new Button[OPTION_SIZE];
		int i = 0;
		for (final String lCountry : inCountries) {
			out[i] = new Button(inHolder, inStyle);
			out[i++].setText(lCountry);
		}
		return out;
	}

	private java.util.List<String> getRandomSubset(
			final java.util.List<CountryBean> inCountries, final int inLength,
			final long inSeed) {
		final java.util.List<String> out = new ArrayList<String>();

		final int lLength = inCountries.size();
		final Random lRandom = new Random(inSeed);

		for (int i = 0; i < inLength; i++) {
			out.add(inCountries.get(lRandom.nextInt(lLength)).getName());
		}
		Collections.sort(out);
		return out;
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

	// ---

	private static class SelectionListener extends SelectionAdapter {
		private final Composite parent;

		protected SelectionListener(final Composite inParent) {
			parent = inParent;
		}

		@Override
		public void widgetSelected(final SelectionEvent inEvent) {
			NotificationHelper.showNotificationCenter(
					((List) inEvent.widget).getSelection()[0], parent);
		}
	}

}
