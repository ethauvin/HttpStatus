<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>

<%
    request.setAttribute("jakarta.servlet.error.status_code", 588);
%>

Code: <hs:code /><br/>
Message: <hs:message default="Default message" /><br/>
Reason: <hs:reason default="Default reason" /><br/>
Cause: <hs:cause default="Default cause" /><br/>