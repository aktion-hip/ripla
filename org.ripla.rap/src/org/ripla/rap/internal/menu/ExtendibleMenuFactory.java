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
package org.ripla.rap.internal.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.useradmin.Authorization;
import org.ripla.interfaces.IMenuExtendible;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.util.ExtendibleMenuMarker;
import org.ripla.util.ExtendibleMenuMarker.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * ExtendibleMenuFactory constructor.
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
		initializePositions(inMenu.getMarkers(), inContributions);
	}

	private void initializePositions(final ExtendibleMenuMarker[] inMarkers,
			final Collection<IExtendibleMenuContribution> inContributions) {
		for (final ExtendibleMenuMarker lMarker : inMarkers) {
			contributions.add(new MarkerItem(lMarker.getMarkerID())); // NOPMD
		}
		for (final IExtendibleMenuContribution lContribution : inContributions) {
			final Position lPosition = lContribution.getPosition();
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
		int i; // NOPMD by Luthiger on 09.09.12 23:37
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
	public DropDownMenu createMenu(final Composite inMenuBar,
			final Image inIcon, final SelectionListener inListener,
			final Authorization inAuthorization) {
		if (checkPermissions(menu.getPermission(), inAuthorization)) {
			return new DropDownMenuExt(inMenuBar,
					DropDownMenuExt.getItemsCreator(menu, contributions,
							inListener, inAuthorization));
		}
		return null;
	}

	// --- inner classes ---

	public interface IExtMenuItem {
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
