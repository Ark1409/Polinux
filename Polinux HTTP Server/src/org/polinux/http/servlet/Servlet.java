package org.polinux.http.servlet;

import java.net.URL;

import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpServerResponse;

/**
 * Represents a general servlet.
 */
public interface Servlet {
	/**
	 * Represents the method that is executed whenever the servlet receive an
	 * {@link HttpServerRequest.RequestMethod} that is not in the list of
	 * {@link HttpServerRequest.RequestMethod known request methods}.
	 * <p>
	 * It should be noted that this method will not also be called if another known
	 * request method can be. This method is always the last resort.
	 * 
	 * @param request  The {@link HttpServerRequest}, containing info about the
	 *                 browser request.
	 * @param response The {@link HttpServerResponse}, which is the object
	 *                 representing the {@link HttpServerResponse response} that
	 *                 will be sent to the server.
	 */
	public void METHOD(HttpServerRequest request, HttpServerResponse response);

	/**
	 * Retrieves all the known {@link URL url patterns} for this servlet.
	 * 
	 * @return An array containing all the {@link URL url patterns} for the servlet.
	 */
	public String[] getUrlPatterns();

}
