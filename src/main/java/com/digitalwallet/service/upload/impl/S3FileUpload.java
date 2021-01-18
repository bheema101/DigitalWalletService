package com.digitalwallet.service.upload.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.digitalwallet.config.FileInfoCreateTable;
import com.digitalwallet.model.Fileinfo;
import com.digitalwallet.model.FormWrapper;
import com.digitalwallet.service.upload.FileUpload;
@Service
public class S3FileUpload  implements FileUpload {
	
	private static final String SORT_KEY_PARAM_NAME = "pnr";

	private static final String PARTION_KEY_PARAM_NAME = "tuid";

	private static final String GLOBAL_INDEX_NAME = "tuid-pnr-index";

	private static final String PNR_VALUE = ":pnrValue";

	private static final String TUID_VALUE = ":tuidValue";

	private static final Logger LOGGER = LoggerFactory.getLogger(S3FileUpload.class);
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${bucketName}")
	private String bucketName;
	
	@Autowired
	private FileInfoCreateTable tableCreatror;
	
	
	
	@Autowired
	AmazonDynamoDB amazonDynamoDB;
	
	private DynamoDBMapper dynamoDBMapper;
	
	@Autowired
	FileRepository fileRepository;
	

	@Override
	public void uploadfile(MultipartFile file) throws Exception {
		
		if (!file.isEmpty()) {
			Map<String, String> metadata = new HashMap<>();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			metadata.put("fileName", file.getOriginalFilename());
			metadata.put("fileType", file.getContentType());
			metadata.put("file Size", String.valueOf(file.getSize()));
			objectMetadata.setUserMetadata(metadata);
			objectMetadata.setContentLength(file.getSize());
			String path = String.format("%s",bucketName);
			String fileId ="";
			try {
				// String tuid, String fileName, String pnr, String tripId, LocalDateTime uploadedTime) {
				Fileinfo fileinfo = new Fileinfo(file.getOriginalFilename(),PARTION_KEY_PARAM_NAME,SORT_KEY_PARAM_NAME,"tripId",LocalDateTime.now());
				fileId = saveinDynomoDB(fileinfo);
				 LOGGER.info("file Saved DynomoDB {}",file.getOriginalFilename());
			}
				catch (Exception e) {
					LOGGER.error("Error occred whil saving dynomoDB {}",file.getOriginalFilename(),e);
				}
			
			
			try {
				LOGGER.info("file uploaded started {} ",file.getOriginalFilename() );
				//s3client.putObject(path, fileId, file.getInputStream(), objectMetadata);
				LOGGER.info("file uploaded Ended {}",file.getOriginalFilename());
				
				}
			
	//		catch (SdkClientException | IOException e) {
			catch (Exception e) {

				throw new Exception(e);
			}
		}


		
	}
	
	
	
	public String saveinDynomoDB(Fileinfo fileInfo) {
		
			LOGGER.info("File savinginto DynomoDB statated ");
			dynamoDBMapper = new  DynamoDBMapper(amazonDynamoDB);
	        dynamoDBMapper.save(fileInfo);
	        LOGGER.info("File savinginto DynomoDB Ended ");
		return fileInfo.getId();
		
	}

