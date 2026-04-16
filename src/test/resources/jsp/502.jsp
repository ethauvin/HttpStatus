<%@ taglib prefix="hs" uri="http://erik.thauvin.net/taglibs/httpstatus" %>

<%
    request.setAttribute("jakarta.servlet.error.status_code", 502);
%>

Code: <hs:code /><br/>
Message: <hs:message /><br/>
Reason: <hs:reason /><br/>
Cause: <hs:cause /><br/>