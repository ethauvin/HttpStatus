/*
 * StatusCode.java
 *
 * Copyright (c) 2015-2021, Erik C. Thauvin (erik@thauvin.net)
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

import java.io.Serializable;

/**
 * The <code>StatusCode</code> class implements methods to check the class of a HTTP status code.
 *
 * @author <a href="mailto:erik@thauvin.net" target="_blank">Erik C. Thauvin</a>
 */
public class StatusCode implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;

    /**
     * Creates a new statusCode object.
     */
    public StatusCode() {
        // Default construtor.
    }
    
    /**
     * Creates a new StatusCode object.
     *
     * @param code The status code.
     */
    public StatusCode(final int code) {
        this.code = code;
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
     * @return The reason, or <code>null</code>.
     */
    public String getReason() {
        return Reasons.getReasonPhrase(code);
    }

    /**
     * Checks if the status code is a client error.
     *
     * @return <code>true</code> if the status code is a client error, <code>false</code> otherwise.
     */
    public boolean isClientError() {
        return code >= 400 && code < 500;
    }

    /**
     * Checks if the status code is a client or server error.
     *
     * @return <code>true</code> if the status code is an error, <code>false</code> otherwise.
     */
    public boolean isError() {
        return code >= 400 && code < 600;
    }

    /**
     * Checks if the status code is informational.
     *
     * @return <code>true</code> if the status code is informational, <code>false</code> otherwise.
     */
    public boolean isInfo() {
        return code >= 100 && code < 200;
    }

    /**
     * Checks if the status code is a redirect.
     *
     * @return <code>true</code> if the status code is a redirect, <code>false</code> otherwise.
     */
    public boolean isRedirect() {
        return code >= 300 && code < 400;
    }

    /**
     * Checks if the status code is a server error.
     *
     * @return <code>true</code> if the status code is a server error, <code>false</code> otherwise.
     */
    public boolean isServerError() {
        return code >= 500 && code < 600;
    }

    /**
     * Checks if the status code is a (<code>OK</code>) success.
     *
     * @return <code>true</code> if the status code is a success, <code>false</code> otherwise.
     */
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }

    /**
     * Checks if the status code is valid.
     *
     * @return <code>true</code> if the status code is valid, <code>false</code> otherwise.
     */
    public boolean isValid() {
        return code >= 100 && code < 600;
    }

    /**
     * Sets the status code.
     *
     * @param code The HTTP status code.
     */
    public void setCode(final int code) {
        this.code = code;
    }
}
