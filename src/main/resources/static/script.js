
var blocks = document.getElementsByClassName("block");
var editors = []
for (const item of blocks){

    /*DecoupledEditor
                .create( item.getElementsByClassName("editor")[0] )
                .then( editor => {
                    var toolbarContainer = item.getElementsByClassName("toolbar-container")[0];
                    toolbarContainer.appendChild( editor.ui.view.toolbar.element );
                } )
                .catch( error => {
                    console.error( error );
                } );*/
    BalloonEditor
        .create( item.getElementsByClassName("editor")[0] )
        .then( editor => {
            editors.push(editor);
        } )
        .catch( error => {
            console.error( error );
        } );

}

window.onload = function() {
    setVersion();
    setAuthor();
};

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



