package com.digitalwallet.service.upload.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.digitalwallet.model.Fileinfo;
import com.digitalwallet.service.upload.FileUpload;
@Service
public class S3FileUpload  implements FileUpload {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(S3FileUpload.class);
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${bucketName}")
	private String bucketName;
	
	@Autowired
	AmazonDynamoDB amazonDynamoDB;
	
	private DynamoDBMapper dynamoDBMapper;
	
	@Autowired
	FileRepository fileRepository;
	

	@Override
	public void uploadfile(MultipartFile file) throws Exception {
		Map<String, String> metadata = new HashMap<>();
		if (!file.isEmpty()) {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			metadata.put("fileName", file.getOriginalFilename());
			metadata.put("fileType", file.getContentType());
			metadata.put("file Size", String.valueOf(file.getSize()));
			objectMetadata.setUserMetadata(metadata);
			objectMetadata.setContentLength(file.getSize());
			String path = String.format("%s",bucketName);
			try {
				LOGGER.info("file uploaded started {} ",file.getOriginalFilename() );
				s3client.putObject(path, file.getOriginalFilename(), file.getInputStream(), objectMetadata);
				LOGGER.info("file uploaded Ended {}",file.getOriginalFilename());
				try {
					 saveinDynomoDB(file.getOriginalFilename());
					 LOGGER.info("file Saved DynomoDB {}",file.getOriginalFilename());
				}
					catch (Exception e) {
						LOGGER.error("Error occred whil saving dynomoDB {}",file.getOriginalFilename(),e);
					}
				}
			
			catch (SdkClientException | IOException e) {
			//catch (Exception e) {

				throw new Exception(e);
			}
		}


		
	}
	
	public void saveinDynomoDB(String fileName) {
		boolean createTable = createTable();
		if(createTable) {
			LOGGER.info("TABLE WAS CREATED");
		}
		Fileinfo save = fileRepository.save(new Fileinfo("Tu1001", fileName, "Pnr", "tripitem", LocalDateTime.now()));
		
		
	}

	private boolean createTable() {
		dynamoDBMapper = new  DynamoDBMapper(amazonDynamoDB);
		CreateTableRequest tableRequest = dynamoDBMapper
		        .generateCreateTableRequest(Fileinfo.class);
		
		
		DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(Fileinfo.class);

		tableRequest.setProvisionedThroughput(
		        new ProvisionedThroughput(1L, 1L));
		//return TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
		return TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
	}
	
	
	public  Fileinfo findBy(String fileName) {
		
		Iterable<Fileinfo> findById = fileRepository.findAll();
		Fileinfo fileInfo = null;
		
		for(Fileinfo x : findById){
			LOGGER.info("fileName :"+x.getFileName()+"id :"+x.getId());
			fileInfo =x;
		};
		
		//Fileinfo findById2 = fileRepository.findById(fileName).get();
		
		return fileInfo;
	}

}
