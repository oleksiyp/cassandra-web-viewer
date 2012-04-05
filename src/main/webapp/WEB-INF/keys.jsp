<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        isELIgnored="false" %>

<%@ page import="org.cassandra_viewer.CassandraBrowser" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.jstl.core.LoopTagStatus" %>
<%@ page import="org.cassandra_viewer.util.Encoder" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Keys from ${fromKey} to ${toKey}</title></head>
<body>
    <h2>Keys</h2>
    <c:if test="${!noKeys}">
        <h3>${fromKey} <span style="font-size: 70%">to</span> ${toKey}</h3>
    </c:if>
    <c:if test="${noKeys}">
        <span style="font-size: 250%; font-weight: bold;">EMPTY</span>
    </c:if>
    <c:forEach var="key" items="${keys}" varStatus="forStatus">
        <tr>
            <%
                String key = (String) pageContext.getAttribute("key");
                String encodedKey = Encoder.encode(key);
            %>
            <a href="<%= encodedKey %>/"><%= encodedKey %></a>
        </tr>
    </c:forEach>

    <%@ include file="controls.jsp"%>

</body>
</html>