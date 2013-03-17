package AnimatedBinaryTree;
/*AnimatedBinaryTree Class
 * 
 *Produces an animated binary tree when an instance is invoked.
 *
 *Authors: Liam Bell, and Gediminas Liekus
 *Version: 2.2*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

	import javax.swing.JButton;
import javax.swing.JPanel;

	import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import AnimatedDataStructure.*;


public class AnimatedBinaryTree extends JPanel implements AnimatedDataStructure{
	
	public static final SwingTimerTimingSource anime_timer = new SwingTimerTimingSource();			// no idea what this is for
	private static int arrayLeft;																	// we could just REMOVE this
	private static Rect r;																			// we could make this local in functions
	private static Step s;																			// same as above
	private List<Rect> rect_list;															// this is where all the rectangles which will be drawn are stored
	private boolean screenshot = false;
	public final static LinkedList<Step> steps = new LinkedList<Step>();							// this is where all the steps of animation will be stored
	static JButton nextBtn;																			// getting reference to the button
	static JButton prevBtn;																			// same as above
	static JButton pauseBtn;
	static JPanel panel;																			// we need to to have the reference to the drawing area to repaint it
	static int currentStep;																			// the currentStep
	static int time=500;																					// could just REMOVE. Though keeping it with important variables might be a good idea as well. I don't like the fact that the minimum value is 1 second of each animation.
	private static String info = ""; 																// a string to display information at each step
	private static Toolkit toolkit =  Toolkit.getDefaultToolkit ();
	public static Dimension Fullscreen = toolkit.getScreenSize();;
	public static int windowWidth = (int) Fullscreen.getWidth();
	public static int windowHeight = (int) Fullscreen.getHeight();
	//private int counter = 0; //counter for paintComponent
	public static boolean continuousAnimation = false; // variable to determine whether to run a continuous animation or not.
		
	//integer variables to store attribute data for rect objects, used later
	private int x, y, w, h;
	
	//integer to keep track of the number of elements allowed without modifying the y integer
	private int n;
		
	public AnimatedBinaryTree(){}

	public AnimatedBinaryTree(int[] arrInt) {
	    System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(anime_timer);

		arrayLeft = arrInt.length;	//MAX = 85
		rect_list = new ArrayList<Rect>(arrayLeft);
		int rs = rectSize();
		h = rs;
		w = rs;
		x= (int)(Fullscreen.getWidth()/2)-w/2;
		y= 10;
		int ymod=(int)((Fullscreen.getHeight()-(h+70))/(Math.log(arrayLeft)/Math.log(2)));
		time = 1;
		currentStep = 0;
		setOpaque(true);
		
		//setupGUI();
			
		rect_list.add(null);
		s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("");																	// it wasn't for the "Info" field
		s.setInfo(info);
		steps.add(s);
		r = new Rect(x, y, w, h, arrInt[0]+"", this);
		rect_list.add(r);
		n = 1;
		for (int i=1; i < arrayLeft; i++) {
			if (i == n){
				y += ymod;
				x=x/2;
				n=(n*2)+1;
			}
			if ((i+1)%2==0) {
				r = new Rect(rect_list.get((i+1)/2).getRec().x-x-10, y, w, h, arrInt[i]+"", this);     // create all the Rectangle and add them to rect_list
			} else {
				r = new Rect(rect_list.get((i+1)/2).getRec().x+x+10, y, w, h, arrInt[i]+"", this);
			}
			rect_list.add(r);
		}
	}
		
	public void endAnimation(){
		while (currentStep != 0) {														// because of a few slight reasons, when we construct the animation, we end up at
			//setInfo(steps.get(currentStep).getInfo());								// the last step, so we need to return to the initial state
			for(Change tempChange: steps.get(currentStep).getChanges()) {
				stepBack(tempChange);
				}
			currentStep--;
			}
		new setupGUI(this);
	}
	
	public void add(int i){
		Rect r;
		if (rect_list.size()%2==0) {
			r = new Rect(rect_list.get(rect_list.size()/2).getRec().x-x-10, y, w, h, i+"", this);     // create all the Rectangle and add them to rect_list
		} else {
			r = new Rect(rect_list.get(rect_list.size()/2).getRec().x+x+10, y, w, h, i+"", this);
		}
		rect_list.add(r);
    	panel.repaint();
    }
	        
		private int rectSpace() {	  	
			return (int) rectSize()/arrayLeft + rectSize()+1;
		}
			
		private int rectSize() {	  	
				return (int) ((Fullscreen.getWidth()-(arrayLeft))/(arrayLeft+4)); 	
		}
		
		public List<Rect> getRectList() {
			return rect_list;
		}
		
		//a swap method where the user defines the timing of the animation.
		public void swap(int a, int b, long t, String info){
			currentStep++;																																					// increment currentStep
			s = new Step();																																					// create a new step
			// Create animation for showing information on what's happening
			String information;
			if (info == "") {
				information = "Swapping index " + a + " (" + rect_list.get(a).getLabel() + ") " + " with index " + b + " (" + rect_list.get(b).getLabel() + ")";			// create the string
			} else {
				information = info;
			}
			s.setInfo(information);																																			// add it to the step
			// Change information
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());																		// create an animator for this, which we could use to trigger this change
			s.getLastAnimator().addTarget(new ChangeLabel(this, information,this));																				// create a timing target to change the label when the animation starts	
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
			// Trigger for a continuous animation
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
			// Log the changes in the Step
			s.addChanges(new Change("swap", rect_list.get(a), rect_list.get(a).getRec().x, rect_list.get(a).getRec().y));
			s.addChanges(new Change("swap", rect_list.get(b), rect_list.get(b).getRec().x, rect_list.get(b).getRec().y));
			// Apply the changes to rect_list
				// We swap the position of 2 Rects in the animation, so we do this here
			int x = rect_list.get(a).getRec().x;
			rect_list.get(a).getRec().x = rect_list.get(b).getRec().x;
			rect_list.get(b).getRec().x = x;
			int y = rect_list.get(a).getRec().y;
			rect_list.get(a).getRec().y = rect_list.get(b).getRec().y;
			rect_list.get(b).getRec().y = y;
				// We also want to swap the positions of Rects in our rect_list, so swapping would be according to current indexes, rather than initial ones
			Rect tempRect = rect_list.get(b);
			rect_list.set(b, rect_list.get(a));
			rect_list.set(a, tempRect);
			steps.add(s);																																					// add the new step to steps
			}
		
		//a swap method that uses default value for t.
		public void swap(int a, int b, String info){
			time=500;
			currentStep++;																																					// increment currentStep
			Step s = new Step();																																					// create a new step
			// Create animation for showing information on what's happening
			String information;
			if (info == "") {
				information = "Swapping index " + a + " (" + rect_list.get(a).getLabel() + ") " + " with index " + b + " (" + rect_list.get(b).getLabel() + ")";			// create the string
			} else {
				information = info;
			}
			s.setInfo(information);																																			// add it to the step
			// Change information
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());																		// create an animator for this, which we could use to trigger this change
			s.getLastAnimator().addTarget(new ChangeLabel(this, information, this));																				// create a timing target to change the label when the animation starts	
			// Create the Animators and PropertySetters (what should the animation change the property from what to what) for swapping rects
			s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());															// we want this Animator to start the same time the first Animator in the step does															
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentY", rect_list.get(a).getRec().y, rect_list.get(b).getRec().y));
			s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), nextBtn);																		// we want this Animator to start straight after the last Animator finishes. This is how it's done just now
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentX", rect_list.get(a).getRec().x, rect_list.get(b).getRec().x)); 		
			//rect 2
			s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());															// we want this Animator to start the same time the first Animator in the step does
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentY", rect_list.get(b).getRec().y, rect_list.get(a).getRec().y));
			s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentX", rect_list.get(b).getRec().x, rect_list.get(a).getRec().x));
			// Trigger for a continuous animation
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
			// Log the changes in the Step
			s.addChanges(new Change("swap", rect_list.get(a), rect_list.get(a).getRec().x, rect_list.get(a).getRec().y, a, b));
			s.addChanges(new Change("swap", rect_list.get(b), rect_list.get(b).getRec().x, rect_list.get(b).getRec().y, b, a));
			// Apply the changes to rect_list
				// We swap the position of 2 Rects in the animation, so we do this here
			setColor(a, Color.RED, "");
			setColor(b, Color.RED, "");
			int x = rect_list.get(a).getRec().x;
			rect_list.get(a).getRec().x = rect_list.get(b).getRec().x;
			rect_list.get(b).getRec().x = x;
			int y = rect_list.get(a).getRec().y;
			rect_list.get(a).getRec().y = rect_list.get(b).getRec().y;
			rect_list.get(b).getRec().y = y;
				// We also want to swap the positions of Rects in our rect_list, so swapping would be according to current indexes, rather than initial ones
			Rect tempRect = rect_list.get(b);
			rect_list.set(b, rect_list.get(a));
			rect_list.set(a, tempRect);
			steps.add(s);																																					// add the new step to steps
			setColor(a, Color.BLUE, "");
			setColor(b, Color.BLUE, "");
		}
		
		 public void modifyLabel(int n, String d, String info){
			currentStep++;
			s = new Step();
			// Animate information
			String information;
			if (info == "") {
				information = "Changing index " + n + " label from " + rect_list.get(n).getLabel() + " to " + d;
			} else {
				information = info;
			}
			s.setInfo(information);
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new ChangeLabel(this, information, this));
			// Animate change of label
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
			s.getLastAnimator().addTarget(new ChangeLabel(rect_list.get(n), d, this));
			// Trigger for a continuous animation
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
			// Log the Changes in Step
			s.addChanges(new Change("modifyLabel", rect_list.get(n), rect_list.get(n).getLabel()));
			// Apply the changes to rect_list
			rect_list.get(n).setLabel(d);
			steps.add(s);	  
		 } 
		 
		 public void setColor(int index, Color c, String info){
			currentStep++;
			s = new Step();
			// Animate information
			String information;
			if (info == "") {
				information = "Changing index " + index + " color from " + rect_list.get(index).getColor() + " to " + c;
			} else {
				information = info;
			}
			s.setInfo(information);
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new ChangeLabel(this, information, this));
			// Animate change of color
			s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
			s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index), "colorDraw", rect_list.get(index).getColor(), c));
			// Trigger for a continuous animation
			s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
			s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
			// Log the Changes in Step
			s.addChanges(new Change("color", rect_list.get(index), rect_list.get(index).getColor()));
			// Apply the changes to rect_list
			rect_list.get(index).setColor(c);
			steps.add(s);
		}
		 
		 public void setColor(int index, int index1, Color c, String info){
				currentStep++;
				s = new Step();
				// Animate information
				String information;
				if (info == "") {
					information = "Changing index " + index + " color from " + rect_list.get(index).getColor() + " to " + c;
				} else {
					information = info;
				}
				s.setInfo(information);
				s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
				s.getLastAnimator().addTarget(new ChangeLabel(this, information, this));
				// Animate change of color
				s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
				s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index), "colorDraw", rect_list.get(index).getColor(), c));
				// Log the Changes in Step
				s.addChanges(new Change("color", rect_list.get(index), rect_list.get(index).getColor()));
				// Apply the changes to rect_list
				rect_list.get(index).setColor(c);
				
				// Animate change of color
				s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
				s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index1), "colorDraw", rect_list.get(index1).getColor(), c));
				// Trigger for a continuous animation
				s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
				s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
				// Log the Changes in Step
				s.addChanges(new Change("color", rect_list.get(index1), rect_list.get(index1).getColor()));
				// Apply the changes to rect_list
				rect_list.get(index1).setColor(c);
				steps.add(s);
			}
		 
		 public void stepForward() {
			//Main.panel.repaint();
				currentStep++;																		// increment the currentStep (because we're moving to the next one)
				if (steps.get(currentStep).getList().size() != 0) {									// if there are any Animators inside the step, trigger the first one
					steps.get(currentStep).getList().get(0).start();
				}
				
				for(Change tempChange : steps.get(currentStep).getChanges()) {
					if (tempChange.getType() == "swap") {
						rect_list.set(tempChange.getNewIndex(), tempChange.getReference());
						panel.repaint();
					}
				}
		 }
		 
		 
		/* function to stepBack from one step to a previous one.
		 * Will have to be extended whenever we add another API command
		 */
		public void stepBack(Change tempChange) {
			if (tempChange.getType() == "swap") {
				// just restore coordinates
				rect_list.set(tempChange.getOldIndex(), tempChange.getReference());
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
		public void setInfo(String info) {
			AnimatedBinaryTree.info = info;
		} 

		/*an overriden method from JFrame: paints the actual rectangle on the screen. It is called each time timingEvent calls repaint() */
		protected void paintComponent(Graphics g){
			Graphics g2g = (Graphics2D) g;
			((Graphics2D) g2g).setBackground(Color.WHITE);

			for(int x=0;x<10;x++){
				g2g.clearRect(0, 0, getWidth(), getHeight()); //clear all rectangle
			}

			//g2g.setColor(Color.BLUE);
			
			/*fill the rectangles and draw a tag next to each one */
			for (int i=1; i<rect_list.size(); i++){
				if (rect_list.get(i)!=null){
					g2g.setColor(rect_list.get(i).getColor());
					if (i>1){
						g2g.drawLine(rect_list.get(i/2).getRec().x+(rect_list.get(i/2).getRec().width/2), (rect_list.get(i/2).getRec().y)+(rect_list.get(i/2).getRec().height), rect_list.get(i).getRec().x+(rect_list.get(i).getRec().width/2), rect_list.get(i).getRec().y);
					}
					g2g.fillRect(rect_list.get(i).getRec().x, rect_list.get(i).getRec().y,rect_list.get(i).getRec().width,rect_list.get(i).getRec().height);
					g2g.setColor(Color.YELLOW);
					g2g.drawString(rect_list.get(i).getLabel(), rect_list.get(i).getRec().x+7, rect_list.get(i).getRec().y+15);
					g2g.drawString(info,300,60);
				}
			}

		}

		@Override
		public void stepBack() {
			setInfo("");
			for(Change tempChange: steps.get(currentStep).getChanges()) {						// in each Step we are storing the values before the animation, so we
				stepBack(tempChange);															// restore it all like it was before this step and then decrement the
			}																					// currentStep value, as well as call Main.panel.repaint() to redraw
			this.repaint();																// the panel in order to show these changes
			currentStep--;
		}

		@Override
		public Dimension getFullscreen() {
			return Fullscreen;
		}

		@Override
		public void setFullscreen(Dimension dimension) {
			Fullscreen = dimension;
		}

		@Override
		public JButton getNextButton() {
			return nextBtn;
		}

		@Override
		public void setNextButton(JButton button) {
			nextBtn = button;
		}

		@Override
		public JButton getPrevButton() {
			return prevBtn;
		}

		@Override
		public void setPrevButton(JButton button) {
			prevBtn = button;
		}

		@Override
		public JButton getPauseButton() {
			return pauseBtn;
		}

		@Override
		public void setPauseButton(JButton button) {
			pauseBtn = button;
		}

		@Override
		public LinkedList<Step> getSteps() {
			return steps;
		}

		@Override
		public SwingTimerTimingSource getAniTimer() {
			return anime_timer;
		}

		@Override
		public int getCurrentStep() {
			return currentStep;
		}

		@Override
		public int getWindowWidth() {
			return windowWidth;
		}

		@Override
		public int getWindowHeight() {
			return windowHeight;
		}

		@Override
		public void setCurrentStep(int currentStep) {
			this.currentStep = currentStep;
		}

		@Override
		public boolean getContinuousAnimation() {
			return continuousAnimation;
		}

		@Override
		public void setContinuousAnimation(boolean continuousAnimation) {
			this.continuousAnimation = continuousAnimation;
		}

		@Override
		public int getTime() {
			return time;
		}

		@Override
		public void setTime(int time) {
			this.time = time;
		}

		@Override
		public boolean getScreenshot() {
			return screenshot;
		}

		@Override
		public void setScreenshot(boolean value) {
			this.screenshot = value;
		}

	}
