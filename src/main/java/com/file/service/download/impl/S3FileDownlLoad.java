package com.file.service.download.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.file.service.download.FileDownload;
import com.file.service.upload.impl.S3FileUpload;

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


	private HttpHeaders createHttpHeaders(String fileName, String mimeType) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-type",mimeType );
        headers.add("Content-disposition", String.format("attachment; filename=\"" + fileName + "\""));
		return headers;
	}
	
	
	

}
