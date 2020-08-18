package com.kh.nxcr.empl.model.exception;

public class EmployeeSaveException extends RuntimeException {

	public EmployeeSaveException() {
		super();
	}

	public EmployeeSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EmployeeSaveException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmployeeSaveException(String message) {
		super(message);
	}

	public EmployeeSaveException(Throwable cause) {
		super(cause);
	}

	
}
