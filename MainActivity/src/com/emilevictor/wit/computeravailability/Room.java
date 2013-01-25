package com.emilevictor.wit.computeravailability;

import java.io.Serializable;

/**
 * Stores a room for the computer availability overview page.
 * @author emilevictor
 *
 */
public class Room implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3578144587651511822L;
	public String roomNumber;
	public Integer totalAvailable;
	public Integer currentAvailable;

	public Room() {
		this.roomNumber = "";
		this.totalAvailable = 0;
		this.currentAvailable = 0;
	}

	public Room(String rn, Integer tA, Integer cA)
	{
		this.roomNumber = rn;
		this.totalAvailable = tA;
		this.currentAvailable = tA;
	}

}
