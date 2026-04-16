<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>

<%
    request.setAttribute("jakarta.servlet.error.status_code", 500);
    request.setAttribute("jakarta.servlet.error.message", "Something went wrong");
    request.setAttribute("jakarta.servlet.error.exception", null);
%>

Code: <hs:code /><br/>
Message: <hs:message /><br/>
Reason: <hs:reason /><br/>
Cause: <hs:cause /><br/>