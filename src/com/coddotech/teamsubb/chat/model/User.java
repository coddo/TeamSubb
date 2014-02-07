package com.coddotech.teamsubb.chat.model;

import com.coddotech.teamsubb.jobs.model.JobManager;

public abstract class User {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator", "Verificator", "Encoder", "Typesetter",
			"Manga", "Stiri", "Postator" };

	protected static final String[] DEFAULT_USER_RANKS = { "Membru", "Moderator", "Administrator", "Fondator" };

	protected int id = -1;

	protected String code = null;
	protected String name = null;
	protected String email = null;
	protected String rank = null;

	protected boolean[] jobs = null;

	protected int jobsArrayStartIndex = 5;

	public int getId() {
		return id;

	}

	public String getCode() {
		return code;

	}

	public String getName() {
		return name;

	}

	public String getEmail() {
		return email;

	}

	public String getRank() {
		return rank;

	}

	public boolean[] getJobs() {
		return jobs;

	}

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
