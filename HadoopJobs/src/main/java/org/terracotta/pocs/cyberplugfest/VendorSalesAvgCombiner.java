package org.terracotta.pocs.cyberplugfest;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class VendorSalesAvgCombiner extends Reducer<Text, MyCustomWritable, Text, MyCustomWritable> {

	@Override
	protected void reduce(Text key, Iterable<MyCustomWritable> values, Context context)
			throws IOException, InterruptedException {

		double sum = 0;
		int count = 0;
		for (MyCustomWritable val : values) {
			sum += val.getSum().get();
			count += val.getCount().get();
		}
		context.write(key, new MyCustomWritable(sum,count));
	}
}