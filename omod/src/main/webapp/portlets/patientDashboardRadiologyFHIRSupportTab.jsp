<%@ page import="org.openmrs.module.radiologyfhirsupport.MRRTTemplate" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
Hello World <br>

Encounters = ${model.encounters}
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
                <td>pic</td>
                <td>pic</td>
                <td>report.name</td>
                <td>report.encounter.dateCreated</td>
                <td>report.mrrtTemplate</td>
            </tr>
        </c:forEach>
    </table>