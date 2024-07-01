package cn.sudoer.javaproj.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.repository.SysUserRepostory;
import cn.sudoer.javaproj.service.SysUserService;
@Controller
@RequestMapping("/")
public class indexPages {
    @Autowired
    SysUserRepostory sysUserRepoSitory;
    @GetMapping("")
    public String index(Map<String, Object> model) {
        SysUserService sysUserService = new SysUserService(sysUserRepoSitory);
        SysUser sysuser = new SysUser();
        sysuser.setUsername("sudoer");
        sysuser.setPassword("123123");
        sysuser.setEmail("hhj@sudoer.cn");
        sysUserService.createUser(sysuser);
        model.put("sysuser", sysuser);
        return "index";
    }
    @GetMapping("login")
    public String login(){
        return "login";
    }
}
