import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/*Simple class to take a screenshot of a given frame, most of the code taken from here:
 * http://binfalse.de/2011/01/shortcutimage-screenshooting-a-jpanel/
 * 
 * 
 * added some code to index the files and write in a local directory 'images'
 * 
 * our class want to modify shotNumber to overwrite a particular file etc
 * 
 * could make png, jpeg etc, all depends on storage requirements and desired quality
 * 
 */
public class screenShot {
	
	private static int shotNumber;
	
	public static void takeScreenShot(JPanel j){
		 BufferedImage bufImage = new BufferedImage(j.getSize().width, j.getSize().height,BufferedImage.TYPE_INT_RGB);  
	       j.paint(bufImage.createGraphics());  

	    try{  
		    File imageFile = new File("images/shot"+shotNumber+".png");  
	        imageFile.createNewFile();  
	        ImageIO.write(bufImage, "png", imageFile);  
	        shotNumber++;
	    }catch(Exception ex){  
	    	System.out.println("Could not write to file");
	    }  
	}
	
	
	
	public static int getShotNumber() {
		return shotNumber;
	}

	public static void setShotNumber(int shotNumber) {
		screenShot.shotNumber = shotNumber;
	}

}
