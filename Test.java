import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;


public final class Test extends JPanel implements TimingTarget{

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
	private final static List<Rect> rect_list = new ArrayList<Rect>();


	public static void setupGUI() {
		JFrame frame = new JFrame("Basic animation test");
		final JButton pauseBtn = new JButton("Pause");
		final JButton resumeBtn = new JButton("Resume");
		final JButton stopBtn = new JButton("Stop");
		final JButton destroyBtn = new JButton("Destroy last");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,4));
	
		buttonPanel.add(resumeBtn);
		buttonPanel.add(pauseBtn);
		buttonPanel.add(stopBtn);
		buttonPanel.add(destroyBtn);
		
		pauseBtn.setEnabled(true);
		resumeBtn.setEnabled(false);
		destroyBtn.setEnabled(false);
		

		JPanel panel = new Test(); /*main is represented as a component (extends JPanel) , and added to the frame */
		frame.add(panel, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.SOUTH);
		ani_timer.init(); /*start animation timer */


		pauseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				/*need to all be paused, cant pause individual elements, maybe because they are associated with same timing target? */
				for(Rect r : rect_list)
					if(r.getAni().isRunning()){
						r.getAni().pause();
						resumeBtn.setEnabled(true);
						pauseBtn.setEnabled(false);
						destroyBtn.setEnabled(true);
					}
					
			}
		});


		resumeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(Rect r : rect_list)
					if(r.getAni().isPaused()){
						r.getAni().resume();
						resumeBtn.setEnabled(false);
						pauseBtn.setEnabled(true);
						destroyBtn.setEnabled(false);
					}
			}
		});

		
		stopBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(Rect r : rect_list)
					if(r.getAni().isRunning()){
						r.getAni().stop();
						resumeBtn.setEnabled(false);
						pauseBtn.setEnabled(false);
						stopBtn.setEnabled(false);
						destroyBtn.setEnabled(false);
					}
			}
		});
		
		destroyBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Animator z = rect_list.get(arrayLeft).getAni();
				if(arrayLeft == 0)
					for(Rect r : rect_list)
						r.getAni().stop(); /*stop all of the animations, calls up void end */
				if(z.isPaused() && arrayLeft >0){
					rect_list.remove(arrayLeft);
					arrayLeft--;
					System.out.println("Elements left: " + (arrayLeft+1));
				}
					
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



	/*class representing a rectangle (array index) , could have string, integer data associations etc */
	public static class Rect{

		private Rectangle rec;
		private Animator ani;
		private Point start;
		private Point end;

		public Point getStart() {
			return start;
		}

		public void setStart(Point start) {
			this.start = start;
		}

		public Point getEnd() {
			return end;
		}

		public void setEnd(Point end) {
			this.end = end;
		}

		public Rect(int x,int y,int w,int h){
			rec = new Rectangle(x,y,w,h);
			start = new Point(x,y);
			end = new Point(700,y);

		}

		public Rect(){
			rec = new Rectangle(10,30,40,40);
			start = new Point(10,30);
			end = new Point(600,30);
		}

		public Animator getAni() {
			return ani;

		}

		public void setRec(Rectangle rec) {
			this.rec = rec;
		}

		public void setAni(Animator ani) {
			this.ani = ani;
		}

		public Rectangle getRec(){
			return rec;
		}

	}


	public Test(){
		int x=10,y=30,w=40,h=40;
		int t = 5;
		arrayLeft = 4;
		setOpaque(true);
		/*create a rectangle class and associate its animator with a new animator, which is 5 seconds long, and repeats infinitely */
		/*addTarget(this) associates the target for the animation to the class main(), where which each timing tick, timingEvent is 
		 * called
		 * 
		 * They are all associated with the same timing target, so on each tick, all of the animator objects will call back to
		 * main's() timingEvent, which handles screen repaints
		 * 
		 * rect_list holds an array of Rect, which contains an animator etc
		 */
		for(int i = 0;i<arrayLeft+1;i++){
			r = new Rect(x,y,w,h);
			r.setAni(new Animator.Builder().setDuration(t, TimeUnit.SECONDS).setRepeatCount(Animator.INFINITE).addTarget(this).build());
			r.getAni().start();
			rect_list.add(r);
			y += 80;
		}


	}


	/*an overriden method from JFrame: paints the actual rectangle on the screen. It is called each time timingEvent calls repaint() */
	protected void paintComponent(Graphics g){

		int tag = 0;
		Graphics g2g = (Graphics2D) g;
		((Graphics2D) g2g).setBackground(Color.WHITE);

		for(int x=0;x<10;x++){
			g2g.clearRect(0, 0, getWidth(), getHeight()); //clear all rectangle
		}

		g2g.setColor(Color.BLUE);
		
		/*fill the rectangles and draw a tag next to each one */
		for(Rect r : rect_list){
			g2g.fillRect(r.getRec().x, r.getRec().y,r.getRec().width,r.getRec().height);
			g2g.drawString("Array tag: "+tag, r.getRec().x, r.getRec().y-10);
			tag++;
		}

	} 


	@Override
	/*from the implements timingEvent, it overrides the previous one
	 * 
	 * the source will probably be for all the animator objects in rect_list, and this is called each time
	 * the framework issues a tick from the main timer. The fraction just sets the x and y positions for each
	 * rectangle class, and schedules a repaint which calls up paintComponent which draws this new position
	 * 
	 * fraction is between 0(start of animation) and 1 (end)
	 * */
	public void timingEvent(Animator source, double fraction) {

		for(Rect r : rect_list){
			r.getRec().x = (int) (r.getStart().x + (r.getEnd().x - r.getStart().x) * fraction);
			r.getRec().y = (int) (r.getStart().y + (r.getEnd().y - r.getStart().y) * fraction);
			repaint();
		}

	}


	@Override
	public void begin(Animator source) {
		// TODO Auto-generated method stub
		System.out.println("Animation has started");

	}

	@Override
	public void end(Animator source) {
		// TODO Auto-generated method stub
		System.out.println("Animation has finished");
		
		for(Rect r : rect_list)
			r.getAni().stop();
	}

	@Override
	public void repeat(Animator source) {

	}

	@Override
	public void reverse(Animator source) {
		// TODO Auto-generated method stub

	}


}

