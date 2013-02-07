import java.util.ArrayList;


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



}
