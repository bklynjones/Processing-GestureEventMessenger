/*Code modified by Brian Jones Finalbuild.net */
package newmotiontester;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.*;
import processing.net.*;

public class NewMotionTester extends PApplet {
	
	Capture video;
	// Previous Frame
	PImage prevFrame;
	// How different must a pixel be to be a "motion" pixel

	//---Variables for Socketr---\\
	//Server msgSocket;
	Server myServer;
	Server myServerTwo;
	String origin;
	String host;
	String whatClientSaid;
	String myResponse;
	int val = 0; // This is never read
	
	//---Global Variables for gestureBuffer
	ArrayList<String> gBuffer = new ArrayList<String>();
	
	//---Variables for MotionTester functions--\\
	int loc;
	int[] xpos = new int[50];
	int[] ypos = new int[50];
	float threshold = 50;

	public void setup() {

		  size(320, 240);
		  
		  	//--Instansiate Socket Server--\\
			myServer = new Server(this, 1234);
			myServerTwo = new Server(this,4567);
			//msgSocket = myServer;

		  // Initialixze array values to zero
		  for (int i=0; i < xpos.length; i ++) {
		    xpos[i]=320/2;
		    ypos[i]=240/2;
		  }

		  video = new Capture(this, width, height, 30);
		  // Create an empty image the same size as the video
		  prevFrame = createImage(video.width, video.height, RGB);
		}

		public void draw() {
			//--Connect to Browser--\\
			handShake(myServer.available());
			handShake(myServerTwo.available());
			
		  background(255);
		  //Shift all the readings by one in their index
		  for (int i=0; i < xpos.length-1; i++) {
		    xpos[i] = xpos[i+1];
		    ypos[i] = ypos[i+1];
		  }

		  // Capture video
		  if (video.available()) {
		    // Save previous frame for motion detection!!
		    prevFrame.copy(video, 0, 0, video.width, video.height, 0, 0, video.width, video.height); // Before we read the new frame, we always save the previous frame for comparison!
		    prevFrame.updatePixels();
		    video.read();
		  }

		  loadPixels();
		  video.loadPixels();
		  prevFrame.loadPixels();

		  // Begin loop to walk through every pixel

		  for (int x = 0; x < video.width*.25; x++) {
		    for (int y = (int)(video.height*.40); y < video.height-video.height*.40; y ++ ) {

		      loc = x + y*video.width;            // Step 1, what is the 1D pixel location
		      int current = video.pixels[loc];      // Step 2, what is the current color
		      int previous = prevFrame.pixels[loc]; // Step 3, what is the previous color

		      // Step 4, compare colors (previous vs. current)
		      float r1 = red(current); 
		      float g1 = green(current); 
		      float b1 = blue(current);
		      float r2 = red(previous); 
		      float g2 = green(previous); 
		      float b2 = blue(previous);
		      float diff = dist(r1, g1, b1, r2, g2, b2);

		      // Step 5, How different are the colors?
		      // If the color at that pixel has changed, then there is motion at that pixel.
		      if (diff > threshold) { 
		        // If motion, display black
		        pixels[loc] = color(0);

		        //record loc x y data into array
		        xpos[xpos.length-1] = loc%width;
		        ypos[ypos.length-1] = loc/width;
		      } 
		      else {
		        // If not, display white
		        pixels[loc] = color(255);
		      }
		    }
		  }


		  for (int x = (int)(video.width-video.width*.20); x < video.width; x++) {
		    for (int y = (int)(video.height*.40); y < video.height-video.height*.40; y ++  ) {

		      loc = x + y*video.width;            // Step 1, what is the 1D pixel location
		      int current = video.pixels[loc];      // Step 2, what is the current color
		      int previous = prevFrame.pixels[loc]; // Step 3, what is the previous color

		      // Step 4, compare colors (previous vs. current)
		      float r1 = red(current); 
		      float g1 = green(current); 
		      float b1 = blue(current);
		      float r2 = red(previous); 
		      float g2 = green(previous); 
		      float b2 = blue(previous);
		      float diff = dist(r1, g1, b1, r2, g2, b2);

		      // Step 5, How different are the colors?
		      // If the color at that pixel has changed, then there is motion at that pixel.
		      if (diff > threshold) { 
		        // If motion, display black
		        pixels[loc] = color(0);

		        //record loc x y data into array
		        xpos[xpos.length-1] = loc%width;
		        ypos[ypos.length-1] = loc/width;
		      } 
		      else {
		        // If not, display white
		        pixels[loc] = color(255);
		      }
		    }
		  }

		  updatePixels();


		  ellipse(locationAverageX(), locationAverageY(), 25, 25);
		  fill(255, 0, 0);
		  noStroke();





		  if ((locationAverageX() < (width*.20) +10) && (locationAverageY() > height - height*3)) {
		    if (second()%2==0) {
		     // println("right");
		      gestureBuffer(myServer,20,"r");
		    }
		  }
		  else if ((locationAverageX() > (width - (width*.20) +10) && (locationAverageY() > height - height*3))) {
		    if (second()%2 == 0) {
		    //  println("left");
		      gestureBuffer(myServer,20,"l");
		      //loc =  video.width/2 + video.height/2*video.width;
		    }
		  }
		  
		  if ((locationAverageX() < (width*.20) +10) && (locationAverageY() > height - height*3)) {
			    if (second()%2==0) {
			     // println("right");
			      //gestureBuffer(myServerTwo,20,"e");
			    	msgBrowser(myServerTwo,"e");
			    }
			  }
			  else if ((locationAverageX() > (width - (width*.20) +10) && (locationAverageY() > height - height*3))) {
			    if (second()%2 == 0) {
			    //  println("left");
			      //gestureBuffer(myServerTwo,20,"k");
			    	msgBrowser(myServerTwo,"k");
			      //loc =  video.width/2 + video.height/2*video.width;
			    }
			  }
		  
		}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { newmotiontester.NewMotionTester.class.getName() });
	}

