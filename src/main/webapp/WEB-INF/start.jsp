<%@page isELIgnored="false" %>

<html>
<head><title>Cassandra web viewer</title></head>
<body onload="document.getElementById('hostname').focus()">
 <form method="GET" action="${pageContext.request.contextPath}/go">
     <b>Cassandra credentials: </b>
     <label for="username" width="200px">username</label>
     <input type="text" name="username" size="6" id="username" />
     <label for="hostname" width="200px">hostname</label>
     <input type="text" name="hostname" size="8" id="hostname" tabindex="1" />
     <label for="port">port</label>
     <input type="text" name="port" id="port" size="4" value="9160" />
     <input type="submit" value="Open!" name="open" />
     <br />
     <span style="font-size: 70%; font-family: serif">(password is specified further in basic authentication window.)</span>
 </form>
</body>
</html>
