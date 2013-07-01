package org.terracotta.pocs.cyberplugfest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VendorSalesAvgMapperTest {
	private MapDriver<LongWritable, Text, Text, MyCustomWritable> m_mapDriver;
	private Configuration m_configuration;

	@Before
	public void setUp() throws Exception {
		m_configuration = new Configuration();
		m_configuration.setInt(VendorSalesAvgMapper.PROPNAME_TOKENCOUNT, 9);
		m_configuration.setInt(VendorSalesAvgMapper.PROPNAME_VENDORINDEX, 3);
		m_configuration.setInt(VendorSalesAvgMapper.PROPNAME_TRANSACTIONINDEX, 2);
		m_mapDriver = MapDriver.newMapDriver(new VendorSalesAvgMapper());
		m_mapDriver.setConfiguration(m_configuration);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		m_mapDriver.withInput(new LongWritable(0), new Text("1371751007001,2084560165888487,423.43,129642,09426447,electronics,transaction,transaction,2013-06-20T13:56:47:001-0400"));
		m_mapDriver.resetOutput();
		m_mapDriver.addOutput(new Text("129642"), new MyCustomWritable(423.43,1));
		m_mapDriver.runTest();
	}
}