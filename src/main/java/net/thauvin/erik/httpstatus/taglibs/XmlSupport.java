/*
 * XmlSupport.java
 *
 * Copyright (c) 2015-2016, Erik C. Thauvin (erik@thauvin.net)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *   Neither the name of this project nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.thauvin.erik.httpstatus.taglibs;

import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Adds support for the <code>default</code> and <code>escapeXml</code> tag attributes.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
public abstract class XmlSupport extends SimpleTagSupport
{
	/**
	 * Default value string.
	 */
	protected String defaultValue;

	/**
	 * Escape XML flag.
	 */
	protected boolean escapeXml = true;

	/**
	 * Sets the default value.
	 *
	 * @param defaultValue The default value.
	 */
	@SuppressWarnings("unused")
	public void setDefault(final String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	/**
	 * Sets the {@link net.thauvin.erik.httpstatus.Utils#escapeXml(String) xml} flag.
	 *
	 * @param escapeXml <code>true</code> or <code>false</code>
	 */
	@SuppressWarnings("unused")
	public void setEscapeXml(final boolean escapeXml)
	{
		this.escapeXml = escapeXml;
	}
}