/*
 * StatusCodeClass.java
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The <code>StatusCodeClass</code> enum represents the classification of HTTP status codes into groups
 * based on their first digit.
 * <p>
 * Each enum constant corresponds to a specific class of HTTP statuses:
 * <p>
 * <ul>
 * <li>{@link #INFORMATIONAL}: Represents HTTP responses with a status code starting with {@code 1}</li>
 * <li>{@link #SUCCESSFUL}: Represents HTTP responses with a status code starting with {@code 2}</li>
 * <li>{@link #REDIRECTION}: Represents HTTP responses with a status code starting with {@code 3}</li>
 * <li>{@link #CLIENT_ERROR}: Represents HTTP responses with a status code starting with {@code 4}</li>
 * <li>{@link #SERVER_ERROR}: Represents HTTP responses with a status code starting with {@code 5}</li>
 * </ul>
 *
 * @since 2.0.0
 */
public enum StatusCodeClass {
    /**
     * Represents HTTP responses with a status code starting with {@code 1}.
     */
    INFORMATIONAL(1),
    /**
     * Represents HTTP responses with a status code starting with {@code 2}.
     */
    SUCCESSFUL(2),
    /**
     * Represents HTTP responses with a status code starting with {@code 3}.
     */
    REDIRECTION(3),
    /**
     * Represents HTTP responses with a status code starting with {@code 4}.
     */
    CLIENT_ERROR(4),
    /**
     * Represents HTTP responses with a status code starting with {@code 5}.
     */
    SERVER_ERROR(5);

    // Static map for O(1) lookup - initialized once
    private static final Map<Integer, StatusCodeClass> LOOKUP_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(StatusCodeClass::getFirstDigit, Function.identity()));
    private final int firstDigit;

    /**
     * Constructs a new instance of {@link StatusCodeClass} with the specified first digit.
     *
     * @param firstDigit The first digit of the HTTP status code class. This value determines the classification of the
     *                   HTTP status codes (e.g., {@code 1} for informational, {@code 2} for successful, etc.)
     */
    StatusCodeClass(int firstDigit) {
        this.firstDigit = firstDigit;
    }


    /**
     * Retrieves the {@link StatusCodeClass} corresponding to the given first digit.
     *
     * @param firstDigit The first digit of the HTTP status code as a string.
     * @return An {@link Optional} containing the matching {@link StatusCodeClass} for the provided digit,
     * or an empty {@link Optional} if no match is found.
     * @see #fromFirstDigit(int)
     */
    public static Optional<StatusCodeClass> fromFirstDigit(int firstDigit) {
        return Optional.ofNullable(LOOKUP_MAP.get(firstDigit));
    }

    /**
     * Retrieves the first digit representing the classification of HTTP status codes.
     *
     * @return The first digit of the HTTP status code class.
     */
    public int getFirstDigit() {
        return firstDigit;
    }
}
