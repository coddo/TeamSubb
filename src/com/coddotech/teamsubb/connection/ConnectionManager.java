package com.coddotech.teamsubb.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.coddotech.teamsubb.jobs.Job;
import com.coddotech.teamsubb.jobs.JobManager;

/**
 * This class is used for bridging the connection between the server and the
 * user of this instance of the client app.<br>
 * 
 * It operates as a static class: it doesn't contain a constructor and all the
 * methods and fields are accessed in a static way.
 * 
 * @author Coddo
 * 
 */

public final class ConnectionManager {

	private static final String URL_USER_LOGGING = "http://anime4fun.ro/wlogin.php";
	private static final String URL_JOBS = "http://anime4fun.ro/jobs.php";

	// private static final String URL_CHAT = "http://anime4fun.ro/chat.php";

	/**
	 * Check whether there is any connection to the internet or not
	 * 
	 * @return A logical value representing the connection state
	 */
	public static boolean isConnected() {
		if (ConnectionManager.sendMessage(ConnectionManager.URL_JOBS,
				new String[] { "" }, new String[] { "" }).equals("error"))
			return false;
		else
			return true;
	}

	/**
	 * Sends a login request to the server using the entered user details
	 * 
	 * @param user
	 *            The username as it is registered on the server
	 * @param pass
	 *            The password for this user
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error
	 *            message in case the connection to the server fails
	 * @return A string containing the login result (false if wrong credentials
	 *         or user_details if good credentials). This resturns the message
	 *         "error" if a connection problem is encountered
	 */
	public static String sendLoginRequest(String user, String pass) {
		String response = ConnectionManager.sendMessage(
				ConnectionManager.URL_USER_LOGGING, new String[] { "user",
						"pass" }, new String[] { user, pass });

		// for server debugging purposes
		System.out.println("\nLogin response:\n");
		System.out.println(response);

		return response;
	}

	/**
	 * Sends a logout request to the server using the specified details
	 * 
	 * @param user
	 *            The username of the staff member to be logged out from the
	 *            server
	 */
	public static void sendLogoutRequest(String user) {
		ConnectionManager.sendMessage(ConnectionManager.URL_USER_LOGGING,
				new String[] { "logout" }, new String[] { user });
	}

	/**
	 * Sends a job search request to the server in order to find suitable jobs
	 * for the currently logged in staff memeber
	 * 
	 * @param user
	 *            The name of the staff member that wiches to find a job
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error
	 *            message in case the connection to the server fails
	 * @return A String containing the details for the jobs if any available
	 *         ones are found. Returns the message "error" if a connection
	 *         problem is encountered
	 */
	public static String sendJobSearchRequest(String user) {
		String response = ConnectionManager.sendMessage(
				ConnectionManager.URL_JOBS, new String[] { "jobs", "staff" },
				new String[] { "search", user });

		// for server debugging purposes
		System.out.println("\nJob search response:\n");
		System.out.println(response);

		return response;
	}

	/**
	 * Sends a request to the server in order to create a job with the specified
	 * information
	 * 
	 * @param user
	 *            The name of the user who creates this job
	 * @param name
	 *            The name of the job
	 * @param type
	 *            The type that the job needs
	 * @param description
	 *            The comments that come along with the job
	 * @param subFile
	 *            The file representing the sub (main file)
	 * @param fonts
	 *            Any font files that are needed to finish the job
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error
	 *            message in case the connection to the server fails
	 * @return A Logical value telling the user if the request was accepted or
	 *         not by the server
	 */
	public static boolean sendJobCreateRequest(String user, String name,
			int type, String description, String nextStaff, String subFile,
			String[] fonts) {
		// user info handling
		String[] messageHeaders = { "jobs", "staff", "jobname", "jobtype",
				"comments", "nextstaff" };
		String[] messages = { "create", user, name, Integer.toString(type),
				description, nextStaff };

		// files handling
		String response;

		String[] fileHeaders;
		String[] files;

		if (fonts != null) {
			fileHeaders = new String[fonts.length + 1];
			files = new String[fileHeaders.length];

			fileHeaders[0] = "sub";
			files[0] = subFile;

			if (fonts.length > 0) {
				for (int i = 1; i < files.length; i++) {
					fileHeaders[i] = "font" + i;
					files[i] = fonts[i - 1];
				}
			}
		} else {
			fileHeaders = new String[] { "sub" };
			files = new String[] { subFile };
		}

		// request sending
		response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS,
				messageHeaders, messages, fileHeaders, files);

