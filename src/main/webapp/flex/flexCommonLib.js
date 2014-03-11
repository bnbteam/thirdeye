
function getServerHomeUrl(){
	
	//return "/";
	return "/thirdeye/";  
	//return "http://thirdeye.lahuman.pe.kr/";
}

function getBusinessCode(){
	
	return getRequestParamOnJS("business");
}

function getRequestParamOnJS(valuename){
    var rtnval = "";
    var nowAddress = unescape(location.href);
    var parameters = (nowAddress.slice(nowAddress.indexOf("?")+1,nowAddress.length)).split("&");
   
    for(var i = 0 ; i < parameters.length ; i++){
        var varName = parameters[i].split("=")[0];
        if(varName.toUpperCase() == valuename.toUpperCase()){
            rtnval = parameters[i].split("=")[1];
            break;
        }
    }
    return rtnval;
}

function getBrowser(){
	
	var browser = {
	    ie: false, 
	    firefox: false, 
	    safari: false, 
	    opera: false, 
	    version: -1
	};
	
	//UserAgent detection
	var useragent = navigator.userAgent.toLowerCase();

	if (useragent.indexOf("opera") != -1) {
	    browser.opera = true;
	} else if (useragent.indexOf("msie") != -1) {
	    browser.ie = true;
	    browser.version = parseFloat(useragent.substring(useragent.indexOf('msie') + 4));
	} else if (useragent.indexOf("safari") != -1) {
	    browser.safari = true;
	    browser.version = parseFloat(useragent.substring(useragent.indexOf('safari') + 7));
	} else if (useragent.indexOf("gecko") != -1) {
	    browser.firefox = true;
	}

	if (browser.ie == true && browser.version == 7) {
	    
	}
	
	return browser;
}

