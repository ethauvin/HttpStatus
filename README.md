#  HttpStatus JSP Tag Library

A simple JSP Tag Library to display the reason of [HTTP status codes](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html) in JSP error pages.

----

For example:

```jsp
<%@ page isErrorPage="true" %>
<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>
<html><head>
<title>${pageContext.errorData.statusCode} <hs:reason default="Server Error"/></title>
</head>
<h1><hs:reason default="Server Error"/></h1>
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

The **<hs:reason>** tag attributes are:

Attribute    | Description                                                                   | Required
------------ | ----------------------------------------------------------------------------- | --------
`statusCode` | The HTTP status error code. If not specified the current status code is used. | No
`default`    | The fallback value to output.                                                 | No

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