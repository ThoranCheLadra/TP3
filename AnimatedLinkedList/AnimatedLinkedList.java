package AnimatedLinkedList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;

import AnimatedDataStructure.AnimatedTemplate;
import AnimatedDataStructure.Change;
import AnimatedDataStructure.ChangeLabel;
import AnimatedDataStructure.ContinuousAnimation;
import AnimatedDataStructure.Rect;
import AnimatedDataStructure.Step;
import AnimatedDataStructure.setupGUI;



//example template class that displays a linked list, some classes have been left in from AnimatedArray, whilst others
//ommited that clashed. currently animation hasn't been implemented

public final class AnimatedLinkedList extends AnimatedTemplate {
	static final long serialVersionUID = 3L;
 	private linkedList rect_list;	// this is where all the rectangles which will be drawn are stored

	public AnimatedLinkedList(int[] arr){
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(ani_timer);

		arrayLeft = arr.length;	//MAX = 85
		
		rect_list = new linkedList(); //a linked list of rectangles

		//the scaling/resizing code is not working currently
		int rs = rectSize();
		int h = rs;
		int w = rs;
		int x= arrayLeft*rs+rectSpace();
		int y= 2*rs;
		time = 100;
		currentStep = 0;
		setOpaque(true);

		Step s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("Start of the animation");																	// it wasn't for the "Info" field
		s.setInfo(info);


		//as its a stack, items need to be added in reverse to begin with to replicate its appearance

		for(int i = 0;i<arrayLeft;i++){
			Rect r = new Rect(x, y, w, h, arr[i]+"", this);								// create all the Rectangle and add them to rect_list
			rect_list.addFirst(r);
			x -= rectSpace();
		}
		steps.add(s);
	}
        
        
        public AnimatedLinkedList(int[] arr, int width, int height){
                windowWidth = width;
                windowHeight = height;
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		Animator.setDefaultTimingSource(ani_timer);

		arrayLeft = arr.length;	//MAX = 85
		
		rect_list = new linkedList(); //a linked list of rectangles

		//the scaling/resizing code is not working currently
		int rs = rectSize(windowWidth);
		int h = rs;
		int w = rs;
		int x= arrayLeft*rs+rectSpace(rs);
		int y= 2*rs;
		time = 100;
		currentStep = 0;
		setOpaque(true);

		Step s = new Step();																	// create a new step to store initial values and all. We wouldn't really need it if 
		setInfo("Start of the animation");																	// it wasn't for the "Info" field
		s.setInfo(info);


		//as its a stack, items need to be added in reverse to begin with to replicate its appearance

		for(int i = 0;i<arrayLeft;i++){
			Rect r = new Rect(x, y, w, h, arr[i]+"", this);								// create all the Rectangle and add them to rect_list
			rect_list.addFirst(r);
			x -= rectSpace(rs);
		}
		steps.add(s);
	}   
        
	//locates, identifies and removes the linked list element with String S
	public void findAndRemove(String i, AnimatedLinkedList anim){
		Step s = new Step();
		// Create animation for showing information on what's happening
		String information = "findAndRemove ";
		s.setInfo(information);	
		// Change information
		s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());																		// create an animator for this, which we could use to trigger this change
		s.getLastAnimator().addTarget(new ChangeLabel(this, information, this));		

		//get first item
		Node<Rect> node = rect_list.getHead();
		//maintain a pointer to the previous node
		Node<Rect> prev = rect_list.getHead();
		int count = 0;

		//check if the i == head, else examine the rest of the list
		if(i.equals(rect_list.getHead().getData().getLabel())){
			rect_list.removeFirst();
			s.addChanges(new Change("deleteHead", prev.getData(), "Removing head of list"));
		} else {
			//check each other node, set colour to indicate change
			while(node.getNext() != null && !(node.getData().getLabel().equals(i))){
				prev = node;
				setColor(count,Color.RED,"Changing color to red");
				setColor(count,Color.BLUE,"Changing color to blue");
				node = node.getNext();
				count++;
			}
			//fade out the found node
			anim.setColor(count,Color.GREEN,"Changing color to green");
			anim.setColor(count,Color.WHITE,"Changing color to white");
			s.addChanges(new Change("pointerSwitch", prev, "Assigning pointer from previous element"));
		}
		//increment the current step
		currentStep++;
		// Trigger for a continuous animation
		s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
		
