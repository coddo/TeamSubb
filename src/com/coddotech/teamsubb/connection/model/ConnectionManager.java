package com.coddotech.teamsubb.connection.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.LoggedUser;
import com.coddotech.teamsubb.jobs.model.Job;

/**
 * This class is used for bridging the connection between the server and the
 * user of this instance of the client app.<br>
 * It operates as a static class: it doesn't contain a constructor and all the
 * methods and fields are accessed in a static way.
 * 
 * @author Coddo
 */

public final class ConnectionManager {

	private static final String URL_USER_LOGGING = "http://anime4fun.ro/wlogin.php";
	private static final String URL_JOBS = "http://anime4fun.ro/jobs.php";
	private static final String URL_CHAT = "http://anime4fun.ro/chat.php";

	/**
	 * Sends a login request to the server using the entered user details
	 * 
	 * @param user
	 *            The username as it is registered on the server
	 * 
	 * @param pass
	 *            The password for this user
	 * 
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error message in case the
	 *            connection to the server fails
	 * 
	 * @return A string containing the login result (false if wrong credentials or user_details if
	 *         good credentials). This resturns the message "error" if a connection problem is
	 *         encountered
	 */
	public static String sendLoginRequest(String user, String pass) {

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Login request", "SEND");

		String[] messageHeaders = new String[] { "user", "pass" };
		String[] message = new String[] { user, pass };

		String response = ConnectionManager.sendMessage(ConnectionManager.URL_USER_LOGGING, messageHeaders, message);

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

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Logout request", "SEND");

		String[] messageHeaders = new String[] { "logout" };
		String[] message = new String[] { user };

		ConnectionManager.sendMessage(ConnectionManager.URL_USER_LOGGING, messageHeaders, message);
	}

	/**
	 * Sends a job search request to the server in order to find suitable jobs
	 * for the currently logged in staff memeber
	 * 
	 * @param user
	 *            The name of the staff member that wiches to find a job
	 * 
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error message in case the
	 *            connection to the server fails
	 * 
	 * @return A String containing the details for the jobs if any available ones are found. Returns
	 *         the message "error" if a connection problem is encountered
	 */
	public static String sendJobSearchRequest(String user) {

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job search request", "SEND");

		String[] messageHeaders = new String[] { "jobs", "staff" };
		String[] message = new String[] { "search", user };

		String response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, messageHeaders, message);

