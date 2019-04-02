package demo.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository<Customer, String> {

	@Query("{'$or':[ {firstName: { $regex: ?0, $options: 'i'} } ]}")
	public List<Customer> findByFirstNameLike(String firstName);
	
	@Query("{'$or':[ {lastName: { $regex: ?0, $options: 'i'} } ]}")
	public List<Customer> findByLastNameLike(String lastName);
	
	@Query("{'$or':[ {roomNumber: { $regex: ?0, $options: 'i'} } ]}")
	public List<Customer> findByRoomNumberLike(String roomNumber);
	
	//@Query("{'$or':[ {firstName: { $regex: ?0, $options: 'i'} } , {lastName: { $regex: ?0, $options: 'i'} } , {roomNumber: { $regex: ?0, $options: 'i'} } ]}")
    //public List<Customer> findByFirstNameOrLastNameOrRoomNumberLike(String searchText);

}