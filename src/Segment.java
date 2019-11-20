import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Segment {
	int roadID, width, height;
	double length;
	Node from, to;
	boolean highlight;
	ArrayList<Location> coords = new ArrayList<Location>();

	public Segment(int roadID, double length, Node from, Node to, ArrayList<Location> coords) {
		this.roadID = roadID;
		this.length = length;
		this.from = from;
		this.to = to;
		this.coords = coords;
	}

	public void draw(Graphics g, Location origin, double scale) {
		if(highlight)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);

		Location start, end;
		Point startPoint, endPoint;

		for (int i = 0; i < coords.size() - 1; i++) {
			start = coords.get(i);
			end = coords.get(i + 1);
			startPoint = start.asPoint(origin, scale);
			endPoint = end.asPoint(origin, scale);
			g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
		}

	}

	public void highlight(boolean b) {
		this.highlight = b;
	}

	public void move(String string) {
		for (int i = 0; i < coords.size(); i++) {
			switch (string) {
			case "up":		coords.set(i, (coords.get(i).moveBy(0, -10))); break;
			case "down":	coords.set(i, (coords.get(i).moveBy(0, 10))); break;
			case "left":	coords.set(i, (coords.get(i).moveBy(10, 0))); break;
			case "right":	coords.set(i, (coords.get(i).moveBy(-10, 0))); break;
			}
		}
	}
}
