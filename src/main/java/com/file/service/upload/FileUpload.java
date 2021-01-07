package com.file.service.upload;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpload {
	public void uploadfile(MultipartFile file) throws Exception ;
}
