package shtykh.nekki;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shtykh on 28/02/15.
 */
public class Util {
	
	private Util(){};
	
	public static Properties loadProperties(Class className, String propFileName) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = className.getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			properties.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		return properties;
	}
}
