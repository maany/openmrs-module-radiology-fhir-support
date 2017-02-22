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
        if (this.value == val) {
            $(this).attr('selected', 'selected')
        }
    })
}

function activateSync(xml) {
     var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
            lineNumbers: true,
            mode:  "xml"
        });
        var editorContainer = document.getElementById('editor');
        var report = document.getElementById("report")
        editor.setValue(xml);
        report.innerHTML=xml;

        var lastEventObject;
        var oldString,newString;

        $('input').change(function (eventObject) {
            lastEventObject = eventObject;
            var target = eventObject.target;
            //alert("text box found");
            console.log("New value : " + target.value)
            oldString = getHTML(eventObject.target).replace(/"/g, '\'').slice(0, -1);
            target.setAttribute("value", target.value)
            newString = getHTML(target).replace(/"/g, '\'').slice(0, -1);
            xml = editor.getValue();
            xml = xml.replace(oldString, newString);
            console.log(xml)
            editor.setValue(xml);
            editor.refresh();

        })

        // Select and Option handlerNormal.
        $('select').on('change', function(eventObject) {
            var target = eventObject.target;
            var value = this.value
            oldString = getHTML(target).replace(/"/g, '\'')
            removeAttributeSelected(target);
            selectOption(target, value);
            newString = getHTML(target).replace(/"/g, '\'');
            xml = editor.getValue()
            var oldArray = oldString.split('\n');
            var newArray = newString.split('\n');
            for(var i=0;i<oldArray.length;i++){
                xml = xml.replace(oldArray[i],newArray[i])
            }
            editor.setValue(xml)
            editor.refresh()
            $(this).val(value)
        })

        // TextArea sync handler
        $('textarea').on('change', function(eventObject) {
            //alert("TextArea changed")
            var target = eventObject.target;
            var value = this.value
            oldString = getHTML(eventObject.target).replace(/"/g, '\'').slice(0, -1);
            target.setAttribute("value", target.value)
            target.innerHTML = target.value;
            newString = getHTML(target).replace(/"/g, '\'').slice(0, -1);
            xml = editor.getValue();
            xml = xml.replace(oldString, newString);
            editor.setValue(xml)
            editor.refresh()
        })
}
