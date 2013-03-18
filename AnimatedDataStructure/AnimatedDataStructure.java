package AnimatedDataStructure;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.JButton;

import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

public abstract interface AnimatedDataStructure {
	
	void setColor(int index, Color toColor, String info);
	
	void setInfo(String info);
	
	void stepForward();
	
	void stepBack();
	
	void endAnimation();
	/* JPanel */
	void repaint();
	
	/* Getters/Setters */
	// Fullscreen
	Dimension getFullscreen();
	
	void setFullscreen(Dimension dimension);
	// nextButton
	JButton getNextButton();
	
	void setNextButton(JButton button);
	// prevButton
	JButton getPrevButton();
	
	void setPrevButton(JButton button);
	// pauseButton
	JButton getPauseButton();
	
	void setPauseButton(JButton button);
	// Steps
	LinkedList<Step> getSteps();
	// Ani_timer
	SwingTimerTimingSource getAniTimer();
	// Current Step
	int getCurrentStep();
        
        int getWindowWidth();
        
        int getWindowHeight();
	
	void setCurrentStep(int currentStep);
	// ContinuousAnimation
	boolean getContinuousAnimation();
	
	void setContinuousAnimation(boolean continuousAnimation);
	// Time / Animations speed
	int getTime();
	
	void setTime(int time);
	// Screenshot
	boolean getScreenshot();
	
	void setScreenshot(boolean value);
	
}
