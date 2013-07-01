package org.terracotta.demo.cyberplugfest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class POSTransaction {
	private static SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");

	private String txId;
	private String amount;
	private String creditCardNo;
	private String vendorId;
	private String vendorGeoLoc;
	private String purchaseType;
	private Date txDate;
	
	public POSTransaction(){
		this.txDate = new Date();
	}
	
	public POSTransaction(String txId, String amount, String creditCardNo,
			String vendorId, String purchaseType) {
		this(txId,amount,creditCardNo,vendorId,purchaseType, new Date());
	}
	
	public POSTransaction(String txId, String amount, String creditCardNo,
			String vendorId, String purchaseType, Date txDate) {
		super();
		this.txId = txId;
		this.txDate = txDate;
		this.creditCardNo = creditCardNo;
		this.vendorId = vendorId;
		this.purchaseType = purchaseType;
		this.amount = amount;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public Date getTxDate() {
		return txDate;
	}
	
	public long getTxDateTimeStamp() {
		return txDate.getTime();
	}

	public String getTxDateString() {
		return dateFormatGmt.format(txDate);
	}
	
	public void setTxDate(Date txDate) {
		this.txDate = txDate;
	}
	
	public String getVendorGeoLoc() {
		return vendorGeoLoc;
	}

	public void setVendorGeoLoc(String vendorGeoLoc) {
		this.vendorGeoLoc = vendorGeoLoc;
	}

	public Map<String, String> toProperties(){
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("timestamp", "" + getTxDateTimeStamp());
		properties.put("transDateTime", getTxDateString());
		properties.put("transID", getTxId());
		properties.put("creditCardNo", getCreditCardNo());
		properties.put("vendorID", getVendorId());
		properties.put("vendorGeoLoc", getVendorGeoLoc());
		properties.put("purchaseType", getPurchaseType());
		properties.put("transactionAmount", getAmount());
		return properties;
	}
}
