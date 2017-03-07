<%@ page import="org.openmrs.module.radiologyfhirsupport.MRRTTemplate" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/js/jquery.json-viewer.js" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/js/clipboard.js" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/js/jquery.magnific-popup.min.js" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/css/jquery.json-viewer.css" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/css/magnific-popup.css" />
<style>
    .white-popup {
        position: relative;
        background: #FFF;
        padding: 20px;
        width: auto;
        max-width: 500px;
        margin: 20px auto;
    }
    .td {
        padding-left: 5px;
        padding-right: 5px;
    }
    .tr {
        background-color: rgb(128,128,128);
    }
</style>
<script>
    function displayJSONContent(url){
        $.ajax({
            url: url ,
            success: function(result){
                alert(result);
                console.log(result)
                $('#json-content').jsonViewer(result);
                $.magnificPopup.open({
                    items: {
                        src: '#popup', // can be a HTML string, jQuery object, or CSS selector
                        type: 'inline'
                    }
                });
            }
        })
    }

    function copyToClipboard(content) {
        // create hidden text element, if it doesn't already exist
        var targetId = "_hiddenCopyText_";
        var origSelectionStart, origSelectionEnd;
        // must use a temporary form element for the selection and copy
        var target = document.createElement("textarea");
        target.style.position = "absolute";
        target.style.left = "-9999px";
        target.style.top = "0";
        target.id = targetId;
        document.body.appendChild(target);
        target.textContent = content;

        // select the content
        var currentFocus = document.activeElement;
        target.focus();
        target.setSelectionRange(0, target.value.length);

        // copy the selection
        var succeed=true;
        try {
            succeed = document.execCommand("copy");
        } catch(e) {
            succeed = false;
            console.log(e.stack)
        }
        // restore original focus
        if (currentFocus && typeof currentFocus.focus === "function") {
            currentFocus.focus();
        }

        // clear temporary content
        target.textContent = "";

        return succeed;
    }
    function copyFromAjaxResponse(url){
        var data;
        $.ajax({
            url: url ,
            success: function(result){
                data=result;
            }
        })

        copyToClipboard(data);
    }
</script>
<div id="create-report">
    <b class="boxHeader">Create MRRT Reports</b>
    <form class="box" method="get" action="${pageContext.request.contextPath}/module/radiologyfhirsupport/report/new.form">
        <table>
            <tr>
                <td>MRRT Template</td>
                <td>
                    <select name="templateId">
                        <c:forEach items="${model.templates}" var="template" varStatus="i">
                            <option value="${template.id}">${template.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <input type="hidden" name="patientId" value="${patient.id}"/>
        <%--TODO add location field  --%>
        <input type="submit" value="Create Report"/>
    </form>
</div>
<b class="boxHeader">Current MRRT Reports</b>
<div class="box">
    <table>
        <tr>
            <th>FHIR Actions</th>
            <th>MRRT Report Name</th>
            <th>Encounter Date</th>
            <th>MRRT Template</th>
        </tr>
        <c:forEach items="${model.reports}" var="report" varStatus="i">
            <tr class="eventRow">
                <td>
                    <a href="${pageContext.request.contextPath}/ws/fhir/DiagnosticReport/${report.encounter.uuid}"><img
                            src="${pageContext.request.contextPath}/images/file.gif"
                            title="Redirect to pure JSON FHIR Diagnostic Report. Use it to save as a json response"/></a>
                    <img
                            src="${pageContext.request.contextPath}/images/note.gif"
                            style="cursor: pointer; cursor: hand;" class="open-popup-link"
                            onClick='displayJSONContent("${pageContext.request.contextPath}/ws/fhir/DiagnosticReport/${report.encounter.uuid}")'
                            title="Quick overview of FHIR Diagnostic Report in a popup"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/module/radiologyfhirsupport/report/view/${report.id}.form">${report.name}</a>
                </td>
                <td>${report.encounter.encounterDatetime.date}/${report.encounter.encounterDatetime.month + 1}/${report.encounter.encounterDatetime.year + 1900}</td>
                <td>${report.mrrtTemplate.name}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="popup" class="white-popup mfp-hide">
    <div id="headers">
        <center><b>FHIR Diagnostic Report</b></center>
    </div>
    <div id="json-content">
        This part will display the result from ajax query.
    </div>
</div>