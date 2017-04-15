package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public final class ReadPropertyData {
	private static File file;
	private static FileInputStream fileInput;
	private static Properties properties;

	static {
		final String propertyFile = System.getProperty("user.dir")
				+ "\\src\\main\\resources\\properties\\data.properties";
		file = new File(propertyFile);
		fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		properties = new Properties();
		try {
			properties.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ReadPropertyData() {
	}

	public static String getBaseUrl() {
		return properties.getProperty("baseUrl");
	}

	public static String getDriverPath() {
		return System.getProperty("user.dir")
				+ properties.getProperty("driver");
	}
}
