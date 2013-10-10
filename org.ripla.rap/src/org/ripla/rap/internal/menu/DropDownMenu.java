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

import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.service.useradmin.Authorization;
import org.ripla.interfaces.IMenuCommand;
import org.ripla.interfaces.IMenuItem;
import org.ripla.rap.Constants;
import org.ripla.rap.util.GridLayoutHelper;

/**
 * A drop down menu composite.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class DropDownMenu extends Composite {

	private ToolItem mainMenuItem;
	private final IMenuCommand defaultCmd;
	private final Composite menuBox;

	/**
	 * DropDownMenu constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inItemCreator
	 *            {@link IMenuItemsCreator} the object responsible to create
	 *            this menu's items
	 */
	public DropDownMenu(final Composite inParent,
			final IMenuItemsCreator inItemCreator) {
		super(inParent, SWT.NONE);
		setData(RWT.CUSTOM_VARIANT, "ripla-menu-dropdown");
		setLayout(GridLayoutHelper.createGridLayout());

		menuBox = GridLayoutHelper.createComposite(this);
		menuBox.setData(RWT.CUSTOM_VARIANT, "ripla-menu-box");

		final Menu lMenu = createMenu(this);
		defaultCmd = inItemCreator.create(lMenu);
		createDropDownToolItem(menuBox, lMenu, inItemCreator.getMenuConfig());
	}

	/**
	 * @return {@link IMenuCommand} this menu's default command, i.e. the first
	 *         one that is visible
	 */
	public IMenuCommand getDefaultCmd() {
		return defaultCmd;
	}

	private Menu createMenu(final Composite inParent) {
		final Menu outMenu = new Menu(inParent.getShell(), SWT.POP_UP);
		outMenu.setData(RWT.CUSTOM_VARIANT, Constants.CSS_MENU);
		setLayout(GridLayoutHelper.createGridLayout());
		return outMenu;
	}

	private void createDropDownToolItem(final Composite inParent,
			final Menu inPullDown, final IMenuItem inMenu) {
		final ToolBar lToolBar = new ToolBar(inParent, SWT.HORIZONTAL);
		lToolBar.setData(RWT.CUSTOM_VARIANT, Constants.CSS_MENU);

		mainMenuItem = new ToolItem(lToolBar, SWT.DROP_DOWN);
		mainMenuItem.setData(RWT.CUSTOM_VARIANT, Constants.CSS_MENU);
		mainMenuItem.setText(inMenu.getLabel());
		mainMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				final int lOffsetY = (inEvent.detail == SWT.ARROW) ? 0
						: lToolBar.getSize().y - 1;
				inPullDown.setLocation(lToolBar.toDisplay(inEvent.x, inEvent.y
						+ lOffsetY));
				inPullDown.setVisible(true);
			}
		});
	}

	/**
	 * Factory method for <code>IMenuItemsCreator</code> instance.
	 * 
	 * @param inMenuConfig
	 * @param inListener
	 * @param inAuthorization
	 * @return {@link IMenuItemsCreator}
	 */
	public static IMenuItemsCreator getItemsCreator(
			final IMenuItem inMenuConfig, final SelectionListener inListener,
			final Authorization inAuthorization) {
		return new MenuItemsCreator(inMenuConfig, inListener, inAuthorization);
	}

	/**
	 * Sets this menu's marker as selected.
	 * 
	 * @param inSelected
	 *            boolean <code>true</code> if menu should be marked selected,
	 *            <code>false</code> for deactivated
	 */
	public void setSelected(final boolean inSelected) {
		menuBox.setData(RWT.CUSTOM_VARIANT,
				inSelected ? "ripla-menu-box-active" : "ripla-menu-box");
		mainMenuItem.setData(RWT.CUSTOM_VARIANT,
				inSelected ? Constants.CSS_MENU_ACTIVE : Constants.CSS_MENU);
	}

	// ---

	/**
	 * Interface for classes responsible to create the menu's items.
	 * 
	 * @author Luthiger
	 */
	public interface IMenuItemsCreator {

		/**
		 * Create the menu item's for the specified menu.
		 * 
		 * @param inMenu
		 *            {@link Menu}
		 * @return {@link IMenuCommand} this menu's default command (i.e. the
		 *         first one)
		 */
		IMenuCommand create(Menu inMenu);

		/**
		 * @return {@link IMenuItem} the menu configuration for the menu
		 *         creation
		 */
		IMenuItem getMenuConfig();
	}

	protected static class MenuItemsCreator implements IMenuItemsCreator {
		private final IMenuItem menuConfig;
		private final Authorization authorization;
		private final SelectionListener listener;
		private IMenuCommand defaultCmd;

		protected MenuItemsCreator(final IMenuItem inMenuConfig,
				final SelectionListener inListener,
				final Authorization inAuthorization) {
			menuConfig = inMenuConfig;
			listener = inListener;
			authorization = inAuthorization;
		}

		@Override
		public IMenuCommand create(final Menu inMenu) {
			for (final IMenuItem lItemConfig : menuConfig.getSubMenu()) {
				createMenuItem(inMenu, lItemConfig, authorization);
			}
			return defaultCmd;
		}

		@Override
		public IMenuItem getMenuConfig() {
			return menuConfig;
		}

		protected SelectionListener getListener() {
			return listener;
		}

		protected void createMenuItem(final Menu inMenu,
				final IMenuItem inMenuConfig,
				final Authorization inAuthorization) {
			// item is created only if permissions are ok
			if (MenuFactory.checkPermissions(inMenuConfig.getPermission(),
					inAuthorization)) {
				final List<IMenuItem> lSubMenuItems = inMenuConfig.getSubMenu();
				final int lStyle = lSubMenuItems.isEmpty() ? SWT.PUSH
						: SWT.CASCADE;
				final MenuItem lItem = new MenuItem(inMenu, lStyle | SWT.LEFT);
				lItem.setText(inMenuConfig.getLabel());
				lItem.setData(RWT.CUSTOM_VARIANT, Constants.CSS_MENU_ITEM);
				lItem.addSelectionListener(listener);
				lItem.setData(MenuFactory.KEY_MENU_ACTION,
						createMenuCommand(inMenuConfig.getControllerName()));
				if (defaultCmd == null) {
					defaultCmd = createMenuCommand(inMenuConfig
							.getControllerName());
				}

				// create nested sub menu
				if (!lSubMenuItems.isEmpty()) {
					final Menu lSubMenu = new Menu(inMenu);
					lItem.setMenu(lSubMenu);
					for (final IMenuItem lItemConfig : lSubMenuItems) {
						createMenuItem(lSubMenu, lItemConfig, inAuthorization);
					}
				}
			}
		}

		protected IMenuCommand createMenuCommand(final String inControllerName) {
			return new IMenuCommand() {
				@Override
				public String getControllerName() {
					return inControllerName;
				}
			};
		}
	}

}
