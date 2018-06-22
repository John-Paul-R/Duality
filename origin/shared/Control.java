package origin.shared;

import java.awt.geom.Point2D;

import robocode.TeamRobot;

public class Control
{
	public static void aimTo(Point2D.Double coords, TeamRobot robot)
	{
		robot.setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(
				Math.atan2(coords.getX()-robot.getX(), coords.getY()-robot.getY())
				- robot.getGunHeadingRadians()));
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