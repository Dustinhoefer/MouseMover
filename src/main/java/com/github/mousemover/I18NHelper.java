package com.github.mousemover;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NHelper {
	private static ResourceBundle messages;
	
	private I18NHelper() {
	}
	
	public static void init() {
		Locale currentLocale = Locale.getDefault();
		messages = ResourceBundle.getBundle("MessageBundle", currentLocale);
	}

	public static String getMessage(String key) {
		return messages.getString(key);
	}

}
