package com.coddotech.teamsubb.gadget.model;

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.coddotech.teamsubb.main.XmlHandler;

/**
 * Class for managing size profiles for the gadget
 * 
 * This class is a singleton.
 * 
 * @author Coddo
 * 
 */
public class GadgetProfiler extends XmlHandler {

	private class Profile {

		String name;

		int sizeFactor;
		int polygon;
		int offsetX;
		int offsetY;
	}

	private Profile[] profiles = null;
	private int selected;

	private static GadgetProfiler instance = null;

	private GadgetProfiler() {
		createXMLComponents("GadgetProfiling.xml");
		fetchProfiles();
	}

	public static GadgetProfiler getInstance() {
		if (instance == null)
			instance = new GadgetProfiler();

		return instance;
	}

	/**
	 * Get the ecuation defining a circle with the set radius
	 * 
	 * @param r
	 *            The radius of the circle
	 * @param offsetX
	 *            Horizontal offset
	 * @param offsetY
	 *            Vertical offset
	 * @return The polygon ecuation for the circle
	 */
	public static int[] generateCircle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];

		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;

			int y = (int) Math.sqrt(r * r - x * x);

			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;

		}

		return polygon;
	}

	public void select(int index) {
		selected = index;
	}

	public String[] getProfiles() {
		String[] names = new String[profiles.length];

		for (int i = 0; i < profiles.length; i++)
			names[i] = profiles[i].name;

		return names;
	}

	public int getSizeFactor() {
		return profiles[selected].sizeFactor;
	}

	public int getPolygon() {
		return profiles[selected].polygon;
	}

	public Point getOffset() {
		return new Point(profiles[selected].offsetX, profiles[selected].offsetY);
	}

	public void fetchProfiles() {
		if (profiles == null) {

			NodeList nodes = xmlFile.getElementsByTagName("profile");

			profiles = new Profile[nodes.getLength()];

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				Profile profile = new Profile();

				profile.name = element.getAttribute("name");
				profile.sizeFactor = Integer.parseInt(element.getAttribute("size_factor"));
				profile.polygon = Integer.parseInt(element.getAttribute("polygon"));
				profile.offsetX = Integer.parseInt(element.getAttribute("offset_x"));
				profile.offsetY = Integer.parseInt(element.getAttribute("offset_y"));

				profiles[i] = profile;
			}
		}
	}
}
