package origin.twin;

import java.awt.geom.Point2D;
import java.io.IOException;

import origin.leader.DataManager;
import origin.leader.Radar;
import origin.shared.DroneMessage;
import origin.shared.Enemy;
import origin.shared.LeaderMessage;
import origin.shared.Control;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.PaintEvent;
import robocode.RobotDeathEvent;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class DualityLeader extends TeamRobot
{
    private DataManager _data;
    private Radar _radar;
    private double ab, h;

    
    public void run()
    {
        _data = new DataManager();
        _data.init(this);
        _radar = new Radar(this, _data);

        while(true)
        {
            _data.execute();
			_radar.execute();
			LeaderMessage message = new LeaderMessage();
		    Enemy target = _data.getTargetEnemy();

			if (target != null)
			{
				Point2D.Double targetCoord = _data.getTargetEnemy().getLocation();//Control.getAimLocation(_data.getTargetEnemy(), this);
				RobotStatus droneState = _data.getDroneState();

				if (droneState != null)
				{
						System.out.println("DroneState is not null!");
					    Point2D.Double dronePos = new Point2D.Double(droneState.getX(),droneState.getY());
					    double droneEnemyAB = Math.atan2(target.getX()-dronePos.getX(), target.getY()-dronePos.getY());
					    double droneDriveDirection = droneEnemyAB + Math.PI/2; 
					    Point2D.Double droneDest = null;
					    if (droneState.getDistanceRemaining() == 0)
					    {
						    double direction = 150 * Math.signum(Math.random()-.5);
					    	droneDest = new Point2D.Double(direction*Math.sin(droneDriveDirection)+dronePos.getX(),
					    			direction*Math.cos(droneDriveDirection)+dronePos.getY());
					    }
					    message.setDest(droneDest);
						//message.setDest(droneDest);
		
				}/*{
						message.setAim(null);
					}*/
				message.setAim(targetCoord);

				Control.aimTo(targetCoord, this);

			}
			try {
				sendMessage(getTeammates()[0], message);
				//System.out.println("Sending LeaderMessage");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to send LeaderMessage");
			}
			if (getGunTurnRemaining() < Math.PI/4 && getGunHeat() == 0 && _data.getTargetEnemy() != null)
			{
				setFire(1.9);
			}
		    setTurnRightRadians(ab + Math.PI/2 -h); 
		    if (getDistanceRemaining() == 0)
		    	setAhead(150 * Math.signum(Math.random()-.5));


			execute();
        }
    }

    public void onMessageReceived(MessageEvent e) 			{
    	_data.onMessageRecieved(e);
    	System.out.println("Message Received");
    }
	public void onBulletHitBullet(BulletHitBulletEvent e) 	{_data.onBulletHitBullet(e);}
	public void onBulletHit(BulletHitEvent e) 				{_data.onBulletHit(e);}
	public void onBulletMissed(BulletMissedEvent e)			{_data.onBulletMissed(e);}
	public void onDeath(DeathEvent e) 						{_data.onDeath(e);}
	public void onHitByBullet(HitByBulletEvent e) 			{_data.onHitByBullet(e);}
	public void onHitRobot(HitRobotEvent e) 				{_data.onHitRobot(e);}
	public void onHitWall(HitWallEvent e) 					{_data.onHitWall(e);}
	public void onPaint(PaintEvent e) 						{_data.onPaint(e);}
	public void onRobotDeath(RobotDeathEvent e) 			{_data.onRobotDeath(e);}
	public void onScannedRobot(ScannedRobotEvent e) 		
	{
		if (!isTeammate(e.getName()))
		{
			_data.onScannedRobot(e);
		}
		h = getHeadingRadians();
        ab = e.getBearingRadians() + h;
	}

	public Point2D getLocation()
	{
		return new Point2D.Double(getX(), getY());
	}
}
