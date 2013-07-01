package org.terracotta.pocs.cyberplugfest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 */
public final class Main
{
	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) throws Exception
	{
		final ConfigurableApplicationContext context;
		if (args.length == 0)
		{
			context = new ClassPathXmlApplicationContext("/META-INF/spring/application-context.xml", Main.class);
		}
		else
		{
			context = new FileSystemXmlApplicationContext(args[0]);
		}
		context.registerShutdownHook();
	}
}
