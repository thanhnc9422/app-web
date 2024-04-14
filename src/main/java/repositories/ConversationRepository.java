package repositories;

import com.example.webApp.models.Conversation;
import com.example.webApp.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
       List<Conversation> findByMembersIn(List<String> members);
}
