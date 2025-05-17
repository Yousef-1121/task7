package Scalable.Tasks.Task7.model;

public class FullUser {

    private UserCore userCore;
    private UserExtra userExtra;
    private UserPreferences userPreferences;

    public FullUser() {}

    public FullUser(UserCore userCore, UserExtra userExtra, UserPreferences userPreferences) {
        this.userCore = userCore;
        this.userExtra = userExtra;
        this.userPreferences = userPreferences;
    }

    public UserCore getUserCore() {
        return userCore;
    }

    public void setUserCore(UserCore userCore) {
        this.userCore = userCore;
    }

    public UserExtra getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(UserExtra userExtra) {
        this.userExtra = userExtra;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }
}
