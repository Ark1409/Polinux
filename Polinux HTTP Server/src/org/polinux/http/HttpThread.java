package org.polinux.http;

import org.polinux.exceptions.http.HttpRuntimeException;
/**
 * Represents a thread related to an {@link HttpServer HTTP server}.
 */
public interface HttpThread extends Runnable {
	/**
	 * Terminates the http thread.
	 * <p>
	 * Execution of this method may cause some errors inside code of the http thread
	 * (e.g. responses and request are immediately canceled), so it is a good idea
	 * to make sure everything is safe and can be closed before the execution of
	 * this method.
	 * 
	 * @throws HttpRuntimeException If an error occurs inside the http thread.
	 */
	public void abort() throws HttpRuntimeException;

	/**
	 * Closes the current http thread. After execution of this method, this instance
	 * may be disposed of (should <i>never</i> be used again).
	 * 
	 * @throws HttpRuntimeException if an I/O error occurs when closing the http
	 *                              thread or if another error occurs.
	 */
	public void close() throws HttpRuntimeException;

	/**
	 * Starts the http thread.
	 * <p>
	 * Execution of this method is preferred over {@link #run()}, for this method
	 * prepare all necessary items before execution of the thread.
	 * 
	 * @throws HttpRuntimeException If the http thread has already been started or
	 *                              if another error occurs.
	 */
	public void execute() throws HttpRuntimeException;

	/**
	 * 
	 * Invokes the run method. {@link #execute()} should be invoked rather than this
	 * method, since the starts the server "raw-ly" (a new thread is not initiated).
	 * 
	 * @deprecated {@link #execute()} should be used instead.
	 */
	@Deprecated
	@Override
	public void run();

	/**
	 * Gets whether the http thread is currently enabled.
	 * 
	 * @return {@code True} if the http thread is currently enabled, {@code false}
	 *         otherwise.
	 */
	public boolean isEnabled();
}
