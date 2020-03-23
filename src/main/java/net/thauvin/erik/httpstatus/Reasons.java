/*
 * Reasons.java
 *
 * Copyright (c) 2015-2020, Erik C. Thauvin (erik@thauvin.net)
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

import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Populates the {@link #REASON_PHRASES reason phrases} map from {@link #BUNDLE_BASENAME bundle properties}, and
 * implements accessor methods.
 *
 * @author <a href="mailto:erik@thauvin.net" target="_blank">Erik C. Thauvin</a>
 * @created 2015-12-02
 * @since 1.0
 */
public final class Reasons {
    /**
     * The resource bundle base name.
     */
    static final String BUNDLE_BASENAME = "net.thauvin.erik.httpstatus.reasons";
    /**
     * The reason phrases map.
     */
    private static final Map<String, String> REASON_PHRASES = new ConcurrentHashMap<>();

    // Initializes the reason phrases map.
    static {
        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASENAME);
        for (final String key : bundle.keySet()) {
            REASON_PHRASES.put(key, bundle.getString(key));
        }
    }

    /**
     * Disables the default constructor.
     */
    private Reasons() {
        throw new UnsupportedOperationException("Illegal constructor call.");
    }

    /**
     * Returns the reason phrase for the specified status code.
     *
     * @param statusCode The status code.
     * @return The reason phrase, or <code>null</code>.
     */
    public static String getReasonPhrase(final int statusCode) {
        return getReasonPhrase(Integer.toString(statusCode));
    }

    /**
     * Returns the reason phrase for the specified status code.
     *
     * @param statusCode The status code.
     * @return The reason phrase, or <code>null</code>.
     */
    @SuppressWarnings({ "WeakerAccess" })
    public static String getReasonPhrase(final String statusCode) {
        return REASON_PHRASES.get(statusCode);
    }

    /**
     * Prints the reason phrase for the given status code(s).
     *
     * @param args The status code(s), prints all if none.
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String... args) {
        if (args.length >= 1) {
            for (final String key : args) {
                final String value = REASON_PHRASES.get(key);
                if (value != null) {
                    System.out.println(key + ": " + value);
                }
            }
        } else {
            final SortedSet<String> keys = new TreeSet<>(REASON_PHRASES.keySet());
            for (final String key : keys) {
                final String value = REASON_PHRASES.get(key);
                if (value != null) {
                    System.out.println(key + ": " + value);
                }
            }

            System.out.println("Total: " + REASON_PHRASES.size());
        }
    }
}