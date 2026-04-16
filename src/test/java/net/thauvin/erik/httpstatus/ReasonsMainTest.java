/*
 * ReasonsMainTests.java
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

package net.thauvin.erik.httpstatus;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rife.bld.extension.testing.CaptureOutput;
import rife.bld.extension.testing.CapturedOutput;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Main Class Tests.
 *
 * @author <a href="https://erik.thauvin.net/">Erik C. Thauvin</a>
 * @created 2019-05-06
 * @since 1.0
 */
@CaptureOutput
class ReasonsMainTest {

    @ParameterizedTest
    @CsvSource({
            "2xx, 13",
            "3xx, 9",
            "4xx, 44",
            "5xx, 26"
    })
    void mainWithClassArg(String codeClass, int count, CapturedOutput output) {
        Reasons.main(codeClass);
        var lines = output.getOutLines();
        assertThat(lines).as("should be %s reasons for %s", count, codeClass).hasSize(count);
        try (var softly = new AutoCloseableSoftAssertions()) {
            for (var line : lines) {
                softly.assertThat(line).startsWith(codeClass.substring(0, 1))
                        .as("%s doesn't start with %s", line, codeClass);
            }
        }
    }

    @Test
    void mainWithEmptyArg(CapturedOutput output) {
        Reasons.main("");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithInvalidArg(CapturedOutput output) {
        Reasons.main("aaa");
        assertThat(output.getOut()).as("invalid argument: aaa").isEmpty();
    }

    @Test
    void mainWithInvalidClassArg(CapturedOutput output) {
        Reasons.main("6xx");
        assertThat(output.getAll()).as("invalid argument: 6xx").isEmpty();
    }

    @Test
    void mainWithInvalidLastChar(CapturedOutput output) {
        Reasons.main("2x3");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithInvalidMiddleChar(CapturedOutput output) {
        Reasons.main("2yx");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithMultipleArgs(CapturedOutput output) {
        Reasons.main("500", "302");
        assertThat(output.getOut()).contains(Reasons.getReasonPhrase("500")).as("500 (302)");
        assertThat(output.getOut()).contains(Reasons.getReasonPhrase("302")).as("(500) 302");
        assertThat(output.getOut()).doesNotContain("404").as("500/302 not 404");
    }

    @Test
    void mainWithNonDigitFirstChar(CapturedOutput output) {
        Reasons.main("axx");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithSingleArg(CapturedOutput output) {
        Reasons.main("401");
        assertThat(output.contains(Reasons.getReasonPhrase("401"))).as("401").isTrue();
        assertThat(output.getOut().contains("500")).as("401 no 500").isFalse();
    }

    @Test
    void mainWithTooLongArg(CapturedOutput output) {
        Reasons.main("22xx");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithTooShortArg(CapturedOutput output) {
        Reasons.main("2x");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithUnsupportedClassDigit(CapturedOutput output) {
        // Pattern matches, but StatusCodeClass.fromFirstDigit(6) returns empty
        Reasons.main("6xx");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithUppercaseX(CapturedOutput output) {
        Reasons.main("2Xx");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithZeroClassDigit(CapturedOutput output) {
        Reasons.main("0xx");
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void mainWithoutArgs(CapturedOutput output) {
        Reasons.main();
        assertThat(output.getOut().contains(Reasons.getReasonPhrase(301))).as("301").isTrue();
        assertThat(output.getOut().contains(Reasons.getReasonPhrase(404))).as("404").isTrue();
    }
}
