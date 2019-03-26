package demo.data;

import org.springframework.data.annotation.Id;

public class Customer {

	@Id
	private String id;

	private String firstName;
	private String lastName;
	private String room;

	public Customer() {
	}

	public Customer(String firstName, String lastName, String room) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.room = room;
	}

	@Override
	public String toString() {
		return String.format("Customer[id=%s, firstName='%s', lastName='%s', room='%s']",
				id, firstName, lastName, room);
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
	
	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getId() {
		return id;
	}

}