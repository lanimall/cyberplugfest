package org.terracotta.pocs.cyberplugfest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class MyCustomWritable implements Writable {
	private DoubleWritable sum;
	private IntWritable count;

	public MyCustomWritable() {
		set(new DoubleWritable(), new IntWritable());
	}

	public MyCustomWritable(Double sum, int count) {
		set(new DoubleWritable(sum),new IntWritable(count));
	}

	public void set(DoubleWritable sum, IntWritable count) {
		this.sum = sum;
		this.count = count;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		sum.readFields(in);
		count.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		sum.write(out);
		count.write(out);
	}

	public DoubleWritable getSum() {
		return sum;
	}

	public IntWritable getCount() {
		return count;
	}

	@Override
	public int hashCode() {
		return sum.hashCode() * 163 + count.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MyCustomWritable) {
			MyCustomWritable tp = (MyCustomWritable) o;
			return sum.equals(tp.sum) && count.equals(tp.count);
		}
		return false;
	}

	@Override
	public String toString() {
		return count.toString() + "\t" + sum.toString();
	}
}