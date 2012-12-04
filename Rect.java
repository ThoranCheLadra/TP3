import java.awt.Color;
import java.awt.Rectangle;

/*class representing a rectangle (array index) , could have string, integer data associations etc */
public class Rect {

		private Rectangle rec;
		private String label;									// this is where the actual value of the element will be stored. We could change it to some other type, like Object, but right now String is more than enough.
		private Color color;

		public Rect(int x,int y,int w,int h, String label){
			rec = new Rectangle(x,y,w,h);
			color = Color.BLUE;
			this.label = label;
		}

		public Rectangle getRec(){
			return rec;
		}
		
		public void setRec(Rectangle rec) {
			this.rec = rec;
		}
		
		public String getLabel() {
			return label;
		}
		
		public void setLabel(String label) {
			this.label = label;
		}
		
		public void setColor(Color c){
			this.color = c;
		}
		
		/* separate method to enable color change during animation */
		public void setColorDraw(Color c){
			this.color = c;
			AnimatedArray.panel.repaint();
		}

		public Color getColor() {
			return color;
		}
		
		public void setCurrentX(int currentX) {
			this.rec.x = currentX;
			AnimatedArray.panel.repaint();
		}
		

		public void setCurrentY(int currentY) {
			this.rec.y = currentY;
			AnimatedArray.panel.repaint();
		}

	}