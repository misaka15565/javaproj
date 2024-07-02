package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sudoer.javaproj.service.UserCookieService;
import jakarta.servlet.http.*;

@Controller
@RequestMapping("/app")
public class AppControllers {
    private final UserCookieService userCookieService;

    public AppControllers(UserCookieService ucs) {
        userCookieService = ucs;
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
        if (authCookie != null) {
            str1 = userCookieService.checkCookie(authCookie) ? "cookie有效" : "cookie无效";
            str2 = userCookieService.getUsernameFromCookie(authCookie);
            str3 = userCookieService.getExpireTimeString(authCookie);
        }

        model.put("str1", str1);
        model.put("str2", str2);
        model.put("str3", str3);
        return "app/cookieCheck";
    }
}
