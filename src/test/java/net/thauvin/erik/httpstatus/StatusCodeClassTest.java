/*
 * StatusCodeClassTest.java
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

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StatusCodeClassTest {

    @Test
    void firstDigitClientError() {
        assertThat(StatusCodeClass.CLIENT_ERROR.getFirstDigit()).isEqualTo(4);
    }

    @Test
    void firstDigitInformational() {
        assertThat(StatusCodeClass.INFORMATIONAL.getFirstDigit()).isEqualTo(1);
    }

    @Test
    void firstDigitRedirection() {
        assertThat(StatusCodeClass.REDIRECTION.getFirstDigit()).isEqualTo(3);
    }

    @Test
    void firstDigitRoundTrip() {
        for (var statusClass : StatusCodeClass.values()) {
            var digit = statusClass.getFirstDigit();
            var lookedUp = StatusCodeClass.fromFirstDigit(digit);
            assertThat(lookedUp).isPresent().contains(statusClass);
        }
    }

    @Test
    void firstDigitServerError() {
        assertThat(StatusCodeClass.SERVER_ERROR.getFirstDigit()).isEqualTo(5);
    }

    @Test
    void firstDigitSuccessful() {
        assertThat(StatusCodeClass.SUCCESSFUL.getFirstDigit()).isEqualTo(2);
    }

    @Test
    void fromFirstDigitWithBoundaryValueString() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(5);
        assertThat(result).isPresent().contains(StatusCodeClass.SERVER_ERROR);
    }

    @Test
    void fromFirstDigitWithInvalidString() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(9);
        assertThat(result).isNotPresent();
    }

    @Test
    void fromFirstDigitWithNegativeInt() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(-1);
        assertThat(result).isNotPresent();
    }

    @Test
    void fromFirstDigitWithValidInt() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(3);
        assertThat(result).isPresent().contains(StatusCodeClass.REDIRECTION);
    }

    @Test
    void fromFirstDigitWithZeroInt() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(0);
        assertThat(result).isNotPresent();
    }
}