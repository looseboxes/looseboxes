if (typeof String.prototype.endsWith !== 'function') {
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
}

var looseboxes = {
    $:
    function(x) {return looseboxes.getElementById(x);},    
    getElementById:
    function (x){if(document.getElementById) return document.getElementById(x);else if(document.all) return document.all(x); else return null;},
    setNodeValue:
    function (k, v) {
        if(!k) return; //window.alert("#$set. innerHTML:"+k.innerHTML+", nodeValue:"+k.nodeValue+", value:"+k.value); 
        if(k.nodeValue !== null && k.nodeValue !== 'undefined') k.nodeValue = v;
        else if(k.value !== null && k.value !== 'undefined') k.value = v; 
        else if(k.innerHTML !== null && k.innerHTML !== 'undefined') k.innerHTML = v;
    },
    getNodeValue:
    function (k) {if(!k) return null;if(k.innerHTML) return k.innerHTML;else if(k.nodeValue) return k.nodeValue;else if(k.value) return k.value; else return null;},
    addLoadEvent:
    function (func) {
//window.alert("#addLoadEvent. Function: "+func);    
        var oldonload = window.onload;
//window.alert("#addLoadEvent. OLD Function: "+oldonload);    
        if (typeof window.onload != 'function') {
            window.onload = func;
        }else{
            window.onload = function() {
                if (oldonload) {
                    oldonload();
                }
                func();
            };
        }
    },
    //Nested Side Bar Menu (Mar 20th, 09). By Dynamic Drive: http://www.dynamicdrive.com/style/
    initsidebarmenu:
    function (mMenuId) {
        var mMenu = looseboxes.$(mMenuId);
        if(!mMenu) return;
        var ultags=mMenu.getElementsByTagName("ul");
        for (var i=0; i<ultags.length; i++){
            if (ultags[i].parentNode.parentNode.id===mMenuId) //if this is a first level submenu
                ultags[i].style.left=ultags[i].parentNode.offsetWidth+"px"; //dynamically position first level submenus to be width of main menu item
            else //else if this is a sub level submenu (ul)
                ultags[i].style.left=ultags[i-1].getElementsByTagName("a")[0].offsetWidth+"px"; //position menu to the right of menu item that activated it
            ultags[i].parentNode.onmouseover=function(){
                document.getElementsByTagName("ul")[0].style.display="block";
            };
            ultags[i].parentNode.onmouseout=function(){
                document.getElementsByTagName("ul")[0].style.display="none";
            };
        }
        for (var j=(ultags.length-1); j>-1; j--){ //loop through all sub menus again, and use "display:none" to hide menus (to prevent possible page scrollbars
            ultags[j].style.visibility="visible";
            ultags[j].style.display="none";
        }
    },
    addToFav:
    function (title,url){
        if (window.sidebar) {// firefox
            window.sidebar.addPanel(title, url, "");
        }else if(window.opera && window.print){ // opera
            var elem = document.createElement('a');
            elem.setAttribute('href',url);
            elem.setAttribute('title',title);
            elem.setAttribute('rel','sidebar');
            elem.click();
        }else if(document.all) {// ie
            window.external.AddFavorite(url, title);
        }
    },
    loadImage:
    function (url, width, height) {
//window.alert("Loading: "+url);
        looseboxes.loadImageX(url, width, height, null);
    },  
    /** This is it */
    loadImageX:
    function (url, width, height, onloadCallback) {
//window.alert("Loading: "+url);
        var img;
        if(width === null || height === null) {
            img = new Image;
        }else{
            img = new Image(width, height);
        }
        if(onloadCallback) {
            img.onload = function() {
                onloadCallback();    
            };
        }
        img.src = url;
    },    
    loadAndDisplayImage:
    function (width, height, imageNodeId, imageSrc, imageLinkId, imageLink) {
//window.alert("Displaying: "+imageSrc);
        looseboxes.loadImageX(imageSrc, width, height, function(){
            looseboxes.displayImage(imageNodeId, imageSrc, imageLinkId, imageLink);
        });
    },  
    displayImage:
    function (imageNodeId, imageSrc, imageLinkId, imageLink) {
//window.alert("Displaying: "+imageSrc);
        looseboxes.$(imageNodeId).src = imageSrc;
        if(imageLinkId !== null && imageLink !== null) {
            looseboxes.$(imageLinkId).href = imageLink;
        }
    },  
    setVisibility:
    function(elemId, visibilityStyle) {
        if(elemId) {looseboxes.$(elemId).style.visibility = visibilityStyle;}
    },
    setDisplay:
    function(elemId, displayStyle) {
        if(elemId) {looseboxes.$(elemId).style.display = displayStyle;}
    },    
    shareOnFacebook:
    function (link, title) {
        looseboxes.open('http://www.facebook.com/sharer.php?u='+encodeURIComponent(link)+'&t='+encodeURIComponent(title),'Share');
        return false;
    },
    followOnTwitter:
    function (twitterLink, title) {
        looseboxes.open('http://twitter.com/'+twitterLink, title);
        return false;
    },
    tweetItem:
    function (status, title) {
        looseboxes.open('http://twitter.com/home?status='+encodeURIComponent(status), title);
        return false;
    },
    open:
    function (strUrl, strWindowName) {
        window.open(strUrl, strWindowName,'toolbar=0,status=0,width=626,height=436');    
    },
    loadURL:
    function (idToSubmit, url, title, errorURL) {
        myAjax.send("GET", idToSubmit, url, function(ajaxResponse){
            if(ajaxResponse) {
                var output = ajaxResponse.responseText;  
                //@related_41
                if(output === 'error' && errorURL !== null) {
                    // Spaces in title caused errors in IE 9
                    looseboxes.open(errorURL, "Error_Encountered_Loading_URL");
                }else{
                    looseboxes.open(output, title);      
                }  
            }
        });
    },
    // Returns a random integer between min and max
    // Using Math.round() will give you a non-uniform distribution!
    getRandomInt:  
    function (min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }        
};

