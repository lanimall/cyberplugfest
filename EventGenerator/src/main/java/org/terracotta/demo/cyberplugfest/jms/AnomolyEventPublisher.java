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
public class AnomolyEventPublisher implements Job {
	private static Logger log = LoggerFactory.getLogger(AnomolyEventPublisher.class);

	private static String EVENT_TYPE = "eventType";
	public static String FIRECOUNTDOWN = "firecountdown";

	private MessageCoordinator messageCoordinator = (MessageCoordinator)SpringApplicationContext.getBean("messageCoordinator");

	// Since Quartz will re-instantiate a class every time it
	// gets executed, members non-static member variables can
	// not be used to maintain state!
	public AnomolyEventPublisher() {}

	public void setMessageCoordinator(MessageCoordinator messageCoordinator) {
		this.messageCoordinator = messageCoordinator;
	}

	public static int getRandomCountdown(){
		return DataUtils.rdm.generateRandomInt(0, 15, true);
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job simply prints out its job name and the
		// date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();

		// Grab and print passed parameters
		JobDataMap data = context.getJobDetail().getJobDataMap();
		int countdown = data.getIntValue(FIRECOUNTDOWN);
		
		if(log.isDebugEnabled())
			log.debug("Countdown:" + countdown);
		
		//execute message sending only if countdown is 0
		if(countdown <= 0){
			log.info("Sending message");

			HashMap<String, String> properties = new HashMap<String, String>();
			properties.put(EVENT_TYPE, "transaction");
			properties.put("posTransaction", "anomoly");

			POSTransaction tx = DataUtils.generateRandomAnomalyTransaction();
			properties.putAll(tx.toProperties());

			StringBuffer sb = new StringBuffer();
			for(Entry<String, String> entry : properties.entrySet()){
				if(sb.length() > 0)
					sb.append(",");
				sb.append(entry.getValue());
			}
			messageCoordinator.sendMessage(sb.toString(), properties);
			
			//reset countdown to soemthing random
			countdown = getRandomCountdown();
			log.info("New Countdown:" + countdown);
		} else {
			countdown--;
		}
		
		data.put(FIRECOUNTDOWN, countdown);
	}
}
