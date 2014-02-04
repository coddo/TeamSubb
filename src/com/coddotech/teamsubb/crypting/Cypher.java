package com.coddotech.teamsubb.crypting;

public class Cypher {

	public static String encrypt(String message) {
		String result = "";

		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == '~')
				result += message.charAt(i);

			else {
				int x = (43 * (int) message.charAt(i) + 10) % 126;

				char[] c = Character.toChars(x);

				result += c[0];
			}

		}

		return result;
	}

	public static String decrypt(String message) {
		String result = "";

		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == '~')
				result += message.charAt(i);

			else {
				int x = ((85 * (int) message.charAt(i) + 32) % 126);

				if (x < 0)
					x = -x;

				char[] c = Character.toChars(x);
				result += c[0];
			}
		}

		return result;
	}
}
