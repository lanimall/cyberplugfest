package org.terracotta.demo.cyberplugfest.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {
	private static Logger log = LoggerFactory.getLogger(IOUtils.class);
	
	public static InputStream getFile(final String fileLocation) throws FileNotFoundException, OperationNotSupportedException {
		if(null == fileLocation)
			throw new OperationNotSupportedException("Location cannot be null");

		InputStream inputStream = null;
		if(fileLocation.indexOf("file:") > -1){
			String trueLocation = fileLocation.substring("file:".length());
			try{
				inputStream = new FileInputStream(trueLocation);
			} catch (FileNotFoundException fne){
				log.warn("could not find the file from path...trying in the classpath.");
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(trueLocation);
				log.info("Found the file in the classpath.");
			}
		} else {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileLocation);
		}
		
		if (inputStream == null) {
			throw new FileNotFoundException("Property file '" + fileLocation
					+ "' not found in the classpath");
		}

		return inputStream;
	}
}
