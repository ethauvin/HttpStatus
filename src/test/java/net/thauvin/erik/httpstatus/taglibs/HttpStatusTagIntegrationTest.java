/*
 * HttpStatusTagIntegrationTest.java
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

package net.thauvin.erik.httpstatus.taglibs;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpStatusTagIntegrationTest {

    @SuppressWarnings({"PMD.RelianceOnDefaultCharset", "PMD.SignatureDeclareThrowsException"})
    private String renderJsp(String jspName) throws Exception {
        var baseDir = new File("build/tomcat");
        var ignored = baseDir.mkdirs();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.setPort(0);

        var webappDir = new File("src/test/resources/jsp").getAbsolutePath();
        var ctx = tomcat.addWebapp("", webappDir);

        var compiledClasses = new File("build/main").getAbsolutePath();

        var resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        compiledClasses,
                        "/"
                )
        );
        ctx.setResources(resources);
        ctx.getResources().setCachingAllowed(false);

        ctx.getServletContext().setAttribute(
                "jakarta.servlet.context.tempdir",
                new File("build/tomcat-tmp").getAbsoluteFile()
        );

        tomcat.start();

        int port = tomcat.getConnector().getLocalPort();
        var url = new URL("http://localhost:" + port + "/" + jspName);

        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        var stream = conn.getResponseCode() >= 400
                ? conn.getErrorStream()
                : conn.getInputStream();

        var sb = new StringBuilder();
        try (var reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }

        tomcat.stop();
        tomcat.destroy();

        return sb.toString().trim();
    }

    @Test
    void test502Jsp() throws Exception {
        var output = renderJsp("502.jsp");
        assertTrue(output.contains("Code: 502<br/>"), String.format("Invalid code%n%s", output));
        assertTrue(output.contains("Message: <br/>"), String.format("Invalid message%n%s", output));
        assertTrue(output.contains("Reason: Bad Gateway<br/>"), String.format("Invalid reason%n%s", output));
        assertTrue(output.contains("Cause: <br/>"), String.format("Invalid cause%n%s", output));
    }

    @Test
    void testBasicJsp() throws Exception {
        var output = renderJsp("basic.jsp");
        assertTrue(output.contains("Code: 500<br/>"), String.format("Invalid code%n%s", output));
        assertTrue(output.contains("Message: Something went wrong<br/>"), String.format("Invalid message%n%s", output));
        assertTrue(output.contains("Reason: Internal Server Error<br/>"), String.format("Invalid reason%n%s", output));
        assertTrue(output.contains("Cause: <br/>"), String.format("Invalid cause%n%s", output));
    }

    @Test
    void testDefaultsJsp() throws Exception {
        var output = renderJsp("defaults.jsp");
        assertTrue(output.contains("Message: Default message<br/>"), String.format("Invalid message%n%s", output));
        assertTrue(output.contains("Reason: Default reason<br/>"), String.format("Invalid reason%n%s", output));
        assertTrue(output.contains("Cause: Default cause<br/>"), String.format("Invalid cause%n%s", output));
    }
}