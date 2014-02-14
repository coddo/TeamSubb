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
	public static File downloadFile(String fileName, String protocol, String domain, String fileLinkPart,
			String dir) throws Exception {

		File file = new File(dir + File.separator + fileName);

		// URI uri = new URI("http", "anime4fun.ro", link.split(Pattern.quote("anime4fun.ro"))[1],
		// null);
		URI uri = new URI(protocol, domain, fileLinkPart, null);

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

		String fileName = extractFileName(link);
		String protocol = extractProtocol(link);
		String domain = extractDomain(link, protocol);
		String fileLink = extractFileLink(link, domain);

		return downloadFile(fileName, protocol, domain, fileLink, dir);
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
		File[] files = new File[links.length];

		for (int i = 0; i < files.length; i++)
			files[i] = downloadFile(links[i], dir);

		return files;
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

	private static String extractProtocol(String link) {
		return link.split(Pattern.quote("://"))[0];
	}

	private static String extractDomain(String link, String protocol) {
		String aux = link.replaceAll(protocol, "");
		aux = aux.replace(aux.substring(0, 3), "");

		return aux.substring(0, aux.indexOf("/"));
	}

	private static String extractFileLink(String link, String domain) {
		return link.split(Pattern.quote(domain))[1];
	}

}