		// for server debugging purposes
		System.out.println("\nJob create response:\n");
		System.out.println(response);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to accept a certain job for this
	 * user
	 * 
	 * @param jobID
	 *            The ID of the job that you want to undertake
	 * @param user
	 *            The name of the user that wants to take the job
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error
	 *            message in case the connection to the server fails
	 * @return A Logical value telling the user if the request was accepted or
	 *         not by the server
	 */
	public static boolean sendJobAcceptRequest(int jobID, String user) {
		String[] messageHeaders = { "acceptjob", "staff" };
		String[] messages = { Integer.toString(jobID), user };

		String response = ConnectionManager.sendMessage(
				ConnectionManager.URL_JOBS, messageHeaders, messages);

		// for server debugging purposes
		System.out.println("\nJob accept response:\n");
		System.out.println(response);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to cancel/abort a certain job for
	 * this user
	 * 
	 * @param job
	 *            The Job entity representing the job to be canceled
	 * @param user
	 *            The name of the user that cancels the job
	 * @return A Logical value telling the user if the request was accepted or
	 *         not by the server
	 */
	public static boolean sendJobCancelRequest(Job job, String user) {
		return ConnectionManager.sendJobPushRequest(job, user, true);
	}

	/**
	 * Send a request to the server in order to forcibly cancel a job, without
	 * sending its data back to the server
	 * 
	 * @param jobID
	 *            The ID of the job to be canceled
	 * @param user
	 *            The name of the user who cancels the job
	 * @return A Logical value telling the user if the request was accepted or
	 *         not by the server
	 */
	public static boolean sendJobForceCancelRequest(int jobID, String user) {
		String[] headers = new String[] { "push", "staff", "jobid" };
		String[] messages = new String[] { "force", user,
				Integer.toString(jobID) };

		String response = ConnectionManager.sendMessage(
				ConnectionManager.URL_JOBS, headers, messages);

		// for server debugging purposes
		System.out.println("\nJob force cancel response:\n");
		System.out.println(response);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send the data of a job that the user has been working on back to the
	 * server
	 * 
	 * @param job
	 *            The Job entity representing the job data to be sent back to
	 *            the server
	 * @param user
	 *            The name of the user that sends the job
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error
	 *            message in case the connection to the server fails
	 * @return A Logical value telling the user if the request was accepted or
	 *         not by the server
	 */
	public static boolean sendJobPushRequest(Job job, String user,
			boolean canceled) {
		// Text messages data
		String[] messageHeaders = { "push", "staff", "jobid", "jobtype",
				"comments", "nextstaff" };
		String[] messages = { "available", user, Integer.toString(job.getID()),
				Integer.toString(job.getType()), job.getDescription(),
				job.getNextStaffMember() };

		if (canceled)
			messages[0] = "canceled";

		// files data
		String response;

		String[] fileHeaders;
		String[] files;

		if (job.getAddedFonts() != null) {
			fileHeaders = new String[job.getAddedFonts().length + 1];
			files = new String[fileHeaders.length];

			fileHeaders[0] = "sub";
			files[0] = job.getSubFile().getAbsolutePath();

			for (int i = 1; i < files.length; i++) {// create fonts collection
				fileHeaders[i] = "font" + i;
				files[i] = job.getAddedFonts()[i - 1].getAbsolutePath();
			}

		} else {
			fileHeaders = new String[] { "sub" };
			files = new String[] { job.getSubFile().getAbsolutePath() };
		}

		// send the request to the server and wait for a response
		response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS,
				messageHeaders, messages, fileHeaders, files);

		// for server debugging purposes
		System.out.println("\nJob push response:\n");
		System.out.println(response);

		// return the servers response
		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to mark a job as finished and make
	 * it disappear from the global list (on the server)
	 * 
	 * @param jobID
	 *            The ID of the job to be ended
	 * @param user
	 *            The name of the user that wants to end the job
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error
	 *            message in case the connection to the server fails
	 * @return A Logical value telling the user if the request was accepted or
	 *         not by the server
	 */
	public static boolean sendJobEndRequest(int jobID, String user) {
		// message data
		String[] messageHeaders = { "push", "staff", "jobid" };
		String[] messages = { "end", user, Integer.toString(jobID) };

		// send the request to the server and wait for a response
		String response = ConnectionManager.sendMessage(
				ConnectionManager.URL_JOBS, messageHeaders, messages);

		// for server debugging purposes
		System.out.println("\nJob end response:\n");
		System.out.println(response);

		// return the response from the server
		return Boolean.parseBoolean(response);
	}

	public static String sendStaffRequest() {
		return ConnectionManager.sendMessage(ConnectionManager.URL_JOBS,
				new String[] { "liststaff" }, new String[] { "1" });
	}

	/**
	 * Download a certain file from the web
	 * 
	 * @param fileData
	 *            A String containing the name of the file to be downloaded and
	 *            the url from where it can be fetched<b> The format is:
	 *            file_name=file_URL
	 * @param dir
	 *            The directory path where to save the file
	 * @return A File entity representing the downloaded file
	 * @throws Exception
	 */
	public static File downloadFile(String fileData, String dir)
			throws Exception {
		String[] nameURLContainer = fileData.split(Pattern
				.quote(JobManager.SEPARATOR_FIELDS));

		File file = new File(dir + File.separator + nameURLContainer[0]);
		// URL fileURL = new URL(URLEncoder.encode(nameURLContainer[1],
		// "UTF-8"));

		URI uri = new URI("http", "anime4fun.ro",
				nameURLContainer[1].split(Pattern.quote("anime4fun.ro"))[1],
				null);
		FileUtils.copyURLToFile(uri.toURL(), file);

		return file;
	}

	/**
	 * Send a message to a server using a MultiPartEntity as the parameter
	 * collection to be sent along with the message. This type of entity can
	 * contains both text data and file data (files)
	 * 
	 * @param url
	 *            The link which contains the location for the server which will
	 *            receive the request
	 * @param data
	 *            An entity containg the parameters to be sent with the message.
	 *            It can contain both plain text and files of all kinds
	 * @return A String value containing the response that has been received
	 *         from the server<br>
	 *         This method returns "false" if a connection error has been
	 *         encountered
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static String sendMessage(String url, MultipartEntity data)
			throws IllegalStateException, IOException {
		// create a http client used as an interface for interacting with the
		// server
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		// the POST type message to be sent to the server
		HttpPost httpPost = new HttpPost(url);

		// set the MultiPartEntity (containing text commands and files <if any>
		// ) for this message
		httpPost.setEntity(data);

		// The response from the server will be stored in this HttpResponse
		// variable
		HttpResponse response;

		// send the message to the server
		response = httpClient.execute(httpPost);

		// convert the entire result in a single string variable
		String result = ConnectionManager.readResult(response.getEntity()
				.getContent());

		// disconnect the client from the server
		httpClient.getConnectionManager().shutdown();

		// dispose unnecessary objects
		httpClient = null;
		response = null;
		httpPost = null;
		data = null;

		// return the server's result
		return result;
	}

	/**
	 * Send a text message to a server with the entered parameters
	 * 
	 * @param url
	 *            The link which contains the location for the server which will
	 *            receive the request
	 * @param messageHeaders
	 *            A String collection containing the parameters for each message
	 * @param messages
	 *            A String collection which contains the values for the
	 *            parameters specified in the Headers String collection
	 * @return A String value containing the response that has been received
	 *         from the server<br>
	 *         This method returns "false" if a connection error has been
	 *         encountered
	 */
	private static String sendMessage(String url, String[] messageHeaders,
			String[] messages) {
		try {

			// create a MultiPart entity which will contain the text message,
			// representing the request that will be made to the server
			MultipartEntity data = new MultipartEntity();

			for (int i = 0; i < messages.length; i++) {
				data.addPart(messageHeaders[i], new StringBody(messages[i]));
			}

			return ConnectionManager.sendMessage(url, data);

		} catch (Exception ex) {
			return "false";
		}
	}

	/**
	 * Send a message to a server with the entered parameters (containing both
	 * text and files)
	 * 
	 * @param url
	 *            The link which contains the location for the server which will
	 *            receive the request
	 * @param messageHeaders
	 *            A String collection containing the parameters for each message
	 * @param messages
	 *            A String collection which contains the values for the
	 *            parameters specified in the Headers String collection
	 * @param fileHeaders
	 *            A String collection containing the parameters for each file
	 * @param files
	 *            A String collection containing the file locations on the disk,
	 *            corresponding to each file Header
	 * @return A String value containing the response that has been received
	 *         from the server<br>
	 *         This method returns "false" if a connection error has been
	 *         encountered
	 */
	private static String sendMessage(String url, String[] messageHeaders,
			String[] messages, String[] fileHeaders, String[] files) {
		try {

			// create a MultiPart entity which will contain the text message and
			// files, representing the request that will be made to the server
			MultipartEntity data = new MultipartEntity();

			for (int i = 0; i < messages.length; i++) {
				data.addPart(messageHeaders[i], new StringBody(messages[i]));
			}

			for (int i = 0; i < files.length; i++) {
				data.addPart(fileHeaders[i], new FileBody(new File(files[i])));
			}

			return ConnectionManager.sendMessage(url, data);

		} catch (Exception ex) {
			return "false";
		}
	}

	/**
	 * Read the result details from a specific request made to the server
	 * 
	 * @param stream
	 *            An ImputStream variable containing the result received after a
	 *            request was made to a certain server
	 * @return A String value containing the result received from this request
	 * @throws IOException
	 */
	private static String readResult(InputStream stream) throws IOException {
		String result = "";

		// read the result of the request
		BufferedReader input = new BufferedReader(new InputStreamReader(stream));

		// recreate the result of the string
		String inputLine;
		while ((inputLine = input.readLine()) != null) {
			result += inputLine;
		}

		// dispose of unnecessary objects
		input.close();
		input = null;
		inputLine = null;

		// return the result
		return result;
	}

}
