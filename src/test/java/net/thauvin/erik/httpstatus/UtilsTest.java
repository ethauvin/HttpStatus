/*
 * UtilsTest.java
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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utils Tests.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
class UtilsTest {
    @Test
    void testEscapeXml() {
        assertThat(Utils.escapeXml(
                "This is a test. We wan't to make sure that everything is <encoded> according the \"encoding\" "
                        + "parameter & value."))
                .isEqualTo("This is a test. We wan&apos;t to make sure that everything is &lt;encoded&gt; " +
                        "according the &quot;encoding&quot; parameter &amp; value.");
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    @Test
    void testOutWrite() throws IOException {
        try (var sw = new StringWriter()) {
            Utils.outWrite(sw, null, "default", false);
            assertThat(sw.toString()).isEqualTo("default").as("outWrite(default)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "", "default", false);
            assertThat(sw.toString()).isEqualTo("").as("outWrite(value empty)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, null, true);
            assertThat(sw.toString()).isEqualTo("").as("outWrite(null)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "value", "default", false);
            assertThat(sw.toString()).isEqualTo("value").as("outWrite(value)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "wan't", "default", true);
            assertThat(sw.toString()).isEqualTo("wan&apos;t").as("outWrite(wan't)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, "1 & 1", true);
            assertThat(sw.toString()).isEqualTo("1 &amp; 1").as("outWrite(1 & 1)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, "", "default", true);
            assertThat(sw.toString()).isEqualTo("").as("outWrite(value empty).as(xml)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, "", true);
            assertThat(sw.toString()).isEqualTo("").as("outWrite(default empty)");

            sw.getBuffer().setLength(0);
            Utils.outWrite(sw, null, null, true);
            assertThat(sw.toString()).isEqualTo("").as("outWrite(null).as(xml)");
        }
    }
}
