#  HttpStatus JSP Tag Library

[![Release](https://img.shields.io/github/release/ethauvin/httpstatus.svg)](https://github.com/ethauvin/httpstatus/releases/latest) [![Maven Central](https://img.shields.io/maven-central/v/net.thauvin.erik.httpstatus/httpstatus.svg?label=maven%20central)](https://search.maven.org/search?q=g:%22net.thauvin.erik.httpstatus%22%20AND%20a:%22httpstatus%22)   
[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause) [![Known Vulnerabilities](https://snyk.io/test/github/ethauvin/httpstatus/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/ethauvin/httpstatus?targetFile=build.gradle) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ethauvin_HttpStatus&metric=alert_status)](https://sonarcloud.io/dashboard?id=ethauvin_HttpStatus)  
[![GitHub CI](https://github.com/ethauvin/httpstatus/actions/workflows/gradle.yml/badge.svg)](https://github.com/ethauvin/httpstatus/actions/workflows/gradle.yml) [![Build status](https://ci.appveyor.com/api/projects/status/w5j4kul3w2rkigxb?svg=true)](https://ci.appveyor.com/project/ethauvin/httpstatus) [![CircleCI](https://circleci.com/gh/ethauvin/HttpStatus/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/HttpStatus/tree/master)


A simple [JSP](http://www.oracle.com/technetwork/java/javaee/jsp/index.html) Tag Library to display the [code](#hscode), [reason](#hsreason), [cause](#hscode) and/or [message](#hsmessage) for [HTTP status codes](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html) in JSP error pages.

For example:

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

would display on a [501 status code](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.2):

    Not Implemented

## Usage with [Gradle](https://gradle.org/) or [Maven](http://maven.apache.org/)
Include the following in your `build.gradle` file:

```gradle
dependencies {
    implementation 'net.thauvin.erik.httpstatus:httpstatus:1.0.6'
}
```

or as a Maven artifact:

```xml
<dependency>
    <groupId>net.thauvin.erik.httpstatus</groupId>
    <artifactId>httpstatus</artifactId>
    <version>1.0.6</version>
</dependency>
```

## hs:cause

The `<hs:cause/>` tag displays the cause of current HTTP status code, if any. A shorthand for:

```jsp
<%= pageContext.getErrorData().getThrowable().getCause().getLocalizedMessage() %>
```

Optional attributes are:

Attribute   | Description
----------- | -------------------------------------------------------------------------------------------
`default`   | The fallback value to output, if no cause is available.
`escapeXml` | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.

## hs:code
The `<hs:code/>` tag displays the current HTTP status code, if any. A shorthand for:

```jsp
<%= pageContext.getErrorData().getStatusCode() %>
```

## hs:message

The `<hs:message/>` tag displays the current error message, if any. A shorthand for:

```jsp
<%= request.getAttribute("javax.servlet.error.message") %>
```

Optional attributes are:

Attribute   | Description
----------- | -------------------------------------------------------------------------------------------
`default`   | The fallback value to output, if no error message is available.
`escapeXml` | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.

## hs:reason

The `<hs:reason/>` tag displays the reason for a HTTP status code, if any. Optional attributes are:

Attribute   | Description
----------- | -------------------------------------------------------------------------------------------
`code`      | The HTTP status error code. If not specified the current status code is used.
`default`   | The fallback value to output, if no reason is available.
`escapeXml` | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.

## StatusCode Bean

The `StatusCode` bean can be used to check the class of the status code error. For example, using the JSTL:

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

// ---

StatusCode statusCode = new StatusCode((Integer) request.getAttribute("javax.servlet.error.status_code"));
if (statusCode.isError()) {
    if (statusCode.isServerError()) {
        String reason = statusCode.getReason();
    } else {
        // ...
    }
}
```

The `StatusCode` bean methods are:

Method            | Description
----------------- | --------------------------------------------------------------------
`getReason`       | Returns the reason for the status code (eg: `Internal Server Error`)
`isClientError`   | Checks if the status code is a client error.
`isError`         | Checks if the status code is a server or client error.
`isInfo`          | Checks if the status code is informational.
`isRedirect`      | Checks if the status code is a redirect.
`isServerError`   | Checks if the status code is a server error.
`isSuccess`       | Checks if the status code is a success. (`OK`)
`isValid`         | Checks if the status code is valid.

## Reasons

The reasons are defined in a [ResourceBundle](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) properties as follows:

Status Code | Reason
----------- | -----------------------------------
`100`       | Continue
`101`       | Switching Protocols
`102`       | Processing
`103`       | Early Hints
`200`       | OK
`201`       | Created
`202`       | Accepted
`203`       | Non-Authoritative Information
`204`       | No Content
`205`       | Reset Content
`206`       | Partial Content
`207`       | Multi-Status
`208`       | Already Reported
`218`       | This is fine
`226`       | IM Used
`300`       | Multiple Choices
`301`       | Moved Permanently
`302`       | Found/Moved Temporarily
`303`       | See Other
`304`       | Not Modified
`305`       | Use Proxy
`306`       | Switch Proxy
`307`       | Temporary Redirect
`308`       | Permanent Redirect
`400`       | Bad Request
`401`       | Unauthorized
`402`       | Payment Required
`403`       | Forbidden
`404`       | Not Found
`405`       | Method Not Allowed
`406`       | Not Acceptable
`407`       | Proxy Authentication Required
`408`       | Request Timeout
`409`       | Conflict
`410`       | Gone
`411`       | Length Required
`412`       | Precondition Failed
`413`       | Request Entity/Payload Too Large
`414`       | Request-URI Too Long
`415`       | Unsupported Media Type
`416`       | Requested Range Not Satisfiable
`417`       | Expectation Failed
`418`       | I'm A Teapot
`419`       | Insufficient Space on Resource
`420`       | Method Failure
`421`       | Misdirected Request
`422`       | Unprocessable Entity
`423`       | Locked
`424`       | Failed Dependency
`426`       | Upgrade Required
`428`       | Precondition Required
`429`       | Too Many Requests
`430`       | Request Header Fields Too Large
`431`       | Request Header Fields Too Large
`440`       | Login Timeout
`444`       | No Response
`449`       | Retry With
`450`       | Blocked by Windows Parental Controls
`451`       | Unavailable For Legal Reasons
`460`       | Client Closed Connection Before Load Balancer Idle Timeout
`463`       | X-Forwarded-For Header with More than 30 IP Addresses
`494`       | Request Header Too Large
`495`       | SSL Certificate Error
`496`       | No SSL Certificate
`497`       | HTTP Request Sent to HTTPS Port
`498`       | Token Expired/Invalid
`499`       | Client Closed Request
`500`       | Internal Server Error
`501`       | Not Implemented
`502`       | Bad Gateway
`503`       | Service Unavailable
`504`       | Gateway Timeout
`505`       | HTTP Version Not Supported
`506`       | Variant Also Negotiates
`507`       | Insufficient Storage
`508`       | Loop Detected
`509`       | Bandwidth Limit Exceeded
`510`       | Not Extended
`511`       | Network Authentication Required
`520`       | Unknown Error
`521`       | Web Server Is Down
`522`       | Origin Connection Time-out
`523`       | Origin Is Unreachable
`524`       | A Timeout Occurred
`525`       | SSL Handshake Failed
`526`       | Invalid SSL Certificate
`527`       | Railgun Error
`529`       | Site is overloaded
`530`       | Site is frozen
`598`       | Network Read Timeout Error
`599`       | Network Connect Timeout Error

## Command Line Usage
You can query the reason phrase for status codes as follows:

```sh
$ java -jar httpstatus-1.0.6.jar 404 500
404: Not Found
500: Internal Server Error
```

If no status code is specified, all will be printed:

```sh
$ java -jar httpstatus-1.0.6.jar
100: Continue
101: Switching Protocols
102: Processing
103: Early Hints
200: OK
201: Created
202: Accepted
203: Non-Authoritative Information
204: No Content
205: Reset Content
206: Partial Content
207: Multi-Status
208: Already Reported
226: IM Used
...
```
