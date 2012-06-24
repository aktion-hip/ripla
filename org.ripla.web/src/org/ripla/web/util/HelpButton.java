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

package org.ripla.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.ripla.web.Activator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 * <p>UI component that displays a <code>[Help]</code> link button.
 * When clicked, a window containing help text is displayed.
 * </p>
 * Usage:<pre>URL lHelpContent = this.getClass().getClassLoader().getResource("help.html");
 *layout.addComponent(new HelpButton("Help", lHelpContent, 700, 600));</pre>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class HelpButton extends CustomComponent {
	private static final Logger LOG = LoggerFactory.getLogger(HelpButton.class);
	
	private final static String NL = System.getProperty("line.separator"); //$NON-NLS-1$
	
	private Map<URL, String> helpCache = new HashMap<URL, String>();
	
	/**
	 * HelpButton constructor.
	 * 
	 * @param inCaption String the link button's caption
	 * @param inHelpContent URL the file's url containing the html formatted help text to display
	 * @param inWidth int the popup windos's width
	 * @param inHeight int the popup windos's height
	 */
	public HelpButton(String inCaption, URL inHelpContent, int inWidth, int inHeight) {
		HorizontalLayout lLayout = new HorizontalLayout();
		lLayout.setStyleName("ripla-help"); //$NON-NLS-1$
		setCompositionRoot(lLayout);
		setWidth(SIZE_UNDEFINED, 0);
		
		lLayout.setWidth(SIZE_UNDEFINED, 0);
		Label lLabel = new Label("["); //$NON-NLS-1$
		RiplaViewHelper.makeUndefinedWidth(lLabel);
		lLayout.addComponent(lLabel);
		lLayout.addComponent(createLinkButton(inCaption, inHelpContent, inWidth, inHeight));
		lLabel = new Label("]"); //$NON-NLS-1$
		RiplaViewHelper.makeUndefinedWidth(lLabel);
		lLayout.addComponent(lLabel);
	}
	
	private Button createLinkButton(String inCaption, final URL inHelpContent, final int inWidth, final int inHeight) {
		Button outLink = new Button(inCaption);
		outLink.setStyleName(BaseTheme.BUTTON_LINK);
		outLink.addListener(new Button.ClickListener() {				
			public void buttonClick(ClickEvent inEvent) {
				HelpWindow lHelpWindow = new HelpWindow(Activator.getMessages().getMessage("help.window.title"),  //$NON-NLS-1$
						getHelpText(inHelpContent), inWidth, inHeight);
				if (lHelpWindow.getParent() == null) {
					getWindow().addWindow(lHelpWindow.getHelpWindow());
				}
				lHelpWindow.setPosition(50, 50);
			}
		});
		return outLink;
	}
	
	private String getHelpText(URL inHelpContent) {
		String out = helpCache.get(inHelpContent);
		if (out == null) {
			try {
				out = readHelpContent(inHelpContent);
			} 
			catch (IOException exc) {
				LOG.error("Problem reading from {}!", inHelpContent, exc); //$NON-NLS-1$
				out = String.format("<p>%s</p>", Activator.getMessages().getMessage("help.errormsg.readmsg")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			helpCache.put(inHelpContent, out);
		}
		return out;
	}
	
	private String readHelpContent(URL inHelpContent) throws IOException {
		StringBuilder outHtml = new StringBuilder();
		InputStream lStream = null;
		InputStreamReader lReader = null;
		BufferedReader lBuffer = null;
		try {
			lStream = inHelpContent.openStream();
			lReader = new InputStreamReader(lStream);
			lBuffer = new BufferedReader(lReader);
			String lLine;
			while ((lLine = lBuffer.readLine()) != null) {
				outHtml.append(lLine).append(NL);
			}			
		}
		finally {
			if (lBuffer != null) {
				try {lBuffer.close();} catch (IOException exc) {}
			}
			if (lReader != null) {
				try {lReader.close();} catch (IOException exc) {}
			}
			if (lStream != null) {
				try {lStream.close();} catch (IOException exc) {}
			}
		}
		return new String(outHtml);
	}
	
// ---

	private static class HelpWindow extends VerticalLayout {
		private Window helpWindow;

		HelpWindow(String inCaption, String inHelpText, int inWidth, int inHeight) {
			setSpacing(true);
			helpWindow = new Window(inCaption);
			helpWindow.addStyleName("ripla-lookup"); //$NON-NLS-1$
			helpWindow.setWidth(inWidth, UNITS_PIXELS);
			helpWindow.setHeight(inHeight, UNITS_PIXELS);

			VerticalLayout lLayout = (VerticalLayout)helpWindow.getContent();
			lLayout.setMargin(true);
			lLayout.setSpacing(true);
			lLayout.setSizeFull();
			lLayout.setStyleName("ripla-view"); //$NON-NLS-1$
			lLayout.addComponent(new Label(inHelpText, Label.CONTENT_XHTML));
			
			Button lClose = new Button(Activator.getMessages().getMessage("lookup.window.button.close"),  //$NON-NLS-1$
					new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent inEvent) {
					(helpWindow.getParent()).removeWindow(helpWindow);
				}
			});
			lLayout.addComponent(lClose);
			lLayout.setComponentAlignment(lClose, Alignment.BOTTOM_RIGHT);
		}
		
		Window getHelpWindow() {
			return helpWindow;
		}

		void setPosition(int inPositionX, int inPositionY) {
			helpWindow.setPositionX(inPositionX);
			helpWindow.setPositionX(inPositionY);
		}
	}		

}
