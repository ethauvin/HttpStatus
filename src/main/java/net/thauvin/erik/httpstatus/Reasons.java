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

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
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
        var firstDigit = String.valueOf(reasonClass.getFirstDigit());
        REASON_PHRASES.keySet().forEach(k -> {
            if (k.startsWith(firstDigit)) {
                reasons.put(k, REASON_PHRASES.get(k));
            }
        });
        return reasons;
    }

    /**
     * Returns the reason phrase for the specified status code.
     *
     * @param <T>        The type of the status code ({@link String} or {@link Integer})
     * @param statusCode The status code for which the reason phrase is to be retrieved
     * @return The reason phrase or null if not match is found
     */
    public static <T> String getReasonPhrase(T statusCode) {
        return REASON_PHRASES.get(toStatusCodeString(statusCode));
    }

    /**
     * Returns the reason phrase for the specified status code.
     *
     * @param <T>           The type of the status code ({@link String} or {@link Integer})
     * @param statusCode    The status code for which the reason phrase is to be retrieved
     * @param defaultReason The default reason phrase to return if no reason phrase is found
     * @return The reason phrase, or the default reason if no match is found, or null if no default provided
     */
    public static <T> String getReasonPhrase(T statusCode, String defaultReason) {
        var reason = REASON_PHRASES.get(toStatusCodeString(statusCode));
        if (reason == null) {
            return defaultReason;
        }
        return reason;
    }

    private static boolean isStatusCodeClass(String code) {
        return code.matches("[1-5]xx");
    }

    /**
     * Prints the reason phrase for the given status code(s).
     *
     * @param args The status code(s) or response class(es), prints all if none
     */
    public static void main(String... args) {
        if (args.length == 0) {
            printAllReasonPhrases();
            return;
        }

        Arrays.stream(args).forEach(Reasons::processStatusCode);
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private static void printAllReasonPhrases() {
        REASON_PHRASES.forEach(Reasons::printReasonPhrase);
        System.out.println("Total: " + REASON_PHRASES.size());
    }

    private static void printReasonClassPhrases(Map<String, String> reasons) {
        reasons.forEach(Reasons::printReasonPhrase);
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private static void printReasonPhrase(String code, String phrase) {
        System.out.println(code + ": " + phrase);
    }

    private static void printSingleStatusCode(String code) {
        Optional.ofNullable(REASON_PHRASES.get(code))
                .ifPresent(phrase -> printReasonPhrase(code, phrase));
    }

    private static void processStatusCode(String code) {
        if (isStatusCodeClass(code)) {
            processStatusCodeClass(code);
        } else {
            printSingleStatusCode(code);
        }
    }

    private static void processStatusCodeClass(String code) {
        var firstDigit = code.substring(0, 1);
        StatusCodeClass.fromFirstDigit(Integer.parseInt(firstDigit))
                .ifPresent(reasonClass -> printReasonClassPhrases(getReasonClass(reasonClass)));
    }

    private static <T> String toStatusCodeString(T statusCode) {
        return (statusCode instanceof Integer)
                ? Integer.toString((Integer) statusCode)
                : String.valueOf(statusCode);
    }
}
