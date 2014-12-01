/**
 * 
 */
package org.ripla.web.internal.menu;

/**
 * A simple glob filter helper.
 *
 * @author lbenno
 */
public final class MenuFilter {
	private static final String WILDCARD = "*";

	private MenuFilter() {
		// prevent instantiation
	}

	/**
	 * Checks the tag for filtering.
	 * 
	 * @param inPattern
	 *            String the glob pattern, e.g. <code>demo.*</code>. May by
	 *            <code>null</code> or empty to disable filtering.
	 * @param inTag
	 *            String the menu tag for filtering, may be <code>null</code> or
	 *            empty (to match every filter)
	 * @return boolean <code>true</code> if the tag passed the specified filter
	 *         pattern
	 */
	public static boolean checkTagFilter(String inPattern, String inTag) {
		if (inPattern == null || inPattern.isEmpty()) {
			// filtering disabled
			return true;
		}
		if (inTag == null) {
			// if filtering enabled, no tag does not match at all
			return false;
		}
		if (inTag.isEmpty()) {
			// if filtering enabled, an empty tag matches every filter
			return true;
		}
		if (inPattern.contains(WILDCARD)) {
			return inTag.startsWith(inPattern.replace(WILDCARD, ""));
		} else {
			return inPattern.equals(inTag);
		}
	}

}
