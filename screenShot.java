import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
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
	
	public static void takeScreenShot(){

	    try{  
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //stolen from online and pasted over andy's code
			Rectangle screenRectangle = new Rectangle(screenSize);				//yw.
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRectangle);
			ImageIO.write(image, "png", new File("images/shot"+shotNumber+".png"));

//		    File imageFile = new File("images/shot"+shotNumber+".png");  
//	        imageFile.createNewFile();  
//	        ImageIO.write(bufImage, "png", imageFile);  
			
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

	
	public static void captureScreen(String fileName) throws Exception {
		
		
		}
}
