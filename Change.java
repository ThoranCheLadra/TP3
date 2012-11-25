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
	
	/* getters only. We don't want any setters */
	
	public String getType() {
		return type;
	}
	
	public Rect getReference() {
		return reference;
	}
	
	/* getters with type casts. If you're using one of these, you be aware of what you'll get */
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
