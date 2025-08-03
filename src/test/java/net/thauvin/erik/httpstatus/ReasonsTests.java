/*
 * ReasonsTests.java
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

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Reasons Tests.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class ReasonsTests {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Reasons.BUNDLE_BASENAME);

    private static Stream<String> provideBundleKeys() {
        return BUNDLE.keySet().stream();
    }

    @Test
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void privateConstructor() throws Exception {
        var constructor = Reasons.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor is not private");
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause(), e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideBundleKeys")
    void reasonPhrase(String key) {
        assertThat(Reasons.getReasonPhrase(key))
                .as("getReasonPhrase(%s)", key)
                .isEqualTo(BUNDLE.getString(key));
    }

    @Test
    void reasonPhraseWithDefault() {
        assertThat(Reasons.getReasonPhrase("404", "Unknown Reason"))
                .isEqualTo("Not Found");
    }

    @Test
    void reasonPhraseWithIntStatusAndDefault() {
        assertThat(Reasons.getReasonPhrase(404, "Unknown Reason"))
                .isEqualTo("Not Found");
    }

    @Test
    void reasonPhraseWithIntStatuses() {
        try (var softly = new AutoCloseableSoftAssertions()) {
            for (var key : BUNDLE.keySet()) {
                softly.assertThat(Reasons.getReasonPhrase(Integer.parseInt(key))).as("getReasonPhrase(%s)", key)
                        .isEqualTo(BUNDLE.getString(key));
            }
        }
    }

    @Test
    void reasonPhraseWithUnknownIntStatusAndDefault() {
        assertThat(Reasons.getReasonPhrase(666, "Unknown Reason"))
                .isEqualTo("Unknown Reason");
    }

    @Test
    void reasonPhraseWithUnknownStatusAndDefault() {
        assertThat(Reasons.getReasonPhrase("666", "Unknown Reason"))
                .isEqualTo("Unknown Reason");
    }

    @Nested
    @DisplayName("Reason Class Tests")
    class ReasonClassTests {
        private static Stream<String> provideClientErrorReasonKeys() {
            var reasons = Reasons.getReasonClass(StatusCodeClass.CLIENT_ERROR);
            return reasons.keySet().stream();
        }

        private static Stream<String> provideInformationalReasonKeys() {
            var reasons = Reasons.getReasonClass(StatusCodeClass.INFORMATIONAL);
            return reasons.keySet().stream();
        }

        private static Stream<String> provideRedirectionReasonKeys() {
            var reasons = Reasons.getReasonClass(StatusCodeClass.REDIRECTION);
            return reasons.keySet().stream();
        }

        private static Stream<String> provideServerErrorReasonKeys() {
            var reasons = Reasons.getReasonClass(StatusCodeClass.SERVER_ERROR);
            return reasons.keySet().stream();
        }

        private static Stream<String> provideSuccessfulReasonKeys() {
            var reasons = Reasons.getReasonClass(StatusCodeClass.SUCCESSFUL);
            return reasons.keySet().stream();
        }

        @ParameterizedTest
        @MethodSource("provideClientErrorReasonKeys")
        void reasonClassClientError(String key) {
            assertThat(key).startsWith("4");
        }

        @ParameterizedTest
        @MethodSource("provideInformationalReasonKeys")
        void reasonClassInformational(String key) {
            assertThat(key).startsWith("1");
        }

        @ParameterizedTest
        @MethodSource("provideRedirectionReasonKeys")
        void reasonClassRedirection(String key) {
            assertThat(key).startsWith("3");
        }

        @ParameterizedTest
        @MethodSource("provideServerErrorReasonKeys")
        void reasonClassServerError(String key) {
            assertThat(key).startsWith("5");
        }

        @ParameterizedTest
        @MethodSource("provideSuccessfulReasonKeys")
        void reasonClassSuccessful(String key) {
            assertThat(key).startsWith("2");
        }
    }
}
