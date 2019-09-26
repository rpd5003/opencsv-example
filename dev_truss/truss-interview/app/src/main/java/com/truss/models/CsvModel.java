package com.truss.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.CsvBindByName;

public class CsvModel {
	
	@CsvBindByName(column = "Timestamp")
	private String Timestamp;
	
	@CsvBindByName(column = "Address")
	private String Address;
	
	@CsvBindByName(column = "ZIP")
	private String ZIP;
	
	@CsvBindByName(column = "FullName")
	private String FullName;
	
	@CsvBindByName(column = "FooDuration")
	private String FooDuration;
	
	@CsvBindByName(column = "BarDuration")
	private String BarDuration;
	
	@CsvBindByName(column = "TotalDuration")
	private String TotalDuration;
	
	@CsvBindByName(column = "Notes")
	private String Notes;
	

	public String getTimestamp() {
		return Timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		
		//convert from pacific to eastern time
        ZoneId fromTimeZone = ZoneId.of("America/Los_Angeles"); 
        ZoneId toTimeZone = ZoneId.of("America/New_York");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
         
        LocalDateTime now = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("M/d/yy h:mm:ss a"));
         
        Timestamp= now.atZone(fromTimeZone).withZoneSameInstant(toTimeZone).format(formatter);
	}
	
	public String getZIP() {
		return ZIP;
	}
	
	public void setZIP(String zip) {
		this.ZIP = StringUtils.leftPad(zip, 5, '0');
	}
	
	public String getFullName() {
		return FullName;
	}
	
	public void setFullName(String fullName) {
		FullName = fullName.toUpperCase(Locale.ENGLISH);
	}
	
	public String getAddress() {
		return Address;
	}
	
	public void setAddress(String address) {
		Address = address;
	}
	
	public String getFooDuration() {
		return FooDuration;
	}
	
	public void setFooDuration(String fooDuration) {
		FooDuration = convertTimestampToEpochString(fooDuration);
	}
	
	public String getBarDuration() {
		return BarDuration;
	}
	
	public void setBarDuration(String barDuration) {
		BarDuration = convertTimestampToEpochString(barDuration);
	}
	
	public String getTotalDuration() {
		return TotalDuration;
	}
	
	public void setTotalDuration(String totalDuration) {
		TotalDuration = totalDuration;
	}
	
	public String getNotes() {
		return Notes;
	}
	
	public void setNotes(String notes) {

		Notes = notes.replaceAll("\uFFFD", "\"");;
	}
	
	public String convertTimestampToEpochString(String timestamp){
		long epoch = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SS");
		    Date dt;
			dt = sdf.parse(timestamp);
		    epoch = dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return String.valueOf(epoch);
	}
	
}
