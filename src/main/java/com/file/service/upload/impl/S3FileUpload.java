package com.file.service.upload.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.file.service.upload.FileUpload;
@Service
public class S3FileUpload  implements FileUpload {
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${bucketName}")
	private String bucketName;
	

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
			String path = String.format("%s/%s",bucketName,"temp");
			try {
				s3client.putObject(path, file.getOriginalFilename(), file.getInputStream(), objectMetadata);
			} catch (SdkClientException | IOException e) {

				throw new Exception(e);
			}
		}


		
	}

}