	private boolean createTable() {
		dynamoDBMapper = new  DynamoDBMapper(amazonDynamoDB);
		CreateTableRequest tableRequest = dynamoDBMapper
		        .generateCreateTableRequest(Fileinfo.class);
		
		
		
		List<AttributeDefinition> otherAttrubites = new ArrayList<AttributeDefinition>();
		otherAttrubites.add(new AttributeDefinition().withAttributeName(PARTION_KEY_PARAM_NAME).withAttributeType(ScalarAttributeType.S));
		otherAttrubites.add(new AttributeDefinition().withAttributeName(SORT_KEY_PARAM_NAME).withAttributeType(ScalarAttributeType.S));
		//otherAttrubites.add(new AttributeDefinition().withAttributeName("fileName").withAttributeType(ScalarAttributeType.S));
		//otherAttrubites.add(new AttributeDefinition().withAttributeName("uploadedTime").withAttributeType(ScalarAttributeType.S));
		tableRequest.getAttributeDefinitions().addAll(otherAttrubites);
		
		List<AttributeDefinition> attributeDefinitions = tableRequest.getAttributeDefinitions();
		List<KeySchemaElement> keySchema = new ArrayList<>();
		keySchema.add(new KeySchemaElement(PARTION_KEY_PARAM_NAME,KeyType.RANGE));
		//keySchema.add(new KeySchemaElement("pnr",KeyType.RANGE));
		tableRequest.getKeySchema().addAll(keySchema);
		GlobalSecondaryIndex globalSecondaryIndex = new GlobalSecondaryIndex();
		KeySchemaElement keySchemaElement = new KeySchemaElement(PARTION_KEY_PARAM_NAME,KeyType.HASH);
		KeySchemaElement keySchemaElement2 = new KeySchemaElement(SORT_KEY_PARAM_NAME,KeyType.RANGE);
		globalSecondaryIndex.setIndexName("tuid-pnr");
		Projection projection = new Projection();
		
		projection.setProjectionType(ProjectionType.INCLUDE);
		projection.setNonKeyAttributes(Arrays.asList(PARTION_KEY_PARAM_NAME,SORT_KEY_PARAM_NAME));
		globalSecondaryIndex.setProjection(projection);
		
		
		
		
		
		globalSecondaryIndex.setKeySchema(Arrays.asList(keySchemaElement,keySchemaElement2));
		tableRequest.setGlobalSecondaryIndexes(Arrays.asList(globalSecondaryIndex));
		globalSecondaryIndex.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(Fileinfo.class);

		tableRequest.setProvisionedThroughput(
		        new ProvisionedThroughput(1L, 1L));
		return TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
	//	return TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
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
	
	
	
	
	public void saveintoS3(MultipartFile file,String fileId) {
		
		if(!file.isEmpty()) {
		Map<String, String> metadata = new HashMap<>();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		metadata.put("fileName", file.getOriginalFilename());
		metadata.put("Content-Type", file.getContentType());
		metadata.put("file Size", String.valueOf(file.getSize()));
		objectMetadata.setUserMetadata(metadata);
		objectMetadata.setContentLength(file.getSize());
		String path = String.format("%s",bucketName);
		try {
			LOGGER.info("file uploaded into s3 started {} ",file.getOriginalFilename() );
			//s3client.putObject(path, fileId, file.getInputStream(), objectMetadata);
			LOGGER.info("file uploaded Ended s3 {}",file.getOriginalFilename());
			
			}
		
		//catch (SdkClientException | IOException e) {
		catch (Exception e) {
		LOGGER.error("Exception occred file uploading into s3",e);
		//	throw new Exception(e);
		}
		}

	}



	@Override
	public void uploadfile(MultipartFile file, FormWrapper formWrapper) throws Exception {
		LOGGER.info("file uploaded uploaded startred");
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
		tableCreatror.createTable(dynamoDB, "Fileinfo");
		String fileId =saveinDynomoDB(new Fileinfo(formWrapper.getTuid(),file.getOriginalFilename(),formWrapper.getPnr(),formWrapper.getTripid(),LocalDateTime.now()));
		saveintoS3(file,fileId);
		LOGGER.info("file uploaded uploaded Ended");
	}
	
	
	public Fileinfo getfiindByid(String id) {
		return  fileRepository.findById(id).get();
		
	}



	@Override
	public List<Fileinfo> getAllFiles(String pnr,String tuid) {
		Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(TUID_VALUE, new AttributeValue(tuid));
		attributeValues.put(PNR_VALUE, new AttributeValue().withS(pnr));

		Map<String, String> attributeNames = new HashMap<String, String>();
		attributeNames.put("#key1", PARTION_KEY_PARAM_NAME);
		attributeNames.put("#key2", SORT_KEY_PARAM_NAME);


		DynamoDBQueryExpression<Fileinfo> queryExpression = new DynamoDBQueryExpression<Fileinfo>()
				.withIndexName(GLOBAL_INDEX_NAME)
				.withExpressionAttributeValues(attributeValues)
				.withExpressionAttributeNames(attributeNames)
				.withKeyConditionExpression("#key1 = "+TUID_VALUE+" and #key2 ="+PNR_VALUE)
				.withConsistentRead(false);
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
		PaginatedQueryList<Fileinfo> files = mapper.query(Fileinfo.class, queryExpression);

		List<String> fileIds = files.stream().map(Fileinfo::getId).collect(Collectors.toList());
		LOGGER.info("filed ids : {}",fileIds);

		return files;
	}

}
