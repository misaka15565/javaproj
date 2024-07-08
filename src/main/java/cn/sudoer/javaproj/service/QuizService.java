package cn.sudoer.javaproj.service;

import org.springframework.stereotype.Service;
import cn.sudoer.javaproj.entity.QuizEntity;
import cn.sudoer.javaproj.entity.QuizType;

import java.util.ArrayList;

//生成数学题的服务
@Service
public class QuizService {
    // 数学题有五种，加减乘除和混合
    // 为比赛生成题目后应该将其暂存，保证每个人拿到的题目一样
    private ArrayList<QuizEntity> competitionQuizList;

    QuizService() {
        this.competitionQuizList = new ArrayList<>();
    }

    ArrayList<QuizEntity> generateQuizs(int numOfQuestions, int numOfDigits, QuizType type) {
        ArrayList<QuizEntity> quizList = new ArrayList<>();
        for (int i = 0; i < numOfQuestions; i++) {
            QuizEntity quiz = new QuizEntity(type, numOfDigits);
            quizList.add(quiz);
        }
        return quizList;
    }

    // 为比赛生成题目（应当只有发起比赛的人调用）
    public void generateCompetitionQuizs(int numOfQuestions, int numOfDigits, QuizType type) {
        this.competitionQuizList = generateQuizs(numOfQuestions, numOfDigits, type);
    }

    // 读取比赛题目
    public ArrayList<QuizEntity> getCompetitionQuizs() {
        return this.competitionQuizList;
    }
}
