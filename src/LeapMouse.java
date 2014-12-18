import java.awt.Dimension;
import java.awt.Robot;

import com.leapmotion.leap.*;

class CustomListener extends Listener{
	public Robot robot;
	public void onConnect (Controller c)
	{
		c.enableGesture(Gesture.Type.TYPE_CIRCLE);
		c.enableGesture(Gesture.Type.TYPE_CIRCLE);
		c.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
	}
	public void onFrame(Controller c)
	{
		try{robot=new Robot();} catch (Exception e) {}
		Frame frame= c.frame();
		InteractionBox box= frame.interactionBox();
		for (Finger f : frame.fingers())
		{
			if(f.type()==Finger.Type.TYPE_INDEX){
				Vector fingerPos=f.tipPosition();
				Vector boxfingerPos=box.normalizePoint(fingerPos);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				robot.mouseMove((int)(screen.width*boxfingerPos.getX()),(int)(screen.height-boxfingerPos.getY()*screen.height));
			}
		}
		
	}
}
public class LeapMouse {

	public static void main(String argus[])
	{
		CustomListener L = new CustomListener();
		Controller c = new Controller();
		c.addListener(L);
		
		try{
			System.in.read();
		}catch (Exception e){}c.removeListener(L);
	}
}
