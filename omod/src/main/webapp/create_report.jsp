<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<style>
    .error {
        color: #ff0000;
    }

    .errorblock {
        color: #000;
        background-color: #ffEEEE;
        border: 3px solid #ff0000;
        padding: 8px;
        margin: 16px;
    }
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/codemirror/CodeMirror/master/lib/codemirror.css"/>
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/codemirror/codemirror.js" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/js/sync.js" />
<script type="text/javascript" src="https://cdn.rawgit.com/codemirror/CodeMirror/master/mode/xml/xml.js"></script>
<script>
    $(document).ready(function () {
        activateSync("${xml}")
    });
</script>
<h2><openmrs:message code="radiologyfhirsupport.mrrt.report.create"/></h2>

<form>
    <table>
        <tr>
            <td>MRRT Report Name</td>
            <td><input type = "text" name="name"/></td>
        </tr>
        <tr>
            <td>Location</td>
            <td>
                <select name="locationId">
                    <c:forEach var="location" items="${locations}">
                        <option value="${location.locationId}"><c:out value="${location.name}"/></option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Date</td>
            <td>
                <input type="datetime-local" name="dateString"/>
            </td>
        </tr>
        <tr>
            <td>MRRT Report</td>
            <td id="report"></td>
        </tr>
        <tr>
            <td>XML</td>
            <td><textarea name = "xml" id = "editor" path="xml" rows="20" cols="50"></textarea></td>
                <%--<td><form:errors path="xml" cssClass="error"/></td>--%>
        </tr>
    </table>
    <input type="submit" value="Create Report " formmethod="post" />
</form>



<%@ include file="/WEB-INF/template/footer.jsp"%>