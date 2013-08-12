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

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * The view to display the Vaadin button widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class ButtonWidgetsView extends AbstractWidgetsView {

	public ButtonWidgetsView() {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = initLayout(lMessages,
				"widgets.title.page.button"); //$NON-NLS-1$

		lLayout.addComponent(getSubtitle(lMessages
				.getMessage("widgets.view.button.subtitle.button"))); //$NON-NLS-1$
		final Button lButton = new Button(
				lMessages.getMessage("widgets.view.button.label.save")); //$NON-NLS-1$
		lButton.setDescription(lMessages
				.getMessage("widgets.view.button.descr.normal")); //$NON-NLS-1$
		lButton.addClickListener(new Listener(lMessages
				.getMessage("widgets.view.button.feedback.normal"))); //$NON-NLS-1$
		lLayout.addComponent(lButton);

		lLayout.addComponent(getSubtitle(lMessages
				.getMessage("widgets.view.button.label.image"))); //$NON-NLS-1$
		final Button lButton2 = new Button(
				lMessages.getMessage("widgets.view.button.label.save")); //$NON-NLS-1$
		lButton2.setIcon(new ThemeResource("../runo/icons/16/ok.png")); //$NON-NLS-1$
		lButton2.setDescription(lMessages
				.getMessage("widgets.view.button.label.image")); //$NON-NLS-1$
		lButton2.addClickListener(new Listener(lMessages
				.getMessage("widgets.view.button.feedback.image"))); //$NON-NLS-1$
		lLayout.addComponent(lButton2);

		lLayout.addComponent(getSubtitle(lMessages
				.getMessage("widgets.view.button.subtitle.disable"))); //$NON-NLS-1$
		lLayout.addComponent(new Label(lMessages
				.getMessage("widgets.view.button.label.disable"))); //$NON-NLS-1$
		final Button lButton3 = new Button(
				lMessages.getMessage("widgets.view.button.label.click")); //$NON-NLS-1$
		lButton3.setDisableOnClick(true);
		lButton3.setDescription(lMessages
				.getMessage("widgets.view.button.descr.disable")); //$NON-NLS-1$
		lButton3.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				try {
					Thread.sleep(3000);
				}
				catch (final InterruptedException exc) {
				}
				Notification.show(lMessages
						.getMessage("widgets.view.button.feedback.disable"),
						Notification.Type.TRAY_NOTIFICATION);
				inEvent.getButton().setEnabled(true);
			}
		});
		lLayout.addComponent(lButton3);

		lLayout.addComponent(getSubtitle(lMessages
				.getMessage("widgets.view.button.subtitle.link"))); //$NON-NLS-1$
		final Button lButton4 = new Button(
				lMessages.getMessage("widgets.view.button.label.click")); //$NON-NLS-1$
		lButton4.setStyleName(BaseTheme.BUTTON_LINK);
		lButton4.addClickListener(new Listener(lMessages
				.getMessage("widgets.view.button.feedback.link"))); //$NON-NLS-1$
		lLayout.addComponent(new Label(lMessages
				.getMessage("widgets.view.button.label.link"))); //$NON-NLS-1$
		lLayout.addComponent(lButton4);

		lLayout.addComponent(getSubtitle(lMessages
				.getMessage("widgets.view.button.subtitle.check"))); //$NON-NLS-1$
		lLayout.addComponent(new Label(lMessages
				.getMessage("widgets.view.button.label.check"))); //$NON-NLS-1$
		final CheckBox lCheckbox = new CheckBox(
				lMessages.getMessage("widgets.view.button.label.click")); //$NON-NLS-1$
		lCheckbox.setImmediate(true);
		lCheckbox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent inEvent) {
				Notification.show(lMessages
						.getMessage("widgets.view.button.feedback.check"),
						Notification.Type.TRAY_NOTIFICATION);

			}
		});
		lLayout.addComponent(lCheckbox);
	}

	private class Listener implements Button.ClickListener {
		private final String message;

		Listener(final String inMessage) {
			message = inMessage;
		}

		@Override
		public void buttonClick(final ClickEvent inEvent) {
			Notification.show(message, Notification.Type.TRAY_NOTIFICATION);
		}
	}

}
