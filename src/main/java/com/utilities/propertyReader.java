package com.utilities;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class propertyReader {
	static Properties p;

	public static String readingProperty(String key) throws IOException {
		p=new Properties();
		FileReader reader = null;
		reader = new FileReader("src/main/resources/data.properties");
		p.load(reader);

		return p.getProperty(key);
	}

	public String writingProperty(String key, String val) throws IOException {
		p.setProperty(key, val);

		p.store(new FileWriter("info.properties"), "");

		return p.getProperty(key);
	}
}
