package com.digitalwallet.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FormWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MultipartFile file;
    
    private String tuid;
    private String tripid;
    private String pnr;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getTuid() {
		return tuid;
	}
	public void setTuid(String tuid) {
		this.tuid = tuid;
	}
	public String getTripid() {
		return tripid;
	}
	public void setTripid(String tripid) {
		this.tripid = tripid;
	}
	public String getPnr() {
		return pnr;
	}
	public void setPnr(String pnr) {
		this.pnr = pnr;
	}
	
	
	@Override
	public String toString() {
		return "FormWrapper [file=" + file + ", tuid=" + tuid + ", tripid=" + tripid + ", pnr=" + pnr + "]";
	}
    
    
    
    
    
    
	
}
