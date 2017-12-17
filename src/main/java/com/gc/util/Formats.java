package com.gc.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.vo.Notification;

public final class Formats {

	private static final Logger LOGGER = LoggerFactory.getLogger(Formats.class);

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

	public static final NumberFormat AMOUNT_FORMATTER = new DecimalFormat("#0.00");

	public static final NumberFormat PHONE_NUMBER_FORMATTER = new DecimalFormat("#0");

	public static final Pattern INPUT_DATE_PATTERN_1 = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}");

	public static final DateFormat INPUT_DATE_FORMATTER_1 = new SimpleDateFormat("dd/MM/yyyy");

	public static final Pattern INPUT_DATE_PATTERN_2 = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{2}");

	public static final DateFormat INPUT_DATE_FORMATTER_2 = new SimpleDateFormat("dd/MM/yy");

	public static Date interpretDate(String dateStr) {
		String trimmedDateStr = dateStr.trim();
		Date d = matchAndBuildDate(trimmedDateStr, INPUT_DATE_PATTERN_1, INPUT_DATE_FORMATTER_1);
		if (d != null) {
			return d;
		}
		return matchAndBuildDate(trimmedDateStr, INPUT_DATE_PATTERN_2, INPUT_DATE_FORMATTER_2);
	}

	private static Date matchAndBuildDate(String dateStr, Pattern p, DateFormat dateFormatter) {
		Matcher matcher = p.matcher(dateStr);
		if (matcher.matches()) {
			String group = matcher.group();
			try {
				return dateFormatter.parse(group);
			} catch (ParseException e) {
				LOGGER.error("Error while parsing date: {}", dateStr, e);
			}
		}
		return null;
	}

	public static String interpretFlatNumber(String flatNo) {
		if (StringUtils.isNotBlank(flatNo)) {
			return flatNo.replaceAll("-", "").replaceAll(" ", "");
		}
		return null;
	}

	public static StrSubstitutor prepareReplaceTemplate(Notification n) {
		Map<String, String> valueMap = n.getTemplate();
		return new StrSubstitutor(valueMap, "$$[", "]");
	}

}
