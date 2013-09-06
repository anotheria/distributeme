package org.distributeme.test.echo;

import java.io.Serializable;

public class Echo implements Serializable {
	private static final long serialVersionUID = 5314033233351550848L;

	public Echo() {
		time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getReply() {
		return reply;
	}

	public void setReply(long reply) {
		this.reply = reply;
	}

	private long time;
	private long reply;

	public String toString() {
		return "EchoObjekt time: " + time + ", reply: " + reply + ", diff: " + (reply - time);
	}
}
