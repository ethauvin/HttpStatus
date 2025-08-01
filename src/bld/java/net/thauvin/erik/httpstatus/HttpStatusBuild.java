/*
 * HttpStatusBuild.java
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

import rife.bld.BuildCommand;
import rife.bld.Project;
import rife.bld.extension.ExecOperation;
import rife.bld.extension.JacocoReportOperation;
import rife.bld.extension.PmdOperation;
import rife.bld.publish.*;
import rife.tools.exceptions.FileUtilsErrorException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.Attributes;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.compile;
import static rife.bld.dependencies.Scope.test;
import static rife.bld.operations.JavadocOptions.DocLinkOption.NO_MISSING;

public class HttpStatusBuild extends Project {
    static final String TEST_RESULTS_DIR = "build/test-results/test/";
    final PmdOperation pmdOp = new PmdOperation()
            .fromProject(this)
            .failOnViolation(true)
            .ruleSets("config/pmd.xml");

    public HttpStatusBuild() {
        pkg = "net.thauvin.erik.httpstatus";
        name = "HttpStatus";
        version = version(2, 0, 0, "SNAPSHOT");

        var description = "HTTP Status Codes & JSP Tag Library";
        var url = "https://github.com/ethauvin/HttpStatus";

        mainClass = "net.thauvin.erik.httpstatus.Reasons";

        javaRelease = 17;

        downloadSources = true;
        autoDownloadPurge = true;
        repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);

        scope(compile)
                .include(dependency("jakarta.servlet", "jakarta.servlet-api", version(6, 1, 0)))
                .include(dependency("jakarta.servlet.jsp", "jakarta.servlet.jsp-api", version(4, 0, 0)))
                .include(dependency("jakarta.el", "jakarta.el-api", version(6, 0, 1)));
        scope(test)
                .include(dependency("org.jetbrains", "annotations", version(26, 0, 2)))
                .include(dependency("org.mockito", "mockito-core", version(5, 18, 0)))
                .include(dependency("org.assertj", "assertj-core", version(3, 27, 3)))
                .include(dependency("org.junit.jupiter", "junit-jupiter", version(5, 13, 4)))
                .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1, 13, 4)));

        jarOperation().manifestAttribute(Attributes.Name.MAIN_CLASS, pkg + '.' + "Reasons");

        javadocOperation().javadocOptions()
                .docTitle(description + ' ' + version.toString())
                .docLint(NO_MISSING)
                .link("https://jakarta.ee/specifications/platform/9/apidocs/");

        publishOperation()
                .repository(version.isSnapshot() ? repository(CENTRAL_SNAPSHOTS.location())
                        .withCredentials(property("central.user"), property("central.password"))
                        : repository(CENTRAL_RELEASES.location())
                        .withCredentials(property("central.user"), property("central.password")))
                .repository(repository("github"))
                .info(new PublishInfo()
                        .groupId(pkg)
                        .artifactId(name.toLowerCase())
                        .name(name)
                        .version(version)
                        .description(description)
                        .url(url)
                        .developer(new PublishDeveloper()
                                .id("ethauvin")
                                .name("Erik C. Thauvin")
                                .email("erik@thauvin.net")
                                .url("https://erik.thauvin.net/")
                        )
                        .license(new PublishLicense()
                                .name("The BSD 3-Clause License")
                                .url("https://opensource.org/licenses/BSD-3-Clause")
                        )
                        .scm(new PublishScm()
                                .connection("scm:git:" + url + ".git")
                                .developerConnection("scm:git:git@github.com:ethauvin/" + name + ".git")
                                .url(url))
                        .signKey(property("sign.key"))
                        .signPassphrase(property("sign.passphrase")));
    }

    public static void main(String[] args) {
        new HttpStatusBuild().start(args);
    }

    @BuildCommand(summary = "Generates JaCoCo Reports")
    public void jacoco() throws Exception {
        var op = new JacocoReportOperation().fromProject(this);
        //op.testToolOptions("--reports-dir=" + TEST_RESULTS_DIR);

        Exception ex = null;
        try {
            op.execute();
        } catch (Exception e) {
            ex = e;
        }

        renderWithXunitViewer();

        if (ex != null) {
            throw ex;
        }
    }

    @BuildCommand(summary = "Runs PMD analysis")
    public void pmd() throws Exception {
        pmdOp.execute();
    }

    @BuildCommand(value = "pmd-cli", summary = "Runs PMD analysis (CLI)")
    public void pmdCli() throws Exception {
        pmdOp.includeLineNumber(false).execute();
    }

    private void pomRoot() throws FileUtilsErrorException {
        PomBuilder.generateInto(publishOperation().fromProject(this).info(), dependencies(),
                new File(workDirectory, "pom.xml"));
    }

    private void renderWithXunitViewer() throws Exception {
        var xunitViewer = new File("/usr/bin/xunit-viewer");
        if (xunitViewer.exists() && xunitViewer.canExecute()) {
            var reportsDir = "build/reports/tests/test/";

            Files.createDirectories(Path.of(reportsDir));

            new ExecOperation()
                    .fromProject(this)
                    .command(xunitViewer.getPath(), "-r", TEST_RESULTS_DIR, "-o", reportsDir + "index.html")
                    .execute();
        }
    }

    @Override
    public void test() throws Exception {
        var op = testOperation().fromProject(this);
        op.testToolOptions().reportsDir(new File(TEST_RESULTS_DIR));

        Exception ex = null;
        try {
            op.execute();
        } catch (Exception e) {
            ex = e;
        }

        renderWithXunitViewer();

        if (ex != null) {
            throw ex;
        }
    }

    @Override
    public void publish() throws Exception {
        super.publish();
        pomRoot();
    }

    @Override
    public void publishLocal() throws Exception {
        super.publishLocal();
        pomRoot();
    }
}
