package com.digitalwallet.excption;

public class FileExistsExcption extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileExistsExcption() {
		super();
	}

	public FileExistsExcption(String message, Throwable cause) {
		super(message, cause);
	}

	public FileExistsExcption(String message) {
		super(message);
	}
	
	
}
