package cn.sudoer.javaproj;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.repository.*;
@Controller
public class WebPages {
    @Autowired
    SysUserRepoSitory sysUserRepoSitory;
    @GetMapping("")
    public String index(Map<String, Object> model) {
        SysUser sysuser = new SysUser();
        sysuser.setUserName("sudoer");
        sysuser.setPassword("123123");
        sysuser.setEmail("hhj@sudoer.cn");
        model.put("sysuser", sysuser);
        this.sysUserRepoSitory.saveAndFlush(sysuser);
        UserEntity user = new UserEntity("sudoer", 18);
        model.put("user", user);
        return "index";
    }
}

class UserEntity {
    private String userName;
    private Integer age;

    public UserEntity(String userName, Integer age) {
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}