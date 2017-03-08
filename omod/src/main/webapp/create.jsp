<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%--<%@ include file="template/localHeader.jsp"%>--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/codemirror/CodeMirror/master/lib/codemirror.css">
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/codemirror/codemirror.js" />
<script type="text/javascript" src="https://cdn.rawgit.com/codemirror/CodeMirror/master/mode/xml/xml.js"></script>
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
        $('#templateForm').submit(function(){
            alert('hola')
            var string = $('#editor').val()
            newTemp = string.replace(/"/g, "'");
            $('#editor').val(newTemp);
            return true;
        })
    });

</script>

<h2><openmrs:message code="radiologyfhirsupport.mrrt.create"/></h2>

<form id="templateForm">
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.create.summary"/> </b>
    <div class="box">
        <table cellpadding="3px" cellspacing="2px">
            <tr>
                <td>MRRT Template Name</td>
                <td><input type="text" name="name"/></td>
            </tr>
        </table>
    </div>
    <br>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.create.xml"/> <a href="https://radreport.org"><openmrs:message code="radiologyfhirsupport.mrrt.create.xml.url"/> </a> </b>
    <div class="box">
        <textarea name="xml" id="editor" path="xml" rows="20" cols="50"></textarea>
        <%--<td><form:errors path="xml" cssClass="error"/></td>--%>
    </div>
    <br>
    <input type="submit" value="Create Template" formmethod="post"/>
    &nbsp;
    <input type="button" value="Cancel"
           onclick="history.go(-1); return; document.location='index.htm?autoJump=false&amp;phrase='">
</form>

<script>
    var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
        lineNumbers: true,
        mode:  "xml"
    });
</script>


<%@ include file="/WEB-INF/template/footer.jsp"%>