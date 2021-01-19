package com.digitalwallet.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
//@EnableDynamoDBRepositories(basePackages = "com.digitalwallet.service.upload.impl")

public class S3Configuration {

	@Value("${accessKeyID}")
	private String accessKey;
	
	@Value("${secretAccessKeyID}")
	private String storAccessKey;
	
	
	@Bean
	public AmazonS3 createAmzonS3Client() {
		
		AWSCredentials credentials = new BasicAWSCredentials(
				accessKey, 
				storAccessKey
				);
		
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()//bheema101221@gmail.com
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.AP_SOUTH_1)
				  .build();
		return s3client;
	}
	
	@Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
	public AmazonDynamoDB amazonDynamoDB() {
		//for aws webservice
		/*AWSCredentials credentials = new BasicAWSCredentials(
				accessKey, 
				storAccessKey
				);
	    AmazonDynamoDB amazonDynamoDB 
	      = new AmazonDynamoDBClient(credentials);
	    
	    if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
	        amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
	    }
	    
	    amazonDynamoDB.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
	    
	    return amazonDynamoDB;
	    */
	    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(getAWSCredentialsProvider())
	            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:1234", "us-east-1"))
	            .build();
	    
	    return client;
    	
		/*
		 * AmazonDynamoDB client = new AmazonDynamoDBClient(new BasicAWSCredentials(
		 * "dummykey", "dummysecertkey")); //.standard().withCredentials() if
		 * (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
		 * client.setEndpoint(amazonDynamoDBEndpoint); } //
		 * client.setRegion(Region.getRegion(Regions.US_EAST_1));
		 * 
		 * return client;
		 */
	}

    
    
    public AWSCredentialsProvider getAWSCredentialsProvider() {
    	return new AWSCredentialsProvider() {

			@Override
			public AWSCredentials getCredentials() {
				// TODO Auto-generated method stub
				return amazonAWSCredentials();
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				
			}
    		
    	};
    }
    
    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
          amazonAWSAccessKey, amazonAWSSecretKey);
    }

}
