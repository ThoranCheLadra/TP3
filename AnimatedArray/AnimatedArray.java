package AnimatedArray;

import AnimatedDataStructure.screenShot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.JButton;
import javax.swing.JPanel;

import AnimatedDataStructure.AnimatedDataStructure;
import AnimatedDataStructure.Change;
import AnimatedDataStructure.ChangeLabel;
import AnimatedDataStructure.ContinuousAnimation;
import AnimatedDataStructure.Rect;
import AnimatedDataStructure.Step;
import AnimatedDataStructure.setupGUI;

public final class AnimatedArray extends JPanel implements AnimatedDataStructure {
	
	private static Toolkit toolkit =  Toolkit.getDefaultToolkit ();
	public static Dimension Fullscreen = toolkit.getScreenSize();
	public static int windowWidth = (int) Fullscreen.getWidth();
	public static int windowHeight = (int) Fullscreen.getHeight();
	private JButton nextBtn;																			// getting reference to the button
 	private JButton prevBtn;																			// same as above
	private JButton pauseBtn;
	private LinkedList<Step> steps = new LinkedList<Step>();							// this is where all the steps of animation will be stored																			// we need to to have the reference to the drawing area to repaint it																		// could just REMOVE. Though keeping it with important variables might be a good idea as well. I don't like the fact that the minimum value is 1 second of each animation.
 	private final SwingTimerTimingSource ani_timer = new SwingTimerTimingSource(); // this is for starting the animation
	private int currentStep;																			// the currentStep
 	private boolean continuousAnimation = false; // variable to determine whether to run a continuous animation or not.
 	private int time;
 	private boolean screenshot = true;
 	private List<Rect> rect_list;																// we could just REMOVE this																	// same as above															// this is where all the rectangles which will be drawn are stored
	private String info = ""; 																// a string to display information at each step
	private String flashList = "";
	
	private int arrayLeft;

	public AnimatedArray(int[] arrInt) {
		Fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
        System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        Animator.setDefaultTimingSource(ani_timer);

		arrayLeft = arrInt.length;	//MAX = 85
		rect_list = new ArrayList<Rect>(arrayLeft);
		int rs = rectSize();
		int h = rs;
		int w = rs;
		int x= rs;
		int y= 2*rs;
		time = 100;
		currentStep = 0;
		setOpaque(true);
		
		Step s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("The start of the animation");																	// it wasn't for the "Info" field
		s.setInfo(info);
		for(int i = 0;i<arrayLeft;i++){
			Rect r = new Rect(x, y, w, h, arrInt[i]+"", this);								// create all the Rectangle and add them to rect_list
			rect_list.add(r);
			x += rectSpace();
			flashList+=arrInt[i]+",";
		}
		steps.add(s);	
		flashList = flashList.substring(0, flashList.length() - 1) + ':'; 	//	end of the init part of the flashlist
	}
    
	
	 public AnimatedArray(int[] arrInt, int width, int height){
		windowWidth = width;
		windowHeight = height;

                
                
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(ani_timer);

		arrayLeft = arrInt.length;	//MAX = 85
		rect_list = new ArrayList<Rect>(arrayLeft);
		int rs = rectSize(width);
		int h = rs;
		int w = rs;
		int x= rs;
		int y= 2*rs;
		time = 100;

		currentStep = 0;
		setOpaque(true);
		
		Step s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("");																	// it wasn't for the "Info" field
		s.setInfo(info);
		for(int i = 0;i<arrayLeft;i++){
			Rect r = new Rect(x, y, w, h, arrInt[i]+"", this);								// create all the Rectangle and add them to rect_list
			rect_list.add(r);
			x += rectSpace(rectSize(width));
                        //System.out.println(x);
		}
		steps.add(s);
		
		
		
		
	}
	
	private int rectSpace() {	  	
		return (int) rectSize()/arrayLeft + rectSize()+1;
	}
	
	private int rectSpace(int rectSize) {	  	
		return (int) rectSize/arrayLeft + rectSize+1;
	}
		
	private int rectSize() {	  	
			return (int) ((Fullscreen.getWidth()-(arrayLeft))/(arrayLeft+4)); 	
	}
	
	private int rectSize(int width){
            return (int) ((width-(arrayLeft))/(arrayLeft+4)); 	
	}
	
