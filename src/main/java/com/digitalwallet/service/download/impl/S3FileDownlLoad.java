package com.digitalwallet.service.download.impl;

import java.io.IOException;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.Base64;
import com.amazonaws.util.IOUtils;
import com.digitalwallet.service.download.FileDownload;
import com.digitalwallet.util.Util;

@Service
public class S3FileDownlLoad implements FileDownload {
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${bucketName}")
	private String bucketName;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(S3FileDownlLoad.class);
	
	
	public ResponseEntity<Resource> download(String fileName) throws IOException {
		S3Object s3Object = s3client.getObject("digitalwallet", fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		//inputStream.
		byte[] byteArray = IOUtils.toByteArray(inputStream);
		String mimeType = URLConnection.guessContentTypeFromName(fileName);
		if (mimeType == null) {
			//unknown mimetype so set the mimetype to application/octet-stream
			mimeType = "application/octet-stream";
		}
		
		HttpHeaders headers = createHttpHeaders(fileName, mimeType);
        ByteArrayResource resource = new ByteArrayResource(byteArray,fileName);
        
        return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(resource.contentLength())
	            .body(resource);
	}
	
	public ResponseEntity<Resource> download(String fileId,String fileName,String tuid) throws IOException {
		fileId = fileId.concat(Util.encode(fileName));
		String path = String.format("%s/%s", bucketName,tuid);
		S3Object s3Object = s3client.getObject(path, fileId);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		//inputStream.
		byte[] byteArray = IOUtils.toByteArray(inputStream);
		String mimeType = URLConnection.guessContentTypeFromName(fileName);
		if (mimeType == null) {
			//unknown mimetype so set the mimetype to application/octet-stream
			mimeType = "application/octet-stream";
		}
		
		HttpHeaders headers = createHttpHeaders(fileName, mimeType);
        ByteArrayResource resource = new ByteArrayResource(byteArray,fileName);
        
        return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(resource.contentLength())
	            .body(resource);
	}


	private HttpHeaders createHttpHeaders(String fileName, String mimeType) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-type",mimeType );
        headers.add("Content-disposition", String.format("attachment; filename=\"" + fileName + "\""));
		return headers;
	}
	
	
	

	
	
	public static void main(String[] args) {
		S3FileDownlLoad fd = new S3FileDownlLoad();
	//	String decode = fd.decode("demo.txt");
		//System.err.println(decode);
	}
}
