package  
{
	/**
	 * ...
	 * @author Alexander Ferguson
	 */
	public class StepList 
	{
		
		private var SIZE:Number;					//NUMBER OF BOXES ( get from a class )
		private var OrderList:Array;
		private	var stepList:Array; 
		
		public function StepList() 
		{
			stepList = [0,7]; 
			SIZE = 10;
			OrderList = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
		}
		public function getSize():int {
			return SIZE;
		}
		public function getList():Array {
			return stepList;
		}
		public function getOrderList():Array {
			return OrderList;
		}
		public function StepListFromString(inputString:String):void {
						
			//decide on a protocol to parse.
			// need length of array, and swaps+colour changes.
			// LEN:(S1,S2)(S1,S2)(S1,S2)(S1,255,140,140) ? 
			// LEN:s1,s2-s1,s2-c1,1,2,3-
			// maybe iterate over getindex of, with a cursor. 
			// example:
			// 12:-4,3-4,2-5,3-4,6-4,6-7,6
			// you forgot about initial variables
			
			// so it should look like:
			// 0,2,3,4,2,5,4,2,3,4,6,7:-4,3-4,2-5,3-4,6-4,6-7,6
			// [start array.toString]:-[swap]-[colour]-
			
			
			
			trace("THIS IS THE STRING TO PARSE: " + inputString);
			var cursor:int = inputString.indexOf(':', 0);
			var initSplit:String = inputString.substr(0, cursor);// gets all nums before ":"
			trace("THIS IS THE INPUT STRING: " + initSplit); 
			var initSplitArray:Array = initSplit.split(",", 100);
			
		//	for (i = 0; i < initSplitArray.length; i++) {
		//		trace("initSplitArray[" + i + "]: " + initSplitArray[i]);
		//	}
			

			
			SIZE = initSplitArray.length; 
			trace("LENGTH = " + SIZE);
			var splitArray:Array = inputString.split("-",1000);
			var i:int;
		//	for (i = 0; i < splitArray.length; i++) {
		//		trace("splitArray[" + i + "]: " + splitArray[i]);
		//	}
			
			
			var tempArray:Array = [[0,0,0,255]];

			for (i = 1; i < splitArray.length; i++) {
				var innerSplit:Array = splitArray[i].split(",", 4);
		//		trace("INNERSPLIT: " + innerSplit);
				if (innerSplit.length == 2) { // SWAP
					tempArray[i] = [int(innerSplit[0]), int(innerSplit[1])];
				}
				if (innerSplit.length == 4) { // COLOUR
					tempArray[i] = [int(innerSplit[0]), int(innerSplit[1]),int(innerSplit[2]), int(innerSplit[3])];
				}
				if (innerSplit.length == 0) {
					// ends with a hyphen or something. ignore.
					SIZE--;
				}

			}
			trace("FINAL SIZE = " + SIZE);
			stepList = tempArray;
			
			
			OrderList = initSplitArray;
			trace("ORDER LIST = " + OrderList);
			

		//	for (i = 0; i < stepList.length; i++) {
		//		trace("STEPLIST["+i+"]: "+stepList[i]);
		//	}
			
			
			
			
		}
		

		
	}

}