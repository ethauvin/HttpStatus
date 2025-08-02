/*
 * XmlSupportTests.java
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

package net.thauvin.erik.httpstatus.taglibs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class XmlSupportTests {
    // Simple concrete implementation of the abstract XmlSupport class for testing purposes
    static class XmlSupportImpl extends XmlSupport {
    }

    @Nested
    @DisplayName("Default Value Tests")
    class DefaultValueTests {
        @Test
        void setDefaultValue() {
            var xmlSupport = new XmlSupportImpl();
            var defaultValue = "Default Value";

            xmlSupport.setDefault(defaultValue);

            assertEquals(defaultValue, xmlSupport.defaultValue,
                    "The defaultValue should be set correctly.");
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = {" ", "  "})
        void setEmptyDefaultValue(String input) {
            var xmlSupport = new XmlSupportImpl();

            xmlSupport.setDefault(input);

            assertEquals(input, xmlSupport.defaultValue,
                    "The defaultValue should be set to an empty string.");
        }

        @ParameterizedTest
        @NullSource
        void setNullDefaultValue(String input) {
            var xmlSupport = new XmlSupportImpl();

            xmlSupport.setDefault(input);

            assertNull(xmlSupport.defaultValue, "The defaultValue should be set to null.");
        }
    }

    @Nested
    @DisplayName("Escape XML Tests")
    class EscapeXmlTests {

        @Test
        void setEscapeXmlDefault() {
            var xmlSupport = new XmlSupportImpl();

            assertTrue(xmlSupport.escapeXml, "The default value of escapeXml should be true.");
        }

        @Test
        void setEscapeXmlFalse() {
            var xmlSupport = new XmlSupportImpl();
            var escapeXml = false;

            xmlSupport.setEscapeXml(escapeXml);

            assertEquals(escapeXml, xmlSupport.escapeXml, "The escapeXml should be set to false.");
        }

        @Test
        void setEscapeXmlTrue() {
            var xmlSupport = new XmlSupportImpl();
            var escapeXml = true;

            xmlSupport.setEscapeXml(escapeXml);

            assertEquals(escapeXml, xmlSupport.escapeXml, "The escapeXml should be set to true.");
        }

    }
}