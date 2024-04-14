package repositories;

import com.example.webApp.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findOneById(String id);
    public User findOneByUserName(String userName);
    public List<User> findAll();
    public User findOneByUserNameAndPassword(String userName, String Password);
}
