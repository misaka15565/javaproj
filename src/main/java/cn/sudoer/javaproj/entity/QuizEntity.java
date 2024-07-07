package cn.sudoer.javaproj.entity;

import static cn.sudoer.javaproj.entity.QuizType.*;

import org.slf4j.LoggerFactory;

import static cn.sudoer.javaproj.entity.Operator.*;
import static cn.sudoer.javaproj.entity.QuizFormat.*;

//单个数学题
//鉴于是小学生的，答案和题目都是整数
//不存数据库，所以不用@entity
//加减乘除的题目都是两个数，混合的题目是三个数
public class QuizEntity {
    private QuizType type;
    private Integer num1;
    private Integer num2;
    private Integer num3;
    private Integer answer;
    private Operator operator1;// 三个数的题目才会用到，因为两个数的题目只有一个运算符，可以从type里面推断
    private Operator operator2;
    private QuizFormat format;// 括号状态（也是三个数的题目才会用到

    private final static String[] operatorStrings = { "+", "-", "×", "÷" };// 注意编码问题，÷是除号，不是斜杠

    private Integer calcualte(Integer num1, Integer num2, Operator operator) {
        switch (operator) {
            case Operator_ADD:
                return num1 + num2;
            case Operator_SUB:
                return num1 - num2;
            case Operator_MUL:
                return num1 * num2;
            case Operator_DIV:
                return num1 / num2;
            default:
                LoggerFactory.getLogger(QuizEntity.class).error("Unknown operator");
                return 0;
        }
    }

    private void generateMixedQuiz(Integer numOfDigits) {
        this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
        this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
        this.num3 = (int) (Math.random() * Math.pow(10, numOfDigits));
        this.operator1 = Operator.values()[(int) (Math.random() * 4)];
        this.operator2 = Operator.values()[(int) (Math.random() * 4)];
        this.format = QuizFormat.values()[(int) (Math.random() * 3)];

        // 检查括号必要性，当题目中同时有低级运算符和高级运算符时，加在低级运算符上的括号才是需要的
        int[] operatorPriority = { 0, 0, 1, 1 };
        if (this.format == QuizFormat_BRACKET_1_2) {
            if (operatorPriority[this.operator1.ordinal()] < operatorPriority[this.operator2.ordinal()]) {
                this.format = QuizFormat_NO_BRACKET;
            }
        }
        if (this.format == QuizFormat_BRACKET_2_3) {
            if (operatorPriority[this.operator2.ordinal()] < operatorPriority[this.operator1.ordinal()]) {
                this.format = QuizFormat_NO_BRACKET;
            }
        }
        // 计算答案
        if (this.format == QuizFormat_NO_BRACKET) {
            // 比较两个运算符的优先级
            if (operatorPriority[this.operator1.ordinal()] < operatorPriority[this.operator2.ordinal()]) {
                // 第一个运算符优先级低，先算后两个数
                this.answer = calcualte(this.num2, this.num3, this.operator2);
                // 再算第一个数
                this.answer = calcualte(this.num1, this.answer, this.operator1);
            }else{
                // 第二个运算符优先级低或者运算符优先级相同，先算前两个数
                this.answer = calcualte(this.num1, this.num2, this.operator1);
                // 再算第三个数
                this.answer = calcualte(this.answer, this.num3, this.operator2);
            }
        }else if(this.format==QuizFormat_BRACKET_1_2){
            // 先算第一个数和第二个数
            int temp = calcualte(this.num1, this.num2, this.operator1);
            // 再算第三个数
            this.answer = calcualte(temp, this.num3, this.operator2);
        }else{
            // 先算第二个数和第三个数
            int temp = calcualte(this.num2, this.num3, this.operator2);
            // 再算第一个数
            this.answer = calcualte(this.num1, temp, this.operator1);
        }
    }

    // 题目类型、位数
    // 题目中三个数和答案最多有多少位，至少有一个达到这个位数
    public QuizEntity(QuizType type, Integer numOfDigits) {
        this.type = type;
        if (this.type != QuizType_MIX) {
            this.operator1 = Operator.values()[(int) (Math.random() * 4)];
            this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
            this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
            this.answer = this.calcualte(numOfDigits, numOfDigits, this.operator1);
            if(this.type==QuizType_SUB){
                // 减法的结果不能为负数
                while(this.answer<0){
                    this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
                    this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
                    this.answer = this.calcualte(numOfDigits, numOfDigits, this.operator1);
                }
            }else if(this.type==QuizType_DIV){
                // 除法的结果不能为小数
                while(this.num1%this.num2!=0){
                    this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
                    this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
                    this.answer = this.calcualte(numOfDigits, numOfDigits, this.operator1);
                }
            }
        } else {
            this.generateMixedQuiz(numOfDigits);
        }
    }
}

enum QuizType {
    QuizType_ADD, QuizType_SUB, QuizType_MUL, QuizType_DIV, QuizType_MIX
}

enum Operator {
    Operator_ADD, Operator_SUB, Operator_MUL, Operator_DIV
}

// 无括号、括号在第一第二个数、括号在第二第三个数
enum QuizFormat {
    QuizFormat_NO_BRACKET, QuizFormat_BRACKET_1_2, QuizFormat_BRACKET_2_3
}