package cn.sudoer.javaproj;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPages {
    @GetMapping("")
    public String index(Map<String, Object> model) {
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