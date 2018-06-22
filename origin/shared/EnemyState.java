package origin.shared;

public class EnemyState extends BotState
{
	private double _absBearing;
	private double _distance;

	public EnemyState(double x, double y, double energy, double headingRadians, double velocity, long time, double distance, double absBearing) {
		super(x, y, energy, headingRadians, velocity, time);
		_distance = distance;
		_absBearing = absBearing;
	}
	
	public double getDistance()
	{
		return _distance;
	}
	public double getBearing()
	{
		return _absBearing;
	}

}
