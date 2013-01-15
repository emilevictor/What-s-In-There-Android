package com.emilevictor.wit;

public class NoBuildingFoundException extends Exception {
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
