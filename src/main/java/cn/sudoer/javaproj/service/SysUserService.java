package cn.sudoer.javaproj.service;

import org.springframework.stereotype.Service;

import cn.sudoer.javaproj.entity.SysUser;
import cn.sudoer.javaproj.repository.SysUserRepostory;

@Service
public class SysUserService {
    private final SysUserRepostory sysUserRepoSitory;

    public SysUserService(SysUserRepostory sysUserRepoSitory) {
        this.sysUserRepoSitory = sysUserRepoSitory;
    }

    public boolean createUser(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        // 查找有无同名用户
        SysUser user = sysUserRepoSitory.findByUsername(username);
        if (user != null) {
            return false;
        } else {
            sysUserRepoSitory.saveAndFlush(sysUser);
            return true;
        }
    }
    public boolean createUser(SysUser user){
        SysUser user1 = sysUserRepoSitory.findByUsername(user.getUsername());
        if(user1!=null){
            return false;
        }else{
            sysUserRepoSitory.saveAndFlush(user);
            return true;
        }
    }
    public boolean login(String username, String password) {
        SysUser user = sysUserRepoSitory.findByUsername(username);
        String hashedPasswordString=SysUser.getHashString(password);
        if (user == null) {
            return false;
        } else {
            return user.getPasswordHash().equals(hashedPasswordString);
        }
    }
}