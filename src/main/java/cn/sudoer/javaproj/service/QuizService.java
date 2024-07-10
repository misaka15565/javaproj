package cn.sudoer.javaproj.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import cn.sudoer.javaproj.entity.QuizEntity;
import cn.sudoer.javaproj.entity.QuizType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//生成数学题的服务
@Service
public class QuizService {
    // 数学题有五种，加减乘除和混合
    // 为比赛生成题目后应该将其暂存，保证每个人拿到的题目一样
    private ArrayList<QuizEntity> competitionQuizList;// 这样写是全服只支持一个比赛的，但是算了
    private Map<String, ArrayList<QuizEntity>> userQuizListMap;// 暂存用户的题目
    private Map<String, Integer> userScoreMap;// 暂存用户的分数（真的只是暂存！只存最近一次的）
    private final UserScoreHistoryService userScoreHistoryService;

    QuizService(UserScoreHistoryService userScoreHistoryService) {
        this.competitionQuizList = new ArrayList<>();
        this.userQuizListMap = new HashMap<String, ArrayList<QuizEntity>>();
        this.userScoreMap = new HashMap<String, Integer>();
        this.userScoreHistoryService = userScoreHistoryService;
    }

    private ArrayList<QuizEntity> generateQuizs(int numOfQuestions, int numOfDigits, QuizType type) {
        ArrayList<QuizEntity> quizList = new ArrayList<>();
        for (int i = 0; i < numOfQuestions; i++) {
            QuizEntity quiz = new QuizEntity(type, numOfDigits);
            // 检查是否有相同的quiz
            boolean flag = true;
            //效率？不管了
            for (QuizEntity q : quizList) {
                if (q.equals(quiz)) {
                    LoggerFactory.getLogger(QuizService.class).trace("生成了重复的题目");
                    i--;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                quizList.add(quiz);
            }
        }
        return quizList;
    }

    public void generateUserQuizs(String username, int numOfQuestions, int numOfDigits, QuizType type) {
        ArrayList<QuizEntity> quizList = generateQuizs(numOfQuestions, numOfDigits, type);
        this.userQuizListMap.put(username, quizList);
    }

    public ArrayList<QuizEntity> getUserQuizs(String username) {
        ArrayList<QuizEntity> res = this.userQuizListMap.get(username);
        if (res == null)
            LoggerFactory.getLogger(QuizService.class).error("用户" + username + "没有题目");
        return res;
    }

    // 为比赛生成题目（应当只有比赛服务调用）
    public void generateCompetitionQuizs(int numOfQuestions, int numOfDigits, QuizType type) {
        this.competitionQuizList = generateQuizs(numOfQuestions, numOfDigits, type);
    }

    // 读取比赛题目
    public ArrayList<QuizEntity> getCompetitionQuizs() {
        return this.competitionQuizList;
    }

    public void setUserScore(String username, Integer score, Integer timeused) {
        this.userScoreMap.put(username, score);
        // 顺便存入数据库
        userScoreHistoryService.addHistory(username, score, timeused);
    }

    public Integer getUserScore(String username) {
        return this.userScoreMap.get(username);
    }
}
