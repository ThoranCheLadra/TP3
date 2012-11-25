import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.triggers.TimingTriggerEvent;
import org.jdesktop.swing.animation.timing.triggers.TriggerUtility;

import javax.swing.JButton;


public class Step {
	
	private LinkedList<Animator> list;
	private List<Change> changes = new ArrayList<Change>();
	private String stepInfo;
	
	public Step() {
		this.list = new LinkedList<Animator>();
		this.stepInfo = "";
	}
	
	public void setInfo(String s) {
		this.stepInfo = s;
	}
	
	public String getInfo() {
		return stepInfo;
	}
	
	public LinkedList<Animator> getList() {
		return this.list;
	}
	
	public List<Change> getChanges() {
		return this.changes;
	}
	
	public void setChanges(List<Change> rect) {
		this.changes = rect;
	}
	
	public void addChanges(Change r) {
		changes.add(r);
	}

	/* after is supposed to be either instanceof JButton or Step or Animator */
	public void addAnimator(Animator item, Object after) {
		if (after == null) {
			// animation will be triggered in a different way
		} else if ( (this.list.peekLast() == null)&&(after instanceof JButton) ) {
			//TriggerUtility.addActionTrigger(after, item);
		} else if (after instanceof Step) {
			// nothing yet
		} else if (after instanceof Animator) {
			TriggerUtility.addTimingTrigger((Animator)after, item, TimingTriggerEvent.START);
		} else {
			TriggerUtility.addTimingTrigger(this.list.peekLast(), item, TimingTriggerEvent.STOP);
		}
		this.list.addLast(item);
	}
	
	public Animator getFirstAnimator() {
		return this.list.peekFirst();
	}
	
	public Animator getLastAnimator() {
		return this.list.peekLast();
	}
}
