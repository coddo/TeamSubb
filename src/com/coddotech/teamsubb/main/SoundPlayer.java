package com.coddotech.teamsubb.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SoundPlayer {

	private static final String AUDIO_RESOURCE = System.getProperty("user.dir") + File.separator + "resources"
			+ File.separator + "sound";

	private static final String JOB = "job.wav";
	private static final String CHAT = "chat.wav";

	private static void playSound(String soundFile) {
		try {
			// open the sound file as a Java input stream
			InputStream in = new FileInputStream(soundFile);

			// create an audiostream from the inputstream
			AudioStream audioStream = new AudioStream(in);

			// play the audio clip with the audioplayer class
			AudioPlayer.player.start(audioStream);
		}

		catch (Exception ex) {

		}
	}

	public static void playJobSound() {
		// playSound(AUDIO_RESOURCE + File.separator + JOB);
	}

	public static void playChatSound() {
		// playSound(AUDIO_RESOURCE + File.separator + CHAT);
	}
}
