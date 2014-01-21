package com.coddotech.teamsubb.jobs.model;

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
		String[] fontNames;

		File fontsDir = null;

		String osName = System.getProperty("os.name").split(" ")[0];

		if (osName.equals("Windows"))
			fontsDir = new File("C:" + File.separator + "Windows" + File.separator + "Fonts");

		fontNames = fontsDir.list();

		return fontNames;
	}

	/**
	 * Exclude all the system fonts from the list
	 * 
	 * @param fontLinks
	 *            The collection of font links to be trimmed.
	 * 
	 * @return A String collection
	 */
	public static String[] excludeSystemFonts(String[] fontLinks) {

		if (fontLinks == null)
			return null;

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
	 * Exclude all the fonts that are found on the server.
	 * 
	 * @param fontFiles
	 *            The collection of font files to be trimmed.
	 * 
	 * @return A File[] collection
	 */
	public static File[] excludeServerFontsAsFiles(File[] fontFiles) {

		if (fontFiles == null || fontFiles.length == 0)
			return null;

		List<String> excludable = FontsManager.getServerFonts();

		List<File> created = new ArrayList<File>();

		for (File fontFile : fontFiles) {

			if (!excludable.contains(fontFile.getName()))
				created.add(fontFile);
		}

		return created.toArray(new File[created.size()]);

	}

	/**
	 * Exclude all the fonts that are found on the server.
	 * 
	 * @param fonts
	 *            The fonts to be trimmed
	 * 
	 * @return A String collection
	 */
	public static String[] excludeServerFontsAsStrings(String[] fonts) {

		if (fonts == null || fonts.length == 0)
			return null;

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

		return Arrays.asList(response.split(Pattern.quote(JobManager.SEPARATOR_FIELDS)));
	}

}
