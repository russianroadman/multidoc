
window.onload = function() {
    console.log('window loaded');
}

var editors = [];
var focusedElement = null;

forceUpdateDoc();
updateDoc();

var doc = {
    link : window.location.search.substring(6),
    title : 'unknown',
    blocks : []
}

function getVersionById(id){
    let result;
    for (var i = 0; i < doc.blocks.length; i++){
        result = doc.blocks[i].versions.find(el => el.id === id);
        if (result) break;
    }
    return result;
}

function getBlockById(id){
    return doc.blocks.find(el => el.id === id);
}

function setModelVersionContent(index, data){
    let blockIndex = index;
    let versionIndex = parseInt(
        document
            .getElementsByClassName('block')[index]
            .getElementsByClassName('block-version')[0].innerHTML
    )-1;
    doc.blocks[blockIndex].versions[versionIndex].content = data;
}

function focus(index, condition){
    if (condition) {
        focusedElement = document.getElementsByClassName('block')[index];
    } else {
        focusedElement = null;
    }
    //console.log('focusedElement: ', focusedElement);
}

function setDoc(json){

    doc.link = json.link;
    doc.title = json.title;
    doc.blocks = json.blocks;

    if (focusedElement){
        updateViewWithFocused();
    } else {
        updateView();
    }

}

function goLeft(element){

    // <====

    var blockIndex = parseInt(element.parentElement.parentElement.getElementsByClassName('block-number')[0].innerHTML)-1;
    var versionIndex = parseInt(element.parentElement.parentElement.getElementsByClassName('block-version')[0].innerHTML)-1;

    if (versionIndex > 0){

        let starColor = '#efefef';
        if (doc.blocks[blockIndex].versions[versionIndex-1].isPreferred == 'true'){
            starColor = '#fff491';
        }
        let versionAuthor = doc.blocks[blockIndex].versions[versionIndex-1].author;

        element.parentElement.parentElement.getElementsByClassName('block-version')[0].innerHTML = versionIndex;
        element.parentElement.parentElement.parentElement.getElementsByClassName('editor-star-version-svg')[0].style.fill = starColor;
        element.parentElement.parentElement.getElementsByClassName('block-author')[0].value = versionAuthor;
        editors[blockIndex].setData(
            doc.blocks[blockIndex].versions[versionIndex-1].content
        );
    }

}

function goRight(element){

    // ====>

    var blockIndex = parseInt(element.parentElement.parentElement.getElementsByClassName('block-number')[0].innerHTML)-1;
    var versionIndex = parseInt(element.parentElement.parentElement.getElementsByClassName('block-version')[0].innerHTML)-1;
    //console.log(blockIndex, "-", versionIndex);

    if (versionIndex < doc.blocks[blockIndex].versions.length-1){

        let starColor = '#efefef';
        if (doc.blocks[blockIndex].versions[versionIndex+1].isPreferred == 'true'){
            starColor = '#fff491';
        }
        let versionAuthor = doc.blocks[blockIndex].versions[versionIndex+1].author;

        element.parentElement.parentElement.getElementsByClassName('block-version')[0].innerHTML = versionIndex+2;
        element.parentElement.parentElement.parentElement.getElementsByClassName('editor-star-version-svg')[0].style.fill = starColor;
        element.parentElement.parentElement.getElementsByClassName('block-author')[0].value = versionAuthor;
        editors[blockIndex].setData(
            doc.blocks[blockIndex].versions[versionIndex+1].content
        );
    }

}

