<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>${keyspace} type definition</title></head>
<body>
    <h2>Type definition</h2>
    <h3>${keyspace}</h3>
    <form>

        <table cellpadding="4">
            <tr>
                <th>
                    Column family
                </th>
                <th colspan="2">
                    Column
                </th>
                <th>
                    Type
                </th>
                <th>
                    Is array?
                </th>
                <th>
                    Start offset
                </th>
                <th>
                    End offset
                </th>
            </tr>

            <tr>
                <td colspan="3">
                    (ANY)
                </td>
                <td>
                    <select>
                        <option>default</option>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
            <tr>
                <td>
                    Employees
                </td>
                <td colspan="2">
                    dept_no
                </td>
                <td>
                    <select>
                        <option>null</option>
                        <option>boolean(1 byte)</option>
                        <option>boolean(4 byte)</option>
                        <option>char</option>
                        <option>short</option>
                        <option>integer</option>
                        <option>long</option>
                        <option>float</option>
                        <option>double</option>
                        <option>Date</option>
                        <option>String</option>
                        <option>UUID</option>
                        <option>Object</option>
                    </select>
                </td>
                <td>
                    <input type="checkbox" name="isArray" value="array" />
                </td>
                <td>
                    <input type="text" name="prefixLength" value="0" size="1" />
                </td>
                <td>
                    <input type="text" name="postfixLength" value="0" size="1" />
                </td>
            </tr>
        </table>

    </form>

    <%@ include file="controls.jsp"%>

</body>
</html>