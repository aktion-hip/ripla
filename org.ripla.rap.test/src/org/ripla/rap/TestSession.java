package org.ripla.rap;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

/**
 * <p>
 * <strong>IMPORTANT:</strong> This class is <em>not</em> part the public RAP
 * API. It may change or disappear without further notice. Use this class at
 * your own risk.
 * </p>
 */
@SuppressWarnings("deprecation")
public class TestSession implements HttpSession {

	private final Map<String, Object> attributes;
	private String id;
	private ServletContext servletContext;
	private boolean isInvalidated;
	private boolean newSession;
	private int maxInactiveInterval;

	public TestSession() {
		attributes = new HashMap<String, Object>();
		servletContext = new TestServletContext();
		id = String.valueOf(hashCode());
	}

	@Override
	public long getCreationTime() {
		return 0;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		if (isInvalidated) {
			final String text = "Unable to obtain session id. Session already invalidated.";
			throw new IllegalStateException(text);
		}
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void setMaxInactiveInterval(final int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getAttribute(final String arg0) {
		return attributes.get(arg0);
	}

	@Override
	public Object getValue(final String arg0) {
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		final Iterator iterator = attributes.keySet().iterator();
		return new Enumeration<String>() {
			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public String nextElement() {
				return (String) iterator.next();
			}
		};
	}

	@Override
	public String[] getValueNames() {
		return null;
	}

	@Override
	public void setAttribute(final String arg0, final Object arg1) {
		if (arg1 instanceof HttpSessionBindingListener) {
			final HttpSessionBindingListener listener = (HttpSessionBindingListener) arg1;
			listener.valueBound(new HttpSessionBindingEvent(this, arg0, arg1));
		}
		attributes.put(arg0, arg1);
	}

	@Override
	public void putValue(final String arg0, final Object arg1) {
	}

	@Override
	public void removeAttribute(final String arg0) {
		final Object removed = attributes.remove(arg0);
		if (removed instanceof HttpSessionBindingListener) {
			final HttpSessionBindingListener listener = (HttpSessionBindingListener) removed;
			final HttpSessionBindingEvent evt = new HttpSessionBindingEvent(
					this, arg0, removed);
			listener.valueUnbound(evt);
		}
	}

	@Override
	public void removeValue(final String arg0) {
	}

	@Override
	public void invalidate() {
		final Object[] keys = attributes.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			final String key = (String) keys[i];
			final Object value = attributes.get(key);
			if (value instanceof HttpSessionBindingListener) {
				final HttpSessionBindingListener listener = (HttpSessionBindingListener) value;
				listener.valueUnbound(new HttpSessionBindingEvent(this, key,
						value));
			}
		}
		attributes.clear();
		isInvalidated = true;
	}

	public boolean isInvalidated() {
		return isInvalidated;
	}

	@Override
	public boolean isNew() {
		return newSession;
	}

	public void setNew(final boolean newSession) {
		this.newSession = newSession;
	}
}
