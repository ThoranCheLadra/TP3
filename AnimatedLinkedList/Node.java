package AnimatedLinkedList;


//a class to represent an individual Rect node in a linked list

public class Node<Rect> {
	
	private Rect data;
	private Node<Rect> next;
	
	public Node(Rect data, Node<Rect> next){
		this.data = data;
		this.next = next;
	}
	
	//check to see if their data is equal, then they are equal
	public boolean equals(Node<Rect> node){
		return node.data.equals(this.data);
	}
	
	//return the node's string data
	public String toString(){
		return (String) this.data;
	}

	public Rect getData() {
		return data;
	}

	public void setData(Rect data) {
		this.data = data;
	}

	public Node<Rect> getNext() {
		return next;
	}

	public void setNext(Node<Rect> next) {
		this.next = next;
	}

}
