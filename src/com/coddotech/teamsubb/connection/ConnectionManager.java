package com.coddotech.teamsubb.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class ConnectionManager {

	private static final String URL_LOGIN = "http://anime4fun.ro/wlogin.php";
	private static final String URL_LOGOUT = "http://anime4fun.ro/wlogout.php";

	private String loginResult; // contains user data

	/**
	 * Retrieve the results of the login process
	 * 
	 * @return A String containing the data received from the remote server
	 */
	public String getLoginResult() {
		return loginResult;
	}

	/**
	 * Constructor for this class
	 */
	public ConnectionManager() {
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		loginResult = null;
	}

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
	public boolean sendLoginRequest(String user, String pass) {
		try {
			// create request variables
			URL server = new URL(ConnectionManager.URL_LOGIN);
			HttpURLConnection con = (HttpURLConnection) server.openConnection();
			con.setRequestMethod("POST");

			// send the request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes("user=" + user + "&pass=" + pass);
			wr.flush();
			wr.close();

			// get result
			loginResult = this.readResult(con);

			// dispose objects

			server = null;
			con.disconnect();
			con = null;

			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Sends a logout request to the server using the specified details
	 * 
	 * @param user
	 *            The username of the staff member to be logged out from the
	 *            server
	 */
	public static void sendLogoutRequest(String user) {
		try {
			URL server = new URL(ConnectionManager.URL_LOGOUT);
			HttpURLConnection con = (HttpURLConnection) server.openConnection();
			con.setRequestMethod("POST");

			// send the request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes("logout=" + user);
			wr.flush();
			wr.close();

			server = null;
			con.disconnect();
			con = null;
		} catch (Exception ex) {
			// ignore exceptions
		}
	}

	/**
	 * Read the result details from a specific request made to the server
	 * 
	 * @param con
	 *            The connection variable used to send the request
	 * @return A String value containing the result received from this request
	 * @throws IOException
	 */
	private String readResult(HttpURLConnection con) throws IOException {
		String result = "";

		// read the result of the request
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));

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
