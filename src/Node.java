import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Node {
	int nodeID, height, width;
	int size = 4;
	double lat, lon;
	Point point;
	Location loc;
	boolean highlight;
	ArrayList<Segment> ingoing = new ArrayList<Segment>();
	ArrayList<Segment> outgoing = new ArrayList<Segment>();

	public Node(int nodeID, double lat, double lon) {
		this.nodeID = nodeID;
		this.lat = lat;
		this.lon = lon;
		this.loc = Location.newFromLatLon(lat, lon);
		this.outgoing = new ArrayList<Segment>();
	}

	public String toString() {
		return this.nodeID + " " + this.lat + " " + this.lon;
	}

	public void draw(Graphics g, Location origin, double scale) {
		point = new Point(loc.asPoint(origin, scale));
		if(highlight)
			g.setColor(Color.RED);
		else
			g.setColor(Color.GREEN);
		g.drawOval((int) point.x-(size/2), (int) point.y-(size/2), size, size);
		g.fillOval((int) point.x-(size/2), (int) point.y-(size/2), size, size);
	}

	public void highlight(boolean b) {
		this.highlight = b;
	}

	public void move(String string) {
		switch (string) {
			case "up":		loc = loc.moveBy(0, -10);break;
			case "down":	loc = loc.moveBy(0, 10);break;
			case "left":	loc = loc.moveBy(10, 0);break;
			case "right":	loc = loc.moveBy(-10, 0);break;
		}
	}
}
