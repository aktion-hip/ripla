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

package org.ripla.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.ComboBox;

/**
 * <p>
 * Class to create a generic select widget.
 * </p>
 * Usage:
 * 
 * <pre>
 * layout.addComponent(GenericSelect.getSelection(property,
 * 		GenericSelect.toCollection(&quot;red&quot;, &quot;blue&quot;, &quot;yellow&quot;), 250, false, null));
 * </pre>
 * 
 * @author Luthiger
 */
public final class GenericSelect {

	private GenericSelect() {
	}

	/**
	 * Creates a <code>Selection</code> widget with the provided options.
	 * 
	 * @param inProperty
	 *            {@link Property&lt;String>}
	 * @param inOptions
	 *            {@link Collection}
	 * @param inWidth
	 *            int
	 * @param inAllowNull
	 *            boolean
	 * @param inProcessor
	 *            {@link IProcessor} for post processing, may be
	 *            <code>null</code>
	 * @return {@link ComboBox}
	 */
	@SuppressWarnings("serial")
	public static ComboBox getSelection(final Property<String> inProperty,
			final Collection<String> inOptions, final int inWidth,
			final boolean inAllowNull, final IProcessor inProcessor) {
		final ComboBox outSelect = new ComboBox(null, inOptions);
		outSelect.select(inProperty.getValue());
		outSelect.setStyleName("ripla-select"); //$NON-NLS-1$
		outSelect.setWidth(inWidth, Unit.PIXELS);
		outSelect.setNullSelectionAllowed(inAllowNull);
		outSelect.setImmediate(true);
		outSelect.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent inEvent) {
				final String lItemID = (String) inEvent.getProperty()
						.getValue();
				inProperty.setValue(lItemID);
				if (inProcessor != null) {
					inProcessor.process(lItemID);
				}
			}
		});
		return outSelect;
	}

	/**
	 * Convenience method, creates a <code>Collection</code> with the specified
	 * String values.
	 * 
	 * @param inValues
	 *            String []
	 * @return {@link Collection}
	 */
	public static Collection<String> toCollection(final String... inValues) {
		final Collection<String> out = new ArrayList<String>();
		Collections.addAll(out, inValues);
		return out;
	}

	// ---

	/**
	 * Process the selection.
	 * 
	 * @author Luthiger
	 */
	public interface IProcessor {
		/**
		 * Do something depending on the selected item.
		 * 
		 * @param inItemID
		 *            String the selected item's id
		 */
		void process(String inItemID);
	}

}
