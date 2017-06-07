#  HttpStatus JSP Tag Library

[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause) [![release](http://github-release-version.herokuapp.com/github/ethauvin/httpstatus/release.svg?style=flat)](https://github.com/ethauvin/httpstatus/releases/latest) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.thauvin.erik.httpstatus/httpstatus/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.thauvin.erik.httpstatus/httpstatus) [ ![Download](https://api.bintray.com/packages/ethauvin/maven/HttpStatus/images/download.svg) ](https://bintray.com/ethauvin/maven/HttpStatus/_latestVersion)  
[![Dependency Status](https://www.versioneye.com/user/projects/56a6947a1b78fd002d00018a/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56a6947a1b78fd002d00018a) [![Build Status](https://travis-ci.org/ethauvin/HttpStatus.svg?branch=master)](https://travis-ci.org/ethauvin/HttpStatus) [![Build status](https://ci.appveyor.com/api/projects/status/w5j4kul3w2rkigxb?svg=true)](https://ci.appveyor.com/project/ethauvin/httpstatus) [![CircleCI](https://circleci.com/gh/ethauvin/HttpStatus/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/HttpStatus/tree/master)


A simple [JSP](http://www.oracle.com/technetwork/java/javaee/jsp/index.html) Tag Library to display the [code](#hscode), [reason](#hsreason) and/or [cause](#hscode) for [HTTP status codes](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html) in JSP error pages.

----

For example:

```jsp
<%@ page isErrorPage="true" %>
<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>
<html><head>
<title><hs:code/> <hs:reason default="Server Error"/></title>
</head>
<h1><hs:reason default="Server Error"/></h1>
Cause: <pre><hs:cause default="Unable to complete your request."/></pre>
...
```

or

```jsp
<%@ page isErrorPage="true" import="net.thauvin.erik.httpstatus.Reasons" %>
<%= Reasons.getReasonPhrase(pageContext.getErrorData().getStatusCode()) %>
```

would display on a [501 status code](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.2):

    Not Implemented

----

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

## hs:reason

The `<hs:reason/>` tag displays the reason for a HTTP status code, if any. Optional attributes are:

Attribute   | Description
----------- | -------------------------------------------------------------------------------------------
`code`      | The HTTP status error code. If not specified the current status code is used.
`default`   | The fallback value to output, if no reason is available.
`escapeXml` | Converts &lt;, &gt;, &amp;, ', " to their corresponding [entity codes](http://dev.w3.org/html5/html-author/charref). Value is `true` by default.

The reasons are defined in a [ResourceBundle](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) properties as follows:

Status Code | Reason
----------- | -------------------------------
`100`       | Continue
`101`       | Switching Protocols
`102`       | Processing
`200`       | OK
`201`       | Created
`202`       | Accepted
`203`       | Non-Authoritative Information
`204`       | No Content
`205`       | Reset Content
`206`       | Partial Content
`207`       | Multi-Status
`208`       | Already Reported
`226`       | IM Used
`300`       | Multiple Choices
`301`       | Moved Permanently
`302`       | Moved Temporarily
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
`413`       | Request Entity Too Large
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
`431`       | Request Header Fields Too Large
`440`       | Login Timeout
`444`       | No Response
`449`       | Retry With
`450`       | Blocked by Windows Parental Controls
`451`       | Unavailable For Legal Reasons
`494`       | Request Header Too Large
`495`       | Cert Error
`496`       | No Cert
`497`       | HTTP to HTTPS
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
`522`       | Origin Connection Time-out
`598`       | Network Read Timeout Error
`599`       | Network Connect Timeout Error

----

## Usage with [Gradle](https://gradle.org/) or [Maven](http://maven.apache.org/)
Include the following in your `build.gradle` file:

```gradle
dependencies {
	compile 'net.thauvin.erik.httpstatus:httpstatus:1.0.3'
}
```

or as a Maven artifact:

```xml
<dependency>
    <groupId>net.thauvin.erik.httpstatus</groupId>
    <artifactId>httpstatus</artifactId>
    <version>1.0.3</version>
</dependency>
```
