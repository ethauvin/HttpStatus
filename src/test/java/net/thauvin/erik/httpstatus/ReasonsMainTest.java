/*
 * ReasonsMainTest.java
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
class ReasonsMainTest {
    private final static PrintStream originalOut = System.out;
    private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
    }

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    public void resetStreams() {
        outContent.reset();
    }

    @Test
    void testMain() {
        Reasons.main("401");
        assertThat(outContent.toString()).contains(Reasons.getReasonPhrase("401")).as("401");
        assertThat(outContent.toString()).doesNotContain("500").as("401 no 500");
    }

    @Test
    void testMainAll() {
        Reasons.main();
        assertThat(outContent.toString()).contains(Reasons.getReasonPhrase(301)).as("301");
        assertThat(outContent.toString()).contains(Reasons.getReasonPhrase(404)).as("404");
    }

    @Test
    void testMainArgs() {
        Reasons.main("500", "302");
        assertThat(outContent.toString()).contains(Reasons.getReasonPhrase("500")).as("500 (302)");
        assertThat(outContent.toString()).contains(Reasons.getReasonPhrase("302")).as("(500) 302");
        assertThat(outContent.toString()).doesNotContain("404").as("500/302 not 404");
    }

    @Test
    void testMainArgsClass() {
        Reasons.main("2xx");
        assertThat(outContent.toString()).contains(Reasons.getReasonPhrase("200")).as("2xx");
    }

    @Test
    void testMainInvalid() {
        Reasons.main("aaa");
        assertThat(outContent.toString()).as("invalid argument: aaa").isEmpty();
    }
}
