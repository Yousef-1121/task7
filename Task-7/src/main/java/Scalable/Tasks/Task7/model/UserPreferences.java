package Scalable.Tasks.Task7.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_preferences")
public class UserPreferences {

    @Id
    private String id;

    private Integer userID;
    private String theme;
    private boolean notificationsEnabled;

    public UserPreferences() {}

    public UserPreferences(Integer userID, String theme, boolean notificationsEnabled) {
        this.userID = userID;
        this.theme = theme;
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
