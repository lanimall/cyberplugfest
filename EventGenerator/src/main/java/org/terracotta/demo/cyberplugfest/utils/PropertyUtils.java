package org.terracotta.demo.cyberplugfest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 *
 */
public class PropertyUtils {
	private static Logger log = LoggerFactory.getLogger(PropertyUtils.class);
	
	private Properties properties;	

	public PropertyUtils(String propLocation) throws Exception {
		this.properties = loadProperties(propLocation);
	}

	public String getProperty(String key){
		if(log.isDebugEnabled())
			log.debug("Getting key:" + key);
		
		String val = System.getProperty(key);
		if (val == null)
			val = properties.getProperty(key);
		
		if(log.isDebugEnabled())
			log.debug("value:" + val);
		
		return (val == null)?null:val.trim();
	}

	public Boolean getPropertyAsBoolean(String key){
		String val = getProperty(key);
		if (val == null)
			return null;
		
		return Boolean.parseBoolean(val);
	}

	public Integer getPropertyAsInt(String key){
		String val = getProperty(key);
		if (val == null)
			return null;
		
		try{
			return Integer.parseInt(val);
		} catch (NumberFormatException nfe){
			return null;
		}
	}

	public Long getPropertyAsLong(String key){
		String val = getProperty(key);
		if (val == null)
			return null;
		
		try{
			return Long.parseLong(val);
		} catch (NumberFormatException nfe){
			return null;
		}
	}

	public String getProperty(String key, String defaultVal){
		String val = getProperty(key);
		if (val == null)
			val = defaultVal;
		return val;
	}

	public Long getPropertyAsLong(String key, long defaultVal){
		Long val = getPropertyAsLong(key);
		if (val == null)
			return defaultVal;
		return val;
	}

	public Integer getPropertyAsInt(String key, int defaultVal){
		Integer val = getPropertyAsInt(key);
		if (val == null)
			return defaultVal;
		return val;
	}

	public Boolean getPropertyAsBoolean(String key, boolean defaultVal){
		Boolean val = getPropertyAsBoolean(key);
		if (val == null)
			return defaultVal;
		return val;
	}

	public String[] getPropertiesAsStringValueArray(String key) {
		int index = 1;
		ArrayList<String> list = new ArrayList<String>();
		String tmpProperty;

		while ((tmpProperty = getProperty(key + '.' + index++)) != null) {
			list.add(tmpProperty);
		}

		return list.toArray(new String[list.size()]);
	}
	
	private Properties loadProperties(final String location) throws Exception {
		if(null == location)
			throw new OperationNotSupportedException("Location cannot be null");

		Properties props = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = IOUtils.getFile(location);
			props.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			inputStream.close();
		}
		return props;
	}
}
