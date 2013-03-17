package AnimatedLinkedList;

import AnimatedArray.AnimatedArray;



/*a class that instantiates an AnimatedLinkedList object and tests its functionality, to be 
 * interpreted visually
 */

public class testAnimatedLinkedList {
	
	private static AnimatedLinkedList list;
	public static int time = 200;
	
	public static void main(String[] args) {
		int[] arr = {12,18,99,2};
		list = new AnimatedLinkedList(arr);
		list.addToHead(2);
		list.findAndRemove("99", list);
		list.findAndRemove("2", list);
		list.addToHead(66);
		list.endAnimation();
	}

}
