package com.digitalwallet.config;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

@Service
public class FileInfoCreateTable {

   public static void main(String[] args) throws Exception {
	//public void createTable() {
        AmazonDynamoDB client = createClinent();


        DynamoDB dynamoDB = new DynamoDB(client);

        String tableName = "Fileinfo";
        //deleteTable(tableName,client);
        createTable(dynamoDB, tableName);

    }

public static void createTable(DynamoDB dynamoDB, String tableName) {
	try {
	    System.out.println("Attempting to create table; please wait...");
	    /*Table table = dynamoDB.createTable(tableName,
	        Arrays.asList(new KeySchemaElement("id", KeyType.HASH)), // Partition
	                                                                  // key
	            
	        Arrays.asList(new AttributeDefinition("id", ScalarAttributeType.S),
	            new AttributeDefinition("tuid", ScalarAttributeType.S),
	            new AttributeDefinition("tripid", ScalarAttributeType.S),
	            new AttributeDefinition("pnr", ScalarAttributeType.S),
	            new AttributeDefinition("fileName", ScalarAttributeType.S),
	            new AttributeDefinition("uploadedDateTime", ScalarAttributeType.S)),
	        
	        new ProvisionedThroughput(1L, 1L));
	    table.waitForActive();
	    System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());*/
	    
	    
	    
	    CreateTableRequest createTableRequest = new CreateTableRequest();
	    createTableRequest.setTableName(tableName);
	    createTableRequest.setKeySchema( Arrays.asList(new KeySchemaElement("id", KeyType.HASH),
	    		                                       new KeySchemaElement("tuid", KeyType.RANGE)));
	    		                                       //new KeySchemaElement("fileName", KeyType.RANGE)));
	    createTableRequest.setAttributeDefinitions(Arrays.asList(new AttributeDefinition("id", ScalarAttributeType.S),
	            new AttributeDefinition("tuid", ScalarAttributeType.S),
	           // new AttributeDefinition("tripid", ScalarAttributeType.S),
	            new AttributeDefinition("pnr", ScalarAttributeType.S),
	            new AttributeDefinition("fileName", ScalarAttributeType.S)));
	          //  new AttributeDefinition("uploadedDateTime", ScalarAttributeType.S)));
	    createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
	    GlobalSecondaryIndex gsi = new GlobalSecondaryIndex();
	    gsi.setKeySchema(Arrays.asList(new KeySchemaElement("tuid", KeyType.HASH),
	    		                       new KeySchemaElement("pnr", KeyType.RANGE)));
	    gsi.setProjection(new Projection().withProjectionType(ProjectionType.INCLUDE).withNonKeyAttributes(Arrays.asList("tripid","fileName","uploadedDateTime")));
	    gsi.setIndexName("tuid-pnr-index");
	    gsi.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
	    createTableRequest.setGlobalSecondaryIndexes(Arrays.asList(gsi));
	    
	    Table table = dynamoDB.createTable(createTableRequest);
	    
	    System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

	}
	catch (Exception e) {
	    System.err.println("Unable to create table: ");
	    System.err.println(e.getMessage());
	}
}

public static AmazonDynamoDB createClinent() {
	/*AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSCredentialsProvider() {
		
		@Override
		public void refresh() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public AWSCredentials getCredentials() {
			 return new BasicAWSCredentials(
			          "dummykey", "dummysecertkey");
		}
	})
	        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:1234", "us-east-1"))
	        .build();
	*/
	
	
	AmazonDynamoDB client2 = new AmazonDynamoDBClient(amazonAWSCredentials());
    //.standard().withCredentials()
    if (!StringUtils.isEmpty("http://localhost:1234")) {
    	client2.setEndpoint("http://localhost:1234");
    }
    client2.setRegion(Region.getRegion(Regions.US_EAST_1));
    
   // return client;
	
	
	
	
	
	return client2;
}
   
   public static void deleteTable(String tableName,AmazonDynamoDB client) {
	   DeleteTableRequest deleteTableRequest = new DeleteTableRequest();
       deleteTableRequest.setTableName(tableName);
       client.deleteTable(deleteTableRequest);
       System.out.println("table deleted successfully");
   }
   
   public static AWSCredentials amazonAWSCredentials() {
       return new BasicAWSCredentials(
         "dummykey", "dummysecertkey");
   }
   
}
