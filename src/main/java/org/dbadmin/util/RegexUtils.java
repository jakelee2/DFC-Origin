package org.dbadmin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by henrynguyen on 9/6/16.
 */
public class RegexUtils {

    private static final String[] PHONE_FORMATS = {"\\d{3}-\\d{7}", "\\d{3}-\\d{3}-\\d{4}"};
    private static SimpleDateFormat[] possibleFormats = new SimpleDateFormat[] {
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("yyyy,MM,dd"),
        new SimpleDateFormat("yyyy,MM,dd,HH,mm") };

    public static boolean isPhoneNumber(String sPhoneNumber) {
    	Matcher matcher = null;
        for (String r : PHONE_FORMATS) {
            Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
            try {
            	matcher = pattern.matcher(sPhoneNumber);
            } catch (Exception ex) {
            	//NOP
            }
            if (matcher != null && matcher.matches())
                return true;
        }
        return false;
    } 

    public static boolean validateAddress( String address )
    {
    	if (address!=null) return address.matches(
            "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
    	else
    		return false;
    }

    public static Date parseDate(String date) {
        for (SimpleDateFormat format: possibleFormats)
        {
            format.setLenient(false);
        }

        Date retVal = null;
        int index = 0;
        while (retVal == null && index < possibleFormats.length) {
            try {
                retVal = possibleFormats[index++].parse(date);
            } catch (ParseException ex) { /* Do nothing */ }
        }
        return retVal;
    }

    public static boolean isParsableDate(String date) {
        return parseDate(date) != null;
    }
}
