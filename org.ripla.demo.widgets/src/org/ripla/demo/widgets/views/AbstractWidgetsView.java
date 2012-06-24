/*******************************************************************************
* Copyright (c) 2012 RelationWare, Benno Luthiger
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* RelationWare, Benno Luthiger
******************************************************************************/

package org.ripla.demo.widgets.views;

import org.ripla.demo.widgets.Activator;
import org.ripla.demo.widgets.data.CountryBean;
import org.ripla.web.interfaces.IMessages;
import org.ripla.web.util.LabelValueTable;
import org.ripla.web.util.RiplaViewHelper;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * Base class for all views in the demo widgets bundle.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public abstract class AbstractWidgetsView extends CustomComponent {
	
	protected VerticalLayout initLayout(IMessages inMessages) {
		VerticalLayout outLayout = new VerticalLayout();
		setCompositionRoot(outLayout);
		outLayout.setStyleName("demo-view"); //$NON-NLS-1$
		return outLayout;
	}
	
	protected VerticalLayout initLayout(IMessages inMessages, String inTitleKey) {
		VerticalLayout outLayout = initLayout(inMessages);
		outLayout.addComponent(new Label(String.format(RiplaViewHelper.TMPL_TITLE, "demo-pagetitle", inMessages.getMessage(inTitleKey)), Label.CONTENT_XHTML)); //$NON-NLS-1$ //$NON-NLS-2$
		return outLayout;
	}
	
	protected Label getSubtitle(String inTitle) {
		return new Label(String.format(RiplaViewHelper.TMPL_TITLE, "demo-subtitle", inTitle), Label.CONTENT_XHTML); //$NON-NLS-1$
	}

	/**
	 * @param inCountry
	 * @return {@link Layout}
	 */
	protected Layout createCountryPopup(CountryBean inCountry) {
		VerticalLayout outLayout = new VerticalLayout();
		outLayout.addComponent(getSubtitle(inCountry.getName()));
		
		IMessages lMessages = Activator.getMessages();
		LabelValueTable lValues = new LabelValueTable();
		lValues.addRowEmphasized(lMessages.getMessage("widgets.view.code.un"), inCountry.getUnCode()); //$NON-NLS-1$
		lValues.addRowEmphasized(lMessages.getMessage("widgets.view.region.un11"), inCountry.getUnRegion11()); //$NON-NLS-1$
		lValues.addRowEmphasized(lMessages.getMessage("widgets.view.region.sres4"), inCountry.getSresRegion()); //$NON-NLS-1$
		lValues.addRow(new Label(lMessages.getMessage("widgets.view.label"))); //$NON-NLS-1$
		lValues.addRowEmphasized("1990", inCountry.getPop1990()); //$NON-NLS-1$
		lValues.addRowEmphasized("1995", inCountry.getPop1995()); //$NON-NLS-1$
		lValues.addRowEmphasized("2000", inCountry.getPop2000()); //$NON-NLS-1$
		lValues.addRowEmphasized("2005", inCountry.getPop2005()); //$NON-NLS-1$
		lValues.addRowEmphasized("2010", inCountry.getPop2010()); //$NON-NLS-1$
		lValues.addRowEmphasized("2015", inCountry.getPop2015()); //$NON-NLS-1$
		lValues.addRowEmphasized("2020", inCountry.getPop2020()); //$NON-NLS-1$
		lValues.addRowEmphasized("2025", inCountry.getPop2025()); //$NON-NLS-1$
		lValues.addRowEmphasized("2030", inCountry.getPop2030()); //$NON-NLS-1$
		lValues.addRowEmphasized("2035", inCountry.getPop2035()); //$NON-NLS-1$
		lValues.addRowEmphasized("2040", inCountry.getPop2040()); //$NON-NLS-1$
		lValues.addRowEmphasized("2045", inCountry.getPop2045()); //$NON-NLS-1$
		lValues.addRowEmphasized("2050", inCountry.getPop2050()); //$NON-NLS-1$
		lValues.addRowEmphasized("2055", inCountry.getPop2055()); //$NON-NLS-1$
		lValues.addRowEmphasized("2060", inCountry.getPop2060()); //$NON-NLS-1$
		lValues.addRowEmphasized("2065", inCountry.getPop2065()); //$NON-NLS-1$
		lValues.addRowEmphasized("2070", inCountry.getPop2070()); //$NON-NLS-1$
		lValues.addRowEmphasized("2075", inCountry.getPop2075()); //$NON-NLS-1$
		lValues.addRowEmphasized("2080", inCountry.getPop2080()); //$NON-NLS-1$
		lValues.addRowEmphasized("2085", inCountry.getPop2085()); //$NON-NLS-1$
		lValues.addRowEmphasized("2090", inCountry.getPop2090()); //$NON-NLS-1$
		lValues.addRowEmphasized("2095", inCountry.getPop2095()); //$NON-NLS-1$
		lValues.addRowEmphasized("2100", inCountry.getPop2100()); //$NON-NLS-1$
		outLayout.addComponent(lValues);
		return outLayout;
	}

}
