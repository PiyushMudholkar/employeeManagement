package com.nphc.employeeManagement.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import com.nphc.employeeManagement.exception.NPHCException;

public class DateUtil {

	public static DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ofPattern("" + "[" +NPHCConstants.DATE_FORMAT_1 +"]" + "[" +NPHCConstants.DATE_FORMAT_2 +"]"));
	
	
	public static DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();

	public static LocalDate convertStringToDate(String dateString) throws NPHCException {
		LocalDate dateTime=null;
		try {
		dateTime = LocalDate.parse(dateString, dateTimeFormatter);
		
		}catch (DateTimeParseException e) {
			throw new NPHCException(String.format("Invalid date format for %s , supportd formats are %s and %s ", dateString, NPHCConstants.DATE_FORMAT_1, NPHCConstants.DATE_FORMAT_2) );
		}
		return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());

	}
	
	public static String convertDateToString(LocalDate date) throws NPHCException {
		String dateSting=null;
		try {
			//if we need to allow both the format for filter we can use dateTimeFormatter from line no 16
			dateSting = date.format(DateTimeFormatter.ofPattern(NPHCConstants.DATE_FORMAT_1));
		}catch (DateTimeParseException e) {
			throw new NPHCException(String.format("failed to format date to format yyyy-MM-dd ", date) );
		}
		return dateSting;

	}
}
