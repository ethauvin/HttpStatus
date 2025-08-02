/*
 * StatusCodeTests.java
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

package net.thauvin.erik.httpstatus;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * StatusCode Tests.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @since 1.1.0
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class StatusCodeTests {
    static Stream<Integer> statusCodes() {
        var bundle = ResourceBundle.getBundle(Reasons.BUNDLE_BASENAME);
        return bundle.keySet().stream()
                .map(Integer::parseInt);
    }

    @ParameterizedTest
    @MethodSource("statusCodes")
    void statusCode(int code) {
        if (code >= 100 && code < 200) {
            assertThat(StatusCode.isInfo(code)).as("%s is info", code).isTrue();
        }
        if (code >= 100 && code < 200) {
            assertThat(StatusCode.isInformational(code)).as("%s is info", code).isTrue();
        }
        if (code >= 200 && code < 300) {
            assertThat(StatusCode.isSuccess(code)).as("%s is ok", code).isTrue();
        }
        if (code >= 200 && code < 300) {
            assertThat(StatusCode.isSuccessful(code)).as("%s is ok", code).isTrue();
        }
        if (code >= 300 && code < 400) {
            assertThat(StatusCode.isRedirect(code)).as("%s is redirect", code).isTrue();
        }
        if (code >= 400 && code < 500) {
            assertThat(StatusCode.isClientError(code)).as("%s is client error", code).isTrue();
        }
        if (code >= 500 && code < 600) {
            assertThat(StatusCode.isServerError(code)).as("%s is server error", code).isTrue();
        }
        if (code >= 400 && code < 600) {
            assertThat(StatusCode.isError(code)).as("%s is error", code).isTrue();
        }

        assertThat(StatusCode.isValid(code)).as("%s is valid", code).isTrue();
    }

    @ParameterizedTest
    @MethodSource("statusCodes")
    void statusCodeObject(int code) {
        var statusCode = new StatusCode();
        statusCode.setCode(code);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(statusCode.getCode()).as("is not %s", code).isEqualTo(code);
            softly.assertThat(statusCode.isInfo()).as("%s is info", code)
                    .isEqualTo(code >= 100 && code < 200);
            softly.assertThat(statusCode.isInformational()).as("%s is info", code)
                    .isEqualTo(code >= 100 && code < 200);
            softly.assertThat(statusCode.isSuccess()).as("%s is ok", code)
                    .isEqualTo(code >= 200 && code < 300);
            softly.assertThat(statusCode.isSuccessful()).as("%s is ok", code)
                    .isEqualTo(code >= 200 && code < 300);
            softly.assertThat(statusCode.isRedirect()).as("%s is redirect", code)
                    .isEqualTo(code >= 300 && code < 400);
            softly.assertThat(statusCode.isClientError()).as("%s is client error", code)
                    .isEqualTo(code >= 400 && code < 500);
            softly.assertThat(statusCode.isServerError()).as("%s is server error", code)
                    .isEqualTo(code >= 500 && code < 600);
            softly.assertThat(statusCode.isError()).as("%s is error", code)
                    .isEqualTo(code >= 400 && code < 600);
            softly.assertThat(statusCode.isValid()).as("%s is valid", code).isTrue();

            softly.assertThat(statusCode.getReason()).as("%s reason phrase is not valid", code)
                    .isEqualTo(Reasons.getReasonPhrase(code));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 99, 600, 900})
    void statusCodeWithUnknowns(int code) {
        var statusCode = new StatusCode(code);
        try (var softly = new AutoCloseableSoftAssertions()) {
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
}
