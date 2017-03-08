<%@ page import="java.util.Calendar" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%--<%@ include file="template/localHeader.jsp"%>--%>

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
        $('#date').attr("value",dateSetter());
    });
    function dateSetter() {
        //dateFormatFixer
        var millis = ${report.encounter.encounterDatetime.getTime()};
        var date = new Date(new Date(millis).toLocaleString("en-US",{timeZone : "<%= Calendar.getInstance().getTimeZone().getID()%>"}));
        var day = date.getDate();
        var month = date.getMonth() + 1
        var hours = date.getHours();
        var minutes = date.getMinutes()
        if (month < 10)
            month = '0' + month
        if (day < 10)
            day = '0' + day
        if (hours < 10)
            hours = '0' + hours
        if (minutes < 10)
            minutes = '0' + minutes

        var final = date.getFullYear() + '-' + month + '-' + day + 'T' + hours + ':' + minutes
        return final;

    }
    function deleteReport(){
        alert('Make REST call to delete report')
        $.ajax({
            url: window.location.pathname,
            type: 'DELETE',
            success : function (result) {
                alert('delete success')
                window.location.replace("${deleteRedirectURL}");
            }
        })
    }
</script>
<h2><openmrs:message code="radiologyfhirsupport.mrrt.report.edit"/></h2>
<b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.report.create.summary"/></b>
<form>
    <div class="box">
        <table>
            <tr>
                <td>MRRT Report Name</td>
                <td><input type="text" name="name" value="${report.name}"/></td>
            </tr>
            <tr>
                <td>Location</td>
                <td>
                    <select name="locationId">
                        <c:forEach var="location" items="${locations}">
                            <c:if test="${location.locationId == report.encounter.location.locationId}">
                                <option value="${location.locationId}" selected="selected"><c:out
                                        value="${location.name}"/></option>
                            </c:if>
                            <c:if test="${location.locationId != report.encounter.location.locationId}">
                                <option value="${location.locationId}"><c:out value="${location.name}"/></option>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Date</td>
                <td>
                    <input id='date' type="datetime-local" name="dateString"/>
                </td>
            </tr>
        </table>
    </div>
    <br>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.report.create.report"/></b>
    <div class="box">
        <div id="report"></div>
    </div>
    <br>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.report.create.xml"/></b>
    <div class="box">
        <textarea name="xml" id="editor" path="xml" rows="20" cols="50"></textarea>
        <%--<td><form:errors path="xml" cssClass="error"/></td>--%>
    </div>
    <br>
    <input type="submit" value="Save Report " formmethod="post"/>
    &nbsp;
    <input type="button" value="Delete Report" onclick="deleteReport()"/>
    &nbsp;
    <input type="button" value="Cancel"
           onclick="history.go(-1); return; document.location='index.htm?autoJump=false&amp;phrase='">
</form>



<%@ include file="/WEB-INF/template/footer.jsp"%>