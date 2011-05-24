
		var classArray = new Array();
		classArray = ["poszero","posone","postwo","posthree","posfour","posfive","possix","posseven","poseight","posnine"];
		var z=0;
// galleryarray is a global defined by the php at the head of the page

$(document).ready(function() {

// ttl-img_width is returned outerWidth of all <img> elements in slideshow
 var ttl_img_width = 0;
 
  //var boxOffset = $('#box').offset();

 
 // This Function builds the page based on the elements contained within the "lo_image" directory
	imageListr(galleryarray);
	
		function imageListr(feedr){
		
			$.each(feedr,function(){
				$(document.createElement('img')).appendTo('#box');
			});
				
		
			$('img').each(function(i,v){
				$(v).attr({
						src:"lo_image/" + feedr[i],
						width: "1916"				
						});
			});

	}//end imageListr function

	$('img').each(function(i,v){


	  var imgWidth =  $(v).outerWidth();//<--determine the outer dimension of each image
	  ttl_img_width = ttl_img_width + imgWidth;//<--See note on variable in top declarations
		
		/* for debug
		console.log(ttl_img_width);
		console.log(imgWidth);
		*/
	});

	$('#box').css({ width: ttl_img_width});	


	// create  Web socket instances
	var ws = new WebSocket("ws://LocalHost:1234");
	var wstwo = new WebSocket("ws://LocalHost:4567");
	
		//open connections
		ws.onopen = function() {
			alert("CONNECTED");
			ws.send("Connection with Processing Sketch Establish");//<--outgoing test message
		};
		
		wstwo.onopen = function() {
			alert("CONNECTED TO Line TWO");
			wstwo.send("Line Two Connection with Processing Sketch Establish");
	     };

		//First socket: handles gesture events
		

		ws.onmessage = function(evt) {
			
			var $box = $('#box');// target element with id: 'box'
 			var position = $box.position();// return the value of the elements top left corner
			
			//console.log(position.left);
			
			if(evt.data === "l"){
				if(position.left < 0){
						$('#box').animate({"left": '+=1916'},"slow");
				}
			}
			else if(evt.data === "r"){
				//if((position.left) *-1 ===11496){
					$('#box').addClass(classArray[z]);
					z++;
					console.log(i);							}
		};
		
		// Second socket:  used for UI feeback 
		var kBol = new Boolean(false);
		var eBol = new Boolean(false);
		
		wstwo.onmessage = function(evt) {
		
			if(evt.data === "k"){
				kBol = true;
				eBol = false;
			}
		else if(evt.data === "e"){
				kBol = false;
				eBol = true;
			}

			if(kBol === true){
				//$('#leftBar').css({ opacity: '.5'});
				//$('#rightBar').animate({"opacity": '0'},"slow");
				$('#leftBar').fadeIn();
				$('#rightBar').fadeOut();
			}
			//console.log(evt.data);

			if(eBol  === true){
				//$('#rightBar').css({ opacity: '.5'});
				//$('#leftBar').animate({"opacity": '0'},"slow");
				$('#rightBar').fadeIn();
				$('#leftBar').fadeOut();
			}
			//console.log(evt.data);
		}
		
		// other ws socket functions 
		ws.onclose = function(evt) {
			alert("closed con");
		};

		ws.onerror = function(evt) {
			alert("error error");
		};
		
		wstwo.onclose = function(evt) {
			alert("closed con line two");
		};

		wstwo.onerror = function(evt) {
			alert("error error");
		};
		
		
	
});//End document
$('#bod').click(function() 
	{
	if( z ===7){
		z=0;
		$(this).removeClass().addClass('a1 ' + classArray[z]); 
	}
	$(this).addClass(classArray[z]);
	z++;
	console.log(z);		
});