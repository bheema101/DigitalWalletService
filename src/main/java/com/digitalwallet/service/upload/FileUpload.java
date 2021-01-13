package com.digitalwallet.service.upload;

import org.springframework.web.multipart.MultipartFile;

import com.digitalwallet.model.Fileinfo;
import com.digitalwallet.model.FormWrapper;

public interface FileUpload {
	public void uploadfile(MultipartFile file) throws Exception ;
	public void uploadfile(MultipartFile file,FormWrapper formWrapper) throws Exception ;
	public  Fileinfo findBy(String fileName);
}
