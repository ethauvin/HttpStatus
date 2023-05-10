/*
 * HttpStatusBuild.java
 *
 * Copyright 2023 sErik C. Thauvin (erik@thauvin.net)
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
import rife.bld.dependencies.Dependency;
import rife.bld.extension.PmdOperation;
import rife.bld.publish.PublishDeveloper;
import rife.bld.publish.PublishInfo;
import rife.bld.publish.PublishLicense;
import rife.bld.publish.PublishScm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.compile;
import static rife.bld.dependencies.Scope.test;
import static rife.bld.operations.JavadocOptions.DocLinkOption.NO_MISSING;

public class HttpStatusBuild extends Project {
    public HttpStatusBuild() {
        pkg = "net.thauvin.erik.httpstatus";
        name = "HttpStatus";
        version = version(1, 1, 0, "SNAPSHOT");

        var description = "HttpStatus JSP Tag Library";
        var url = "https://github.com/ethauvin/HttpStatus";

        mainClass = "net.thauvin.erik.httpstatus.Reasons";

        javaRelease = 17;

        downloadSources = true;
        repositories = List.of(MAVEN_LOCAL, MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);

        scope(compile)
                .include(dependency("jakarta.servlet", "jakarta.servlet-api", version(6, 0, 0)))
                .include(dependency("jakarta.servlet.jsp", "jakarta.servlet.jsp-api", version(3, 1, 1)))
                .include(dependency("jakarta.el", "jakarta.el-api", version(5, 0, 1)));
        scope(test)
                .include(dependency("org.assertj", "assertj-joda-time", version(2, 2, 0)))
                .include(dependency("org.junit.jupiter", "junit-jupiter", version(5, 9, 3)))
                .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1, 9, 3)));

        javadocOperation().javadocOptions()
                .docTitle(description + ' ' + version.toString())
                .docLint(NO_MISSING)
                .link("https://jakarta.ee/specifications/platform/9/apidocs/");

        publishOperation()
                .repository(version.isSnapshot() ? repository(SONATYPE_SNAPSHOTS_LEGACY.location())
                        .withCredentials(property("sonatype.user"), property("sonatype.password"))
                        : repository(SONATYPE_RELEASES.location())
                        .withCredentials(property("sonatype.user"), property("sonatype.password")))
                .repository(MAVEN_LOCAL)
                .info(new PublishInfo()
                        .groupId(pkg)
                        .artifactId(name.toLowerCase())
                        .name(name)
                        .description(description)
                        .url(url)
                        .developer(new PublishDeveloper().id("ethauvin").name("Erik C. Thauvin").email("erik@thauvin.net")
                                .url("https://erik.thauvin.net/"))
                        .license(new PublishLicense().name("The BSD 3-Clause License")
                                .url("http://opensource.org/licenses/BSD-3-Clause"))
                        .scm(new PublishScm().connection("scm:git:" + url + ".git")
                                .developerConnection("scm:git:git@github.com:ethauvin/" + name + ".git")
                                .url(url))
                        .signKey(property("sign.key"))
                        .signPassphrase(property("sign.passphrase")));
    }

    public static void main(String[] args) {
        new HttpStatusBuild().start(args);
    }

    @Override
    public void publish() throws Exception {
        super.publish();
        var pomPath = Path.of(MAVEN_LOCAL.getArtifactLocation(new Dependency(pkg, name.toLowerCase(), version)),
                version.toString(),
                name.toLowerCase() + '-' + version + ".pom");
        Files.copy(pomPath, Path.of(workDirectory.getAbsolutePath(), "pom.xml"), StandardCopyOption.REPLACE_EXISTING);
    }

    @BuildCommand(summary = "Runs PMD analysis")
    public void pmd() throws Exception {
        new PmdOperation()
                .fromProject(this)
                .failOnViolation(true)
                .ruleSets("config/pmd.xml")
                .execute();
    }
}