package com.pa.main;

import java.util.LinkedList;

import org.polinux.http.HttpServerRequest;
import org.polinux.http.HttpServerResponse;
import org.polinux.http.polinux.servlet.PolinuxHttpServlet;

public class HomeServlet extends PolinuxHttpServlet {

	public HomeServlet() {
	}

	@Override
	public void GET(HttpServerRequest arg0, HttpServerResponse arg1) {
		arg1.getWriter().println(
				"<p>HELLOTHISISMYONLYRESPONSe HELLOTHISISMYONLYRESPONSe HELLOTHISISMYONLYRESPONSe HELLOTHISISMYONLYRESPONSe</p><br><br><p>Parameters:</p><br>"
						+ arg0.getParameters());

		Object o = new LinkedList<HttpServerRequest>();

		
		String s = (String) o;
		
		arg1.getWriter().print(s);
	}

	@Override
	public void POST(HttpServerRequest arg0, HttpServerResponse arg1) {
		arg1.getWriter().println(
				"<p>HELLOTHISISMYONLYRESPONSe HELLOTHISISMYONLYRESPONSe HELLOTHISISMYONLYRESPONSe HELLOTHISISMYONLYRESPONSe</p><br><br><p>Parameters:</p><br>"
						+ arg0.getParameters());
	}

	@Override
	public void init() {
	}

//	@Override
//	public void onLoad() {
//	}

}
