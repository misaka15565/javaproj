package cn.sudoer.javaproj.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_score")
//记录用户得分记录，有用户名、得分、用时、成绩提交时间、题目数量、题目类型、题目数字位数字段
public class UserScoreHistoryEntity {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private Integer score;

    @Column
    private Integer timeUsed;//单位：秒

    @Column
    private Integer timeLimit;

    @Column
    private String submitTime;//yyyymmdd_hhmmss

    @Column
    private Integer numOfQuestions;

    @Column
    private String quizType;

    @Column
    private Integer numOfDigits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(Integer numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public Integer getNumOfDigits() {
        return numOfDigits;
    }

    public void setNumOfDigits(Integer numOfDigits) {
        this.numOfDigits = numOfDigits;
    }
    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
