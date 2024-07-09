package cn.sudoer.javaproj.controller;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import cn.sudoer.javaproj.entity.QuizEntity;
import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.entity.UserSettings;
import cn.sudoer.javaproj.service.*;

@Controller
@RequestMapping("/api")
public class APIs {
    private final SysUserService sysUserService;
    private final UserCookieService userCookieService;
    private final UserSettingsService userSettingsService;
    private final QuizService quizService;

    public APIs(SysUserService sysUserService, UserCookieService userCookieService,
            UserSettingsService userSettingsService, QuizService quizService) {
        this.sysUserService = sysUserService;
        this.userCookieService = userCookieService;
        this.userSettingsService = userSettingsService;
        this.quizService = quizService;
        LoggerFactory.getLogger(getClass()).trace("APIs init");
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.trace("username:" + username);
        if (username == null || password == null) {
            return "redirect:/login";// 返回登录界面
        }
        boolean loginSuccess = sysUserService.login(username, password);
        if (loginSuccess) {
            String authCookieString = userCookieService.getCookie(username);
            Cookie cookie = new Cookie("auth", authCookieString);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/loginSuccess";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.trace("username:" + username + " register");
        if (username == null || password == null || email == null) {
            return "redirect:/register";// 返回注册界面
        }
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPasswordHashByClearText(password);
        sysUser.setEmail(email);
        boolean registerSuccess = sysUserService.createUser(sysUser);
        if (registerSuccess) {
            return "redirect:/login";
        } else {
            return "redirect:/register";
        }
    }

    @GetMapping("/changeUserSettings")
    public String changeUserSettings(HttpServletRequest request, HttpServletResponse response) {
        // 获取所有参数并打印
        // 先获取参数列表
        Map<String, String[]> map = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            for (String value : values) {
                sb.append(key).append(":").append(value).append("\n");
            }
        }
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.trace(sb.toString());
        // 然后获取当前的设置
        Cookie[] allcookie = request.getCookies();
        String authCookie = null;
        if (allcookie != null) {
            for (Cookie cookie : allcookie) {
                if (cookie.getName().equals("auth")) {
                    authCookie = cookie.getValue();
                    break;
                }
            }
        } else {
            LoggerFactory.getLogger(getClass()).error("no cookie found");// 能访问到这的不应该没auth cookie
            return "redirect:/";
        }
        // 从数据库获取用户设置信息
        UserSettings userSettings = userSettingsService
                .getUserSettingsByUsername(userCookieService.getUsernameFromCookie(authCookie));
        // 修改设置
        userSettings.setTimeLimitEnable(false);// 如果复选框不勾选，默认是不发请求的，所以这里先设置为false
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            for (String value : values) {
                if (key.equals("operationType")) {
                    userSettings.setQuizType(value);
                } else if (key.equals("digit")) {
                    try {
                        userSettings.setNumOfDigits(Integer.parseInt(value));
                    } catch (Exception e) {
                        logger.warn(e.toString());
                    }
                } else if (key.equals("questionNum")) {
                    try {
                        userSettings.setNumOfQuestions(Integer.parseInt(value));
                    } catch (Exception e) {
                        logger.warn(e.toString());
                    }
                } else if (key.equals("timeLimitValue")) {
                    try {
                        userSettings.setTimeLimit(Integer.parseInt(value));
                    } catch (Exception e) {
                        logger.warn(e.toString());
                    }
                } else if (key.equals("timeLimit")) {
                    userSettings.setTimeLimitEnable(value.equals("on"));
                }
            }
        }
        // 检查时间限制，如果时间限制为0则关闭时间限制
        if (userSettings.getTimeLimit() == 0) {
            userSettings.setTimeLimitEnable(false);
        }
        if (userSettings.getTimeLimitEnable() == false) {
            userSettings.setTimeLimit(0);
        }
        userSettingsService.saveUserSettings(userSettings);
        return "redirect:/app/menu";
    }

    @PostMapping("/quizsubmit")
    public String quizsubmit(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        // 获取所有参数并打印
        // 先获取参数列表
        Map<String, String[]> map = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            for (String value : values) {
                sb.append(key).append(":").append(value).append("\n");
            }
        }
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.trace(sb.toString());
        //获取用户名
        String username = userCookieService.getUsernameFromCookies(request.getCookies());
        //获取题目列表
        ArrayList<QuizEntity> quizList = quizService.getUserQuizs(username);
        //计算分数，满分100，四舍六入五凑双
        int correctcount=0;
        for(int index=0;index<quizList.size();index++){
            String answer = request.getParameter("answer"+index);
            LoggerFactory.getLogger(getClass()).trace("answer "+answer+" "+quizList.get(index).getAnswer());
            if(answer==null){
                continue;
            }
            if(quizList.get(index).getAnswer().toString().equals(answer)){
                correctcount++;
            }
        }
        Integer score = (int)(100.0*correctcount/quizList.size()+0.5);
        LoggerFactory.getLogger(getClass()).trace("score:"+score);
        model.put("score",score);
        //保存分数
        quizService.setUserScore(username, score, 0);
        return "redirect:/app/judge";
    }
}
