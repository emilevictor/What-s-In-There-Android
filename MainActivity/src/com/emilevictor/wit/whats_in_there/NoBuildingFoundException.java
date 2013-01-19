package com.emilevictor.wit.whats_in_there;

public class NoBuildingFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3030286743120115139L;
	String exception;
	public NoBuildingFoundException(){
		super();
		exception="Unknown";
	}
	public NoBuildingFoundException(String exp){
		super(exp);
		this.exception=exp;
	}
	public String getException(){
		return this.exception;
	}
}