	public void swap(int a, int b, String info) {
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
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentY", rect_list.get(a).getRec().y, rect_list.get(a).getRec().y+rectSpace()));
		s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), nextBtn);																		// we want this Animator to start straight after the last Animator finishes. This is how it's done just now
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentX", rect_list.get(a).getRec().x, rect_list.get(b).getRec().x)); 
		s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentY", rect_list.get(a).getRec().y+rectSpace(), rect_list.get(a).getRec().y));		
		//rect 2
		s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());															// we want this Animator to start the same time the first Animator in the step does
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentY", rect_list.get(b).getRec().y, rect_list.get(b).getRec().y-rectSpace()));
		s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentX", rect_list.get(b).getRec().x, rect_list.get(a).getRec().x));
		s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), nextBtn);
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
			// We also want to swap the positions of Rects in our rect_list, so swapping would be according to current indexes, rather than initial ones
		Rect tempRect = rect_list.get(b);
		rect_list.set(b, rect_list.get(a));
		rect_list.set(a, tempRect);
		flashList +="-"+a+","+b;
		steps.add(s);																																					// add the new step to steps

	}
	
	 public void modifyLabel(int n, String d, String info) {
		currentStep++;
		Step s = new Step();
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
	 
	 public void setColor(int index, Color c, String info) {
		currentStep++;
		Step s = new Step();
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
		flashList+="-"+index+","+c.getRed()+","+c.getGreen()+","+c.getBlue();	//i,R,G,B
		steps.add(s);
	 }
	 
	 public void setColor(int index, int index1, Color c, String info) {
			currentStep++;
			Step s = new Step();
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
			flashList+="-"+index+","+c.getRed()+","+c.getGreen()+","+c.getBlue();	//i,R,G,B
			steps.add(s);
		}
	 
	 
	/* function to stepBack from one step to a previous one.
	 * Will have to be extended whenever we add another API command
	 */
	private void changeBack(Change tempChange) {
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
	public void setInfo(String info) {
		this.info = info;
	}
	
	/* function to stepForward from one step to the next one.
	 * Will have to be extended whenever we add another API command
	 */
	public void stepForward() {
		currentStep++; // increment the currentStep (because we're moving to the next one)
		if (steps.get(currentStep).getList().size() != 0) { // if there are any Animators inside the step, trigger the first one
			steps.get(currentStep).getList().get(0).start();
		}
	}
	
	public void stepBack() {
		setInfo("");
		for(Change tempChange: steps.get(currentStep).getChanges()) {						// in each Step we are storing the values before the animation, so we
			changeBack(tempChange);															// restore it all like it was before this step and then decrement the
		}																					// currentStep value, as well as call Main.panel.repaint() to redraw
		this.repaint();																// the panel in order to show these changes
		currentStep--;
	}
	
	public void endAnimation(){
        while (currentStep != 0) {														// because of a few slight reasons, when we construct the animation, we end up at
			//setInfo(steps.get(currentStep).getInfo());								// the last step, so we need to return to the initial state
			for(Change tempChange: steps.get(currentStep).getChanges()) {
				changeBack(tempChange);
			}
			currentStep--;
        }
        System.out.println(flashList);
        new setupGUI(this);
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
		Font font = new Font("Arial", Font.PLAIN, (int) 20 * rectSize(windowWidth) / 72);
		g2g.setFont(font);
		/* fill the rectangles and draw a tag next to each one */
		for(Rect r : rect_list){
			g2g.setColor(r.getColor());
			g2g.fillRect(r.getRec().x, r.getRec().y,r.getRec().width,r.getRec().height);
			g2g.setColor(Color.WHITE);
			g2g.drawString(r.getLabel(), r.getRec().x+(rectSize(windowWidth)/2)-10, r.getRec().y+(rectSize(windowWidth)/2));
		}
	} 

	/* Getters/Setters */
	// Fullscreen
	public Dimension getFullscreen() {
		return Fullscreen;
	}
	
	public void setFullscreen(Dimension dimension) {
		Fullscreen = dimension;
	}
	// nextButton
	public JButton getNextButton() {
		return nextBtn;
	}
	
	public void setNextButton(JButton button) {
		nextBtn = button;
	}
	// prevButton
	public JButton getPrevButton() {
		return prevBtn;
	}
	
	public void setPrevButton(JButton button) {
		prevBtn = button;
	}
	// pauseButton
	public JButton getPauseButton() {
		return pauseBtn;
	}
	
	public void setPauseButton(JButton button) {
		pauseBtn = button;
	}
	// Steps
	public LinkedList<Step> getSteps() {
		return steps;
	}
	// Ani_timer
	public SwingTimerTimingSource getAniTimer() {
		return ani_timer;
	}
	// Current Step
	public int getCurrentStep() {
		return currentStep;
	}
	
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep; 
	}
	// ContinuousAnimation
	public boolean getContinuousAnimation() {
		return continuousAnimation;
	}
	
	public void setContinuousAnimation(boolean continuousAnimation) {
		this.continuousAnimation = continuousAnimation;
	}
	// Time / Animations speed
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	// Screenshot
	public boolean getScreenshot() {
		return screenshot;
	}
	
	public void setScreenshot(boolean value) {
		this.screenshot = value;
	}
        
         @Override
        public int getWindowWidth() {
            return windowWidth;
        }

        @Override
        public int getWindowHeight() {
            return windowHeight;
        }
}

