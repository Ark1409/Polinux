package org.polinux.http.servlet;

import java.net.URI;

import org.polinux.http.HttpHeader;
import org.polinux.http.HttpServer;
import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpServerResponse;

/**
 * Represents an {@code HTTP} Servlet inside an {@link HttpServer}. Servlets are
 * used to create server-side code / processes for an application
 * 
 * @see HttpServer
 */
public interface HttpServlet extends Servlet {
	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#GET HTTP GET} request. {@code GET}
	 * requests represent a default browser request when accessing a {@link URI}.
	 * This is the most known / used {@code HTTP} request method.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 * @see #POST(HttpServerRequest, HttpServerResponse)
	 */
	public void GET(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#HEAD HTTP HEAD} request. {@code HEAD}
	 * requests are used to view the {@link HttpHeader headers} that will be
	 * returned to the browser in an
	 * {@link #GET(HttpServerRequest, HttpServerResponse) HTTP GET Request}.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 * @see #GET(HttpServerRequest, HttpServerResponse)
	 */
	public void HEAD(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#POST HTTP POST} request. {@code POST}
	 * requests represent a default browser request used when sending personal user
	 * information over to the server (e.g. passwords, form submissions, etc). This
	 * is one of the most used {@code HTTP} request method.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 * @see #GET(HttpServerRequest, HttpServerResponse)
	 */
	public void POST(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#PUT HTTP PUT} request. {@code PUT}
	 * requests are used to create a new resource or replaces a representation of
	 * the target resource with the contents of the browser request.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 * @see #POST(HttpServerRequest, HttpServerResponse)
	 */
	public void PUT(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#DELETE HTTP DELETE} request.
	 * {@code DELETE} requests are used to delete the resource at the request
	 * {@link URI}.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 */
	public void DELETE(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#OPTIONS HTTP OPTIONS} request.
	 * {@code OPTIONS} requests are used to identify the
	 * {@link HttpServerRequest.RequestMethod HTTP Requests Methods} that are
	 * allowed to be used at the {@link URI request URI}.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 */
	public void OPTIONS(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod#OPTIONS HTTP OPTIONS} request.
	 * {@code TRACE} requests are used to perform a message loop-back test along the
	 * path to the {@link URI request URI}, providing a useful debugging mechanism.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 */
	public void TRACE(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Forwards the underlying {@link HttpServerRequest request} and
	 * {@link HttpServerResponse response} to the proper
	 * {@link HttpServerRequest.RequestMethod HTTP Request Method} implementation
	 * inside this object. {@link #METHOD(HttpServerRequest, HttpServerResponse)} is
	 * called if the {@link HttpServerRequest#getMethod() request method} is not
	 * existing inside this object.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 * @see #METHOD(HttpServerRequest, HttpServerResponse)
	 */
	public void service(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Retrieves the name of the {@code HTTP Servlet}.
	 * 
	 * @return The name of the servlet.
	 */
	public String getName();

	/**
	 * Initializes the {@code HTTP Servlet}. This method may be ignored by
	 * sub-classes. This method can be set to do nothing.
	 */
	public void init();
}
