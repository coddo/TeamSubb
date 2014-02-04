package com.coddotech.teamsubb.connection.model;

import java.io.File;
import java.net.URI;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * Module for downloading files
 * 
 * @author Coddo
 * 
 */
public class FileDownloader {

	/**
	 * Download a file from the web.
	 * 
	 * @param fileName
	 *            The name of the file to be created
	 * 
	 * @param link
	 *            The link from where the file has to be fetched
	 * 
	 * @param dir
	 *            The directory path where to save the file
	 * 
	 * @return A File entity representing the downloaded file
	 * 
	 * @throws Exception
	 */
	public static File downloadFile(String fileName, String link, String dir) throws Exception {

		File file = new File(dir + File.separator + fileName);

		URI uri = new URI("http", "anime4fun.ro", link.split(Pattern.quote("anime4fun.ro"))[1], null);

		FileUtils.copyURLToFile(uri.toURL(), file);

		return file;
	}

	/**
	 * Download a file from the web.
	 * 
	 * @param link
	 *            The link from where the file has to be fetched
	 * 
	 * @param dir
	 *            The directory path where to save the file
	 * 
	 * @return A File entity representing the downloaded file
	 * 
	 * @throws Exception
	 */
	public static File downloadFile(String link, String dir) throws Exception {

		String fileName = FileDownloader.extractFileName(link);

		return FileDownloader.downloadFile(fileName, link, dir);
	}

	/**
	 * Download multiple files from the web.
	 * 
	 * @param fileNames
	 *            The names of the files to be created
	 * 
	 * @param links
	 *            The links from where the files have to be fetched
	 * 
	 * @param dir
	 *            The directory path where to save the file
	 * @return A collection of File entities representing the downloaded files
	 * 
	 * @throws Exception
	 */
	public static File[] downloadFiles(String[] fileNames, String[] links, String dir) throws Exception {

		File[] files = new File[links.length];

		for (int i = 0; i < links.length; i++)
			files[i] = FileDownloader.downloadFile(fileNames[i], links[i], dir);

		return files;
	}

	/**
	 * Download multiple files from the web.
	 * 
	 * @param links
	 *            The links from where the files have to be fetched
	 * 
	 * @param dir
	 *            The directory path where to save the file
	 * 
	 * @return A collection of File entities representing the downloaded files
	 * 
	 * @throws Exception
	 */
	public static File[] downloadFiles(String[] links, String dir) throws Exception {

		String[] fileNames = new String[links.length];

		for (int i = 0; i < links.length; i++)
			fileNames[i] = FileDownloader.extractFileName(links[i]);

		return FileDownloader.downloadFiles(fileNames, links, dir);
	}

	/**
	 * Extract the file name from a link.
	 * 
	 * @param link
	 *            The link containing the file name
	 * 
	 * @return A String value
	 */
	public static String extractFileName(String link) {
		String[] data = link.split(Pattern.quote("/"));

		return data[data.length - 1];
	}

}
