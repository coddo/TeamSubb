package com.coddotech.teamsubb.jobs.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.connection.model.FileDownloader;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class FontsManager extends Observable {

	public static final String FONTS_ADD = "add fonts";

	private static File FONTS_WINDOWS = null;
	private static File[] FONTS_LINUX = null;

	private static enum Platform {
		Windows, Linux;
	}

	private static Platform operatingSystem = null;

	/**
	 * Copy all the selected fonts to the job's directory
	 * 
	 * @param fonts
	 *            List of font paths (String[])
	 * @param job
	 *            The job entity which will contain the fonts
	 */
	public void addCustomFonts(String[] fonts, Job job) {
		File[] fontFiles = new File[fonts.length];

		if (fonts.length == 0)
			fontFiles = null;

		boolean ok = true;

		for (int i = 0; i < fonts.length; i++) {

			File source = new File(fonts[i]);

			fontFiles[i] = new File(job.getDirectoryPath() + File.separator + source.getName());

			try {

				if (!fontFiles[i].exists())
					FileUtils.copyFile(source, fontFiles[i]);

			}

			catch (Exception ex) {
				ok = false;

			}
		}

		if (ok)
			ok = job.setAddedFonts(fontFiles);

		NotificationEntity notif = new NotificationEntity(FONTS_ADD, ok);

		this.setChanged();
		this.notifyObservers(notif);

	}

	/**
	 * Retrieve a list containing all the system fonts
	 * 
	 * @return A String collection
	 */
	public static String[] getSystemFonts() {
		generateFontPaths();

		List<String> fontNames = new ArrayList<String>();

		switch (FontsManager.getOS()) {

			case Windows: {
				fontNames = Arrays.asList(FONTS_WINDOWS.list());

			}
				break;

			case Linux: {

				for (File fontFolder : FONTS_LINUX) {

					for (File font : FileUtils.listFiles(fontFolder, null, true))
						fontNames.add(font.getName());

				}

			}
				break;

		}

		if (fontNames.size() == 0)
			return null;

		return fontNames.toArray(new String[fontNames.size()]);
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

		String[] sysf = FontsManager.getSystemFonts();

		if (sysf == null)
			return fontLinks;

		List<String> systemFonts = Arrays.asList(sysf);

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

		String[] fonts = (response == null) ? null : response.split(Pattern.quote(JobManager.SEPARATOR_DATA));

		return (fonts == null) ? null : Arrays.asList(fonts);
	}

	/**
	 * Get the operating system under which this app is running
	 * 
	 * @return A Platform entity
	 */
	private static Platform getOS() {
		if (operatingSystem == null) {
			String os = System.getProperty("os.name").toLowerCase();

			if (os.indexOf("win") >= 0)
				operatingSystem = Platform.Windows;

			if (os.indexOf("nux") >= 0)
				operatingSystem = Platform.Linux;

		}

		return operatingSystem;
	}

	/**
	 * Create the File entities representing the font folders for each operating system
	 */
	private static void generateFontPaths() {
		String properties = System.getProperties().toString();

		if (FontsManager.getOS() == Platform.Windows)
			if (FONTS_WINDOWS == null) {
				String mainDrive = extractWindowsDrive(properties);

				FONTS_WINDOWS = new File(mainDrive + "Windows" + File.separator + "Fonts");
			}

		if (FontsManager.getOS() == Platform.Linux)
			if (FONTS_LINUX == null) {
				FONTS_LINUX = new File[3];

				String sharedFontsPath = File.separator + "share" + File.separator + "fonts";
				String userFontsPath = System.getProperty("user.name") + File.separator + ".fonts";

				FONTS_LINUX[0] = new File(File.separator + "usr" + sharedFontsPath);
				FONTS_LINUX[1] = new File(File.separator + "usr" + File.separator + "local" + sharedFontsPath);
				FONTS_LINUX[2] = new File(File.separator + "home" + File.separator + userFontsPath);

				// Create the user fonts path if it doesn't exist
				if (!FONTS_LINUX[2].exists())
					FONTS_LINUX[2].mkdir();
			}
	}

	/**
	 * Read the windows drive letter from the Windows Properties string.
	 * 
	 * @param properties
	 *            The string representing containing all the Windows properties
	 * 
	 * @return A String value representing the drive letter in which the Windows OS is installed. <br>
	 *         For example: C:\
	 */
	private static String extractWindowsDrive(String properties) {
		int index = properties.indexOf("Windows" + File.separator + "system32");

		while (properties.charAt(index) != ';')
			index--;

		index++;

		return properties.charAt(index) + ":" + File.separator;
	}
}
