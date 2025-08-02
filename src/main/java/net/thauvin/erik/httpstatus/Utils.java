/*
 * Utils.java
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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;

/**
 * The <code>Utils</code> class implements a collection of utility methods used throughout this project.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
public final class Utils {
    private static final Map<Character, String> XML_ENTITIES = Map.of(
            '<', "&lt;",
            '>', "&gt;",
            '&', "&amp;",
            '\'', "&apos;",
            '"', "&quot;"
    );

    /**
     * Disables the default constructor.
     *
     * @throws UnsupportedOperationException If the constructor is called
     */
    private Utils() {
        throw new UnsupportedOperationException("Illegal constructor call.");
    }

    /**
     * Converts <code>&lt;</code>, <code>&gt;</code>, <code>&amp;</code>, <code>'</code>, <code>"</code>
     * to their corresponding entity codes.
     *
     * @param value The string value to convert
     * @return The converted string value
     */
    public static String escapeXml(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        var escaped = new StringBuilder(value.length() * 2);
        for (char c : value.toCharArray()) {
            escaped.append(XML_ENTITIES.getOrDefault(c, String.valueOf(c)));
        }
        return escaped.toString();
    }

    /**
     * Writes a string value to the specified writer, with optional XML escaping.
     * The default value is used when the actual value is null.
     *
     * @param out          The writer to output the value to
     * @param value        The string value
     * @param defaultValue The default value
     * @param xml          The {@link #escapeXml(String) xml} flag
     * @throws IOException          If an I/O error occurs
     * @throws NullPointerException If the writer is null
     */
    public static void outWrite(Writer out, String value, String defaultValue, boolean xml)
            throws IOException {
        Objects.requireNonNull(out, "Writer cannot be null");

        var textToWrite = value != null ? value : defaultValue;
        if (textToWrite != null) {
            writeEscapedText(out, textToWrite, xml);
        }
    }

    private static void writeEscapedText(Writer out, String text, boolean shouldEscape)
            throws IOException {
        out.write(shouldEscape ? escapeXml(text) : text);
    }
}
