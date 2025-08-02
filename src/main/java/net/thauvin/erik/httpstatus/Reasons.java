/*
 * Reasons.java
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

import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Populates the {@link #REASON_PHRASES reason phrases} map from {@link #BUNDLE_BASENAME bundle properties}, and
 * implements accessor methods.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
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
    @SuppressWarnings("PMD.UseConcurrentHashMap")
    private static final Map<String, String> REASON_PHRASES = new ConcurrentSkipListMap<>();

    // Initializes the reason phrases map.
    static {
        var bundle = ResourceBundle.getBundle(BUNDLE_BASENAME);
        for (var key : bundle.keySet()) {
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
     * Retrieves a map of reason phrases associated with a specific standard HTTP response class.
     * <p>
     * The response class is determined by the first digit of the status codes provided by the {@code StatusCodeClass}.
     * This method filters and collects those reason phrases whose status code starts with the specified class digit.
     *
     * @param reasonClass The {@code StatusCodeClass} enum representing the HTTP response class
     * @return A map where the keys are status code strings and the values are the corresponding reason phrases
     * @since 2.0.0
     */
    public static Map<String, String> getReasonClass(StatusCodeClass reasonClass) {
        var reasons = new ConcurrentSkipListMap<String, String>();
        REASON_PHRASES.keySet().forEach(k -> {
            if (k.startsWith(reasonClass.getFirstDigit())) {
                reasons.put(k, REASON_PHRASES.get(k));
            }
        });
        return reasons;
    }

    /**
     * Returns the reason phrase for the specified status code.
     *
     * @param statusCode The status code for which the reason phrase is to be retrieved
     * @return The reason phrase, or {@code null}
     */
    public static String getReasonPhrase(String statusCode) {
        return REASON_PHRASES.get(statusCode);
    }


    /**
     * Returns the reason phrase for the specified status code. If no reason phrase is found, the default reason is
     * returned.
     *
     * @param statusCode    The status code for which the reason phrase is to be retrieved
     * @param defaultReason The default reason phrase to return if no reason phrase is found for the given status code
     * @return The reason phrase, or the default reason if no match is found
     * @since 2.0.0
     */
    public static String getReasonPhrase(String statusCode, String defaultReason) {
        var reason = getReasonPhrase(statusCode);
        return Objects.requireNonNullElse(reason, defaultReason);
    }

    /**
     * Returns the reason phrase for the specified status code. If no reason phrase is found, the default reason is
     * returned.
     *
     * @param statusCode    The status code for which the reason phrase is to be retrieved
     * @param defaultReason The default reason phrase to return if no reason phrase is found for the given status code
     * @return The reason phrase, or the default reason if no match is found
     * @since 2.0.0
     */
    public static String getReasonPhrase(int statusCode, String defaultReason) {
        return getReasonPhrase(Integer.toString(statusCode), defaultReason);
    }

    /**
     * Returns the reason phrase for the specified status code.
     *
     * @param statusCode The status code for which the reason phrase is to be retrieved
     * @return The reason phrase, or {@code null}
     */
    public static String getReasonPhrase(int statusCode) {
        return getReasonPhrase(Integer.toString(statusCode));
    }

    /**
     * Prints the reason phrase for the given status code(s).
     *
     * @param args The status code(s) or response class(es), prints all if none
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) {
        if (args.length >= 1) {
            for (var arg : args) {
                if (arg.matches("[1-5]xx")) { // e.g.: 2xx
                    var reasonClass = StatusCodeClass.fromFirstDigit(arg.substring(0, 1));
                    if (reasonClass.isPresent()) {
                        var reasons = getReasonClass(reasonClass.get());
                        reasons.forEach((k, v) -> System.out.println(k + ": " + v));
                    } else {
                        System.out.println("Invalid reason class: " + arg);
                    }

                } else { // e.g.: 404
                    var value = REASON_PHRASES.get(arg);
                    if (value != null) {
                        System.out.println(arg + ": " + value);
                    }
                }
            }
        } else { // Print all
            REASON_PHRASES.keySet().forEach(k -> System.out.println(k + ": " + REASON_PHRASES.get(k)));
            System.out.println("Total: " + REASON_PHRASES.size());
        }
    }
}
