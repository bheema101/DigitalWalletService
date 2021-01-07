package com.file.service.download;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

public interface FileDownload {
	public ResponseEntity<Resource> download(String fileName) throws IOException;
	
}