		return response;
	}

	/**
	 * Sends a request to the server in order to create a job with the specified information
	 * 
	 * @param user
	 *            The name of the user who creates this job
	 * 
	 * @param name
	 *            The name of the job
	 * 
	 * @param type
	 *            The type that the job needs
	 * 
	 * @param description
	 *            The comments that come along with the job
	 * 
	 * @param subFile
	 *            The file representing the sub (main file)
	 * 
	 * @param fonts
	 *            Any font files that are needed to finish the job
	 * 
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error message in case the
	 *            connection to the server fails
	 * 
	 * @return A Logical value telling the user if the request was accepted or not by the server
	 */
	public static boolean sendJobCreateRequest(String user, String name, int type, String description,
			String nextStaff, String torrent, String subFile, String[] fonts) {

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job create request", "SEND");

		// user info handling
		String[] messageHeaders = { "jobs", "staff", "jobname", "jobtype", "comments", "nextstaff", "torrent" };
		String[] messages = { "create", user, name, Integer.toString(type), description, nextStaff, torrent };

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
		}
		else {
			fileHeaders = new String[] { "sub" };
			files = new String[] { subFile };
		}

		// request sending
		response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, messageHeaders, messages, fileHeaders,
				files);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to accept a certain job for this user
	 * 
	 * @param jobID
	 *            The ID of the job that you want to undertake
	 * 
	 * @param user
	 *            The name of the user that wants to take the job
	 * 
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error message in case the
	 *            connection to the server fails
	 * 
	 * @return A Logical value telling the user if the request was accepted or not by the server
	 */
	public static boolean sendJobAcceptRequest(int jobID, String user) {

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job accept request", "SEND");

		String[] messageHeaders = { "acceptjob", "staff" };
		String[] messages = { Integer.toString(jobID), user };

		String response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, messageHeaders, messages);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to cancel/abort a certain job for this user
	 * 
	 * @param job
	 *            The Job entity representing the job to be canceled
	 * 
	 * @param user
	 *            The name of the user that cancels the job
	 * 
	 * @return A Logical value telling the user if the request was accepted or not by the server
	 */
	public static boolean sendJobCancelRequest(Job job, String user) {
		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job cancel request", "SEND");

		return ConnectionManager.sendJobPushRequest(job, user, true);
	}

	/**
	 * Send a request to the server in order to forcibly cancel a job, without sending its data back
	 * to the server
	 * 
	 * @param jobID
	 *            The ID of the job to be canceled
	 * 
	 * @param user
	 *            The name of the user who cancels the job
	 * 
	 * @return A Logical value telling the user if the request was accepted or not by the server
	 */
	public static boolean sendJobForceCancelRequest(int jobID, String user) {
		String[] headers = new String[] { "push", "staff", "jobid" };
		String[] messages = new String[] { "force", user, Integer.toString(jobID) };

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job force cancel request", "SEND");

		String response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, headers, messages);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send the data of a job that the user has been working on back to the server
	 * 
	 * @param job
	 *            The Job entity representing the job data to be sent back to the server
	 * 
	 * @param user
	 *            The name of the user that sends the job
	 * 
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error message in case the
	 *            connection to the server fails
	 * 
	 * @return A Logical value telling the user if the request was accepted or not by the server
	 */
	public static boolean sendJobPushRequest(Job job, String user, boolean canceled) {

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job push request", "SEND");

		job.enhanceAddedFonts();

		// Text messages data
		String[] messageHeaders = { "push", "staff", "jobid", "jobtype", "comments", "nextstaff", "torrent" };

		String[] messages = { "available", user, Integer.toString(job.getID()), Integer.toString(job.getType()),
				job.getComments(), job.getNextStaffMember(), job.getTorrent() };

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

			// create fonts collection
			for (int i = 1; i < files.length; i++) {

				fileHeaders[i] = "font" + i;
				files[i] = job.getAddedFonts()[i - 1].getAbsolutePath();

			}

		}
		else {

			fileHeaders = new String[] { "sub" };
			files = new String[] { job.getSubFile().getAbsolutePath() };

		}

		// send the request to the server and wait for a response
		response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, messageHeaders, messages, fileHeaders,
				files);

		// return the servers response
		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to mark a job as finished and make it disappear from
	 * the global list (on the server)
	 * 
	 * @param jobID
	 *            The ID of the job to be ended
	 * 
	 * @param user
	 *            The name of the user that wants to end the job
	 * 
	 * @param showMessageOnError
	 *            A logical value telling the method whether to display an error message in case the
	 *            connection to the server fails
	 * 
	 * @return A Logical value telling the user if the request was accepted or not by the server
	 */
	public static boolean sendJobEndRequest(int jobID, String user) {
		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Job end request", "SEND");

		// message data
		String[] messageHeaders = { "push", "staff", "jobid" };
		String[] messages = { "end", user, Integer.toString(jobID) };

		// send the request to the server and wait for a response
		String response = ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, messageHeaders, messages);

		// return the response from the server
		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to receive a list of all the fonts existent on the
	 * server
	 * 
	 * @return A String value
	 */
	public static String sendFontsRequest() {
		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Fonts request", "SEND");

		String[] messageHeaders = new String[] { "listfonts" };
		String[] messages = new String[] { "1" };

		return ConnectionManager.sendMessage(ConnectionManager.URL_JOBS, messageHeaders, messages);
	}

	/**
	 * Send a request to the server in order to receive a list of all the registered staff and their
	 * attributes
	 * 
	 * @return A String value
	 */
	public static String sendStaffRequest() {
		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Staff request", "SEND");

		String[] messageHeaders = new String[] { "liststaff" };
		String[] messages = new String[] { "1" };

		return ConnectionManager.sendMessage(ConnectionManager.URL_CHAT, messageHeaders, messages);
	}

	/**
	 * Send a request to the chat module of the server (irc history, private history, online staff
	 * list).
	 * 
	 * @param type
	 *            A String value indicating the type of message that is sent
	 * @return A String value
	 */
	public static String sendChatDetailsRequest(String type) {
		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Send chat request", type);

		String[] messageHeaders = new String[] { "chat" };
		String[] messages = new String[] { type };

		return ConnectionManager.sendMessage(ConnectionManager.URL_CHAT, messageHeaders, messages);
	}

	/**
	 * Send a chat message
	 * 
	 * @param destination
	 *            The ID (integer) of the person that receives the message. 0 means it is and IRC
	 *            (mass) message
	 * @param message
	 *            The message to be sent
	 * @return A Logical value representing the status (sent or not) of the message
	 */
	public static boolean sendChatMessageRequest(int destination, String message) {
		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Send chat message request", "SEND");

		String[] messageHeaders = new String[] { "chat", "dest", "msg" };
		String[] messages = new String[] { "msg", Integer.toString(destination), message };

		String response = ConnectionManager.sendMessage(ConnectionManager.URL_CHAT, messageHeaders, messages);

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a message to a server using a MultiPartEntity as the parameter collection to be sent
	 * along with the message. This type of entity can contains both text data and file data (files)
	 * 
	 * @param url
	 *            The link which contains the location for the server which will receive the request
	 * 
	 * @param data
	 *            An entity containg the parameters to be sent with the message.
	 *            It can contain both plain text and files of all kinds
	 * 
	 * @return A String value containing the response that has been received from the server.<br>
	 *         This method returns "false" if a connection error has been encountered
	 * 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static String sendMessage(String url, MultipartEntity data) throws IllegalStateException, IOException {
		// create a http client used as an interface for interacting with the server
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");

		// the POST type message to be sent to the server
		HttpPost httpPost = new HttpPost(url);

		// set the MultiPartEntity (containing text commands and files <if any>) for this
		// message
		httpPost.setEntity(data);

		// The response from the server will be stored in this HttpResponse variable
		HttpResponse response;

		// send the message to the server
		response = httpClient.execute(httpPost);

		// convert the entire result in a single string variable
		String result = ConnectionManager.readResult(response.getEntity().getContent());

		// disconnect the client from the server
		httpClient.getConnectionManager().shutdown();

		// dispose unnecessary objects
		httpClient = null;
		response = null;
		httpPost = null;
		data = null;

		ActivityLogger.logActivity(ConnectionManager.class.getName(), "Server response", result);

		// return the server's result
		return result;

	}

	/**
	 * Send a text message to a server with the entered parameters
	 * 
	 * @param url
	 *            The link which contains the location for the server which will receive the request
	 * 
	 * @param messageHeaders
	 *            A String collection containing the parameters for each message
	 * 
	 * @param messages
	 *            A String collection which contains the values for the parameters specified in the
	 *            Headers String collection
	 * 
	 * @return A String value containing the response that has been received from the server.<br>
	 *         This method returns "false" if a connection error has been encountered
	 */
	private static String sendMessage(String url, String[] messageHeaders, String[] messages) {
		// append special information to the text bodies
		messageHeaders = ConnectionManager.appendSessionHeaders(messageHeaders);
		messages = ConnectionManager.appendSessionMessages(messages);

		// create a MultiPart entity which will contain the text message, representing the
		// request that will be made to the server
		MultipartEntity data = new MultipartEntity();

		try {
			for (int i = 0; i < messages.length; i++) {
				data.addPart(messageHeaders[i], new StringBody(encodeMessage(messages[i])));
			}

			String response = ConnectionManager.sendMessage(url, data);

			return (response.indexOf("<!DOCTYPE") == 0) ? null : response;
		}

		catch (Exception ex) {
			ActivityLogger.logException(ConnectionManager.class.getName(), "Server request", ex);

			return null;
		}

	}

	/**
	 * Send a message to a server with the entered parameters (containing both text and files)
	 * 
	 * @param url
	 *            The link which contains the location for the server which will receive the request
	 * 
	 * @param messageHeaders
	 *            A String collection containing the parameters for each message
	 * 
	 * @param messages
	 *            A String collection which contains the values for the parameters specified in the
	 *            Headers String collection
	 * 
	 * @param fileHeaders
	 *            A String collection containing the parameters for each file
	 * 
	 * @param files
	 *            A String collection containing the file locations on the disk, corresponding to
	 *            each file Header
	 * 
	 * @return A String value containing the response that has been received from the server.<br>
	 *         This method returns "false" if a connection error has been encountered.
	 */
	private static String sendMessage(String url, String[] messageHeaders, String[] messages, String[] fileHeaders,
			String[] files) {

		// append special information to the text bodies
		messageHeaders = ConnectionManager.appendSessionHeaders(messageHeaders);
		messages = ConnectionManager.appendSessionMessages(messages);

		// create a MultiPart entity which will contain the text message and
		// files, representing the request that will be made to the server
		MultipartEntity data = new MultipartEntity();

		try {
			for (int i = 0; i < messages.length; i++) {
				data.addPart(messageHeaders[i], new StringBody(encodeMessage(messages[i])));

			}

			for (int i = 0; i < files.length; i++) {
				data.addPart(fileHeaders[i], new FileBody(new File(files[i])));

			}

			return ConnectionManager.sendMessage(url, data);

		}
		catch (Exception ex) {
			ActivityLogger.logException(ConnectionManager.class.getName(), "Server request", ex);

			return null;
		}
	}

	private static String[] appendSessionHeaders(String[] messageHeaders) {
		if (LoggedUser.getInstance().getName() == null)
			return messageHeaders;

		String[] data = new String[messageHeaders.length + 2];

		data[data.length - 2] = "userid";
		data[data.length - 1] = "code";

		for (int i = 0; i < messageHeaders.length; i++)
			data[i] = messageHeaders[i];

		return data;
	}

	private static String[] appendSessionMessages(String[] messages) {
		LoggedUser user = LoggedUser.getInstance();

		if (user.getName() == null)
			return messages;

		String[] data = new String[messages.length + 2];

		data[data.length - 2] = Integer.toString(user.getId());
		data[data.length - 1] = user.getCode();

		for (int i = 0; i < messages.length; i++)
			data[i] = messages[i];

		return data;
	}

	private static String encodeMessage(String message) throws Exception {
		return URLEncoder.encode(message, "UTF-8");
	}

	/**
	 * Read the result details from a specific request made to the server
	 * 
	 * @param stream
	 *            An ImputStream variable containing the result received after a request was made to
	 *            a certain server
	 * 
	 * @return A String value containing the result received from this request
	 * 
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
