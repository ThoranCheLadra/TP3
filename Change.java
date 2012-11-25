import java.awt.Point;
import java.awt.Color;

public class Change {
	private String type; // type of change
	private Rect reference; // index of the object that was changed
	private Object old; // position before the change
	
	/* Constructors for creating changes with position changes */
	public Change(String type, Rect reference, Point position) {
		this.type = type;
		this.reference = reference;
		this.old = position;
	}
	
	public Change(String type, Rect reference, int x, int y) {
		this.type = type;
		this.reference = reference;
		this.old = new Point(x, y);
	}
	
	/* Constructors for creating changes with label changes */
	public Change(String type, Rect reference, String label) {
		this.type = type;
		this.reference = reference;
		this.old = label;
	}
	
	/* Constructors for creating changes with Color changes */
	public Change(String type, Rect reference, Color c) {
		this.type = type;
		this.reference = reference;
		this.old = c;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String t) {
		this.type = t;
	}
	
	public Rect getReference() {
		return reference;
	}
	
	public void setReference(Rect reference) {
		this.reference = reference;
	}
	
	public Point getPoint() {
		return (Point) old;
	}
	
	public Color getColor() {
		return (Color) old;
	}
	
	public String getLabel() {
		return (String) old;
	}
}
