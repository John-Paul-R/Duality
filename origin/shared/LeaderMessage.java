package origin.shared;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class LeaderMessage implements Serializable
{
	private static final long serialVersionUID = 819498407676368739L;
	
	private Point2D.Double dest;
    private Point2D.Double aim;

    public LeaderMessage() {}

    public void setDest(Point2D.Double destCoord) 	{ dest = destCoord; }
    public void setAim(Point2D.Double aimCoord) 	{ this.aim = aimCoord; }
    
    public Point2D.Double getDest()		{return dest;}
    public Point2D.Double getAim()		{return aim;}
}