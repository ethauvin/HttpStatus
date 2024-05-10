/*
 * HttpStatusBuild.java
 *
 * Copyright 2015-2023 Erik C. Thauvin (erik@thauvin.net)
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
import rife.bld.extension.JacocoReportOperation;
import rife.bld.extension.PmdOperation;
import rife.bld.publish.*;
import rife.tools.exceptions.FileUtilsErrorException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.Attributes;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.compile;
import static rife.bld.dependencies.Scope.test;
import static rife.bld.operations.JavadocOptions.DocLinkOption.NO_MISSING;

public class HttpStatusBuild extends Project {
    final PmdOperation pmdOp = new PmdOperation()
            .fromProject(this)
            .failOnViolation(true)
            .ruleSets("config/pmd.xml");

    public HttpStatusBuild() {
        pkg = "net.thauvin.erik.httpstatus";
        name = "HttpStatus";
        version = version(1, 1, 1, "SNAPSHOT");

        var description = "Tag library to display the code, reason, cause and/or message for HTTP status codes in JSP error pages";
        var url = "https://github.com/ethauvin/HttpStatus";

        mainClass = "net.thauvin.erik.httpstatus.Reasons";

        javaRelease = 17;

        downloadSources = true;
        autoDownloadPurge = true;
        repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);

        scope(compile)
                .include(dependency("jakarta.servlet", "jakarta.servlet-api", version(6, 0, 0)))
                .include(dependency("jakarta.servlet.jsp", "jakarta.servlet.jsp-api", version(3, 1, 1)))
                .include(dependency("jakarta.el", "jakarta.el-api", version(6, 0, 0)));
        scope(test)
                .include(dependency("org.assertj", "assertj-core", version(3, 25, 3)))
                .include(dependency("org.junit.jupiter", "junit-jupiter", version(5, 10, 2)))
                .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1, 10, 2)));

        jarOperation().manifestAttribute(Attributes.Name.MAIN_CLASS, pkg + '.' + "Reasons");

        javadocOperation().javadocOptions()
                .docTitle(description + ' ' + version.toString())
                .docLint(NO_MISSING)
                .link("https://jakarta.ee/specifications/platform/9/apidocs/");

        publishOperation()
                .repository(version.isSnapshot() ? repository(SONATYPE_SNAPSHOTS_LEGACY.location())
                        .withCredentials(property("sonatype.user"), property("sonatype.password"))
                        : repository(SONATYPE_RELEASES_LEGACY.location())
                        .withCredentials(property("sonatype.user"), property("sonatype.password")))
                .info(new PublishInfo()
                        .groupId(pkg)
                        .artifactId(name.toLowerCase())
                        .name(name)
                        .version(version)
                        .description(description)
                        .url(url)
                        .developer(
                                new PublishDeveloper()
                                        .id("ethauvin")
                                        .name("Erik C. Thauvin")
                                        .email("erik@thauvin.net")
                                        .url("https://erik.thauvin.net/")
                        )
                        .license(
                                new PublishLicense()
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
    public void jacoco() throws IOException {
        new JacocoReportOperation()
                .fromProject(this)
                .execute();
    }

    @BuildCommand(summary = "Runs PMD analysis")
    public void pmd() {
        pmdOp.execute();
    }

    @BuildCommand(value = "pmd-cli", summary = "Runs PMD analysis (CLI)")
    public void pmdCli() {
        pmdOp.includeLineNumber(false).execute();
    }

    @Override
    public void publish() throws Exception {
        super.publish();
        rootPom();
    }

    @Override
    public void publishLocal() throws Exception {
        super.publishLocal();
        rootPom();
    }

    private void rootPom() throws FileUtilsErrorException {
        PomBuilder.generateInto(publishOperation().info(), dependencies(),
                Path.of(workDirectory.getPath(), "pom.xml").toFile());
    }
}
