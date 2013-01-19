package com.emilevictor.wit.whats_in_there;

import java.util.ArrayList;
import java.util.List;

public class RoomBuildingParser {
	private String inputFromForm;
	
	public class InvalidInputException extends Exception {
	    /**
		 * 
		 */
		private static final long serialVersionUID = -1857854769415033105L;

		public InvalidInputException(String message) {
	        super(message);
	    }
	}
	
	public RoomBuildingParser(String inputFromForm) {
		this.inputFromForm = inputFromForm;
	}
	
	public List<String> giveMeRoomAndBuildingSeparately() throws InvalidInputException
	{
		String[] splitString = this.inputFromForm.trim().split("-");
		if (splitString.length < 2 || splitString.length > 2)
		{
			throw new InvalidInputException("The input in the form was in the wrong format.");
		}
		List<String> roomBuilding = new ArrayList<String>();
		roomBuilding.add(splitString[0]);
		roomBuilding.add(splitString[1]);
		return roomBuilding;
	}
}
