package com.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
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
}
