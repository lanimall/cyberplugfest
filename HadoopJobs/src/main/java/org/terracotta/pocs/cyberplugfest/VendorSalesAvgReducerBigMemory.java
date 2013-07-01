package org.terracotta.pocs.cyberplugfest;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.terracotta.bigmemory.hadoop.BigmemoryElementWritable;

public class VendorSalesAvgReducerBigMemory extends Reducer<Text, MyCustomWritable, Text, BigmemoryElementWritable> {

	@Override
	protected void reduce(Text key, Iterable<MyCustomWritable> values, Context context)
			throws IOException, InterruptedException {

		double sum = 0;
		int count = 0;
		for (MyCustomWritable val : values) {
			sum += val.getSum().get();
			count += val.getCount().get();
		}
		BigmemoryElementWritable elementWritable = new BigmemoryElementWritable(key.toString(), new Double(sum / count).toString());
		context.write(key, elementWritable);
	}
}