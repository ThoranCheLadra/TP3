package AnimatedDataStructure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


public class setupGUI {
	
	private AnimatedDataStructure anim;
	
	public setupGUI(AnimatedDataStructure animDS) {
		anim = animDS;
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		anim.setFullscreen(toolkit.getScreenSize());
		//	Fullscreen = new Dimension(100,100); // for debugging size
		
		final JFrame frame = new JFrame("Algorithm Animator");
		anim.setNextButton(new JButton("Next Step"));
		anim.setPrevButton(new JButton("Prev step"));
		anim.setPauseButton(new JButton("Play"));
                
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
	
		JFrame stepListFrame = new JFrame("Animation step list");
		JPanel listPanel = new JPanel();
		final JList stepList = new JList(anim.getSteps().toArray());
                stepList.setVisibleRowCount(8);
		stepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
		JButton selectStep = new JButton("Go");
	
		buttonPanel.add(anim.getPrevButton());
		buttonPanel.add(anim.getPauseButton());
		buttonPanel.add(anim.getNextButton());
		
                JScrollPane listScroller = new JScrollPane(stepList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                listScroller.setPreferredSize(new Dimension(400, 250));
                
                listPanel.add(listScroller);
                listPanel.add(selectStep);
                
        anim.getPauseButton().setEnabled(true);
        anim.getNextButton().setEnabled(true);
        anim.getPrevButton().setEnabled(false);
		
		frame.add((JPanel)anim, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.SOUTH);
		stepListFrame.add(listPanel, BorderLayout.CENTER);
		
		anim.getAniTimer().init(); /*start animation timer */
		
		selectStep.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
                                if(stepList.getSelectedIndex() <= anim.getCurrentStep()) {
                                	while (stepList.getSelectedIndex() < anim.getCurrentStep()) {
                                		anim.stepBack();
                                	}
                                    if (anim.getCurrentStep() == 0) {																// if we're at the first step, disable prevBtn
                                    	anim.getPrevButton().setEnabled(false);
                                    }
                                    anim.getNextButton().setEnabled(true);															// we're definitely not at the last step anymore, so enable nextBtn
                                    anim.getPauseButton().setEnabled(true);
                                
                                } else {
                                    																		// increment the currentStep (because we're moving to the next one)
                                    for(int i = anim.getCurrentStep(); i < stepList.getSelectedIndex(); i++){						// in each Step we are storing the values before the animation, so we
                                    	anim.stepForward();															// restore it all like it was before this step and then decrement the
                                    }
                                    if (anim.getCurrentStep() == anim.getSteps().size() - 1) {												// if we're at the last step, disable nextBtn
                                    	anim.getNextButton().setEnabled(false);
                                    	anim.getPauseButton().setEnabled(false);
                                    }
                                    anim.getPrevButton().setEnabled(true);	
                                }
			}
		});
	
	
		anim.getPauseButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (!anim.getContinuousAnimation()) {
					anim.getNextButton().setEnabled(false);
					anim.getPrevButton().setEnabled(false);
					anim.getPauseButton().setText("Pause");
					anim.setContinuousAnimation(true);
					anim.setCurrentStep(anim.getCurrentStep()+1);
					anim.getSteps().get(anim.getCurrentStep()).getList().get(0).start();
				} else {
					anim.getPauseButton().setText("Play");
					anim.setContinuousAnimation(false);
					anim.getNextButton().setEnabled(true);
					if (anim.getCurrentStep() == anim.getSteps().size() - 1) {
						anim.getNextButton().setEnabled(false);
					}
					anim.getPrevButton().setEnabled(true);
				}
			}
		});

		anim.getNextButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				anim.stepForward();
				anim.getNextButton().setEnabled(false);
				anim.getPauseButton().setEnabled(false);
				anim.getPrevButton().setEnabled(true);															// we're definitely not at the first step anymore, so enable prevBtn
			}
		});

		
		anim.getPrevButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//setInfo(steps.get(currentStep).getInfo());
				anim.stepBack();
				if (anim.getCurrentStep() == 0) {																// if we're at the first step, disable prevBtn
					anim.getPrevButton().setEnabled(false);
				}
				anim.getNextButton().setEnabled(true);															// we're definitely not at the last step anymore, so enable nextBtn
				anim.getPauseButton().setEnabled(true);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}
		});

		frame.setSize(anim.getWindowWidth(), anim.getWindowHeight());
		frame.validate();
		frame.setVisible(true);
                
                stepListFrame.setSize(600, 300);
                stepListFrame.setVisible(true);
	}
}
