/*
 * Copyright 2012 by
 *
 * Software AG, Uhlandstrasse 12, D-64297 Darmstadt, GERMANY
 *
 * All rights reserved
 *
 * This software is the confidential and proprietary
 * information of Software AG ('Confidential Information').
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with Software AG or its distributors.
 */

package org.terracotta.demo.cyberplugfest.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final Logger log = LoggerFactory.getLogger(Config.class);	
	
	//singleton instance
	private static Config instance;
	
	public static final String APP_CONFIGPATH_DEFAULT = "application.properties";
	public static final String APP_CONFIGPATH_ENVPROP = "app.config.path";
	
	//wrapper property
	private final PropertyUtils properties; 

	public static Config getInstance() {
		if (instance == null)
		{
			synchronized(Config.class) {  //1
				if (instance == null){
					try {
						String propLocation = "";
						if(null != System.getProperty(APP_CONFIGPATH_ENVPROP)){
							propLocation = System.getProperty(APP_CONFIGPATH_ENVPROP);
							log.info(APP_CONFIGPATH_ENVPROP + " environment property specified: Loading application configuration from " + propLocation);
						}
						else{
							log.info("Loading application configuration from classpath " + APP_CONFIGPATH_DEFAULT);
							propLocation = APP_CONFIGPATH_DEFAULT;
						}

						instance = new Config(propLocation);
					} catch (Exception e) {
						log.error("Could not load the property file", e);
					}
				}
			}
		}
		return instance;
	}
	
	private Config(String propertyFile) throws Exception {
		properties = new PropertyUtils(propertyFile);
	}

	public PropertyUtils getProperties() {
		return properties;
	}
}
