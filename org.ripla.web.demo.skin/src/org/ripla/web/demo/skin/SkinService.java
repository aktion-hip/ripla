package org.ripla.web.demo.skin;

import org.ripla.services.ISkin;
import org.ripla.services.ISkinService;

/**
 * The service to create a demo skin.
 * 
 * @author Luthiger
 */
public class SkinService implements ISkinService {
	public static final String SKIN_ID = "org.ripla.web.demo.skin";

	@Override
	public String getSkinID() {
		return SKIN_ID;
	}

	@Override
	public String getSkinName() {
		return "Ripla Demo Skin (Reindeer)";
	}

	@Override
	public ISkin createSkin() {
		return new Skin();
	}

}
