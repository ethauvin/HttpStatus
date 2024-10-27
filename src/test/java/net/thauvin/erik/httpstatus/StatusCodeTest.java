/*
 * StatusCodeTest.java
 *
 * Copyright 2015-2024 Erik C. Thauvin (erik@thauvin.net)
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

import org.assertj.core.api.AutoCloseableSoftAssertions;
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
        var bundle = ResourceBundle.getBundle(Reasons.BUNDLE_BASENAME);
        var statusCode = new StatusCode();

        try (var softly = new AutoCloseableSoftAssertions()) {
            for (var key : bundle.keySet()) {
                int code = Integer.parseInt(key);

                statusCode.setCode(code);
                softly.assertThat(statusCode.getCode()).as("is not %s", code).isEqualTo(code);
                softly.assertThat(statusCode.isInfo()).as("%s is info", code).isEqualTo(code >= 100 && code < 200);
                softly.assertThat(statusCode.isSuccess()).as("%s is ok", code).isEqualTo(code >= 200 && code < 300);
                softly.assertThat(statusCode.isRedirect()).as("%s is redirect", code)
                        .isEqualTo(code >= 300 && code < 400);
                softly.assertThat(statusCode.isClientError()).as("%s is client error", code)
                        .isEqualTo(code >= 400 && code < 500);
                softly.assertThat(statusCode.isServerError()).as("%s is server error", code)
                        .isEqualTo(code >= 500 && code < 600);
                softly.assertThat(statusCode.isError()).as("%s is error", code).isEqualTo(code >= 400 && code < 600);
                softly.assertThat(statusCode.isValid()).as("%s is valid", code).isTrue();

                softly.assertThat(statusCode.getReason()).as("%s reason phrase is not valid", code)
                        .isEqualTo(Reasons.getReasonPhrase(code));
            }
        }

        try (var softly = new AutoCloseableSoftAssertions()) {
            int[] unknowns = {0, 99, 600};

            for (var code : unknowns) {
                statusCode.setCode(code);
                softly.assertThat(statusCode.getCode()).as("is not %s", code).isEqualTo(code);
                softly.assertThat(statusCode.isInfo()).as("%s is info", code).isFalse();
                softly.assertThat(statusCode.isSuccess()).as("%s is ok", code).isFalse();
                softly.assertThat(statusCode.isRedirect()).as("%s is redirect", code).isFalse();
                softly.assertThat(statusCode.isClientError()).as("%s is client error", code).isFalse();
                softly.assertThat(statusCode.isServerError()).as("%s is server error", code).isFalse();
                softly.assertThat(statusCode.isError()).as("%s is error", code).isFalse();
                softly.assertThat(statusCode.isValid()).as("%s is invalid", code).isFalse();
                softly.assertThat(statusCode.getReason()).as("%s reason phrase is not null.", code).isNull();
            }
        }

        statusCode = new StatusCode(900);
        assertThat(statusCode.getCode()).as("is not %s", statusCode.getCode()).isEqualTo(900);
    }
}
