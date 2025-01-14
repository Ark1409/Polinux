package org.polinux.http.polinux.servlet;

import org.polinux.configuration.servlet.PolinuxHttpServletConfiguration;
import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpServerResponse;
import org.polinux.http.servlet.HttpServlet;

public abstract class PolinuxHttpServlet implements HttpServlet {
	protected PolinuxHttpServletConfiguration configuration;

	/**
	 * Constructs a {@code PolinuxHttpServlet}. Sub-classes of this class must have
	 * an empty constructor in order for the servlet to be able to be called.
	 */
	public PolinuxHttpServlet() {
		// Default Constructor. Must exist (sub-class must have public empty
		// constructor).
	}

	public void init(PolinuxHttpServletConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void service(HttpServerRequest request, HttpServerResponse response) {
		String method = request.getMethod();

		switch (method) {
		case HttpServerRequest.RequestMethod.HEAD:
			HEAD(request, response);
			break;
		case HttpServerRequest.RequestMethod.OPTIONS:
			OPTIONS(request, response);
			break;
		case HttpServerRequest.RequestMethod.TRACE:
			TRACE(request, response);
			break;
		case HttpServerRequest.RequestMethod.GET:
			GET(request, response);
			break;
		case HttpServerRequest.RequestMethod.POST:
			POST(request, response);
			break;
		case HttpServerRequest.RequestMethod.PUT:
			PUT(request, response);
			break;
		case HttpServerRequest.RequestMethod.DELETE:
			DELETE(request, response);
			break;
		default:
			METHOD(request, response);
			break;
		}

		return;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void HEAD(HttpServerRequest request, HttpServerResponse response) {
		HttpServerRequest req = request;
		HttpServerResponse res = response;
		GET(req, res);
		if (!res.containsHeader("Content-Length"))
			res.setHeader("Content-Length", String.valueOf(res.getWriter().toString().getBytes().length));
		((PolinuxHttpServerResponse) res).getWriter().clearContent();
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void OPTIONS(HttpServerRequest request, HttpServerResponse response) {

		HttpServerRequest req = request;
		HttpServerResponse res = response;

		String allowString = "OPTIONS";

		GET(req, res);

		if (res.getStatus() != HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED) {
			allowString += ", GET";
		}

		((PolinuxHttpServerResponse) res).reset();

//

		POST(req, res);

		if (res.getStatus() != HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED) {
			allowString += ", POST";
		}

		((PolinuxHttpServerResponse) res).reset();

//

		PUT(req, res);

		if (res.getStatus() != HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED) {
			allowString += ", PUT";
		}

		((PolinuxHttpServerResponse) res).reset();

//
		HEAD(req, res);

		if (res.getStatus() != HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED) {
			allowString += ", HEAD";
		}

		((PolinuxHttpServerResponse) res).reset();

//

		TRACE(req, res);

		if (res.getStatus() != HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED) {
			allowString += ", TRACE";
		}

		((PolinuxHttpServerResponse) res).reset();

//

		DELETE(req, res);

		if (res.getStatus() != HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED) {
			allowString += ", DELETE";
		}

		((PolinuxHttpServerResponse) res).reset();

		res.setHeader("Allow", allowString);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void TRACE(HttpServerRequest request, HttpServerResponse response) {
		response.setStatus(HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void METHOD(HttpServerRequest request, HttpServerResponse response) {
		response.setStatus(HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void POST(HttpServerRequest request, HttpServerResponse response) {
		response.setStatus(HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void PUT(HttpServerRequest request, HttpServerResponse response) {
		response.setStatus(HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void DELETE(HttpServerRequest request, HttpServerResponse response) {
		response.setStatus(HttpServerResponse.StatusCode.METHOD_NOT_ALLOWED);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return this.configuration.getServletName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getUrlPatterns() {
		return this.configuration.getServletUrlPatterns();
	}

	/**
	 *
	 */
	public PolinuxHttpServletConfiguration getConfiguration() {
		return configuration;
	}

}
