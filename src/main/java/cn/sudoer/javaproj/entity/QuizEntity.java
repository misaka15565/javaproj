package cn.sudoer.javaproj.entity;

import static cn.sudoer.javaproj.entity.QuizType.*;

import org.slf4j.LoggerFactory;

//单个数学题
//鉴于是小学生的，答案和题目都是整数
//不存数据库，所以不用@entity
//加减乘除的题目都是两个数，混合的题目是三个数
public class QuizEntity {
    private QuizType type;
    private Integer num1;
    private Integer num2;
    private Integer answer;
    private Operator operator;

    public QuizType getType() {
        return type;
    }

    public void setType(QuizType type) {
        this.type = type;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator1) {
        this.operator = operator1;
    }

    public String Quiz_toString() {
        return num1 + operatorStrings[operator.ordinal()] + num2 + "=";
    }

    public String Quiz_toStringWithAnswer() {
        return num1 + operatorStrings[operator.ordinal()] + num2 + "=" + answer;
    }

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

    // 题目类型、位数
    // 题目中三个数和答案最多有多少位，至少有一个达到这个位数
    public QuizEntity(QuizType type, Integer numOfDigits) {
        this.type = type;
        if (this.type == QuizType_MIX) {
            // 随机生成一个类型
            this.type = QuizType.values()[(int) (Math.random() * 4)];
        }
        this.operator = Operator.values()[this.type.ordinal()];
        this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
        this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
        if (!(this.num2 == 0 && this.type == QuizType_MUL)) {
            this.answer = this.calcualte(this.num1, this.num2, this.operator);
        } else {
            this.num2 = 0;// 强制进入else if 的while
        }
        if (this.type == QuizType_SUB) {
            // 减法的结果不能为负数
            while (this.answer < 0) {
                this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
                this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
                this.answer = this.calcualte(this.num1, this.num2, this.operator);
            }
        } else if (this.type == QuizType_DIV) {
            // 除法的结果不能为小数，除数不能为0
            while (this.num2 == 0 || this.num1 % this.num2 != 0) {
                this.num1 = (int) (Math.random() * Math.pow(10, numOfDigits));
                this.num2 = (int) (Math.random() * Math.pow(10, numOfDigits));
                if (this.num2 == 0) {
                    continue;
                }
                this.answer = this.calcualte(this.num1, this.num2, this.operator);
            }
        }
    }

    public Boolean equals(QuizEntity quiz) {
        return this.type == quiz.type && this.num1 == quiz.num1 && this.num2 == quiz.num2 &&
                this.answer == quiz.answer && this.operator == quiz.operator;
    }

}

enum Operator {
    Operator_ADD, Operator_SUB, Operator_MUL, Operator_DIV
}
