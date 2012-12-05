import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTarget;

/* used this to change the label. Couldn't really find any other smart and easy to use way to do it while attaching it to Triggers */
public final class ContinuousAnimation implements TimingTarget{
	private int step; // which step to trigger

    public ContinuousAnimation(int currentStep) {
    	step = currentStep; // which step to trigger
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
    	if ((AnimatedArray.continuousAnimation)&&(step < AnimatedArray.steps.size())) {
    		AnimatedArray.steps.get(step).getList().get(0).start();
    		AnimatedArray.currentStep++;
    		if (AnimatedArray.currentStep == AnimatedArray.steps.size() - 1) {
    			AnimatedArray.pauseBtn.setText("Play");
    			AnimatedArray.pauseBtn.setEnabled(false);
    			AnimatedArray.continuousAnimation = false;
    			AnimatedArray.nextBtn.setEnabled(false);
				AnimatedArray.prevBtn.setEnabled(true);
    		}
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

