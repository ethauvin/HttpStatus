/*
 * ReasonsMainTests.java
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Main Class Tests.
 *
 * @author <a href="https://erik.thauvin.net/">Erik C. Thauvin</a>
 * @created 2019-05-06
 * @since 1.0
 */
class ReasonsMainTests {
    private static final ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();
    private static final PrintStream SYSTEM_OUT = System.out;

    @AfterAll
    public static void restoreStreams() {
        System.setOut(SYSTEM_OUT);
    }

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(OUTPUT_STREAM));
    }

    @Test
    void mainWithArgsClass() {
        Reasons.main("2xx");
        var lines = OUTPUT_STREAM.toString().split("\n");
        assertThat(lines).as("should be 13 reasons for 2xx").hasSize(13);
        try (var softly = new AutoCloseableSoftAssertions()) {
            for (var line : lines) {
                softly.assertThat(line).startsWith("2").as("%s starts with 2", line);
            }
        }
    }

    @Test
    void mainWithInvalidArg() {
        Reasons.main("aaa");
        assertThat(OUTPUT_STREAM.toString()).as("invalid argument: aaa").isEmpty();
    }

    @Test
    void mainWithMultipleArgs() {
        Reasons.main("500", "302");
        assertThat(OUTPUT_STREAM.toString()).contains(Reasons.getReasonPhrase("500")).as("500 (302)");
        assertThat(OUTPUT_STREAM.toString()).contains(Reasons.getReasonPhrase("302")).as("(500) 302");
        assertThat(OUTPUT_STREAM.toString()).doesNotContain("404").as("500/302 not 404");
    }

    @Test
    void mainWithSingleArg() {
        Reasons.main("401");
        assertThat(OUTPUT_STREAM.toString()).contains(Reasons.getReasonPhrase("401")).as("401");
        assertThat(OUTPUT_STREAM.toString()).doesNotContain("500").as("401 no 500");
    }

    @Test
    void mainWithoutArgs() {
        Reasons.main();
        assertThat(OUTPUT_STREAM.toString()).contains(Reasons.getReasonPhrase(301)).as("301");
        assertThat(OUTPUT_STREAM.toString()).contains(Reasons.getReasonPhrase(404)).as("404");
    }

    @BeforeEach
    public void resetStreams() {
        OUTPUT_STREAM.reset();
    }
}
