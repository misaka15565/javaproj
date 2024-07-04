package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.service.SysUserService;
import cn.sudoer.javaproj.service.UserCookieService;
import jakarta.servlet.http.*;

@Controller
@RequestMapping("/app")
public class AppControllers {
    private final UserCookieService userCookieService;
    private final SysUserService sysUserService;
    public AppControllers(UserCookieService ucs, SysUserService sus) {
        userCookieService = ucs;
        sysUserService = sus;
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
        return "app/menu";
    }
}
