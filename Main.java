import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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


public final class Main extends JPanel {

	public static void main(String[] args) {
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(ani_timer);

		/*setup thread for creating GUI */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setupGUI();
			}
		});
	}


	private static final SwingTimerTimingSource ani_timer = new SwingTimerTimingSource();
	private static int arrayLeft;
	private static Rect r;
	private static Step s;
	private static List<Rect> rect_list = new ArrayList<Rect>();
	private final static LinkedList<Step> steps = new LinkedList<Step>();
	static JButton resumeBtn;
	static JButton stopBtn;
	static JPanel panel;
	static int currentStep;
	static int t;
	private static String info = ""; // a string to display information at each step
	//private static JButton resumeBtn;


	public static void setupGUI() {
		JFrame frame = new JFrame("Triggers Test");
		final JButton pauseBtn = new JButton("Pause");
		resumeBtn = new JButton("Next Step");
		stopBtn = new JButton("Prev step");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
	
		buttonPanel.add(resumeBtn);
		buttonPanel.add(pauseBtn);
		buttonPanel.add(stopBtn);
		
		pauseBtn.setEnabled(true);
		resumeBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		

		panel = new Main(); /*main is represented as a component (extends JPanel) , and added to the frame */
		frame.add(panel, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.SOUTH);
		ani_timer.init(); /*start animation timer */


		pauseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// does nothing just now					
			}
		});


		resumeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.panel.repaint();
				currentStep++;
				if (steps.get(currentStep).getList().size() != 0) {
					steps.get(currentStep).getList().get(0).start();				
				}
				if (currentStep == steps.size() - 1) {
					resumeBtn.setEnabled(false);
				}
				stopBtn.setEnabled(true);
			}
		});

		
		stopBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//setInfo(steps.get(currentStep).getInfo());
				for(Change tempChange: steps.get(currentStep).getChanges()) {
					stepBack(tempChange);
				}
				Main.panel.repaint();
				currentStep--;
				if (currentStep == 0) {
					stopBtn.setEnabled(false);
				}
				resumeBtn.setEnabled(true);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				ani_timer.dispose();
			}
		});

		frame.setSize(800, 500);
		frame.validate();
		frame.setVisible(true);
	}

	public Main(){
		int x=60,y=30,w=40,h=40;
		t = 1;
		currentStep = 0;
		arrayLeft = 4;
		setOpaque(true);
		
		s = new Step();
		setInfo("");
		s.setInfo(info);
		for(int i = 0;i<arrayLeft+1;i++){
			r = new Rect(x, y, w, h, Integer.toString(i));
			rect_list.add(r);
			y += 80;
		}
		steps.add(s);
		
		swapRect(1,0,t);
		setRectColor(3, Color.RED);
		modifyLabel(2, "asd");
		modifyLabel(2, "500");
		swapRect(0,4,t);
		swapRect(1,3,t);
		
		while (currentStep != 0) {
			//setInfo(steps.get(currentStep).getInfo());
			for(Change tempChange: steps.get(currentStep).getChanges()) {
				stepBack(tempChange);
			}
			currentStep--;
		}
	}
	
	public void swapRect(int a, int b, long t){
		currentStep++;
		s = new Step();
		
		String information = "Swapping index " + a + " (" + rect_list.get(a).getLabel() + ") " + " with index " + b + " (" + rect_list.get(b).getLabel() + ")";
		s.setInfo(information);
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		TimingTargetChangeLabel temp = new TimingTargetChangeLabel(this, information);
		s.getLastAnimator().addTarget(temp);
		
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), s.getFirstAnimator());
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentX", rect_list.get(a).getRec().x, rect_list.get(a).getRec().x+50));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentY", rect_list.get(a).getRec().y, rect_list.get(b).getRec().y)); 
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(a), "currentX", rect_list.get(a).getRec().x+50, rect_list.get(a).getRec().x));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), s.getFirstAnimator());
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentX", rect_list.get(b).getRec().x, rect_list.get(b).getRec().x-50));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentY", rect_list.get(b).getRec().y, rect_list.get(a).getRec().y));
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(b), "currentX", rect_list.get(b).getRec().x-50, rect_list.get(b).getRec().x));
		
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		temp = new TimingTargetChangeLabel(this, "");
		s.getLastAnimator().addTarget(temp);
		
		s.addChanges(new Change("swap", rect_list.get(a), rect_list.get(a).getRec().x, rect_list.get(a).getRec().y));
		s.addChanges(new Change("swap", rect_list.get(b), rect_list.get(b).getRec().x, rect_list.get(b).getRec().y));
		int y = rect_list.get(a).getRec().y;
		rect_list.get(a).getRec().y = rect_list.get(b).getRec().y;
		rect_list.get(b).getRec().y = y;
		Rect tempRect = rect_list.get(b);
		rect_list.set(b, rect_list.get(a));
		rect_list.set(a, tempRect);
		steps.add(s);
	}
	
	 public void modifyLabel(int n, String d){
		currentStep++;
		s = new Step();
		  
		String information = "Changing index " + n + " label from " + rect_list.get(n).getLabel() + " to " + d;
		s.setInfo(information);
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		TimingTargetChangeLabel temp = new TimingTargetChangeLabel(this, information);
		s.getLastAnimator().addTarget(temp);
		  
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), s.getFirstAnimator());
		temp = new TimingTargetChangeLabel(rect_list.get(n), d);
		s.getLastAnimator().addTarget(temp);
		  
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		temp = new TimingTargetChangeLabel(this, "");
		s.getLastAnimator().addTarget(temp);
		  
		s.addChanges(new Change("modifyLabel", rect_list.get(n), rect_list.get(n).getLabel()));
		rect_list.get(n).setLabel(d);
		steps.add(s);	  
	 } 
	 
	 public void setRectColor(int index, Color c){
		currentStep++;
		s = new Step();
		
		String information = "Changing index " + index + " color from " + rect_list.get(index).getColor() + " to " + c;
		s.setInfo(information);
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		TimingTargetChangeLabel temp = new TimingTargetChangeLabel(this, information);
		s.getLastAnimator().addTarget(temp);
		
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), s.getFirstAnimator());
		s.getLastAnimator().addTarget(PropertySetter.getTarget(rect_list.get(index), "colorDraw", rect_list.get(index).getColor(), c));
		
		s.addAnimator(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).build(), resumeBtn);
		temp = new TimingTargetChangeLabel(this, "");
		s.getLastAnimator().addTarget(temp);
		
		s.addChanges(new Change("color", rect_list.get(index), rect_list.get(index).getColor()));
		rect_list.get(index).setColor(c);
		steps.add(s);
	}
	 
	public static void stepBack(Change tempChange) {
		if (tempChange.getType() == "swap") {
			tempChange.getReference().getRec().x = tempChange.getPoint().x;
			tempChange.getReference().getRec().y = tempChange.getPoint().y;
		} else if (tempChange.getType() == "color") {
			tempChange.getReference().setColor(tempChange.getColor());
		} else if (tempChange.getType() == "modifyLabel") {
			tempChange.getReference().setLabel(tempChange.getLabel());
		}
	}
	
	public static void setInfo(String info) {
		Main.info = info;
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
		for(Rect r : rect_list){
			g2g.setColor(r.getColor());
			g2g.fillRect(r.getRec().x, r.getRec().y,r.getRec().width,r.getRec().height);
			g2g.drawString(r.getLabel(), r.getRec().x, r.getRec().y-10);
			g2g.drawString(info,300,60);
		}

	} 

}

