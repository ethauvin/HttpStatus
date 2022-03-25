/*
 * ReasonsMainTest.java
 *
 * Copyright (c) 2015-2022, Erik C. Thauvin (erik@thauvin.net)
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Main Class Tests.
 *
 * @author <a href="https://erik.thauvin.net/" target="_blank">Erik C. Thauvin</a>
 * @created 2019-05-06
 * @since 1.0
 */
@SuppressFBWarnings({"DM_DEFAULT_ENCODING", "ITU_INAPPROPRIATE_TOSTRING_USE"})
public class ReasonsMainTest {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @AfterTest
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @BeforeTest
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeMethod
    public void resetStreams() {
        outContent.reset();
    }

    @Test
    public void testMain() {
        Reasons.main("401");
        assertTrue(outContent.toString().contains(Reasons.getReasonPhrase("401")), "401");
        assertFalse(outContent.toString().contains("500"), "401 no 500");
    }

    @Test
    public void testMainAll() {
        Reasons.main();
        assertTrue(outContent.toString().contains(Reasons.getReasonPhrase(301)), "301");
        assertTrue(outContent.toString().contains(Reasons.getReasonPhrase(404)), "404");
    }

    @Test
    public void testMainArgs() {
        Reasons.main("500", "302");
        assertTrue(outContent.toString().contains(Reasons.getReasonPhrase("500")), "500 (302)");
        assertTrue(outContent.toString().contains(Reasons.getReasonPhrase("302")), "(500) 302");
        assertFalse(outContent.toString().contains("404"), "500/302 not 404");
    }

    @Test
    public void testMainInvalid() {
        Reasons.main("aaa");
        assertTrue(outContent.toString().isEmpty(), "invalid argument: aaa");
    }
}
