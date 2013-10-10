package org.ripla.rap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * <p>
 * <strong>IMPORTANT:</strong> This class is <em>not</em> part the public RAP
 * API. It may change or disappear without further notice. Use this class at
 * your own risk.
 * </p>
 */
public class TestServletOutputStream extends ServletOutputStream {

	private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

	@Override
	public void write(final int bytes) throws IOException {
		stream.write(bytes);
	}

	public ByteArrayOutputStream getContent() {
		return stream;
	}
}