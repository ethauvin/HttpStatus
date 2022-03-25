/*
 * UtilsTest.java
 *
 * Copyright (c) 2015-2021, Erik C. Thauvin (erik@thauvin.net)
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
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.testng.Assert.assertEquals;

/**
 * Utils Tests.
 *
 * @author <a href="mailto:erik@thauvin.net" target="_blank">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
public class UtilsTest {
    @Test
    public void testEscapeXml() {
        assertEquals(Utils.escapeXml(
                "This is a test. We wan't to make sure that everything is <encoded> according the \"encoding\" "
                        + "parameter & value."),
                "This is a test. We wan&apos;t to make sure that everything is &lt;encoded&gt; according the "
                        + "&quot;encoding&quot; parameter &amp; value.");
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    @SuppressFBWarnings("CE_CLASS_ENVY")
    @Test
    public void testOutWrite() throws IOException {
        try (StringWriter sw = new StringWriter()) {
            Utils.outWrite(sw, null, "default", false);
            assertEquals(sw.toString(), "default", "outWrite(default)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "", "default", false);
            assertEquals(sw.toString(), "", "outWrite(value empty)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, null, true);
            assertEquals(sw.toString(), "", "outWrite(null)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "value", "default", false);
            assertEquals(sw.toString(), "value", "outWrite(value)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "wan't", "default", true);
            assertEquals(sw.toString(), "wan&apos;t", "outWrite(wan't)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, "1 & 1", true);
            assertEquals(sw.toString(), "1 &amp; 1", "outWrite(1 & 1)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "", "default", true);
            assertEquals(sw.toString(), "", "outWrite(value empty, xml)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, "", true);
            assertEquals(sw.toString(), "", "outWrite(default empty)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, null, true);
            assertEquals(sw.toString(), "", "outWrite(null, xml)");
        }
    }
}
