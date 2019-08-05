
var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() {
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        }
        anHttpRequest.open( "GET", aUrl, true );
        anHttpRequest.send( null );
    }
}
function getCode(phoneNumber) {
    console.log("phoneNumber => {$0}",arguments[0])
    var theurl="http://127.0.0.1:9090/v1/sendCode?userPhone="+arguments[0];
    var client = new HttpClient();
    client.get(theurl, function(response) {
        var response1 = response;
        console.log(response1)
        //alert(response1)
})
}