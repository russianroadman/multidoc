
var blocks = document.getElementsByClassName("block");
var editors = []

window.onload = function() {
    setVersion();
    setAuthor();
    setListenersToTextAreas();
    arrangeEditors();
    setListenerToDocTitle();
};

function setListenerToDocTitle(){
    document.getElementById('mdoc-title').addEventListener('change', setDocTitle);
}

function setDocTitle(){
    title = {
        content : document.getElementById('mdoc-title').value
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-doc-title",
        data : JSON.stringify(title),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data);
            $('#mdoc-title').value = data.content;
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function addNewBlock(){

}

function setBlockTitle(){

}

function addNewVersion(){

}

function setVersionAuthor(){

}

function saveVersionContent(){

}

function searchViaAjax() {
    search = {
        content : editors[0].getData()
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "test",
        data : JSON.stringify(search),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data);
            editors[0].setData(data.content);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

for (const item of blocks){
    DecoupledEditor
                .create( item.getElementsByClassName("editor")[0] )
                .then( editor => {
                    editors.push(editor);
                    var toolbarContainer = document.getElementsByClassName("toolbar-container")[0];
                    toolbarContainer.appendChild( editor.ui.view.toolbar.element );
                } )
                .catch( error => {
                    console.error( error );
                } );
}

function arrangeEditors(){
    var toolbars = document.getElementsByClassName("ck-toolbar_grouping");
    for (var i = 0; i<toolbars.length; i++){
        toolbars[i].style.visibility = "hidden";
    }
    toolbars[0].style.visibility = "visible";
}

function rearrangeEditors(index){
    console.log(index);
    var toolbars = document.getElementsByClassName("ck-toolbar_grouping");
    for (var i = 0; i<toolbars.length; i++){
        toolbars[i].style.visibility = "hidden";
    }
    console.log(toolbars[index].parentElement);
    toolbars[index].style.visibility = "visible";
}

function setVersion(){
    var blocks = document.getElementsByClassName("block");
    for (var i = 0; i<blocks.length; i++){
        var vs = blocks[i].getElementsByClassName("version-content")[0].innerHTML;
        editors[i].setData(vs);
    }
}

function setAuthor(){
    var blocks = document.getElementsByClassName("block");
    for (var i = 0; i<blocks.length; i++){
        var vs = blocks[i].getElementsByClassName("version-author")[0].innerHTML;
        blocks[i].getElementsByClassName("block-author")[0].innerHTML = vs;
    }
}

function test(){
    document.getElementsByClassName("editor")[0].submit();
}

function setListenersToTextAreas(){
    var x = document.getElementsByClassName("editor");
    for (var i = 0; i < x.length; i++){
        x[i].addEventListener('focus', (event) => {
            rearrangeEditors(
                parseInt(
                    event.target
                        .parentElement.parentElement
                        .getElementsByClassName("block-controller")[0]
                        .getElementsByClassName("left-ctrl")[0]
                        .getElementsByClassName("block-number")[0]
                        .innerHTML
                )-1
            );
        });
    }
}





