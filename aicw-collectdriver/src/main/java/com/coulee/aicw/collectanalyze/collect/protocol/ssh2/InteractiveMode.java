package com.coulee.aicw.collectanalyze.collect.protocol.ssh2;

import ch.ethz.ssh2.InteractiveCallback;

public class InteractiveMode implements InteractiveCallback {
	
	private String password;
	
	public InteractiveMode(String password) {
		this.password = password;
	}

	@Override
	public String[] replyToChallenge(String name, String instruction, int numPrompts, String[] prompt, boolean[] echo) throws Exception {
		String[] result = new String[numPrompts];
		for (int i = 0; i < numPrompts; i++) {
			result[i] = this.password;
		}
		return result;
	}

}
