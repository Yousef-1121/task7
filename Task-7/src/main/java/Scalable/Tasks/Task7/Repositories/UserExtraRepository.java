package Scalable.Tasks.Task7.Repositories;

import Scalable.Tasks.Task7.model.UserExtra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtraRepository extends MongoRepository<UserExtra, String> {

    UserExtra findByUserID(Integer userID);
}
