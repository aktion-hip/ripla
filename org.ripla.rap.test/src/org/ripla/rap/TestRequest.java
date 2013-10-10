package org.ripla.rap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * <p>
 * <strong>IMPORTANT:</strong> This class is <em>not</em> part the public RAP
 * API. It may change or disappear without further notice. Use this class at
 * your own risk.
 * </p>
 */
public final class TestRequest implements HttpServletRequest {

	private static final String DEFAULT_SCHEME = "http";
	public static final String DEFAULT_REQUEST_URI = "/fooapp/rap";
	public static final String DEFAULT_CONTEX_PATH = "/fooapp";
	public static final String DEFAULT_SERVER_NAME = "fooserver";
	public static final String DEFAULT_SERVLET_PATH = "/rap";
	public static final int PORT = 8080;

	private final StringBuffer requestURL;
	private HttpSession session;
	private String scheme;
	private String serverName;
	private final String contextPath;
	private String requestURI;
	private String servletPath;
	private String pathInfo;
	private final Map<String, String[]> parameters;
	private final Map<String, String> headers;
	private final Map<String, Object> attributes;
	private final Collection<Cookie> cookies;
	private String contentType;
	private String body;
	private String method;
	private Locale[] locales;

	public TestRequest() {
		requestURL = new StringBuffer();
		scheme = DEFAULT_SCHEME;
		serverName = DEFAULT_SERVER_NAME;
		contextPath = DEFAULT_CONTEX_PATH;
		requestURI = DEFAULT_REQUEST_URI;
		servletPath = DEFAULT_SERVLET_PATH;
		parameters = new HashMap<String, String[]>();
		headers = new HashMap<String, String>();
		attributes = new HashMap<String, Object>();
		cookies = new LinkedList<Cookie>();
	}

	@Override
	public String getAuthType() {
		return null;
	}

	public void addCookie(final Cookie cookie) {
		cookies.add(cookie);
	}

	@Override
	public Cookie[] getCookies() {
		return cookies.toArray(new Cookie[cookies.size()]);
	}

	@Override
	public long getDateHeader(final String arg0) {
		return 0;
	}

	@Override
	public String getHeader(final String arg0) {
		return headers.get(arg0);
	}

	public void setHeader(final String arg0, final String arg1) {
		headers.put(arg0, arg1);
	}

	@Override
	public Enumeration<String> getHeaders(final String arg0) {
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return new Enumeration<String>() {
			private final Iterator iterator = headers.keySet().iterator();

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
	public int getIntHeader(final String arg0) {
		return 0;
	}

	@Override
	public String getMethod() {
		return method;
	}

	public void setMethod(final String method) {
		this.method = method;
	}

	@Override
	public String getPathInfo() {
		return pathInfo;
	}

	public void setPathInfo(final String pathInfo) {
		this.pathInfo = pathInfo;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public String getQueryString() {
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public boolean isUserInRole(final String arg0) {
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(final String requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		return requestURL;
	}

	@Override
	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(final String servletPath) {
		this.servletPath = servletPath;
	}

	@Override
	public HttpSession getSession(final boolean arg0) {
		return session;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public Object getAttribute(final String arg0) {
		return attributes.get(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public void setCharacterEncoding(final String arg0)
			throws UnsupportedEncodingException {
	}

	@Override
	public int getContentLength() {
		return body != null ? body.length() : 0;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final StringReader reader = new StringReader(body);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return reader.read();
			}
		};
	}

	@Override
	public String getParameter(final String arg0) {
		final String[] value = parameters.get(arg0);
		String result = null;
		if (value != null) {
			result = value[0];
		}
		return result;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return new Enumeration<String>() {
			private final Iterator iterator = parameters.keySet().iterator();

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
	public String[] getParameterValues(final String arg0) {
		return parameters.get(arg0);
	}

	public void setParameter(final String key, final String value) {
		if (value == null) {
			parameters.remove(key);
		} else {
			parameters.put(key, new String[] { value });
		}
	}

	public void addParameter(final String key, final String value) {
		if (parameters.containsKey(key)) {
			final String[] values = parameters.get(key);
			final String[] newValues = new String[values.length + 1];
			System.arraycopy(values, 0, newValues, 0, values.length);
			newValues[values.length] = value;
			parameters.put(key, newValues);
		} else {
			setParameter(key, value);
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameters;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public String getScheme() {
		return scheme;
	}

	public void setScheme(final String scheme) {
		this.scheme = scheme;
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	public void setServerName(final String serverName) {
		this.serverName = serverName;
	}

	@Override
	public int getServerPort() {
		return PORT;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new StringReader(body != null ? body : ""));
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public void setAttribute(final String arg0, final Object arg1) {
		attributes.put(arg0, arg1);
	}

	@Override
	public void removeAttribute(final String arg0) {
	}

	@Override
	public Locale getLocale() {
		return locales == null || locales.length == 0 ? Locale.getDefault()
				: locales[0];
	}

	@Override
	public Enumeration<Locale> getLocales() {
		Locale[] returnedLocales = locales;
		if (locales == null || locales.length == 0) {
			returnedLocales = new Locale[] { Locale.getDefault() };
		}
		final Iterator<Locale> iterator = Arrays.asList(returnedLocales)
				.iterator();
		return new Enumeration<Locale>() {

			@Override
			public Locale nextElement() {
				return iterator.next();
			}

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}
		};
	}

	public void setLocales(final Locale... locales) {
		this.locales = locales;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(final String arg0) {
		return null;
	}

	@Override
	public String getRealPath(final String arg0) {
		return null;
	}

	public void setSession(final HttpSession session) {
		this.session = session;
	}

	@Override
	public String getLocalAddr() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLocalPort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRemotePort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(final ServletRequest servletRequest,
			final ServletResponse servletResponse) throws IllegalStateException {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}

	@Override
	public boolean authenticate(final HttpServletResponse response)
			throws IOException, ServletException {
		return false;
	}

	@Override
	public void login(final String username, final String password)
			throws ServletException {
	}

	@Override
	public void logout() throws ServletException {
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public Part getPart(final String name) throws IOException, ServletException {
		return null;
	}
}