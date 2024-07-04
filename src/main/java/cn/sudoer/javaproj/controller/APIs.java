package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.service.SysUserService;
import cn.sudoer.javaproj.service.UserCookieService;
@Controller
@RequestMapping("/api")
public class APIs {
    private final SysUserService sysUserService;
    private final UserCookieService userCookieService;
    public APIs(SysUserService sysUserService,UserCookieService userCookieService) {
        this.sysUserService = sysUserService;
        this.userCookieService=userCookieService;
        LoggerFactory.getLogger(getClass()).trace("APIs init");
    }
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Logger logger=LoggerFactory.getLogger(this.getClass());
        logger.trace("username:"+username);
        if(username==null || password==null){
            return "redirect:/login";//返回登录界面
        }
        boolean loginSuccess = sysUserService.login(username, password);
        if (loginSuccess) {
            String authCookieString=userCookieService.getCookie(username);
            Cookie cookie = new Cookie("auth",authCookieString);
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
        Logger logger=LoggerFactory.getLogger(this.getClass());
        logger.trace("username:"+username+" register");
        if(username==null || password==null || email==null){
            return "redirect:/register";//返回注册界面
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
        //获取所有参数并打印
        //先获取参数列表
        Map<String, String[]> map=request.getParameterMap();
        StringBuilder sb=new StringBuilder();
        for(String key:map.keySet()){
            String[] values=map.get(key);
            for(String value:values){
                sb.append(key).append(":").append(value).append("\n");
            }
        }
        Logger logger=LoggerFactory.getLogger(this.getClass());
        logger.trace(sb.toString());
        return "redirect:/app/menu";
    }
}
