package com.coddotech.teamsubb.connection.model;

import java.io.File;
import java.net.URI;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.coddotech.teamsubb.jobs.model.LinkParser;

/**
 * Module for downloading files
 * 
 * @author Coddo
 * 
 */
public class FileDownloader {

	/**
	 * 
	 * @param fileName
	 *            The name of the file to be created
	 * @param link
	 *            The link from where the file has to be fetched
	 * @param dir
	 *            The directory path where to save the file
	 * @return A File entity representing the downloaded file
	 * @throws Exception
	 */
	public static File downloadFile(String fileName, String link, String dir)
			throws Exception {

		File file = new File(dir + File.separator + fileName);

		URI uri = new URI("http", "anime4fun.ro", link.split(Pattern
				.quote("anime4fun.ro"))[1], null);

		FileUtils.copyURLToFile(uri.toURL(), file);

		return file;
	}

	/**
	 * 
	 * @param fileName
	 *            The names of the files to be created
	 * @param link
	 *            The links from where the files have to be fetched
	 * @param dir
	 *            The directory path where to save the file
	 * @return A collection of File entities representing the downloaded files
	 * @throws Exception
	 */
	public static File[] downloadFiles(String[] fileNames, String[] links,
			String dir) throws Exception {

		File[] files = new File[links.length];

		for (int i = 0; i < links.length; i++)
			files[i] = FileDownloader.downloadFile(fileNames[i], links[i], dir);

		return files;
	}

	/**
	 * Download a certain file from the web
	 * 
	 * @param fileData
	 *            A String containing the name of the file to be downloaded and
	 *            the url from where it can be fetched.<b> The format is:
	 *            file_name=file_URL.
	 * 
	 * @param dir
	 *            The directory path where to save the file
	 * 
	 * @return A File entity representing the downloaded file
	 * 
	 * @throws Exception
	 */
	public static File downloadFile(String fileData, String dir)
			throws Exception {

		String[] data = LinkParser.splitFileData(fileData);

		return FileDownloader.downloadFile(data[0], data[1], dir);

	}

	/**
	 * Download multiple files from the web
	 * 
	 * @param filesData
	 *            A collection of Strings each containing the name of the file
	 *            to be downloaded and the url from where it can be fetched.<b>
	 *            The format is: file_name=file_URL
	 * @param dir
	 *            The directory path where to save the files
	 * 
	 * @return A collection of File entities representing the downloaded file
	 * 
	 * @throws Exception
	 */
	public static File[] downloadFiles(String[] filesData, String dir)
			throws Exception {

		File[] files = new File[filesData.length];

		for (int i = 0; i < filesData.length; i++)
			files[i] = FileDownloader.downloadFile(filesData[i], dir);

		return files;

	}
}
