
window.onload = function() {
    autosizeTextAreas();
};

function autosizeTextAreas(){
    var x = document.getElementsByClassName("textarea-editor")
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



/****************************************************************/
/***************************** AJAX *****************************/
/****************************************************************/

/*                  changes in a document model                 */

function saveDocTitle(element){
    title = {
        content : element.value
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
        blockNumber : number
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
        versionNumber : vNumber
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
        versionNumber : vNumber
    }
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "save-version",
        data : JSON.stringify(version),
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

function addNewBlock(element){
    element.style.visibility = "hidden";
    block = {
        blockTitle : document.getElementById("new-block-title").value,
        author : document.getElementById("new-block-author").value,
    }
    $.ajax({
        url: 'new-block',
        type: 'POST',
        contentType : "application/json",
        data : JSON.stringify(block),
        success: function (data) {
            document.getElementById("content").outerHTML = data;
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
        blockNumber : bNum
    }
    loc = {
        blockNumber : bNum,
        versionNumber : vNum,
        right : "true"
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
                    var text = element.getElementsByClassName("textarea-editor")[0];
                    text.value = data.content;
                    text.style.height = "auto";
                    text.style.height = text.scrollHeight+'px';
                    element.getElementsByClassName("block-version")[0].innerHTML = vNum+1;
                    autosizeTextAreas();
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
            blockNumber : currentBlockNumber,
            versionNumber : currentVersionNumber,
            right : right
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
                var text = element.parentElement.parentElement.parentElement
                    .getElementsByClassName("textarea-editor")[0];
                text.value = data.content;
                text.style.height = "auto";
                text.style.height = text.scrollHeight+'px';
                if (right == "true"){
                    element.parentElement.parentElement.parentElement
                        .getElementsByClassName("block-version")[0].innerHTML = currentVersionNumber+1;
                } else {
                    element.parentElement.parentElement.parentElement
                        .getElementsByClassName("block-version")[0].innerHTML = currentVersionNumber-1;
                }
                autosizeTextAreas();
            },
            error : function(e) {
                console.log("ERROR: ", e);
            }
        })
    }
}


