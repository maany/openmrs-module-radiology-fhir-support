<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>


<h2>
    <%--<openmrs:message code="radiologyfhirsupport.mrrt.manage"/>--%>
</h2>
<p>
<a href="${pageContext.request.contextPath}/module/radiologyfhirsupport/template/new.form"><openmrs:message
        code="radiologyfhirsupport.mrrt.registered.new"/></a><br/><br/>
</p>


<div>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.manage.list.table.header"/></b>

    <div class="box">
        <div class="searchWidgetContainer" id="findPersons">
            <table>
                <tr>
                    <th>S.no</th>
                    <th>Name</th>
                    <th>Encounter UUID</th>
                    <th>Encounter Date</th>
                </tr>
                <% int sNo = 0; %>
                <c:forEach items="${templateMap}" var="templateMap" varStatus="i">
                    <% sNo++;%>
                    <tr>
                        <td><c:out value="<%= sNo%>"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/module/radiologyfhirsupport/template/view/${templateMap.key.id}.form"><c:out
                                    value="${templateMap.key.name}"/></a></td>
                        <td><a href="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${templateMap.value.encounterId}" ><c:out value="${templateMap.key.encounterUuid}"/></a></td>
                        <td><c:out value="${templateMap.value.encounterDatetime}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>