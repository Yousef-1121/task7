package Scalable.Tasks.Task7.Repositories;

import Scalable.Tasks.Task7.model.UserPreferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferencesRepository extends MongoRepository<UserPreferences, String> {

    UserPreferences findByUserID(Integer userID);
}
