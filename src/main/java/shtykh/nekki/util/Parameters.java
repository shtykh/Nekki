package shtykh.nekki.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by shtykh on 01/03/15.
 */
public class Parameters {
	private Parameters() {}
	private static Logger log = Logger.getLogger(Parameters.class);

	public static String INPUT = "files/input/";
	public static String DONE = "files/done/";
	public static String BAD = "files/bad/";
	public static int THREAD_POOL_SIZE = 20;
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static int MAX_CONTENT_SIZE = 1024;

	public static void init(){
		Properties properties;
		try {
			properties = loadProperties("log4j.properties");
			PropertyConfigurator.configure(properties);
		}
		catch (Exception e) {
			System.err.println(e);
		}
		try {
			properties = loadProperties("nekki.properties");
			INPUT = properties.getProperty("INPUT_PATH", INPUT);
			DONE = properties.getProperty("DONE_PATH", DONE);
			BAD = properties.getProperty("BAD_PATH", BAD);

			DATE_FORMAT = properties.getProperty("DATE_FORMAT", DATE_FORMAT);
			THREAD_POOL_SIZE = Integer.parseInt(properties.getProperty("THREAD_POOL_SIZE", String.valueOf(THREAD_POOL_SIZE)));
			MAX_CONTENT_SIZE = Integer.parseInt(properties.getProperty("MAX_CONTENT_SIZE", String.valueOf(MAX_CONTENT_SIZE)));
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		for(String fieldName : new String[]{"INPUT", "DONE", "BAD"}) {
			try {
				Field field = Parameters.class.getField(fieldName);
				makeDirectory(field);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				log.error(e.getMessage());
			}
		}
	}

	private static void makeDirectory(Field field) throws IllegalAccessException {
		String path = (String) field.get(null);
			File file = new File(path);
			boolean success = file.mkdirs();
			log.info("Folder " + file.getAbsolutePath() + 
					(success ? " is created" : " is not created") + 
					" as " + field.getName());
	}

	private static Properties loadProperties(String propFileName) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = Parameters.class.getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			properties.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		return properties;
	}

}
