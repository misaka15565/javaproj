package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.repository.SysUserRepostory;
import cn.sudoer.javaproj.service.SysUserService;
@Controller
@RequestMapping("/api")
public class APIs {
    @Autowired
    SysUserRepostory sysUserRepoSitory;
    @GetMapping("/login")
    public String login(HttpServletRequest request, Map<String, Object> model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username==null || password==null){
            return "login";//返回登录界面
        }
        SysUserService sysUserService = new SysUserService(sysUserRepoSitory);

        return "loginSuccess";
    }
}
