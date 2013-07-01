package org.terracotta.demo.cyberplugfest.jms;

import java.util.HashMap;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.demo.cyberplugfest.utils.DataUtils;
import org.terracotta.demo.cyberplugfest.utils.POSTransaction;
import org.terracotta.demo.cyberplugfest.utils.SpringApplicationContext;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EventPublisher implements Job {
	private static Logger log = LoggerFactory.getLogger(EventPublisher.class);

	private static String EVENT_TYPE = "eventType";
	
	private MessageCoordinator messageCoordinator = (MessageCoordinator)SpringApplicationContext.getBean("messageCoordinator");
	
	// Since Quartz will re-instantiate a class every time it
	// gets executed, members non-static member variables can
	// not be used to maintain state!
	public EventPublisher() {}

	public void setMessageCoordinator(MessageCoordinator messageCoordinator) {
		this.messageCoordinator = messageCoordinator;
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job simply prints out its job name and the
		// date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();

		// Grab and print passed parameters
		JobDataMap data = context.getJobDetail().getJobDataMap();

		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put(EVENT_TYPE, "transaction");
		properties.put("posTransaction", "normal");
		
		POSTransaction tx = DataUtils.generateRandomNormalTransaction();
		properties.putAll(tx.toProperties());
		
		StringBuffer sb = new StringBuffer();
		for(Entry<String, String> entry : properties.entrySet()){
			if(sb.length() > 0)
				sb.append(",");
			sb.append(entry.getValue());
	    }
		messageCoordinator.sendMessage(sb.toString(), properties);
	}
}
