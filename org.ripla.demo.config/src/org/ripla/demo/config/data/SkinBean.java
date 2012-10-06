package org.ripla.demo.config.data;

/**
 * Bean for a skin.
 * 
 * @author Luthiger
 */
public class SkinBean implements Comparable<SkinBean> {
	private final transient String skinID;
	private final transient String name;

	/**
	 * SkinBean constructor.
	 * 
	 * @param inID
	 *            String the skin's id
	 * @param inName
	 *            String the skin's (display) name
	 */
	public SkinBean(final String inID, final String inName) {
		skinID = inID;
		name = inName;
	}

	/**
	 * @return String the skin's id
	 */
	public String getID() {
		return skinID;
	}

	/**
	 * @return String the skin's (display) name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final SkinBean inSkin) {
		return getName().compareTo(inSkin.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skinID == null) ? 0 : skinID.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SkinBean other = (SkinBean) obj;
		if (skinID == null) {
			if (other.skinID != null)
				return false;
		} else if (!skinID.equals(other.skinID))
			return false;
		return true;
	}

}
