window.onload = function() {
    setDownloadDocWrapper();
    setDocumentWrapper();
    loadLinksFromCookies();
    addLinkToCookies();
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

/****************************************************************/
/***************************** AJAX *****************************/
/****************************************************************/

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
}

