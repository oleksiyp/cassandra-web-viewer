<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        isELIgnored="false" %>
<%@ page
        import="org.cassandra_viewer.util.Encoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>${hostPort} keyspaces</title></head>
<body>
<h2>Keyspaces</h2>

<c:forEach var="keyspace" items="${keyspaces}">
    <%
        String keyspaceEncoded = Encoder.encode((String) pageContext.getAttribute("keyspace"));
    %>
    <a href="<%= keyspaceEncoded %>/"><%= keyspaceEncoded %></a><br />
</c:forEach>

<%@ include file="controls.jsp"%>

</body>
</html>