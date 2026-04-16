/*
 * ReasonTag.java
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

import jakarta.servlet.jsp.PageContext;
import net.thauvin.erik.httpstatus.Reasons;
import net.thauvin.erik.httpstatus.Utils;

import java.io.IOException;

/**
 * Outputs the HTTP status Reason Phrase for either the explicitly supplied
 * status code or, when none is provided, the status code associated with the
 * current error being processed by the JSP container.
 *
 * <p>If no reason phrase is available, the tag prints the configured default
 * value. XML escaping is applied when enabled.</p>
 *
 * @author Erik C. Thauvin
 * @created 2015-12-02
 * @since 1.0
 */
public class ReasonTag extends XmlSupport {

    private int statusCode = -1;

    /**
     * Writes the Reason Phrase for the current or specified HTTP status code.
     * If the page is not processing an error and no explicit code is set,
     * the default value is used instead.
     *
     * @throws IOException If an error occurs while writing output
     */
    @Override
    public void doTag() throws IOException {
        var pageContext = (PageContext) getJspContext();
        var out = pageContext.getOut();

        String reason;

        if (statusCode >= 0) {
            reason = Reasons.getReasonPhrase(statusCode);
        } else {
            var errorData = pageContext.getErrorData();
            var code = (errorData != null) ? errorData.getStatusCode() : -1;
            reason = (code >= 0) ? Reasons.getReasonPhrase(code) : null;
        }

        Utils.outWrite(out, reason, defaultValue, escapeXml);
    }

    /**
     * Sets the HTTP status code to be used when resolving the Reason Phrase.
     */
    public void setCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
