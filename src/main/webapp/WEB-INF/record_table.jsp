<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        isELIgnored="false" %>

<%@ page import="org.cassandra_viewer.data.CassandraBrowser" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.jstl.core.LoopTagStatus" %>
<%@ page import="org.cassandra_viewer.util.Encoder" %>
<%@ page import="org.cassandra_viewer.data.ValueTimestamp" %>
<%@ page import="org.cassandra_viewer.data.GeneralColumn" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Records table</title></head>
<body>
    <h2>Records table</h2>
    <table border='1' cellpadding='0' cellspacing='0'>
        <tr>
            <th>KEY</th>
            <c:forEach var="column" items="${columns}">
                <%
                    GeneralColumn genColumn = (GeneralColumn) pageContext.getAttribute("column");


                    if (genColumn.getSuperColumn() == null) {
                %>
                <th colspan="2"> <%= Encoder.encode(genColumn.getColumn()) %> </th>
                <% } else { %>
                <th> <%= Encoder.encode(genColumn.getSuperColumn()) %> </th>
                <th> <%= Encoder.encode(genColumn.getColumn()) %> </th>
                <% } %>
            </c:forEach>
        </tr>
        <c:forEach var="key" items="${keys}" varStatus="forStatus">
            <tr>
                <%
                    LoopTagStatus status = (LoopTagStatus) pageContext.getAttribute("forStatus");
                    int i = status.getIndex();
                    String key = (String) pageContext.getAttribute("key");
                    String encodedKey = Encoder.encode(key);
                %>
                <td><%= encodedKey %></td>
                <c:forEach var="column" items="${columns}">
                    <%

                        List<Map<GeneralColumn,ValueTimestamp>> records;
                        records = (List<Map<GeneralColumn, ValueTimestamp>>) request.getAttribute("records");

                        GeneralColumn column = (GeneralColumn) pageContext.getAttribute("column");

                        ValueTimestamp valueTs = records.get(i).get(column);
                        Object value = valueTs == null ? null : valueTs.getValue();
                        if (value == null) {
                            value = "";
                        } else
                        {
                            value = CassandraBrowser.stringify(value);
                            if (value.toString().trim().equals("")) {
                                value = "&nbsp;";
                            } else {
                                value = Encoder.encode((String) value);
                            }
                        }
                    %>
                    <td colspan="2"><%= value %></td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>

    <%@ include file="controls.jsp"%>

</body>
</html>