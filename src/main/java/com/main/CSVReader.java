package com.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

	private List<List<String>> records = new ArrayList<>();

	public List<List<String>> read(String file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			br.readLine();
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				records.add(Arrays.asList(values));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}
}
