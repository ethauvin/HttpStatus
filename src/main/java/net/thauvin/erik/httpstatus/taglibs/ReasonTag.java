/*
 * ReasonTag.java
 *
 * Copyright (c) 2015 Erik C. Thauvin (http://erik.thauvin.net/)
 * All rights reserved.
 */
package net.thauvin.erik.httpstatus.taglibs;

import net.thauvin.erik.httpstatus.Reasons;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * The <code>ReasonTag</code> class.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-02
 * @since 1.0
 */
public class ReasonTag extends SimpleTagSupport
{
	private int statusCode;
	private String defaultValue;

	@Override
	public void doTag()
			throws JspException
	{
		final PageContext pageContext = (PageContext) getJspContext();
		final JspWriter out = pageContext.getOut();

		try
		{
			try
			{
				if (statusCode > 0)
				{
					out.write(Reasons.getReasonPhrase(statusCode));
				}
				else
				{
					out.write(Reasons.getReasonPhrase(pageContext.getErrorData().getStatusCode()));
				}
			}
			catch(NullPointerException npe)
			{
				if (defaultValue != null)
				{
					out.write(defaultValue);
				}
				else
				{
					out.write("");
				}
			}
		}
		catch (IOException e)
		{
			// Ignore.
		}
	}

	/**
	 * Sets the status code.
	 *
	 * @param statusCode The status code.
	 */
	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	/**
	 * Set the default value.
	 *
	 * @param defaultValue The default value.
	 */
	public void setDefault(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

}