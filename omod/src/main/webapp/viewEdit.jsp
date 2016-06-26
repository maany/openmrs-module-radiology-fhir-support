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

<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/codemirror/CodeMirror/master/lib/codemirror.css">
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/codemirror/codemirror.js" />
<script type="text/javascript" src="https://cdn.rawgit.com/codemirror/CodeMirror/master/mode/xml/xml.js"></script>


<h2><openmrs:message code="radiologyfhirsupport.mrrt.viewEdit"/></h2>

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
        <td><textarea id = "editor" rows="20" cols="50"></textarea></td>
        <%--<td><form:errors path="
        .+xml" cssClass="error"/></td>--%>
    </tr>
</table>
<input type="submit" value="Save Changes" formmethod="post" />
<button>Delete</button>
</form:form>
<script>
    var editor = document.getElementById('editor');
    var xml = "${xml}";
    editor.value+= xml;
</script>
<script>
    var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
        lineNumbers: true,
        mode:  "xml"
    });
</script>


<%@ include file="/WEB-INF/template/footer.jsp" %>
