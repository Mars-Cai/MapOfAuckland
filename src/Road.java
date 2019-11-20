import java.awt.Graphics;
import java.util.ArrayList;

public class Road {


	int roadID, type, speedLimit,roadClass;
	String roadName, roadCity;
	boolean oneway, notforcar, notforpede, notforbicy;
	ArrayList<Segment> segments = new ArrayList<Segment>();


	public Road(int roadID, int type, String roadName, String roadCity, boolean oneway, int speedLimit, int roadClass,boolean notforcar, boolean notforpede, boolean notforbicy) {
		this.roadID=roadID;
		this.type = type;
		this.roadName = roadName;
		this.roadCity = roadCity;
		this.oneway = oneway;
		this.speedLimit  = speedLimit;
		this.roadClass = roadClass;
		this.notforcar = notforcar;
		this.notforpede = notforpede;
		this.notforbicy = notforbicy;
	}

}
