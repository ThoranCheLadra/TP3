import java.awt.Color;
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
import org.jdesktop.core.animation.timing.Animator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arthur Bigeard
 */
public class Main {
    
        private static AnimatedArray anim;
        public static void setupGUI() {
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		anim.Fullscreen = toolkit.getScreenSize();
	//	Fullscreen = new Dimension(100,100);
		
		JFrame frame = new JFrame("Algorithm Animator");
		final JButton pauseBtn = new JButton("Pause");
		anim.nextBtn = new JButton("Next Step");
		anim.prevBtn = new JButton("Prev step");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
	
		buttonPanel.add(anim.nextBtn);
		buttonPanel.add(pauseBtn);
		buttonPanel.add(anim.prevBtn);
		
		pauseBtn.setEnabled(true);
		anim.nextBtn.setEnabled(true);
		anim.prevBtn.setEnabled(false);
		

		anim.panel = new AnimatedArray(); /*main is represented as a component (extends JPanel) , and added to the frame */
		frame.add(anim.panel, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.SOUTH);

		anim.ani_timer.init(); /*start animation timer */
	

		pauseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// does nothing just now					
			}
		});


		anim.nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Main.panel.repaint();
				anim.currentStep++;																		// increment the currentStep (because we're moving to the next one)
				if (anim.steps.get(anim.currentStep).getList().size() != 0) {									// if there are any Animators inside the step, trigger the first one
					anim.steps.get(anim.currentStep).getList().get(0).start();
				}
				if (anim.currentStep == anim.steps.size() - 1) {												// if we're at the last step, disable nextBtn
					anim.nextBtn.setEnabled(false);
				}
				anim.prevBtn.setEnabled(true);															// we're definitely not at the first step anymore, so enable prevBtn
			}
		});

		
		anim.prevBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//setInfo(steps.get(currentStep).getInfo());
				anim.setInfo("");
				for(Change tempChange: anim.steps.get(anim.currentStep).getChanges()) {						// in each Step we are storing the values before the animation, so we
					anim.stepBack(tempChange);															// restore it all like it was before this step and then decrement the
				}																					// currentStep value, as well as call Main.panel.repaint() to redraw
				AnimatedArray.panel.repaint();																// the panel in order to show these changes
				anim.currentStep--;
				if (anim.currentStep == 0) {																// if we're at the first step, disable prevBtn
					anim.prevBtn.setEnabled(false);
				}
				anim.nextBtn.setEnabled(true);															// we're definitely not at the last step anymore, so enable nextBtn
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
		int[] arr = {4,3,4,8,1,2,12,5};
                anim = new AnimatedArray(arr);
		/*setup thread for creating GUI */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setupGUI();
			}
		});
        
		/* a[0] to a[n-1] is the array to sort */
		int i,j;
		int iMin;
		/* advance the position through the entire array */
		/*   (could do j < n-1 because single element is also min element) */
		for (j = 0; j < anim.getRectList().size()-1; j++) {
		    /* find the min element in the unsorted a[j .. n-1] */

		    /* assume the min is the first element */
		    iMin = j;
		    /* test against elements after j to find the smallest */
		    for ( i = j+1; i < anim.getRectList().size(); i++) {

		        /* if this element is less, then it is the new minimum */  
		    	anim.setRectColor(i, iMin, Color.RED);
		    	System.out.println("next step");
		        if ( Integer.parseInt(anim.getRectList().get(i).getLabel()) < Integer.parseInt(anim.getRectList().get(iMin).getLabel()) ) {
		            /* found new minimum; remember its index */
		        	anim.setRectColor(iMin, Color.BLUE);
		        	System.out.println("next step");
		            iMin = i;
		        } else {
		        	anim.setRectColor(i, Color.BLUE);
		        	System.out.println("next step");
		        }
		    }


		    /* iMin is the index of the minimum element. Swap it with the current position */
		    if ( iMin != j ) {
		    	anim.swapRect(j, iMin, 500);
		    	System.out.println("next step");
		    }
		    iMin = j;
		    anim.setRectColor(iMin, Color.GREEN);
		    System.out.println("next step");
		}
		anim.setRectColor(anim.getRectList().size()-1, Color.GREEN);
		
        anim.endAnimation();
	}
}
