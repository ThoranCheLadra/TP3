import java.awt.BorderLayout;
import java.awt.Color;
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


public final class AnimatedArray extends JPanel {



	public static final SwingTimerTimingSource ani_timer = new SwingTimerTimingSource();			// no idea what this is for
	private static int arrayLeft;																	// we could just REMOVE this
	private static Rect r;																			// we could make this local in functions
	private static Step s;																			// same as above
	private static List<Rect> rect_list;															// this is where all the rectangles which will be drawn are stored
	public final static LinkedList<Step> steps = new LinkedList<Step>();							// this is where all the steps of animation will be stored
	static JButton nextBtn;																		// getting reference to the button
 	static JButton prevBtn;																			// same as above
	static JPanel panel;																			// we need to to have the reference to the drawing area to repaint it
	static int currentStep;																			// the currentStep
	static int t;																					// could just REMOVE. Though keeping it with important variables might be a good idea as well. I don't like the fact that the minimum value is 1 second of each animation.
	private static String info = ""; 																// a string to display information at each step
	public static Dimension Fullscreen;
	private int counter = 0; //counter for paintComponent
	
	
	public AnimatedArray(){
            
        }

	public AnimatedArray(int[] arrInt){
                Toolkit toolkit =  Toolkit.getDefaultToolkit ();
                Fullscreen = toolkit.getScreenSize();
                System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(ani_timer);

		arrayLeft = arrInt.length;	//MAX = 85
		rect_list = new ArrayList<Rect>(arrayLeft);
		int rs = rectSize();
		int h = rs;
		int w = rs;
		int x= rs;
		int y= 2*rs;
		t = 1;
		currentStep = 0;
		setOpaque(true);
		
		s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("");																	// it wasn't for the "Info" field
		s.setInfo(info);
		for(int i = 0;i<arrayLeft;i++){
			r = new Rect(x, y, w, h, arrInt[i]+"");								// create all the Rectangle and add them to rect_list
			rect_list.add(r);
			x += rectSpace();
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
	
	public void swapRect(int a, int b, long t){
		currentStep++;																																					// increment currentStep
		s = new Step();																																					// create a new step
		// Create animation for showing information on what's happening
		String information = "Swapping index " + a + " (" + rect_list.get(a).getLabel() + ") " + " with index " + b + " (" + rect_list.get(b).getLabel() + ")";			// create the string
		s.setInfo(information);																																			// add it to the step
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);																		// create an animator for this, which we could use to trigger this change
		s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, information));																					// create a timing target to change the label when the animation starts
		// Create the Animators and PropertySetters (what should the animation change the property from what to what) for swapping rects
		
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());															// we want this Animator to start the same time the first Animator in the step does															
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentY", rect_list.get(a).getRec().y, rect_list.get(a).getRec().y+rectSpace()));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);																		// we want this Animator to start straight after the last Animator finishes. This is how it's done just now
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentX", rect_list.get(a).getRec().x, rect_list.get(b).getRec().x)); 
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentY", rect_list.get(a).getRec().y+rectSpace(), rect_list.get(a).getRec().y));
		
		//rect 2
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());															// we want this Animator to start the same time the first Animator in the step does
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentY", rect_list.get(b).getRec().y, rect_list.get(b).getRec().y-rectSpace()));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentX", rect_list.get(b).getRec().x, rect_list.get(a).getRec().x));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentY", rect_list.get(b).getRec().y-rectSpace(), rect_list.get(b).getRec().y));
		// Change the information back to empty string ""
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);																		// do this after the last Animator in the Step finishes
		//s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, ""));
		// Log the changes in the Step
		s.addChanges(new Change("swap", rect_list.get(a), rect_list.get(a).getRec().x, rect_list.get(a).getRec().y));
		s.addChanges(new Change("swap", rect_list.get(b), rect_list.get(b).getRec().x, rect_list.get(b).getRec().y));
		// Apply the changes to rect_list
			// We swap the position of 2 Rects in the animation, so we do this here
		int x = rect_list.get(a).getRec().x;
		rect_list.get(a).getRec().x = rect_list.get(b).getRec().x;
		rect_list.get(b).getRec().x = x;
			// We also want to swap the positions of Rects in our rect_list, so swapping would be according to current indexes, rather than initial ones
		Rect tempRect = rect_list.get(b);
		rect_list.set(b, rect_list.get(a));
		rect_list.set(a, tempRect);
		steps.add(s);																																					// add the new step to steps
	}
	
	 public void modifyLabel(int n, String d){
		currentStep++;
		s = new Step();
		// Animate information
		String information = "Changing index " + n + " label from " + rect_list.get(n).getLabel() + " to " + d;
		s.setInfo(information);
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, information));
		// Animate change of label
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
		s.getLastAnimator().addTarget(new TimingTargetChangeLabel(rect_list.get(n), d));
		// Change the information back to empty string ""
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		//s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, ""));
		// Log the Changes in Step
		s.addChanges(new Change("modifyLabel", rect_list.get(n), rect_list.get(n).getLabel()));
		// Apply the changes to rect_list
		rect_list.get(n).setLabel(d);
		steps.add(s);	  
	 } 
	 
	 public void setRectColor(int index, Color c){
		currentStep++;
		s = new Step();
		// Animate information
		String information = "Changing index " + index + " color from " + rect_list.get(index).getColor() + " to " + c;
		s.setInfo(information);
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, information));
		// Animate change of color
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index), "colorDraw", rect_list.get(index).getColor(), c));
		// Change the information back to empty string ""
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
		//s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, ""));
		// Log the Changes in Step
		s.addChanges(new Change("color", rect_list.get(index), rect_list.get(index).getColor()));
		// Apply the changes to rect_list
		rect_list.get(index).setColor(c);
		steps.add(s);
	}
	 
	 public void setRectColor(int index, int index1, Color c){
			currentStep++;
			s = new Step();
			// Animate information
			String information = "Changing index " + index + " color from " + rect_list.get(index).getColor() + " to " + c;
			s.setInfo(information);
			s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, information));
			// Animate change of color
			s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index), "colorDraw", rect_list.get(index).getColor(), c));
			// Change the information back to empty string ""
			s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
			//s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, ""));
			// Log the Changes in Step
			s.addChanges(new Change("color", rect_list.get(index), rect_list.get(index).getColor()));
			// Apply the changes to rect_list
			rect_list.get(index).setColor(c);
			
			s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, information));
			// Animate change of color
			s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index1), "colorDraw", rect_list.get(index1).getColor(), c));
			// Change the information back to empty string ""
			s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.MILLISECONDS).build(), nextBtn);
			//s.getLastAnimator().addTarget(new TimingTargetChangeLabel(this, ""));
			// Log the Changes in Step
			s.addChanges(new Change("color", rect_list.get(index1), rect_list.get(index1).getColor()));
			// Apply the changes to rect_list
			rect_list.get(index1).setColor(c);
			steps.add(s);
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
		AnimatedArray.info = info;
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
		
		/* fill the rectangles and draw a tag next to each one */
		for(Rect r : rect_list){
			g2g.setColor(r.getColor());
			g2g.fillRect(r.getRec().x, r.getRec().y,r.getRec().width,r.getRec().height);
			g2g.setColor(Color.WHITE);
			g2g.drawString(r.getLabel(), r.getRec().x, r.getRec().y+10);
		}
		/* draw the information */
		g2g.setColor(Color.BLUE);
		g2g.drawString(info,(int)(Fullscreen.width/4),(int)(Fullscreen.height*3/4));
		
		
		/*
		 * would take a screenshot here, the counter value really should depend on the speed of a continuous animation
		 * 
		 *
		if(counter == 100){
			screenShot.takeScreenShot(panel);
			counter = 0;
		}
		*/
	} 

}

