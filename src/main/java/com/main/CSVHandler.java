package com.main;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;

import static java.util.stream.Collectors.*;

public class CSVHandler {
	private final static String DATE = "0";
	private final static String BYTES_IN = "1";
	private final static String BYTES_OUT = "2";
	private final static String PACKETS_IN = "3";
	private final static String PACKETS_OUT = "4";
	private final static String APPLICATION = "5";
	private final static String DESTINATION = "6";
	private final static String PROTOCOL = "7";
	private final static String SOURCE = "8";
	private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private Map<String, Long> top10Receivers = new LinkedHashMap<>();
	private Map<String, Long> top10Transmiters = new LinkedHashMap<>();
	private Map<String, List<String[]>> dataReceivers = new HashMap<>();
	private Map<String, List<String[]>> dataTransmitters = new HashMap<>();
	
	public void computeTop(List<List<String>> records) {
		Map<String, Long> receivers = new HashMap<>();
		Map<String, Long> transmiters = new HashMap<>();
		Map<String, Integer> protocols = new HashMap<>();
		Map<String, Integer> apps = new HashMap<>();
		for (List<String> record: records) {
			receivers.merge(record.get(Integer.parseInt(DESTINATION)),
					Long.parseLong(record.get(Integer.parseInt(BYTES_IN))), Long::sum);

			transmiters.merge(record.get(Integer.parseInt(SOURCE)),
					Long.parseLong(record.get(Integer.parseInt(BYTES_OUT))), Long::sum);

			merge(protocols, record, PROTOCOL);
			merge(apps, record, APPLICATION);
		}

		saveTopN(receivers, 10, top10Receivers);
		saveTopN(transmiters, 10, top10Transmiters);
		protocols.remove("");
		List<String> top3Protocols = getTopKeys(protocols, 3);
		apps.remove("");
		List<String> top10Apps = getTopKeys(apps, 10);

		try (FileWriter file = new FileWriter("top.txt")) {
			Gson gson = new Gson();
			file.write(gson.toJson(top10Receivers) + '\n');
			file.write(gson.toJson(top10Transmiters) + '\n');
			file.write(gson.toJson(top3Protocols) + '\n');
			file.write(gson.toJson(top10Apps));
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}

	public void timeRange(Date from, Date to, List<List<String>> records) {
		Map<String, Long> receiversBytes = new HashMap<>();
		Map<String, Long> receiversPackets = new HashMap<>();
		Map<String, Long> transmitersBytes = new HashMap<>();
		Map<String, Long> transmitersPackets = new HashMap<>();
		Map<String, Integer> protocols = new HashMap<>();
		Map<String, Integer> apps = new HashMap<>();
		records.stream().filter(record -> {
			Date date = null;
			try {
				date = format.parse(record.get(Integer.parseInt(DATE)));
			} catch (NumberFormatException | ParseException e) {
				e.printStackTrace();
			}
			return date.after(from) && date.before(to);
		}).forEach(record ->{
			receiversBytes.merge(record.get(Integer.parseInt(DESTINATION)),
					Long.parseLong(record.get(Integer.parseInt(BYTES_IN))), Long::sum);
			receiversPackets.merge(record.get(Integer.parseInt(DESTINATION)),
					Long.parseLong(record.get(Integer.parseInt(PACKETS_IN))), Long::sum);
			transmitersBytes.merge(record.get(Integer.parseInt(SOURCE)),
					Long.parseLong(record.get(Integer.parseInt(BYTES_OUT))), Long::sum);
			transmitersPackets.merge(record.get(Integer.parseInt(SOURCE)),
					Long.parseLong(record.get(Integer.parseInt(PACKETS_OUT))), Long::sum);
			merge(protocols, record, PROTOCOL);
			merge(apps, record, APPLICATION);
		});
		
		List<Receiver> receivers = new ArrayList<>();
		sortMapLong(receiversBytes).entrySet().stream().forEach(entry ->{
			Receiver receiver = new Receiver();
			receiver.setName(entry.getKey());
			receiver.setBytesIn(entry.getValue());
			receivers.add(receiver);
		});
		
		sortMapLong(receiversPackets).entrySet().stream().forEach(entry ->{
			for(Receiver receiver: receivers) {
				if(receiver.getName().equals(entry.getKey())) {
					receiver.setPacketsIn(entry.getValue());
					break;
				}
			}
		});
		
		List<Transmitter> transmitters = new ArrayList<>();
		sortMapLong(transmitersBytes).entrySet().stream().forEach(entry ->{
			Transmitter transmitter = new Transmitter();
			transmitter.setName(entry.getKey());
			transmitter.setBytesOut(entry.getValue());
			transmitters.add(transmitter);
		});
		
		sortMapLong(transmitersPackets).entrySet().stream().forEach(entry ->{
			for(Transmitter transmitter: transmitters) {
				if(transmitter.getName().equals(entry.getKey())) {
					transmitter.setPacketsOut(entry.getValue());
					break;
				}
			}
		});
		
		Resume resume = new Resume();
		resume.setFrom(from);
		resume.setTo(to);
		resume.setReceivers(receivers);
		resume.setTransmitters(transmitters);
		resume.setProtocols(sortMapInteger(protocols));
		resume.setApps(sortMapInteger(apps));
		saveResume(resume);
	}
	
	private void saveResume(Resume resume) {
		try (FileWriter file = new FileWriter("resume.txt")) {
			Gson gson = new Gson();
			file.write(gson.toJson(resume));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void computeDataforTopChart(List<List<String>> records) {
		top10Receivers.keySet().stream().forEach(key -> dataReceivers.put(key, new ArrayList<>()));		
		top10Transmiters.keySet().stream().forEach(key -> dataTransmitters.put(key, new ArrayList<>()));		
		
		for (List<String> record: records) {
			String key = record.get(Integer.parseInt(DESTINATION));
			
			if(top10Receivers.containsKey(key)) {
				dataReceivers.get(key).add(
						new String[]{
								record.get(Integer.parseInt(DATE)),
								record.get(Integer.parseInt(BYTES_IN))
						});
			}
			key = record.get(Integer.parseInt(SOURCE));
			if(top10Transmiters.containsKey(key)) {
				dataTransmitters.get(key).add(
						new String[]{
								record.get(Integer.parseInt(DATE)),
								record.get(Integer.parseInt(BYTES_OUT))
						});
			}
		}
	}
	
	private void merge(Map<String, Integer> map, List<String> record, String subject) {
		map.merge(record.get(Integer.parseInt(subject)), 1, Integer::sum);
	}	
	private Map<String, Long> sortMapLong(Map<String, Long> map) {
		LinkedHashMap<String, Long> sortedMap = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		.collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
		return sortedMap;
	}
	private Map<String, Integer> sortMapInteger(Map<String, Integer> map) {
		LinkedHashMap<String, Integer> sortedMap = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		.collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
		return sortedMap;
	}
	private void saveTopN(Map<String, Long> mapAll, int n, Map<String, Long> selected) {
		mapAll.entrySet().stream().filter(e -> e.getValue() == 0).map(e -> mapAll.remove(e.getKey()));
		sortMapLong(mapAll).entrySet().stream()
				.filter(e -> e.getValue() != 0).limit(n).forEach(e -> {
					selected.put(e.getKey(), e.getValue());
				});
	}
	private <K, V extends Comparable<?>> List<K> getTopKeys(Map<K, V> map, int n) {
		Function<K, V> getVal = Functions.forMap(map);
		Ordering<K> ordering = Ordering.natural().onResultOf(getVal);
		return ordering.greatestOf(map.keySet(), n);
	}

	public Map<String, List<String[]>> getDataReceivers() {
		return dataReceivers;
	}

	public Map<String, List<String[]>> getDataTransmitters() {
		return dataTransmitters;
	}
}
