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

import org.ripla.interfaces.IMessages;
import org.ripla.web.Activator;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Displays a popup window for a dialog. Usage:
 * 
 * <pre>
 * DialogWindow lDialog = Dialog.openQuestion(&quot;Warning&quot;, &quot;Do you ...?&quot;,
 * 		new Dialog.ICommand() {
 * 		});
 * Button lButton = new Button(&quot;Button with confirmation&quot;);
 * lButton.addListener(Dialog.createClickListener(lDialog, this));
 * </pre>
 * 
 * @author Luthiger
 */
public final class Dialog {

	private Dialog() {
	}

	/**
	 * Creates a dialog window with two buttons (yes / no).
	 * 
	 * @param inTitle
	 *            String the dialog window's title
	 * @param inQuestion
	 *            String the question
	 * @return {@link DialogWindow}
	 */
	public static DialogWindow openQuestion(final String inTitle,
			final String inQuestion) {
		return new DialogWindow(inTitle, inQuestion);
	}

	private static HorizontalLayout createButtons(final Button inButton1,
			final Button inButton2) {
		final HorizontalLayout outButtons = new HorizontalLayout();
		outButtons.setStyleName("ripla-buttons"); //$NON-NLS-1$
		outButtons.setSpacing(true);
		outButtons.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);
		outButtons.addComponent(inButton1);
		outButtons.addComponent(inButton2);
		outButtons.setExpandRatio(inButton2, 1);
		outButtons.setComponentAlignment(inButton2, Alignment.MIDDLE_LEFT);
		return outButtons;
	}

	/**
	 * Creates a dialog window with two buttons (yes / no).<br />
	 * This dialog adds click listeners to the buttons. The <code>NO</code>
	 * listener closes the dialog window. The <code>YES</code> listener closes
	 * the dialog window and executes the passed <code>ICommand</code>.
	 * 
	 * @param inTitle
	 *            String the dialog window's title
	 * @param inQuestion
	 *            String the question
	 * @param inCommand
	 *            {@link ICommand} the command to execute if the yes-button is
	 *            clicked
	 * @return
	 */
	@SuppressWarnings("serial")
	public static DialogWindow openQuestion(final String inTitle,
			final String inQuestion, final ICommand inCommand) {
		final DialogWindow out = new DialogWindow(inTitle, inQuestion);
		out.addNoListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				out.setVisible(false);
			}
		});
		out.addYesListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				out.setVisible(false);
				inCommand.execute();
			}
		});
		return out;
	}

	/**
	 * Creates a <code>Button.ClickListener</code> that displays the passed
	 * <code>DialogWindow</code>. Usage:
	 * 
	 * <pre>
	 * Button lButton = new Button(&quot;Button with confirmation&quot;);
	 * lButton.addListener(Dialog.createClickListener(lDialog, this));
	 * </pre>
	 * 
	 * @param inDialog
	 *            {@link DialogWindow}
	 * @param inComponent
	 *            {@link Component}
	 * @return {@link ClickListener}
	 */
	@SuppressWarnings("serial")
	public static Button.ClickListener createClickListener(
			final DialogWindow inDialog, final Component inComponent) {
		return new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				UI.getCurrent().addWindow(inDialog);
				inDialog.center();
				inDialog.setVisible(true);
			}
		};
	}

	// ---

	/**
	 * Interface for commands to execute if the dialog's <code>YES</code> button
	 * is clicked.
	 * 
	 * @author Luthiger Created: 24.11.2011
	 */
	public interface ICommand {
		/**
		 * Execute the command
		 */
		void execute();
	}

	/**
	 * A dialog window with title, dialog and two click buttons.
	 */
	@SuppressWarnings("serial")
	public static class DialogWindow extends Window {
		private final transient Button buttonYes;
		private final transient Button buttonNo;

		/**
		 * Set constructor private
		 */
		DialogWindow(final String inTitle, final String inQuestion) {
			super(inTitle);
			final IMessages lMessages = Activator.getMessages();
			final VerticalLayout lLayout = new VerticalLayout();
			setContent(lLayout);
			lLayout.setMargin(true);
			lLayout.setSpacing(true);
			lLayout.setSizeUndefined();

			lLayout.addComponent(new Label(inQuestion));
			buttonYes = new Button(
					lMessages.getMessage("dialog.button.lbl.yes")); //$NON-NLS-1$
			buttonYes.setClickShortcut(KeyCode.ENTER);
			buttonNo = new Button(lMessages.getMessage("dialog.button.lbl.no")); //$NON-NLS-1$
			buttonNo.setClickShortcut(KeyCode.ESCAPE);
			lLayout.addComponent(createButtons(buttonYes, buttonNo));
			setModal(true);
		};

		/**
		 * Closes the window, can be called when the parent window is detached.
		 */
		@Override
		public void close() {
			UI.getCurrent().removeWindow(this);
		}

		/**
		 * Sets the dialog's visibility.
		 * 
		 * @param inVisible
		 *            boolean <code>true</code> makes the existing dialog window
		 *            visible, <code>false</code> makes the visible window
		 *            invisible
		 */
		@Override
		public void setVisible(final boolean inVisible) {
			setVisible(inVisible);
		}

		/**
		 * @param inListener
		 *            {@link ClickListener} adds the <code>yes</code> button's
		 *            click listener
		 */
		public void addYesListener(final Button.ClickListener inListener) {
			buttonYes.addClickListener(inListener);
		}

		/**
		 * @param inListener
		 *            {@link ClickListener} adds the <code>no</code> button's
		 *            click listener
		 */
		public void addNoListener(final Button.ClickListener inListener) {
			buttonNo.addClickListener(inListener);
		}
	}

}