function updateView(){

    document.getElementById('mdoc-title').value = doc.title;
    var content = document.getElementById('content');

    html = '';

    for (var i = 0; i < doc.blocks.length; i++){


        let starColor = '#efefef';

        if (doc.blocks[i].versions[0].isPreferred == 'true'){
            starColor = '#fff491';
        }

        let blockNumber = i+1;
        let blockTitle = doc.blocks[i].title;
        let versionNumber = 1;
        let versionSize = doc.blocks[i].versions.length;
        let versionAuthor = doc.blocks[i].versions[0].author;

        let blockId = doc.blocks[i].id;
        let versionId = doc.blocks[i].versions[0].id;

        html +=

        '<div class="block">' +
            '<input class="blockId" style="display:none" type="text" value="' + blockId + '">' +
            '<input class="versionId" style="display:none" type="text" value="' + versionId + '">' +
            '<div class="block-content">' +
                '<div class="editor-wrapper">' +
                    '<div class="editor-bar">' +
                        '<button class="editor-star-version" onclick="starVersion(this)">' +
                            '<svg fill="' + starColor + '" class="editor-star-version-svg" xmlns="http://www.w3.org/2000/svg" width="1.5vw" height="1.5vw" viewBox="0 0 24 24"><path d="M12 .587l3.668 7.568 8.332 1.151-6.064 5.828 1.48 8.279-7.416-3.967-7.417 3.967 1.481-8.279-6.064-5.828 8.332-1.151z"></path></svg>' +
                        '</button>' +
                        '<button class="editor-actions" onclick="openContextActions(this)">' +
                            '•••' +
                        '</button>' +
                    '</div>' +
                    '<div class="context-actions">' +
                        '<button class="context-actions-button" onclick="deleteBlock(this)">delete&nbsp;block</button>' +
                        '<button class="context-actions-button" onclick="deleteVersion(this)" style="border-top:1px solid lightgray; border-bottom:1px solid lightgray;">delete&nbsp;version</button>' +
                        '<button class="context-actions-button" onclick="closeEditorContextMenu(this)">close</button>' +
                    '</div>' +
                    '<div class="editor"></div>' +
                '</div>' +
            '</div>' +
            '<div class="block-controller">' +
                '<div class="left-ctrl">' +
                    '<div class="block-number">'+ blockNumber +'</div>' +
                    '<input class="block-name" onchange="saveBlockTitle(this)" value="'+ blockTitle +'">' +
                '</div>' +
                '<div class="middle-ctrl">' +
                    '<button class="block-left" onclick="goLeft(this)">' +
                        '<svg xmlns="http://www.w3.org/2000/svg" fill="lightgrey" width="2vh" height="2vh" viewBox="0 0 24 24"><path d="M3 12l18-12v24z"></path></svg>' +
                    '</button>' +
                    '<div class="block-version">'+ versionNumber +'</div>/<div class="block-version block-max-version">'+ versionSize +'</div>' +
                    '<button class="block-right" onclick="goRight(this)">' +
                        '<svg xmlns="http://www.w3.org/2000/svg" fill="lightgrey" width="2vh" height="2vh" viewBox="0 0 24 24"><path d="M21 12l-18 12v-24z"></path></svg>' +
                    '</button>' +
                '</div>' +
                '<div class="right-ctrl">' +
                    '<input class="block-author" value="'+ versionAuthor +'" onchange="saveVersionAuthor(this)">' +
                    '<button class="block-add-version" onclick="showNewVersion(this)">' +
                        '<svg fill="gray" xmlns="http://www.w3.org/2000/svg" width="2vh" height="2vh" viewBox="0 0 24 24"><path d="M11 11v-11h1v11h11v1h-11v11h-1v-11h-11v-1h11z"></path></svg>' +
                    '</button>' +
                '</div>' +
            '</div>' +
        '</div>' ;

    }

    content.innerHTML = html;

    applyEditors();

}

function updateViewWithFocused(){

    let blocks = document.getElementsByClassName('block');

    for (let i = 0; i < editors.length; i++){

        let vId = blocks[i].getElementsByClassName('versionId')[0].value;

        if (blocks[i] != focusedElement){
            editors[i].setData( getVersionById(vId).content );
        }

    }

}

