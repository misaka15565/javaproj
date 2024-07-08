package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sudoer.javaproj.entity.*;
import cn.sudoer.javaproj.service.*;
import jakarta.servlet.http.*;

@Controller
@RequestMapping("/app")
public class AppControllers {
    private final UserCookieService userCookieService;
    private final SysUserService sysUserService;
    private final UserSettingsService userSettingsService;
    public AppControllers(UserCookieService ucs, SysUserService sus, UserSettingsService uss) {
        userCookieService = ucs;
        sysUserService = sus;
        userSettingsService = uss;
    }

    @GetMapping("/cookieCheck")
    public String cookieCheck(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        Cookie[] allcookie = request.getCookies();
        // 找到auth的cookie
        String authCookie = null;
        if (allcookie != null) {
            for (Cookie cookie : allcookie) {
                if (cookie.getName().equals("auth")) {
                    authCookie = cookie.getValue();
                    break;
                }
            }
        }
        String str1 = "null";
        String str2 = "null";
        String str3 = "null";
        String str4 = "null";
        if (authCookie != null) {
            str1 = userCookieService.checkCookie(authCookie) ? "cookie有效" : "cookie无效";
            str2 = userCookieService.getUsernameFromCookie(authCookie);
            str3 = userCookieService.getExpireTimeString(authCookie);
            SysUser user=sysUserService.getUserByUsername(str2);
            str4=user.getEmail();
        }

        model.put("str1", str1);
        model.put("str2", str2);
        model.put("str3", str3);
        model.put("str4", str4);
        return "app/cookieCheck";
    }
    @GetMapping("/menu")
    public String menu(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        //获取auth cookie
        Cookie[] allcookie = request.getCookies();
        String authCookie = null;
        if (allcookie != null) {
            for (Cookie cookie : allcookie) {
                if (cookie.getName().equals("auth")) {
                    authCookie = cookie.getValue();
                    break;
                }
            }
        }else{
            LoggerFactory.getLogger(getClass()).error("no cookie found");//能访问到这的不应该没auth cookie
            return "redirect:/";
        }
        //从数据库获取用户设置信息
        UserSettings userSettings = userSettingsService.getUserSettingsByUsername(userCookieService.getUsernameFromCookie(authCookie));
        model.put("userSettings", userSettings);
        return "app/menu";
    }
    @GetMapping("/prepare")//准备页面，用户在此调整设置或者选择开始答题或者选择开始比赛
    public String prepare(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        //获取auth cookie
        Cookie[] allcookie = request.getCookies();
        String authCookie = null;
        if (allcookie != null) {
            for (Cookie cookie : allcookie) {
                if (cookie.getName().equals("auth")) {
                    authCookie = cookie.getValue();
                    break;
                }
            }
        }else{
            LoggerFactory.getLogger(getClass()).error("no cookie found");//能访问到这的不应该没auth cookie
            return "redirect:/";
        }
        //从数据库获取用户设置信息
        UserSettings userSettings = userSettingsService.getUserSettingsByUsername(userCookieService.getUsernameFromCookie(authCookie));
        model.put("userSettings", userSettings);
        return "app/prepare";
    }
}
