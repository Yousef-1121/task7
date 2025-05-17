package Scalable.Tasks.Task7.Repositories;

import Scalable.Tasks.Task7.model.UserCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCoreRepository3 extends JpaRepository<UserCore, Integer> {

    // Handles users with ID from 2000 to 2999
    @Query("SELECT MAX(id) FROM UserCore")
    Long findLastUserId();
}
