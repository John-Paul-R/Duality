package origin.leader;

import java.awt.geom.Point2D;
import java.util.concurrent.ConcurrentHashMap;


import origin.shared.DataManager;
import robocode.Rules;
import origin.shared.Enemy;
import origin.twin.Duality_Leader;

public class Radar
{
	private Duality_Leader _self;
	private DataManager _data;
	
	public Radar(Duality_Leader self, DataManager data)
	{
		_self = self;
		_data = data;
	}
	
	public void execute() 
	{
/*		Enemy target = _data.getTargetEnemy();
		final double FACTOR = 2.1;
		if (target != null && target.getTime() == _data.getSelf().getTime())
    	{
			double absBearing = target.getBearing();
			_self.setTurnRadarRightRadians( FACTOR * robocode.util.Utils.normalRelativeAngle(absBearing - _self.getRadarHeadingRadians()) );
    	}
		else*/
		oldestScanned();
	}


	private Enemy oldestScanned = null;
	private long oldestScannedAge = -1;
	private double turnAmountRemaining = 0;
	public void oldestScanned()
	{
		long currentTime = _self.getTime();
		String nOldestScannedName = _data.getOldestName();
		Enemy nOldestScanned = _data.getEnemy(nOldestScannedName);
		System.out.println("HI THERE");
		turnAmountRemaining -= (Math.PI/4);
		if (nOldestScanned != null)
		{
	    	if (nOldestScanned != oldestScanned || oldestScannedAge != nOldestScanned.getAge(_self.getTime()))
	    	{
				oldestScanned = nOldestScanned;
				oldestScannedAge = nOldestScanned.getAge(currentTime);
	    	}
		}
		if (oldestScanned != null && oldestScanned.getAge(currentTime) > 5)
		{
			_self.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		}
		else if (oldestScanned != null /*&& turnAmountRemaining <= 0*//* && nOldestScanned != oldestScanned*/)
		{
			double relX = oldestScanned.getX()-_self.getX();
			double relY = oldestScanned.getY()-_self.getY();
			double absBearing = Math.atan2(relX, relY);
			double radarTurnToTarget = robocode.util.Utils.normalRelativeAngle(absBearing - _self.getRadarHeadingRadians());
			long timeSinceOldestScan = oldestScanned.getAge(currentTime);
			double mea = calcSimpleMEA(_self.getLocation(), oldestScanned.getLocation(), Rules.MAX_VELOCITY, (int) Math.signum(radarTurnToTarget), timeSinceOldestScan);
			
			double turnAngle = robocode.util.Utils.normalRelativeAngle(mea - _self.getRadarHeadingRadians()); //radarTurnToTarget; 
			turnAmountRemaining = Math.abs(turnAngle);
			_self.setTurnRadarRightRadians(turnAngle);
		}
		else
		{
			_self.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		}
	
	
	}
	public static double calcSimpleMEA(Point2D reference, Point2D target, double velocity, int angularDirectionSignnum, long time)
	{
		double relX = target.getX()-reference.getX();
		double relY = target.getY()-reference.getY();
		double bearing = Math.atan2(relX, relY);
		double perpendicular = bearing + Math.PI/2;
		
		double maxEscapeDistance = velocity * angularDirectionSignnum * time;
		double maxRelEscapeX = maxEscapeDistance * Math.sin(perpendicular) + relX;
		double maxRelEscapeY = maxEscapeDistance * Math.cos(perpendicular) + relY;
		double maxEscapeAngle = Math.atan2(maxRelEscapeX, maxRelEscapeY);
		
		return maxEscapeAngle;
	}
}