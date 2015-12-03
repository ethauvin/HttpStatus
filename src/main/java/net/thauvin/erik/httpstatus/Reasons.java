/*
 * Reasons.java
 *
 * Copyright (c) 2015 Erik C. Thauvin (http://erik.thauvin.net/)
 * All rights reserved.
 */
package net.thauvin.erik.httpstatus;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * The <code>Reasons</code> class.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-02
 * @since 1.0
 */
public class Reasons
{
	/**
	 * The reason phrases map.
	 */
	private static final Map<String, String> REASON_PHRASES = new TreeMap<String, String>();

	/**
	 * Gets the reason phrase for the specified status code.
	 *
	 * @param statusCode The status code.
	 *
	 * @return The reason phrase, or <code>null</code>.
	 */
	public static String getReasonPhrase(int statusCode)
	{
		return getReasonPhrase(Integer.toString(statusCode));
	}

	/**
	 * Gets the reason phrase for the specified status code.
	 *
	 * @param statusCode The status code.
	 *
	 * @return The reason phrase, or <code>null</code>.
	 */
	public static String getReasonPhrase(String statusCode)
	{
		return REASON_PHRASES.get(statusCode);
	}

	/**
	 * Outputs the status codes and reason phrases.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args)
	{
		for (final Map.Entry<String, String> entry : REASON_PHRASES.entrySet())
		{
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	/**
	 * Initializes the reason phrases map.
	 */
	static
	{
		final ResourceBundle bundle = ResourceBundle.getBundle("net.thauvin.erik.httpstatus.reasons");
		for (final String key : bundle.keySet())
		{
			REASON_PHRASES.put(key, bundle.getString(key));
		}
	}
}