package 
{
	import flash.display.SimpleButton;
	import flash.utils.getTimer;
    import flash.utils.Timer;
    import flash.events.MouseEvent;
    import flash.geom.Rectangle;
	import flash.display.Sprite;
	import flash.events.*;
	import flash.text.*;
	import flash.events.TimerEvent;
	import flash.geom.ColorTransform;
	import flash.geom.Transform;
	import flash.display.SimpleButton;
	
	/**
	 * ...
	 * @author Alexander Ferguson
	 */
	public class Main extends Sprite 
	{
		
		// create text field and button
		// user enters code and hits accept
		// remove button and textfield

		
		private var myFirstTextBox:TextField = new TextField();
		private var myText:String = "";
		private var start:EasyButton = new EasyButton("Submit");
		private var StringToParse:String;
		private var enterPrompt:TextField = new TextField();

		
		public function CaptureUserInput():void { 
			captureText(); 
		} 
		
		public function captureText():void {
			enterPrompt.text = "Paste text to run:";
			addChild(enterPrompt);
			
			myFirstTextBox.type = TextFieldType.INPUT; 
			myFirstTextBox.x = 100;
			myFirstTextBox.y = 0;
			myFirstTextBox.width = 200;
			myFirstTextBox.height = 20;
			myFirstTextBox.background = true; 
			myFirstTextBox.border = true;
			addChild(myFirstTextBox); 
			myFirstTextBox.text = myText; 
			
			
			addChild(start);
			start.x = 300;
            start.addEventListener(MouseEvent.CLICK, getStringFromUser); 

		}
		
		public function getStringFromUser(e:Event = null):void { 
			StringToParse = myFirstTextBox.text; 
			removeChild(start);
			removeChild(enterPrompt);
			removeChild(myFirstTextBox);

			runMain();
		} 
		
		
		public function Main():void 
		{
			if (stage) init();
			else addEventListener(Event.ADDED_TO_STAGE, init);
			
		}
		
		private function init(e:Event = null):void  //SETUP DATA STRUCTURES AND GUI
		{
			
			removeEventListener(Event.ADDED_TO_STAGE, init);
			// entry point
			captureText();
			
			
		}
		
		private function runMain():void {		
		
			var SIZE:int;
			var stepList:Array; 
			var OrderL:Array;
			var s:StepList = new StepList();
			s.StepListFromString(StringToParse);
			SIZE = s.getSize();						// NUMBER OF BOXES ( get from a class )
			stepList = s.getList(); 				// ( get from a class )
			OrderL = s.getOrderList();
			
		//	var title:TextField = new TextField();
		//	title.text = "";
		//	addChild(title);
			
			var stepCount:Number = 0;
			var orderArray:Array = new Array(SIZE);
			var shapeArray:Array = new Array(SIZE);
			var labelArray:Array = new Array(SIZE);
			
			var i:Number;
			for(i =0; i<SIZE; i++){		//SETUP BOXES
				var sprite:Sprite =new Sprite(); 
				var   text:Sprite =new Sprite();
				sprite.graphics.lineStyle(NaN);
				sprite.graphics.beginFill(0x0000FF);
				sprite.graphics.drawRect(0, 0, 40, 40);
				sprite.graphics.endFill();
				

		
				sprite.x = (i*50);
				sprite.y = 200;
				sprite.width = 40;
				sprite.height = 40;
				
		          

				var t:TextField = new TextField();
				t.textColor = 0xfff000;
				t.text = OrderL[i];
				t.x = 15+(i*50);
				t.y = 210;
				t.width = sprite.width;
				t.height = sprite.height;
		
				
				  var group:Sprite = new Sprite(  );
						group.addChild(sprite);
						group.addChild(t);
				shapeArray[i] = ( group );
				addChild(group);

			//					sprite.addChild(t);

				orderArray[i] = i;
	//			trace("shapearray 0 =" + shapeArray[i].getChildAt(0).toString());
	//			trace("shapearray 1 =" + shapeArray[i].getChildAt(1).toString());

		}
	/*		var orderText:TextField = new TextField();
			orderText.text = orderArray.toString();
			orderText.y = 20;
			orderText.width = 600;
			addChild(orderText);
		
			var o:TextField = new TextField();
			o.textColor = 0xffffff;
			o.text = " "+orderArray.toString();
			trace(orderArray.toString());
			o.x = 15;
			o.y = 8;
			o.width = 100;
			o.height = 50;
		*/

		
		
		
		
		function swap(a:Number,b:Number):void{
			trace("Swapping "+a+" and "+b);

			
			var tempA:Number = a;
			var tempB:Number = b;
			
			if(a>b){
				 a = b;
				 b = tempA;
			}
			
			var delta:Number = (b-a);
			var timer1:Timer =  new Timer( 10,50 );
			var timer2:Timer = new Timer( 10,50 );
			var timer3:Timer = new Timer( 10,50 );

			timer1.addEventListener( TimerEvent.TIMER,moveUp);
			timer1.addEventListener(TimerEvent.TIMER_COMPLETE,oneDone);
			timer2.addEventListener( TimerEvent.TIMER,moveSide);
			timer2.addEventListener(TimerEvent.TIMER_COMPLETE,twoDone);
			timer3.addEventListener( TimerEvent.TIMER,moveDown);
			timer3.addEventListener(TimerEvent.TIMER_COMPLETE, threeDone);
			
			timer1.start();

			function oneDone(e:TimerEvent):void{
				timer2.start();
				}
				
			function twoDone(e:TimerEvent):void{
				timer3.start();
				}
				
			function threeDone(e:TimerEvent):void{ // need to swap shape and order arrays to match the visuals
			
				trace("before : ["+orderArray.toString()+"]");
				var tempSwitch:Number = orderArray[a];
				orderArray[a] = orderArray[b];
				orderArray[b] = tempSwitch;
				trace("After : ["+orderArray.toString()+"]");
		//		orderText.text = orderArray.toString();
				
				nextS();

			}

			function moveUp():void {
				shapeArray[orderArray[a]].y++;
				shapeArray[orderArray[b]].y--;
			}
			function moveSide():void{

				shapeArray[orderArray[a]].x = shapeArray[orderArray[a]].x + delta;
				shapeArray[orderArray[b]].x = shapeArray[orderArray[b]].x - delta;
			}
			function moveDown():void{
				shapeArray[orderArray[b]].y++;
				shapeArray[orderArray[a]].y--;
			}
			
		
		}
		
		
		
				function nextS():int{
			if (stepCount == stepList.length) {
				trace("finished");
				return 1;				
			}
			if (stepList[stepCount].length==2){ 					//- swap commands have 2 arguments
				swap(stepList[stepCount][0],stepList[stepCount][1]);
			}
			if (stepList[stepCount].length == 4){ 
				highlight(stepList[stepCount][0],stepList[stepCount][1],stepList[stepCount][2],stepList[stepCount][3]);
			}
			stepCount++;
			return 0;
		}
		function rgb2hex(r:int, g:int, b:int) : uint{
			var redChannel:int = r << 16;
			var greenChannel:int = g << 8;
			var blueChannel:int = b;
			return redChannel | greenChannel | blueChannel;
		}
		
		function highlight(i:Number,R:int,G:int,B:int):void{
				trace("Colouring "+i);

				//red	:0xff0000 
				//green	:0x00ff00 
				//yellow:0xffff00
				//blue	:0x0000ff
				//black :0x000000
				//white :0xffffff
				//0,2,3,4,2,5,4,2,3,4,6,7:-4,3-4,255,0,255-5,3-4,6-4,60,60,60-7,6
				
				var toColor:	uint=rgb2hex(R,G,B);
				var stepLength:Number = 10; // frame speed
			//	var steps:Number 	= 	50; // number of frames
				var progress:Number = 	0;
				var hTimer:Timer =  new Timer( stepLength,25 ); 
				hTimer.start();
				hTimer.addEventListener( TimerEvent.TIMER,startCol);
				hTimer.addEventListener( TimerEvent.TIMER_COMPLETE, finishCol);
				
				var colorTransform:ColorTransform = new ColorTransform();
				colorTransform.color = (toColor);
				
				function startCol(e:Event):void{ //change colour, wait for timer
					shapeArray[orderArray[i]].getChildAt(0).transform.colorTransform = colorTransform;
					shapeArray[orderArray[i]].getChildAt(1).textColor = (0xffffff);
				}
				
				function finishCol(e:Event):void{ //this function is just to make sure that the finished colour is requested
					nextS();
				}
		}
		
		//first
		nextS();
		
		}
		
		
	}
	
}