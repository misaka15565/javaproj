package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
@Controller
@RequestMapping("/")
@Component
public class indexPages {
    private SysUserService sysUserService;
    public indexPages(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
        LoggerFactory.getLogger(getClass()).trace("indexPages init");
    }
    @GetMapping("/")
    public String root(HttpServletRequest request){
        LoggerFactory.getLogger(getClass()).trace(request.getRemoteAddr()+" root page");
        return "redirect:/index";
    }
    @GetMapping("/index")
    public String index(HttpServletRequest request,Map<String, Object> model) {
        LoggerFactory.getLogger(getClass()).trace("index page");
        SysUser sysuser = new SysUser();
        sysuser.setUsername("admin");
        sysuser.setPasswordHashByClearText("admin");
        sysuser.setEmail("admin@sudoer.cn");
        sysUserService.createUser(sysuser);
        model.put("sysuser", sysuser);
        //如果访问的是根目录，重定向到index.html
        
        return "index";
    }
    @GetMapping("login")
    public String login(HttpServletRequest request){
        LoggerFactory.getLogger(this.getClass()).trace(request.getRemoteAddr()+" login page");
        return "login";
    }
    @GetMapping("loginSuccess")
    public String loginSuccess(HttpServletRequest request, Map<String, Object> model) {
        return "loginSuccess";
    }
    @GetMapping("register")
    public String register(HttpServletRequest request){
        LoggerFactory.getLogger(this.getClass()).trace(request.getRemoteAddr()+" register page");
        return "register";
    }
}
