package com.main;

public class Transmitter {
	private String name;
	private long bytesOut;
	private long packetsOut;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getBytesOut() {
		return bytesOut;
	}
	public void setBytesOut(long bytesOut) {
		this.bytesOut = bytesOut;
	}
	public long getPacketsOut() {
		return packetsOut;
	}
	public void setPacketsOut(long packetsOut) {
		this.packetsOut = packetsOut;
	}
}
