<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>


<h2>
    <openmrs:message code="radiologyfhirsupport.manage"/>
</h2>
<p>
    <a href="${pageContext.request.contextPath}/module/radiologyfhirsupport/template/new.form"><openmrs:message
            code="radiologyfhirsupport.manage"/></a><br/><br/>
</p>

<c:out value="${message}"/>
<c:out value="${voidTemplates.size()}"/>
<c:out value="${reports.size()}"/>
<%--<c:forEach var="template" items="${voidTemplates}">
    <c:out value="${template.name}"/>
</c:forEach>--%>
<div>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.manage.list.table.header"/></b>
    <div class="box">
        <table>
            <tr>
                <th>S.no</th>
                <th>MRRT Template Name</th>
                <th>No of Reports</th>
                <th>Actions</th>
            </tr>
            <% int sNo=0;%>
            <c:forEach var="template" items="${voidTemplates}">
                <%sNo++;%>
                <tr>
                    <td><c:out value="<%= sNo%>"/></td>
                    <td><c:out value="${template.name}"/></td>
                    <td><c:out value="${reports.get(i).size()}"/> </td>
                    <td><a href=""><img src="" title=""/></a> </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp" %>