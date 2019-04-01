package demo.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository<Customer, String> {

	public Customer findByFirstNameLike(String firstName);

	public List<Customer> findByLastNameLike(String lastName);
	
	public List<Customer> findByRoomNumberLike(String roomNumber);
	
	@Query("{'$or':[ { 'firstName' : ?0 } , { 'lastName' : ?0 } , { 'roomNumber' : ?0 } ]}")
    public List<Customer> findByFirstNameOrLastNameOrRoomNumberLike(String searchText);

}