		steps.add(s);
	}
	
	public void setColor(int index, Color c, String message) {
		currentStep++;
		Step s = new Step();
		//get first item
		Node<Rect> node = rect_list.getHead();
		for (int i = 0; (i < index)&&(node != null); i++) {
			node = node.getNext();
		}
		// Animate information
		String information = "Changing index " + index + " color from " + node.getData().getColor()+ " to " + c;
		s.setInfo(information);
		s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(new ChangeLabel(this, information, this));
		// Animate change of color
		s.addAnimator(new Animator.Builder().setDuration(time, TimeUnit.MILLISECONDS).build(), s.getFirstAnimator());
		s.getLastAnimator().addTarget(PropertySetter.getTarget(node.getData(), "colorDraw", node.getData().getColor(), c));
		// Trigger for a continuous animation
		s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
		// Log the Changes in Step
		s.addChanges(new Change("color", node.getData(), node.getData().getColor()));
		// Apply the changes to rect_list
		node.getData().setColor(c);
		steps.add(s);
	}

	/* function to stepBack from one step to a previous one.
	 * Will have to be extended whenever we add another API command
	 */
	private void changeBack(Change tempChange) {
		if (tempChange.getType() == "swap") {
			// just restore coordinates
			tempChange.getReference().getRec().x = tempChange.getPoint().x;
			tempChange.getReference().getRec().y = tempChange.getPoint().y;
		} else if (tempChange.getType() == "color") {
			// just restore the color
			tempChange.getReference().setColor(tempChange.getColor());
		} else if (tempChange.getType() == "modifyLabel") {
			// just restore the label
			tempChange.getReference().setLabel(tempChange.getLabel());
		} else if (tempChange.getType() == "add") {
			// remove the rectangle
			rect_list.remove(tempChange.getReference());
		} else if (tempChange.getType() == "deleteHead") {
			rect_list.addFirst(tempChange.getReference());
		}
	}
	
	/* function to stepForward from one step to the next one.
	 * Will have to be extended whenever we add another API command
	 */
	public void stepForward() {
		currentStep++; // increment the currentStep (because we're moving to the next one)
		if (steps.get(currentStep).getChanges().get(0).getType() == "add") {
			rect_list.addFirst(steps.get(currentStep).getChanges().get(0).getReference());
			this.repaint();
		}
		//catches the even when the pointers are switched
		if (steps.get(currentStep).getChanges().get(0).getType() == "pointerSwitch") {
			Node<Rect> p = steps.get(currentStep).getChanges().get(0).getNodeReference();
			rect_list.removeNode(p, p.getNext(),rectSpace(rectSize()));
			this.repaint();
		}
		//catches the event when the head is deleted
		if (steps.get(currentStep).getChanges().get(0).getType() == "deleteHead") {
			rect_list.removeFirst();
			this.repaint();
		}

		if (steps.get(currentStep).getList().size() != 0) { // if there are any Animators inside the step, trigger the first one
			steps.get(currentStep).getList().get(0).start();
		}
	}
	
	public void stepBack() {
		setInfo("");
		for(Change tempChange: steps.get(currentStep).getChanges()) {						// in each Step we are storing the values before the animation, so we
			changeBack(tempChange);															// restore it all like it was before this step and then decrement the
		}																					// currentStep value, as well as call Main.panel.repaint() to redraw
		this.repaint();																// the panel in order to show these changes
		currentStep--;
	}

    public void endAnimation(){
        while (currentStep != 0) {														// because of a few slight reasons, when we construct the animation, we end up at
		//setInfo(steps.get(currentStep).getInfo());								// the last step, so we need to return to the initial state
		for(Change tempChange: steps.get(currentStep).getChanges()) {
			changeBack(tempChange);
		}
		currentStep--;
        }
        new setupGUI(this);
    }
    
	//adds an element to the front of the list
	public void addToHead(int i){
		currentStep++;
		Step s = new Step();	
		
		// Create animation for showing information on what's happening
		String information = "Adding to head";
		s.setInfo(information);	

		Rect rec = new Rect(rect_list.getHead().getData().getRec().x-rectSpace(), rect_list.getHead().getData().getRec().y, rectSize(), rectSize(), i+"", this);
		rect_list.addFirst(rec);

		// Trigger for a continuous animation
		s.addAnimator(new Animator.Builder().setDuration(1, TimeUnit.MILLISECONDS).build(), nextBtn);
		s.getLastAnimator().addTarget(new ContinuousAnimation(currentStep+1, this));
		// Log the changes in the Step
		s.addChanges(new Change("add", rec, "Added a rectangle with value "+i));
		
		steps.add(s);	
	}


	/*an overriden method from JFrame: paints the actual rectangle on the screen. It is called each time timingEvent calls repaint() */
	protected void paintComponent(Graphics g){

		//counter++;

		Graphics g2g = (Graphics2D) g;
		((Graphics2D) g2g).setBackground(Color.WHITE);

		for(int x=0;x<10;x++){
			g2g.clearRect(0, 0, getWidth(), getHeight()); //clear all rectangles
		}

		//g2g.setColor(Color.BLUE);
		g2g.setColor(Color.BLUE);
		g2g.drawString(info,(int)(Fullscreen.width/4),(int)(Fullscreen.height*3/4));
		Font font = new Font("Arial", Font.PLAIN, (int) 20 * rectSize(windowWidth) / 72);
		g2g.setFont(font);
		
		
		//the linked list is converted into an array so that it can be worked with
		ArrayList<Rect> arr = new ArrayList<Rect>();
		arr = rect_list.toArray();
		
		/* fill the rectangles and draw a tag next to each one */
		
		for(Rect r : arr){
			g2g.setColor(r.getColor());
			g2g.fillRect(r.getRec().x, r.getRec().y,r.getRec().width,r.getRec().height);
			g2g.setColor(Color.WHITE);
			g2g.drawString(r.getLabel(), r.getRec().x+(rectSize(windowWidth)/2)-10, r.getRec().y+(rectSize(windowWidth)/2));
		}

		//draw pointers, including final NULL pointer
		for(int i=0;i<arr.size()-1;i++){
			g2g.setColor(Color.BLACK);
			g2g.drawLine((arr.get(i).getRec().x+40)+arr.get(i).getRec().width/2, arr.get(i).getRec().y+20+arr.get(i).getRec().height/2, arr.get(i+1).getRec().x-40 + arr.get(i+1).getRec().width/2, arr.get(i+1).getRec().y+20+arr.get(i+1).getRec().height/2);
			g2g.fillOval(arr.get(i+1).getRec().x-40 + arr.get(i+1).getRec().width/2, arr.get(i+1).getRec().y+15+arr.get(i+1).getRec().height/2, 10, 10);
		}

		g2g.drawLine((arr.get(arr.size()-1).getRec().x+40)+arr.get(arr.size()-1).getRec().width/2, arr.get(arr.size()-1).getRec().y+20+arr.get(arr.size()-1).getRec().height/2,      arr.get(arr.size()-1).getRec().x+rectSpace(rectSize(windowWidth))+30,arr.get(arr.size()-1).getRec().y+20+arr.get(arr.size()-1).getRec().height/2);
		g2g.setColor(Color.RED);
		g2g.fillOval(arr.get(arr.size()-1).getRec().x+rectSpace(rectSize(windowWidth))+30,arr.get(arr.size()-1).getRec().y+20+arr.get(arr.size()-1).getRec().height/2-5, 10, 10);

		/* draw the information 
		g2g.setColor(Color.BLUE);
		g2g.drawString(info,(int)(Fullscreen.width/4),(int)(Fullscreen.height*3/4));
                * */
	}	
}