int locationAverageX() {

	  int xAvg;
	  int ttlx=0;

	  for (int i = 0; i < xpos.length; i++) {
	    ttlx = ttlx + xpos[i];
	  }
	  xAvg =  ttlx/xpos.length;
	  return xAvg;
	}


	int locationAverageY() {

	  int yAvg;
	  int ttly=0;

	  for (int i = 0; i < xpos.length; i++) { 
	    ttly = ttly + ypos[i];
	  }
	  yAvg =  ttly/xpos.length;
	  return yAvg;
	}
	
	//---Start of Functions for Socket Connection--\\
	

	

	void handShake(Client tThisClient) {

		Client thisClient = tThisClient;
		if (thisClient == null) {
			return;
		}

		println("Connecting:");

		whatClientSaid = thisClient.readString();

		// Make sure we get the entire message
		while (whatClientSaid == null) {
			whatClientSaid = thisClient.readString();
		}
		// While there is incoming data
		while (thisClient.available() > 0) {
			whatClientSaid = whatClientSaid + thisClient.readString();
		}
		// println( "Coming from the browser==>" + whatClientSaid);
		// if this is the inital, then setup the connection

		if (whatClientSaid.indexOf("Sec-WebSocket") > 0) {

			origin = getOrigin(whatClientSaid);
			host = getHost(whatClientSaid);

			myResponse = "HTTP/1.1 101 WebSocket Protocol Handshake\r\n"
					+ "Upgrade: WebSocket\r\n" + "Connection: Upgrade\r\n"
					+ "Sec-WebSocket-Origin:" + origin + "\r\n"
					+ "Sec-WebSocket-Location: ws://" + host + "/\r\n" + "\r\n"
					+ new String(procClientHeader(whatClientSaid));

			println("I'm here now " + myResponse);
			thisClient.write(myResponse);
			isWebSocket = true;
			
//			UpdateServer();
		} else {
			// else this is data streaming in from the client
			println(whatClientSaid);
		}
	}
	
	boolean isWebSocket = false;
	
	public void gestureBuffer(Server tMsgSocket,int ttrigger, String tMsgToBeBuffered){
		
		Server msgSocket = tMsgSocket;
		int trigger = ttrigger;
		String msgToBeBuffered = tMsgToBeBuffered;
		
		//multiple function calls by the location trigger fills up gBuffer
		gBuffer.add(msgToBeBuffered);
		println(msgToBeBuffered);
		println(gBuffer.size());
		
		//gBuffer is tested against size of trigger until true
		if(gBuffer.size() > trigger){
			
			// iterate through ArrayList
			for( int i = 0; i < gBuffer.size(); i++){
				// if the elements don't match the first message sent clear gBuffer and reset
				if( gBuffer.get(0) != gBuffer.get(i)){
					gBuffer.clear();
					gBuffer.trimToSize();
				}
				else{
				// if they match then clear the message for transmission to browser and reset gBuffer
					gBuffer.clear();
					gBuffer.trimToSize();
					String fireMsg = msgToBeBuffered;
					msgBrowser(msgSocket,fireMsg);
					println(fireMsg + " is off to the browser!");
					
				// reset location of loc 
					for (int p=0; p < xpos.length; p ++) {
					      xpos[p]=video.width/2;
					      ypos[p]=video.width/2;
					    }
					}	
			}
		}
		
	}
