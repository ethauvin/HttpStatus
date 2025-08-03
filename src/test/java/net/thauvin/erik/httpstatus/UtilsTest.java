/*
 * UtilsTest.java
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utils Tests.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
class UtilsTest {
    @Test
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void privateConstructor() throws Exception {
        var constructor = Utils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor is not private");
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause(), e.getMessage());
        }
    }

    @Nested
    @DisplayName("EscapeXml Tests")
    class EscapeXmlTests {
        @Test
        void escapeXml() {
            var xmlInput = "<xml>&data>'text'\"more data\"&more</xml>";
            var expectedOutput = "&lt;xml&gt;&amp;data&gt;&apos;text&apos;&quot;more data&quot;&amp;more&lt;/xml&gt;";
            assertThat(Utils.escapeXml(xmlInput)).isEqualTo(expectedOutput);
        }

        @Test
        void escapeXmlWithContinuousSpecialCharacters() {
            var input = "<<<>>>&&&'''\"\"\"";
            var expected = "&lt;&lt;&lt;&gt;&gt;&gt;&amp;&amp;&amp;&apos;&apos;&apos;&quot;&quot;&quot;";
            assertThat(Utils.escapeXml(input)).isEqualTo(expected);
        }

        @ParameterizedTest
        @EmptySource
        void escapeXmlWithEmptyString(String input) {
            assertThat(Utils.escapeXml(input)).isEmpty();
        }

        @Test
        void escapeXmlWithMixedContent() {
            var input = "Mix123<&>'\"Text";
            var expected = "Mix123&lt;&amp;&gt;&apos;&quot;Text";
            assertThat(Utils.escapeXml(input)).isEqualTo(expected);
        }

        @ParameterizedTest
        @NullSource
        void escapeXmlWithNullString(String input) {
            assertThat(Utils.escapeXml(input)).isNull();
        }

        @Test
        void escapeXmlWithNumericCharacters() {
            var input = "1234567890";
            var expected = "1234567890";
            assertThat(Utils.escapeXml(input)).isEqualTo(expected);
        }

        @Test
        void escapeXmlWithSpecialCharacters() {
            assertThat(Utils.escapeXml("<>&'\"")).isEqualTo("&lt;&gt;&amp;&apos;&quot;");
        }

        @Test
        void escapeXmlWithTextWithNoSpecialCharacters() {
            assertThat(Utils.escapeXml("No special characters here"))
                    .isEqualTo("No special characters here");
        }
    }

    @Nested
    @DisplayName("OutWrite Tests")
    class OutWriteTests {
        private static final String DEFAULT_VALUE = "default";

        @Test
        void outWrite() throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, "value", DEFAULT_VALUE, false);
                assertThat(sw.toString()).as("outWrite(value)").isEqualTo("value");
            }
        }

        @Test
        void outWriteWithAmpersand() throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, null, "1 & 1", true);
                assertThat(sw.toString()).as("outWrite(1 & 1)").isEqualTo("1 &amp; 1");
            }
        }

        @Test
        void outWriteWithApostrophe() throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, "she's", DEFAULT_VALUE, true);
                assertThat(sw.toString()).as("outWrite(she's)").isEqualTo("she&apos;s");
            }
        }

        @Test
        void outWriteWithDefaultValue() throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, null, DEFAULT_VALUE, false);
                assertThat(sw.toString()).as("outWrite(default)").isEqualTo(DEFAULT_VALUE);
            }
        }

        @ParameterizedTest
        @EmptySource
        void outWriteWithEmptyValue(String input) throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, input, DEFAULT_VALUE, false);
                assertThat(sw.toString()).as("outWrite(value empty)").isEmpty();
            }
        }

        @Test
        void outWriteWithEmptyValueAndNullDefaultValue() throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, "", null, true);
                assertThat(sw.toString()).as("outWrite(default empty)").isEmpty();
            }
        }

        @ParameterizedTest
        @EmptySource
        void outWriteWithEmptyValueAsXml(String input) throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, input, DEFAULT_VALUE, true);
                assertThat(sw.toString()).as("outWrite(value empty).as(xml)").isEmpty();
            }
        }

        @ParameterizedTest
        @NullAndEmptySource
        void outWriteWithNullValue(String input) throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, input, input, true);
                assertThat(sw.toString()).as("outWrite(null)").isEmpty();
            }
        }

        @Test
        void outWriteWithNullValueAndEmptyDefaultValue() throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, null, "", true);
                assertThat(sw.toString()).as("outWrite(default empty)").isEmpty();
            }
        }

        @ParameterizedTest
        @NullSource
        void outWriteWithNullValueAndNullDefaultValue(String input) throws IOException {
            try (var sw = new StringWriter()) {
                Utils.outWrite(sw, input, input, true);
                assertThat(sw.toString()).as("outWrite(null).as(xml)").isEmpty();
            }
        }
    }
}
