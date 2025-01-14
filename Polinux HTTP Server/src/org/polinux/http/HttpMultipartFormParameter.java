package org.polinux.http;

/**
 * Represents a form parameter inside an {@link HttpServerRequest} with request
 * {@link HttpServerRequest#getContentType() content-type} of
 * {@value HttpServerResponse.ContentType#FORM_BINARY}. Usually found inside
 * {@code HTTP} {@link HttpServerRequest.RequestMethod#POST POST} requests.
 */
public class HttpMultipartFormParameter extends HttpParameter {
	protected HttpHeader contentDisposition;
	protected HttpHeader contentType;

	public HttpMultipartFormParameter(HttpHeader contentDisposition, HttpHeader contentType, String name,
			String content) {
		super(name, content);
		this.contentDisposition = contentDisposition;
		this.contentType = contentType;
	}

	public HttpHeader getContentDisposition() {
		return contentDisposition;
	}

	public HttpHeader getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		final String sep = "\r\n";
		String asString = "";
		if (contentDisposition != null)
			asString += contentDisposition.toString() + sep;
		if (contentType != null)
			asString += contentType.toString() + sep;
		return (asString += sep + this.value);
	}

}