var myDropDownMenu = {
    closetimer: 0,
    previousId: null,
    openExcl: function(elemId,keepRefererence) {
        myDropDownMenu.cancelclosetime();
        myDropDownMenu.closePrevious();
        myDropDownMenu.open(elemId, keepRefererence);
    },
    open: function(elemId,keepReference) {
        if(keepReference) {
            myDropDownMenu.previousId = elemId;
        }
        looseboxes.setVisibility(elemId, 'visible');
    }, 
    openTill: function(elemId,timeout) {
        myDropDownMenu.open(elemId, true);
        myDropDownMenu.closeAfter(elemId, timeout);
    }, 
    closeAfter: function(elemId, timeout) {
//window.alert("Will close "+myDropDownMenu.previousId+" after "+timeout+" millis.");
        myDropDownMenu.previousId = elemId;
        window.setTimeout('myDropDownMenu.closePrevious()', timeout);
    }, 
    closePrevious: function() {
//window.alert("Closing previous: "+myDropDownMenu.previousId);
        if(myDropDownMenu.previousId !== null) {
            myDropDownMenu.close(myDropDownMenu.previousId);
        }
    },
    close: function(elemId) {
        looseboxes.setVisibility(elemId, 'hidden');
    },
    setclosetime: function (timeout) {
        myDropDownMenu.closetimer = window.setTimeout('myDropDownMenu.closePrevious()', timeout);
    },
    cancelclosetime: function () {if(myDropDownMenu.closetimer) {window.clearTimeout(myDropDownMenu.closetimer);myDropDownMenu.closetimer = null;}},
    toggle: function(id, id2, collapseMode) {
//window.alert("myDropDownMenu#toggle. CollapseMode: "+collapseMode+", id1: "+id+", id2: "+id2);    
        var ddmenu_item = looseboxes.$(id);
        var ddmenu_ctrl = looseboxes.$(id2);
//window.alert("myDropDownMenu#toggle. item: "+ddmenu_item+", control: "+ddmenu_ctrl);    
//window.alert("myDropDownMenu#toggle. ControlNode.value: "+looseboxes.getNodeValue(ddmenu_ctrl));
        var open = collapseMode ? 
            (looseboxes.getNodeValue(ddmenu_ctrl) === '+' || ddmenu_item.style.display === 'none') :
            (looseboxes.getNodeValue(ddmenu_ctrl) === '+' || ddmenu_item.style.visibility === 'hidden');
//window.alert("myDropDownMenu#toggle. Display: "+ddmenu_item.style.display+", visibility: "+ddmenu_item.style.visibility+", open: "+open);    
        if(open) {
            myDropDownMenu.toggleopen(id, id2, collapseMode);
        }else{
            myDropDownMenu.toggleclose(id, id2, collapseMode);
        }
    },
    toggleopen: function(id, id2, collapseMode){
        var ddmenu_item = looseboxes.$(id);
        if(collapseMode) ddmenu_item.style.display = 'block';
        ddmenu_item.style.visibility = 'visible';
        var ddmenu_ctrl = looseboxes.$(id2);
//window.alert("myDropDownMenu#toggleopen. "+ddmenu_ctrl.innerHTML+", nodeValue: "+ddmenu_ctrl.nodeValue+", value: "+ddmenu_ctrl.value);    
        looseboxes.setNodeValue(ddmenu_ctrl, '-');
    },
    toggleclose: function(id, id2, collapseMode){
        var ddmenu_item = looseboxes.$(id);
        if(collapseMode) ddmenu_item.style.display = 'none';
        ddmenu_item.style.visibility = 'hidden';
        var ddmenu_ctrl = looseboxes.$(id2);
//window.alert("myDropDownMenu#toggleclose. Menu ctrl: "+ddmenu_ctrl);        
        looseboxes.setNodeValue(ddmenu_ctrl, '+');
    }
};
/////////////////////////// CSS Handling Scripts ///////////////////// 
var myCSSHandler = {
    setWarningMessageClass: function(mNode) {myCSSHandler.setClass(mNode, "warningMessage");},    
    setInformationMessageClass: function(mNode) {myCSSHandler.setClass(mNode, "informationMessage");},    
    addClass: function(mNode,mClass) {if(!mNode || !mClass) return;if(mNode.className) mNode.className += (" " + mClass); else mNode.className = mClass;},     
    setClass: function(mNode,mClass) {if(!mNode || !mClass) return;mNode.className = mClass;}     
};
//////////////////////////  AJAX FUNCTIONS ///////////////////////////
var myAjaxRequest = {
    newInstance: 
    function (){
        var mReq;  
        try{
            mReq = new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
        }catch (e){ // IE5 & IE6
            try{mReq = new ActiveXObject("Msxml2.XMLHTTP");}catch (e) {
                try{mReq = new ActiveXObject("Microsoft.XMLHTTP");}catch (e){return null;}
            }
        }
        return mReq;
    }
};
var myAjax = {
    request: function(){return myAjaxRequest.newInstance();},
    responseValue: null,
    get: 
    function(idToSubmit, idToUpdate, url) {
        myAjax.send("GET", idToSubmit, url, function(ajaxResponse){
            var mTarget = looseboxes.$(idToUpdate);
//window.alert("myAjax#get$callback. Target element:"+mTarget+", Response:"+ajaxResponse);
            if(mTarget && ajaxResponse) {
//window.alert("myAjax#get$callback. (mTarget && ajaxResonse)");
                looseboxes.setNodeValue(mTarget, ajaxResponse.responseText);
            }
        });
    },
    send: 
    function (method, idToSubmit, url, callback) {
//window.alert("myAjax#send..........................."+url);   
        if(idToSubmit) {
            var mForm = document.forms[idToSubmit];
//window.alert("myAjax#send. Form: "+mForm);        
            if(mForm) {
                var mFormElements = mForm.childNodes;
                var len = mFormElements.length;
                for(var i = 0; i < len; i++) {
                    if(!(mFormElements[i].name) || !(mFormElements[i].value)) continue;  
                    url += (mFormElements[i].name+"="+encodeURIComponent(mFormElements[i].value));   
                    if(i < (len - 1)) url += "&";
                }
            }else{ 
                var mElement = looseboxes.$(idToSubmit);
                var mElementValue = looseboxes.getNodeValue(idToSubmit);
//window.alert("myAjax#send. Element: "+mElement);        
                if(mElement && mElementValue) {url += (mElement.name+"="+encodeURIComponent(mElementValue));}
            }
        }
//window.alert("myAjax#send. URL: "+url);        
        var mReq = myAjax.request();
//window.alert("myAjax#send. Ajax Request: "+mReq);        
        if(mReq === null) return;
        mReq.onreadystatechange = function() {
//window.alert("myAjax#send. AjaxRequest.onreadystatechange readyState: "+mReq.readyState);
            if (mReq.readyState == 4) {
                if (mReq.status == 200) {
//window.alert("myAjax#send. AjaxRequest.status: "+mReq.status);
                    if(!callback) return;
//window.alert("myAjax#send. XHR: "+mReq);
                    callback(mReq);
                }
            }
        };
        mReq.open(method, url, true);
        mReq.send(null);
    }
};
////////////////////////// FORM HANDLING FUNCTIONS /////////////////////////////
var myFormHandler = {
    hasError: null,
    validate: function(){return !myFormHandler.hasError;},
    validateConsent: function(){if(myFormHandler.hasError) myFormHandler.disableSubmitBtn(); else myFormHandler.enableSubmitBtn();},
    /**@related_25 The submit button in our forms must have ID 'submitId' for this to work **/
    // I tried using document.getElementsByName but that didn't work
    enableSubmitBtn: function(){var btn = looseboxes.$("submitId");if(!btn) return;if(btn.disabled) btn.disabled = false;},
    disableSubmitBtn: function(){var btn = looseboxes.$("submitId");if(!btn) return;if(!btn.disabled) btn.disabled = true;},
    changeTuring:
    function(idToUpdate, attributeName, mURL, mWidth, mHeight) {
        myAjax.send("GET", null, mURL, function(){
            // We create a unique url to decieve the browser cache rules
            var uniqueUrl = mURL + "?t=" + new Date().getTime();
            Image_captcha = new Image(mWidth, mHeight);
            Image_captcha.src = uniqueUrl;
            var mElem = looseboxes.$(idToUpdate);
            mElem.setAttribute(attributeName, uniqueUrl);
        });
    },
    validateInput:
    function (idToSubmit, idToUpdate, url, disableSubmit) {
//window.alert("myFormHandler#validateInput");    
        if(disableSubmit) myFormHandler.disableSubmitBtn();
        myAjax.send("GET", idToSubmit, url, function(ajaxResponse){
            var mTarget = looseboxes.$(idToUpdate);
//window.alert("myFormHandler#validateInput$callback. Target element:"+mTarget+", Response:"+ajaxResponse.responseText);
            if(mTarget && ajaxResponse) {
                var text = ajaxResponse.responseText.toString().toLowerCase();
                if(text === "valid") {
//window.alert("myFormHandler#validateInput$callback. AjaxRespose == valid");
                    myCSSHandler.setInformationMessageClass(mTarget);myFormHandler.hasError = false;
                }else {
//window.alert("myFormHandler#validateInput$callback. AjaxRespose != valid");
                    myCSSHandler.setWarningMessageClass(mTarget);myFormHandler.hasError = true;  
                    // myFormHandler.disableSubmitBtn(); Already disabled above. Once an element is disabled we can't even access it again
                }  
                looseboxes.setNodeValue(mTarget, text);
            }
        });
    },
    matchValues:
    function(id1, id2, msgId1, msgId2) {
//window.alert("myFormHandler#matchValues");       
        var tgt1 = looseboxes.$(id1);var name1 = tgt1.getAttribute("name");var val1 = tgt1.value;
        var tgt2 = looseboxes.$(id2);var name2 = tgt2.getAttribute("name");var val2 = tgt2.value;
//window.alert("matchValues: ["+name1+":"+val1+"] and ["+name2+":"+val2+"]");    
        if(!tgt1 || !tgt2) return;
        var mOne = val1 === null || val1 === "";
        var mMsgElement = mOne ? looseboxes.$(msgId1) : looseboxes.$(msgId2);
//window.alert("myFormHandler#matchValues Message Element: "+mMsgElement.id+", style: "+mMsgElement.style);    
        myCSSHandler.setWarningMessageClass(mMsgElement);myFormHandler.hasError = true;myFormHandler.disableSubmitBtn();
        if (mOne) {
            looseboxes.setNodeValue(mMsgElement, name1+" is required");tgt1.value = "";tgt2.value = "";tgt1.focus();
        }else if (val2 === null || val2 === "") {
            looseboxes.setNodeValue(mMsgElement, name2+" is required");tgt2.value = "";tgt2.focus();
        }else if (val1 !== val2) {
//window.alert("myFormHandler#matchValues. Mismatch");        
            looseboxes.setNodeValue(mMsgElement, name2+" must match "+name1);tgt2.value = "";tgt2.focus();
        }else {
            myCSSHandler.setInformationMessageClass(mMsgElement);myFormHandler.hasError = false;looseboxes.setNodeValue(mMsgElement, "VALID");myFormHandler.enableSubmitBtn();
        }
    },
    clearField: function(idToClear) {var tgt = looseboxes.$(idToClear);if(tgt) {tgt.value = "";}},
    updateAttribute: 
    function(srcId, tgtId, attrName){
//window.alert("updateAttribute. source: "+srcId+", target: "+tgtId);    
        var mSrc = looseboxes.$(srcId);if(!mSrc) return;
        var mTgt = looseboxes.$(tgtId);if(!mTgt) return;
        if(mSrc.value) mTgt.setAttribute(attrName, mSrc.value);
        else if(mSrc.nodeValue) mTgt.setAttribute(attrName, mSrc.nodeValue);
    },

    updateCheckboxes:
    function (parentNodeId, mastercheckboxId) {
        var mastercheckbox = looseboxes.$(mastercheckboxId);
        var newState = !mastercheckbox.checked;
        var inputs;
        if(parentNodeId === null) {
            inputs = document.getElementsByTagName("input");
        }else{
            inputs = looseboxes.$(parentNodeId).getElementsByTagName("input");
        }
        for(var i = 0; i < inputs.length; i++) {
            if(inputs[i] !== mastercheckbox && inputs[i].type === "checkbox") {
                inputs[i].checked = newState; 
            }  
        }  
    }
};
////////////////////////////// GOOGLE MAP SCRIPTS //////////////////////////////
var googlemap = {
    msgNode: null,
    addressNode: null,
    canvas: null,
    latNode: null,
    lngNode: null,
    
    init: 
    function (msgNodeId, addressNodeId, canvasId, latId, lngId) {
    //window.alert("googlemap#init. IDs. Messagsse: "+msgNodeId+
    //    ", address: "+addressNodeId+", canvas: "+canvasId+
    //    ", latitude: "+latId+", longitude: "+lngId);    
        googlemap.msgNode = looseboxes.$(msgNodeId);
        googlemap.addressNode = looseboxes.$(addressNodeId);
        googlemap.canvas = looseboxes.$(canvasId);
        googlemap.latNode = looseboxes.$(latId);
        googlemap.lngNode = looseboxes.$(lngId);
    //window.alert("googlemap#init. NODEs. Messagsse: "+googlemap.msgNode+
    //    ", address: "+googlemap.addressNode+", canvas: "+googlemap.canvas+
    //    ", latitude: "+googlemap.latNode+", longitude: "+googlemap.lngNode);    
    },
    
    loadscript: 
    function () {
    //window.alert("googlemap#loadscript.");    
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://maps.googleapis.com/maps/api/js?v=3&sensor=false&callback=googlemap.load";
        document.body.appendChild(script);
    },
    
    load: 
    function () {
        var mLatLng;
        if(googlemap.latNode && googlemap.lngNode) {
            if(googlemap.latNode.value && googlemap.lngNode.value) {
                mLatLng = new google.maps.LatLng(googlemap.latNode.value, googlemap.lngNode.value);
            }
        }

        if(mLatLng) {
            
            googlemap.loadMap(16, mLatLng, mLatLng);
            
        }else{
            
            var address = googlemap.addressNode.innerHTML;
//window.alert("googlemap#load. Address: "+address);    
            if(address) {
                googlemap.loadAddress(address);
            }else{
                // Load default address - somewhere in abuja
                googlemap.loadDefault();
            }
        }  
//window.alert("googlemap#init. NODEs. Messagsse: "+googlemap.msgNode+
//    ", address: "+googlemap.addressNode+", canvas: "+googlemap.canvas+
//    ", latitude: "+googlemap.latNode+", longitude: "+googlemap.lngNode);    
    },
    
    loadDefault:
    function() {
        // Load default address - somewhere in abuja
        googlemap.loadMap(1, new google.maps.LatLng(9.066478, 7.483286), "Address could not be located");
    },    
    
    loadMap:
    function(mZoom, mLatLng, mMsg) {
    
        var mOptions = googlemap.getOptions(mZoom, mLatLng, google.maps.MapTypeId.ROADMAP);

        googlemap.msgNode.innerHTML = mMsg;
//window.alert("Creating maps");
        // Create the map using the options
        new google.maps.Map(googlemap.canvas, mOptions);
    },
    
    getOptions:
    function (zm, latlng, mapType) {
        // Initialize map options using the specified latitude and logitude
        var mOptions = {
          zoom: zm,
          center: latlng,
          mapTypeId: mapType
        };
        return mOptions;
    },

    loadAddress:
    function (address) {
    
        // Create the geocoder
        var geocoder = new google.maps.Geocoder();    
        
        // Retrieve the 'location' for the specified address 
        geocoder.geocode( {'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                var mLatLng = results[0].geometry.location;
                googlemap.loadMap(16, mLatLng, mLatLng);
            }else{
                googlemap.loadDefault();
            } 
        });
    }
};

