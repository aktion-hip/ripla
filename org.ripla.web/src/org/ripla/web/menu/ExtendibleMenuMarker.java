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
package org.ripla.web.menu;

/**
 * Instances of this class mark a position in an extendible menu where contributions can be attached to,
 * either before or after.
 * 
 * @author Luthiger
 */
public class ExtendibleMenuMarker {
	private String markerID;

	/**
	 * ExtendibleMenuMarker constructor.
	 * 
	 * @param inMarkerID String the marker's identification
	 */
	public ExtendibleMenuMarker(String inMarkerID) {
		markerID = inMarkerID;
	}
	
	public String getMarkerID() {
		return markerID;
	}

// ---
	
	public enum PositionType {
		APPEND, INSERT_BEFORE, INSERT_AFTER;
	}

	public static class Position {
		private PositionType type;
		private String markerID;

		public Position(PositionType inType, String inMarkerID) {
			type = inType;
			markerID = inMarkerID;
		}
		public PositionType getType() {
			return type;
		}
		public String getMarkerID() {
			return markerID;
		}
	}
	
}
