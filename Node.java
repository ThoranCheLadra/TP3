

//a class to represent an individual Rect node in a linked list

public class Node<Rect> {
	
	private Rect data;
	private Node<Rect> next;
	
	public Node(Rect data, Node<Rect> next){
		this.data = data;
		this.next = next;
		
	}
	
	public boolean equals(Node<Rect> node){
		if(node.data instanceof String){
			return node.data.equals(this.data);
		}else if(node.data instanceof Integer){
			return node.data == this.data;
		}else{
			return false;
		}
	}
	

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
