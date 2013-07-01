package org.terracotta.demo.cyberplugfest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {
	public static RandomUtil rdm = new RandomUtil();
	
	public static POSTransaction generateRandomNormalTransaction(){
		POSTransaction tx = new POSTransaction();
		tx.setTxId(rdm.generateRandomNumericString(8));
		tx.setCreditCardNo(rdm.generateRandomNumericString(16));
		tx.setVendorId(rdm.getRandomObjectFromList(DataUtils.vendorIDs));
		tx.setVendorGeoLoc(DataUtils.vendorGeoLocations.get(tx.getVendorId()));
		tx.setPurchaseType(rdm.getRandomObjectFromArray(DataUtils.vendorFoodType.get(tx.getVendorId())));
		
		String purchaseAmount;
		if("food".equals(tx.getPurchaseType())){
			purchaseAmount = rdm.generateRandomInt(20, 150, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("beverage".equals(tx.getPurchaseType())){
			purchaseAmount = rdm.generateRandomInt(1, 20, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("produce".equals(tx.getPurchaseType())){
			purchaseAmount = rdm.generateRandomInt(10, 30, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("electronics".equals(tx.getPurchaseType())){
			purchaseAmount = rdm.generateRandomInt(100, 500, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("appliance".equals(tx.getPurchaseType())){
			purchaseAmount = rdm.generateRandomInt(500, 2000, true) + "." + rdm.generateRandomInt(0,100,false);
		} else {
			purchaseAmount = rdm.generateRandomInt(5, 100, true) + "." + rdm.generateRandomInt(0,100,false);
		}
		tx.setAmount(purchaseAmount);
		
		return tx;
	}
	
	public static POSTransaction generateRandomAnomalyTransaction(){
		POSTransaction tx = new POSTransaction();
		tx.setTxId(rdm.generateRandomNumericString(8));
		tx.setCreditCardNo(rdm.getRandomObjectFromList(anomalyCreditCards));
		tx.setVendorId(rdm.getRandomObjectFromList(DataUtils.vendorIDs));
		tx.setVendorGeoLoc(DataUtils.vendorGeoLocations.get(tx.getVendorId()));
		tx.setPurchaseType(rdm.getRandomObjectFromArray(DataUtils.vendorFoodType.get(tx.getVendorId())));
		
		String purchaseAmount;
		int multiplier = rdm.generateRandomInt(10, 19, true);
		if("food".equals(tx.getPurchaseType())){
			purchaseAmount = multiplier * rdm.generateRandomInt(20, 150, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("beverage".equals(tx.getPurchaseType())){
			purchaseAmount = multiplier * rdm.generateRandomInt(1, 20, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("produce".equals(tx.getPurchaseType())){
			purchaseAmount = multiplier * rdm.generateRandomInt(10, 30, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("electronics".equals(tx.getPurchaseType())){
			purchaseAmount = multiplier * rdm.generateRandomInt(100, 500, true) + "." + rdm.generateRandomInt(0,100,false);
		} else if("appliance".equals(tx.getPurchaseType())){
			purchaseAmount = multiplier * rdm.generateRandomInt(500, 2000, true) + "." + rdm.generateRandomInt(0,100,false);
		} else {
			purchaseAmount = multiplier * rdm.generateRandomInt(5, 100, true) + "." + rdm.generateRandomInt(0,100,false);
		}
		tx.setAmount(purchaseAmount);
		return tx;
	}
	
	public static Map<String, String[]> vendorFoodType = new HashMap<String, String[]>();
	static {
		vendorFoodType.put("998877", new String[]{"food","beverage","produce","electronics"});
		vendorFoodType.put("557643", new String[]{"food","beverage","produce"});
		vendorFoodType.put("323451", new String[]{"food","beverage"});
		vendorFoodType.put("129642", new String[]{"electronics","appliance"});
		vendorFoodType.put("345672", new String[]{"electronics","appliance"});
		vendorFoodType.put("982373", new String[]{"food","electronics"});
	}
	
	public static Map<String, String> vendorGeoLocations = new HashMap<String, String>();
	static {
		vendorGeoLocations.put("998877", "32.705664,-117.155617");
		vendorGeoLocations.put("557643", "35.46067,-84.023437");
		vendorGeoLocations.put("323451", "31.052934,-92.109375");
		vendorGeoLocations.put("129642", "44.339565,-117.421875");
		vendorGeoLocations.put("345672", "36.031332,-117.070312");
		vendorGeoLocations.put("982373", "35.46067,-84.023437");
	}
	
	public static List<String> vendorIDs = new ArrayList<String>();
	static {
		vendorIDs.add("998877");
		vendorIDs.add("557643");
		vendorIDs.add("323451");
		vendorIDs.add("129642");
		vendorIDs.add("345672");
		vendorIDs.add("982373");
	}
	
	public static List<String> purchaseTypes = new ArrayList<String>();
	static {
		purchaseTypes.add("food");
		purchaseTypes.add("beverage");
		purchaseTypes.add("produce");
		purchaseTypes.add("electronics");
		purchaseTypes.add("appliance");
	}
	
	public static List<String> anomalyCreditCards = new ArrayList<String>();
	static {
		anomalyCreditCards.add("4444543298761000");
		anomalyCreditCards.add("3999123455556789");
		anomalyCreditCards.add("5100223388997766");
	}
}
