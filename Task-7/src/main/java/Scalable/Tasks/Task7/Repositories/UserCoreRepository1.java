package Scalable.Tasks.Task7.Repositories;

import Scalable.Tasks.Task7.model.UserCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCoreRepository1 extends JpaRepository<UserCore, Integer> {

    // Handles users with ID from 1 to 999
    @Query("SELECT MAX(id) FROM UserCore")
    Long findLastUserId();
}
