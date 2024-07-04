package cn.sudoer.javaproj.entity;

import jakarta.persistence.*;

//用户设置数据实体
@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;


    @Column 
    private String operationType;//计算类型（加减乘除混合）

    @Column
    private Integer numOfDigits;//数字位数

    @Column
    private Integer numOfQuestions;//题目数量

    @Column
    private Boolean timeLimitEnable;//是否开启时间限制

    @Column
    private Integer timeLimit;//时间限制（单位：分钟）

    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getNumOfDigits() {
        return numOfDigits;
    }

    public void setNumOfDigits(Integer numOfDigits) {
        this.numOfDigits = numOfDigits;
    }

    public Integer getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(Integer numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public Boolean getTimeLimitEnable() {
        return timeLimitEnable;
    }

    public void setTimeLimitEnable(Boolean timeLimitEnable) {
        this.timeLimitEnable = timeLimitEnable;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
