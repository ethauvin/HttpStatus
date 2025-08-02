/*
 * CauseTagTests.java
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Implements the CauseTagTests class.
 *
 * @author <a href="https://erik.thauvin.net/">Erik C. Thauvin</a>
 * @since 1.1.0
 */
class CauseTagTests {
    private final CauseTag tag = new CauseTag();

    @Nested
    @DisplayName("DoTag Tests")
    class DoTagTests {
        private PageContext createMockPageContext(Throwable throwable) {
            var pageContext = mock(PageContext.class);
            var errorData = mock(ErrorData.class);
            when(errorData.getThrowable()).thenReturn(throwable);
            when(pageContext.getErrorData()).thenReturn(errorData);
            when(pageContext.getOut()).thenReturn(new MockJspWriter());
            return pageContext;
        }

        @Test
        void doTagHandlesNullCause() throws Exception {
            var pageContext = createMockPageContext(new Exception());
            tag.setJspContext(pageContext);
            tag.setDefault("No error"); // Set a default value
            tag.doTag();
            assertThat(getOutputFromPageContext(pageContext)).isEqualTo("No error");
        }

        @Test
        void doTagWritesCause() throws Exception {
            var pageContext = createMockPageContext(new Exception("Exception", new Exception("Test Cause")));
            tag.setJspContext(pageContext);
            tag.setDefault(null); // Ensure the default value is not used
            tag.doTag();
            assertThat(getOutputFromPageContext(pageContext)).isEqualTo("Test Cause");
        }

        private String getOutputFromPageContext(PageContext pageContext) {
            var writer = (MockJspWriter) pageContext.getOut();
            return writer.getContent();
        }
    }

    @Nested
    @DisplayName("GetCause Tests")
    class GetCauseTests {
        final static String message = "This is the cause";

        @Test
        void cause() {
            assertThat(tag.getCause(new Exception(message))).as("has cause").isEqualTo(message);
        }

        @Test
        void causeWithEmptyMessage() {
            assertThat(tag.getCause(new Exception(""))).as("empty").isEmpty();
        }

        @Test
        void causeWithNoMessage() {
            assertThat(tag.getCause(new Exception())).as("no cause").isNull();
        }

        @Test
        void causeWithNullLocalizedMessage() {
            Throwable cause = new Throwable() {
                @Override
                public String getLocalizedMessage() {
                    return null;
                }
            };

            assertThat(tag.getCause(cause)).as("null localized message").isNull();
        }

        @Test
        void causeWithNullMessage() {
            assertThat(tag.getCause(null)).as("null").isNull();
        }

        @Test
        void causeWithSpecialCharacters() {
            var message = "!@#$%^&*()_+<>?";

            assertThat(tag.getCause(new Exception(message))).as("special characters").isEqualTo(message);
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "  ", "\t", "\n", "\r"})
        void causeWithWhitespace(String message) {
            assertThat(tag.getCause(new Exception(message))).as("whitespace").isEqualTo(message);
        }
    }
}
