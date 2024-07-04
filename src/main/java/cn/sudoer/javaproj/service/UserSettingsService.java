package cn.sudoer.javaproj.service;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import cn.sudoer.javaproj.entity.UserSettings;
import cn.sudoer.javaproj.repository.*;;
@Service
public class UserSettingsService {
    private final UserSettingsRepostory userSettingsRepostory;
    public UserSettingsService(UserSettingsRepostory userSettingsRepostory){
        this.userSettingsRepostory=userSettingsRepostory;
    }
    public UserSettings getUserSettingsByUsername(String username){
        return userSettingsRepostory.findByUsername(username);
    }

    public void saveUserSettings(UserSettings userSettings){
        userSettingsRepostory.saveAndFlush(userSettings);
    }

    public void setDefaultSettings(String username){
        UserSettings userSettings = new UserSettings();
        userSettings.setUsername(username);
        userSettings.setOperationType("Mixed");
        userSettings.setNumOfDigits(3);
        userSettings.setNumOfQuestions(10);
        userSettings.setTimeLimitEnable(false);
        userSettings.setTimeLimit(0);
        this.saveUserSettings(userSettings);
    }
}
