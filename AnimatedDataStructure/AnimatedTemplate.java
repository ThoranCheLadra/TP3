package AnimatedDataStructure;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.util.List;
import java.util.LinkedList;

import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AnimatedTemplate extends JPanel implements AnimatedDataStructure {
	static final long serialVersionUID = 1L;
	protected static Toolkit toolkit =  Toolkit.getDefaultToolkit ();
	public static Dimension Fullscreen = toolkit.getScreenSize();
	public static int windowWidth = (int) Fullscreen.getWidth();
	public static int windowHeight = (int) Fullscreen.getHeight();
	protected JButton nextBtn;																			// getting reference to the button
	protected JButton prevBtn;																			// same as above
	protected JButton pauseBtn;
	protected LinkedList<Step> steps = new LinkedList<Step>();							// this is where all the steps of animation will be stored																			// we need to to have the reference to the drawing area to repaint it																		// could just REMOVE. Though keeping it with important variables might be a good idea as well. I don't like the fact that the minimum value is 1 second of each animation.
	protected final SwingTimerTimingSource ani_timer = new SwingTimerTimingSource(); // this is for starting the animation
	protected int currentStep;																			// the currentStep
	protected boolean continuousAnimation = false; // variable to determine whether to run a continuous animation or not.
 	protected int time;
 	protected boolean screenshot = true;
 	protected List<Rect> rect_list;																// we could just REMOVE this																	// same as above															// this is where all the rectangles which will be drawn are stored
 	protected String info = ""; 																// a string to display information at each step
	
 	protected int arrayLeft;
	
	protected int rectSpace() {	  	
		return (int) rectSize()/arrayLeft + rectSize()+1;
	}
	
	protected int rectSpace(int rectSize) {	  	
		return (int) rectSize/arrayLeft + rectSize+1;
	}
		
	protected int rectSize() {	  	
		return (int) ((windowWidth-(arrayLeft))/(arrayLeft+4)); 	
	}
	
	protected int rectSize(int width){
		return (int) ((width-(arrayLeft))/(arrayLeft+4)); 	
	}
	
	/* function for changing the information on what's happening */
	public void setInfo(String info) {
		this.info = info;
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

