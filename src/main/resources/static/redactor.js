var focused = null;
var editors = [];

window.onload = function() {
    setDownloadDocWrapper();
    setDocumentWrapper();
    autosizeTextAreas();
    loadLinksFromCookies();
    addLinkToCookies();
    //updateVersions();
    updateVersionsCKEditor();
};

function setDownloadDocWrapper(){
    var link = window.location.search.toString().substring(6);
    document.getElementById("download-document-wrapper").innerHTML = '<button style="background:#82b2ff; color:white" class="menu-list-button menu-list-item" onclick="downloadDocument()">Convert to PDF</button>'
}

function setDocumentWrapper(){
    var link = window.location.search.toString().substring(6);
    document.getElementById("delete-document-wrapper").innerHTML = '<form action="delete-document/'+link+'" method="get" style="display:flex;flex-direction: column;align-items: stretch;"><button style="background:#FF7777; color:white" class="menu-list-button menu-list-item">Delete document</button></form>';
}

function getAllCookies(){
    var x = [];
    for ( var i = 0; i < localStorage.length; i++ ) {
      if (localStorage.getItem("link"+i) != null){
          x.push(localStorage.getItem("link"+i));
      }
    }
    return x;
}

function loadLinksFromCookies(){
    var x = getAllCookies();
    var name;
    if (x.length > 0){
        html = '';
        for (var i = 0; i < x.length; i++){
            name = getNameByLink(x[i].substring(x[i].indexOf('=')+1));
            if (name != ''){
                html += '<a class="menu-list-link menu-list-item" href="'+ x[i] +'">'+name+'</a>';
            }
        }
        document.getElementById('menu-recent').innerHTML = html;
    }
}

function addLinkToCookies(){
    var x = localStorage.length;
    var link = window.location.toString();
    if (!getAllCookies().includes(link)){
        localStorage.setItem("link"+x, link);
    }
}

function autosizeTextAreas(){

    var x = document.getElementsByClassName("textarea-editor");
    for (const item of x){
        autosize(item);
    }

}

function showNewBlock(){
    document.getElementById('new-block').style.visibility = "visible";
}

function hideNewBlock(){
    document.getElementById('new-block').style.visibility = "hidden";
}

function showNewVersion(element){
    var blockNumber = element.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    var blockTitle = element.parentElement.parentElement.getElementsByClassName("block-name")[0].value;
    document.getElementById('new-version-block-number').innerHTML = blockNumber;
    document.getElementById('new-version-block-title').innerHTML = blockTitle;
    document.getElementById('new-version').style.visibility = "visible";
}

function hideNewVersion(){
    document.getElementById('new-version').style.visibility = "hidden";
}

function share(){
    document.getElementById('share-input-wrapper').style.visibility = "visible";
    document.getElementById('share-input').value = window.location;
}

function copyToClipboard(){
    navigator.clipboard.writeText(document.getElementById('share-input').value);
}

function closeShare(){
    document.getElementById('share-input-wrapper').style.visibility = "hidden";
}

function openContextActions(element){
    element.parentElement.parentElement.getElementsByClassName('context-actions')[0].style.visibility = "visible";
}

function closeEditorContextMenu(element){
    element.parentElement.parentElement.getElementsByClassName('context-actions')[0].style.visibility = "hidden";
}

