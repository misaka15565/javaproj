package cn.sudoer.javaproj.service;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.sudoer.javaproj.entity.*;

@Service
public class CompetitionService {
    // 此service用于记录比赛参加的所有用户，其中发起比赛的用户有权开始比赛
    // 以比赛发起用户的设置为比赛设置
    // 全服只支持一个比赛
    private Set<String> competitionUserSet;
    private String competitionOwner;
    private final QuizService quizService;
    private final SysUserService sysUserService;
    private final UserSettingsService userSettingsService;
    private Map<String, Integer> userScoreMap;
    private Boolean isCompetitionRunning;// 比赛是否正在进行
    // 记录比赛开始的时间
    private Date competitionStartTime;
    // 记录比赛结束的时间
    private Date competitionEndTime;
    // 从比赛开始到比赛结束后宽限期结束前，都可以通过api提交答案
    // 宽限期（毫秒）
    public final Integer gracePeriod = 10 * 1000;

    public CompetitionService(QuizService qs, SysUserService sus, UserSettingsService uss) {
        userScoreMap = new HashMap<>();
        competitionUserSet = new HashSet<>();
        quizService = qs;
        sysUserService = sus;
        userSettingsService = uss;
        isCompetitionRunning = false;
    }

    public void generateCompetitionQuizs() {
        SysUser user = sysUserService.getUserByUsername(competitionOwner);
        if (user == null) {
            LoggerFactory.getLogger(getClass()).warn("用户" + competitionOwner + "不存在");
            return;
        }
        UserSettings userSettings = userSettingsService.getUserSettingsByUsername(competitionOwner);
        if (userSettings == null) {
            LoggerFactory.getLogger(getClass()).warn("用户" + competitionOwner + "没有设置");
            return;
        }
        quizService.generateCompetitionQuizs(userSettings.getNumOfQuestions(), userSettings.getNumOfDigits(),
                userSettings.getQuizTypeEnum());
    }

    public ArrayList<QuizEntity> getCompetitionQuizList() {
        return quizService.getCompetitionQuizs();
    }

    public Boolean addUser(String username) {
        if (competitionOwner == null) {
            LoggerFactory.getLogger(getClass()).warn("没有比赛");
            return false;
        }
        competitionUserSet.add(username);
        return true;
    }

    public Boolean deleteUser(String username) {
        // 检查username是否在比赛中
        if (!competitionUserSet.contains(username)) {
            LoggerFactory.getLogger(getClass()).warn("用户" + username + "不在比赛中");
            return false;
        }
        competitionUserSet.remove(username);
        return true;
    }

    public ArrayList<String> getCompetitionUserList() {
        return new ArrayList<>(competitionUserSet);// java真方便啊。。。
    }

    public Boolean createCompetition(String username) {
        if (competitionOwner != null) {
            LoggerFactory.getLogger(getClass()).warn("已经有比赛了");
            return false;
        }
        competitionOwner = username;
        return true;
    }

    public String getCompetitionOwner() {
        return competitionOwner;
    }

    // 杀死比赛（什么也不留
    public void killCompetition() {
        competitionOwner = null;
        competitionUserSet.clear();
        userScoreMap.clear();
        isCompetitionRunning = false;
    }

    public void startCompetition() {
        LoggerFactory.getLogger(getClass()).trace("比赛开始");
        UserSettings userSettings = userSettingsService.getUserSettingsByUsername(competitionOwner);
        if (userSettings == null) {
            LoggerFactory.getLogger(getClass()).error("用户" + competitionOwner + "没有设置");
            return;
        }
        userScoreMap.clear();
        competitionStartTime = new Date();
        competitionEndTime = new Date(competitionStartTime.getTime() + userSettings.getTimeLimit() * 60 * 1000);
        generateCompetitionQuizs();
        isCompetitionRunning = true;
    }

    public Boolean getIsCompetitionRunning() {
        if (isCompetitionRunning) {
            Date now = new Date();
            if (now.after(competitionEndTime)) {
                isCompetitionRunning = false;
            }
        }
        return isCompetitionRunning;
    }

    public Date getCompetitionStartTime() {
        return competitionStartTime;
    }

    public Date getCompetitionEndTime() {
        return competitionEndTime;
    }

    // 检查是否仍然在宽限期内
    public Boolean getIsInGracePeriod() {
        Date now = new Date();
        return now.before(new Date(competitionEndTime.getTime() + gracePeriod));
    }

    public Integer submitAnswers(String username, ArrayList<Integer> answer) {
        LoggerFactory.getLogger(getClass()).trace(username + "提交答案");
        if (!getIsInGracePeriod()) {
            LoggerFactory.getLogger(getClass()).warn("宽限期已过");
            return -1;
        }
        if (!isCompetitionRunning) {
            LoggerFactory.getLogger(getClass()).warn("比赛未开始");
            return -1;
        }
        if (!competitionUserSet.contains(username)) {
            LoggerFactory.getLogger(getClass()).warn("用户" + username + "不在比赛中");
            return -1;
        }
        if (answer.size() != quizService.getCompetitionQuizs().size()) {
            LoggerFactory.getLogger(getClass()).warn("答案数量不对");
            return -1;
        }
        Integer score = 0;
        for (int i = 0; i < answer.size(); i++) {
            if (answer.get(i).equals(quizService.getCompetitionQuizs().get(i).getAnswer())) {
                score++;
            }
        }
        score = (int) ((score / (double) answer.size()) * 100 + 0.5);
        userScoreMap.put(username, score);
        LoggerFactory.getLogger(getClass()).trace(username + "得分：" + score);
        return score;
    }

    public ArrayList<String> getRankList() {
        // 按照分数对用户进行排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(userScoreMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        ArrayList<String> rankList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : list) {
            rankList.add(entry.getKey() + " : " + entry.getValue());
        }
        return rankList;
    }

    public Integer getUserScore(String username) {
        return userScoreMap.get(username);
    }
}
