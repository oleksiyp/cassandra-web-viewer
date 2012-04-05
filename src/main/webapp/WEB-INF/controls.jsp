<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        isELIgnored="false"
        %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.cassandra_viewer.util.Encoder" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.cassandra_viewer.util.HttpParamsHelper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!
%>

<%
    Map params = new TreeMap();
    HttpParamsHelper.suckInParam(params, request, "start");
    HttpParamsHelper.suckInParam(params, request, "end");
    HttpParamsHelper.suckInParam(params, request, "count");
    HttpParamsHelper.suckInParam(params, request, "showInTable");

%>
<hr />

<c:if test="${pageType eq 'TABLE'}"> <a href="?<%= HttpParamsHelper.toParamsStr(HttpParamsHelper.change(params, "showInTable", "false")) %>">Show list</a> <br /> </c:if>
<c:if test="${pageType eq 'KEYS'}"> <a href="?<%= HttpParamsHelper.toParamsStr(HttpParamsHelper.change(params, "showInTable", "true")) %>">Show table</a> <br /> </c:if>
<a href="..">Back</a> <br />
<c:if test="${pageType eq 'KEYS' || pageType eq 'TABLE'}">
    <%
        String fromKey = (String) request.getAttribute("fromKey");
        String toKey = (String) request.getAttribute("toKey");
        Map formParams = HttpParamsHelper.change(HttpParamsHelper.change(params, "toKey", toKey), "fromKey", fromKey);
    %>
    <form action="${pageContext.request.contextPath}/navigate">
        <input type="submit" name="next" value="Next page" ${hasNext ? "" : "disabled='disabled'"}" />
        <input type="submit" name="previous" value="Previous" ${hasPrevious ? "" : "disabled='disabled'"}" />
        <input type="text" name="jumpPos" size="2" />
        <input type="submit" name="jump" value="Jump" />
        <input type="text" name="limitCount" size="2" value="100" />
        <input type="submit" name="limit" value="Limit count" />
        <%= HttpParamsHelper.getHiddensStr(formParams) %>
    </form>
    <br />
</c:if>

