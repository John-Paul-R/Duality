package origin.shared;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Enemy
{
	private LinkedList<EnemyState> _states;
	private EnemyState cState;
	public Enemy()
	{
		_states = new LinkedList<EnemyState>();
	}


	
	public void addState(EnemyState state) 
	{
		cState = state;
		_states.add(state);
	}
	
	public EnemyState getState()
	{
		return cState;
	}
	public EnemyState getPreviousState()
	{
		return cState;
	}
	public EnemyState getState(int index)
	{
		return _states.get(index);
	}
	
	
	//Getters
	public double getX() {
		return cState.getX();
	}

	public double getY() {
		return cState.getY();
	}

	public double getEnergy() {
		return cState.getEnergy();
	}

	public double getHeading() {
		return cState.getHeading();
	}

	public double getVelocity() {
		return cState.getVelocity();
	}

	public long getTime() {
		return cState.getTime();
	}

	public int cIndex() 
	{
		return _states.size()-1;
	}
	public double getTurn()
	{
		double prevHeading = getState(cIndex()-1).getHeading();
		return  cState.getHeading() - prevHeading;
	}
	
	public double getBearing()
	{
		return cState.getBearing();
	}
	public Point2D.Double getLocation() 
	{
		return cState.getLocation();
	}
	
	public double getDistance() 
	{
		return cState.getDistance();
	}
	
	public long getAge(long time) {
		return time-getTime();
	}
}
