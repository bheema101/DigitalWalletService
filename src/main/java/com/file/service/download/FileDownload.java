package com.file.service.download;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

public interface FileDownload {
	public byte[] downloadimage() throws IOException;
	public ResponseEntity<Resource> download(String fileName) throws IOException;
	
	public ResponseEntity<ByteArrayResource> downloadfile() throws IOException;
	public InputStream downloadimage(String fileName) throws IOException;
	
}
