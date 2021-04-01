/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author: winiga
 */


var savedEditor = false;
var editor = new Quill('#editor-container', {
    modules: {
        toolbar: '#quill-toolbar',
        syntax: true
    },
    placeholder: 'Free Write...',
    theme: 'snow'
});

editor.on('editor-change', function (eventName, ...args) {

    savedEditor = false;
});

editor.keyboard.addBinding({key: 's', shortKey: true}, function (range, context) {

    console.log(editor.getText(0, 300));

    if (confirm("do you want to save changes")) {

        const getToken = new XMLHttpRequest();
        let param = '';
        if (document.getElementById("paramId") && document.getElementById("paramId").value)
            param = '/' + document.getElementById("paramId").value;
        let ctx  = document.getElementById('ctx').value;
        getToken.open('GET', ctx + '/JWTProvider' + param, true);
        getToken.overrideMimeType("text/plain");

        getToken.onload = () => {

            if (getToken.status === 200) {

                const xhr = new XMLHttpRequest();
                const pd = {};
                xhr.open('POST', ctx + '/api/post', true);
                xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                xhr.setRequestHeader('Authorization', 'Bearer ' + getToken.responseText);
                xhr.onload = () => {
                    if (xhr.status === 200) {
                        //console.log(xhr.responseText);
                        savedEditor = true;
                        PF('editorInfo').renderMessage({"summary": "Enrégistrement effectué",
                            "severity": "info",
                            "life": "550"});

                    } else {
                        PF('editorInfo').renderMessage({"summary": "Opération échoué",
                            "detail": "Une erreur est survenue lors de la dernière opération.",
                            "severity": "error"});
                    }
                };
                pd.details = JSON.stringify(editor.getContents());
                pd.title = document.getElementById("post-title").value;
                pd.resume = editor.getText(0, 280);
                xhr.send(JSON.stringify(pd));
            }
        };

        getToken.send();

    }

    return true;
});


/**
 * Step1. select local image
 *
 */
function selectLocalImage() {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.click();

    // Listen upload local image and save to server
    input.onchange = () => {
        const file = input.files[0];

        // file type is only image.
        if (/^image\//.test(file.type)) {
            saveToServer(file);
        } else {
            console.warn('You could only upload images.');
        }
    };
}

/**
 * Step2. save to server
 *
 * @param {File} file
 */
function saveToServer(file) {
    let preset = 'blogjava';
    var IMGUR_API_URL = 'https://api.cloudinary.com/v1_1/gwiniga/upload';

    const fd = new FormData();

    const xhr = new XMLHttpRequest();
    xhr.open('POST', IMGUR_API_URL, true);
    //xhr.setRequestHeader('Authorization', 'Client-ID ' + IMGUR_CLIENT_ID);
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

    xhr.onload = () => {
        if (xhr.status === 200) {
            // this is callback data: url
            const url = JSON.parse(xhr.responseText).url;
            //console.log(url);
            insertToEditor(url);
        }
    };

    fd.append('upload_preset', preset);
    fd.append('file', file);
    xhr.send(fd);
}

/**
 * Step3. insert image url to rich editor.
 *
 * @param {string} url
 */
function insertToEditor(url) {
    // push image url to rich editor.
    const range = editor.getSelection();
    editor.insertEmbed(range.index, 'image', url);
}

// quill editor add image handler
editor.getModule('toolbar').addHandler('image', () => {
    selectLocalImage();
});

//editor.keyboard.addBinding({ key: 's', shortKey: true }, function(range, context) {
//                   
//    if(confirm("do you want to save changes")) {
//
//        const getToken = new XMLHttpRequest();
//        
//        getToken.open('GET', '/jeeblog-1.0-SNAPSHOT/JWTProvider', true);
//        getToken.overrideMimeType("text/plain");
//
//        getToken.onload = () => {
//
//            if (getToken.status === 200) {
//                const xhr = new XMLHttpRequest();
//                const pd = {};
//                let param = '';
//                if(document.getElementById("paramId") && document.getElementById("paramId").value)
//                    param = '/' + document.getElementById("paramId").value;
//                
//                xhr.open('POST', '/jeeblog-1.0-SNAPSHOT/api/post' + param , true);
//                xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
//                xhr.setRequestHeader('Authorization', 'Bearer ' + getToken.responseText);
//                xhr.onload = () => {
//                    if (xhr.status === 200) {
//                      console.log(xhr.responseText);
//                    }
//                };
//                pd.details = JSON.stringify(editor.getContents());
//                xhr.send(JSON.stringify(pd));
//            }
//        };
//
//        getToken.send();
//
//    }
//
//   return true;
//});


/**
 * disable default action on browser
 * 
 * @param {Object} e 
 */

document.addEventListener("keydown", function (e) {

    if (e.keyCode === 83 && (navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)) {
        e.preventDefault();
    }
}, false);

window.addEventListener("beforeunload", function (e) {

    if (!savedEditor) {
        var confirmationMessage = 'Des modifications non sauvegardées ont récemment été effectuées,'
                + 'En quittant ces modifications seront définitivement perdues.';

        (e || window.event).returnValue = confirmationMessage; //Gecko + IE
        return confirmationMessage; //Gecko + Webkit, Safari, Chrome etc.
    }
});

