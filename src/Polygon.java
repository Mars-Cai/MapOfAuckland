import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
	int type;
	String label;
	int endLevel, cityIdx;
	List<Location> location;

	public Polygon(int type, int endLevel, int cityIdx, String label, List<Location> location) {
		this.type = type;
		this.endLevel = endLevel;
		this.cityIdx = cityIdx;
		this.label = label;
		this.location = location;
	}

	public void draw(Graphics g, Location origin, double scale) {
		/*
		 * according to type, giving different color to draw
		 * fill all polygon point by point with two array
		 */
		if (type <= 19 && type >= 1)
			g.setColor(Color.decode("0xC7C9C8"));
		else if (type <= 39 && type >= 20)
			g.setColor(Color.decode("#cbe6a3"));
		else
			g.setColor(Color.decode("#a3ccff"));

		int[] xPoints = new int[location.size()];
		int[] yPoints = new int[location.size()];
		int i = 0;

		for (Location location : location) {
			xPoints[i] = location.asPoint(origin, scale).x;
			yPoints[i] = location.asPoint(origin, scale).y;
			i++;
		}
		g.fillPolygon(xPoints, yPoints, i);
	}

	public void move(String string) {
		for (int i = 0; i < location.size(); i++) {
			switch (string) {
			case "up":		location.set(i, (location.get(i).moveBy(0, -10))); break;
			case "down":	location.set(i, (location.get(i).moveBy(0, 10))); break;
			case "left":	location.set(i, (location.get(i).moveBy(10, 0))); break;
			case "right":	location.set(i, (location.get(i).moveBy(-10, 0))); break;
			}
		}
	}
}
