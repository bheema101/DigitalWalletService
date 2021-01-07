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

@Service
public class S3FileDownlLoad implements FileDownload {
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${bucketName}")
	private String bucketName;
	
	public byte[] downloadimage() throws IOException {
		S3Object s3Object = s3client.getObject(bucketName, "LATE BIRTH.pdf");
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		
		//inputStream.
		File file = new File("LATE BIRTH.pdf");
		//file.
		byte[] byteArray = IOUtils.toByteArray(inputStream);
		OutputStream os = new FileOutputStream(file);
		os.write(byteArray);
		return byteArray;
	}
	
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
		
		HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-type",mimeType );
        headers.add("Content-disposition", String.format("attachment; filename=\"" + fileName + "\""));
        ByteArrayResource resource2 = new ByteArrayResource(byteArray,fileName);
        
        return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(resource2.contentLength())
	            .body(resource2);
	}
	
	
	public ResponseEntity<ByteArrayResource> downloadfile() throws IOException {
		 // Get a range of bytes from an object and print the bytes.
		S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
        // Get an entire object, overriding the specified response headers, and print the object's content.
        ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                .withCacheControl("No-cache")
                .withContentDisposition("attachment; filename=LATE BIRTH.pdf");
        GetObjectRequest getObjectRequestHeaderOverride = new GetObjectRequest(bucketName, "LATE BIRTH.pdf")
                .withResponseHeaders(headerOverrides);
        headerOverrideObject = s3client.getObject(getObjectRequestHeaderOverride);
        byte[] data = IOUtils.toByteArray(headerOverrideObject.getObjectContent());
        final ByteArrayResource resource = new ByteArrayResource(data);
        
       
        
        
        
        
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + "LATE BIRTH.pdf" + "\"")
                .body(resource);
        	

	}
	
	
	public InputStream downloadimage(String fileName) throws IOException {
		S3Object s3Object = s3client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		
		//inputStream.
		//File file = new File(fileName);
		
		//file.
		//byte[] byteArray = IOUtils.toByteArray(inputStream);
		//InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		/*OutputStream os = new FileOutputStream(file);
		os.write(byteArray);*/
		return inputStream;
	}


}
