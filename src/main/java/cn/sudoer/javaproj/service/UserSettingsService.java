package cn.sudoer.javaproj.service;

import org.springframework.stereotype.Service;

import cn.sudoer.javaproj.entity.UserSettings;
import cn.sudoer.javaproj.repository.*;;
@Service
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;
    public UserSettingsService(UserSettingsRepository userSettingsRepostory){
        this.userSettingsRepository=userSettingsRepostory;
    }
    public UserSettings getUserSettingsByUsername(String username){
        return userSettingsRepository.findByUsername(username);
    }

    public void saveUserSettings(UserSettings userSettings){
        userSettingsRepository.saveAndFlush(userSettings);
    }

    public void setDefaultSettings(String username){
        UserSettings userSettings = new UserSettings();
        userSettings.setUsername(username);
        userSettings.setQuizType("Mixed");
        userSettings.setNumOfDigits(1);
        userSettings.setNumOfQuestions(5);
        userSettings.setTimeLimitEnable(true);
        userSettings.setTimeLimit(1);
        this.saveUserSettings(userSettings);
    }
}
