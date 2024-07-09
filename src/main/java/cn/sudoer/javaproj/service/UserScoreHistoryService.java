package cn.sudoer.javaproj.service;

import org.springframework.stereotype.Service;

import cn.sudoer.javaproj.entity.UserScoreHistoryEntity;
import cn.sudoer.javaproj.entity.UserSettings;
import cn.sudoer.javaproj.repository.*;

@Service
public class UserScoreHistoryService {
    private final UserScoreHistoryRepository userScoreHistoryRepository;
    private final UserSettingsService userSettingsService;

    public UserScoreHistoryService(UserScoreHistoryRepository userScoreHistoryRepostory,
            UserSettingsService userSettingsService) {
        this.userScoreHistoryRepository = userScoreHistoryRepostory;
        this.userSettingsService = userSettingsService;
    }

    public UserScoreHistoryEntity[] getUserScoreHistoryByUsername(String username) {
        return userScoreHistoryRepository.findByUsername(username);
    }

    public void saveUserScoreHistory(UserScoreHistoryEntity userScoreHistory) {
        userScoreHistoryRepository.saveAndFlush(userScoreHistory);
    }

    public void addHistory(String username, Integer score, Integer timeused) {
        UserScoreHistoryEntity userScoreHistory = new UserScoreHistoryEntity();
        userScoreHistory.setUsername(username);
        userScoreHistory.setScore(score);
        userScoreHistory.setTimeUsed(timeused);
        // 从userseting获取题目类型等信息
        UserSettings userSettings = userSettingsService.getUserSettingsByUsername(username);
        userScoreHistory.setQuizType(userSettings.getQuizType());
        userScoreHistory.setNumOfDigits(userSettings.getNumOfDigits());
        userScoreHistory.setNumOfQuestions(userSettings.getNumOfQuestions());
        userScoreHistory.setTimeUsed(timeused);
        userScoreHistory.setSubmitTime(
                new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()));// 时间格式yyyymmdd_hhmmss，提交当前时间
        this.saveUserScoreHistory(userScoreHistory);
    }
}
