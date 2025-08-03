# HTTP Status Code & JSP Tag Library

[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause)
[![Java](https://img.shields.io/badge/java-17%2B-blue)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![bld](https://img.shields.io/badge/2.3.0-FA9052?label=bld&labelColor=2392FF)](https://rife2.com/bld)
[![Release](https://img.shields.io/github/release/ethauvin/httpstatus.svg)](https://github.com/ethauvin/httpstatus/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/net.thauvin.erik.httpstatus/httpstatus.svg?color=blue)](https://central.sonatype.com/artifact/net.thauvin.erik.httpstatus/httpstatus)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fnet%2Fthauvin%2Ferik%2Fhttpstatus%2Fhttpstatus%2Fmaven-metadata.xml&label=snapshot)](https://github.com/ethauvin/HttpStatus/packages/2260867/versions)

[![Known Vulnerabilities](https://snyk.io/test/github/ethauvin/httpstatus/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/ethauvin/httpstatus?targetFile=pom.xml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ethauvin_HttpStatus&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ethauvin_HttpStatus)
[![GitHub CI](https://github.com/ethauvin/httpstatus/actions/workflows/bld.yml/badge.svg)](https://github.com/ethauvin/httpstatus/actions/workflows/bld.yml)
[![CircleCI](https://circleci.com/gh/ethauvin/HttpStatus/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/HttpStatus/tree/master)

A simple library to search for and display information about [HTTP status codes](https://datatracker.ietf.org/doc/html/rfc9110#name-status-codes).

## Table of Contents

- [Examples](#examples)
  - [Java](#java)
  - [JSP](#jsp)
- [Usage](#usage)
  - [bld](#bld)
  - [Gradle](#gradle)
  - [Maven](#maven)
- [JSP Tags](#jsp-tags)
  - [hs:cause](#hscause)
  - [hs:code](#hscode)
  - [hs:message](#hsmessage)
  - [hs:reason](#hsreason)
- [StatusCode](#statuscode)
  - [Status Code Class](#status-code-class)
- [Reasons](#reasons)
- [Command Line Usage](#command-line-usage)
- [Contributing](#contributing)

## Examples (TL;DR)

### Java

```java
Reasons.getReasonPhrase(404); // Not Found
Reasons.getReasonPhrase(666, "Unknown Reason"); // Unknown Reason

var reasons = Reasons.getReasonClass(StatusCodeClass.CLIENT_ERROR); // 4xx
reasons.forEach((code, reason) -> System.out.println(code + ": " + reason));

StatusCode.isServerError(500) // true
StatusCode.isError(301) // false
```

### [JSP](http://www.oracle.com/technetwork/java/javaee/jsp/index.html)

```jsp
<%@ page isErrorPage="true" %>
<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>
<html><head>
<title><hs:code/> <hs:reason default="Server Error"/></title>
</head>
<h1><hs:reason default="Server Error"/></h1>
Cause: <pre><hs:cause default="Unable to complete your request."/></pre>
Message: <pre><hs:message default="A server error has occured."/></pre>
...
```

or

```jsp
<%@ page isErrorPage="true" import="net.thauvin.erik.httpstatus.Reasons" %>
<%= Reasons.getReasonPhrase(pageContext.getErrorData().getStatusCode()) %>
```

would display on a [501 status code](https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.2):

```console
Not Implemented
```

## Usage

### [bld](https://rife2.com/bld)

Include the following in your `bld` build file:

```java
repositories = List.of(MAVEN_CENTRAL, CENTRAL_SNAPSHOTS);

scope(compile).include(
        dependency("net.thauvin.erik.httpstatus","httpstatus", version(2, 0, 0, "SNAPSHOT"))
);
```

### [Gradle](https://gradle.org/)

Include the following in your `build.gradle` file:

```gradle
repositories {
    maven {
        name = 'Central Portal Snapshots'
        url = 'https://central.sonatype.com/repository/maven-snapshots/'
    }
    mavenCentral()
}

dependencies {
    implementation 'net.thauvin.erik.httpstatus:httpstatus:2.0.0-SNAPSHOT'
}
```

### Maven

Instructions for using with Maven, Ivy, etc. can be found on [Maven Central](https://central.sonatype.com/artifact/net.thauvin.erik.httpstatus/httpstatus).

## JSP Tags

### [hs:cause](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/taglibs/CauseTag.html)

The `<hs:cause/>` tag displays the cause of current HTTP status code, if any. A shorthand for:

```jsp
<%= pageContext.getErrorData().getThrowable().getCause().getLocalizedMessage() %>
```

Optional attributes are:

| Attribute    | Description                                                                                                                                       |
|:-------------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| `default`    | The fallback value to output, if no cause is                                                                                                      |
| `escapeXml`  | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.  |

### [hs:code](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/taglibs/CodeTag.html)

The `<hs:code/>` tag displays the current HTTP status code, if any. A shorthand for:

```jsp
<%= pageContext.getErrorData().getStatusCode() %>
```

### [hs:message](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/taglibs/MessageTag.html)

The `<hs:message/>` tag displays the current error message, if any. A shorthand for:

```jsp
<%= request.getAttribute("javax.servlet.error.message") %>
```

Optional attributes are:

| Attribute   | Description                                                                                                                                       |
|:------------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| `default`   | The fallback value to output, if no error message is available.                                                                                   |
| `escapeXml` | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.  |

### [hs:reason](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/taglibs/ReasonTag.html)

The `<hs:reason/>` tag displays the reason for an HTTP status code, if any. Optional attributes are:

| Attribute    | Description                                                                                                                                       |
|:-------------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| `default`    | The fallback value to output, if no reason is available.                                                                                          |
| `code`       | The HTTP status error code. If not specified the current status code is used.                                                                     |
| `escapeXml`  | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.  |

## StatusCode

The [StatusCode](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html) bean can be used to check the class of the status code error.

For example, in Java:

```java
var statusCode = new StatusCode(500);
if (statusCode.isError()) {
    System.out.println(statusCode.getReason()); // Internal Server Error
}
```

Or using the JSTL:

```jsp
<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="statusCode" class="net.thauvin.erik.httpstatus.StatusCode"/>
<c:set target="${statusCode}" property="code"><hs:code/></c:set>
<c:choose>
    <c:when test="${statusCode.isClientError()}">
        An error occurred on your side. (<hs:reason/>)
    </c:when>
    <c:otherwise>
        An error occurred on our side. (<hs:message/>)
    </c:otherwise>
</c:choose>
```

or in a Servlet:

```java
import net.thauvin.erik.httpstatus.StatusCode;

public class ExampleServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        var statusCode = new StatusCode(
                (Integer) request.getAttribute("javax.servlet.error.status_code"));
        if (statusCode.isError()) {
            if (statusCode.isServerError()) {
                var reason = statusCode.getReason();
            } else {
                // ...
            }
        }
    }
}
```

The [StatusCode](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html) bean methods are:

| Method                                                                                                             | Description                                                           |
|:-------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------|
| [getReason](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#getReason())         | Returns the reason for the status code (e.g. `Internal Server Error`) |
| [isClientError](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isClientError()) | Checks if the status code is a client error.                          |
| [isError](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isError())             | Checks if the status code is a server or client error.                |
| [isInfo](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isInfo())               | Checks if the status code is informational.                           |
| [isRedirect](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isRedirect())       | Checks if the status code is a redirect.                              |
| [isServerError](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isServerError()) | Checks if the status code is a server error.                          |
| [isSuccess](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isSuccess())         | Checks if the status code is a success. (`OK`)                        |
| [isValid](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#isValid())             | Checks if the status code is valid.                                   |

These methods are also available as [static methods](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCode.html#method-summary).

### Status Code Class

The [StatusCodeClass](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html) can be used to retrieve all status codes in a specific [class](https://datatracker.ietf.org/doc/html/rfc7231#section-6):

```java
var reasons = Reasons.getReasonClass(StatusCodeClass.SERVER_ERROR); // 5xx
reasons.forEach((code, reason) -> System.out.println(code + ": " + reason)); // 500: Internal Server Error
```

The defined [standard classes](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html#enum-constant-summary) are:

| StatusCodeClass                                                                                                       | Values | Description                                                     |
|:----------------------------------------------------------------------------------------------------------------------|:------:|:----------------------------------------------------------------|
| [INFORMATIONAL](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html#INFORMATIONAL) |  1xx   | The request was received, continuing process                    |
| [SUCCESSFUL](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html#SUCCESSFUL)       |  2xx   | The request was successfully received, understood, and accepted |
| [REDIRECTION](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html#REDIRECTION)     |  3xx   | Further action needs to be taken to complete the request        |
| [CLIENT_ERROR](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html#CLIENT_ERROR)   |  4xx   | The request contains bad syntax or cannot be fulfilled          |
| [SERVER_ERROR](https://ethauvin.github.io/HttpStatus/net/thauvin/erik/httpstatus/StatusCodeClass.html#SERVER_ERROR)   |  5xx   | The server failed to fulfil an apparently valid request         |

## Reasons

The reasons are defined in a [ResourceBundle](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ResourceBundle.html) properties as follows:

| Status Code  | Reason                                                      |
|:-------------|:------------------------------------------------------------|
| `100`        | Continue                                                    |
| `101`        | Switching Protocols                                         |
| `102`        | Processing                                                  |
| `103`        | Early Hints                                                 |
| `110`        | Response is Stale                                           |
| `111`        | Revalidation Failed                                         |
| `112`        | Disconnected Operation                                      |
| `113`        | Heuristic Expiration                                        |
| `199`        | Miscellaneous Warning                                       |
| `200`        | OK                                                          |
| `201`        | Created                                                     |
| `202`        | Accepted                                                    |
| `203`        | Non-Authoritative Information                               |
| `204`        | No Content                                                  |
| `205`        | Reset Content                                               |
| `206`        | Partial Content                                             |
| `207`        | Multi-Status                                                |
| `208`        | Already Reported                                            |
| `214`        | Transformation Applied                                      |
| `218`        | This is fine                                                |
| `226`        | IM Used                                                     |
| `299`        | Miscellaneous Persistent Warning                            |
| `300`        | Multiple Choices                                            |
| `301`        | Moved Permanently                                           |
| `302`        | Found/Moved Temporarily                                     |
| `303`        | See Other                                                   |
| `304`        | Not Modified                                                |
| `305`        | Use Proxy                                                   |
| `306`        | Unused                                                      |
| `307`        | Temporary Redirect                                          |
| `308`        | Permanent Redirect                                          |
| `400`        | Bad Request                                                 |
| `401`        | Unauthorized                                                |
| `402`        | Payment Required                                            |
| `403`        | Forbidden                                                   |
| `404`        | Not Found                                                   |
| `405`        | Method Not Allowed                                          |
| `406`        | Not Acceptable                                              |
| `407`        | Proxy Authentication Required                               |
| `408`        | Request Timeout                                             |
| `409`        | Conflict                                                    |
| `410`        | Gone                                                        |
| `411`        | Length Required                                             |
| `412`        | Precondition Failed                                         |
| `413`        | Payload Too Large                                           |
| `414`        | URI Too Long                                                |
| `415`        | Unsupported Media Type                                      |
| `416`        | Range Not Satisfiable                                       |
| `417`        | Expectation Failed                                          |
| `418`        | I'm A Teapot                                                |
| `419`        | Insufficient Space on Resource                              |
| `420`        | Method Failure                                              |
| `421`        | Misdirected Request                                         |
| `422`        | Unprocessable Content                                       |
| `423`        | Locked                                                      |
| `424`        | Failed Dependency                                           |
| `425`        | Too Early                                                   |
| `426`        | Upgrade Required                                            |
| `428`        | Precondition Required                                       |
| `429`        | Too Many Requests                                           |
| `430`        | Request Header Fields Too Large                             |
| `431`        | Request Header Fields Too Large                             |
| `440`        | Login Timeout                                               |
| `444`        | No Response                                                 |
| `449`        | Retry With                                                  |
| `450`        | Blocked by Windows Parental Controls                        |
| `451`        | Unavailable For Legal Reasons                               |
| `460`        | Client Closed Connection Before Load Balancer Idle Timeout  |
| `463`        | X-Forwarded-For Header with More than 30 IP Addresses       |
| `494`        | Request Header Too Large                                    |
| `495`        | SSL Certificate Error                                       |
| `496`        | SSL Certificate Required                                    |
| `497`        | HTTP Request Sent to HTTPS Port                             |
| `498`        | Token Expired/Invalid                                       |
| `499`        | Client Closed Request                                       |
| `500`        | Internal Server Error                                       |
| `501`        | Not Implemented                                             |
| `502`        | Bad Gateway                                                 |
| `503`        | Service Unavailable                                         |
| `504`        | Gateway Timeout                                             |
| `505`        | HTTP Version Not Supported                                  |
| `506`        | Variant Also Negotiates                                     |
| `507`        | Insufficient Storage                                        |
| `508`        | Loop Detected                                               |
| `509`        | Bandwidth Limit Exceeded                                    |
| `510`        | Not Extended                                                |
| `511`        | Network Authentication Required                             |
| `520`        | Unknown Error                                               |
| `521`        | Web Server Is Down                                          |
| `522`        | Connection Timed Out                                        |
| `523`        | Origin Is Unreachable                                       |
| `524`        | A Timeout Occurred                                          |
| `525`        | SSL Handshake Failed                                        |
| `526`        | Invalid SSL Certificate                                     |
| `527`        | Railgun Error                                               |
| `529`        | Site is overloaded                                          |
| `530`        | Site is frozen                                              |
| `540`        | Temporarily Disabled                                        |
| `561`        | Unauthorized                                                |
| `598`        | Network Read Timeout Error                                  |
| `599`        | Network Connect Timeout Error                               |
| `783`        | Unexpected Token                                            |

## Command Line Usage

You can query the reason phrase for status codes as follows:

```console
> java -jar httpstatus-2.0.0.jar 404 500
404: Not Found
500: Internal Server Error
```

If no status code is specified, all will be printed:

```console
> java -jar httpstatus-2.0.0.jar
100: Continue
101: Switching Protocols
102: Processing
103: Early Hints
110: Response is Stale
111: Revalidation Failed
112: Disconnected Operation
113: Heuristic Expiration
199: Miscellaneous Warning
200: OK
201: Created
202: Accepted
203: Non-Authoritative Information
...
```

You can also print status codes by [classes](https://datatracker.ietf.org/doc/html/rfc7231#section-6):

```console
> java -jar httpstatus-2.0.0.jar 2xx
200: OK
201: Created
202: Accepted
203: Non-Authoritative Information
...
```

## Contributing

If you want to contribute to this project, all you have to do is clone the GitHub
repository:

```console
git clone git@github.com:ethauvin/HttpStatus.git
```

Then use [bld](https://rife2.com/bld) to build:

```console
cd HttpStatus
./bld compile
```

The project has an [IntelliJ IDEA](https://www.jetbrains.com/idea/) project structure. You can just open it after all
the dependencies were downloaded and peruse the code.