//<%-- update scroller with feeds scripts --%>  
function myFeedObject() {
    this.sourceURL = null;
    this.prefix = null;
    this.href = null;
    this.titles = null;
}    

var myFeedLoader = {

    feedsArr: new Array(),
    offset: null,
    maxCount: null,
    maxLength: null,
    nodeId:null,
    style: null,
    interval: null,
    updateTimeoutId: null,

    init:
    function (mOffset, mMaxCount, mMaxLength, mNodeId, mStyle, mInterval) {
        myFeedLoader.offset = mOffset;
        myFeedLoader.maxCount = mMaxCount;
        myFeedLoader.maxLength = mMaxLength;
        myFeedLoader.nodeId = mNodeId;
        myFeedLoader.style = mStyle;
        myFeedLoader.interval = mInterval;
    },
    
    loadFeeds:
    function (mURL, mPrefix, mHREF, mUpdate) {

//window.alert("myFeedLoader#loadFeeds. URL: "+mURL+"\nUpdate: "+mUpdate); 

        myAjax.send("GET", null, mURL,
        function(ajaxResponse){
//window.alert("myFeedLoader#loadFeeds$callback(ajaxResonse)");
            if(!ajaxResponse) return;
            
            var mText = ajaxResponse.responseText;
//window.alert("myFeedLoader#loadFeeds$callback(ajaxResonse) Text length: "+mText.length);

            // !mText did't work
            if(mText === null || mText.length === 0) return;
            
            var mFeedObj = new myFeedObject();

            mFeedObj.sourceURL = mURL;
//window.alert("myFeedLoader#loadFeeds$callback(ajaxResonse) GOT HERE");

            mFeedObj.titles = mText.split(", "); // Notice the space
            
//window.alert("myFeedLoader#loadFeeds$callback(ajaxResonse) Results: "+mFeedObj.titles);
            mFeedObj.prefix = mPrefix;

            mFeedObj.href = mHREF;

            myFeedLoader.feedsArr.push(mFeedObj);
//window.alert("myFeedLoader#loadFeeds$callback(ajaxResonse) After push, sources: "+myFeedLoader.feedsArr.length);

            if(mUpdate === true) {
//window.alert("myFeedLoader#loadFeeds$callback(ajaxResonse) Updating.");
                myFeedLoader.update();
            }
        });
    },
    
    update:
    function () {
//window.alert("myFeedLoader#update. Available sources: "+myFeedLoader.feedsArr.length);

        if(myFeedLoader.feedsArr.length === 0) return;
        
        // The count is divided amongst the number of feeds    
        var mMax = myFeedLoader.maxCount / myFeedLoader.feedsArr.length;

        if(mMax < 1) mMax = 1;
        
        var mAllFeedText = "";

        for(var i=0; i<myFeedLoader.feedsArr.length; i++) {
//window.alert("myFeedLoader#update. Index: "+i);

            for(var j=0; j<myFeedLoader.feedsArr[i].titles.length; j++) {
                
                if(j === mMax) break;
                
                var mFeed = myFeedLoader.getFeed(i, j, myFeedLoader.offset, myFeedLoader.maxLength);
//window.alert("myFeedLoader#update. Feed["+i+"]["+j+"]="+mFeed);
                
                if(mFeed !== null && mFeed.length > 0) {
                    
                    if(j === 0) {
                        mAllFeedText += myFeedLoader.feedsArr[i].prefix;
//window.alert("myFeedLoader#update. Appended Prefix"+myFeedLoader.feedsArr[i].prefix);
                    }
                    
                    mAllFeedText += (mFeed + "&emsp;");
                }
            }
        }
        
        if(myFeedLoader.style !== null) {
            mAllFeedText = "<span style=\""+myFeedLoader.style+"\">"+mAllFeedText+"</span>";
        }
        
//window.alert("myFeedLoader#update. Total length: "+mAllFeedText.length);
        
        looseboxes.$(myFeedLoader.nodeId).innerHTML = mAllFeedText;
        
        // Update our index
        myFeedLoader.offset = myFeedLoader.offset + 1;
//window.alert("myFeedLoader#update. Next index: "+myFeedLoader.offset+", interval: "+myFeedLoader.interval);
        
        if(myFeedLoader.interval > 0) {
            myFeedLoader.updateTimeoutId = setTimeout(
                function(){myFeedLoader.update();}, myFeedLoader.interval
            );
        }
    },
    
    getFeed:
    function(i, j, mOffset, mMaxLength) {

        j = j + mOffset;

        while(j >= myFeedLoader.feedsArr[i].titles.length) {
            j = j - myFeedLoader.feedsArr[i].titles.length;
        }
//window.alert("myFeedLoader#getFeed. Adjusted Index: "+j);

        var mText = myFeedLoader.feedsArr[i].titles[j];
        
//window.alert("myFeedLoader#getFeed. Text: "+mText);

        if(mText === null || mText.length === 0) return null;
        
        if(mText.length > mMaxLength) {
            mText = mText.substring(0, mMaxLength - 3) + "...";
        }else{
            mText = mText + ".";
        }

        // Add a link
        var output = "<a href=\""+myFeedLoader.feedsArr[i].href+"\" target=\"_blank\">"+mText+"</a>";
        
//window.alert("myFeedLoader#getFeed. Output: "+output);
        return output;
    },
    
    cancelUpdate:
    function() {
        if(myFeedLoader.updateTimeoutId === null) return;
        window.clearTimeout(myFeedLoader.updateTimeoutId);
    }
};    

