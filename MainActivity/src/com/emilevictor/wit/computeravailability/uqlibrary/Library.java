package com.emilevictor.wit.computeravailability.uqlibrary;

import java.io.Serializable;

/**
 * Stores a UQ library and all information that can be gathered for today.
 * @author emilevictor
 *
 */
public class Library implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1483400271656587330L;
	private String name;
	private boolean openToday;
	private String openTimesToday;
	
	
	private Integer numberComputersAvailable;
	private Integer numberComputersTotal;
	
	private Boolean has24x7StudySpace;
	
	private String extraComments;

	public String getExtraComments() {
		return extraComments;
	}

	public void setExtraComments(String extraComments) {
		this.extraComments = extraComments;
	}

	public Boolean getHas24x7StudySpace() {
		return has24x7StudySpace;
	}

	public void setHas24x7StudySpace(Boolean has24x7StudySpace) {
		this.has24x7StudySpace = has24x7StudySpace;
	}

	public Integer getNumberComputersTotal() {
		return numberComputersTotal;
	}

	public void setNumberComputersTotal(Integer numberComputersTotal) {
		this.numberComputersTotal = numberComputersTotal;
	}

	public Integer getNumberComputersAvailable() {
		return numberComputersAvailable;
	}

	public void setNumberComputersAvailable(Integer numberComputersAvailable) {
		this.numberComputersAvailable = numberComputersAvailable;
	}

	

	public String getOpenTimesToday() {
		return openTimesToday;
	}

	public void setOpenTimesToday(String openTimesToday) {
		this.openTimesToday = openTimesToday;
	}

	public boolean getOpenToday() {
		return openToday;
	}

	public void setOpenToday(boolean openToday) {
		this.openToday = openToday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
