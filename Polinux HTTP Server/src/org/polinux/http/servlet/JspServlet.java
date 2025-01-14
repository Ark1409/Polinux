package org.polinux.http.servlet;

import java.io.File;
import java.util.List;

public interface JspServlet extends Servlet, java.io.Serializable {
	public File getFile();

	public String getHtml();

	public List<Object> getScriplets();

}
