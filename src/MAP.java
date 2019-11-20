
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This is a small example class to demonstrate extending the GUI class and
 * implementing the abstract methods. Instead of doing anything maps-related, it
 * draws some squares to the drawing area which are removed when clicked. Some
 * information is given in the text area, and pressing any of the navigation
 * buttons makes a new set of squares.
 *
 * @author tony
 */
public class MAP extends GUI {
	static Map<Integer, Node> nodesMap = new HashMap<Integer, Node>();
	static Map<Integer, Road> roadsMap = new HashMap<Integer, Road>();
	static Map<Integer, Segment> segsMap = new HashMap<Integer, Segment>();
	static Set<Polygon> polygon = new HashSet<Polygon>();
	Trie trie = new Trie();
	private double scale;
	Location origin, topLeft, botRight;
	int width, height;
	Node selectNode = null;
	ArrayList<Segment> selectSeg = new ArrayList<Segment>();
	ArrayList<Segment> allSegs= new ArrayList<Segment>();


	@Override
	protected void redraw(Graphics g) {//call draw method in each class
		for (Node n : nodesMap.values()) {
			n.draw(g, origin, scale);
		}
		for (Segment s : allSegs) {
			s.draw(g, origin, scale);
		}
		for (Polygon p : polygon) {
			p.draw(g, origin, scale);
		}
	}

	@Override
	protected void onClick(MouseEvent e) {
		/* highlight the chosen intersection and print out the information around
		 * print out name and ID to distinguish different with same name
		 */
		String output = "";
		List<String> text = new ArrayList<String>();
		int range = 6; // in case the graph too small to click
		for (Node n : nodesMap.values()) {
			if (e.getX() < (n.point.x + range) && e.getX() > (n.point.x - range) && e.getY() < (n.point.y + range)
					&& e.getY() > (n.point.y - range)) {
				if (selectNode != null)
					selectNode.highlight(false);
				selectNode = n;
				selectNode.highlight(true);
				getTextOutputArea().append("\nIntersection ID: " + n.nodeID);
				for (Segment s : n.ingoing) {
					for (Road r : roadsMap.values()) {
						if (r.roadID == s.roadID )
							text.add(r.roadName+"("+r.roadID+")");
					}
				}
				for (Segment s : n.outgoing) {
					for (Road r : roadsMap.values()) {
						if (r.roadID == s.roadID )
							text.add(r.roadName+"("+r.roadID+")");
					}
				}
				for (String s : text) {
					output += ", " + s;
				}
				break;
			}
		}
		getTextOutputArea().append(output);
	}

	@Override
	protected void onSearch() {//highlight every road with with same content
		String context = getSearchBox().getText();
		getTextOutputArea().setText("");
		for(Segment s : selectSeg)
			s.highlight(false);
		ArrayList<String> results = trie.search(context);
		if(results ==null)
			getTextOutputArea().setText("\nNothing found");
		for(int i=0;i<results.size();i++) {
			getTextOutputArea().append("\n"+results.get(i));
			for(Road r:roadsMap.values()) {
				if(r.roadName == results.get(i)) {
					for(Segment s:r.segments) {
						selectSeg.add(s);
						s.highlight(true);
					}
				}
			}
		}
	}

