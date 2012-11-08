package org.ripla.demo.skin.modest;

import org.ripla.web.services.ISkin;
import org.ripla.web.util.FooterHelper;
import org.ripla.web.util.LabelHelper;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * The modest demo skin.
 * 
 * @author Luthiger
 */
public class Skin implements ISkin {
	public static final String SKIN_ID = "org.ripla.demo.skin.modest";

	private final transient HorizontalLayout menuBarComponent;
	private final transient HorizontalLayout menuBarLayout;

	public Skin() {
		menuBarLayout = new HorizontalLayout();
		menuBarLayout.setWidth("100%"); //$NON-NLS-1$
		menuBarLayout.setHeight(39, Sizeable.UNITS_PIXELS);
		menuBarLayout.setStyleName("modest-menubar");

		menuBarComponent = new HorizontalLayout();
		menuBarComponent.setStyleName("ripla-menubar"); //$NON-NLS-1$
		menuBarComponent.setWidth("100%"); //$NON-NLS-1$
		menuBarComponent.setHeight(39, Sizeable.UNITS_PIXELS);

		menuBarComponent.addComponent(menuBarLayout);
		menuBarComponent.setExpandRatio(menuBarLayout, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getSkinID()
	 */
	@Override
	public String getSkinID() {
		return SKIN_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getSkinName()
	 */
	@Override
	public String getSkinName() {
		return "Modest Demo Skin (Chameleon)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hasHeader()
	 */
	@Override
	public boolean hasHeader() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getHeader()
	 */
	@Override
	public Component getHeader(final String inAppName) {
		final HorizontalLayout out = new HorizontalLayout();
		out.setStyleName("demo-header"); //$NON-NLS-1$
		out.setWidth("100%"); //$NON-NLS-1$
		out.setHeight(65, Sizeable.UNITS_PIXELS);

		final Label lTitle = LabelHelper.createLabel(
				"Ripla Demo [with modest skin]", "demo-header-text");
		lTitle.setSizeUndefined();
		out.addComponent(lTitle);

		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hasFooter()
	 */
	@Override
	public boolean hasFooter() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getFooter()
	 */
	@Override
	public Component getFooter() {
		final FooterHelper out = FooterHelper
				.createFooter(FooterHelper.DFT_FOOTER_TEXT);
		out.setHeight(19);
		out.setStyleName("demo-footer");
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hasToolBar()
	 */
	@Override
	public boolean hasToolBar() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hatMenuBar()
	 */
	@Override
	public boolean hasMenuBar() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getToolbarSeparator()
	 */
	@Override
	public Label getToolbarSeparator() {
		final Label outSeparator = new Label("|", Label.CONTENT_XHTML); //$NON-NLS-1$
		outSeparator.setSizeUndefined();
		return outSeparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getMenuBarLayout()
	 */
	@Override
	public HorizontalLayout getMenuBarLayout() {
		return menuBarLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getMenuBarComponent()
	 */
	@Override
	public HorizontalLayout getMenuBarComponent() {
		return menuBarComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getSubMenuIcon()
	 */
	@Override
	public Resource getSubMenuIcon() {
		return null;
	}

}
