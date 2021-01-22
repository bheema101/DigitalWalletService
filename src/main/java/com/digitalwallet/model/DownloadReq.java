package com.digitalwallet.model;

public class DownloadReq {
	
	String tuid;
	String fileId;
	String fileName;
	
	
	public DownloadReq(String tuid, String id, String fileName) {
		super();
		this.tuid = tuid;
		this.fileId = id;
		this.fileName = fileName;
	}
	
	
	public DownloadReq() {
	}


	public String getTuid() {
		return tuid;
	}
	public void setTuid(String tuid) {
		this.tuid = tuid;
	}
	
	
	
	public String getFileId() {
		return fileId;
	}

	
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "DownloadReq [tuid=" + tuid + ", id=" + fileId + ", fileName=" + fileName + "]";
	}
	
	

}