	@Override
	protected void onMove(Move m) {
		if (m == Move.ZOOM_IN)
			scale *= 1.2;
		if (m == Move.ZOOM_OUT)
			scale /= 1.2;

		for (Node n : nodesMap.values()) {
			if (m == Move.NORTH)
				n.move("up");
			if (m == Move.SOUTH)
				n.move("down");
			if (m == Move.WEST)
				n.move("left");
			if (m == Move.EAST)
				n.move("right");
		}

		for (Segment s : allSegs) {
			if (m == Move.NORTH)
				s.move("up");
			if (m == Move.SOUTH)
				s.move("down");
			if (m == Move.WEST)
				s.move("left");
			if (m == Move.EAST)
				s.move("right");
		}

		for (Polygon p : polygon) {
			if (m == Move.NORTH)
				p.move("up");
			if (m == Move.SOUTH)
				p.move("down");
			if (m == Move.WEST)
				p.move("left");
			if (m == Move.EAST)
				p.move("right");
		}
	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		/* read each file and get data
		 * match every data from correct position
		 */
		getTextOutputArea().setText("Loading...");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nodes));
			String line;
			double topLat = -36.847622;
			double botLat = -36.847622;
			double leftLon = 174.763444;
			double rightLon = 174.763444;
			for (line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] values = line.split("\t");
				int nodeID = Integer.parseInt(values[0]);
				double lat = Double.parseDouble(values[1]);
				double lon = Double.parseDouble(values[2]);
				Node node = new Node(nodeID, lat, lon);
				nodesMap.put(nodeID, node);// to get the topLeft point and the botRight point
				if (lat > topLat)
					topLat = lat;
				if (lat < botLat)
					botLat = lat;
				if (lon < leftLon)
					leftLon = lon;
				if (lon > rightLon)
					rightLon = lon;
			}
			topLeft = Location.newFromLatLon(topLat, leftLon);
			botRight = Location.newFromLatLon(botLat, rightLon);
			origin = Location.newFromLatLon(topLat, leftLon);
			scale = 500 / (topLeft.y - botRight.y);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(roads));
			String line;
			reader.readLine(); // skip the first line
			for (line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] values = line.split("\t");
				int roadID = Integer.parseInt(values[0]);
				int type = Integer.parseInt(values[1]);
				String roadName = values[2];
				String roadCity = values[3];
				boolean oneway = Boolean.parseBoolean(values[4]);
				int speedLimit = Integer.parseInt(values[5]);
				int roadClass = Integer.parseInt(values[6]);
				boolean notforcar = Boolean.parseBoolean(values[7]);
				boolean notforpede = Boolean.parseBoolean(values[8]);
				boolean notforbicy = Boolean.parseBoolean(values[9]);
				Road road = new Road(roadID, type, roadName, roadCity, oneway, speedLimit, roadClass, notforcar,
						notforpede, notforbicy);
				roadsMap.put(roadID, road);
				trie.add(roadName);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(segments));
			String line;
			reader.readLine(); // skip the first line
			for (line = reader.readLine(); line != null; line = reader.readLine()) {
				ArrayList<Location> coords = new ArrayList<Location>();
				String[] values = line.split("\t");
				int roadID = Integer.parseInt(values[0]);
				Road road = roadsMap.get(roadID);
				double length = Double.parseDouble(values[1]);
				Node from = nodesMap.get(Integer.parseInt(values[2]));
				Node to = nodesMap.get(Integer.parseInt(values[3]));
				for (int i = 4; i < values.length; i += 2) {
					Location l = Location.newFromLatLon(Double.parseDouble(values[i]),
							Double.parseDouble(values[i + 1]));
					coords.add(l);
				}
				Segment seg = new Segment(roadID, length, from, to, coords);
				segsMap.put(roadID, seg);
				allSegs.add(seg);
				road.segments.add(seg);
				from.ingoing.add(seg);
				to.outgoing.add(seg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(polygons));
			String line = null,label = null;
			int type = 0;
			int endLevel = 0,cityIdx = 0;
			List<Location> location;
			List<String> coords;
			while ((line = reader.readLine()) != null) {
				if(line.contains("Type"))
					type=Integer.decode(line.split("=")[1]);
				if(line.contains("EndLevel"))
					endLevel = Integer.decode(line.split("=")[1]);
				if(line.contains("CityIdx"))
					cityIdx = Integer.decode(line.split("=")[1]);
				if(line.contains("Label"))
					label = line.split("=")[1];
				if(line.contains("Data")) {
					String coord = line.split("=")[1];
					coord = coord.substring(1,coord.lastIndexOf(')'));
					coords = Arrays.asList(coord.split("\\),\\("));
					location = new ArrayList<Location>();
					for(String s : coords) {
						location.add(Location.newFromLatLon(Double.parseDouble(s.split(",")[0]), Double.parseDouble(s.split(",")[1])));
					}
					polygon.add(new Polygon(type,endLevel,cityIdx,label,location));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getTextOutputArea().setText("That's all.");
	}

	public static void main(String[] args) {
		new MAP();
	}
}

