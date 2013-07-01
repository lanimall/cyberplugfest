package org.terracotta.pocs.cyberplugfest;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VendorSalesAvgMapper extends Mapper<LongWritable, Text, Text, MyCustomWritable> {
	private static Logger log = LoggerFactory.getLogger(VendorSalesAvgMapper.class);
	public static final String PROPNAME_TOKENCOUNT = "input.format.validtokens.count";
	public static final String PROPNAME_VENDORINDEX = "input.format.vendorTokenIndex";
	public static final String PROPNAME_TRANSACTIONINDEX = "input.format.transactionAmountIndex";

	private int validTokenCount;
	private int vendorTokenIndex;
	private int transactionAmountIndex;
			
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		vendorTokenIndex = context.getConfiguration().getInt(PROPNAME_VENDORINDEX, -1);
		transactionAmountIndex = context.getConfiguration().getInt(PROPNAME_TRANSACTIONINDEX, -1);
		validTokenCount = context.getConfiguration().getInt(PROPNAME_TOKENCOUNT, -1);
		if(validTokenCount < 0 || vendorTokenIndex < 0 || transactionAmountIndex < 0){
			throw new IllegalArgumentException("Make sure properties " + PROPNAME_TOKENCOUNT + "," + PROPNAME_VENDORINDEX + ", and " + PROPNAME_TRANSACTIONINDEX + " are all set with valid integers.");
		}
	}

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		log.debug("entering map");
    	String line = value.toString();
    	log.debug(line);
        StringTokenizer lineTokenizer = new StringTokenizer(line, ",");
        
        //quick check that the file input is properly formated
        if(lineTokenizer.countTokens() >= validTokenCount){
        	String vendorId = null;
        	Double transactionAmount = null;
        	try{
        		String token = null;
        		int tokenIndex = 0;
            	while ((vendorId == null || transactionAmount == null) && lineTokenizer.hasMoreTokens() && tokenIndex <= validTokenCount) {
            		token = lineTokenizer.nextToken();
            		if(tokenIndex == vendorTokenIndex){
                		vendorId = token;
                	} else if (tokenIndex == transactionAmountIndex) {
                		transactionAmount = Double.parseDouble(token);
                	}
                    tokenIndex++;
                }
            	
            	if(null != vendorId && null != transactionAmount){
            		context.write(new Text(vendorId), new MyCustomWritable(transactionAmount, 1));
            	}
        	} catch (Exception exc) {
        		log.warn("An error occurred during the line parsing", exc);
        	}
        }
	}
}
