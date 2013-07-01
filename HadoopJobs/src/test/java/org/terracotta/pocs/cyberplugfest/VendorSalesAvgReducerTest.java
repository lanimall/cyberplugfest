package org.terracotta.pocs.cyberplugfest;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VendorSalesAvgReducerTest {
	private ReduceDriver<Text, MyCustomWritable, Text, DoubleWritable> m_reduceDriver;
	private Configuration m_configuration;

	@Before
	public void setUp() throws Exception {
		m_configuration = new Configuration();

		m_reduceDriver = ReduceDriver.newReduceDriver(new VendorSalesAvgReducer());
		m_reduceDriver.setConfiguration(m_configuration);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() {
		final List<MyCustomWritable> list = new ArrayList<MyCustomWritable>();
        list.add(new MyCustomWritable(23.60,3));
        list.add(new MyCustomWritable(34.65,2));
        list.add(new MyCustomWritable(76.45,6));

        final Text rowcol = new Text("vendorid1");
        m_reduceDriver.withInput(rowcol, list);
        m_reduceDriver.resetOutput();
        m_reduceDriver.addOutput(rowcol, new DoubleWritable((23.60+34.65+76.45)/11));
        m_reduceDriver.runTest();
	}
}
