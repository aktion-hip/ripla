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
package org.ripla.util;

/**
 * Instances of this class mark a position in an extendible menu where
 * contributions can be attached to, either before or after.
 * 
 * @author Luthiger
 */
public final class ExtendibleMenuMarker {
	private final transient String markerID;

	/**
	 * ExtendibleMenuMarker constructor.
	 * 
	 * @param inMarkerID
	 *            String the marker's identification
	 */
	public ExtendibleMenuMarker(final String inMarkerID) {
		markerID = inMarkerID;
	}

	public String getMarkerID() {
		return markerID;
	}

	// ---

	public enum PositionType {
		APPEND, INSERT_BEFORE, INSERT_AFTER;
	}

	/**
	 * Usage e.g.:
	 * 
	 * <pre>
	 * new Position(PositionType.APPEND, &quot;menuStart&quot;);
	 * </pre>
	 * 
	 */
	public static class Position {
		private final transient PositionType type;
		private final transient String markerID;

		public Position(final PositionType inType, final String inMarkerID) {
			type = inType;
			markerID = inMarkerID;
		}

		public PositionType getType() {
			return type;
		}

		public String getMarkerID() {
			return markerID;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((markerID == null) ? 0 : markerID.hashCode());
			result = prime * result + ((type == null) ? 0 : type.name().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Position other = (Position) obj;
			if (markerID == null) {
				if (other.markerID != null) {
					return false;
				}
			} else if (!markerID.equals(other.markerID)) {
				return false;
			}
			if (!type.name().equals(other.type.name())) {
				return false;
			}
			return true;
		}
	}

}
