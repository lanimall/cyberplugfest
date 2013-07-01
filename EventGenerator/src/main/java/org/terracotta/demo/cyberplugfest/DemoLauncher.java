package org.terracotta.demo.cyberplugfest;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Scanner;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.terracotta.demo.cyberplugfest.jms.AnomolyEventPublisher;
import org.terracotta.demo.cyberplugfest.jms.EventPublisher;
import org.terracotta.demo.cyberplugfest.utils.Config;

/**
 * @author Fabien Sanglier
 * 
 */
public class DemoLauncher {
	private static Logger log = LoggerFactory.getLogger(DemoLauncher.class);

	private static final String JOBINTERVAL = "transactions.intervalinmillis";
	private static final String ANOMALYJOBINTERVAL = "anomolies.intervalinmillis";

	private static final int JOBINTERVAL_DEFAULT = 1000;

	private Scheduler sched = null;

	public DemoLauncher(Scheduler sched) {
		this.sched = sched;
		init();
	}

	public static void main(String[] args) throws Exception {
		final ConfigurableApplicationContext context;
		if (args.length == 0)
		{
			context = new ClassPathXmlApplicationContext("/application-context.xml", DemoLauncher.class);
		}
		else
		{
			context = new FileSystemXmlApplicationContext(args[0]);
		}

		SchedulerFactory sf = new StdSchedulerFactory();
		DemoLauncher launcher = new DemoLauncher(sf.getScheduler());
		launcher.start();

		boolean keepRunning = true;
		while (keepRunning) {
			System.out.println("Press 'P' to pause triggers, 'S' to start them, and 'Q' to terminate program.");
			String input = getInput();
			if (input.length() == 0) {
				continue;
			}
			keepRunning = (!"Q".equalsIgnoreCase(input));
		}

		launcher.shutdown();
		context.registerShutdownHook();
		System.out.println("Completed");
		System.exit(0);
	}

	private static String getInput() {
		System.out.println(">>");

		// option1
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter(System.getProperty("line.separator"));
		return sc.nextLine();
	}

	public void init() {
		log.info("------- Initializing Schedulers -------------------");
		try {
			// get a "nice round" time a few seconds in the future....
			setupNormalJob(nextGivenSecondDate(null, 5));
			setupAnomolyJob(nextGivenSecondDate(null, 10));
			sched.start();
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}

	private void setupNormalJob(Date startTime) throws SchedulerException{
		log.info("------- Scheduling the normal transaction job ----------------");

		JobDetail job1 = newJob(EventPublisher.class)
				.withIdentity("normalTransactions", "transactions")
				.build();

		int intervalMillis = -1;
		if(System.getenv().containsKey(JOBINTERVAL)){
			try {
				intervalMillis = Integer.parseInt(System.getenv(JOBINTERVAL));
			} catch (NumberFormatException e) {
				log.warn("");
			}
		} else {
			intervalMillis = Config.getInstance().getProperties().getPropertyAsInt(JOBINTERVAL, JOBINTERVAL_DEFAULT);
		}

		if(intervalMillis < 0)
			intervalMillis = JOBINTERVAL_DEFAULT;

		SimpleTrigger trigger1 = newTrigger() 
				.withIdentity("normalTransactions", "transactions")
				.startAt(startTime)
				.withSchedule(simpleSchedule()
						.withIntervalInMilliseconds(intervalMillis)
						.repeatForever()
						.withMisfireHandlingInstructionNextWithRemainingCount()
						)
						.build();

		// schedule the job to run
		Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
		log.info(job1.getKey() +
				" will run at: " + scheduleTime1 +  
				" and repeat: " + trigger1.getRepeatCount() + 
				" times, every " + trigger1.getRepeatInterval() + " milliseconds");
	}

	private void setupAnomolyJob(Date startTime) throws SchedulerException{
		log.info("------- Scheduling the anomoly transactions job ----------------");

		JobDetail job1 = newJob(AnomolyEventPublisher.class)
				.withIdentity("anomolyTransactions", "transactions")
				.build();

		//set initial countdown
		job1.getJobDataMap().put(AnomolyEventPublisher.FIRECOUNTDOWN, AnomolyEventPublisher.getRandomCountdown());
		
		int intervalMillis = -1;
		if(System.getenv().containsKey(ANOMALYJOBINTERVAL)){
			try {
				intervalMillis = Integer.parseInt(System.getenv(ANOMALYJOBINTERVAL));
			} catch (NumberFormatException e) {
				log.warn("");
			}
		} else {
			intervalMillis = Config.getInstance().getProperties().getPropertyAsInt(ANOMALYJOBINTERVAL, JOBINTERVAL_DEFAULT);
		}

		if(intervalMillis < 0)
			intervalMillis = JOBINTERVAL_DEFAULT;

		SimpleTrigger trigger1 = newTrigger() 
				.withIdentity("anomolyTransactions", "transactions")
				.startAt(startTime)
				.withSchedule(simpleSchedule()
						.withIntervalInMilliseconds(intervalMillis)
						.repeatForever()
						.withMisfireHandlingInstructionNextWithRemainingCount()
						)
						.build();

		// schedule the job to run
		Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
		log.info(job1.getKey() +
				" will run at: " + scheduleTime1 +  
				" and repeat: " + trigger1.getRepeatCount() + 
				" times, every " + trigger1.getRepeatInterval() + " milliseconds");
	}

	public void start() {
		try {
			sched.resumeTriggers(GroupMatcher.triggerGroupEquals("transactions"));
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}

	public void pause() {
		try {
			sched.pauseTriggers(GroupMatcher.triggerGroupEquals("transactions"));
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}

	public void shutdown() {
		try {
			log.info("------- Shutting Down ---------------------");
			sched.shutdown(true);
			log.info("------- Shutdown Complete -----------------");

			SchedulerMetaData metaData = sched.getMetaData();
			log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}
}
