package org.ripla.rap;

/**
 * <p>
 * <strong>IMPORTANT:</strong> This class is <em>not</em> part the public RAP
 * API. It may change or disappear without further notice. Use this class at
 * your own risk.
 * </p>
 */
public interface TestLogger {

	void log(String message, Throwable throwable);

}