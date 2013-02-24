package com.emilevictor.wit.computeravailability.uqlibrary;

/**
 * Stores a UQ library and all information that can be gathered for today.
 * @author emilevictor
 *
 */
public class Library {

	private String name;
	private String openToday;
	private String openTimesToday;
	
	private String openingHour;
	private String closingHour;
	
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

	public String getClosingHour() {
		return closingHour;
	}

	public void setClosingHour(String closingHour) {
		this.closingHour = closingHour;
	}

	public String getOpeningHour() {
		return openingHour;
	}

	public void setOpeningHour(String openingHour) {
		this.openingHour = openingHour;
	}

	public String getOpenTimesToday() {
		return openTimesToday;
	}

	public void setOpenTimesToday(String openTimesToday) {
		this.openTimesToday = openTimesToday;
	}

	public String getOpenToday() {
		return openToday;
	}

	public void setOpenToday(String openToday) {
		this.openToday = openToday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
