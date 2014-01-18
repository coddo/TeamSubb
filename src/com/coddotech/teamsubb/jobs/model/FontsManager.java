package com.coddotech.teamsubb.jobs.model;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontsManager {

	/**
	 * Retrieve a list containing all the system fonts
	 * 
	 * @return A String collection
	 */
	public static String[] getSystemFonts() {
		Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAllFonts();

		String[] fontNames = new String[fonts.length];

		for (int i = 0; i < fonts.length; i++)
			fontNames[i] = fonts[i].getName();

		return fontNames;
	}

	/**
	 * Exclude all the system fonts from the list
	 * 
	 * @param fonts
	 *            The font list to be trimmed
	 * @return A String collection
	 */
	public static String[] separateSystemFonts(String[] fonts) {

		return FontsManager.excludeFonts(fonts,
				Arrays.asList(FontsManager.getSystemFonts()));

	}

	/**
	 * Remove all the occurences of the items in the second collection from the
	 * first collection
	 * 
	 * @param fonts
	 *            The collection to be trimmed
	 * @param toExclude
	 *            The collection of items to be removed from the first String[]
	 * @return A String[] collection
	 */
	public static String[] separateFontsCollections(String[] fonts,
			String[] toExclude) {

		return FontsManager.excludeFonts(fonts, Arrays.asList(toExclude));

	}

	private static String[] excludeFonts(String[] fonts, List<String> toExclude) {

		List<String> fontList = new ArrayList<String>();

		for (String font : fonts) {

			if (!toExclude.contains(font))
				fontList.add(font);

		}

		return fontList.toArray(new String[fontList.size()]);

	}

}
