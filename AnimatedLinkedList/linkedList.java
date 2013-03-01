package AnimatedLinkedList;
import java.awt.Rectangle;
import java.util.ArrayList;

import AnimatedDataStructure.Rect;


//a simple class to represent a LIFO linked-List stack of Rect objects 

public class linkedList {
	
	private Node<Rect> head;
	private int count; //keeping track of its size 
	
	public linkedList(){
		head = null;
		count = 0;
	}
	
	public void addFirst(Rect data){
		head = new Node<Rect>(data, head);
		count++;
	}
	
	public boolean removeFirst(){
		if(head != null){
			head = head.getNext();
			count--;
			return true;
		}else
			return false;
	}
	
	public void remove(Rect target){
		Node<Rect> pos = head;
		Node<Rect> posOld = null;
		Rect posData;
		
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
	
	
	//removes a specific node
	public void removeNode(Node<Rect> prev, Node<Rect> target, int spacing){
		
		Node<Rect> after = target.getNext();
		prev.setNext(after);
		target.setNext(null);
		target = null;
		
		while(after != null){
			Rectangle r = after.getData().getRec();
			r.x -= spacing;
			after = after.getNext();
		}
			
	}
	
	
	public Node<Rect> find(Rect target){
		Node<Rect> pos = head;
		Rect posData;
		
		while(pos != null){
			posData = pos.getData();
			if(posData.equals(target))
				return pos;
			pos = pos.getNext();
		}
		return null;
	}
	
	public Rect get(int a) {
		Node<Rect> pos = head;
		int i = 0;
		while (i < a) {
			pos = pos.getNext();
			i++;
		}
		return pos.getData();
	}
	
	public void set(int a, Rect newRect) {
		Node<Rect> pos = head;
		int i = 0;
		while (i < a) {
			pos = pos.getNext();
			i++;
		}
		pos.setData(newRect);
	}

	
	public void replaceAll(){
		
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
