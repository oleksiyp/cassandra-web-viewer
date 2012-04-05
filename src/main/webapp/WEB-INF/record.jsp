<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.cassandra_viewer.util.Encoder" %>
<%@ page import="org.cassandra_viewer.ValueTimestamp" %>
<%@ page import="org.cassandra_viewer.GeneralColumn" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.cassandra_viewer.CassandraBrowser" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<%
    String encodedKey = org.cassandra_viewer.util.Encoder.encode((String) request.getAttribute("key"));
%>

<head><title>Record <%=encodedKey%></title></head>
<body>
    <h2>Record</h2>
    <h3><%=encodedKey%></h3>

    <table border="1" cellpadding="0" cellspacing="0">
        <tr><th colspan="2">COLUMN</th><th>VALUE</th><th>TIMESTAMP</th></tr>
        <c:forEach var="column" items="${columns}">
            <tr>
                <%
                    Map<GeneralColumn, ValueTimestamp> record;
                    record = (Map<GeneralColumn, ValueTimestamp>) request.getAttribute("record");
                    GeneralColumn column = (GeneralColumn) pageContext.getAttribute("column");
                    ValueTimestamp valueTs = record.get(column);

                    Object value = valueTs == null ? null : valueTs.getValue();
                    if (value == null) {
                        value = "";
                    } else
                    {
                        value = CassandraBrowser.stringify(value);
                        if (value.toString().trim().equals("")) {
                            value = "&nbsp;";
                        } else {
                            value = org.cassandra_viewer.util.Encoder.encode((String) value);
                        }
                    }
                    if (column.getSuperColumn() == null) {
                %>
                <td colspan="2"><%=column.getColumn()%></td>
                <% } else { %>
                <td><%=column.getColumn()%></td>
                <td><%=column.getSuperColumn()%></td>
                <% } %> <td><%=value%></td>
                <td><%=valueTs.getTimestamp()%></td>
            </tr>
        </c:forEach>
    </table>

    <%@ include file="controls.jsp"%>

</body>
</html>