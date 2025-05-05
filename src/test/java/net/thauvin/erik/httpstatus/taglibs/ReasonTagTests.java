/*
 * ReasonTagTests.java
 *
 * Copyright 2015-2025 Erik C. Thauvin (erik@thauvin.net)
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

import jakarta.servlet.jsp.ErrorData;
import jakarta.servlet.jsp.PageContext;
import net.thauvin.erik.httpstatus.Reasons;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReasonTagTests {
    @Test
    void doTagWithDefaultErrorData() throws IOException {
        var reasonTag = new ReasonTag();
        var code = 404;
        var pageContext = mock(PageContext.class);
        var jspWriter = new MockJspWriter();
        var errorData = mock(ErrorData.class);

        when(pageContext.getOut()).thenReturn(jspWriter);
        when(pageContext.getErrorData()).thenReturn(errorData);
        when(errorData.getStatusCode()).thenReturn(code);

        reasonTag.setJspContext(pageContext);
        reasonTag.doTag();

        assertThat(jspWriter.getContent()).isEqualTo(Reasons.getReasonPhrase(code));
    }

    @Test
    void doTagWithValidStatusCode() throws IOException {
        var reasonTag = new ReasonTag();
        var code = 200;
        var pageContext = mock(PageContext.class);
        var jspWriter = new MockJspWriter();

        reasonTag.setCode(code);

        when(pageContext.getOut()).thenReturn(jspWriter);

        reasonTag.setJspContext(pageContext);
        reasonTag.doTag();

        assertThat(jspWriter.getContent()).isEqualTo(Reasons.getReasonPhrase(code));
    }
}