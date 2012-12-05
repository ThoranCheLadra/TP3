import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.triggers.TimingTriggerEvent;
import org.jdesktop.swing.animation.timing.triggers.TriggerUtility;

import javax.swing.JButton;


public class Step {
	
	private LinkedList<Animator> list;										// list of Animators. We don't really need to store all Animators related to the Step here. I think we could only store the ones we need to Trigger
																			// later on, but that's something I just thought about and might be worth thinking about when optimising.
	private List<Change> changes = new ArrayList<Change>();					// list of Changes
	private String stepInfo;												// what change is being made. Could be a list or array of strings, but right now one String is enough.
	
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

	/* this function adds the Animator to the list of changes
	 * and automatically adds triggers for certain cases we need
	 * according to what the second variable (after) is instance of
	 */
	public void addAnimator(Animator item, Object after) {
		if (this.list.peekLast() == null) {
			System.out.println("Should be working #2");
			//TriggerUtility.addActionTrigger(after, item);
		} else if (after == null) {
			TriggerUtility.addTimingTrigger(this.list.peekLast(), item, TimingTriggerEvent.STOP);
		} else if (after instanceof Step) {
			System.out.println("Should be working #3");
			// nothing yet
		} else if (after instanceof Animator) {
			System.out.println("Should be working #4");
			TriggerUtility.addTimingTrigger((Animator)after, item, TimingTriggerEvent.START);
		} else if (after instanceof JButton) {
			System.out.println("Should be working #5");
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
