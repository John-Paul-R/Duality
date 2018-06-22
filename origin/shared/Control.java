package origin.shared;

import java.awt.geom.Point2D;

import robocode.Rules;
import robocode.TeamRobot;

public class Control
{
	public static void aimTo(Point2D.Double coords, TeamRobot robot)
	{
		robot.setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(
				Math.atan2(coords.getX()-robot.getX(), coords.getY()-robot.getY())
				- robot.getGunHeadingRadians()));
	}
	public static Point2D.Double getAimLocation(Enemy target, TeamRobot self) 
	{
    	double turnRate = target.getTurn();
    	double relX = target.getX() - self.getX();
    	double relY = target.getY() - self.getY();
    	double cHeading = target.getHeading();
    	double velocity = target.getVelocity();
    	
    	double selfX = self.getX();
    	double selfY = self.getY();
    	
    	double deltaTime = 0;
    	double dataAgeOffset = self.getTime() - target.getTime();
    	while (dataAgeOffset > 0)
    	{
    		relX += (velocity * Math.sin(cHeading + turnRate));
    		relY += (velocity * Math.cos(cHeading + turnRate));
    		cHeading += turnRate;
    		dataAgeOffset -= 1;
    	}
    	
    	final double bulletVelocity = Rules.getBulletSpeed(1.9);
    	   	
    	while ((bulletVelocity * deltaTime) < Math.abs(Point2D.distance(0, 0, relX, relY)))
    	{
    		relX += (velocity * Math.sin(cHeading + turnRate));
    		relY += (velocity * Math.cos(cHeading + turnRate));
    		cHeading += turnRate;
    		deltaTime += 1;
    	}
		
    	//Limit coordinate to map
    	Point2D.Double targetCoord = (new Point2D.Double(
    			Math.max(Math.min(relX + selfX, self.getBattleFieldWidth()), 0),
    			Math.max(Math.min(relY + selfY, self.getBattleFieldHeight()), 0)));
    	return targetCoord;
	}
	public static void goTo(Point2D.Double coords, TeamRobot robot)
	{
		double x = coords.getX();
		double y = coords.getY();
		x -= robot.getX();
		y -= robot.getY();
	 

		double targetAbsBearing = Math.atan2(x, y);
		double turnToTarget = robocode.util.Utils.normalRelativeAngle(targetAbsBearing - robot.getHeadingRadians());

		double dist = Math.hypot(x, y);
	 
		/* This is a simple method of performing "set front as back" */
		double turnAngle = Math.atan(Math.tan(turnToTarget));
		robot.setTurnRightRadians(turnAngle);
		if(turnToTarget == turnAngle) {
			robot.setAhead(dist);
		} else {
			robot.setBack(dist);
		}

    }
}