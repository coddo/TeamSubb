package com.coddotech.teamsubb.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

public final class ConnectionManager {

	private static final String URL_USER_LOGGING = "http://anime4fun.ro/wlogin.php";

	// private static final String URL_JOBS = "http://anime4fun.ro/jobs.php";
	// private static final String URL_CHAT = "http://anime4fun.ro/chat.php";

	/**
	 * Sends a login request to the server using the entered user details
	 * 
	 * @param user
	 *            The username as it is registered on the server
	 * @param pass
	 *            The password for this user
	 * @return A logical value indicating if the connection was successful or
	 *         not
	 */
	public static String sendLoginRequest(String user, String pass) {
		return ConnectionManager.sendMessage(
				ConnectionManager.URL_USER_LOGGING, new String[] { "user",
						"pass" }, new String[] { user, pass });
	}

	/**
	 * Sends a logout request to the server using the specified details
	 * 
	 * @param user
	 *            The username of the staff member to be logged out from the
	 *            server
	 */
	public static String sendLogoutRequest(String user) {
		return ConnectionManager.sendMessage(
				ConnectionManager.URL_USER_LOGGING, new String[] { "logout" },
				new String[] { user });
	}

	/**
	 * THIS IS JUST A METHOD USED FOR VERIFICATIONS. IT IS STILL USEFUL FOR
	 * FURTHER DEBUGGING
	 * 
	 * @param files
	 * @throws Exception
	 */
	public static void sendFilesPOST(String[] files) throws Exception {
		ConnectionManager.sendMessage("http://anime4fun.ro/uploadimage.php",
				new String[] { "submit" }, new String[] { "testare_finala" },
				new String[] { "uploaded_image" },
				new String[] { "C:\\Users\\Claudiu\\Desktop\\test2.jpg" });
	}

	/**
	 * Send a message to a server using a MultiPartEntity as the parameter
	 * collection to be sent along with the message
	 * 
	 * @param url
	 *            The link which contains the location for the server which will
	 *            receive the request
	 * @param mpEntity
	 *            An entity containg the parameters to be sent with the message.
	 *            It can contain both plain text and files of all kinds
	 * @return A String value containing the response that has been received
	 *         from the server<br>
	 *         This method returns "error" if a connection error has been
	 *         encountered
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static String sendMessage(String url, MultipartEntity mpEntity)
			throws IllegalStateException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(url);

		httppost.setEntity(mpEntity);

		HttpResponse response = httpclient.execute(httppost);

		String result = ConnectionManager.readResult(response.getEntity()
				.getContent());

		httpclient.getConnectionManager().shutdown();

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
	 * @param messageBodies
	 *            A String collection which contains the values for the
	 *            parameters specified in the Headers String collection
	 * @return A String value containing the response that has been received
	 *         from the server<br>
	 *         This method returns "error" if a connection error has been
	 *         encountered
	 */
	private static String sendMessage(String url, String[] messageHeaders,
			String[] messageBodies) {
		try {

			MultipartEntity mpEntity = new MultipartEntity();

			for (int i = 0; i < messageBodies.length; i++) {
				mpEntity.addPart(messageHeaders[i], new StringBody(
						messageBodies[i]));
			}

			return ConnectionManager.sendMessage(url, mpEntity);

		} catch (Exception ex) {
			return "error";
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
	 * @param messageBodies
	 *            A String collection which contains the values for the
	 *            parameters specified in the Headers String collection
	 * @param fileHeaders
	 *            A String collection containing the parameters for each file
	 * @param files
	 *            A String collection containing the file locations on the disk,
	 *            corresponding to each file Header
	 * @return A String value containing the response that has been received
	 *         from the server<br>
	 *         This method returns "error" if a connection error has been
	 *         encountered
	 */
	private static String sendMessage(String url, String[] messageHeaders,
			String[] messageBodies, String[] fileHeaders, String[] files) {
		try {
			MultipartEntity mpEntity = new MultipartEntity();

			for (int i = 0; i < messageBodies.length; i++) {
				mpEntity.addPart(messageHeaders[i], new StringBody(
						messageBodies[i]));
			}

			for (int i = 0; i < files.length; i++) {
				mpEntity.addPart(fileHeaders[i], new FileBody(
						new File(files[i])));
			}

			return ConnectionManager.sendMessage(url, mpEntity);

		} catch (Exception ex) {
			return "error";
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
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));

		// recreate the result of the string
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			result += inputLine;
		}

		// dispose of unnecessary objects
		in.close();
		in = null;
		inputLine = null;

		// return the result
		return result;
	}

}
