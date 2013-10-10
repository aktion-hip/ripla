package org.ripla.rap.demo.widgets.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;

/**
 * The model for country data.
 * 
 * @author Luthiger
 */
public final class CountryBean { // NOPMD by Luthiger on 06.09.12 23:53
	private static final NumberFormat FORMAT = new DecimalFormat("#,##0"); //$NON-NLS-1$ // NOPMD by Luthiger on 06.09.12 23:55
	private static final String SEPARATOR = ";"; //$NON-NLS-1$

	private final String[] values;

	private CountryBean(final String inLine) {
		final String[] lData = inLine.split(SEPARATOR);
		values = new String[lData.length];
		values[0] = lData[0];
		values[1] = lData[1].trim();
		values[2] = lData[2];
		values[3] = lData[3];
		values[4] = format(lData[4]);
		values[5] = format(lData[5]);
		values[6] = format(lData[6]);
		values[7] = format(lData[7]);
		values[8] = format(lData[8]);
		values[9] = format(lData[9]);
		values[10] = format(lData[10]);
		values[11] = format(lData[11]);
		values[12] = format(lData[12]);
		values[13] = format(lData[13]);
		values[14] = format(lData[14]);
		values[15] = format(lData[15]);
		values[16] = format(lData[16]);
		values[17] = format(lData[17]);
		values[18] = format(lData[18]);
		values[19] = format(lData[19]);
		values[20] = format(lData[20]);
		values[21] = format(lData[21]);
		values[22] = format(lData[22]);
		values[23] = format(lData[23]);
		values[24] = format(lData[24]);
		values[25] = format(lData[25]);
		values[26] = format(lData[26]);
	}

	private String format(final String inValue) {
		final long lValue = Long.parseLong(inValue);
		return FORMAT.format(lValue);
	}

	/**
	 * Factory method
	 * 
	 * @param inLine
	 *            String the input data, ';' separated
	 * @return {@link CountryBean}
	 */
	public static CountryBean createItem(final String inLine) {
		return new CountryBean(inLine);
	}

	/**
	 * @return the pop2100
	 */
	public String getPop2100() {
		return values[26];
	}

	/**
	 * @return the pop2095
	 */
	public String getPop2095() {
		return values[25];
	}

	/**
	 * @return the pop2090
	 */
	public String getPop2090() {
		return values[24];
	}

	/**
	 * @return the pop2085
	 */
	public String getPop2085() {
		return values[23];
	}

	/**
	 * @return the pop2080
	 */
	public String getPop2080() {
		return values[22];
	}

	/**
	 * @return the pop2075
	 */
	public String getPop2075() {
		return values[21];
	}

	/**
	 * @return the pop2070
	 */
	public String getPop2070() {
		return values[20];
	}

	/**
	 * @return the pop2065
	 */
	public String getPop2065() {
		return values[19];
	}

	/**
	 * @return the pop2060
	 */
	public String getPop2060() {
		return values[18];
	}

	/**
	 * @return the pop2055
	 */
	public String getPop2055() {
		return values[17];
	}

	/**
	 * @return the pop2050
	 */
	public String getPop2050() {
		return values[16];
	}

	/**
	 * @return the pop2045
	 */
	public String getPop2045() {
		return values[15];
	}

	/**
	 * @return the pop2040
	 */
	public String getPop2040() {
		return values[14];
	}

	/**
	 * @return the pop2035
	 */
	public String getPop2035() {
		return values[13];
	}

	/**
	 * @return the pop2030
	 */
	public String getPop2030() {
		return values[12];
	}

	/**
	 * @return the pop2025
	 */
	public String getPop2025() {
		return values[11];
	}

	/**
	 * @return the pop2020
	 */
	public String getPop2020() {
		return values[10];
	}

	/**
	 * @return the pop2015
	 */
	public String getPop2015() {
		return values[9];
	}

	/**
	 * @return the pop2010
	 */
	public String getPop2010() {
		return values[8];
	}

	/**
	 * @return the pop2005
	 */
	public String getPop2005() {
		return values[7];
	}

	/**
	 * @return the pop2000
	 */
	public String getPop2000() {
		return values[6];
	}

	/**
	 * @return the pop1995
	 */
	public String getPop1995() {
		return values[5];
	}

	/**
	 * @return the pop1990
	 */
	public String getPop1990() {
		return values[4];
	}

	/**
	 * @return the sresRegion
	 */
	public String getSresRegion() {
		return values[3];
	}

	/**
	 * @return the unRegion11
	 */
	public String getUnRegion11() {
		return values[2];
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return values[1];
	}

	/**
	 * @return the unCode
	 */
	public String getUnCode() {
		return values[0];
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", getName(), getSresRegion()); //$NON-NLS-1$
	}

	/**
	 * @param inColumnIndex
	 *            int
	 * @return String the bean's value in the specified column
	 */
	public String getValue(final int inColumnIndex) {
		return values[inColumnIndex];
	}

	/**
	 * @return String the country information formatted as html
	 */
	public String toHtml() {
		final IMessages lMessages = Activator.getMessages();
		final StringBuilder out = new StringBuilder();
		out.append("<h2>").append(getName()).append("</h2>");
		out.append("<table>");
		appendRow(out, lMessages.getMessage("widgets.view.code.un"),
				getUnCode());
		appendRow(out, lMessages.getMessage("widgets.view.region.un11"),
				getUnRegion11());
		appendRow(out, lMessages.getMessage("widgets.view.region.sres4"),
				getSresRegion());
		out.append("<td colspan=2>")
				.append(lMessages.getMessage("widgets.view.label"))
				.append("</td>");
		appendRow(out, "1990", getPop1990());
		appendRow(out, "1995", getPop1995());
		appendRow(out, "2000", getPop2000());
		appendRow(out, "2005", getPop2005());
		appendRow(out, "2010", getPop2010());
		appendRow(out, "2015", getPop2015());
		appendRow(out, "2020", getPop2020());
		appendRow(out, "2025", getPop2025());
		appendRow(out, "2030", getPop2030());
		appendRow(out, "2035", getPop2035());
		appendRow(out, "2040", getPop2040());
		appendRow(out, "2045", getPop2045());
		appendRow(out, "2050", getPop2050());
		appendRow(out, "2055", getPop2055());
		appendRow(out, "2060", getPop2060());
		appendRow(out, "2065", getPop2065());
		appendRow(out, "2070", getPop2070());
		appendRow(out, "2075", getPop2075());
		appendRow(out, "2080", getPop2080());
		appendRow(out, "2085", getPop2085());
		appendRow(out, "2090", getPop2090());
		appendRow(out, "2095", getPop2095());
		appendRow(out, "2100", getPop2100());
		out.append("</table>");
		return new String(out);
	}

	private StringBuilder appendRow(final StringBuilder inAppend,
			final String inVal1, final String inVal2) {
		inAppend.append("<tr>");
		inAppend.append("<td><em>").append(inVal1).append("</em></td>");
		inAppend.append("<td><strong>").append(inVal2).append("</strong></td>");
		inAppend.append("</tr>");
		return inAppend;
	}

}
