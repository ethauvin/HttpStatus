/*
 * Reasons.java
 *
 * Copyright (c) 2015, Erik C. Thauvin (erik@thauvin.net)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the author nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
	 * The resource bundle base name.
	 */
	public static final String BUNDLE_BASENAME = "net.thauvin.erik.httpstatus.reasons";

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
		final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASENAME);
		for (final String key : bundle.keySet())
		{
			REASON_PHRASES.put(key, bundle.getString(key));
		}
	}
}