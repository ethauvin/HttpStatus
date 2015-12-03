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
<%= Reasons.getReasonPhrase(501) %>
```

would display on a [501 status code](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.2):

    Not Implemented

----

The **<hs:reason>** tag attributes are:

Attribute    | Description                                                                   | Required
------------ | ----------------------------------------------------------------------------- | --------
`statusCode` | The HTTP status error code. If not specified the current status code is used. | No
`default`    | The fallback value to output.                                                 | No