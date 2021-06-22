
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



