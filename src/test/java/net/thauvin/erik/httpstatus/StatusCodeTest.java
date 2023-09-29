/*
 * StatusCodeTest.java
 *
 * Copyright 2015-2023 Erik C. Thauvin (erik@thauvin.net)
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

package net.thauvin.erik.httpstatus;

import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StatusCode Tests.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @since 1.1.0
 */
class StatusCodeTest {
    @Test
    void testStatusCode() {
        final ResourceBundle bundle = ResourceBundle.getBundle(Reasons.BUNDLE_BASENAME);
        StatusCode statusCode = new StatusCode();
        for (final String key : bundle.keySet()) {
            final int code = Integer.parseInt(key);
            statusCode.setCode(code);
            assertThat(statusCode.getCode()).as("is not " + code).isEqualTo(code);
            assertThat(statusCode.isInfo()).as(code + " is info").isEqualTo(code >= 100 && code < 200);
            assertThat(statusCode.isSuccess()).as(code + " is ok").isEqualTo(code >= 200 && code < 300);
            assertThat(statusCode.isRedirect()).as(code + " is redirect").isEqualTo(code >= 300 && code < 400);
            assertThat(statusCode.isClientError()).as(code + " is client error").isEqualTo(code >= 400 && code < 500);
            assertThat(statusCode.isServerError()).as(code + " is server error").isEqualTo(code >= 500 && code < 600);
            assertThat(statusCode.isError()).as(code + " is error").isEqualTo(code >= 400 && code < 600);
            assertThat(statusCode.isValid()).as(code + "is valid").isTrue();

            assertThat(statusCode.getReason()).as(code + "reason phrase is not valid")
                    .isEqualTo(Reasons.getReasonPhrase(code));
        }

        final int[] unknowns = {0, 99, 600};
        for (final int code : unknowns) {
            statusCode.setCode(code);
            assertThat(statusCode.getCode()).as("is not " + code).isEqualTo(code);
            assertThat(statusCode.isInfo()).as(code + " is info").isFalse();
            assertThat(statusCode.isSuccess()).as(code + " is ok").isFalse();
            assertThat(statusCode.isRedirect()).as(code + " is redirect").isFalse();
            assertThat(statusCode.isClientError()).as(code + " is client error").isFalse();
            assertThat(statusCode.isServerError()).as(code + " is server error").isFalse();
            assertThat(statusCode.isError()).as(code + " is error").isFalse();
            assertThat(statusCode.isValid()).as("600 is invalid").isFalse();
            assertThat(statusCode.getReason()).as(code + "reason phrase is not null.").isNull();
        }

        statusCode = new StatusCode(900);
        assertThat(statusCode.getCode()).as("is not 900").isEqualTo(900);
    }
}
