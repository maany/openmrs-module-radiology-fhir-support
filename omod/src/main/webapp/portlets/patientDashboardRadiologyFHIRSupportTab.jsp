<%@ page import="org.openmrs.module.radiologyfhirsupport.MRRTTemplate" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
Hello World <br>

<%--<%! List<MRRTTemplate> mrrtTemplates = templates; %>--%>
    <table>
        <tr>
            <th>Edit</th>
            <th>FHIR</th>
            <th>MRRT Report Name</th>
            <th>Encounter Date</th>
            <th>MRRT Template</th>
        </tr>
        <c:forEach items="${model.reports}" var="report" varStatus="i">
            <tr>
                <td><a href="">pic</a></td>
                <td><a href="">pic ${report.encounter.uuid}</a></td>
                <td>${report.name}</td>
                <td>${report.encounter.encounterDatetime}</td>
                <td>${report.mrrtTemplate.name}</td>
            </tr>
        </c:forEach>
    </table>
    <div id="create-report">
        <form method="get" action="${pageContext.request.contextPath}/module/radiologyfhirsupport/report/new.form">
            <% %>
            MRRT Template
            <select name="templateId">
                <c:forEach items="${model.templates}" var="template" varStatus="i">
                    <option value="${template.id}">${template.name}</option>
                </c:forEach>
            </select>
            <input type="hidden" name="patientId" value="${patient.id}"/>
         <%--TODO add location field  --%>
        <input type="submit" value="Create Report"/>
        </form>
    </div>
