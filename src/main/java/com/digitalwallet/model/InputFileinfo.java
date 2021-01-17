package com.digitalwallet.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InputFileinfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
    private String tuid;
    private String fileName;
    private String pnr;
    private String tripId;
    private LocalDateTime uploadedTime;
    
    
    
    
	



	public InputFileinfo(String tuid, String fileName, String pnr, String tripId,
			LocalDateTime uploadedTime) {
		super();
		this.tuid = tuid;
		this.fileName = fileName;
		this.pnr = pnr;
		this.tripId = tripId;
		this.uploadedTime = uploadedTime;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getTuid() {
		return tuid;
	}



	public void setTuid(String tuid) {
		this.tuid = tuid;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getPnr() {
		return pnr;
	}



	public void setPnr(String pnr) {
		this.pnr = pnr;
	}



	public String getTripId() {
		return tripId;
	}



	public void setTripId(String tripId) {
		this.tripId = tripId;
	}



	public LocalDateTime getUploadedTime() {
		return uploadedTime;
	}



	public void setUploadedTime(LocalDateTime uploadedTime) {
		this.uploadedTime = uploadedTime;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
	@Override
	public String toString() {
		return "InputFileinfo [id=" + id + ", tuid=" + tuid + ", fileName=" + fileName + ", pnr=" + pnr + ", tripId="
				+ tripId + ", uploadedTime=" + uploadedTime + "]";
	}


}
