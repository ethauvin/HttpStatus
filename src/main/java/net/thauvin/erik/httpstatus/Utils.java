/*
 * Utils.java
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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * A collection of utility methods used throughout the project.
 * This class is not intended to be instantiated.
 * <p>
 * Provides helpers for XML escaping and safe writer output.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @since 1.0.0
 */
@NullMarked
public final class Utils {

    /**
     * Prevents instantiation of this utility class.
     * <p>
     * Throws an exception if invoked to ensure the class is used only statically.
     */
    private Utils() {
        throw new UnsupportedOperationException("Illegal constructor call.");
    }

    /**
     * Converts the characters {@code <}, {@code >}, {@code &}, {@code '}, and {@code "}
     * to their corresponding XML entity codes.
     * <p>
     * Returns the original value when empty. Only the predefined XML entities
     * are escaped; all other characters are preserved as-is.
     *
     * @param value the string to escape
     * @return the escaped string
     * @throws NullPointerException if the value is {@code null}
     */
    @SuppressWarnings("PMD.ForLoopVariableCount")
    public static String escapeXml(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        if (value.isEmpty()) {
            return value;
        }

        var escaped = new StringBuilder(value.length() * 2);
        for (int i = 0, len = value.length(); i < len; i++) {
            char c = value.charAt(i);
            switch (c) {
                case '<' -> escaped.append("&lt;");
                case '>' -> escaped.append("&gt;");
                case '&' -> escaped.append("&amp;");
                case '\'' -> escaped.append("&apos;");
                case '"' -> escaped.append("&quot;");
                default -> escaped.append(c);
            }
        }
        return escaped.toString();
    }

    /**
     * Writes a string value to the given writer, optionally escaping XML characters.
     * When the value is null, the provided default value is used instead. If both are
     * null, nothing is written.
     * <p>
     * The writer must not be null. XML escaping is performed using {@link #escapeXml(String)}.
     *
     * @param out          the writer to write to
     * @param value        the string value to write, may be {@code null}
     * @param defaultValue the default value to use when {@code value} is {@code null}, may be {@code null}
     * @param escapeXml    {@code true} to escape XML characters, {@code false} otherwise
     * @throws IOException          If an I/O error occurs while writing
     * @throws NullPointerException If the writer is {@code null}
     */
    public static void outWrite(Writer out, @Nullable String value, @Nullable String defaultValue, boolean escapeXml)
            throws IOException {
        Objects.requireNonNull(out, "Writer cannot be null");

        var textToWrite = value != null ? value : defaultValue;
        if (textToWrite != null) {
            out.write(escapeXml ? escapeXml(textToWrite) : textToWrite);
        }
    }
}