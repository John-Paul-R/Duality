package origin.twin;

import java.awt.geom.Point2D;
import java.io.IOException;

import origin.shared.DroneMessage;
import origin.shared.Control;
import origin.shared.LeaderMessage;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.Droid;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.PaintEvent;
import robocode.StatusEvent;
import robocode.TeamRobot;

public class Duality_Spectre extends TeamRobot implements Droid
{
	private DroneMessage md = new DroneMessage();
	private LeaderMessage lm = null;
    private String format;
    public void run()
    {
		format = "[%d] %s\n";

    	for(;;)
    	{
			//For message ordering, set onStatus to be the last event called in the drone bot.
			//Then, set onMessageRecieved to happen as the last event in the leader. (Essentially, ensure that the message is sent before the leader checks for messages)
			//Note: check to see if this actully works.  Its actually fairly likely that event prioritization only changes the way that the robots recieve this information, not when the server processes the events.  (in which case, this would likely not have an effect on whether or not the message is recieved)
			//After doing research, if the messages are indeed always handled in the same order, then you must make sure that the message is sent fromt the drone before onMessage is processed in the server.  Check when that is.  If it is after all of the other events, good! that works as is.  Just thought about this. Event ordering actually fixes this problem, I think.
			//False alarm.  Just order the leader's event to come after the drone sends the message and it *should* be fine (at least in my head...)  

    		//set target from leader 
    		if (lm != null)
    		{
    			if (lm.getAim() != null)
    				System.out.println("AIM RECEIVED");
    			if (lm.getDest() != null)
    				System.out.println("DEST RECEIVED");
    		}
    			
    		/*try {
    			Control.aimTo(lm.getAim(), this);
        		Control.goTo(lm.getDest(), this);	
        	} catch (NullPointerException e) {
        		System.out.printf(format, "No LeaderMessage");
			}*/
    		try {
				broadcastMessage(md);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		md = new DroneMessage();
    		lm = null;
    		execute();
    	}
        //I *think* it'll reduce codesize if I just execute everything in onMessage()
    }
    //Everything from these methods should be send to Duality_Leader via messages.
    @Override
    public void onMessageReceived(MessageEvent e)
    {
    	lm = (LeaderMessage) e.getMessage();
    	System.out.printf(format, getTime(), "Message Recieved");
    }
	public void onBulletHitBullet(BulletHitBulletEvent e) 	{md.onBulletHitBullet(e);}
	public void onBulletHit(BulletHitEvent e) 				{md.onBulletHit(e);}
	public void onBulletMissed(BulletMissedEvent e)			{md.onBulletMissed(e);}
	public void onDeath(DeathEvent e) 						{md.onDeath(e);}
	public void onHitByBullet(HitByBulletEvent e) 			{md.onHitByBullet(e);}
	public void onHitRobot(HitRobotEvent e) 				{md.onHitRobot(e);}
	public void onHitWall(HitWallEvent e) 					{md.onHitWall(e);}
	public void onPaint(PaintEvent e) 						{}
	public void onStatus(StatusEvent e)						{md.onStatus(e);}
}
