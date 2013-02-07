import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.triggers.TimingTriggerEvent;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;
import org.jdesktop.swing.animation.timing.triggers.TriggerUtility;



//example template class that displays a linked list, some classes have been left in from AnimatedArray, whilst others
//ommited that clashed. currently animation hasn't been implemented

public final class AnimatedLinkedList extends JPanel {


	public static final SwingTimerTimingSource ani_timer = new SwingTimerTimingSource();			// no idea what this is for
	private static int arrayLeft;																	// we could just REMOVE this
	private static Rect r;																			// we could make this local in functions
	private static Step s;																			// same as above
	private static linkedList rect_list;															// this is where all the rectangles which will be drawn are stored
	public final static LinkedList<Step> steps = new LinkedList<Step>();							// this is where all the steps of animation will be stored
	static JButton nextBtn;																			// getting reference to the button
	static JButton prevBtn;																			// same as above
	static JButton pauseBtn;
	static JPanel panel;																			// we need to to have the reference to the drawing area to repaint it
	static int currentStep;																			// the currentStep
	static int t;																					// could just REMOVE. Though keeping it with important variables might be a good idea as well. I don't like the fact that the minimum value is 1 second of each animation.
	private static String info = ""; 																// a string to display information at each step
	public static Dimension Fullscreen;
	private int counter = 0; //counter for paintComponent
	public static boolean continuousAnimation = false; // variable to determine whether to run a continuous animation or not.


	public AnimatedLinkedList(){

	}

	public AnimatedLinkedList(int[] arr){
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		Fullscreen = toolkit.getScreenSize();
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(ani_timer);

		arrayLeft = arr.length;	//MAX = 85
		
		rect_list = new linkedList(); //a linked list of rectangles

		//the scaling/resizing code is not working currently
		int rs = rectSize();
		int h = rs;
		int w = rs;
		int x= arrayLeft*rs+rectSpace();
		int y= 2*rs;
		t = 1;
		currentStep = 0;
		setOpaque(true);

		s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("");																	// it wasn't for the "Info" field
		s.setInfo(info);


		//as its a stack, items need to be added in reverse to begin with to replicate its appearance

		for(int i = 0;i<arrayLeft;i++){
			r = new Rect(x, y, w, h, arr[i]+"");								// create all the Rectangle and add them to rect_list
			rect_list.addFirst(r);
			x -= rectSpace();
		}
		steps.add(s);

	}


	public void endAnimation(){
		while (currentStep != 0) {														// because of a few slight reasons, when we construct the animation, we end up at
			//setInfo(steps.get(currentStep).getInfo());								// the last step, so we need to return to the initial state
			for(Change tempChange: steps.get(currentStep).getChanges()) {
				stepBack(tempChange);
			}
			currentStep--;
		}
	}

	private int rectSpace() {	  	
		return (int) rectSize()/arrayLeft + rectSize()+1;
	}

	private int rectSize() {	  	
		return (int) ((Fullscreen.getWidth()-(arrayLeft))/(arrayLeft+4)); 	
	}

	public linkedList getRectList() {
		return rect_list;
	}



	/* function to stepBack from one step to a previous one.
	 * Will have to be extended whenever we add another API command
	 */
	public static void stepBack(Change tempChange) {
		if (tempChange.getType() == "swap") {
			// just restore coordinates
			tempChange.getReference().getRec().x = tempChange.getPoint().x;
			tempChange.getReference().getRec().y = tempChange.getPoint().y;
		} else if (tempChange.getType() == "color") {
			// just restore the color
			tempChange.getReference().setColor(tempChange.getColor());
		} else if (tempChange.getType() == "modifyLabel") {
			// just restore the label
			tempChange.getReference().setLabel(tempChange.getLabel());
		}
	}

	/* function for changing the information on what's happening */
	public static void setInfo(String info) {
		AnimatedLinkedList.info = info;
	} 



	/*an overriden method from JFrame: paints the actual rectangle on the screen. It is called each time timingEvent calls repaint() */
	protected void paintComponent(Graphics g){

		//counter++;

		Graphics g2g = (Graphics2D) g;
		((Graphics2D) g2g).setBackground(Color.WHITE);

		for(int x=0;x<10;x++){
			g2g.clearRect(0, 0, getWidth(), getHeight()); //clear all rectangles
		}

		//g2g.setColor(Color.BLUE);
		g2g.setColor(Color.BLUE);
		g2g.drawString(info,(int)(Fullscreen.width/4),(int)(Fullscreen.height*3/4));
		Font font = new Font("Arial", Font.PLAIN, (int) 20 * rectSize() / 72);
		g2g.setFont(font);
		
		
		//the linked list is converted into an array so that it can be worked with
		ArrayList<Rect> arr = new ArrayList<Rect>();
		arr = rect_list.toArray();

		/* fill the rectangles and draw a tag next to each one */
		
		for(Rect r : arr){
			g2g.setColor(r.getColor());
			g2g.fillRect(r.getRec().x, r.getRec().y,r.getRec().width,r.getRec().height);
			g2g.setColor(Color.WHITE);
			g2g.drawString(r.getLabel(), r.getRec().x+(rectSize()/2)-10, r.getRec().y+(rectSize()/2));
		}

		//draw pointers, including final NULL pointer
		for(int i=0;i<arr.size()-1;i++){
			g2g.setColor(Color.BLACK);
			g2g.drawLine((arr.get(i).getRec().x+40)+arr.get(i).getRec().width/2, arr.get(i).getRec().y+20+arr.get(i).getRec().height/2, arr.get(i+1).getRec().x-40 + arr.get(i+1).getRec().width/2, arr.get(i+1).getRec().y+20+arr.get(i+1).getRec().height/2);
			g2g.fillOval(arr.get(i+1).getRec().x-40 + arr.get(i+1).getRec().width/2, arr.get(i+1).getRec().y+15+arr.get(i+1).getRec().height/2, 10, 10);
		}

		g2g.drawLine((arr.get(arr.size()-1).getRec().x+40)+arr.get(arr.size()-1).getRec().width/2, arr.get(arr.size()-1).getRec().y+20+arr.get(arr.size()-1).getRec().height/2,      arr.get(arr.size()-1).getRec().x+rectSpace()+30,arr.get(arr.size()-1).getRec().y+20+arr.get(arr.size()-1).getRec().height/2);
		g2g.setColor(Color.RED);
		g2g.fillOval(arr.get(arr.size()-1).getRec().x+rectSpace()+30,arr.get(arr.size()-1).getRec().y+20+arr.get(arr.size()-1).getRec().height/2-5, 10, 10);

		/* draw the information */
		g2g.setColor(Color.BLUE);
		g2g.drawString(info,(int)(Fullscreen.width/4),(int)(Fullscreen.height*3/4));

	}


}

