import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


public class setupGUI {
	private static AnimatedArray anim = Main.anim;

	public static void setupGUI() {
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		anim.Fullscreen = toolkit.getScreenSize();
	//	Fullscreen = new Dimension(100,100);

		final JFrame frame = new JFrame("Algorithm Animator");
                
		anim.nextBtn = new JButton("Next Step");
		anim.prevBtn = new JButton("Prev step");
		anim.pauseBtn = new JButton("Play");
		JButton addBtn = new JButton("Add");
                
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
	
		JFrame stepListFrame = new JFrame("Animation step list");
		JPanel listPanel = new JPanel();
		final JList stepList = new JList(anim.steps.toArray());
                stepList.setVisibleRowCount(8);
		stepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
		JButton selectStep = new JButton("Go");
	
		buttonPanel.add(anim.prevBtn);
		buttonPanel.add(anim.pauseBtn);
		buttonPanel.add(anim.nextBtn);
		
                JScrollPane listScroller = new JScrollPane(stepList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                listScroller.setPreferredSize(new Dimension(400, 250));
                
                listPanel.add(listScroller);
                listPanel.add(selectStep);
                
		anim.pauseBtn.setEnabled(true);
		anim.nextBtn.setEnabled(true);
		anim.prevBtn.setEnabled(false);
		

		anim.panel = new AnimatedArray(); /*main is represented as a component (extends JPanel) , and added to the frame */
		frame.add(anim.panel, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.SOUTH);
		stepListFrame.add(listPanel, BorderLayout.CENTER);
		
		anim.ani_timer.init(); /*start animation timer */
<<<<<<< HEAD
                
                
         /*       frame.addComponentListener(new ComponentListener() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                      //  anim.resize(frame.getHeight(), frame.getWidth());
                    }

                    @Override
                    public void componentMoved(ComponentEvent e) {
                      //  throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void componentShown(ComponentEvent e) {
                      //  throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void componentHidden(ComponentEvent e) {
                      //  throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
                */
=======
		
>>>>>>> 207e372eed143e3b8be90c1b45291985b990b12f
		selectStep.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
                                if(stepList.getSelectedIndex() < anim.currentStep){
                                    anim.setInfo("");
                                    for(int i = anim.currentStep; i > stepList.getSelectedIndex()-1; i--){	
                                        for(Change tempChange: anim.steps.get(i).getChanges()) {						// in each Step we are storing the values before the animation, so we
                                                anim.changeBack(tempChange);															// restore it all like it was before this step and then decrement the
                                        }	
                                        anim.currentStep--;
                                    }
                                    // currentStep value, as well as call Main.panel.repaint() to redraw
                                    AnimatedArray.panel.repaint();																// the panel in order to show these changes

                                    if (anim.currentStep == 0) {																// if we're at the first step, disable prevBtn
                                            anim.prevBtn.setEnabled(false);
                                    }
                                    anim.nextBtn.setEnabled(true);															// we're definitely not at the last step anymore, so enable nextBtn
                                    anim.pauseBtn.setEnabled(true);
                                    //anim.currentStep = stepList.getSelectedIndex();
                                    }
                                else{
                                    																		// increment the currentStep (because we're moving to the next one)
                                    for(int i = anim.currentStep; i < stepList.getSelectedIndex(); i++){						// in each Step we are storing the values before the animation, so we
                                        anim.stepForward();															// restore it all like it was before this step and then decrement the
                                    }
                                    if (anim.currentStep == anim.steps.size() - 1) {												// if we're at the last step, disable nextBtn
                                            anim.nextBtn.setEnabled(false);
                                            anim.pauseBtn.setEnabled(false);
                                    }
                                    anim.prevBtn.setEnabled(true);	
                                }
			}
		});
	
	
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
				anim.stepForward();
				anim.nextBtn.setEnabled(false);
				anim.pauseBtn.setEnabled(false);
				anim.prevBtn.setEnabled(true);															// we're definitely not at the first step anymore, so enable prevBtn
			}
		});

		
		anim.prevBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//setInfo(steps.get(currentStep).getInfo());
				anim.setInfo("");
				for(Change tempChange: anim.steps.get(anim.currentStep).getChanges()) {						// in each Step we are storing the values before the animation, so we
					anim.changeBack(tempChange);															// restore it all like it was before this step and then decrement the
				}																					// currentStep value, as well as call Main.panel.repaint() to redraw
				AnimatedArray.panel.repaint();																// the panel in order to show these changes
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

		frame.setSize(anim.windowWidth, anim.windowHeight);
		frame.validate();
		frame.setVisible(true);
                
                stepListFrame.setSize(600, 300);
                stepListFrame.setVisible(true);
	}
}
