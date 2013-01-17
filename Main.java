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

import java.util.Random;
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
    public static int time = 200;
    
	public static void main(String[] args) {
		int[] arr = {4,3,8,1,2,12};
		anim = new AnimatedArray(arr);
		/*setup thread for creating GUI */
		
		
		quicksort(arr, 0, arr.length-1, time);
                
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setupGUI.setupGUI();
			}
		});
		
        anim.endAnimation();
	}
	// taken from http://en.wikipedia.org/wiki/Quicksort , translated to Java and our API applied
	
	// left is the index of the leftmost element of the array
	// right is the index of the rightmost element of the array (inclusive)
	// number of elements in subarray = right-left+1
	public static int partition(int[] array, int left, int right, int pivotIndex, int time) {
	    int pivotValue = array[pivotIndex];
	    // Move pivot to end
	    int temp = array[pivotIndex];
	    array[pivotIndex] = array[right];
	    array[right] = temp;
	    anim.swapRect(pivotIndex, right, time);
	    int storeIndex = left;
	    for(int i = left; i < right; i++) { // left <= i <= right
	    	if (array[i] < pivotValue) {
	    		temp = array[i];
	    		array[i] = array[storeIndex];
	    		array[storeIndex] = temp;
	    		anim.swapRect(i, storeIndex, time);
	    		storeIndex = storeIndex + 1;
	    	}
	    }
	    // Move pivot to its final place
	    temp = array[storeIndex];
	    array[storeIndex] = array[right];
	    array[right] = temp;
	    anim.swapRect(storeIndex, right, time);
	    return storeIndex;
	}
	
	public static void quicksort(int[] array, int left, int right, int time) {
		// If the list has 2 or more items
		if (left < right) {
			Random random = new Random();
			int pivotIndex = random.nextInt(right-left) + left;
			// Get lists of bigger and smaller items and final position of pivot
			int pivotNewIndex = partition(array, left, right, pivotIndex, time);
			// Recursively sort elements smaller than the pivot
			quicksort(array, left, pivotNewIndex - 1, time);
			// Recursively sort elements at least as big as the pivot
			quicksort(array, pivotNewIndex+1, right, time);
		}
		
	}
	
}
