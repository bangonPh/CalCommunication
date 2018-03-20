package com.communication.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.communication.model.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainCalculation {

	private final static String SOURCES_PATH = "D:\\Java\\promotion.log";
	private final static String DESTINATION_PATH = "D:\\Java\\promotion.json";
	private final static String FMT_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
	private final static String PIPE_CHAR = "\\|";
	private final static String PROMOTION_P1 = "P1";
	
	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new FileReader(SOURCES_PATH))) {
			List<Customer> customers = new ArrayList<>();
			String line, start = null, end = null;
			while ((line = br.readLine()) != null) {
				Customer customer = new Customer();
				String[] sourceLine = line.split(PIPE_CHAR);
				for (int i = 0; i < sourceLine.length; i++) {
					// Date
					if (i == 0 && !isEmpty(sourceLine[0])) {
						start = sourceLine[0];
						end = sourceLine[0];
					}
					
					// Start Time
					if (i == 1 && !isEmpty(sourceLine[1])) {
						start += " " + sourceLine[1];
					}
					
					// End Time
					if (i == 2 && !isEmpty(sourceLine[2])) {
						end += " " + sourceLine[2];
					}
					
					// Mobile Number
					if (i == 3 && !isEmpty(sourceLine[3])) {
						customer.setMobileNumber(sourceLine[3]);
					}
					
					// Promotion
					if (i == 4 && !isEmpty(sourceLine[4])) {
						customer.setPromotion(sourceLine[4]);
					}
				}
				
				// Calculation Service Charge
				customer.setStartDate(start != null ? parseToDate(start) : null);
				customer.setEndDate(end != null ? parseToDate(end) : null);
				
				if (customer != null && PROMOTION_P1.equals(customer.getPromotion())) {
					customer.setServiceCharge(serviceChargeCal(customer.getStartDate(), customer.getEndDate()));
				}
				customers.add(customer);
			}
			
			// Export JSON File with Google GSON 
			if (customers != null && !customers.isEmpty()) {
				try (Writer writer = new FileWriter(DESTINATION_PATH)) {
				    Gson gson = new GsonBuilder().create();
				    gson.toJson(customers, writer);
				}
			}
			System.out.println("### Successful ###");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("### Failure ###");
		}
	}
	
	/*
	 * Method to check empty String
	 */
	private static boolean isEmpty(String data) {
		if (data == null || data.isEmpty()) return true;
		return false;
	}
	
	private static Date parseToDate(String date) throws ParseException {
		if (date != null && !date.isEmpty()) {
			SimpleDateFormat fmt = new SimpleDateFormat(FMT_DATE_TIME);
			return fmt.parse(date);
		}
		return null;
	}
	
	private static String serviceChargeCal(Date start, Date end) throws Exception {
		if (start == null || end == null) return null;
		double serviceCharge = 0;
		
		long diff = end.getTime() - start.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		// 3 Baht for First minutes
		// 0.05 Baht per seconds, If grater than 1 minutes
		double pricePerMinutes = 1.0 / 60.0;
		if (diffSeconds > 0 || diffMinutes > 0 || diffHours > 0 || diffDays > 0) {
			long totalSeconds = diffDays * (24 * 60 * 60); // 1 Days to 1,440 Minutes(24 Hours)
			totalSeconds += diffHours * (60 * 60); // 1 Hours to 60 Minutes.
			totalSeconds += diffMinutes * 60; // 1 Minutes to 60 Seconds
			totalSeconds += diffSeconds;
			
			if (totalSeconds > 60) {
				// minus to 60 seconds or 1 minutes
				serviceCharge = 3 + ((totalSeconds - 60) * pricePerMinutes);
			}else {
				throw new Exception("Fail to calculation !!!");
			}
		}
		return String.format("%.2f", serviceCharge);
	}
}
