<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        import="org.cassandra_viewer.util.Encoder"
        isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>${keyspace} column families</title></head>
<body>
<h2>Column families</h2>

<c:forEach var="family" items="${families}">
    <%
        String familyEncoded = Encoder.encode((String) pageContext.getAttribute("family"));
    %>
    <a href="<%= familyEncoded %>/"><%= familyEncoded %></a><br />
</c:forEach>
<%@ include file="controls.jsp"%>

</body>
</html>