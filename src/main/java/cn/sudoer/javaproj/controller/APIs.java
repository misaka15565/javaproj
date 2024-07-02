package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import cn.sudoer.javaproj.repository.SysUserRepostory;
import cn.sudoer.javaproj.service.SysUserService;

@Controller
@RequestMapping("/api")
public class APIs {
    private final SysUserRepostory sysUserRepoSitory;
    public APIs(SysUserRepostory sysUserRepoSitory) {
        this.sysUserRepoSitory = sysUserRepoSitory;
        LoggerFactory.getLogger(getClass()).trace("APIs init");
    }
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Logger logger=LoggerFactory.getLogger(this.getClass());
        logger.info("username:"+username);
        if(username==null || password==null){
            return "redirect:/login";//返回登录界面
        }
        SysUserService sysUserService = new SysUserService(sysUserRepoSitory);
        boolean loginSuccess = sysUserService.login(username, password);
        if (loginSuccess) {
            return "redirect:/loginSuccess";
        } else {
            return "redirect:/login";
        }
    }
}
