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
package org.ripla.web.internal.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.useradmin.Authorization;
import org.ripla.interfaces.IMenuCommand;
import org.ripla.interfaces.IMenuExtendible;
import org.ripla.interfaces.IMenuItem;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.util.ExtendibleMenuMarker;
import org.ripla.util.ExtendibleMenuMarker.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Menu factory for an extendible menu.
 * 
 * @author Luthiger
 */
public final class ExtendibleMenuFactory extends MenuFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(ExtendibleMenuFactory.class);

	private final transient IMenuExtendible menu;
	private final transient List<IExtMenuItem> contributions = new ArrayList<ExtendibleMenuFactory.IExtMenuItem>();

	/**
	 * Constructor
	 * 
	 * @param inMenu
	 *            {@link IVIFMenuExtendible} the extendible menu to process
	 * @param inContributions
	 *            Collection of {@link IExtendibleMenuContribution}s the set of
	 *            contributions to the extendible menu
	 */
	public ExtendibleMenuFactory(final IMenuExtendible inMenu,
			final Collection<IExtendibleMenuContribution> inContributions) {
		super(inMenu);
		menu = inMenu;
		initializePositions(inMenu.getMarkers(), getSorted(inContributions));
	}

	private Collection<IExtendibleMenuContribution> getSorted(
			Collection<IExtendibleMenuContribution> inContributions) {
		// distribute all contributions to their positions
		Map<ExtendibleMenuMarker.Position, List<IExtendibleMenuContribution>> helper = new HashMap<ExtendibleMenuMarker.Position, List<IExtendibleMenuContribution>>(
				7);
		for (IExtendibleMenuContribution lContribution : inContributions) {
			Position lPosition = lContribution.getPosition();
			List<IExtendibleMenuContribution> lPositionList = helper
					.get(lPosition);
			if (lPositionList == null) {
				lPositionList = new ArrayList<IExtendibleMenuContribution>();
				helper.put(lPosition, lPositionList);
			}
			lPositionList.add(lContribution);
		}
		// sort each position's list
		ArrayList<IExtendibleMenuContribution> out = new ArrayList<IExtendibleMenuContribution>(
				inContributions.size());
		for (Entry<Position, List<IExtendibleMenuContribution>> lPositionSet : helper
				.entrySet()) {
			switch (lPositionSet.getKey().getType()) {
			case APPEND:
				out.addAll(getWithSort(lPositionSet.getValue(), true));
				break;
			case INSERT_BEFORE:
				out.addAll(getWithSort(lPositionSet.getValue(), true));
				break;
			case INSERT_AFTER:
				out.addAll(getWithSort(lPositionSet.getValue(), false));
				break;
			default:
				out.addAll(getWithSort(lPositionSet.getValue(), true));
			}
		}
		return out;
	}

	private List<IExtendibleMenuContribution> getWithSort(
			List<IExtendibleMenuContribution> inToSort, boolean inAscending) {
		Collections.sort(inToSort, new ContributionComparator(inAscending));
		return inToSort;
	}

	private void initializePositions(final ExtendibleMenuMarker[] inMarkers,
			final Collection<IExtendibleMenuContribution> inContributions) {
		for (final ExtendibleMenuMarker lMarker : inMarkers) {
			contributions.add(new MarkerItem(lMarker.getMarkerID())); // NOPMD
		}
		for (final IExtendibleMenuContribution lContribution : inContributions) {
			final ExtendibleMenuMarker.Position lPosition = lContribution
					.getPosition();
			switch (lPosition.getType()) {
			case INSERT_BEFORE:
				insert(lPosition.getMarkerID(), lContribution, 0);
				break;
			case INSERT_AFTER:
				insert(lPosition.getMarkerID(), lContribution, 1);
				break;
			case APPEND:
			default: // append
				appendTo(lPosition.getMarkerID(), lContribution);
			}
		}
	}

	private void insert(final String inMarkerID,
			final IExtendibleMenuContribution inContribution,
			final int inInsertOffset) {
		final IExtMenuItem lItem = find(inMarkerID);
		if (lItem == null) {
			LOG.error("Can't find ID {}!", inMarkerID); //$NON-NLS-1$
			throw new IllegalArgumentException("Can't find ID " + inMarkerID); //$NON-NLS-1$
		}

		final int lIndex = contributions.indexOf(lItem);
		if (lIndex >= 0) {
			contributions.add(lIndex + inInsertOffset, new ContributionAdapter(
					inContribution));
		}
	}

	private IExtMenuItem find(final String inMarkerID) {
		for (final IExtMenuItem lItem : contributions) {
			final String lItemID = lItem.getMarkerID();
			if (lItemID.equalsIgnoreCase(inMarkerID)) {
				return lItem;
			}
		}
		return null;
	}

	private void appendTo(final String inMarkerID,
			final IExtendibleMenuContribution inContribution) {
		int i; // NOPMD
		final Iterator<IExtMenuItem> lItems = contributions.iterator();
		for (i = 0; lItems.hasNext(); i++) {
			final IExtMenuItem lItem = lItems.next();
			if (lItem.isMarker()) {
				final String lID = lItem.getMarkerID();
				if (lID.equalsIgnoreCase(inMarkerID)) {
					i++;
					for (; lItems.hasNext(); i++) {
						final IExtMenuItem lNextItem = lItems.next();
						if (lNextItem.isMarker()) { // NOPMD by Luthiger
							break;
						}
					}
					contributions.add(i - 1, new ContributionAdapter( // NOPMD
							inContribution));
					return;
				}
			}
		}
	}

	@Override
	public MenuItem createMenu(final MenuBar inMenuBar,
			final Resource inSubMenuIcon,
			final Map<Integer, IMenuCommand> inMap, final Command inCommand,
			final Authorization inAuthorization, String inMenuTagFilter) {
		if (!checkPermissions(menu.getPermission(), inAuthorization)) {
			return null;
		}

		if (!MenuFilter.checkTagFilter(inMenuTagFilter, menu.getTag())) {
			return null;
		}

		final MenuItem outItem = inMenuBar.addItem(menu.getLabel(), null,
				inCommand);
		outItem.setIcon(inSubMenuIcon);

		boolean lFirst = true;
		for (final IExtMenuItem lItem : contributions) {
			if (lItem.isMarker()) {
				continue;
			}

			final IExtendibleMenuContribution lContribution = lItem
					.getContribution();

			// check the permission the item needs to be displayed
			if (!checkPermissions(lContribution.getPermission(),
					inAuthorization)) {
				continue;
			}

			final MenuItem lMenuItem = outItem.addItem(
					lContribution.getLabel(), null, inCommand);
			addCommand(inMap, lMenuItem,
					createMenuCommand(lContribution.getControllerName()));
			if (lFirst) {
				// clicking the menu has the same effect as clicking the first
				// sub menu item
				addCommand(inMap, outItem,
						createMenuCommand(lContribution.getControllerName()));
				lFirst = false;
			}

			// process sub menu
			final List<IMenuItem> lSubMenu = lContribution.getSubMenu();
			if (!lSubMenu.isEmpty()) {
				createSubMenu(lSubMenu, lMenuItem, inMap, inCommand,
						inAuthorization);
			}
		}
		return outItem;
	}

	// --- inner classes ---

	private static class ContributionComparator implements
			Comparator<IExtendibleMenuContribution> {

		private int ascending;

		protected ContributionComparator(boolean inAscending) {
			ascending = inAscending ? 1 : -1;
		}

		@Override
		public int compare(IExtendibleMenuContribution inContribution1,
				IExtendibleMenuContribution inContribution2) {
			return ascending
					* inContribution1.getLabel().compareTo(
							inContribution2.getLabel());
		}

	}

	private interface IExtMenuItem {
		boolean isMarker();

		String getMarkerID();

		IExtendibleMenuContribution getContribution();
	}

	private static class MarkerItem implements IExtMenuItem {
		private final transient String markerID;

		public MarkerItem(final String inMarkerID) {
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
		private final transient IExtendibleMenuContribution contribution;

		public ContributionAdapter(
				final IExtendibleMenuContribution inContribution) {
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
		return FrameworkUtil.getBundle(
				contributions.get(0).getContribution().getClass())
				.getSymbolicName();
	}

}
