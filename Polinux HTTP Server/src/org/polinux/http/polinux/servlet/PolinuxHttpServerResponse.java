package org.polinux.http.polinux.servlet;

import java.io.StringWriter;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.polinux.http.HttpHeader;
import org.polinux.http.HttpResponseCookie;
import org.polinux.http.HttpServer;
import org.polinux.http.HttpServerResponse;
import org.polinux.http.polinux.server.PolinuxHttpClientThread;
import org.polinux.http.polinux.server.PolinuxHttpServer;
import org.polinux.utils.date.TimeUtils;
import org.polinux.utils.enc.CharacterSet;

@SuppressWarnings({ "resource" })
public class PolinuxHttpServerResponse implements HttpServerResponse {
	protected Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	protected String contentType = HttpServerResponse.ContentType.HTML + "; charset="
			+ CharacterSet.name(CharacterSet.UTF_8);

	protected int status = HttpServerResponse.StatusCode.OK;

	protected String statusMessage = HttpServerResponse.StatusCode.getStatusMessage(status);

	protected List<HttpHeader> headers = new LinkedList<HttpHeader>();

	protected List<HttpResponseCookie> cookies = new LinkedList<HttpResponseCookie>();

	protected PolinuxHttpServer server = null;

	protected PolinuxHttpClientThread client = null;

	protected PolinuxHttpServletWriter writer = new PolinuxHttpServletWriter(new StringWriter());

//
//	public PolinuxHttpServerResponse() {
//		// Default constructor. Must exist as public constructor in sub-class.
//	}

	public PolinuxHttpServerResponse(PolinuxHttpClientThread client) {
		this.server = client.getServerThread().getServer();
		this.client = client;
	}

	/** {@inheritDoc} */
	@Override
	public String getContentType() {
		return contentType;
	}

	/** {@inheritDoc} */
	@Override
	public void setContentType(String type) {
		this.contentType = type;
	}

	/** {@inheritDoc} */
	@Override
	public List<HttpHeader> getHeaders() {
		ensureValidHeader();
		return headers;
	}

	private void ensureValidHeader() {
		if (this.headers == null)
			this.headers = new LinkedList<HttpHeader>();
	}

	private void ensureValidCookie() {
		if (this.cookies == null)
			this.cookies = new LinkedList<HttpResponseCookie>();
	}

	/** {@inheritDoc} */
	@Override
	public void setHeader(HttpHeader header) {
		if (header == null)
			return;

		if (header.getHeader() == null)
			return;

		if (header.getValue() == null) {
			removeAllHeaders(header);
			return;
		}

		if (header.getHeader().trim().equalsIgnoreCase("Set-Cookie")) {
			this.addCookie(HttpResponseCookie.parse(header.getValue())[0]);
			return;
		}

		headers.add(header);
	}

	private void removeAllHeaders(String header) {
		this.ensureValidHeader();
		if (header == null)
			return;

		List<HttpHeader> headers = new LinkedList<HttpHeader>();

		for (int i = 0; i < this.getHeaders().size(); i++) {
			HttpHeader h = this.getHeaders().get(i);
			if (h.getHeader().equalsIgnoreCase(header)) {
				headers.add(h);
			}
		}

		for (int i = 0; i < headers.size(); i++) {
			HttpHeader myIndex = headers.get(i);
			this.headers.remove(myIndex);
		}
	}

	private void removeAllHeaders(HttpHeader header) {
		if (header != null)
			this.removeAllHeaders(header.getHeader());
	}

