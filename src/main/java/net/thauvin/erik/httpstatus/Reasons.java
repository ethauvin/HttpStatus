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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * Loads HTTP status reason phrases from the resource bundle and provides
 * lookup utilities for individual status codes and status code classes.
 * <p>
 * The reason phrases are stored in a sorted, immutable map for predictable
 * iteration order and thread‑safe access.
 */
@NullMarked
@SuppressWarnings("PMD.UseConcurrentHashMap")
public final class Reasons {

    /**
     * The resource bundle base name containing the reason phrases.
     */
    static final String BUNDLE_BASENAME = "net.thauvin.erik.httpstatus.reasons";

    /**
     * Immutable, sorted map of status code strings to their reason phrases.
     * <p>
     * Populated once at class initialization.
     */
    private static final Map<String, String> REASON_PHRASES;

    /**
     * Immutable, sorted per-class views of {@link #REASON_PHRASES}, keyed by
     * {@link StatusCodeClass}.
     * <p>
     * Precomputed once at class initialization so repeated calls to
     * {@link #getReasonClass(StatusCodeClass)} don't rescan the full map.
     */
    private static final Map<StatusCodeClass, Map<String, String>> REASON_PHRASES_BY_CLASS;

    static {
        var bundle = ResourceBundle.getBundle(BUNDLE_BASENAME);
        var sorted = new TreeMap<String, String>();
        bundle.keySet().forEach(k -> sorted.put(k, bundle.getString(k)));
        REASON_PHRASES = Collections.unmodifiableSortedMap(sorted);

        var mutable = new EnumMap<StatusCodeClass, SortedMap<String, String>>(StatusCodeClass.class);
        REASON_PHRASES.forEach((code, phrase) ->
                StatusCodeClass.fromFirstDigit(code.charAt(0) - '0')
                        .ifPresent(rc -> mutable.computeIfAbsent(rc, x -> new TreeMap<>())
                                .put(code, phrase))
        );

        Map<StatusCodeClass, Map<String, String>> byClass = new EnumMap<>(StatusCodeClass.class);
        mutable.forEach((rc, m) -> byClass.put(rc, Collections.unmodifiableSortedMap(m)));
        for (var rc : StatusCodeClass.values()) {
            byClass.putIfAbsent(rc, Collections.emptySortedMap());
        }

        REASON_PHRASES_BY_CLASS = Collections.unmodifiableMap(byClass);
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
     *
     * @param reasonClass the status code class to look up
     * @return a sorted map of status codes to reason phrases for the given class
     */
    public static Map<String, String> getReasonClass(StatusCodeClass reasonClass) {
        return REASON_PHRASES_BY_CLASS.get(reasonClass);
    }

    /**
     * Returns the reason phrase for the given status code, or null if none exists.
     *
     * @param statusCode the HTTP status code
     * @return the reason phrase, or {@code null} if none exists
     */
    @Nullable
    public static String getReasonPhrase(int statusCode) {
        return REASON_PHRASES.get(Integer.toString(statusCode));
    }

    /**
     * Returns the reason phrase for the given status code, or null if none exists.
     *
     * @param statusCode the HTTP status code as a string
     * @return the reason phrase, or {@code null} if none exists
     */
    @Nullable
    public static String getReasonPhrase(String statusCode) {
        return REASON_PHRASES.get(statusCode);
    }

    /**
     * Returns the reason phrase for the given status code, or the provided
     * default value if no phrase is defined.
     *
     * @param statusCode    the HTTP status code
     * @param defaultReason the default reason phrase to return if none exists
     * @return the reason phrase, or {@code defaultReason} if none exists
     */
    public static String getReasonPhrase(int statusCode, String defaultReason) {
        return Optional.ofNullable(getReasonPhrase(statusCode)).orElse(defaultReason);
    }

    /**
     * Returns the reason phrase for the given status code, or the provided
     * default value if no phrase is defined.
     *
     * @param statusCode    the HTTP status code as a string
     * @param defaultReason the default reason phrase to return if none exists
     * @return the reason phrase, or {@code defaultReason} if none exists
     */
    public static String getReasonPhrase(String statusCode, String defaultReason) {
        return Optional.ofNullable(getReasonPhrase(statusCode)).orElse(defaultReason);
    }

    /**
     * Checks whether the given code represents a status code class pattern (e.g., {@code 2xx}).
     *
     * @param code the code to check
     * @return {@code true} if the code is a status code class pattern, {@code false} otherwise
     */
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
     *
     * @param args the status codes or status code classes to print (e.g., "404", "2xx")
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

    /**
     * Prints a single reason phrase.
     *
     * @param code   the status code
     * @param phrase the reason phrase
     */
    @SuppressWarnings("PMD.SystemPrintln")
    private static void printReasonPhrase(String code, String phrase) {
        System.out.println(code + ": " + phrase);
    }

    /**
     * Prints the reason phrase for a single status code if it exists.
     *
     * @param code the status code as a string
     */
    private static void printSingleStatusCode(String code) {
        Optional.ofNullable(REASON_PHRASES.get(code))
                .ifPresent(phrase -> printReasonPhrase(code, phrase));
    }

    /**
     * Processes a status code argument, handling both individual codes and class patterns.
     *
     * @param code the status code or class pattern to process
     */
    private static void processStatusCode(String code) {
        if (isStatusCodeClass(code)) {
            processStatusCodeClass(code);
        } else {
            printSingleStatusCode(code);
        }
    }

    /**
     * Processes a status code class pattern and prints all matching reason phrases.
     *
     * @param code the status code class pattern (e.g., "4xx")
     */
    private static void processStatusCodeClass(String code) {
        var firstDigit = code.charAt(0) - '0';
        StatusCodeClass.fromFirstDigit(firstDigit)
                .ifPresent(reasonClass ->
                        getReasonClass(reasonClass).forEach(Reasons::printReasonPhrase));
    }
}