void msgBrowser(Server tMsgSocket, String tMsg ) {
	
	Server msgSocket = tMsgSocket;
		String msg = tMsg;
		// When I call this outside of the handshake function it all falls apart

		msgSocket.write(0x00);

		msgSocket.write(msg);

		msgSocket.write(0xff);
	}
	
	
	byte[] procClientHeader(String header) {
		String[] data = split(header, '\n');

		
		BigInteger sec1 = null;
		BigInteger sec2 = null;

		byte[] SecKey3 = new byte[8];
		byte[] lSecKeyResp = new byte[8];

		/*
		 * get the two Sec keys and return them as longs
		 */
		for (int i = 0; i < data.length; i++) {
			if (data[i].indexOf("Sec-WebSocket-Key1:") != -1) {
				sec1 = procValue(data[i]);
				// println("l1:" + sec1);
			}
			if (data[i].indexOf("Sec-WebSocket-Key2:") != -1) {
				sec2 = procValue(data[i]);
				// println("l2:" + sec2);
			}
		}

		/*
		 * get the random 8 bits at the end as a string
		 */
		SecKey3 = data[data.length - 1].getBytes();
		// println("sec3:" + SecKey3);

		// concatene 3 parts secNum1 + secNum2 + secKey
		byte[] l128Bit = new byte[16];
		byte[] lTmp;
		lTmp = sec1.toByteArray();
		for (int i = 0; i < 4; i++) {
			l128Bit[i] = lTmp[i];
		}
		lTmp = sec2.toByteArray();
		for (int i = 0; i < 4; i++) {
			l128Bit[i + 4] = lTmp[i];
		}
		lTmp = SecKey3;
		for (int i = 0; i < 8; i++) {
			l128Bit[i + 8] = lTmp[i];
		}

		// println("Concatted stuff:" + l128Bit );
		// println("length:" + l128Bit.length);
		MessageDigest m = null;
		try {
			m = java.security.MessageDigest.getInstance("MD5");
			// m.reset();
			lSecKeyResp = m.digest(l128Bit);
		} catch (Exception e) {
		}

		// println("md5sum:" + lSecKeyResp);

		return lSecKeyResp;
	}
	BigInteger procValue(String Value) {

		Value = Value.substring(20);
		Value = Value.trim();

		String number = "";
		int count = 0;
		BigInteger returnLong = new BigInteger("0");

		CharacterIterator it = new StringCharacterIterator(Value);

		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (Character.isDigit(ch)) {
				number = number + ch;
			}

			if (ch == ' ') {
				count++;
			}
		}

		// println("Number:" + number);
		long l = Long.parseLong(number) / count;

		return BigInteger.valueOf(l);
	}
	
	String getOrigin(String header) {
		String[] data = split(header, '\n');
		for (int i = 0; i < data.length; i++) {
			if (data[i].indexOf("Origin") != -1) {
				return data[i].substring(8).trim();
			}
		}
		return null;
	}

	String getHost(String header) {
		String[] data = split(header, '\n');
		for (int i = 0; i < data.length; i++) {
			if (data[i].indexOf("Host") != -1) {
				return data[i].substring(5).trim();
			}
		}
		return null;
	}
}