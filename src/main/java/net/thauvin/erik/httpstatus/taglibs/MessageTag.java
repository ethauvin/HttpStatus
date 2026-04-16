/*
 * MessageTag.java
 *
 * Copyright 2015-2026 Erik C. Thauvin (erik@thauvin.net)
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

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.jsp.PageContext;
import net.thauvin.erik.httpstatus.Utils;

import java.io.IOException;

/**
 * Outputs the error message associated with the current HTTP status error.
 * If no message is available, the tag prints the configured default value.
 * XML escaping is applied when enabled.
 *
 * <p>This tag is typically used on JSP error pages to display the message
 * supplied by the servlet container or application.</p>
 *
 * @author Erik C. Thauvin
 * @created 2022-03-16
 * @since 1.0.5
 */
public class MessageTag extends XmlSupport {

    /**
     * Writes the error message associated with the current request. If the
     * request is not processing an error, the default value is used instead.
     *
     * @throws IOException If an error occurs while writing output
     */
    @Override
    public void doTag() throws IOException {
        var pageContext = (PageContext) getJspContext();
        var out = pageContext.getOut();

        var message = (String) pageContext.getRequest()
                .getAttribute(RequestDispatcher.ERROR_MESSAGE);

        Utils.outWrite(out, message, defaultValue, escapeXml);
    }
}
