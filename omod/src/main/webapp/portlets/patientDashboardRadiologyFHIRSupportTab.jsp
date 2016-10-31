<%@ page import="org.openmrs.module.radiologyfhirsupport.MRRTTemplate" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
Hello World <br>
Message = ${model.message}
Template Size = ${model.templates.size}
Encounters = ${model.encounters}
<%--<%! List<MRRTTemplate> mrrtTemplates = templates; %>--%>
