package cn.sudoer.javaproj;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.sudoer.javaproj.service.SysUserService;
import cn.sudoer.javaproj.entity.*;

@Component
public class AppStartupRunner implements CommandLineRunner {
    private final SysUserService sysUserService;

    public AppStartupRunner(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void run(String... args) throws Exception {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("sudoer");
        sysUser.setPassword("123456");
        sysUser.setEmail("sudoer@sudoer.cn");
        sysUserService.createUser(sysUser);
    }
}