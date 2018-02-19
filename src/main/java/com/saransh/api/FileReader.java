package com.saransh.api;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * File Read operation using Apache's FileUtils which is optimized to use less memory.
 * @author sbansal
 *
 */
@Component
public class FileReader {
	private static final Logger logger = LogManager.getLogger(FileReader.class);

	public LineIterator readFile() {
		LineIterator it = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			// PS: The data file is located in /resources folder.
			File routedataFile = new File(classLoader.getResource("routes-data").getFile());
			it = FileUtils.lineIterator(routedataFile, "UTF-8");
		} catch (IOException e) {
			logger.error("Exception reading file :: " + e, e);
		}
		return it;
	}
}
