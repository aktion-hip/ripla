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
package org.ripla.web.internal.menu;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.useradmin.Authorization;
import org.ripla.web.interfaces.IMenuCommand;
import org.ripla.web.interfaces.IMenuExtendible;
import org.ripla.web.interfaces.IMenuItem;
import org.ripla.web.menu.ExtendibleMenuMarker;
import org.ripla.web.menu.ExtendibleMenuMarker.Position;
import org.ripla.web.services.IExtendibleMenuContribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Menu factory for an extendible menu.
 *  
 * @author Luthiger
 * Created: 29.10.2011
 */
public class ExtendibleMenuFactory extends MenuFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ExtendibleMenuFactory.class);
	
	private IMenuExtendible menu;
	private List<IExtMenuItem> contributions = new Vector<ExtendibleMenuFactory.IExtMenuItem>();

	/**
	 * Constructor
	 * 
	 * @param inMenu {@link IVIFMenuExtendible} the extendible menu to process
	 * @param inContributions Collection of {@link IExtendibleMenuContribution}s the set of contributions to the extendible menu
	 */
	public ExtendibleMenuFactory(IMenuExtendible inMenu, Collection<IExtendibleMenuContribution> inContributions) {
		super(inMenu);
		menu = inMenu;
		initializePositions(inMenu.getMarkers(), inContributions);
	}
	
	private void initializePositions(ExtendibleMenuMarker[] inMarkers, Collection<IExtendibleMenuContribution> inContributions) {
		for (ExtendibleMenuMarker lMarker : inMarkers) {
			contributions.add(new MarkerItem(lMarker.getMarkerID()));
		}
		for (IExtendibleMenuContribution lContribution : inContributions) {
			Position lPosition = lContribution.getPosition();
			switch (lPosition.getType()) {
			case APPEND:
				appendTo(lPosition.getMarkerID(), lContribution);
				break;
			case INSERT_BEFORE:
				insert(lPosition.getMarkerID(), lContribution, 0);
				break;
			case INSERT_AFTER:				
				insert(lPosition.getMarkerID(), lContribution, 1);
				break;
			}
		}
	}

	private void insert(String inMarkerID, IExtendibleMenuContribution inContribution, int inInsertOffset) {
		IExtMenuItem lItem = find(inMarkerID);
		if (lItem == null) {
			LOG.error("Can't find ID {}!", inMarkerID); //$NON-NLS-1$
			throw new IllegalArgumentException("Can't find ID " + inMarkerID); //$NON-NLS-1$
		}
		
		int lIndex = contributions.indexOf(lItem);
		if (lIndex >= 0) {
			contributions.add(lIndex + inInsertOffset, new ContributionAdapter(inContribution));
		}
	}

	private IExtMenuItem find(String inMarkerID) {
		for (IExtMenuItem lItem : contributions) {
			String lItemID = lItem.getMarkerID();
			if (lItemID.equalsIgnoreCase(inMarkerID)) {
				return lItem;
			}
		}
		return null;
	}

	private void appendTo(String inMarkerID, IExtendibleMenuContribution inContribution) {
		int i;
		Iterator<IExtMenuItem> lItems = contributions.iterator();
		for (i = 0; lItems.hasNext(); i++) {
			IExtMenuItem lItem = lItems.next();
			if (lItem.isMarker()) {
				String lID = lItem.getMarkerID();
				if (lID.equalsIgnoreCase(inMarkerID)) {
					i++;
					for (; lItems.hasNext(); i++) {
						IExtMenuItem lNextItem = lItems.next();
						if (lNextItem.isMarker()) {
							break;
						}
					}
					contributions.add(i-1, new ContributionAdapter(inContribution));
					return;
				}
			}
		}
	}

	@Override
	public MenuItem createMenu(MenuBar inMenuBar, Map<Integer, IMenuCommand> inMap, Command inCommand, Authorization inAuthorization) {
		if (!checkPermissions(menu.getPermission(), inAuthorization)) return null;
		
		MenuItem outItem = inMenuBar.addItem(menu.getLabel(), null, inCommand);
		
		boolean lFirst = true;
		for (IExtMenuItem lItem : contributions) {
			if (lItem.isMarker()) {
				continue;
			}
			
			final IExtendibleMenuContribution lContribution = lItem.getContribution();
			
			//check the permission the item needs to be displayed
			if (!checkPermissions(lContribution.getPermission(), inAuthorization)) {
				continue;
			}
			
			MenuItem lMenuItem = outItem.addItem(lContribution.getLabel(), null, inCommand);
			addCommand(inMap, lMenuItem, createMenuCommand(lContribution.getControllerName()));
			if (lFirst) {
				//clicking the menu has the same effect as clicking the first sub menu item
				addCommand(inMap, outItem, createMenuCommand(lContribution.getControllerName()));
				lFirst = false;
			}
			
			//process sub menu
			List<IMenuItem> lSubMenu = lContribution.getSubMenu();
			if (!lSubMenu.isEmpty()) {
				createSubMenu(lSubMenu, lMenuItem, inMap, inCommand, inAuthorization);
			}
		}
		return outItem;
	}
	
// --- inner classes ---
	
	private static interface IExtMenuItem {
		boolean isMarker();
		String getMarkerID();
		IExtendibleMenuContribution getContribution();
	}
	
	private static class MarkerItem implements IExtMenuItem {
		private String markerID;

		public MarkerItem(String inMarkerID) {
			markerID = inMarkerID;
		}
		@Override
		public boolean isMarker() {
			return true;
		}
		@Override
		public String getMarkerID() {
			return markerID;
		}
		@Override
		public IExtendibleMenuContribution getContribution() {
			return null;
		}
	}
	
	private static class ContributionAdapter implements IExtMenuItem {
		private IExtendibleMenuContribution contribution;
		
		public ContributionAdapter(IExtendibleMenuContribution inContribution) {
			contribution = inContribution;
		}
		@Override
		public boolean isMarker() {
			return false;
		}
		@Override
		public String getMarkerID() {
			return ""; //$NON-NLS-1$
		}
		@Override
		public IExtendibleMenuContribution getContribution() {
			return contribution;
		}
	}

	@Override
	public String getProviderSymbolicName() {
		return FrameworkUtil.getBundle(contributions.get(0).getContribution().getClass()).getSymbolicName();
	}

}
