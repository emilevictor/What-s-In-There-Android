package com.emilevictor.wit.whats_in_there;

public class ClassNotCompleteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4739353386944992904L;


	String exception;
	public ClassNotCompleteException(){
		super();
		exception="Unknown";
	}
	public ClassNotCompleteException(String exp){
		super(exp);
		this.exception=exp;
	}
	public String getException(){
		return this.exception;
	}
}
