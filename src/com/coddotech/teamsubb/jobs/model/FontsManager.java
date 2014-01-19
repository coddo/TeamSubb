package com.coddotech.teamsubb.jobs.model;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.connection.model.FileDownloader;

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
	 * @param fontLinks
	 *            The collection of font links to be trimmed.
	 * @return A String collection
	 */
	public static String[] excludeSystemFonts(String[] fontLinks) {

		List<String> systemFonts = Arrays.asList(FontsManager.getSystemFonts());

		List<String> created = new ArrayList<String>();

		for (String link : fontLinks) {

			String name = FileDownloader.extractFileName(link);

			if (!systemFonts.contains(name))
				created.add(link);

		}

		return created.toArray(new String[created.size()]);

	}

	/**
	 * Remove all the occurences of the items in the second collection from the
	 * first collection
	 * 
	 * @param fontFiles
	 *            The collection of font files to be trimmed.
	 * @param toExclude
	 *            The collection of items to be removed from the first String[]
	 * @return A String[] collection
	 */
	public static File[] excludeServerFontsAsFiles(File[] fontFiles) {

		List<String> excludable = FontsManager.getServerFonts();

		List<File> created = new ArrayList<File>();

		for (File fontFile : fontFiles) {

			if (!excludable.contains(fontFile.getName()))
				created.add(fontFile);
		}

		return created.toArray(new File[created.size()]);

	}

	public static String[] excludeServerFontsAsStrings(String[] fonts) {
		
		List<String> excludable = FontsManager.getServerFonts();

		List<String> created = new ArrayList<String>();

		for (String font : fonts) {

			String name = new File(font).getName();
			
			if (!excludable.contains(name))
				created.add(font);

		}

		return created.toArray(new String[created.size()]);
	}

	private static List<String> getServerFonts() {
		String response = ConnectionManager.sendFontsRequest();

		return Arrays.asList(response.split(Pattern
				.quote(JobManager.SEPARATOR_FIELDS)));
	}

}
