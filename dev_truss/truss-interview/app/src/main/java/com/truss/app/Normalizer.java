package com.truss.app;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import com.truss.models.CsvModel;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class Normalizer {
	private static Logger logger = Logger.getLogger(Normalizer.class);

	public static <T> void main(String[] args) throws IOException {
		//throw error and exit if 2 arguments not present
		if (args.length != 2) {
			System.out.println("Usage: ./normalizer full_path_to_input.csv full_path_to_output.csv");
			System.exit(0);
		}
		normalizeCSV(args[0], args[1]);
	}

	public static void normalizeCSV(String inputFilename, String outputFilename) {
		try {
			
			//Read input file and marshal into POJO
			Reader reader = Files.newBufferedReader(Paths.get(inputFilename));
			CsvToBean<CsvModel> csvToBean = new CsvToBeanBuilder<CsvModel>(reader).withType(CsvModel.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			//create csv writer. do not use any quotes for quoted elements, and do not use escape characters
			Writer writer = Files.newBufferedWriter(Paths.get(outputFilename));
            CSVWriter csvWriter = new CSVWriter(writer, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            
            //writer header columns to output csv
			String[] headerColumns = new String[] { "Timestamp", "Address", "ZIP", "FullName", "FooDuration", "BarDuration", "TotalDuration", "Notes" };
			csvWriter.writeNext(headerColumns, false);

			//iterate over each row in source csv in List of POJOs, and apply any transformations
			Iterator<CsvModel> csvModelIterator = csvToBean.iterator();
			while (csvModelIterator.hasNext()) {
				CsvModel c = csvModelIterator.next();
				
				//if address field has commas, preserve quotes
				if(c.getAddress().indexOf(',') != -1){
					c.setAddress("\"" + c.getAddress() + "\"");
				}
				
				//calculate total duration from foo duration and bar duration
				c.setTotalDuration(String.valueOf(Integer.parseInt(c.getFooDuration()) + Integer.parseInt(c.getBarDuration())));
				
				//build string from POJO fields in order of header columns
				String str = c.getTimestamp() + "," + c.getAddress() + "," + c.getZIP() + "," + c.getFullName() + ","
						+ c.getFooDuration() + "," + c.getBarDuration() + "," + c.getTotalDuration() + ","
						+ c.getNotes();
				
				//split on commas, and ignore quotes and other characters
				String[] arr = str.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				
				//write current transformed row to output file
				csvWriter.writeNext(arr, false);
			}

			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

	}
}