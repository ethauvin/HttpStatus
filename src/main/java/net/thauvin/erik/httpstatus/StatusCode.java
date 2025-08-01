/*
 * StatusCode.java
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

import java.io.Serial;
import java.io.Serializable;

/**
 * The <code>StatusCode</code> bean implements methods to check the class of an HTTP status code.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @since 1.1.0
 */
public class StatusCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int code;

    /**
     * Creates a new StatusCode object.
     */
    public StatusCode() {
        // Default constructor.
    }

    /**
     * Creates a new StatusCode object.
     *
     * @param code the status code
     * @since 2.0.0
     */
    public StatusCode(int code) {
        this.code = code;
    }

    /**
     * Checks if the status code is a client error. (e.g.: <code>Internal Server Error</code>)
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a client error, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isClientError(int code) {
        return code >= 400 && code < 500;
    }

    /**
     * Checks if the status code is a client or server error.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is an error, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isError(int code) {
        return code >= 400 && code < 600;
    }

    /**
     * Checks if the status code is informational.
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
     * Checks if the status code is informational.
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
     * Checks if the status code is a redirect.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a redirect, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isRedirect(int code) {
        return code >= 300 && code < 400;
    }

    /**
     * Checks if the status code is a server error.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is a server error, <code>false</code> otherwise
     * @since 2.0.0
     */
    public static boolean isServerError(int code) {
        return code >= 500 && code < 600;
    }

    /**
     * Checks if the status code is a (<code>OK</code>) success.
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
     * Checks if the status code is a (<code>OK</code>) success.
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
     * Checks if the status code is valid.
     *
     * @param code the status code
     * @return <code>true</code> if the status code is valid, <code>false</code> otherwise
     */
    public static boolean isValid(int code) {
        return code == 783 || (code >= 100 && code < 600);
    }

    /**
     * Returns the status code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the reason for the status code.
     *
     * @return The reason, or <code>null</code>
     * @see Reasons#getReasonPhrase(int)
     */
    public String getReason() {
        return Reasons.getReasonPhrase(code);
    }

    /**
     * Checks if the status code is a client error. (e.g.: <code>Internal Server Error</code>)
     *
     * @return <code>true</code> if the status code is a client error, <code>false</code> otherwise
     */
    public boolean isClientError() {
        return isClientError(code);
    }

    /**
     * Checks if the status code is a client or server error.
     *
     * @return <code>true</code> if the status code is an error, <code>false</code> otherwise
     */
    public boolean isError() {
        return isError(code);
    }

    /**
     * Checks if the status code is informational.
     *
     * @return <code>true</code> if the status code is informational, <code>false</code> otherwise
     */
    public boolean isInfo() {
        return isInfo(code);
    }

    /**
     * Checks if the status code is informational.
     *
     * @return <code>true</code> if the status code is informational, <code>false</code> otherwise
     * @since 2.0.0
     */
    public boolean isInformational() {
        return isInfo(code);
    }

    /**
     * Checks if the status code is a redirect.
     *
     * @return <code>true</code> if the status code is a redirect, <code>false</code> otherwise
     */
    public boolean isRedirect() {
        return isRedirect(code);
    }

    /**
     * Checks if the status code is a server error.
     *
     * @return <code>true</code> if the status code is a server error, <code>false</code> otherwise
     */
    public boolean isServerError() {
        return isServerError(code);
    }

    /**
     * Checks if the status code is a (<code>OK</code>) success.
     *
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise
     */
    public boolean isSuccess() {
        return isSuccess(code);
    }

    /**
     * Checks if the status code is a (<code>OK</code>) success.
     *
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise
     * @since 2.0.0
     */
    public boolean isSuccessful() {
        return isSuccess(code);
    }

    /**
     * Checks if the status code is valid.
     *
     * @return <code>true</code> if the status code is valid, <code>false</code> otherwise
     */
    public boolean isValid() {
        return isValid(code);
    }

    /**
     * Sets the status code.
     *
     * @param code The HTTP status code
     */
    public void setCode(int code) {
        this.code = code;
    }
}
