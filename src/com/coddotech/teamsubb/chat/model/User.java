package com.coddotech.teamsubb.chat.model;

import com.coddotech.teamsubb.jobs.model.JobManager;

/**
 * Class representing a user for this app.
 * 
 * @author coddo
 * 
 */
public abstract class User {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator", "Verificator", "Encoder", "Typesetter",
			"Manga", "Stiri", "Postator" };

	public static final String[] DEFAULT_USER_RANKS = { "Membru", "Moderator", "Administrator", "Fondator" };

	private int id = -1;

	private String code = null;
	private String name = null;
	private String email = null;
	private String rank = null;

	private boolean[] jobs = null;

	private int jobsArrayStartIndex = 5;

	public void setJobsArrayStartIndex(int index) {
		this.jobsArrayStartIndex = index;
	}

	public int getId() {
		return id;

	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;

	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;

	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRank() {
		return rank;

	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public boolean[] getJobs() {
		return jobs;

	}

	/**
	 * Get the names of the Jobs that can be done by this user
	 * 
	 * @return A String collection
	 */
	public String[] getJobNames() {
		int available = 0;

		for (int i = 0; i < this.jobs.length; i++) {

			if (this.jobs[i])
				available++;

		}

		int counter = 0;

		String[] userJobs = new String[available];

		for (int i = 0; i < this.jobs.length; i++) {

			if (this.jobs[i]) {
				userJobs[counter] = User.DEFAULT_JOBS_INFO_HEADERS[i];
				counter++;

			}

		}

		return userJobs;

	}

	public boolean isFondator() {
		return this.getRank().equals(User.DEFAULT_USER_RANKS[3]);
	}

	public boolean isAdmin() {
		return this.getRank().equals(User.DEFAULT_USER_RANKS[2]);
	}

	public boolean isModerator() {
		return this.getRank().equals(User.DEFAULT_USER_RANKS[1]);
	}

	/**
	 * Parse and set the user details from a raw String.
	 * 
	 * @param rawData
	 *            A String value
	 */
	public void setUserDetails(String rawData) {
		String[] data = rawData.split(JobManager.SEPARATOR_DATA);

		this.createUserInfo(data);
		this.createJobsInfo(data);
	}

	/**
	 * Set the user jobs, which start from the 5th item in the array<br>
	 * <br>
	 * 
	 * @param data
	 *            A String collection containing the server's response split into chunks
	 */
	protected void createJobsInfo(String[] data) {
		boolean[] jobsData = { false, false, false, false, false, false, false };

		for (int i = jobsArrayStartIndex; i < data.length; i++) {

			for (int j = 0; j < User.DEFAULT_JOBS_INFO_HEADERS.length; j++) {

				if (data[i].equals(User.DEFAULT_JOBS_INFO_HEADERS[j])) {
					jobsData[j] = true;

					break;
				}
			}
		}

		this.jobs = jobsData;
	}

	/**
	 * Set the user information as follows:
	 * user name, email and rank <br>
	 * <br>
	 * 
	 * data[0] -> id <br>
	 * data[1] -> code <br>
	 * data[2] -> name <br>
	 * data[3] -> email <br>
	 * data[4] -> rank (integer)<br>
	 * data[5 -> n] -> job titles
	 * 
	 * @param data
	 *            A String collection containing the server's response split into chunks
	 */
	protected void createUserInfo(String[] data) {
		this.id = Integer.parseInt(data[0]);

		this.code = data[1];
		this.name = data[2];
		this.email = data[3];

		/*
		 * The rank is stored as an integer, indicating on of the following
		 * ranks: 0 - Membru; 1 - Moderator; 2 - Administrator; 3 - Fondator;
		 */
		this.rank = User.DEFAULT_USER_RANKS[Integer.parseInt(data[4])];
	}

}
