package com.file.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.file.model.Fileinfo;
import com.file.service.download.FileDownload;
import com.file.service.upload.FileUpload;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DigitalWalletController {


	
	
	@Autowired
	FileUpload fileuploadservice;
	
	
	@Autowired
	FileDownload fileDownloadServie;
	
	
	@Autowired
	AmazonS3 s3client;
	
	@Value("${bucketName}")
	private String bucketName;
	
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DigitalWalletController.class);
	
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, consumes = {
			"multipart/form-data" }, value = "/upload")
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
		LOGGER.info("file {} upload started...",uploadfile.getOriginalFilename());
		if (uploadfile.isEmpty()) {
			LOGGER.info("file is empty",uploadfile.getOriginalFilename());
			return new ResponseEntity("please select a file!", HttpStatus.OK);
		}

		try {
			fileuploadservice.uploadfile(uploadfile);

		} catch (Exception e) {
			LOGGER.error("Exception occred while uploading file {} ",uploadfile.getOriginalFilename(),e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("file {} upload Ended...",uploadfile.getOriginalFilename());
		return new ResponseEntity("Successfully uploaded - " + uploadfile.getOriginalFilename(), new HttpHeaders(),
				HttpStatus.OK);
	}

	
	
	
	@RequestMapping("/getAllFiles")
	public List<String> getallFilesNames() {
		 List<String> fileNames = new ArrayList<String>();
	       
	        
	        ObjectListing listObjects = s3client.listObjects(bucketName);
	        List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
	        for(S3ObjectSummary os :objectSummaries) {
	        	fileNames.add(os.getKey());
	        }
			return fileNames;
	}
	
	@RequestMapping("/fileResource")
	
	public ResponseEntity<Resource>  downloadFileSource(HttpServletRequest request2, HttpServletResponse response,
			@RequestParam(name = "fileName") String fileName)  {
		ResponseEntity<Resource> resoponse = null;
		try {
			resoponse =fileDownloadServie.download(fileName);
		}catch (Exception e) {
			LOGGER.error("Excption occured while file downloading from s3:",e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return 	resoponse;	
	}
	
	
	
	
	@RequestMapping("/readfromlocal")
	public void downloadSorcefromFileSustem(HttpServletRequest request2, HttpServletResponse response,
			@RequestParam(name = "fileName") String fileName) throws IOException {
		
		File file = new File(fileName);
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			//unknown mimetype so set the mimetype to application/octet-stream
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());

		
		if(file.exists()) {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
		}
		
		
	}
	
	
	@RequestMapping("getdynomoDB")
	public  ResponseEntity<Fileinfo> findBy( @RequestParam("fileName")String fileName) {
		return ResponseEntity.ok(fileuploadservice.findBy(fileName));
	}
	
	
	@RequestMapping(value = "/hello")
	public String Hello() {
		LOGGER.info("file {} upload started...");
		/*List<Bucket> buckets = s3client.listBuckets();
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getName());
		}*/
		return "HI";

	}
		
	

	
}
