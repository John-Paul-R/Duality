package origin.leader;

import origin.shared.BotState;
import origin.shared.DroneMessage;
import origin.shared.Enemy;
import origin.shared.EnemyState;
import origin.twin.DualityLeader;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.KeyEvent;
import robocode.MessageEvent;
import robocode.MouseEvent;
import robocode.PaintEvent;
import robocode.RobotDeathEvent;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.StatusEvent;
import robocode.TeamRobot;
import robocode.WinEvent;

public class DataManager
{
	private TeamRobot _self;
	private HashMap<String, Enemy> _enemies;
	private LinkedList<BotState> _selfData;
	private LinkedList<DroneMessage> _droneData;
	private BotState cSelf;
	private Enemy _target;
	private String _targetName;
	public DataManager() {}
	
	public void init(DualityLeader self) 
	{
		_self = self;
		_enemies = new HashMap<String, Enemy>();
		_selfData = new LinkedList<BotState>();
		_droneData = new LinkedList<DroneMessage>();
	}
	
	//Do
	public void execute() // Note:  It is almost always optimal to run the 'execute()' method for DataManager before doing anything else in your main while loop
	{
		cSelf = new BotState(_self.getX(), _self.getY(), _self.getEnergy(), _self.getHeadingRadians(), _self.getVelocity(), _self.getTime());
		_selfData.add(cSelf);
		chooseTarget();
	}
	public void onScannedRobot(ScannedRobotEvent e)
	{
		if (!(_self.isTeammate(e.getName()) &&!_enemies.containsKey(e.getName())))
		{
			addEnemy(e);
		}
		addEnemyState(e);
	}
	public void addEnemy(ScannedRobotEvent e)
	{
		Enemy enemy = new Enemy();
		_enemies.put(e.getName(), enemy);
	}
	public void addEnemyState(ScannedRobotEvent e)
	{
		Enemy enemy = _enemies.get(e.getName());
        double eX = (cSelf.getX() + Math.sin((_self.getHeadingRadians() + e.getBearingRadians()) % (2*Math.PI)) * e.getDistance());
        double eY = (cSelf.getY() + Math.cos((_self.getHeadingRadians() + e.getBearingRadians()) % (2*Math.PI)) * e.getDistance());
		EnemyState scanData = new EnemyState(eX, eY, e.getEnergy(), e.getHeadingRadians(), e.getVelocity(), e.getTime(), e.getDistance(), e.getBearingRadians() + cSelf.getHeading());
		enemy.addState(scanData);
	}
	public void chooseTarget() //possibly change this so that it learns who is likely to be the leader (Keeping in mind that not all teams have a leader)
	{//Also, it may be beneficial to choose the closest between the two bots... (then again, that could make it easier to kill the leader, so maybe not)
        String cClosestName = "";
        Enemy cClosestEnemy = null;
        double cClosestDist = Integer.MAX_VALUE;
        for (Entry<String, Enemy> entry : getEnemies().entrySet())
        {
            if (entry.getValue().getDistance() < cClosestDist)
            {
                cClosestName = entry.getKey();
                cClosestEnemy = entry.getValue();
                cClosestDist = cClosestEnemy.getDistance();
            }
        }
        _target = cClosestEnemy;
        _targetName = cClosestName;
	}	


	
	//Get
	public BotState getSelf()
		{ return cSelf; }

	public Enemy getEnemy(String name)
		{ return _enemies.get(name); }

	public EnemyState getEnemyState(String name)
		{ return _enemies.get(name).getState(); }

	public EnemyState getEnemyState(String name, int index)
		{ return _enemies.get(name).getState(index); }
	
	public Enemy getTargetEnemy() 
	{
		return _target;
	}
	public String getTargetName() 
	{
		return _targetName;
	}
	public HashMap<String, Enemy> getEnemies() 
	{
		return _enemies;
	}
	public String getOldestName()
	{
		String oldestName = "";
		long oldestTime = 0;
		for (Entry<String, Enemy> entry : _enemies.entrySet())
		{
			long entryTime = entry.getValue().getAge(_self.getTime());
			if ( entryTime > oldestTime)
			{
				
				oldestName = entry.getKey();
				oldestTime = entryTime;
			
			}
/*			else if (entryTime == oldestTime && _enemies.get(oldestName) != null)
			{
				if (entry.getValue().getScanOrder() < _enemies.get(oldestName).getScanOrder())
				{
					oldestName = entry.getKey();
					oldestTime = entryTime;
				}
			}*/
			 
		}
		return oldestName;
	}
	//EVENTS (Note that some events (onStatus) will trigger before the DataManager is initialized)
	public void onMessageRecieved(MessageEvent e) 
	{
		_droneData.add((DroneMessage) e.getMessage());
	}
	public RobotStatus getDroneState()
	{

		if (_droneData.size() > 0)
		{
			RobotStatus lastStatus = _droneData.getLast().getStatus();
			if (lastStatus != null)
			{
				return lastStatus;
			}
		}
				
		return null;
	}

	public void onBattleEnded(BattleEndedEvent e) {}
	public void onBulletHitBullet(BulletHitBulletEvent e) {}
	public void onBulletHit(BulletHitEvent e) {}
	public void onBulletMissed(BulletMissedEvent e) {}
	public void onDeath(DeathEvent e) {}
	public void onHitByBullet(HitByBulletEvent e) {}
	public void onHitRobot(HitRobotEvent e) {}
	public void onHitWall(HitWallEvent e) {}
	public void onPaint(PaintEvent e) {}
	public void onRobotDeath(RobotDeathEvent e) {}
	public void onSkippedTurn(SkippedTurnEvent e) {}
	//public void onStatus(StatusEvent e) {}
	public void onWin(WinEvent e) {}
}