//function updateViewWithFocused(){
//
//    document.getElementById('mdoc-title').value = doc.title;
//
//    var content = document.getElementById('content');
//    var focusedMet = false;
//    var html;
//
//    var b = document.getElementsByClassName('block');
//    var v = 0;
//
//    var focusedIndex = null;
//    var focusedEditor = null;
//
//    while (document.getElementsByClassName('block').length > 1){
//        if (b[v] === focusedElement){
//            v = 1;
//        } else {
//            b[v].remove();
//        }
//    }
//
//    for (var i = 0; i < doc.blocks.length; i++){
//
//        let blockId = doc.blocks[i].id;
//        let versionId = doc.blocks[i].versions[0].id;
//
//        if (blockId == focusedElement.getElementsByClassName('blockId')[0].value){
//
//            focusedMet = true;
//            focusedIndex = i;
//            focusedEditor = editors[i];
//            //console.log('met focus');
//
//        } else {
//
//            let starColor = '#efefef';
//            if (doc.blocks[i].versions[0].isPreferred == 'true'){ starColor = '#fff491'; }
//            let blockNumber = i+1;
//            let blockTitle = doc.blocks[i].title;
//            let versionNumber = 1;
//            let versionSize = doc.blocks[i].versions.length;
//            let versionAuthor = doc.blocks[i].versions[0].author;
//
//            html =
//
//            '<input class="blockId" style="display:none" type="number" value="' + blockId + '">' +
//            '<input class="versionId" style="display:none" type="number" value="' + versionId + '">' +
//            '<div class="block-content">' +
//                '<div class="editor-wrapper">' +
//                    '<div class="editor-bar">' +
//                        '<button class="editor-star-version" onclick="starVersion(this)">' +
//                            '<svg fill="' + starColor + '" class="editor-star-version-svg" xmlns="http://www.w3.org/2000/svg" width="1.5vw" height="1.5vw" viewBox="0 0 24 24"><path d="M12 .587l3.668 7.568 8.332 1.151-6.064 5.828 1.48 8.279-7.416-3.967-7.417 3.967 1.481-8.279-6.064-5.828 8.332-1.151z"></path></svg>' +
//                        '</button>' +
//                        '<button class="editor-actions" onclick="openContextActions(this)">' +
//                            '•••' +
//                        '</button>' +
//                    '</div>' +
//                    '<div class="context-actions">' +
//                        '<button class="context-actions-button" onclick="deleteBlock(this)">delete&nbsp;block</button>' +
//                        '<button class="context-actions-button" onclick="deleteVersion(this)" style="border-top:1px solid lightgray; border-bottom:1px solid lightgray;">delete&nbsp;version</button>' +
//                        '<button class="context-actions-button" onclick="closeEditorContextMenu(this)">close</button>' +
//                    '</div>' +
//                    '<div class="editor"></div>' +
//                '</div>' +
//            '</div>' +
//            '<div class="block-controller">' +
//                '<div class="left-ctrl">' +
//                    '<div class="block-number">'+ blockNumber +'</div>' +
//                    '<input class="block-name" onchange="saveBlockTitle(this)" value="'+ blockTitle +'">' +
//                '</div>' +
//                '<div class="middle-ctrl">' +
//                    '<button class="block-left" onclick="goLeft(this)">' +
//                        '<svg xmlns="http://www.w3.org/2000/svg" fill="lightgrey" width="2vh" height="2vh" viewBox="0 0 24 24"><path d="M3 12l18-12v24z"></path></svg>' +
//                    '</button>' +
//                    '<div class="block-version">'+ versionNumber +'</div>/<div class="block-version block-max-version">'+ versionSize +'</div>' +
//                    '<button class="block-right" onclick="goRight(this)">' +
//                        '<svg xmlns="http://www.w3.org/2000/svg" fill="lightgrey" width="2vh" height="2vh" viewBox="0 0 24 24"><path d="M21 12l-18 12v-24z"></path></svg>' +
//                    '</button>' +
//                '</div>' +
//                '<div class="right-ctrl">' +
//                    '<input class="block-author" value="'+ versionAuthor +'" onchange="saveVersionAuthor(this)">' +
//                    '<button class="block-add-version" onclick="showNewVersion(this)">' +
//                        '<svg fill="gray" xmlns="http://www.w3.org/2000/svg" width="2vh" height="2vh" viewBox="0 0 24 24"><path d="M11 11v-11h1v11h11v1h-11v11h-1v-11h-11v-1h11z"></path></svg>' +
//                    '</button>' +
//                '</div>' +
//            '</div>';
//
//            if (focusedMet){
//
////                $('<div/>', { class: 'block' })
////                    .insertAfter( $( ".block" ).last() )
////                    .html(html);
//
//                let el = document.getElementById('content').appendChild( document.createElement("div") );
//                el.className = "block";
//                el.innerHTML = html;
//
//            } else {
//
////                $('<div/>', { class: 'block' })
////                    .insertBefore( focusedElement )
////                    .html(html);
//
//                let el = document.getElementById('content').insertBefore( document.createElement("div"), focusedElement );
//                el.className = "block";
//                el.innerHTML = html;
//
//            }
//
//        }
//
//    }
//
//    //applyEditorsWithFocused(focusedIndex, focusedEditor);
//
//}

