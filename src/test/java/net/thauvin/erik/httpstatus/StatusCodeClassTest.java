/*
 * StatusCodeClassTest.java
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StatusCodeClassTest {
    @Test
    void fromFirstDigitWithBoundaryValueString() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit("5");
        assertThat(result).isPresent().contains(StatusCodeClass.SERVER_ERROR);
    }

    @Test
    void fromFirstDigitWithInvalidString() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit("9");
        assertThat(result).isNotPresent();
    }

    @Test
    void fromFirstDigitWithNegativeInt() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(-1);
        assertThat(result).isNotPresent();
    }

    @Test
    void fromFirstDigitWithNonNumericString() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit("A");
        assertThat(result).isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromFirstDigitWithNullString(String input) {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(input);
        assertThat(result).isNotPresent();
    }

    @Test
    void fromFirstDigitWithValidInt() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(3);
        assertThat(result).isPresent().contains(StatusCodeClass.REDIRECTION);
    }

    @Test
    void fromFirstDigitWithValidString() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit("2");
        assertThat(result).isPresent().contains(StatusCodeClass.SUCCESSFUL);
    }

    @Test
    void fromFirstDigitWithZeroInt() {
        Optional<StatusCodeClass> result = StatusCodeClass.fromFirstDigit(0);
        assertThat(result).isNotPresent();
    }
}