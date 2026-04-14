/*
 * Reasons.java
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

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Loads HTTP status reason phrases from the resource bundle and provides
 * lookup utilities for individual status codes and status code classes.
 * <p>
 * The reason phrases are stored in a sorted, immutable map for predictable
 * iteration order and thread‑safe access.
 */
public final class Reasons {

    /**
     * The resource bundle base name containing the reason phrases.
     */
    static final String BUNDLE_BASENAME = "net.thauvin.erik.httpstatus.reasons";

    /**
     * Immutable map of status code strings to their reason phrases.
     * <p>
     * Populated once at class initialization.
     */
    private static final Map<String, String> REASON_PHRASES;

    static {
        var bundle = ResourceBundle.getBundle(BUNDLE_BASENAME);
        var map = new TreeMap<String, String>();
        for (var key : bundle.keySet()) {
            map.put(key, bundle.getString(key));
        }
        REASON_PHRASES = Map.copyOf(map);
    }

    /**
     * Prevents instantiation of this utility class.
     */
    private Reasons() {
        throw new UnsupportedOperationException("Illegal constructor call.");
    }

    /**
     * Returns all reason phrases belonging to the given status code class.
     * <p>
     * The returned map is sorted by status code.
     */
    public static Map<String, String> getReasonClass(StatusCodeClass reasonClass) {
        var firstDigit = Character.forDigit(reasonClass.getFirstDigit(), 10);
        var result = new TreeMap<String, String>();

        for (var entry : REASON_PHRASES.entrySet()) {
            if (entry.getKey().charAt(0) == firstDigit) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Returns the reason phrase for the given status code, or null if none exists.
     * <p>
     * Accepts either an integer or string representation of the status code.
     */
    public static String getReasonPhrase(Object statusCode) {
        return REASON_PHRASES.get(toStatusCodeString(statusCode));
    }

    /**
     * Returns the reason phrase for the given status code, or the provided
     * default value if no phrase is defined.
     */
    public static String getReasonPhrase(Object statusCode, String defaultReason) {
        return Optional.ofNullable(getReasonPhrase(statusCode)).orElse(defaultReason);
    }

    private static boolean isStatusCodeClass(String code) {
        return code.length() == 3
                && code.charAt(0) >= '1'
                && code.charAt(0) <= '5'
                && code.charAt(1) == 'x'
                && code.charAt(2) == 'x';
    }

    /**
     * Prints reason phrases for the given status code(s) or status code class(es).
     * <p>
     * If no arguments are provided, prints all reason phrases.
     */
    public static void main(String... args) {
        if (args.length == 0) {
            printAllReasonPhrases();
            return;
        }
        for (var arg : args) {
            processStatusCode(arg);
        }
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private static void printAllReasonPhrases() {
        REASON_PHRASES.forEach(Reasons::printReasonPhrase);
        System.out.println("Total: " + REASON_PHRASES.size());
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
        var firstDigit = code.charAt(0) - '0';
        StatusCodeClass.fromFirstDigit(firstDigit)
                .ifPresent(reasonClass ->
                        getReasonClass(reasonClass).forEach(Reasons::printReasonPhrase));
    }

    /**
     * Converts a status code to its string representation.
     * <p>
     * Supports both integer and string inputs.
     */
    private static String toStatusCodeString(Object statusCode) {
        return (statusCode instanceof Integer i)
                ? Integer.toString(i)
                : String.valueOf(statusCode);
    }
}