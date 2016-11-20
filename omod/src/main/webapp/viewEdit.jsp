<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
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
    var editorDOMElement;
    $(document).ready(function () {
        editorDOMElement = editor.getWrapperElement();
        //$(editorDOMElement).hide();
        $("#delete").click(function () {
            var pageUrl = window.location.href;
            alert("Are you sure you want to delete this template??");
                       $.ajax({
             url: pageUrl,
             type: 'DELETE'
             });
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
        <td>MRRT Template Name</td>
        <td><form:input path="name"/></td>
        <td><form:errors path="name" cssClass="error"/></td>
    </tr>

    <tr>
        <td>Template</td>
        <td id="template"></td>
    </tr>
<%--    <tr>
        <td><button id="showCodeWindow">Show XML</button></td>
        <td><button id="hideCodeWindow">Hide XML</button></td>
    </tr>--%>
    <tr>
            <td>XML</td>
            <td><textarea name="xml" id = "editor" rows="20" cols="50"></textarea></td>
        <%--<td><form:errors path="
        .+xml" cssClass="error"/></td>--%>
    </tr>
</table>
<input type="submit" value="Save Changes" formmethod="post" />
<button id="delete">Delete</button>
</form:form>
<%--<script>
    $("#hideCodeWindow").click(function () {
        $(editorDOMElement).hide(1000);
    });
    $("#showCodeWindow").click(function () {
        $(editorDOMElement).show(1000);
    })
</script>--%>
<script>
    var editor = document.getElementById('editor');
    var xml = "${xml}";
    editor.value+= xml;
    var template = document.getElementById('template');
    template.innerHTML = xml;
</script>
<script>
    var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
        lineNumbers: true,
        mode:  "xml"
    });
</script>
<script>
    function getHTML(target){
        var wrap = document.createElement('div');
        wrap.appendChild(target.cloneNode(true));
        return wrap.innerHTML;
    }
    var lastEventObject;
    var oldString,newString;
    var inputs = document.getElementsByTagName('input');
    $('input').change(function (eventObject) {
        lastEventObject = eventObject;
        var target = eventObject.target;
        //alert(getHTML(eventObject.target));
        //alert("Form changed");
        switch (target.type) {
            case "text":
                //alert("text box found");
                //console.log("New value : " + target.value)
                oldString = getHTML(eventObject.target).replace(/"/g, '\'').slice(0,-1);
                target.setAttribute("value",target.value)
                newString = getHTML(target).replace(/"/g, '\'').slice(0,-1);
                xml = editor.getValue();
                xml  = xml.replace(oldString,newString);
                //console.log(xml)
                break;
            default:

        }
        editor.getDoc().setValue(xml);
        editor.refresh();
    });

</script>



<%@ include file="/WEB-INF/template/footer.jsp" %>
