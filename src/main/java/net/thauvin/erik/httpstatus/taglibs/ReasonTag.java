/*
 * ReasonTag.java
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
package net.thauvin.erik.httpstatus.taglibs;

import net.thauvin.erik.httpstatus.Reasons;
import net.thauvin.erik.httpstatus.Utils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * The <code>&lt;hs:reason&gt;</code> tag returns the Reason Phrase for the current (or specified) HTTP Status Error
 * Code.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-02
 * @since 1.0
 */
public class ReasonTag extends XmlSupport
{
	private int statusCode;

	@Override
	public void doTag()
			throws JspException, IOException
	{
		final PageContext pageContext = (PageContext) getJspContext();
		final JspWriter out = pageContext.getOut();

		try
		{
			if (statusCode >= 0)
			{
				Utils.outWrite(out, Reasons.getReasonPhrase(statusCode), defaultValue, escapeXml);
			}
			else
			{
				Utils.outWrite(out,
				               Reasons.getReasonPhrase(pageContext.getErrorData().getStatusCode()),
				               defaultValue,
				               escapeXml);
			}
		}
		catch (IOException ignore)
		{
			// Ignore.
		}
	}

	/**
	 * Sets the status code.
	 *
	 * @param statusCode The status code.
	 */
	@SuppressWarnings("unused")
	public void setCode(final int statusCode)
	{
		this.statusCode = statusCode;
	}


}
