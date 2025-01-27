package cn.sudoer.javaproj.controller;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.sudoer.javaproj.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/restapi")
public class RESTAPIs {
    private final CompetitionService competitionService;
    private final UserCookieService userCookieService;

    public RESTAPIs(CompetitionService competitionService, UserCookieService userCookieService) {
        this.competitionService = competitionService;
        this.userCookieService = userCookieService;
    }

    @GetMapping("/Competition/getList")
    public ArrayList<String> getCompetitionList() {
        return competitionService.getCompetitionUserList();
    }

    @GetMapping("/Competition/addUser")
    public void addUser(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username != null) {
            competitionService.addUser(username);
        }
    }

    @GetMapping("/Competition/deleteUser")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username != null) {
            competitionService.deleteUser(username);
        }
    }

    @GetMapping("/Competition/createCompetition")
    public Boolean createCompetition(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username != null) {
            return competitionService.createCompetition(username);
        }
        return false;
    }

    @GetMapping("/Competition/startCompetition")
    public Boolean startCompetition(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username != null && username.equals(competitionService.getCompetitionOwner())) {
            competitionService.startCompetition();
            return true;
        }
        return false;
    }

    @GetMapping("/Competition/isCompetitionRunning")
    public Boolean isCompetitionRunning(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        // 如果用户参加了比赛
        if (competitionService.getCompetitionUserList().contains(username)) {
            return competitionService.getIsCompetitionRunning();
        } else {
            return false;
        }
    }

    @GetMapping("/Competition/getCompetitonEndTime")
    public Date getCompetitionEndTime(HttpServletRequest request, HttpServletResponse response) {
        return competitionService.getCompetitionEndTime();
    }

    @PostMapping("/Competition/submitAnswer")
    public Boolean submitAnswer(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username == null) {
            return false;
        }
        // 获取问题个数
        int numOfQuestions = competitionService.getCompetitionQuizList().size();
        // 从参数列表获取答案
        ArrayList<Integer> answers = new ArrayList<>();
        for (int i = 0; i < numOfQuestions; i++) {
            String answer = request.getParameter("answer" + i);
            if (answer == null) {
                return false;
            }
            try {
                answers.add(Integer.parseInt(answer));
            } catch (Exception e) {
                LoggerFactory.getLogger(getClass()).trace(username + " 第" + i + "答案格式错误");
                // 将答案置为-1
                answers.add(-1);
            }
        }
        // 提交答案
        return competitionService.submitAnswers(username, answers) != -1;
    }

    @GetMapping("/Competition/killCompetition")
    public Boolean killCompetition(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        Date competitionEndTime = competitionService.getCompetitionEndTime();
        Date now = new Date();
        Boolean flag = false;
        if (username != null && username.equals(competitionService.getCompetitionOwner())) {
            competitionService.killCompetition();
            flag = true;
        } else if (now.after(new Date(competitionEndTime.getTime() + competitionService.gracePeriod + 60 * 1000))) {
            // 如果上一个比赛的宽限期已经结束超过1分钟
            competitionService.killCompetition();
            flag = true;
        }
        return flag;
    }

    @GetMapping("/Competition/getTimeLeft") // 获取剩余时间（秒
    public Integer getTimeLeft(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username != null && competitionService.getCompetitionUserList().contains(username)) {
            Date now = new Date();
            Date competitionEndTime = competitionService.getCompetitionEndTime();
            if (now.after(competitionEndTime)) {
                return 0;
            } else {
                LoggerFactory.getLogger(getClass())
                        .trace("剩余时间" + (competitionEndTime.getTime() - now.getTime()) / 1000);
                return (int) (competitionEndTime.getTime() - now.getTime()) / 1000;
            }
        }
        return 0;
    }

    @GetMapping("/Competition/checkUser") // 检查用户是否可以进入比赛
    public Boolean checkUser(HttpServletRequest request, HttpServletResponse response) {
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        if (username == null) {
            return false;
        }
        return competitionService.checkUser(username);
    }

    @GetMapping("/Competition/getRankList")
    ArrayList<String> getRankList(HttpServletRequest request, HttpServletResponse response) {
        return competitionService.getRankList();
    }

    @GetMapping("/Competition/getCompetitionOwner")
    String getCompetitionOwner(HttpServletRequest request, HttpServletResponse response) {
        String res = competitionService.getCompetitionOwner();
        if (res == null) {
            res = "";
        }
        return res;
    }
}