function openMenu(){
    $('.menu').toggleClass('menu-active');
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function callSleep(ms){
  await sleep(ms);
}

/****************************************************************/
/***************************** AJAX *****************************/
/****************************************************************/

function saveDocTitle(element){
    title = {
        content : element.value,
        link : window.location.search
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-doc-title",
        data : JSON.stringify(title),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data.content);
            element.value = data.content;
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function saveBlockTitle(element){
    var number = element.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    title = {
        content : element.value,
        blockNumber : number,
        link : window.location.search
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-block-title",
        data : JSON.stringify(title),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data);
            element.value = data.content;
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function saveVersionAuthor(element){
    var bNumber = element.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    var vNumber = element.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML;
    author = {
        content : element.value,
        blockNumber : bNumber,
        versionNumber : vNumber,
        link : window.location.search
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-version-author",
        data : JSON.stringify(author),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data);
            element.value = data.content;
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function saveVersion(element){
    var bNumber = element.parentElement.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    var vNumber = element.parentElement.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML;
    version = {
        content : element.value,
        blockNumber : bNumber,
        versionNumber : vNumber,
        link : window.location.search
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-version",
        data : JSON.stringify(version),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data);
            autosizeTextAreas();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });

}

function getContent(editor, i){
    var block = document.getElementsByClassName('block')[i];
    console.log('i: ', i, ', block: ', block);
    content = {
        content : "",
        blockNumber : block.getElementsByClassName("block-number")[0].innerHTML,
        versionNumber : block.getElementsByClassName("block-version")[0].innerHTML,
        link : window.location.search
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "get-version",
        data : JSON.stringify(content),
        success : function(data) {
            console.log("SUCCESS: ", data);
            editor.setData(data.content);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function updateVersions(){

    var blocks = document.getElementsByClassName("block");
    var _link = window.location.search;

    var _blocks = [];

    var content = {
        link : _link
    }

    for (var i = 0; i < blocks.length; i++){

        var data = {
            blockNumber : blocks[i].getElementsByClassName("block-number")[0].innerHTML,
            versionNumber : blocks[i].getElementsByClassName("block-version")[0].innerHTML,
            content : blocks[i].getElementsByClassName("textarea-editor")[0].value
        }

        _blocks.push(data);

    }

    content.blocks = _blocks;

    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "update-version",
        data : JSON.stringify(content),
        success : function(data) {
            console.log('data aquired: ', data);
            for (var i = 0; i < data.blocks.length; i++){
                /* not safe, fix later */
                document.getElementsByClassName("textarea-editor")[i].value = data.blocks[i].content;
            }
            autosizeTextAreas();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        complete: function () {
            updateVersions();
        }
    });

}

function addNewBlock(element){
    element.style.visibility = "hidden";
    block = {
        blockTitle : document.getElementById("new-block-title").value,
        author : document.getElementById("new-block-author").value,
        link : window.location.search
    }
    $.ajax({
        url: 'new-block',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(block),
        success: function (data) {
            document.getElementById("content").outerHTML = data;
            // **************************************************
            applyEditors();
            autosizeTextAreas();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    })
}

function addNewVersion(element){
    element.style.visibility = "hidden";
    var bNum = parseInt(document.getElementById("new-version-block-number").innerHTML);
    var vNum = parseInt(document.getElementsByClassName("block")[bNum-1]
        .getElementsByClassName("block-max-version")[0]
        .innerHTML);
    version = {
        author : document.getElementById("new-version-author").value,
        blockNumber : bNum,
        link : window.location.search
    }
    loc = {
        blockNumber : bNum,
        versionNumber : vNum,
        right : "true",
        link : window.location.search
    }
    $.ajax({
        url: 'new-version',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(version),
        success: function (data) {
            document.getElementById("content").outerHTML = data;
            /* nested ajax */
            $.ajax({
                url: 'change-version',
                type: 'POST',
                contentType : "application/json",
                data : JSON.stringify(loc),
                dataType : 'json',
                success: function (data) {
                    console.log("SUCCESS: ", data);
                    element = document.getElementsByClassName("block")[bNum-1];
                    element.getElementsByClassName("block-author")[0].value = data.author;

//                    var text = element.getElementsByClassName("textarea-editor")[0];
//                    text.value = data.content;
//                    text.style.height = "auto";
//                    text.style.height = text.scrollHeight+'px';

                    editors[bNum-1].setData(data.content);

                    element.getElementsByClassName("block-version")[0].innerHTML = vNum+1;
                    element
                        .getElementsByClassName("editor-star-version-svg")[0]
                        .style.fill = '#efefef'
                    autosizeTextAreas();
                    // **************************************************
                    applyEditors();
                },
                error : function(e) {
                    console.log("ERROR: ", e);
                }
            })
            /* end of nested ajax */
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    })
}

function changeVersion(element, right){
    var currentBlockNumber = parseInt(element.parentElement.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML);
    var currentVersionNumber = parseInt(element.parentElement.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML);
    var maxVersion = parseInt(element.parentElement.parentElement.parentElement.getElementsByClassName("block-max-version")[0].innerHTML);
    if ( (currentVersionNumber > 1 && right == false) || (currentVersionNumber < maxVersion && right == true) ){
        if (right) { right = "true" } else { right = "false" }
        loc = {
            blockNumber : currentBlockNumber.toString(),
            versionNumber : currentVersionNumber.toString(),
            right : right,
            link : window.location.search
        }
        console.log(loc);
        $.ajax({
            url: 'change-version',
            type: 'POST',
            contentType : "application/json",
            data : JSON.stringify(loc),
            dataType : 'json',
            success: function (data) {
                console.log("SUCCESS: ", data);

                element.parentElement.parentElement.getElementsByClassName("block-author")[0].value = data.author;

//                var text = element.parentElement.parentElement.parentElement
//                    .getElementsByClassName("textarea-editor")[0];
//                text.value = data.content;
//                text.style.height = "auto";
//                text.style.height = text.scrollHeight+'px';

                editors[currentBlockNumber-1].setData(data.content);

                if (right == "true"){
                    element.parentElement.parentElement.parentElement
                        .getElementsByClassName("block-version")[0].innerHTML = currentVersionNumber+1;
                } else {
                    element.parentElement.parentElement.parentElement
                        .getElementsByClassName("block-version")[0].innerHTML = currentVersionNumber-1;
                }

                if (data.starred){
                    element
                        .parentElement
                        .parentElement
                        .parentElement
                        .getElementsByClassName("editor-star-version-svg")[0]
                        .style.fill = '#fff491'
                } else {
                    element
                        .parentElement
                        .parentElement
                        .parentElement
                        .getElementsByClassName("editor-star-version-svg")[0]
                        .style.fill = '#efefef'
                }



                autosizeTextAreas();
                // **************************************************
                //applyEditors();
            },
            error : function(e) {
                console.log("ERROR: ", e);
            }
        })
    }
}

function deleteBlock(element){
    element.parentElement.style.visibility = "hidden";
    var number = element.parentElement.parentElement.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    block = {
        blockNumber : number,
        link : window.location.search
    }
    $.ajax({
        url: 'delete-block',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(block),
        success: function (data) {
            document.getElementById("content").outerHTML = data;
            autosizeTextAreas();
            // **************************************************
            applyEditors();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    })
}

function deleteVersion(element){
    element.parentElement.style.visibility = "hidden";
    var bNumber = element.parentElement.parentElement.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    var vNumber = element.parentElement.parentElement.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML;
    version = {
        blockNumber : bNumber,
        versionNumber : vNumber,
        link : window.location.search
    }
    $.ajax({
        url: 'delete-version',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(version),
        success: function (data) {
            document.getElementById("content").outerHTML = data;
            autosizeTextAreas();
            // **************************************************
            applyEditors();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    })
}

function starVersion(element){
    element.getElementsByClassName("editor-star-version-svg")[0].style.fill = '#fff491';
    var bNumber = element.parentElement.parentElement.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    var vNumber = element.parentElement.parentElement.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML;
    version = {
        blockNumber : bNumber,
        versionNumber : vNumber,
        link : window.location.search
    }
    $.ajax({
        url: 'star-version',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(version),
        success: function (data) {
            console.log('SUCCESS');
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    })
}

function getNameByLink(src){
    var out = null;
    link = {
        source : src
    }
    $.ajax({
        async: false,
        url: 'get-doc-title',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(link),
        success: function (data) {
            out = data;
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    })
    return out;
}

function downloadDocument(){
    openMenu();

    window.location.href = 'print/'+window.location.search.toString().substring(6);

//    var request = 'download-document/'+window.location.search.toString().substring(6);
//    $.ajax({
//        url: request,
//        type: 'GET',
//        success: function (data) {
//            var sampleArr = base64ToArrayBuffer(data.bytes);
//            saveByteArray(data.title, sampleArr);
//        },
//        error : function(e) {
//            console.log("ERROR: ", e);
//        }
//    })

//    var request = 'download-document-string/'+window.location.search.toString().substring(6);
//    $.ajax({
//        url: request,
//        type: 'GET',
//        success: function (data) {
//            console.log(data);
//        },
//        error : function(e) {
//            console.log("ERROR: ", e);
//        }
//    })



//    var request = 'print/'+window.location.search.toString().substring(6);
//    $.ajax({
//        url: request,
//        type: 'GET',
//        error : function(e) {
//            console.log("ERROR: ", e);
//        }
//    })

}



/************** PDF **************/
function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
       var ascii = binaryString.charCodeAt(i);
       bytes[i] = ascii;
    }
    return bytes;
 }

function saveByteArray(reportName, byte) {
    var blob = new Blob([byte], {type: "application/pdf"});
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    var fileName = reportName;
    link.download = fileName;
    link.click();
};


/************** CKEDITOR **************/
function saveData(data){
    // ?
}

function saveVersionCKEditor(element){
    var bNumber = element.parentElement.parentElement.parentElement.getElementsByClassName("block-number")[0].innerHTML;
    var vNumber = element.parentElement.parentElement.parentElement.getElementsByClassName("block-version")[0].innerHTML;
    var value = editors[parseInt(bNumber)-1].getData();
    version = {
        content : value,
        blockNumber : bNumber,
        versionNumber : vNumber,
        link : window.location.search
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-version",
        data : JSON.stringify(version),
        dataType : 'json',
        success : function(data) {
            console.log("SUCCESS: ", data);
            autosizeTextAreas();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });

}

function updateVersionsCKEditor(){

    var blocks = document.getElementsByClassName("block");
    var _link = window.location.search;

    var _blocks = [];

    var content = {
        link : _link
    }

    for (var i = 0; i < blocks.length; i++){

        var data = {
            blockNumber : blocks[i].getElementsByClassName("block-number")[0].innerHTML,
            versionNumber : blocks[i].getElementsByClassName("block-version")[0].innerHTML,
            content : editors[i].getData()
        }

        _blocks.push(data);

    }

    content.blocks = _blocks;

    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "update-version",
        data : JSON.stringify(content),
        success : function(data) {

            console.log('data aquired: ', data);
            for (var i = 0; i < data.blocks.length; i++){

                var text = data.blocks[i].content;
                var pos;
                var range;

                if (!editors[i].editing.view.document.isFocused){
                    editors[i].setData(text);
                }

            }
            autosizeTextAreas();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        complete: function () {
            updateVersionsCKEditor();
        }
    });

}

function getContentCKEditor(){

    var blocks = document.getElementsByClassName("block");
    var _link = window.location.search;

    var _blocks = [];

    var content = {
        link : _link
    }

    for (var i = 0; i < blocks.length; i++){

        var data = {
            blockNumber : blocks[i].getElementsByClassName("block-number")[0].innerHTML,
            versionNumber : blocks[i].getElementsByClassName("block-version")[0].innerHTML,
            content : editors[i].getData()
        }

        _blocks.push(data);

    }

    content.blocks = _blocks;

    console.log(content);

    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "get-versions",
        data : JSON.stringify(content),
        success : function(data) {

            for (var i = 0; i < data.blocks.length; i++){

                var text = data.blocks[i].content;
                var pos;
                var range;

                if (!editors[i].editing.view.document.isFocused){
                    editors[i].setData(text);
                }

            }
            autosizeTextAreas();
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        complete: function () {
            updateVersionsCKEditor();
        }
    });

}

/* editors */

function applyEditors(){
    editors = [];
    var x = document.getElementsByClassName('editor');
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
            licenseKey: '',
        } )
        .then( editor => {
            editors.push(editor);
            var v = editors.length-1;
            console.log('editor ', v, ' has been initialized, id: ', editor.id);
            getContent(editor, v);
        } )
        .catch( error => {
            console.error( 'Oops, something went wrong!' );
            console.error( 'Please, report the following error on https://github.com/ckeditor/ckeditor5/issues with the build id and the error stack trace:' );
            console.warn( 'Build id: kipsbl6tnqa0-dwsdpsjqxezr' );
            console.error( error );
        } );
    }

}

