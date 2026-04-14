/*
 * StatusCodeClass.java
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

/**
 * Represents the classification of HTTP status codes based on their first digit.
 * Each enum constant corresponds to one of the standard HTTP status code classes.
 *
 * <ul>
 *   <li>{@link #INFORMATIONAL} — status codes starting with {@code 1}</li>
 *   <li>{@link #SUCCESSFUL} — status codes starting with {@code 2}</li>
 *   <li>{@link #REDIRECTION} — status codes starting with {@code 3}</li>
 *   <li>{@link #CLIENT_ERROR} — status codes starting with {@code 4}</li>
 *   <li>{@link #SERVER_ERROR} — status codes starting with {@code 5}</li>
 * </ul>
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @since 2.0.0
 */
public enum StatusCodeClass {
    /**
     * Status codes starting with {@code 1}.
     */
    INFORMATIONAL(1),

    /**
     * Status codes starting with {@code 2}.
     */
    SUCCESSFUL(2),

    /**
     * Status codes starting with {@code 3}.
     */
    REDIRECTION(3),

    /**
     * Status codes starting with {@code 4}.
     */
    CLIENT_ERROR(4),

    /**
     * Status codes starting with {@code 5}.
     */
    SERVER_ERROR(5);

    private static final Map<Integer, StatusCodeClass> LOOKUP_MAP = Map.of(
            1, INFORMATIONAL,
            2, SUCCESSFUL,
            3, REDIRECTION,
            4, CLIENT_ERROR,
            5, SERVER_ERROR
    );

    private final int firstDigit;

    /**
     * Creates a new classification based on the first digit of the status code.
     */
    StatusCodeClass(int firstDigit) {
        this.firstDigit = firstDigit;
    }

    /**
     * Returns the classification corresponding to the given first digit.
     * The digit must be between {@code 1} and {@code 5}.
     * <p>
     * Returns an empty {@link Optional} if the digit does not correspond
     * to a known HTTP status code class.
     */
    public static Optional<StatusCodeClass> fromFirstDigit(int firstDigit) {
        return Optional.ofNullable(LOOKUP_MAP.get(firstDigit));
    }

    /**
     * Returns the first digit representing this HTTP status code class.
     */
    public int getFirstDigit() {
        return firstDigit;
    }
}
