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
<script>
    $(document).ready(function () {
        $("button").click(function () {
            var pageUrl = window.location.href;
            alert("Are you sure you want to delete this template??");
            /*            $.ajax({
             url: pageUrl,
             type: 'DELETE'
             });*/
        });
    });
</script>
<h2><openmrs:message code="radiologyfhirsupport.mrrt.viewEdit"/></h2>
Yoyo
<form:form modelAttribute="template">
<form:errors path="*" cssClass="errorblock" element="div"/>
<form:hidden path="id"/>
<table>
    <tr>
        <td>MRRT Report Name</td>
        <td><form:input path="name"/></td>
        <td><form:errors path="name" cssClass="error"/></td>
    </tr>
    <tr>
        <td>XML</td>
        <td><form:textarea path="xml" rows="20" cols="50"/></td>
        <td><form:errors path="xml" cssClass="error"/></td>
    </tr>
</table>
<input type="submit" value="Save Changes" formmethod="post" />
<button>Delete</button>
</form:form>


<%@ include file="/WEB-INF/template/footer.jsp" %>
