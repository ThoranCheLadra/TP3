import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//a duplicate of the Main class for AnimatedArray, for linkedList.
//omitted user code as animation hasn't yet been implemented

public class testAnimatedLinkedList {


	private static AnimatedLinkedList anim;

	public static void setupGUI() {

		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		anim.Fullscreen = toolkit.getScreenSize();
		//	Fullscreen = new Dimension(100,100);

		JFrame frame = new JFrame("Algorithm Animator");
		anim.nextBtn = new JButton("Next Step");
		anim.prevBtn = new JButton("Prev step");
		anim.pauseBtn = new JButton("Play");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));

		buttonPanel.add(anim.nextBtn);
		buttonPanel.add(anim.pauseBtn);
		buttonPanel.add(anim.prevBtn);

		anim.pauseBtn.setEnabled(true);
		anim.nextBtn.setEnabled(true);
		anim.prevBtn.setEnabled(false);


		anim.panel = new AnimatedLinkedList(); /*main is represented as a component (extends JPanel) , and added to the frame */
		frame.add(anim.panel, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.SOUTH);

		anim.ani_timer.init(); /*start animation timer */


		anim.pauseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (!anim.continuousAnimation) {
					anim.nextBtn.setEnabled(false);
					anim.prevBtn.setEnabled(false);
					anim.pauseBtn.setText("Pause");
					anim.continuousAnimation = true;
					anim.currentStep++;
					anim.steps.get(anim.currentStep).getList().get(0).start();
				} else {
					anim.pauseBtn.setText("Play");
					anim.continuousAnimation = false;
					anim.nextBtn.setEnabled(true);
					if (anim.currentStep == anim.steps.size() - 1) {
						anim.nextBtn.setEnabled(false);
					}
					anim.prevBtn.setEnabled(true);
				}
			}
		});


		anim.nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Main.panel.repaint();
				anim.currentStep++;																		// increment the currentStep (because we're moving to the next one)
				if (anim.steps.get(anim.currentStep).getList().size() != 0) {									// if there are any Animators inside the step, trigger the first one
					anim.steps.get(anim.currentStep).getList().get(0).start();
				}
				anim.nextBtn.setEnabled(false);
				anim.pauseBtn.setEnabled(false);
				anim.prevBtn.setEnabled(false);															// we're definitely not at the first step anymore, so enable prevBtn
			}
		});


		anim.prevBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//setInfo(steps.get(currentStep).getInfo());
				anim.setInfo("");
				for(Change tempChange: anim.steps.get(anim.currentStep).getChanges()) {						// in each Step we are storing the values before the animation, so we
					anim.stepBack(tempChange);															// restore it all like it was before this step and then decrement the
				}																					// currentStep value, as well as call Main.panel.repaint() to redraw
				AnimatedLinkedList.panel.repaint();																// the panel in order to show these changes
				anim.currentStep--;
				if (anim.currentStep == 0) {																// if we're at the first step, disable prevBtn
					anim.prevBtn.setEnabled(false);
				}
				anim.nextBtn.setEnabled(true);															// we're definitely not at the last step anymore, so enable nextBtn
				anim.pauseBtn.setEnabled(true);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				anim.ani_timer.dispose();
			}
		});

		frame.setSize(anim.Fullscreen.width, anim.Fullscreen.height);
		frame.validate();
		frame.setVisible(true);


	}


	public static void main(String[] args) {
		int[] arr = {4,3,18,20,66,103};
		anim = new AnimatedLinkedList(arr);
		/*setup thread for creating GUI */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setupGUI();
			}
		});

	}



}
