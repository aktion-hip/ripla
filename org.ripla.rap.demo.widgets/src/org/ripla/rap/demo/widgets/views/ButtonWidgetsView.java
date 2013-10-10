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

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.util.ImageUtil;
import org.ripla.rap.util.LabelHelper;
import org.ripla.rap.util.NotificationHelper;

/**
 * The view to display the Vaadin button widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class ButtonWidgetsView extends AbstractWidgetsView {

	/**
	 * @param inParent
	 */
	public ButtonWidgetsView(final Composite inParent) {
		super(inParent);
		final IMessages lMessages = Activator.getMessages();
		createTitle(lMessages.getMessage("widgets.title.page.button"));
		createSubTitle(lMessages
				.getMessage("widgets.view.button.subtitle.button"));

		Button lButton = new Button(this, SWT.PUSH);
		lButton.setText(lMessages.getMessage("widgets.view.button.label.save"));
		lButton.setToolTipText(lMessages
				.getMessage("widgets.view.button.descr.normal"));
		lButton.addSelectionListener(new ClickAdapter(lMessages
				.getMessage("widgets.view.button.feedback.normal"), this));

		createSubTitle(lMessages.getMessage("widgets.view.button.label.image"));
		lButton = new Button(this, SWT.PUSH);
		lButton.setImage(ImageUtil.getImage(inParent.getDisplay(),
				ButtonWidgetsView.class, "synced.png"));
		lButton.setText(lMessages.getMessage("widgets.view.button.label.save"));
		lButton.setToolTipText(lMessages
				.getMessage("widgets.view.button.label.image"));
		lButton.addSelectionListener(new ClickAdapter(lMessages
				.getMessage("widgets.view.button.feedback.image"), this));

		createSubTitle(lMessages
				.getMessage("widgets.view.button.subtitle.disable"));
		LabelHelper.createLabel(this,
				lMessages.getMessage("widgets.view.button.label.disable"),
				"ripla-font");
		final Button lButtonDisable = new Button(this, SWT.PUSH);
		lButtonDisable.setText(lMessages
				.getMessage("widgets.view.button.label.click"));
		lButtonDisable.setToolTipText(lMessages
				.getMessage("widgets.view.button.descr.disable"));
		lButtonDisable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				lButtonDisable.setEnabled(false);
				lButtonDisable.pack();
				final ServerPushSession lPush = new ServerPushSession();
				final Runnable lRunnable = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (final InterruptedException exc) {
							// intentionally left empty
						}
						inParent.getDisplay().asyncExec(new Runnable() {
							@Override
							public void run() {
								if (!lButtonDisable.isDisposed()) {
									lButtonDisable.setEnabled(true);
								}
								NotificationHelper.showNotification(
										lMessages
												.getMessage("widgets.view.button.feedback.disable"),
										getShell());
							}
						});
						lPush.stop();
					}
				};
				lPush.start();
				final Thread lThread = new Thread(lRunnable);
				lThread.setDaemon(true);
				lThread.start();
			}
		});

		createSubTitle(lMessages
				.getMessage("widgets.view.button.subtitle.link"));
		LabelHelper.createLabel(this,
				lMessages.getMessage("widgets.view.button.label.link"),
				"ripla-font");
		final Link lLink = new Link(this, SWT.NONE);
		lLink.setText(lMessages.getMessage("widgets.view.button.label.linked"));
		lLink.addSelectionListener(new ClickAdapter(lMessages
				.getMessage("widgets.view.button.feedback.link"), this));

		createSubTitle(lMessages
				.getMessage("widgets.view.button.subtitle.check"));
		LabelHelper.createLabel(this,
				lMessages.getMessage("widgets.view.button.label.check"),
				"ripla-font");
		lButton = new Button(this, SWT.CHECK);
		lButton.setText(lMessages.getMessage("widgets.view.button.label.click"));
		lButton.setToolTipText(lMessages
				.getMessage("widgets.view.button.descr.normal"));
		lButton.addSelectionListener(new ClickAdapter(lMessages
				.getMessage("widgets.view.button.feedback.check"), this));

	}

	private static class ClickAdapter extends SelectionAdapter {
		private final String message;
		private final Composite parent;

		protected ClickAdapter(final String inMessage, final Composite inParent) {
			message = inMessage;
			parent = inParent;
		}

		@Override
		public void widgetSelected(final SelectionEvent inEvent) {
			NotificationHelper.showNotification(message, parent);
		}
	}

}
