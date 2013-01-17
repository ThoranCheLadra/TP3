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
    
    public static AnimatedArray anim;
    
	public static void main(String[] args) {
		int[] arr = {4,3,8,1,2,12};
                anim = new AnimatedArray(arr);
		/*setup thread for creating GUI */
		
		
		int time = 2;
		int i, j,t=0;
		for(i = 0; i < arr.length; i++){
			for(j = 1; j < (arr.length-i); j++){
				anim.setRectColor(j, j-1, Color.RED, time);
					if(arr[j-1] > arr[j]){
					t = arr[j-1];
					arr[j-1]=arr[j];
					arr[j]=t;
					anim.swapRect(j, j-1, time);
				}
				anim.setRectColor(j, j-1, Color.BLUE, time);
			}
			anim.setRectColor(j-1, Color.DARK_GRAY, time);
		}
                
                SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setupGUI.setupGUI();
			}
		});
		
        anim.endAnimation();
	}
}
