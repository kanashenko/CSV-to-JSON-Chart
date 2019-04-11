package com.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	public static void main(String[] args) {

		final List<List<String>> records = new ArrayList<>();
		
		Thread computationThread = new Thread(() -> {
			CSVHandler handler = new CSVHandler();
			CSVReader reader = new CSVReader();
			records.addAll(reader.read("C:\\Users\\Zver\\Downloads\\2018-11-20-17-17.csv"));
			String from = "7/13/2018 5:23:22.000000000 PM";
			String to = "7/17/2018 5:23:22.000000000 PM";
			try {
				handler.timeRange(format.parse(from), format.parse(to), records);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		computationThread.start();

		try {
			computationThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		records.stream().forEach(System.out::println);
		TopChart.showChart(records);
	}
}
