package cn.sudoer.javaproj.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
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
}