var myProgress = {
    
    mInterval: null,
    mURL: null,
    mMsgId:null,
    mMsgStyle: null,
    mProgressId: null,
    mDoneColor: null,
    mLeftColor: null,
    mIndicatorHeight: null,
    mTimeout: null,
    mRefreshURL: null,
    mStopURL: null,
    mStopOptionHtml: null,
    startTime: null,
    updateTimeoutId: null,
    mIndeterminateInterval: null,
    indeterminateProgressPos: 1,
    indeterminateDisplaying: false,
    indeterminateTimeoutId: null,
    mLastPos: null,
    
    init:
    function (interval, indeterminateInterval, timeout, 
        url, msgId, progressId, refreshUrl, stopUrl) {
    
        myProgress.initX(interval, indeterminateInterval, timeout, url, msgId, 
        progressId, "font-size:1em; margin-left:1em", "#777777", "#CCCCCC", "1em", refreshUrl,
        stopUrl, "&emsp;<input type=\"button\" onclick=\"myProgress.terminate()\" value=\"Stop\"/>");
    },
    
    initX:
    function (interval, indeterminateInterval, timeout, url, 
        msgId, progressId, messageStyle, doneColor, leftColor, 
        indicatorHeight, refreshUrl, stopUrl, stopOptionHtml) {
        
//window.alert("myProgress#init. URL: "+myProgress.mURL); 
        myProgress.mInterval = interval;
        myProgress.mURL = url;
        myProgress.mMsgId = msgId;
        myProgress.mMsgStyle = messageStyle;
        myProgress.mProgressId = progressId;
        myProgress.mDoneColor = doneColor;
        myProgress.mLeftColor = leftColor;
        myProgress.mIndicatorHeight = indicatorHeight;
        myProgress.mTimeout = timeout;
        myProgress.mIndeterminateInterval = indeterminateInterval;
        myProgress.mRefreshURL = refreshUrl;
        myProgress.mStopURL = stopUrl;
        myProgress.mStopOptionHtml = stopOptionHtml;

        myProgress.update();
    },

    update:
    function () {

//window.alert("myProgress#update. URL: "+myProgress.mURL); 
        var currentTime = new Date().getTime();
            
        if(myProgress.startTime === null) {
            
            myProgress.startTime = currentTime;
            
        }else{
            
            if((currentTime - myProgress.startTime) > myProgress.mTimeout) {
                
                myProgress.terminate();
                
                return;
            }
        }

        myAjax.send("GET", null, myProgress.mURL,
        function(ajaxResponse){
//window.alert("myProgress#update$callback(ajaxResonse)");
            if(!ajaxResponse) return;
            
            // ajaxResponse.responseXML gives lots of problem

            var resText = ajaxResponse.responseText;
//window.alert("myProgress#update$callback(ajaxResonse) Response xml:\n"+resText);

            var pos = null;
            var msg = null;
            
            // Expected format: 75,Some message here
            // Expected format: 1,Some message to display here
            // 
            // @related_progressIndicatorResponseFormat
            //
            if(resText !== null && resText.length > 0) {
                
                var n = resText.indexOf(',');    

                if(n > 0) {
                    pos = resText.substring(0, n);
                    msg = resText.substring(n+1, resText.length);
                }
            }
            
//window.alert("myProgress#update$callback(ajaxResonse)\nupdating position: "+pos);
//window.alert("myProgress#update$callback(ajaxResonse) Message length: "+(msg==null?"null":msg.length));

            // !msg did't work
            if(msg === null || msg.length === 0) {
                
                myProgress.terminate();
                
            }else{

                if(myProgress.mIndeterminateInterval !== null) {
                    
                    // Display indeterminate progress bar
                    if(myProgress.indeterminateDisplaying !== true) {
                        
                        myProgress.indeterminateDisplaying = true;
                        
                        myProgress.displayIndeterminate();
                        
                        window.scrollTo(0, 0);
                    }
                    
                }else{
                    
                    // Update the progress indicator
                    //
                    
                    if(myProgress.mLastPos !== pos) {
                        
                        mLastPos = pos;
                        
                        myProgress.displayProgress(pos);    
                    }
                    
                    if(myProgress.updateTimeoutId === null) {
                        
                        window.scrollTo(0, 0);
                    }
                }
                
                if(myProgress.mStopOptionHtml !== null) {
                    msg += myProgress.mStopOptionHtml;
                }
                
                // Update the message display
                myProgress.displayMessage(msg);    

                if(pos >= 100) {
                    myProgress.terminate();
                }else{
                    myProgress.scheduleNextUpdate();
                }
            }
        });
    },

    displayIndeterminate:
    function() {
        if(myProgress.mIndeterminateInterval > 0) {
            myProgress.indeterminateTimeoutId = setInterval(
                function(){myProgress.nextProgress();}, myProgress.mIndeterminateInterval
            );
        }
    },

    scheduleNextUpdate:
    function() {
        if(myProgress.mInterval > 0) {
            myProgress.updateTimeoutId = setTimeout(
                function(){myProgress.update();}, myProgress.mInterval
            );
        }
    },
    
    nextProgress:
    function() {
        if(myProgress.mProgressId !== null) {
            var prog = myProgress.nextProgressIndicator();
            // This method had issues
//                    looseboxes.setNodeValue(myProgress.mProgressId, prog);
            looseboxes.$(myProgress.mProgressId).innerHTML = prog;
            looseboxes.setDisplay(myProgress.mProgressId, "block");
        }
    },

    displayProgress:
    function(pos) {
        if(myProgress.mProgressId !== null) {
            var prog = myProgress.getProgressIndicator(pos);
            // This method had issues
//                    looseboxes.setNodeValue(myProgress.mProgressId, prog);
            looseboxes.$(myProgress.mProgressId).innerHTML = prog;
            looseboxes.setDisplay(myProgress.mProgressId, "block");
        }
    },
    
    displayMessage:
    function(msg) {
        if(myProgress.mMsgId !== null) {

            if(myProgress.mMsgStyle !== null) {
                msg = "<div style=\""+myProgress.mMsgStyle+"\">"+msg+"</div>";
            }
            
            // This method had issues
//            looseboxes.setNodeValue(myProgress.mMsgId, msg);
            looseboxes.$(myProgress.mMsgId).innerHTML = msg;
            looseboxes.setDisplay(myProgress.mMsgId, "block");
        }
    },

    nextProgressIndicator:
    function() {

        var parts = 10;
        
        if(myProgress.indeterminateProgressPos === null || 
            myProgress.indeterminateProgressPos >= parts) {
            
            myProgress.indeterminateProgressPos = 0;
        }
        
        // Update the progress indicator
//                    var sp = 2; // Allowance, incase browser has default margins etc

        // compute widths in percent
        //
        var leftWidth = (myProgress.indeterminateProgressPos++ - 0) * (100 / parts);
        var progWidth = 1 * (100 / parts);
        var rightWidth = 100 - (leftWidth + progWidth);

        var left = "<div style=\"background-color:"+myProgress.mLeftColor+"; width:"+leftWidth+"%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0; float:left\"></div>";
        var prog = "<div style=\"background-color:"+myProgress.mDoneColor+"; width:"+progWidth+"%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0; float:left\"></div>";
        var right = "<div style=\"background-color:"+myProgress.mLeftColor+"; width:"+rightWidth+"%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0; float:left\"></div>";
        return "<div style=\"width:100%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0;\">" + left + prog + right + "</div>";
    },

    getProgressIndicator:
    function(pos) {
        // Update the progress indicator
//                    var sp = 2; // Allowance, incase browser has default margins etc
        var completed = "<div style=\"background-color:"+myProgress.mDoneColor+"; width:"+(pos)+"%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0; float:left\"></div>";
        var remaining = "<div style=\"background-color:"+myProgress.mLeftColor+"; width:"+(100-pos)+"%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0; float:left\"></div>";
        return "<div style=\"width:100%; height:"+myProgress.mIndicatorHeight+"; margin:0; border:0; padding:0;\">" + completed + remaining + "</div>";
    },
    
    stop:
    function() {
        if(myProgress.mStopURL === null) {
            return;
        }
        myAjax.send("GET", null, myProgress.mStopURL,
        function(ajaxResponse){
//window.alert("myProgress#update$callback(ajaxResonse)");
            if(!ajaxResponse) return;
            // ajaxResponse.responseXML gives lots of problem
            var resText = ajaxResponse.responseText;
//window.alert("myProgress#update$callback(ajaxResonse) Response xml:\n"+resText);
        });
    },    
    
    terminate:
    function() {
    
        myProgress.stop();

        myProgress.cancelIndeterminateDisplay();
        
        myProgress.cancelUpdate();

        if(myProgress.mProgressId !== null) {
            // Hide the progress indicator
            looseboxes.setDisplay(myProgress.mProgressId, "none");
        }

        if(myProgress.mMsgId !== null) {
            // Hide the message display
            looseboxes.setDisplay(myProgress.mMsgId, "none");
        }

        if(myProgress.mRefreshURL !== null) {

            myProgress.displayMessage(" Please wait... Refreshing");

            document.location.href = myProgress.mRefreshURL;
            
            // When the page is reloaded, the execution may get here again
            // this prevents continous reloads
            myProgress.mRefreshURL = null;
        
            if(myProgress.mMsgId !== null) {        
                looseboxes.setDisplay(myProgress.mMsgId, "none");
            }
        }
    },
    
    cancelIndeterminateDisplay:
    function() {
        if(myProgress.indeterminateTimeoutId === null) return;
        window.clearInterval(myProgress.indeterminateTimeoutId);
    },
    
    cancelUpdate:
    function() {
        if(myProgress.updateTimeoutId === null) return;
        window.clearTimeout(myProgress.updateTimeoutId);
    }
};

