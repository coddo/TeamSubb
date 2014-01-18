package com.coddotech.teamsubb.jobs.model;

import java.util.regex.Pattern;

public class LinkParser {

	/**
	 * Convert a file link into an easily accessible data string. <br>
	 * 
	 * Output structure: file_nama + SEPARATOR + file_link - as a concatenated
	 * entity
	 * 
	 * @param link
	 *            The link for the file
	 * @return A String
	 */
	public static String parseFileLink(String link) {
		String[] split = link.split(Pattern.quote("/"));

		String name = split[split.length - 1];

		return name + JobManager.SEPARATOR_FIELDS + link;
	}

	/**
	 * Convert a file links into easily accessible data strings. <br>
	 * 
	 * Output structures: file_nama + SEPARATOR + file_link - as a concatenated
	 * entity
	 * 
	 * @param link
	 *            The link for the file
	 * @return A String collection
	 */
	public static String[] parseFileLinks(String[] links) {

		for (int i = 0; i < links.length; i++)
			links[i] = LinkParser.parseFileLink(links[i]);

		return links;
	}

	/**
	 * Split the the file entities parsed by this class into separate strings
	 * 
	 * @param fileData
	 *            A String which has the structure of a parsed link
	 * 
	 * @return A String
	 */
	public static String[] splitFileData(String fileData) {
		String separator = Pattern.quote(JobManager.SEPARATOR_FIELDS);

		return fileData.split(separator);
	}
}
