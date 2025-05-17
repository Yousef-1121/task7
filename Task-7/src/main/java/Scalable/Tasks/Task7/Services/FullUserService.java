package Scalable.Tasks.Task7.Services;

import Scalable.Tasks.Task7.Repositories.*;
import Scalable.Tasks.Task7.model.FullUser;
import Scalable.Tasks.Task7.model.UserCore;
import Scalable.Tasks.Task7.model.UserExtra;
import Scalable.Tasks.Task7.model.UserPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FullUserService {

    private final UserCoreRepository1 userCoreRepository1;
    private final UserCoreRepository2 userCoreRepository2;
    private final UserCoreRepository3 userCoreRepository3;
    private final UserExtraRepository userExtraRepository;
    private final UserPreferencesRepository userPreferencesRepository;

    @Autowired
    public FullUserService(UserCoreRepository1 userCoreRepository1,
                           UserCoreRepository2 userCoreRepository2,
                           UserCoreRepository3 userCoreRepository3,
                           UserExtraRepository userExtraRepository,
                           UserPreferencesRepository userPreferencesRepository) {
        this.userCoreRepository1 = userCoreRepository1;
        this.userCoreRepository2 = userCoreRepository2;
        this.userCoreRepository3 = userCoreRepository3;
        this.userExtraRepository = userExtraRepository;
        this.userPreferencesRepository = userPreferencesRepository;
    }

    // ================================
    // Fetch Full User Info by ID
    // ================================
    public FullUser getFullUserInformation(Integer userID) {
        UserCore userCore;

        if (userID < 1000) {
            userCore = userCoreRepository1.findById(userID).orElse(null);
        } else if (userID < 2000) {
            userCore = userCoreRepository2.findById(userID).orElse(null);
        } else if (userID < 3000) {
            userCore = userCoreRepository3.findById(userID).orElse(null);
        } else {
            return null;
        }

        if (userCore == null) return null;

        UserExtra userExtra = userExtraRepository.findByUserID(userID);
        UserPreferences userPreferences = userPreferencesRepository.findByUserID(userID);

        return new FullUser(userCore, userExtra, userPreferences);
    }

    // ================================
    // Create New Full User
    // ================================
    public void createFullUser(String username,
                               String password,
                               String firstName,
                               String lastName,
                               String email,
                               String phone,
                               String address,
                               String theme,
                               boolean notificationsEnabled) {

        // Determine new user ID
        Long last1 = userCoreRepository1.findLastUserId();
        Long last2 = userCoreRepository2.findLastUserId();
        Long last3 = userCoreRepository3.findLastUserId();

        int lastId = Math.max(
                Math.max(last1 != null ? last1.intValue() : 0,
                        last2 != null ? last2.intValue() : 0),
                last3 != null ? last3.intValue() : 0
        );

        int newUserId = lastId + 1;

        UserCore userCore = new UserCore();
        userCore.setUsername(username);
        userCore.setPassword(password);

        // Save to appropriate shard
        if (newUserId < 1000) {
            userCore = userCoreRepository1.save(userCore);
        } else if (newUserId < 2000) {
            userCore = userCoreRepository2.save(userCore);
        } else {
            userCore = userCoreRepository3.save(userCore);
        }

        // Save vertical Mongo data
        UserExtra userExtra = new UserExtra();
        userExtra.setUserID(userCore.getId());
        userExtra.setFirstName(firstName);
        userExtra.setLastName(lastName);
        userExtra.setEmail(email);
        userExtra.setPhone(phone);
        userExtra.setAddress(address);
        userExtraRepository.save(userExtra);

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserID(userCore.getId());
        userPreferences.setTheme(theme);
        userPreferences.setNotificationsEnabled(notificationsEnabled);
        userPreferencesRepository.save(userPreferences);

        System.out.println("✅ User created successfully with ID: " + userCore.getId());
    }
}
