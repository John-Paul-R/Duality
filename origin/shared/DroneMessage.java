package origin.shared;
import java.io.Serializable;

//LinkedList
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.RobotStatus;
import robocode.StatusEvent;

public class DroneMessage implements Serializable
{
	private static final long serialVersionUID = 2887372454693590904L;
	
	public DroneMessage() {}
	
	private BulletHitBulletEvent bhb;
    private BulletHitEvent bh;
    private BulletMissedEvent bm;
    private DeathEvent d;
    private HitByBulletEvent hbb;
    private HitRobotEvent hr;
    private RobotStatus s;
    private long time;

    public void onBulletHitBullet(BulletHitBulletEvent e) 	{bhb = e;}
    public void onBulletHit(BulletHitEvent e) 				{bh = e;}
    public void onBulletMissed(BulletMissedEvent e)			{bm = e;}
    public void onDeath(DeathEvent e) 						{d = e;}
    public void onHitByBullet(HitByBulletEvent e) 			{hbb = e;}
    public void onHitRobot(HitRobotEvent e) 				{hr = e;}
    public void onStatus(RobotStatus e, long t) 			{s = e; time = t;}

    public BulletHitBulletEvent getBulletHitBullet() 	{return bhb;}
    public BulletHitEvent getBulletHit() 				{return bh;}
    public BulletMissedEvent getBulletMissed()			{return bm;}
    public DeathEvent getDeath() 						{return d;}
    public HitByBulletEvent getHitByBullet() 			{return hbb;}
    public HitRobotEvent getHitRobot() 					{return hr;}
    public RobotStatus getStatus()						{return s;}
    public long getTime()								{return time;}

}
