package demo.data;

import org.springframework.data.annotation.Id;

public class Customer {

	@Id
	private String id;

	private String firstName;
	private String lastName;
	private String roomNumber;

	public Customer() {
	}

	public Customer(String firstName, String lastName, String roomNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.roomNumber = roomNumber;
	}

	@Override
	public String toString() {
		return String.format("Customer[id=%s, firstName='%s', lastName='%s', roomNumber='%s']",
				id, firstName, lastName, roomNumber);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getId() {
		return id;
	}

}