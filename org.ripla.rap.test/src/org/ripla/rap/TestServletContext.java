package org.ripla.rap;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * <p>
 * <strong>IMPORTANT:</strong> This class is <em>not</em> part the public RAP
 * API. It may change or disappear without further notice. Use this class at
 * your own risk.
 * </p>
 */
public class TestServletContext implements ServletContext {
	private final Map<String, Object> initParameters;
	private final Map<String, Object> attributes;
	private final Map<String, FilterRegistration> filters;
	private final Map<String, ServletRegistration> servlets;
	private String servletContextName;
	private TestLogger logger;
	private int majorVersion;
	private int minorVersion;

	public TestServletContext() {
		this.initParameters = new HashMap<String, Object>();
		this.attributes = new HashMap<String, Object>();
		this.filters = new HashMap<String, FilterRegistration>();
		this.servlets = new HashMap<String, ServletRegistration>();
	}

	public void setVersion(final int majorVersion, final int minorVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public void setLogger(final TestLogger logger) {
		this.logger = logger;
	}

	@Override
	public ServletContext getContext(final String arg0) {
		return null;
	}

	@Override
	public int getMajorVersion() {
		return majorVersion;
	}

	@Override
	public int getMinorVersion() {
		return minorVersion;
	}

	@Override
	public String getMimeType(final String arg0) {
		return null;
	}

	@Override
	public Set<String> getResourcePaths(final String arg0) {
		return null;
	}

	@Override
	public URL getResource(final String arg0) throws MalformedURLException {
		return null;
	}

	@Override
	public InputStream getResourceAsStream(final String arg0) {
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(final String arg0) {
		return null;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(final String arg0) {
		return null;
	}

	@Override
	public Servlet getServlet(final String arg0) throws ServletException {
		return null;
	}

	@Override
	public Enumeration<Servlet> getServlets() {
		return null;
	}

	@Override
	public Enumeration<String> getServletNames() {
		return null;
	}

	@Override
	public void log(final String arg0) {
		log(arg0, null);
	}

	@Override
	public void log(final Exception arg0, final String arg1) {
		log(arg1, arg0);
	}

	@Override
	public void log(final String arg0, final Throwable arg1) {
		if (logger != null) {
			logger.log(arg0, arg1);
		}
	}

	@Override
	public String getRealPath(final String path) {
		return path;
		// return Fixture.WEB_CONTEXT_DIR + path;
	}

	@Override
	public String getServerInfo() {
		return null;
	}

	@Override
	public String getInitParameter(final String name) {
		return (String) initParameters.get(name);
	}

	@Override
	public boolean setInitParameter(final String name, final String value) {
		initParameters.put(name, value);
		return true;
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return null;
	}

	@Override
	public Object getAttribute(final String arg0) {
		return attributes.get(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return new Enumeration<String>() {
			Iterator<String> iterator = attributes.keySet().iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public String nextElement() {
				return iterator.next();
			}
		};
	}

	@Override
	public void setAttribute(final String arg0, final Object arg1) {
		// ThemeManagerHelper.adaptApplicationContext(arg1);
		// attributes.put(arg0, arg1);
	}

	@Override
	public void removeAttribute(final String arg0) {
		attributes.remove(arg0);
	}

	@Override
	public String getServletContextName() {
		return servletContextName;
	}

	public void setServletContextName(final String servletContextName) {
		this.servletContextName = servletContextName;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public int getEffectiveMajorVersion() {
		return 0;
	}

	@Override
	public int getEffectiveMinorVersion() {
		return 0;
	}

	@Override
	public Dynamic addServlet(final String servletName, final String className) {
		return null;
	}

	@Override
	public Dynamic addServlet(final String servletName, final Servlet servlet) {
		return null;
		// final TestServletRegistration result = new TestServletRegistration(
		// servletName, servlet);
		// servlets.put(servletName, result);
		// return result;
	}

	@Override
	public Dynamic addServlet(final String servletName,
			final Class<? extends Servlet> servletClass) {
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(final Class<T> clazz)
			throws ServletException {
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(final String servletName) {
		return servlets.get(servletName);
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return servlets;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
			final String filterName, final String className) {
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
			final String filterName, final Filter filter) {
		return null;
		// final TestFilterRegistration result = new TestFilterRegistration(
		// filterName, filter);
		// filters.put(filterName, result);
		// return result;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
			final String filterName, final Class<? extends Filter> filterClass) {
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(final Class<T> clazz)
			throws ServletException {
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(final String filterName) {
		return filters.get(filterName);
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return filters;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return null;
	}

	@Override
	public void setSessionTrackingModes(
			final Set<SessionTrackingMode> sessionTrackingModes) {
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return null;
	}

	@Override
	public void addListener(final String className) {
	}

	@Override
	public <T extends EventListener> void addListener(final T t) {
	}

	@Override
	public void addListener(final Class<? extends EventListener> listenerClass) {
	}

	@Override
	public <T extends EventListener> T createListener(final Class<T> clazz)
			throws ServletException {
		return null;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		return null;
	}

	@Override
	public void declareRoles(final String... roleNames) {
	}

}
