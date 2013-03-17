package AnimatedLinkedList;
import java.awt.Rectangle;
import java.util.ArrayList;

import AnimatedDataStructure.Rect;


//a simple class to represent a LIFO linked-List stack of Rect objects 

public class linkedList {

	private Node<Rect> head;
	private int count; //keeping track of its size 

	//constructor
	public linkedList(){
		head = null;
		count = 0;
	}

	//add to head of list
	public void addFirst(Rect data){
		head = new Node<Rect>(data, head);
		count++;
	}
	
	//remove the first element, returns false if fails
	public boolean removeFirst(){
		if(head != null){
			head = head.getNext();
			count--;
			return true;
		}else
			return false;
	}

	//remove a specific node with the target supplied (here rect is the data)
	public void remove(Rect target){
		Node<Rect> pos = head;
		Node<Rect> posOld = null;
		Rect posData;
		//while a node exists, acquire its data and remove it
		//maintains a pointer to the old (or prev) location, to point to the node succeeding it
		while(pos != null) {
			posData = pos.getData();
			if(posData.equals(target))
				if (posOld == null) {
					head = pos.getNext();
				} else {
					posOld.setNext(pos.getNext());
				}
			posOld = pos;
			pos = pos.getNext();
		}
	}


	//removes the node from the linked list by the correct spacing
	public void removeNode(Node<Rect> prev, Node<Rect> target, int spacing){
		//get the next node and set the previous node to point to it
		Node<Rect> after = target.getNext();
		prev.setNext(after);
		target.setNext(null);
		target = null;
		
		//shift n elements after the target node left 1 spacing
		while(after != null){
			Rectangle r = after.getData().getRec();
			r.x -= spacing;
			after = after.getNext();
		}

	}

	//helper function for printing the linked list
	public void printList(){
		Node<Rect> pos = head;
		while(pos != null){
			System.out.println(pos.getData().getLabel());
			pos = pos.getNext();
		}
	}

	//translates the linked-list into an arraylist
	public ArrayList<Rect> toArray(){
		Node<Rect> pos = head;
		ArrayList<Rect> arr = new ArrayList<Rect>();

		while(pos != null){
			arr.add(pos.getData());
			pos = pos.getNext();
		}
		return arr;
	}


	//check if the list is empty
	public boolean isEmpty(){
		return count == 0;
	}

	public int getCount() {
		return count;
	}

	public Node<Rect> getHead() {
		return head;
	}

}