/******************************************/
/************** AJAX REQUESTS *************/
/******************************************/

function saveDoc(){

    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-doc",
        data : JSON.stringify(doc),
        success : function() {
            console.log("doc saved");
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });

}

function updateDoc(){
    link = window.location.search.substring(6);
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "update-doc/"+link,
        success : function(data) {
            console.log('doc updated');
            setDoc(data);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        complete: function () {
            updateDoc();
        }
    });
}

function forceUpdateDoc(){
    link = window.location.search.substring(6);
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "force-update-doc/"+link,
        success : function(data) {
            console.log('doc force updated');
            setDoc(data);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function deleteBlock(element){
    let block = element.parentElement.parentElement.parentElement.parentElement;
    blockIndex = parseInt(block.getElementsByClassName('block-number')[0].innerHTML)-1;
    link = window.location.search.substring(6);
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "delete-block/"+link+"/"+blockIndex,
        success : function() {
            console.log('block deleted');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function deleteVersion(element){
    let block = element.parentElement.parentElement.parentElement.parentElement;
    blockIndex = parseInt(block.getElementsByClassName('block-number')[0].innerHTML)-1;
    versionIndex = parseInt(block.getElementsByClassName('block-version')[0].innerHTML)-1;
    link = window.location.search.substring(6);
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "delete-version/"+link+"/"+blockIndex+"/"+versionIndex,
        success : function() {
            console.log('version deleted');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function starVersion(element){
    let block = element.parentElement.parentElement.parentElement.parentElement;
    blockIndex = parseInt(block.getElementsByClassName('block-number')[0].innerHTML)-1;
    versionIndex = parseInt(block.getElementsByClassName('block-version')[0].innerHTML)-1;
    link = window.location.search.substring(6);
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "star-version/"+link+"/"+blockIndex+"/"+versionIndex,
        success : function() {
            console.log('version marked as favourite');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function addNewBlock(element){
    element.style.visibility = "hidden";
    var link = window.location.search.substring(6);
    var author = document.getElementById("new-block-author").value;
    var blockTitle = document.getElementById("new-block-title").value;
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "add-block/"+link+"/"+author+"/"+blockTitle,
        success : function() {
            console.log('block added');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });

}

function addNewVersion(element){
    element.style.visibility = "hidden";
    var link = window.location.search.substring(6);
    var bIndex = parseInt(document.getElementById("new-version-block-number").innerHTML)-1;
    var author = document.getElementById("new-version-author").value;
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "add-version/"+link+"/"+bIndex+"/"+author,
        success : function() {
            console.log('version added');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function saveVersionAuthor(element){
    var link = window.location.search.substring(6);
    var blockIndex = parseInt(element.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML)-1;
    var versionIndex = parseInt(element.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML)-1;
    var authorName = element.value;
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "save-version-author/"+link+"/"+blockIndex+"/"+versionIndex+"/"+authorName,
        success : function() {
            console.log('author changed');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function saveBlockTitle(element){
    var link = window.location.search.substring(6);
    var blockIndex = parseInt(element.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML)-1;
    var title = element.value;
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "save-block-title/"+link+"/"+blockIndex+"/"+title,
        success : function() {
            console.log('block title changed');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function saveDocTitle(element){
    var link = window.location.search.substring(6);
    var title = element.value;
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "save-doc-title/"+link+"/"+title,
        success : function() {
            console.log('doc title changed');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

/* CKEDITOR */

function applyEditors(){
    editors = [];
    var x = document.getElementsByClassName('editor');
    //console.log(x);
    var i;
    for (i = 0; i < x.length; i++){
        BalloonEditor
            .create( x[i], {
            toolbar: {
                items: [
                    'fontFamily',
                    'fontSize',
                    'fontColor',
                    'fontBackgroundColor',
                    'bold',
                    'italic',
                    'underline',
                    'bulletedList',
                    'numberedList',
                    'insertTable',
                    'undo',
                    'redo',
                    'alignment',
                    'removeFormat'
                ]
            },
            language: 'en',
            image: {
                toolbar: [
                    'imageTextAlternative',
                    'imageStyle:full',
                    'imageStyle:side'
                ]
            },
            table: {
                contentToolbar: [
                    'tableColumn',
                    'tableRow',
                    'mergeTableCells'
                ]
            },
                licenseKey: ''
            } )
            .then( editor => {
                editors.push(editor);
                let v = editors.length-1;
                editor.setData(doc.blocks[v].versions[0].content);
                editor.model.document.on( 'change:data', () => {
                    setModelVersionContent(v, editor.getData());
                    saveDoc();
                } );
                editor.editing.view.document.on( 'change:isFocused', (a, b, c) => {
                    focus(v, c);
                } );
            } )
            .catch( error => {
                console.error( 'Oops, something went wrong!' );
                console.error( 'Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:' );
                console.warn( 'Build id: kipsbl6tnqa0-dwsdpsjqxezr' );
                console.error( error );
            } );
    }

}

function applyEditorsWithFocused(fi, ed){
    editors = [];
    var x = document.getElementsByClassName('editor');
    var i;
    for (i = 0; i < x.length; i++){

        if (i != fi){

            BalloonEditor
            .create( x[i], {
            toolbar: {
                items: [
                    'fontFamily',
                    'fontSize',
                    'fontColor',
                    'fontBackgroundColor',
                    'bold',
                    'italic',
                    'underline',
                    'bulletedList',
                    'numberedList',
                    'insertTable',
                    'undo',
                    'redo',
                    'alignment',
                    'removeFormat'
                ]
            },
            language: 'en',
            image: {
                toolbar: [
                    'imageTextAlternative',
                    'imageStyle:full',
                    'imageStyle:side'
                ]
            },
            table: {
                contentToolbar: [
                    'tableColumn',
                    'tableRow',
                    'mergeTableCells'
                ]
            },
                licenseKey: ''
            } )
            .then( editor => {

                if (editors.length == fi){
                    editors.push(ed);
                }

                editors.push(editor);
                let v = editors.length-1;
                editor.setData(doc.blocks[v].versions[0].content);

                editor.model.document.on( 'change:data', () => {
                    setModelVersionContent(v, editor.getData());
                    saveDoc();
                } );

                editor.editing.view.document.on( 'change:isFocused', (a, b, c) => {
                    focus(v, c);
                } );

            } )
            .catch( error => {
                console.error( 'Oops, something went wrong!' );
                console.error( 'Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:' );
                console.warn( 'Build id: kipsbl6tnqa0-dwsdpsjqxezr' );
                console.error( error );
            } );

        }

    }
}