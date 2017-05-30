/*
 * Utils.java
 *
 * Copyright (c) 2015-2016, Erik C. Thauvin (erik@thauvin.net)
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

/**
 * The <code>Utils</code> class implements a collection of utility methods used throughout this project.
 *
 * @author <a href="mailto:erik@thauvin.net" target="_blank">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
public final class Utils {

    /**
     * Disables the default constructor.
     *
     * @throws UnsupportedOperationException If the constructor is called.
     */
    private Utils()
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Illegal constructor call.");
    }

    /**
     * Converts <code>&lt;</code>, <code>&gt;</code>, <code>&amp;</code>, <code>'</code>, <code>"</code>
     * to their corresponding entity codes.
     *
     * @param value The string value to convert.
     * @return The converted string value.
     */
    public static String escapeXml(final String value) {
        final StringBuilder escaped = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            switch (c) {
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '&':
                    escaped.append("&amp;");
                    break;
                case '\'':
                    escaped.append("&#039;");
                    break;
                case '"':
                    escaped.append("&#034;");
                    break;
                default:
                    escaped.append(c);
                    break;
            }
        }

        return escaped.toString();
    }

    /**
     * Writes a string value to the specified writer. The default value is used when the actual value is null.
     *
     * @param out          The writer to output the value to.
     * @param value        The string value.
     * @param defaultValue The default value.
     * @param xml          The {@link #escapeXml(String) xml} flag.
     * @throws IOException If an I/O error occurs.
     */
    public static void outWrite(final Writer out, final String value, final String defaultValue, final boolean xml)
            throws IOException {
        if (xml) {
            if (value != null) {
                out.write(escapeXml(value));
            } else if (defaultValue != null) {
                out.write(escapeXml(defaultValue));
            }
        } else {
            if (value != null) {
                out.write(value);
            } else if (defaultValue != null) {
                out.write(defaultValue);
            }
        }
    }
}