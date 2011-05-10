     


$(document).ready(function() {


//alert('window width: ' + $(window).width() +'window height' + $(window).height());

var ws = new WebSocket("ws://LocalHost:1234");
      
      ws.onopen = function() {
        alert("CONNECTED");
        //ws.send( {'test': 'message'} );
        
      //  setInterval(function(){
        
       ws.send("Connection with Processing Sketch Establish");
       // },1000);
        
       
      };
      
      ws.onclose = function(evt) {
       alert("closed con");
      };
      
      ws.onerror = function(evt) {
        alert("error error");
      };
      
      ws.onmessage = function(evt) {
      if(evt.data === "l"){
      $('#box').animate({
      			"left": '+=1152'},"slow");
      //$("#box").css('background','blue');
      $("<b>" + evt.data + "</b>").appendTo('div')
		}
		else if(evt.data === "r"){
		$('#box').animate({
      			"left": '-=1152'},"slow");
      //$("#box").css('background','blue');
      $("<b>" + evt.data + "</b>").appendTo('div')}		             
       };





});