	/** {@inheritDoc} */
	@Override
	public void addCookie(HttpResponseCookie cookie) {
		this.ensureValidCookie();
		if (cookie != null) {
			this.removeCookie(cookie.getName());
			this.cookies.add(cookie);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int getStatus() {
		return this.status;
	}

	/** {@inheritDoc} */
	@Override
	public void setStatus(int status) {
		this.status = status;
		this.setStatusMessage(HttpServerResponse.StatusCode.getStatusMessage(this.status));
	}

	/** {@inheritDoc} */
	@Override
	public String getStatusMessage() {
		return this.statusMessage;
	}

	/** {@inheritDoc} */
	@Override
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/** {@inheritDoc} */
	@Override
	public HttpHeader getHeader(String name) {
		if (name == null)
			return null;

		for (int i = 0; i < this.getHeaders().size(); i++) {
			final HttpHeader h = this.getHeaders().get(i);

			if (h.getHeader().equalsIgnoreCase(name))
				return h;
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public PolinuxHttpServletWriter getWriter() {
		return writer;
	}



	/** {@inheritDoc} */
	@Override
	public PolinuxHttpServer getServer() {
		return this.server;
	}

	/** {@inheritDoc} */
	@Override
	public java.nio.charset.Charset getCharset() {
		this.ensureValidHeader();
		for (int i = 0; i < this.headers.size(); i++) {
			HttpHeader header = this.headers.get(i);
			if (header.getHeader().equalsIgnoreCase("Content-Type")) {
				if (header.getValue().contains(";")) {
					final String[] split = header.getValue().split(";");

					for (int i1 = 0; i1 < split.length; i1++) {
						String item = split[i1].trim();

						if (item.toLowerCase().startsWith("charset") && item.contains("=")) {
							final String[] itemSplit = item.split("=");
							String set;
							try {
								set = itemSplit[1].trim();
							} catch (RuntimeException e) {
								continue;
							}

							return CharacterSet.fromName(set);
						}
					}
				}
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Integer getContentLength() {
//		if (this.getCharset() != null)
//			return this.getWriter().toString().getBytes(this.getCharset()).length;

		return this.getWriter().toString().getBytes().length;
	}

	/** {@inheritDoc} */
	@Override
	public void removeCookie(String name) {
		this.ensureValidCookie();
		int cIndex = -1;

		for (int i = 0; i < this.cookies.size(); i++) {
			HttpResponseCookie c = this.cookies.get(i);
			if (c.getName().equals(name)) {
				cIndex = i;
				break;
			}

		}

		if (cIndex != -1)
			this.cookies.remove(cIndex);
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/** {@inheritDoc} */
	@Override
	public HttpResponseCookie[] getCookies() {
		this.ensureValidCookie();
		return this.cookies.toArray(new HttpResponseCookie[0]);
	}

	/**
	 * Generates a String representing the {@code PolinuxHttpServerResponse}.
	 * 
	 * @param lineSeparator The line separator. Usually {@code \r\n}.
	 * @return The server response, as a string.
	 * 
	 */
	public String asString(final String lineSeparator) {
		return asString(lineSeparator, false);
	}

	/**
	 * Generates a String representing the {@code PolinuxHttpServerResponse}.
	 * 
	 * @param lineSeparator The line separator. Usually {@code \r\n}.
	 * @param addHeaders    Whether to add necessary headers. If set to
	 *                      {@code true}, dispose of this object after use.
	 * @return The server response, as a string.
	 * 
	 */
	public String asString(final String lineSeparator, final boolean addHeaders) {
		final String lineSeperator = lineSeparator;
		String asString = HttpServer.HTTP_VERSION + " " + this.getStatus() + " " + this.getStatusMessage()
				+ lineSeperator;

		this.ensureValidHeader();
		this.ensureValidCookie();

		if (addHeaders)
			this.setDefaultHeaders();

		for (int i = 0; i < this.headers.size(); i++) {
			HttpHeader h = headers.get(i);

			if (h != null) {
				asString += h.toString() + lineSeperator;
			}
		}

		for (int i = 0; i < this.cookies.size(); i++) {
			HttpResponseCookie c = cookies.get(i);

			if (c != null) {
				asString += c.toHttpHeader().toString() + lineSeperator;
			}
		}

		asString += lineSeperator;

		asString += this.getWriter().toString();
		return asString;

	}
	
	

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return asString("\r\n", false);
	}

	/** {@inheritDoc} */
	@Override
	public String[] getHeaderNames() {
		this.ensureValidHeader();
		String[] names = new String[this.getHeaders().size()];

		for (int i = 0; i < this.headers.size(); i++) {
			names[i] = this.headers.get(i).getHeader();
		}

		return names;
	}

	/**
	 * Sets default HTTP headers whenever a response is sent to the client.
	 */
	protected void setDefaultHeaders() {
		final List<HttpHeader> h = new LinkedList<HttpHeader>();

		h.add(new HttpHeader("Cache-Control", "private, no-cache, no-store, must-revalidate, max-age=0"));
		h.add(new HttpHeader("Pragma", "no-cache"));
		h.add(new HttpHeader("Expires", "-1"));
		h.add(new HttpHeader("Server", this.getServer().getName()));
		h.add(new HttpHeader("Content-Type", this.getContentType()));
		h.add(new HttpHeader("Content-Length", this.getContentLength()));
		h.add(new HttpHeader("Date", TimeUtils.getDateHttpHeaderFormat().format(new Date())));

		this.removeHeader("Server");

		List<HttpHeader> addHeaders = new LinkedList<HttpHeader>();

		for (int i = 0; i < h.size(); i++) {
			final HttpHeader header = h.get(i);

			if (this.getHeader(header.getHeader()) != null) {
				addHeaders.add(header);
			}
		}

		this.getHeaders().addAll(addHeaders);
	}
	
//	protected void setDefaultHeaders() {
//		this.ensureValidHeader();
//
//		List<HttpHeader> headers = new LinkedList<HttpHeader>();
//		if (getHeader("Content-Length") == null)
//			headers.add(new HttpHeader("Content-Length", this.getContentLength()));
//
//		if (getHeader("Content-Type") == null)
//			headers.add(new HttpHeader("Content-Type", this.getContentType()));
//
////		if (getHeader("Content-Encoding") == null)
////			headers.add(new HttpHeader("Content-Encoding", "gzip"));
//
//		if (getHeader("Date") == null)
//			headers.add(new HttpHeader("Date", TimeUtils.getDateHttpHeaderFormat().format(new Date())));
//
//		if (this.getHeader("Server") != null)
//			this.removeHeader("Server");
//		headers.add(new HttpHeader("Server", this.getServer().getName()));
//
//		System.out.println("Ablue to do theaders:\r\n" + headers);
//
//		for (int i = 0; i < headers.size(); i++) {
//			this.setHeader(headers.get(i));
//		}
//
//	}

	/**
	 * Resets the current server response to its original state.
	 */
	/* Package private */
	void reset() {
		attributes = new LinkedHashMap<String, Object>();
		contentType = HttpServerResponse.ContentType.HTML + "; charset=" + CharacterSet.name(CharacterSet.UTF_8);
		status = HttpServerResponse.StatusCode.OK;
		statusMessage = HttpServerResponse.StatusCode.getStatusMessage(status);
		headers = new LinkedList<HttpHeader>();
		cookies = new LinkedList<HttpResponseCookie>();
		writer = new PolinuxHttpServletWriter(new StringWriter());
	}

	/** {@inheritDoc} */
	@Override
	public Socket getSocket() {
		return this.client.getSocket();
	}
}
