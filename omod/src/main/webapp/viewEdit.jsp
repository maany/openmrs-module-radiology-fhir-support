<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%--<%@ include file="template/localHeader.jsp"%>--%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/codemirror/CodeMirror/master/lib/codemirror.css">
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/codemirror/codemirror.js" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/js/sync.js" />
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
        var editor = activateSync("${xml}")
        $("#delete").click(function () {
            var response = confirm("Are you sure you want to delete this template??");
            if (response == true) {
                editor.setValue(' ');
                editor.refresh();
                $.ajax({
                    url: window.location.pathname,
                    type: 'DELETE',
                    success: function (result) {
                        alert('delete success')
                        window.location.replace("${deleteRedirectURL}");
                    },
                    error: function (result) {
                        alert('error');
                        console.log(result);
                        window.location.replace("${deleteRedirectURL}");
                    }
                })
            }else {

            }
        })

    });

</script>
<h2><openmrs:message code="radiologyfhirsupport.mrrt.viewEdit"/></h2>
<form>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.create.summary"/> </b>
    <div class="box">
    <table cellpadding="3px" cellspacing="2px">
        <tr>
            <td>MRRT Template Name</td>
            <td><input type="text" name="name" value="${template.name}"/></td>
        </tr>
    </table>
    </div>
    <br>
    <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.view.template"/> </b>
     <div class="box">
            <div id='template'></div>
     </div>
     <br>
     <b class="boxHeader"><openmrs:message code="radiologyfhirsupport.mrrt.create.xml"/> <a href="https://radreport.org"><openmrs:message code="radiologyfhirsupport.mrrt.create.xml.url"/> </a> </b>
      <div class="box">
            <textarea name="xml" id = "editor" rows="20" cols="50"></textarea>
      </div>
    <br>
    <input type="submit" value="Save Changes" formmethod="post" />
    <input type="button" id="delete" value="Delete"/>
    <input type="button" value="Cancel"
           onclick="history.go(-1); return; document.location='index.htm?autoJump=false&amp;phrase='">
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
