package com.main;

public class Receiver {
	private String name;
	private long bytesIn;
	private long packetsIn;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getBytesIn() {
		return bytesIn;
	}
	public void setBytesIn(long bytesIn) {
		this.bytesIn = bytesIn;
	}
	public long getPacketsIn() {
		return packetsIn;
	}
	public void setPacketsIn(long packetsIn) {
		this.packetsIn = packetsIn;
	}
}
