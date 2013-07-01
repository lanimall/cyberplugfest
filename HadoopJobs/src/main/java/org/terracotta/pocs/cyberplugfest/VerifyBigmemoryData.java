package org.terracotta.pocs.cyberplugfest;

import java.net.URL;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class VerifyBigmemoryData {
	public static void main(String[] args) throws InterruptedException {

		URL url = VerifyBigmemoryData.class.getResource("/ehcache.xml");
		CacheManager cacheManager = CacheManager.newInstance(url);
		
		//vendorAvgSpend
		
		Cache cache = cacheManager.getCache("anomolies");

		int count=0;
		while(count++ < 10){
			final List keyList = cache.getKeys();
			System.out.println("\n\n Anomaly Data:" + keyList.size() + " entries");
			for (Object o : keyList) {
				final Element element = cache.get(o);
				System.out.print(element.getObjectKey());
				System.out.print(" = ");
				System.out.println(element.getObjectValue());
			}

			System.out.println("\n\n ---------------------------- \n\n");
			Thread.sleep(5000);
		}
		
		cacheManager.shutdown();
		System.exit(0);
	}
}