package AnimatedDataStructure;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTarget;

/* used this to change the label. Couldn't really find any other smart and easy to use way to do it while attaching it to Triggers */
public final class ContinuousAnimation implements TimingTarget{
	private int step; // which step to trigger
	private AnimatedDataStructure anim;

    public ContinuousAnimation(int currentStep, AnimatedDataStructure anim) {
    	step = currentStep; // which step to trigger
    	this.anim = anim;
    }

    @Override
    /*from the implements timingEvent, it overrides the previous one
     *
     * the source will probably be for all the animator objects in rect_list, and this is called each time
     * the framework issues a tick from the Main timer. The fraction just sets the x and y positions for each
     * rectangle class, and schedules a repaint which calls up paintComponent which draws this new position
     *
     * fraction is between 0(start of animation) and 1 (end)
     * */
    public void timingEvent(Animator source, double fraction){
            // do nothing
    }

    @Override
    public void begin(Animator source) {
    	if ((!anim.getContinuousAnimation())&&(step < anim.getSteps().size())) {
    		anim.getPauseButton().setEnabled(true);
        	if (anim.getCurrentStep() > 0) {
        		anim.getPrevButton().setEnabled(true);
        	}
        	if (anim.getCurrentStep() < anim.getSteps().size()) {
        		anim.getNextButton().setEnabled(true);
        	}
    	}
    	if ((anim.getContinuousAnimation())&&(step <= anim.getSteps().size())) {
    		if (step < anim.getSteps().size()) {
    			anim.stepForward();
    		}
    		if (anim.getCurrentStep() == anim.getSteps().size() - 1) {
    			anim.getPauseButton().setText("Play");
    			anim.getPauseButton().setEnabled(false);
    			anim.setContinuousAnimation(false);
    			anim.getNextButton().setEnabled(false);
    			anim.getPrevButton().setEnabled(true);
    		}
    	}
    	
    	// take screenshot
    	if(anim.getScreenshot()) {
			screenShot.takeScreenShot();
		}
    }

    @Override
    public void end(Animator source) {
        // do nothing
    }

    @Override
    public void repeat(Animator source) {
    	// do nothing
    }

    @Override
    public void reverse(Animator source) {
        // do nothing
    }


}

