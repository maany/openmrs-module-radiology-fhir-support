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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/codemirror/CodeMirror/master/lib/codemirror.css"/>
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/codemirror/codemirror.js" />
<openmrs:htmlInclude file="/moduleResources/radiologyfhirsupport/js/sync.js" />
<script type="text/javascript" src="https://cdn.rawgit.com/codemirror/CodeMirror/master/mode/xml/xml.js"></script>
<script>
    $(document).ready(function () {
        var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
            lineNumbers: true,
            mode:  "xml"
        });
        var editorContainer = document.getElementById('editor');
        var report = document.getElementById("report")
        var xml = "${xml}"
        editor.setValue(xml);
        report.innerHTML=xml;

        function getHTML(target){
            var wrap = document.createElement('div');
            wrap.appendChild(target.cloneNode(true));
            return wrap.innerHTML;
        }
        var lastEventObject;
        var oldString,newString;
        $('input').change(function (eventObject) {
            lastEventObject = eventObject;
            var target = eventObject.target;
            //alert(getHTML(eventObject.target));
            //alert("Form changed");
            switch (target.type) {
                case "text":
                    alert("text box found");
                    console.log("New value : " + target.value)
                    oldString = getHTML(eventObject.target).replace(/"/g, '\'').slice(0, -1);
                    target.setAttribute("value", target.value)
                    newString = getHTML(target).replace(/"/g, '\'').slice(0, -1);
                    xml = editor.getValue();
                    xml = xml.replace(oldString, newString);
                    console.log(xml)
                    break;
                default:
            }
            editor.setValue(xml);
            editor.refresh();
        })


        // Select and Option handler
        $('select').on('change', function(eventObject) {
            var target = eventObject.target;
            oldString = getHTML(target).replace(/"/g, '\'')
            var selected = this.value
            removeAttributeSelected(target);
            selectOption(target, selected);
            newString = getHTML(target).replace(/"/g, '\'');
            xml = editor.getValue()
            var oldArray = oldString.split('\n');
            var newArray = newString.split('\n');
            for(var i=0;i<oldArray.length;i++){
                xml = xml.replace(oldArray[i],newArray[i])
            }
            editor.setValue(xml)
            editor.refresh()
        })

        function getHTML(target) {
            var wrap = document.createElement('div');
            wrap.appendChild(target.cloneNode(true));
            return wrap.innerHTML;
        }

        function removeAttributeSelected(target) {
            $(target).children('option').each(function() {
                $(this).removeAttr('selected')
            })
        }

        function selectOption(target, val) {
            $(target).children('option').each(function() {
                alert('current value : ' + this.value + " and val is " + val + "  ")
                if (this.value == val) {
                    alert('match found')
                    $(this).attr('selected', 'selected')
                    alert("test " + $(this).attr('selected'))
                }
            })
            alert(getHTML(target))
        }

    });
</script>
<h2><openmrs:message code="radiologyfhirsupport.mrrt.report.create"/></h2>

<form>
    <table>
        <tr>
            <td>MRRT Report Name</td>
            <td><input type = "text" name="name"/></td>
        </tr>
        <tr>
            <td>MRRT Report</td>
            <td id="report"></td>
        </tr>
        <tr>
            <td>XML</td>
            <td><textarea name = "xml" id = "editor" path="xml" rows="20" cols="50"></textarea></td>
                <%--<td><form:errors path="xml" cssClass="error"/></td>--%>
        </tr>
    </table>
    <input type="submit" value="Create Report " formmethod="post" />
</form>



<%@ include file="/WEB-INF/template/footer.jsp"%>