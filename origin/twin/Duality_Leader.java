package origin.twin;

import java.awt.geom.Point2D;
import java.io.IOException;

import origin.leader.Radar;
import origin.shared.DataManager;
import origin.shared.DroneMessage;
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
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class Duality_Leader extends TeamRobot
{
    private DataManager _data;
    private Radar _radar;
    
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
			try {
				Point2D.Double targetCoord = _data.getTargetEnemy().getLocation();//Control.getAimLocation(_data.getTargetEnemy(), this);
				message.setAim(targetCoord);
				Control.aimTo(targetCoord, this);
			} catch (NullPointerException e)
			{
				message.setAim(null);
			}
			message.setDest(new Point2D.Double(this.getBattleFieldWidth()/2, this.getBattleFieldHeight()/2));
			
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
			execute();
        }
    }

    public void onMessageRecieved(MessageEvent e) 			{_data.onMessageRecieved(e);}
    
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
	}

	public Point2D getLocation()
	{
		return new Point2D.Double(getX(), getY());
	}
}
