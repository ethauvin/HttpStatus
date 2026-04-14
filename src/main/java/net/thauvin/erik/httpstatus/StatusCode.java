/*
 * StatusCode.java
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an HTTP status code and provides convenience methods for determining
 * its classification (informational, success, redirect, client error, server error,
 * or project-defined extension codes).
 * <p>
 * Static methods operate on a raw status code, while instance methods operate on
 * the stored {@code code} value. Several methods have both short and long forms
 * (e.g., {@code isInfo} and {@code isInformational}) for readability.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @since 1.1.0
 */
@SuppressFBWarnings("MOM_MISLEADING_OVERLOAD_MODEL")
public class StatusCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    /**
     * Creates a new StatusCode with no initial value.
     */
    public StatusCode() {
        // Default constructor.
    }

    /**
     * Creates a new StatusCode with the given value.
     *
     * @param code the status code
     * @since 2.0.0
     */
    public StatusCode(int code) {
        this.code = code;
    }

    /**
     * Returns the reason phrase for the given status code, or {@code null}
     * if no reason is defined.
     *
     * @param code the status code
     * @return The reason, or <code>null</code>
     * @see Reasons#getReasonPhrase(Object)
     * @since 2.0.0
     */
    public static String getReason(int code) {
        return Reasons.getReasonPhrase(code);
    }

    /**
     * Returns {@code true} if the code represents a client error (4xx).
     * Example: {@code 404 Not Found}.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a client error, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isClientError(int code) {
        return code >= 400 && code < 500;
    }

    /**
     * Returns {@code true} if the code represents any error (4xx or 5xx).
     *
     * @param code the status code
     * @return <code>true</code> if the status code is an error, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isError(int code) {
        return code >= 400 && code < 600;
    }

    /**
     * Returns {@code true} if the code represents an informational response (1xx).
     *
     * @param code the status code
     * @return <code>true</code> if the status code is informational, <code>false</code> otherwise
     * @see #isInformational(int)
     * @since 2.0.0
     */
    public static boolean isInfo(int code) {
        return code >= 100 && code < 200;
    }

    /**
     * Alias for {@link #isInfo(int)}.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is informational, <code>false</code>
     * @see #isInfo(int)
     * @since 2.0.0
     */
    public static boolean isInformational(int code) {
        return isInfo(code);
    }

    /**
     * Returns {@code true} if the code represents a redirect (3xx).
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a redirect, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isRedirect(int code) {
        return code >= 300 && code < 400;
    }

    /**
     * Returns {@code true} if the code represents a server error (5xx).
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a server error, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isServerError(int code) {
        return code >= 500 && code < 600;
    }

    /**
     * Returns {@code true} if the code represents a successful response (2xx).
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise
     * @see #isSuccessful(int)
     * @since 2.0.0
     */
    public static boolean isSuccess(int code) {
        return code >= 200 && code < 300;
    }

    /**
     * Alias for {@link #isSuccess(int)}.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise
     * @see #isSuccess(int)
     * @since 2.0.0
     */
    public static boolean isSuccessful(int code) {
        return isSuccess(code);
    }

    /**
     * Returns {@code true} if the code is a valid HTTP status code.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is valid, <code>false</code> otherwise
     */
    public static boolean isValid(int code) {
        return code == 783 || (code >= 100 && code < 600);
    }

    /**
     * Returns the stored status code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the stored status code.
     *
     * @param code The HTTP status code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Returns the reason phrase for the stored status code, or {@code null}
     * if no reason is defined.
     *
     * @return The reason, or <code>null</code>
     * @see Reasons#getReasonPhrase(Object)
     */
    public String getReason() {
        return Reasons.getReasonPhrase(code);
    }

    /**
     * Returns {@code true} if the stored code represents a client error (4xx).
     *
     * @return <code>true</code> if the status code is a client error, <code>false</code> otherwise
     */
    public boolean isClientError() {
        return isClientError(code);
    }

    /**
     * Returns {@code true} if the stored code represents any error (4xx or 5xx).
     *
     * @return <code>true</code> if the status code is an error, <code>false</code> otherwise
     */
    public boolean isError() {
        return isError(code);
    }

    /**
     * Returns {@code true} if the stored code represents an informational response (1xx).
     *
     * @return <code>true</code> if the status code is informational, <code>false</code> otherwise
     */
    public boolean isInfo() {
        return isInfo(code);
    }

    /**
     * Alias for {@link #isInfo()}.
     *
     * @return <code>true</code> if the status code is informational, <code>false</code> otherwise
     * @since 2.0.0
     */
    public boolean isInformational() {
        return isInfo(code);
    }

    /**
     * Returns {@code true} if the stored code represents a redirect (3xx).
     *
     * @return <code>true</code> if the status code is a redirect, <code>false</code> otherwise
     */
    public boolean isRedirect() {
        return isRedirect(code);
    }

    /**
     * Returns {@code true} if the stored code represents a server error (5xx).
     *
     * @return <code>true</code> if the status code is a server error, <code>false</code> otherwise
     */
    public boolean isServerError() {
        return isServerError(code);
    }

    /**
     * Returns {@code true} if the stored code represents a successful response (2xx).
     *
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise
     */
    public boolean isSuccess() {
        return isSuccess(code);
    }

    /**
     * Alias for {@link #isSuccess()}.
     *
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise
     * @since 2.0.0
     */
    public boolean isSuccessful() {
        return isSuccess(code);
    }

    /**
     * Returns {@code true} if the stored code is a valid HTTP status code.
     *
     * @return <code>true</code> if the status code is valid, <code>false</code> otherwise
     */
    public boolean isValid() {
        return isValid(code);
    }
}
