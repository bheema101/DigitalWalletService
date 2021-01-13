package com.digitalwallet.service.upload;

import org.springframework.web.multipart.MultipartFile;

import com.digitalwallet.model.Fileinfo;

public interface FileUpload {
	public void uploadfile(MultipartFile file) throws Exception ;
	public  Fileinfo findBy(String fileName);
}
