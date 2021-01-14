/**
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
*/



package com.digitalwallet.service.download.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.digitalwallet.model.Fileinfo;
@Service
public class Queryinvoker {
	
	
	@Autowired
	AmazonDynamoDB client;
	
    public  void queryTable() throws Exception {

    	DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("Fileinfo");
        QuerySpec querySpec = new QuerySpec();
        
        
        
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#fileName", "fileName");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":yyyy", "response_user");
        
        
       /* QuerySpec querySpec2 = new QuerySpec().withKeyConditionExpression("#yr = :yyyy").withNameMap(nameMap)
                .withValueMap(valueMap);*/
        querySpec.withFilterExpression("#fileName = :yyyy")
		 .withNameMap(nameMap)
		 .withValueMap(valueMap);
        
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println("Movies from 1985");
            items = table.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getString("id") + ": " + item.getString("fileName"));
            }

        }
        catch (Exception e) {
            System.err.println("Unable to query movies from 1985");
            System.err.println(e.getMessage());
        }

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        ScanSpec scanSpec = new ScanSpec();
        		
        		 
        		 
        		;/*.withProjectionExpression("#yr, title, info.rating")
            .withFilterExpression("#yr between :start_yr and :end_yr").withNameMap(new NameMap().with("#yr", "year"))
            .withValueMap(new ValueMap().withNumber(":start_yr", 1950).withNumber(":end_yr", 1959));
            
*/
        try {
            ItemCollection<ScanOutcome> items2 = table.scan(scanSpec);

            Iterator<Item> iter = items2.iterator();
            while (iter.hasNext()) {
                Item item2 = iter.next();
                System.out.println(item2.toString());
            }

        }
        catch (Exception e) {
            System.err.println("Unable to scan the table:");
            System.err.println(e.getMessage());
        }
        
             
        	
        	
        
        
    }
    
    
    public List<Fileinfo>  getFileinfosbytuid(Fileinfo fileinfo) {
		 Map<String, AttributeValue> expressionAttributeValues =
	     	    new HashMap<String, AttributeValue>();
	     	expressionAttributeValues.put(":tripidValue", new AttributeValue().withS("TU101"));
	     //	expressionAttributeValues.put("fileNameValue", new AttributeValue().withS("response_user"));
	     	expressionAttributeValues.put(":pnrValue", new AttributeValue().withS("YX123"));
	
	     	ScanRequest scanRequest = new ScanRequest()
	     	    .withTableName("Fileinfo");
	     	  // .withFilterExpression("tuid = tripidValue AND fileName = fileNameValue AND pnr = pnrValue")
	     	  // .withFilterExpression("tuid = :tripidValue AND  pnr = :pnrValue")
	     	   // .withFilterExpression("fileName = :fileNameValue")
	     	 //   .withExpressionAttributeValues(expressionAttributeValues);
	     	ScanResult result = client.scan(scanRequest);
	     	DynamoDBMapper mapper = new DynamoDBMapper(client);
	     	List<Fileinfo> fileInfos = mapper.marshallIntoObjects(Fileinfo.class, result.getItems());
	     	
	     	Map<String,String> filenameswithids = new HashMap<>();
	     	List<Map<String,String>> listoffileswithids = new ArrayList<>();
	     	for (Fileinfo fileInfo : fileInfos) {
	     	  // System.out.println(fileInfo.getId());
	     		filenameswithids.put(fileInfo.getId(), fileInfo.getFileName());
	     		
	     	}	
	     	listoffileswithids.add(filenameswithids);
	     
	     	
	     	
	     	
	     	ScanRequest scanRequest2 = new ScanRequest()
		     	    .withTableName("Fileinfo")
		     	  // .withFilterExpression("tuid = tripidValue AND fileName = fileNameValue AND pnr = pnrValue")
		     	   .withFilterExpression("tuid = :tripidValue AND  pnr = :pnrValue")
		     	   // .withFilterExpression("fileName = :fileNameValue")
		     	    .withExpressionAttributeValues(expressionAttributeValues);
		     	ScanResult result2 = client.scan(scanRequest2);
		     //	DynamoDBMapper mapper = new DynamoDBMapper(client);
		     	List<Fileinfo> fileInfos2 = mapper.marshallIntoObjects(Fileinfo.class, result2.getItems());
		     	
		     	
		     	
		     	 Map<String, AttributeValue> expressionAttributeValues3 =
		 	     	    new HashMap<String, AttributeValue>();
		 	     	expressionAttributeValues3.put(":tripidValue", new AttributeValue().withS("TU101"));
		 	     //	expressionAttributeValues3.put("fileNameValue", new AttributeValue().withS("response_user"));

		     	
		     	ScanRequest scanRequest3 = new ScanRequest()
			     	    .withTableName("Fileinfo")
			     	   .withFilterExpression("tuid = :tripidValue")
			     	    .withExpressionAttributeValues(expressionAttributeValues3);
			     	ScanResult result3 = client.scan(scanRequest3);
			     //	DynamoDBMapper mapper = new DynamoDBMapper(client);
			     	List<Fileinfo> fileInfos3 = mapper.marshallIntoObjects(Fileinfo.class, result3.getItems());
			     	
			     	DynamoDB dynamoDB = new DynamoDB(client);
			        Table table = dynamoDB.getTable("Fileinfo");
			        Index index = table.getIndex("tuid-pnr");
	
			        
			      /*  QuerySpec spec = new QuerySpec()
			        	 //   .withKeyConditionExpression("tuid = :tuidValue and pnr=:pnrValue")
			        	   .withKeyConditionExpression("tuid=:tuidValue")
			        		.withValueMap(new ValueMap()
			        	    .withString(":tuidValue","TU104"));
			        	  //  .withString(":pnrValue","YX104"));
*/
			        
			        
			        QuerySpec spec = new QuerySpec()
			        	    .withKeyConditionExpression("tuid = :tuidValue and begins_with(pnr,:pnrValue)")
			        	 //  .withKeyConditionExpression("tuid=:tuidValue")
			        		.withValueMap(new ValueMap()
			        	    .withString(":tuidValue","TU104")
			        	    .withString(":pnrValue","YX104"));
			        System.out.println("Compilation errors");
//		            spec.withKeyConditionExpression("Title = :v_title and begins_with(IssueId, :v_issue)")
//		                .withValueMap(new ValueMap().withString(":v_title", "Compilation error").withString(":v_issue", "A-"));
//		           // items = index.query(querySpec);   

			        ItemCollection<QueryOutcome> items = index.query(spec);

			        if(!items.iterator().hasNext()) {
			        	System.out.println("empty");
			           // return null;
			        }else {
			        	Iterator<Item> iter = items.iterator(); 
			        	while (iter.hasNext()) {
			        		System.out.println(iter.next().toJSONPretty());
			        	}
			        }
			     
			        
			        Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
			        attributeValues.put(":tuidValue", new AttributeValue().withS("TU104"));
			        
			        Map<String, String> attributeNames = new HashMap<String, String>();
			        attributeNames.put("#key1", "tuid");
			        
			        DynamoDBQueryExpression<Fileinfo> queryExpression = new DynamoDBQueryExpression<Fileinfo>()
			        	      .withIndexName("tuid-pnr")
			        	      .withExpressionAttributeValues(attributeValues)
			        	      .withExpressionAttributeNames(attributeNames)
			        	      .withKeyConditionExpression("#key1 = :val1")
			        	      .withConsistentRead(false);
			        PaginatedQueryList<Fileinfo> query = mapper.query(Fileinfo.class, queryExpression);
			        
			        
			        
			        
		    	return fileInfos;
	     	
	}
    
    
    public void insertData() {
    	
    	
    	 Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
	      attributeValues.put(":tuidValue", new AttributeValue("TU101"));
	      //attributeValues.put(":pnrValue", new AttributeValue().withS("YX104"));
	        
	      Map<String, String> attributeNames = new HashMap<String, String>();
	      attributeNames.put("#key1", "tuid");
	    //  attributeNames.put("#key2", "pnr");
	        
	    DynamoDBMapper mapper = new DynamoDBMapper(client);
	    
    	DynamoDBQueryExpression<Fileinfo> queryExpression = new DynamoDBQueryExpression<Fileinfo>()
      	      .withIndexName("tuid-pnr-index")
    	    //  .withIndexName("tuid-pnrall-index")
      	     .withExpressionAttributeValues(attributeValues)
      	     .withExpressionAttributeNames(attributeNames)
      	      .withKeyConditionExpression("#key1 = :tuidValue")
      	      .withConsistentRead(false); 
    	PaginatedQueryList<Fileinfo> query = mapper.query(Fileinfo.class, queryExpression);
    	
    	for(Fileinfo fileinfo : query) {
    		System.out.println(fileinfo);
    	}

    	DynamoDB dynamoDB = new DynamoDB(client);
        //Table table = dynamoDB.getTable("Fileinfo");
        //Fileinfo fileinfo = new Fileinfo("TU101","demo.txt","PN101","TRIP101",LocalDateTime.now());
       
     //   mapper.save(fileinfo);
    	
    }
    
    
    
}
