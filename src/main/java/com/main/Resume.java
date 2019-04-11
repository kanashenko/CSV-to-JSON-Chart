package com.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resume {
	private Date from;
	private Date to;
	private List<Receiver> receivers = new ArrayList<>();
	private List<Transmitter> transmitters = new ArrayList<>();
	private Map<String, Integer> protocols = new HashMap<>();
	private Map<String, Integer> apps = new HashMap<>();
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public List<Receiver> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}
	public List<Transmitter> getTransmitters() {
		return transmitters;
	}
	public void setTransmitters(List<Transmitter> transmitters) {
		this.transmitters = transmitters;
	}
	public Map<String, Integer> getProtocols() {
		return protocols;
	}
	public void setProtocols(Map<String, Integer> protocols) {
		this.protocols = protocols;
	}
	public Map<String, Integer> getApps() {
		return apps;
	}
	public void setApps(Map<String, Integer> apps) {
		this.apps = apps;
	}
}