var myChat = {
    
    loopIntervalIds: new Array(),

    enterkeyPress:
    function (eventSrc, idToSubmit, idToUpdate, firstUrl, refreshUrl) {
//window.alert("Event src: "+eventSrc);        
        myChat.doEnterkeyPress(eventSrc, idToSubmit, idToUpdate, firstUrl, refreshUrl, 3000);
    },
    
    doEnterkeyPress:
    function (eventSrc, idToSubmit, idToUpdate, firstUrl, refreshUrl, loopInterval) {
        myChat.getElement(eventSrc).onkeypress = function(e){
          if (!e) e = window.event;   // resolve event instance
          if (e.keyCode === '13'){
            myChat.doGetChat(idToSubmit, idToUpdate, firstUrl, refreshUrl, loopInterval);  
            return false; // prevent the event's default behaviour
          }
          return true;
        };
    },
        
    getChat:
    function (idToSubmit, idToUpdate, firstUrl, refreshUrl) {
        myChat.doGetChat(idToSubmit, idToUpdate, firstUrl, refreshUrl, 3000);
    },
    
    doGetChat:    
    function (idToSubmit, idToUpdate, firstUrl, refreshUrl, loopInterval) {
        var i;
        var alreadyLooped = false;
        for(i=0; i<myChat.loopIntervalIds.length; i++) {
          var arr = myChat.loopIntervalIds[i];  
          if(arr[0] === idToSubmit) {
              alreadyLooped = true;
              break;
          }  
        }
          
        myAjax.get(idToSubmit, idToUpdate, firstUrl);         
        
//window.alert("Already looped: "+alreadyLooped);

        if(alreadyLooped===false) {
//        if(1===4) {
//window.alert("@loopChat. About to set interval");              
          var mIntervalId = setInterval(
            function(){ 
              myAjax.get(idToSubmit, idToUpdate, refreshUrl); 
            }, 
            loopInterval
          );  
//window.alert("@loopChat. Interval id: "+mIntervalId);
          myChat.loopIntervalIds[myChat.loopIntervalIds.length] = [idToSubmit, mIntervalId];
//window.alert("@loopChat. loopIntervalIds: "+myChat.loopIntervalIds);              
        }
    },  
    
    endChat:
    function (elementId) {
//window.alert("@endChat. loopIntervalIds: "+myChat.loopIntervalIds);              
        var i;
        for(i=0; i<myChat.loopIntervalIds.length; i++) {
          var arr = myChat.loopIntervalIds[i];  
          if(arr[0] === elementId) {
//window.alert("Clearing interval: "+myChat.loopIntervalIds);              
            clearInterval(arr[1]);
            myChat.loopIntervalIds.splice(i, 1);
//window.alert("After clearing chats left: "+myChat.loopIntervalIds);              
            break;
          } 
        }
    },

    getElement:
    function (elementId){if(document.getElementById) return document.getElementById(elementId);else if(document.all) return document.all(elementId); else return null;}
}; 

