function redirectLocation(path) {
    console.log("redirection client to ",arguments[0])
    window.location.replace(path);
}
 function progressBar(){
    var reverse_counter = 10;
    var downloadTimer = setInterval(function(){
   document.getElementById("pbar").value = 10 - --reverse_counter;
    if(reverse_counter <= 0)
    clearInterval(downloadTimer);
  //document.getElementById("counting").innerHTML= reverse_counter;
},1000);
}
